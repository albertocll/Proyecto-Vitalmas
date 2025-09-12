package api.service;

import api.repository.PacienteRepository;

import java.util.List;
import java.util.UUID;

import api.model.Paciente;

public class PacienteService {
    private final PacienteRepository repo;

    public PacienteService(PacienteRepository repo) { this.repo = repo; }

    public List<Paciente> listar() { return repo.listar(); }

    public Paciente alta(String nombre, String dni) {
        List<Paciente> lista = repo.listar();
        Paciente p = new Paciente(UUID.randomUUID().toString(), nombre, dni);
        lista.add(p);
        repo.guardarTodos(lista);
        return p;
    }
}
