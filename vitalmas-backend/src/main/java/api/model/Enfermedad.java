package api.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
public class Enfermedad {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private String nombre;

    private String nivelRiesgo;

    private boolean operar;

    private String tratamiento;

    @ManyToMany(fetch = FetchType.EAGER) // evitas LazyInitialization en la respuesta
    @JoinTable(
        name = "enfermedad_sintoma",
        joinColumns = @JoinColumn(name = "enfermedad_id"),
        inverseJoinColumns = @JoinColumn(name = "sintoma_id"),
        uniqueConstraints = @UniqueConstraint(columnNames = { "enfermedad_id", "sintoma_id" })
    )
    @JsonManagedReference
    private Set<Sintoma> sintomas = new HashSet<>();

    public Enfermedad() {}

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getNivelRiesgo() { return nivelRiesgo; }
    public void setNivelRiesgo(String nivelRiesgo) { this.nivelRiesgo = nivelRiesgo; }
    public boolean isOperar() { return operar; }
    public void setOperar(boolean operar) { this.operar = operar; }
    public String getTratamiento() { return tratamiento; }
    public void setTratamiento(String tratamiento) { this.tratamiento = tratamiento; }
    public Set<Sintoma> getSintomas() { return sintomas; }
    public void setSintomas(Set<Sintoma> sintomas) { this.sintomas = sintomas; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Enfermedad that)) return false;
        return id != null && id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
