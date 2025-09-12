package api.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Repository
public class EnfermedadSintomaLinkDao {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void ensureJoinTable() {
        em.createNativeQuery(
            "CREATE TABLE IF NOT EXISTS ENFERMEDAD_SINTOMA (" +
            "  ENFERMEDAD_ID UUID NOT NULL," +
            "  SINTOMA_ID UUID NOT NULL," +
            "  PRIMARY KEY (ENFERMEDAD_ID, SINTOMA_ID)" +
            ")"
        ).executeUpdate();
    }

    public boolean exists(UUID enfermedadId, UUID sintomaId) {
        Optional<?> res = em.createNativeQuery(
            "SELECT 1 FROM ENFERMEDAD_SINTOMA " +
            "WHERE ENFERMEDAD_ID = ?1 AND SINTOMA_ID = ?2 FETCH FIRST 1 ROWS ONLY"
        )
        .setParameter(1, enfermedadId)
        .setParameter(2, sintomaId)
        .getResultStream()
        .findFirst();
        return res.isPresent();
    }

    @Transactional
    public boolean insertIfAbsent(UUID enfermedadId, UUID sintomaId) {
        ensureJoinTable();
        try {
            int rows = em.createNativeQuery(
                "INSERT INTO ENFERMEDAD_SINTOMA (ENFERMEDAD_ID, SINTOMA_ID) VALUES (?1, ?2)"
            )
            .setParameter(1, enfermedadId)
            .setParameter(2, sintomaId)
            .executeUpdate();
            return rows > 0;
        } catch (PersistenceException ex) {
            if (exists(enfermedadId, sintomaId)) return false;
            throw ex;
        }
    }
}
