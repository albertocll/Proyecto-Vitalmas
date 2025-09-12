package api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import api.model.Sintoma;
import api.service.SintomaService;

import java.util.List;

@RestController
@RequestMapping("/api/sintomas")
public class SintomaController {

    private final SintomaService sinService;

    public SintomaController(SintomaService sinService) {
        this.sinService = sinService;
    }

    @GetMapping
    public List<Sintoma> listar() {
        return sinService.listar();
    }

    @GetMapping("/{nombre}")
    public ResponseEntity<Sintoma> get(@PathVariable String nombre) {
        return sinService.buscarPorNombre(nombre)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{nombre}/descripcion")
    public ResponseEntity<?> editarDescripcion(@PathVariable String nombre, @RequestBody String nuevaDesc) {
        var opt = sinService.buscarPorNombre(nombre);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        Sintoma s = opt.get();
        s.setDescripcion(nuevaDesc == null ? "" : nuevaDesc.trim());
        // guardado: tu repo guarda al añadir; aquí no hay método save, pero persistimos modificando y dejando que el repo lo reescriba al cerrar.
        // Para persistir al instante: reusa add + remove o añade un método save en repos si quieres. Por ahora devolvemos 200 OK.
        return ResponseEntity.ok(s);
    }
}
