package api.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(
    name = "USUARIO",
    uniqueConstraints = @UniqueConstraint(columnNames = "usuario")
)
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, unique = true, length = 80)
    private String usuario;

    @Column(nullable = false, length = 120)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Rol rol;

    @Column(nullable = false)
    private boolean enabled = true;  // nuevo campo para activar/desactivar usuario

    public enum Rol { MEDICO, ADMIN }

    protected Usuario() {} // JPA

    public Usuario(String nombre, String usuario, String password, Rol rol) {
        this.nombre = nombre;
        this.usuario = usuario;
        this.password = password;
        this.rol = rol;
        this.enabled = true;
    }

    // Getters
    public UUID getId() { return id; }
    public String getNombre() { return nombre; }
    public String getUsuario() { return usuario; }
    public String getPassword() { return password; }
    public Rol getRol() { return rol; }
    public boolean isEnabled() { return enabled; }

    // Setters
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    public void setPassword(String password) { this.password = password; }
    public void setRol(Rol rol) { this.rol = rol; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
}
