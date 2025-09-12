package api.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "usuario",
       uniqueConstraints = @UniqueConstraint(name = "uk_usuario", columnNames = "usuario"))
public class Usuario {

    public enum Rol { ADMIN, MEDICO }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 80, unique = true)
    private String usuario;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 120)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol;

    @Column(nullable = false)
    private boolean enabled = true;

    // ====== Constructores ======
    protected Usuario() {
        // JPA necesita constructor vacío
    }

    public Usuario(String usuario, String nombre, String password, Rol rol) {
        this.usuario = usuario;
        this.nombre = nombre;
        this.password = password;
        this.rol = rol;
        this.enabled = true;
    }

    // ====== Getters & Setters ======
    public UUID getId() { return id; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
}
