package api.repository;

import api.model.Enfermedad;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface EnfermedadRepository extends CrudRepository<Enfermedad, UUID> {

    Optional<Enfermedad> findByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCase(String nombre);

    @EntityGraph(attributePaths = "sintomas")
    Optional<Enfermedad> findWithSintomasByNombreIgnoreCase(String nombre);
}
