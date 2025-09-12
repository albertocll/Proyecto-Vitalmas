package api.repository;

import api.model.Sintoma;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SintomaRepository extends JpaRepository<Sintoma, UUID> {

    Optional<Sintoma> findByNombreIgnoreCase(String nombre);

    boolean existsByNombreIgnoreCase(String nombre);

    // Búsqueda parcial (lo pide tu SintomaService)
    List<Sintoma> findByNombreContainingIgnoreCase(String nombre);

    // Relación con enfermedad vía tabla de join manual
    @Query(value = "SELECT s.* FROM SINTOMA s " +
                   "JOIN ENFERMEDAD_SINTOMA es ON es.SINTOMA_ID = s.ID " +
                   "WHERE es.ENFERMEDAD_ID = :enfermedadId",
           nativeQuery = true)
    List<Sintoma> findByEnfermedadId(UUID enfermedadId);
}
