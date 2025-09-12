package api.service;

import api.model.Enfermedad;
import api.model.Sintoma;
import api.repository.EnfermedadRepository;
import api.repository.EnfermedadSintomaLinkDao;
import api.repository.SintomaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class EnfermedadSintomaService {

    private final EnfermedadRepository enfermedadRepository;
    private final SintomaRepository sintomaRepository;
    private final EnfermedadSintomaLinkDao linkDao;

    public EnfermedadSintomaService(EnfermedadRepository enfermedadRepository,
                                    SintomaRepository sintomaRepository,
                                    EnfermedadSintomaLinkDao linkDao) {
        this.enfermedadRepository = enfermedadRepository;
        this.sintomaRepository = sintomaRepository;
        this.linkDao = linkDao;
    }

    /**
     * Vincula un síntoma a una enfermedad.
     * @return true si se creó un nuevo vínculo, false si ya existía (idempotente).
     */
    @Transactional
    public boolean vincularSintoma(UUID enfermedadId, UUID sintomaId) {
        Enfermedad enf = enfermedadRepository.findById(enfermedadId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe la enfermedad"));
        Sintoma sin = sintomaRepository.findById(sintomaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe el síntoma"));

        if (linkDao.exists(enf.getId(), sin.getId())) {
            return false; // ya existía
        }

        return linkDao.insertIfAbsent(enf.getId(), sin.getId());
    }

    /**
     * Lista todos los síntomas asociados a una enfermedad.
     */
    @Transactional(readOnly = true)
    public List<Sintoma> listarSintomasPorEnfermedad(UUID enfermedadId) {
        Enfermedad enf = enfermedadRepository.findById(enfermedadId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe la enfermedad"));
        return enf.getSintomas().stream().toList();
    }
}
