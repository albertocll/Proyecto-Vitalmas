package api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "security.jwt")
public class JwtProperties {

    /**
     * Clave secreta para firmar JWT (mínimo 32 caracteres para HS256)
     */
    private String secret;

    /**
     * Duración en segundos del token
     */
    private long expirationSeconds = 3600;

    // Getters y Setters
    public String getSecret() {
        return secret;
    }
    public void setSecret(String secret) {
        this.secret = secret;
    }

    public long getExpirationSeconds() {
        return expirationSeconds;
    }
    public void setExpirationSeconds(long expirationSeconds) {
        this.expirationSeconds = expirationSeconds;
    }
}
