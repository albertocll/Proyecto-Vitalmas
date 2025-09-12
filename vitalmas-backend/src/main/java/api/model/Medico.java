package api.model;

import java.util.Objects;

public class Medico {
    private String id;          // UUID
    private String nombre;
    private String especialidad;

    // NUEVO: credenciales básicas (opcionales)
    private String usuario;
    private String password;

    public Medico() {}

    public Medico(String id, String nombre, String especialidad) {
        this.id = id;
        this.nombre = nombre;
        this.especialidad = especialidad;
    }

    // Getters/setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    @Override public String toString() {
        return "Medico{id='%s', nombre='%s', especialidad='%s'}".formatted(id, nombre, especialidad);
    }
    @Override public boolean equals(Object o){ if(this==o) return true; if(!(o instanceof Medico m)) return false; return Objects.equals(id,m.id); }
    @Override public int hashCode(){ return Objects.hash(id); }
}
