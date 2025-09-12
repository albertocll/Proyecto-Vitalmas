package api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import api.model.Paciente;
import api.service.PacienteService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController {

    private final PacienteService service;

    public PacienteController(PacienteService service) {
        this.service = service;
    }

    @GetMapping
    public List<Paciente> listar() {
        return service.listar();
    }

    @PostMapping
    public ResponseEntity<Paciente> crear(@RequestBody Map<String, String> body) {
        String nombre = body.getOrDefault("nombre", "").trim();
        String dni = body.getOrDefault("dni", "").trim();
        if (nombre.isEmpty()) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(service.alta(nombre, dni));
    }
}
