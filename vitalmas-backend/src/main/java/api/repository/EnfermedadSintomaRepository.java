package api.repository;

import api.model.Enfermedad;
import api.model.EnfermedadSintoma;
import api.model.Sintoma;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EnfermedadSintomaRepository
        extends JpaRepository<EnfermedadSintoma, EnfermedadSintoma.EnfermedadSintomaId> {

    List<EnfermedadSintoma> findByEnfermedad(Enfermedad enfermedad);

    Optional<EnfermedadSintoma> findByEnfermedadAndSintoma(Enfermedad enfermedad, Sintoma sintoma);
}
