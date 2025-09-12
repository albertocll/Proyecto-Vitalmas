package api.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

/**
 * Entidad intermedia para la relación Enfermedad <-> Sintoma
 */
@Entity
@Table(
        name = "enfermedad_sintoma",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"enfermedad_id", "sintoma_id"})
        }
)
@IdClass(EnfermedadSintoma.EnfermedadSintomaId.class)
public class EnfermedadSintoma implements Serializable {

    @Id
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "enfermedad_id", nullable = false)
    private Enfermedad enfermedad;

    @Id
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "sintoma_id", nullable = false)
    private Sintoma sintoma;

    protected EnfermedadSintoma() {
        // JPA necesita constructor vacío
    }

    public EnfermedadSintoma(Enfermedad enfermedad, Sintoma sintoma) {
        this.enfermedad = enfermedad;
        this.sintoma = sintoma;
    }

    public Enfermedad getEnfermedad() {
        return enfermedad;
    }

    public Sintoma getSintoma() {
        return sintoma;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EnfermedadSintoma that)) return false;
        return Objects.equals(enfermedad, that.enfermedad) &&
               Objects.equals(sintoma, that.sintoma);
    }

    @Override
    public int hashCode() {
        return Objects.hash(enfermedad, sintoma);
    }

    /**
     * Clave compuesta (enfermedad_id + sintoma_id)
     */
    public static class EnfermedadSintomaId implements Serializable {
        private UUID enfermedad;
        private UUID sintoma;

        public EnfermedadSintomaId() {}
        public EnfermedadSintomaId(UUID enfermedad, UUID sintoma) {
            this.enfermedad = enfermedad;
            this.sintoma = sintoma;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof EnfermedadSintomaId that)) return false;
            return Objects.equals(enfermedad, that.enfermedad) &&
                   Objects.equals(sintoma, that.sintoma);
        }

        @Override
        public int hashCode() {
            return Objects.hash(enfermedad, sintoma);
        }
    }
}
