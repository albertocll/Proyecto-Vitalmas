package api.auth;

import api.security.JwtUtil;
import api.service.UsuarioService;  // << este es el importante
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

//@RestController
//@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioService usuarioService;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    public AuthController(UsuarioService usuarioService, PasswordEncoder encoder, JwtUtil jwtUtil) {
        this.usuarioService = usuarioService;
        this.encoder = encoder;
        this.jwtUtil = jwtUtil;
    }

    public record LoginRequest(String usuario, String password) {}

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest body) {
        try {
            UserDetails ud = usuarioService.loadUserByUsername(body.usuario());

            if (!encoder.matches(body.password(), ud.getPassword())) {
                return ResponseEntity.status(401).body(Map.of("error", "Credenciales inválidas"));
            }

            String token = jwtUtil.generateToken(ud.getUsername(), ud.getAuthorities());
            return ResponseEntity.ok(Map.of("token", token));

        } catch (Exception e) {
            return ResponseEntity.status(401).body(Map.of("error", "Credenciales inválidas"));
        }
    }
}
