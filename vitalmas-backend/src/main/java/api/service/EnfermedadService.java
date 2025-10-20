package api.service;

import api.model.Enfermedad;
import api.model.Sintoma;
import api.repository.EnfermedadRepository;
import api.repository.SintomaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
public class EnfermedadService {

    private final EnfermedadRepository enfermedadRepository;
    private final SintomaRepository sintomaRepository;

    public EnfermedadService(EnfermedadRepository enfermedadRepository,
                             SintomaRepository sintomaRepository) {
        this.enfermedadRepository = enfermedadRepository;
        this.sintomaRepository = sintomaRepository;
    }

    @Transactional(readOnly = true)
    public List<Enfermedad> listar() {
        List<Enfermedad> result = new ArrayList<>();
        enfermedadRepository.findAll().forEach(result::add);
        return result;
    }

    @Transactional(readOnly = true)
    public Optional<Enfermedad> buscarPorNombre(String nombre) {
        return enfermedadRepository.findByNombreIgnoreCase(nombre);
    }

    @Transactional(readOnly = true)
    public Optional<Enfermedad> buscarPorId(UUID id) {
        return enfermedadRepository.findById(id);
    }

    @Transactional
    public Enfermedad crear(Enfermedad enfermedad) {
        boolean existe = enfermedadRepository.existsByNombreIgnoreCase(enfermedad.getNombre());
        if (existe) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La enfermedad ya existe");
        }
        return enfermedadRepository.save(enfermedad);
    }

    @Transactional(readOnly = true)
    public Set<Sintoma> listarSintomas(String nombreEnfermedad) {
        Enfermedad enf = enfermedadRepository.findByNombreIgnoreCase(nombreEnfermedad)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No existe la enfermedad"));
        return enf.getSintomas();
    }

    @Transactional
    public boolean desvincularSintoma(UUID enfermedadId, UUID sintomaId) {
        Enfermedad enf = enfermedadRepository.findById(enfermedadId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Enfermedad no encontrada"));
        Sintoma sin = sintomaRepository.findById(sintomaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Síntoma no encontrado"));

        boolean existed = enf.getSintomas().remove(sin);
        if (existed) {
            enfermedadRepository.save(enf); // persiste el cambio en la tabla puente
        }
        return existed; // true -> 204, false -> 404
    }
}
