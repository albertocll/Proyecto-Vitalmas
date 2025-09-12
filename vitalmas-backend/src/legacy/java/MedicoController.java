package api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import api.model.Medico;
import api.service.MedicoService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/medicos")
public class MedicoController {
    private final MedicoService service;
    public MedicoController(MedicoService service){ this.service = service; }

    @GetMapping
    public List<Medico> listar(){ return service.listar(); }

    @PostMapping
    public ResponseEntity<Medico> crear(@RequestBody Map<String,String> body){
        String nombre = body.getOrDefault("nombre", "").trim();
        String esp = body.getOrDefault("especialidad", "").trim();
        if (nombre.isEmpty()) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(service.alta(nombre, esp));
    }
}
