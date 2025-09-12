package api.repository;

import com.fasterxml.jackson.core.type.TypeReference;

import api.model.Medico;
import persistence.JsonStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MedicoRepository {
    private static final String KEY = "medicos.json";
    private final JsonStorage storage;

    public MedicoRepository(JsonStorage storage) { this.storage = storage; }

    public List<Medico> listar() {
        return storage.loadOrDefault(KEY, new TypeReference<List<Medico>>() {}, new ArrayList<>());
    }

    public void guardarTodos(List<Medico> lista) {
        storage.save(KEY, lista);
    }

    public Optional<Medico> buscarPorId(String id) {
        return listar().stream().filter(m -> id.equals(m.getId())).findFirst();
    }
}
