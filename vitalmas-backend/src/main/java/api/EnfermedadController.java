package api;

import api.model.Enfermedad;
import api.model.Sintoma;
import api.service.EnfermedadService;
import api.service.EnfermedadSintomaService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;

@RestController
@RequestMapping(value = "/api/enfermedades", produces = MediaType.APPLICATION_JSON_VALUE)
public class EnfermedadController {

    private final EnfermedadService enfService;
    private final EnfermedadSintomaService linkService;
    private final ObjectMapper objectMapper;

    public EnfermedadController(EnfermedadService enfService,
                                EnfermedadSintomaService linkService,
                                ObjectMapper objectMapper) {
        this.enfService = enfService;
        this.linkService = linkService;
        this.objectMapper = objectMapper;
    }

    // Listar todas
    @GetMapping
    public ResponseEntity<List<Enfermedad>> listar() {
        return ResponseEntity.ok(enfService.listar());
    }

    // Detalle por ID
    @GetMapping("/{id}")
    public ResponseEntity<Enfermedad> detalle(@PathVariable UUID id) {
        return enfService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Crear ENFERMEDAD: robusto (JSON o form-urlencoded)
    @PostMapping(consumes = MediaType.ALL_VALUE)
    public ResponseEntity<Enfermedad> crear(@RequestBody byte[] rawBodyBytes,
                                            @RequestHeader(value = "Content-Type", required = false) String contentType) {
        String raw = new String(rawBodyBytes, StandardCharsets.UTF_8).trim();
        if (raw.isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cuerpo vacío");

        Enfermedad payload = tryParseJson(raw);
        if (payload == null) payload = tryParseForm(raw);

        if (payload == null || payload.getNombre() == null || payload.getNombre().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "JSON inválido o faltan campos obligatorios");
        }

        Enfermedad creada = enfService.crear(payload);
        return ResponseEntity.created(URI.create("/api/enfermedades/" + creada.getId())).body(creada);
    }

    // Listar síntomas de una enfermedad
    @GetMapping("/{id}/sintomas")
    public ResponseEntity<List<Sintoma>> listarSintomas(@PathVariable UUID id) {
        return ResponseEntity.ok(linkService.listarSintomasPorEnfermedad(id));
    }

    // Vincular síntoma a enfermedad
    @PostMapping("/{enfermedadId}/sintomas/{sintomaId}")
    public ResponseEntity<?> vincular(@PathVariable UUID enfermedadId, @PathVariable UUID sintomaId) {
        boolean created = linkService.vincularSintoma(enfermedadId, sintomaId);
        if (created) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.noContent().build(); // idempotente
        }
    }

    // ===== Helpers de parseo =====
    private Enfermedad tryParseJson(String raw) {
        try {
            JsonNode n = objectMapper.readTree(raw);
            Enfermedad e = new Enfermedad();
            if (n.has("nombre")) e.setNombre(txt(n.get("nombre")));
            if (n.has("nivelRiesgo")) e.setNivelRiesgo(txt(n.get("nivelRiesgo")));
            if (n.has("tratamiento")) e.setTratamiento(txt(n.get("tratamiento")));
            if (n.has("operar")) e.setOperar(bool(n.get("operar")));
            return e;
        } catch (IOException ex) {
            return null;
        }
    }

    private Enfermedad tryParseForm(String raw) {
        var params = UriComponentsBuilder.fromUriString("http://x/?" + raw).build().getQueryParams();
        if (params.isEmpty()) return null;
        Enfermedad e = new Enfermedad();
        e.setNombre(params.getFirst("nombre"));
        e.setNivelRiesgo(params.getFirst("nivelRiesgo"));
        e.setTratamiento(params.getFirst("tratamiento"));
        e.setOperar(parseBoolParam(params.getFirst("operar")));
        return e;
    }

    private static String txt(JsonNode n) {
        return (n == null || n.isNull()) ? null : n.asText();
    }

    private static boolean bool(JsonNode n) {
        return n != null && !n.isNull() && n.asBoolean(false);
    }

    private static boolean parseBoolParam(String v) {
        if (v == null) return false;
        String s = v.trim().toLowerCase();
        return s.equals("true") || s.equals("1") || s.equals("si") || s.equals("sí")
                || s.equals("yes") || s.equals("y");
    }
}
