package api.service;

import api.model.Sintoma;
import api.repository.SintomaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class SintomaService {

    private final SintomaRepository repo;

    public SintomaService(SintomaRepository repo) {
        this.repo = repo;
    }

    public List<Sintoma> listar() {
        return StreamSupport.stream(repo.findAll().spliterator(), false).toList();
    }

    public Optional<Sintoma> buscarPorNombre(String nombre) {
        return repo.findByNombreIgnoreCase(nombre);
    }

    public List<Sintoma> buscarPorNombreContiene(String texto) {
        return repo.findByNombreContainingIgnoreCase(texto);
    }

    public Sintoma crear(Sintoma s) {
        repo.findByNombreIgnoreCase(s.getNombre()).ifPresent(x -> {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe el síntoma");
        });
        return repo.save(s);
    }
}
