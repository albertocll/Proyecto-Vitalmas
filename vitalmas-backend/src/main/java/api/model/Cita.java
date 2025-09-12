package api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDateTime;
import java.util.Objects;

public class Cita {
    private String id;            // UUID
    private String medicoId;
    private String pacienteId;
    private String motivo;
    private String fechaHoraIso;  // ISO-8601
    private boolean realizada;

    public Cita() {}
    public Cita(String id, String medicoId, String pacienteId, String motivo, String fechaHoraIso, boolean realizada) {
        this.id = id; this.medicoId = medicoId; this.pacienteId = pacienteId;
        this.motivo = motivo; this.fechaHoraIso = fechaHoraIso; this.realizada = realizada;
    }

    public String getId(){ return id; }
    public void setId(String id){ this.id = id; }
    public String getMedicoId(){ return medicoId; }
    public void setMedicoId(String medicoId){ this.medicoId = medicoId; }
    public String getPacienteId(){ return pacienteId; }
    public void setPacienteId(String pacienteId){ this.pacienteId = pacienteId; }
    public String getMotivo(){ return motivo; }
    public void setMotivo(String motivo){ this.motivo = motivo; }
    public String getFechaHoraIso(){ return fechaHoraIso; }
    public void setFechaHoraIso(String fechaHoraIso){ this.fechaHoraIso = fechaHoraIso; }
    public boolean isRealizada(){ return realizada; }
    public void setRealizada(boolean realizada){ this.realizada = realizada; }

    @JsonIgnore
    public LocalDateTime getFechaHora(){
        return LocalDateTime.parse(fechaHoraIso);
    }

    @Override public String toString(){
        return "Cita{id='%s', medicoId='%s', pacienteId='%s', fecha='%s', realizada=%s}"
                .formatted(id, medicoId, pacienteId, fechaHoraIso, realizada);
    }
    @Override public boolean equals(Object o){
        if(this==o) return true;
        if(!(o instanceof Cita c)) return false;
        return Objects.equals(id,c.id);
    }
    @Override public int hashCode(){ return Objects.hash(id); }
}
