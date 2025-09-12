package api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import api.model.Cita;
import api.service.CitaService;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/citas")
public class CitaController {

    private final CitaService service;

    public CitaController(CitaService service) {
        this.service = service;
    }

    // /api/citas                 -> todas
    // /api/citas?realizada=true  -> solo realizadas
    @GetMapping
    public List<Cita> listar(@RequestParam(value = "realizada", required = false) Boolean realizada) {
        return service.listar(realizada);
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Map<String, String> body) {
        try {
            String medicoId = body.getOrDefault("medicoId", "").trim();
            String pacienteId = body.getOrDefault("pacienteId", "").trim();
            String motivo = body.getOrDefault("motivo", "").trim();
            String fechaIso = body.getOrDefault("fechaHoraIso", "").trim();

            if (medicoId.isEmpty() || pacienteId.isEmpty() || fechaIso.isEmpty()) {
                return ResponseEntity.badRequest().body("Campos requeridos: medicoId, pacienteId, fechaHoraIso");
            }

            LocalDateTime fecha = LocalDateTime.parse(fechaIso);
            Cita c = service.crear(medicoId, pacienteId, motivo, fecha);
            return ResponseEntity.ok(c);

        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("fechaHoraIso debe ser ISO-8601, ej: 2025-09-07T16:00:00");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error creando cita: " + e.getMessage());
        }
    }

    @PutMapping("/{id}/realizar")
    public ResponseEntity<Cita> realizar(@PathVariable("id") String id) {
        Optional<Cita> c = service.marcarRealizada(id);
        return c.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
