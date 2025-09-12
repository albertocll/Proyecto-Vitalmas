package api.model;

import java.util.Objects;

public class Paciente {
    private String id;      // UUID
    private String nombre;
    private String dni;     // opcional

    public Paciente() {}
    public Paciente(String id, String nombre, String dni) {
        this.id = id; this.nombre = nombre; this.dni = dni;
    }

    public String getId(){ return id; }
    public void setId(String id){ this.id = id; }
    public String getNombre(){ return nombre; }
    public void setNombre(String nombre){ this.nombre = nombre; }
    public String getDni(){ return dni; }
    public void setDni(String dni){ this.dni = dni; }

    @Override public String toString(){ return "Paciente{id='%s', nombre='%s', dni='%s'}".formatted(id,nombre,dni); }
    @Override public boolean equals(Object o){ if(this==o) return true; if(!(o instanceof Paciente p)) return false; return Objects.equals(id,p.id); }
    @Override public int hashCode(){ return Objects.hash(id); }
}
