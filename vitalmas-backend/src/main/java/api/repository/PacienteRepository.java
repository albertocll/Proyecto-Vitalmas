package api.repository;

import com.fasterxml.jackson.core.type.TypeReference;

import api.model.Paciente;
import persistence.JsonStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PacienteRepository {
    private static final String KEY = "pacientes.json";
    private final JsonStorage storage;

    public PacienteRepository(JsonStorage storage) { this.storage = storage; }

    public List<Paciente> listar() {
        return storage.loadOrDefault(KEY, new TypeReference<List<Paciente>>() {}, new ArrayList<>());
    }

    public void guardarTodos(List<Paciente> lista) {
        storage.save(KEY, lista);
    }

    public Optional<Paciente> buscarPorId(String id) {
        return listar().stream().filter(p -> id.equals(p.getId())).findFirst();
    }
}
