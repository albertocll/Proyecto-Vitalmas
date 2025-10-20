package api.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "enfermedades")
public class Enfermedad {

    @Id @GeneratedValue
    private UUID id;

    @NotBlank(message = "nombre es obligatorio")
    @Size(min = 2, max = 80, message = "nombre debe tener entre 2 y 80 caracteres")
    @Column(nullable = false, unique = true, length = 80)
    private String nombre;

    @Size(max = 20, message = "nivelRiesgo máximo 20 caracteres")
    private String nivelRiesgo;

    private boolean operar;

    @Size(max = 400, message = "tratamiento máximo 400 caracteres")
    private String tratamiento;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "enfermedad_sintoma",
        joinColumns = @JoinColumn(name = "enfermedad_id"),
        inverseJoinColumns = @JoinColumn(name = "sintoma_id"),
        uniqueConstraints = @UniqueConstraint(columnNames = {"enfermedad_id", "sintoma_id"})
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

    @Override public boolean equals(Object o) { return o instanceof Enfermedad that && id != null && id.equals(that.id); }
    @Override public int hashCode() { return getClass().hashCode(); }
}
