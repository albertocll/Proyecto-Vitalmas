package api.service;

import api.model.Usuario;
import api.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository repo;

    public UsuarioService(UsuarioRepository repo) {
        this.repo = repo;
    }

    // Para registrar usuarios nuevos (si quieres)
    public Usuario crear(Usuario usuario) {
        if (repo.findByUsuario(usuario.getUsuario()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El usuario ya existe");
        }
        return repo.save(usuario);
    }

    public Usuario buscarPorId(UUID id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    }

    public Usuario buscarPorUsuario(String usuario) {
        return repo.findByUsuario(usuario)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    }

    // Spring Security lo usa en el login
    @Override
    public UserDetails loadUserByUsername(String username) {
        Usuario usuario = buscarPorUsuario(username);

        // Mapear el enum Rol a "ROLE_ADMIN" o "ROLE_MEDICO"
        String roleName = "ROLE_" + usuario.getRol().name();
        var authorities = List.of(new SimpleGrantedAuthority(roleName));

        return new User(usuario.getUsuario(), usuario.getPassword(), authorities);
    }
}
