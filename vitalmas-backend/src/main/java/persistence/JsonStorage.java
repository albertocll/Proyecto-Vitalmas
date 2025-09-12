package persistence;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.*;
import java.util.Objects;

public class JsonStorage {
    private final Path base;
    private final ObjectMapper mapper = new ObjectMapper();

    public JsonStorage(Path base) {
        this.base = base;
        try { Files.createDirectories(base); } catch (IOException ignored) {}
    }

    public <T> T loadOrDefault(String fileName, TypeReference<T> type, T defaultValue) {
        Path p = base.resolve(fileName);
        if (!Files.exists(p)) {
            save(fileName, defaultValue);
            return defaultValue;
        }
        try {
            return mapper.readValue(Files.readAllBytes(p), type);
        } catch (Exception e) {
            // JSON corrupto: reseteamos
            save(fileName, defaultValue);
            return defaultValue;
        }
    }

    public void save(String fileName, Object data) {
        Path p = base.resolve(fileName);
        try {
            byte[] json = mapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(Objects.requireNonNullElse(data, ""));
            Files.createDirectories(p.getParent());
            Files.write(p, json, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Error guardando " + p, e);
        }
    }
}
