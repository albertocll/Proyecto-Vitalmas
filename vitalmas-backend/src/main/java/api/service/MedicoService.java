package api.service;

import api.repository.MedicoRepository;

import java.util.List;
import java.util.UUID;

import api.model.Medico;

public class MedicoService {
    private final MedicoRepository repo;

    public MedicoService(MedicoRepository repo) {
        this.repo = repo;
    }

    public List<Medico> listar() {
        return repo.listar();
    }

    public Medico alta(String nombre, String especialidad) {
        List<Medico> lista = repo.listar();
        Medico m = new Medico(UUID.randomUUID().toString(), nombre, especialidad);
        lista.add(m);
        repo.guardarTodos(lista);
        return m;
    }

    /** Necesario para que SeedConfig pueda persistir cambios (usuario/password). */
    public void guardarTodos(List<Medico> lista) {
        repo.guardarTodos(lista);
    }
}
