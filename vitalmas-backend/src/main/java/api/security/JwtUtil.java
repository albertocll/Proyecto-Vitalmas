package api.security;

import api.config.JwtProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    private final SecretKey key;
    private final long expirationSeconds;

    public JwtUtil(JwtProperties props) {
        this.key = Keys.hmacShaKeyFor(props.getSecret().getBytes(StandardCharsets.UTF_8));
        this.expirationSeconds = props.getExpirationSeconds();
    }

    // Genera un token con roles incluidos en claims
    public String generateToken(String username, Collection<? extends GrantedAuthority> authorities) {
        Instant now = Instant.now();
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", authorities == null ? List.of() :
                authorities.stream().map(GrantedAuthority::getAuthority).toList());

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(expirationSeconds)))
                .addClaims(claims)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getUsername(String token) {
        return parseClaims(token).getSubject();
    }

    // Extrae los roles del token y los convierte a GrantedAuthority
    public List<SimpleGrantedAuthority> getAuthorities(String token) {
        Object rolesObj = parseClaims(token).get("roles");
        if (rolesObj instanceof Collection<?> coll) {
            return coll.stream()
                    .map(Object::toString)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }
        return List.of();
    }

    // Devuelve un Authentication válido con username y roles
    public Authentication getAuthentication(String token) {
        String username = getUsername(token);
        var authorities = getAuthorities(token);

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(username, null, authorities);

        auth.setAuthenticated(true); // ⚡ fuerza explícita

        // log de depuración
        System.out.println("JWT ok → usuario=" + username + " roles=" + authorities);

        return auth;
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
