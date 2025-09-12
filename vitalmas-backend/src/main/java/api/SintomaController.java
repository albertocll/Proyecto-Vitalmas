package api;

import api.model.Sintoma;
import api.repository.SintomaRepository;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/sintomas", produces = MediaType.APPLICATION_JSON_VALUE)
public class SintomaController {

    private final SintomaRepository repo;

    public SintomaController(SintomaRepository repo) {
        this.repo = repo;
    }

    // Listar todos los síntomas
    @GetMapping
    public ResponseEntity<List<Sintoma>> listar() {
        return ResponseEntity.ok(repo.findAll());
    }

    // Detalle por ID
    @GetMapping("/{id}")
    public ResponseEntity<Sintoma> detalle(@PathVariable UUID id) {
        return repo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
