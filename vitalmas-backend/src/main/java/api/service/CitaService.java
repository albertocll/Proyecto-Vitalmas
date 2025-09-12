package api.service;

import api.repository.CitaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import api.model.Cita;

public class CitaService {
    private final CitaRepository repo;

    public CitaService(CitaRepository repo) { this.repo = repo; }

    public List<Cita> listar(Boolean realizada) {
        List<Cita> all = repo.listar();
        if (realizada == null) return all;
        return all.stream().filter(c -> c.isRealizada() == realizada).collect(Collectors.toList());
    }

    public Cita crear(String medicoId, String pacienteId, String motivo, LocalDateTime fechaHora) {
        List<Cita> lista = repo.listar();
        Cita c = new Cita(
                UUID.randomUUID().toString(),
                medicoId,
                pacienteId,
                motivo,
                fechaHora.toString(),
                false
        );
        lista.add(c);
        repo.guardarTodos(lista);
        return c;
    }

    public Optional<Cita> marcarRealizada(String id) {
        List<Cita> lista = repo.listar();
        for (Cita c : lista) {
            if (id.equals(c.getId())) {
                c.setRealizada(true);
                repo.guardarTodos(lista);
                return Optional.of(c);
            }
        }
        return Optional.empty();
    }
}
