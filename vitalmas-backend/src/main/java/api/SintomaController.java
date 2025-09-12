package api;

import api.model.Sintoma;
import api.repository.SintomaRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping(value = "/api/sintomas", produces = MediaType.APPLICATION_JSON_VALUE)
public class SintomaController {

    private final SintomaRepository repo;
    private final ObjectMapper objectMapper;

    public SintomaController(SintomaRepository repo, ObjectMapper objectMapper) {
        this.repo = repo;
        this.objectMapper = objectMapper;
    }

    // Listar todos
    @GetMapping
    public ResponseEntity<List<Sintoma>> listar() {
        List<Sintoma> out = StreamSupport.stream(repo.findAll().spliterator(), false).toList();
        return ResponseEntity.ok(out);
    }

    // Crear SÍNTOMA: aceptar cualquier Content-Type y parsear a mano
    // Nota: devolvemos 200 OK porque así lo tenías en tus pruebas.
    @PostMapping(consumes = MediaType.ALL_VALUE)
    public ResponseEntity<Sintoma> crear(@RequestBody byte[] rawBodyBytes) {
        String raw = new String(rawBodyBytes, StandardCharsets.UTF_8).trim();
        if (raw.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cuerpo vacío");
        }

        Sintoma data = tryParseJson(raw);
        if (data == null) data = tryParseForm(raw);

        if (data == null || data.getNombre() == null || data.getNombre().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "JSON inválido o faltan campos obligatorios");
        }

        // Duplicados
        repo.findByNombreIgnoreCase(data.getNombre()).ifPresent(x -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe el síntoma");
        });

        Sintoma creado = repo.save(data);
        // Si prefieres 201: return ResponseEntity.created(URI.create("/api/sintomas/" + creado.getNombre())).body(creado);
        return ResponseEntity.ok(creado);
    }

    // ================== Helpers ==================

    private Sintoma tryParseJson(String raw) {
        try {
            JsonNode node = objectMapper.readTree(raw);
            Sintoma s = new Sintoma();
            if (node.has("nombre")) s.setNombre(asText(node.get("nombre")));
            if (node.has("descripcion")) s.setDescripcion(asText(node.get("descripcion")));
            return s;
        } catch (IOException e) {
            return null;
        }
    }

    private Sintoma tryParseForm(String raw) {
        // nombre=Tos&descripcion=Persistente
        MultiValueMap<String, String> params = parseQueryLike(raw);
        if (params.isEmpty()) return null;

        Sintoma s = new Sintoma();
        s.setNombre(params.getFirst("nombre"));
        s.setDescripcion(params.getFirst("descripcion"));
        return s;
        }

    private MultiValueMap<String, String> parseQueryLike(String raw) {
        try {
            URI fake = URI.create("http://x/?" + raw.replace("\n", "").replace("\r", ""));
            var q = UriComponentsBuilder.fromUri(fake).build().getQueryParams();
            return new LinkedMultiValueMap<>(q);
        } catch (Exception e) {
            return new LinkedMultiValueMap<>();
        }
    }

    private String asText(JsonNode n) { return n == null || n.isNull() ? null : n.asText(); }
}
