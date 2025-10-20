package api;

import api.dto.EnfermedadCreateDTO;
import api.model.Enfermedad;
import api.model.Sintoma;
import api.service.EnfermedadService;
import api.service.EnfermedadSintomaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/enfermedades", produces = MediaType.APPLICATION_JSON_VALUE)
public class EnfermedadController {

    private final EnfermedadService enfService;
    private final EnfermedadSintomaService linkService;

    public EnfermedadController(EnfermedadService enfService,
                                EnfermedadSintomaService linkService) {
        this.enfService = enfService;
        this.linkService = linkService;
    }

    // Listar todas
    @GetMapping
    public ResponseEntity<List<Enfermedad>> listar() {
        return ResponseEntity.ok(enfService.listar());
    }

    // Detalle por ID
    @GetMapping("/{id}")
    public ResponseEntity<Enfermedad> detalle(@PathVariable UUID id) {
        Optional<Enfermedad> opt = enfService.buscarPorId(id);
        return opt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crear (JSON con validación)
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Enfermedad> crear(@Valid @RequestBody EnfermedadCreateDTO dto) {
        Enfermedad e = new Enfermedad();
        e.setNombre(dto.getNombre());
        e.setNivelRiesgo(dto.getNivelRiesgo());
        e.setOperar(Boolean.TRUE.equals(dto.getOperar()));
        e.setTratamiento(dto.getTratamiento());

        Enfermedad creada = enfService.crear(e);
        return ResponseEntity
                .created(URI.create("/api/enfermedades/" + creada.getId()))
                .body(creada);
    }

    // Listar síntomas de una enfermedad
    @GetMapping("/{id}/sintomas")
    public ResponseEntity<List<Sintoma>> listarSintomas(@PathVariable UUID id) {
        return ResponseEntity.ok(linkService.listarSintomasPorEnfermedad(id));
    }

    // Vincular síntoma a enfermedad (idempotente)
    @PostMapping("/{enfermedadId}/sintomas/{sintomaId}")
    public ResponseEntity<Void> vincular(@PathVariable UUID enfermedadId, @PathVariable UUID sintomaId) {
        boolean created = linkService.vincularSintoma(enfermedadId, sintomaId);
        return created ? ResponseEntity.status(HttpStatus.CREATED).build()
                : ResponseEntity.noContent().build();
    }

    // Desvincular síntoma de enfermedad
    @DeleteMapping("/{enfermedadId}/sintomas/{sintomaId}")
    public ResponseEntity<Void> desvincularSintoma(
            @PathVariable UUID enfermedadId,
            @PathVariable UUID sintomaId) {
        boolean removed = enfService.desvincularSintoma(enfermedadId, sintomaId);
        return removed ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
