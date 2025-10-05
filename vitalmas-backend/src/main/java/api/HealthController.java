package api;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HealthController {

    // raíz para probar en el navegador
    @GetMapping(path = "/", produces = MediaType.TEXT_PLAIN_VALUE)
    public String root() {
        return "Vitalmas API OK";
    }

    // health en /api/health y /health
    @GetMapping(path = {"/api/health", "/health", "/api/ping"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> body = new HashMap<>();
        body.put("status", "OK");
        body.put("timestamp", Instant.now().toString());
        return ResponseEntity.ok(body);
    }
}
