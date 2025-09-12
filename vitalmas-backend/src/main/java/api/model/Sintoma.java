package api.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
public class Sintoma {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private String nombre;

    private String descripcion;

    @ManyToMany(mappedBy = "sintomas", fetch = FetchType.LAZY)
    @JsonBackReference
    private Set<Enfermedad> enfermedades = new HashSet<>();

    public Sintoma() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Set<Enfermedad> getEnfermedades() { return enfermedades; }
    public void setEnfermedades(Set<Enfermedad> enfermedades) { this.enfermedades = enfermedades; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sintoma that)) return false;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
