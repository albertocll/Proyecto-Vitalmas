package api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class EnfermedadCreateDTO {

    @NotBlank(message = "nombre es obligatorio")
    @Size(min = 2, max = 120, message = "nombre debe tener entre 2 y 120 caracteres")
    private String nombre;

    @Pattern(regexp = "(?i)^(bajo|moderado|alto)?$", message = "nivelRiesgo debe ser Bajo, Moderado o Alto")
    private String nivelRiesgo;

    private Boolean operar;

    @Size(max = 600, message = "tratamiento no puede superar 600 caracteres")
    private String tratamiento;

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getNivelRiesgo() { return nivelRiesgo; }
    public void setNivelRiesgo(String nivelRiesgo) { this.nivelRiesgo = nivelRiesgo; }
    public Boolean getOperar() { return operar; }
    public void setOperar(Boolean operar) { this.operar = operar; }
    public String getTratamiento() { return tratamiento; }
    public void setTratamiento(String tratamiento) { this.tratamiento = tratamiento; }
}
