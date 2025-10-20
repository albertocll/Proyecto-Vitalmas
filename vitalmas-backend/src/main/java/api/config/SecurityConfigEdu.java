package api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Profile("edu")          // solo se activa con el perfil 'edu'
@Order(0)                // prioridad máxima frente a otras cadenas
public class SecurityConfigEdu {

    @Bean
    SecurityFilterChain eduChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/api/**")     // aplica a todo /api/**
            .cors(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
