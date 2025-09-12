package api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import api.model.Medico;
import api.service.MedicoService;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class LoginBasicController {

    private final MedicoService medicos;

    public LoginBasicController(MedicoService medicos) {
        this.medicos = medicos;
    }

    @PostMapping("/login/basic")
    public ResponseEntity<?> loginBasic(@RequestBody Map<String, String> body) {
        String usuario = body.getOrDefault("usuario", "").trim();
        String password = body.getOrDefault("password", "").trim();
        if (usuario.isEmpty() || password.isEmpty()) {
            return ResponseEntity.badRequest().body("usuario y password requeridos");
        }

        return medicos.listar().stream()
                .filter(m -> m.getUsuario() != null && usuario.equalsIgnoreCase(m.getUsuario()))
                .filter(m -> m.getPassword() != null && password.equals(m.getPassword()))
                .findFirst()
                .<ResponseEntity<?>>map(m -> {
                    // devolvemos datos sin password
                    Medico safe = new Medico(m.getId(), m.getNombre(), m.getEspecialidad());
                    safe.setUsuario(m.getUsuario());
                    Map<String, Object> res = new HashMap<>();
                    res.put("tipo", "MEDICO");
                    res.put("usuario", safe);
                    return ResponseEntity.ok(res);
                })
                .orElseGet(() -> ResponseEntity.status(401).body("Credenciales inválidas"));
    }
}
