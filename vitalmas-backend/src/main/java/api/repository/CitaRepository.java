package api.repository;

import com.fasterxml.jackson.core.type.TypeReference;

import api.model.Cita;
import persistence.JsonStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CitaRepository {
    private static final String KEY = "citas.json";
    private final JsonStorage storage;

    public CitaRepository(JsonStorage storage) { this.storage = storage; }

    public List<Cita> listar() {
        return storage.loadOrDefault(KEY, new TypeReference<List<Cita>>() {}, new ArrayList<>());
    }

    public void guardarTodos(List<Cita> lista) {
        storage.save(KEY, lista);
    }

    public Optional<Cita> buscarPorId(String id) {
        return listar().stream().filter(c -> id.equals(c.getId())).findFirst();
    }
}
