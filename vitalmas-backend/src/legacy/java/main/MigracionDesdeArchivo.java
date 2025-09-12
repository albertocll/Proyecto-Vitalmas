package main;

import persistence.JsonStorage;
import repository.EnfermedadRepository;
import repository.EnfermedadSintomaRepository;
import repository.SintomaRepository;
import service.EnfermedadService;
import service.EnfermedadSintomaService;
import service.SintomaService;
import util.TextNorm;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import api.model.Enfermedad;
import api.model.Sintoma;

/**
 * Importa TODO desde tu Principal.java viejo:
 *  - ENFERMEDADES[][][] => {"nombre","riesgo","SI/NO","tratamiento"}
 *  - SINTOMAS[][][]     => {"Enfermedad","Sint1","Sint2",...}
 * Guarda en ~/.vitalmas/enfermedades.json, sintomas.json, vinculos.json
 */
public class MigracionDesdeArchivo {

    // OPCIONAL: pon aquí la ruta a tu Principal.java viejo si la sabes.
    // Si lo dejas vacío, el programa te pedirá la ruta al arrancar.
    private static final String RUTA_FIJA = "";

    public static void main(String[] args) {
        try {
            Path base = Paths.get(System.getProperty("user.home"), ".vitalmas");
            JsonStorage storage = new JsonStorage(base);

            // Limpieza previa para evitar mezclas
            borrarSiExiste(base.resolve("enfermedades.json"));
            borrarSiExiste(base.resolve("sintomas.json"));
            borrarSiExiste(base.resolve("vinculos.json"));

            // 1) Cargar archivo fuente
            Path fuente = obtenerRutaFuente();
            String texto = Files.readString(fuente, StandardCharsets.UTF_8);

            // 2) Extraer bloques
            String bloqueEnf = extraerBloque(texto, "(?:final\\s+)?String\\s+ENFERMEDADES\\s*\\[\\s*]\\s*\\[\\s*]\\s*\\[\\s*]\\s*=\\s*\\{");
            String bloqueSin = extraerBloque(texto, "(?:final\\s+)?String\\s+SINTOMAS\\s*\\[\\s*]\\s*\\[\\s*]\\s*\\[\\s*]\\s*=\\s*\\{");

            if (bloqueEnf == null || bloqueSin == null) {
                throw new RuntimeException("No se encontraron los bloques ENFERMEDADES o SINTOMAS en " + fuente);
            }

            // 3) Preparar repos/servicios con persistencia
            EnfermedadRepository enfRepo = new EnfermedadRepository(storage);
            SintomaRepository sinRepo = new SintomaRepository(storage);
            EnfermedadSintomaRepository linkRepo = new EnfermedadSintomaRepository(storage);

            EnfermedadService enfService = new EnfermedadService(enfRepo);
            SintomaService sinService = new SintomaService(sinRepo);
            EnfermedadSintomaService linkService = new EnfermedadSintomaService(enfService, sinService, linkRepo);

            // 4) Parsear e importar ENFERMEDADES
            int countEnf = 0;
            for (Quad q : parseEnfermedades(bloqueEnf)) {
                boolean ok = enfService.alta(new Enfermedad(q.a, q.b, toBool(q.c), q.d));
                if (ok) countEnf++;
            }

            // 5) Parsear e importar SINTOMAS (enlaza a Enfermedad)
            int nuevosSint = 0;
            int vinculados = 0;
            Map<String, List<String>> mapa = parseSintomas(bloqueSin);
            for (Map.Entry<String, List<String>> e : mapa.entrySet()) {
                String enf = e.getKey();
                for (String s : e.getValue()) {
                    // alta síntoma si no existe (descripción provisional = nombre)
                    if (sinService.buscarPorNombre(s).isEmpty()) {
                        if (sinService.alta(new Sintoma(s, s))) nuevosSint++;
                    }
                    int code = linkService.vincular(enf, s);
                    if (code == 1 || code == 0) vinculados++;
                }
            }

            // 6) Resultado
            System.out.println("Migración completada desde: " + fuente.toAbsolutePath());
            System.out.println("Enfermedades nuevas: " + countEnf);
            System.out.println("Síntomas nuevos: " + nuevosSint);
            System.out.println("Vínculos procesados: " + vinculados);
            System.out.println("JSON en: " + base.toAbsolutePath());
            System.out.println("(Si repites la migración, no duplica por nombre gracias a la normalización)");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------- Utilidades de IO ----------
    private static void borrarSiExiste(Path p) {
        try { Files.deleteIfExists(p); } catch (Exception ignored) {}
    }

    private static Path obtenerRutaFuente() throws IOException {
        if (RUTA_FIJA != null && !RUTA_FIJA.isBlank()) {
            Path p = Paths.get(RUTA_FIJA);
            if (Files.exists(p)) return p;
            System.out.println("Ruta fija no encontrada: " + p);
        }
        System.out.print("Ruta a tu Principal.java viejo: ");
        try (java.util.Scanner sc = new java.util.Scanner(System.in)) {
            String in = sc.nextLine();
            Path p = Paths.get(in.trim());
            if (!Files.exists(p)) throw new IOException("No existe el archivo: " + p);
            return p;
        }
    }

    // ---------- Extractor de bloques con llaves balanceadas ----------
    private static String extraerBloque(String texto, String regexAsignacion) {
        Pattern pat = Pattern.compile(regexAsignacion);
        Matcher m = pat.matcher(texto);
        if (!m.find()) return null;
        int start = texto.indexOf('{', m.end() - 1);
        if (start < 0) return null;

        int depth = 0;
        boolean inQ = false;
        boolean esc = false;
        for (int i = start; i < texto.length(); i++) {
            char c = texto.charAt(i);
            if (inQ) {
                if (esc) { esc = false; }
                else if (c == '\\') { esc = true; }
                else if (c == '"') { inQ = false; }
            } else {
                if (c == '"') inQ = true;
                else if (c == '{') depth++;
                else if (c == '}') {
                    depth--;
                    if (depth == 0) {
                        return texto.substring(start, i + 1);
                    }
                }
            }
        }
        return null;
    }

    // ---------- Parsers ----------
    /** {"a","b","c","d"} */
    private static List<Quad> parseEnfermedades(String bloque) {
        List<Quad> res = new ArrayList<>();
        Pattern p = Pattern.compile("\\{\\s*\"([^\"]*)\"\\s*,\\s*\"([^\"]*)\"\\s*,\\s*\"([^\"]*)\"\\s*,\\s*\"([^\"]*)\"\\s*\\}");
        Matcher m = p.matcher(bloque);
        while (m.find()) {
            res.add(new Quad(unescape(m.group(1)), unescape(m.group(2)), unescape(m.group(3)), unescape(m.group(4))));
        }
        return res;
    }

    /** {"Enf","Sint1","Sint2",...} dentro de SINTOMAS[][][] */
    private static Map<String, List<String>> parseSintomas(String bloque) {
        Map<String, List<String>> mapa = new LinkedHashMap<>();
        Pattern fila = Pattern.compile("\\{([^{}]*)\\}");
        Matcher mf = fila.matcher(bloque);
        while (mf.find()) {
            String contenido = mf.group(1);
            List<String> cols = splitQuoted(contenido);
            if (cols.size() >= 2) {
                String enf = unescape(stripQuotes(cols.get(0)));
                List<String> sins = new ArrayList<>();
                for (int i = 1; i < cols.size(); i++) {
                    sins.add(unescape(stripQuotes(cols.get(i))));
                }
                // normalización para evitar duplicados por tildes/casos
                String key = TextNorm.normKey(enf);
                mapa.computeIfAbsent(key, k -> new ArrayList<>());
                List<String> prev = mapa.get(key);
                for (String s : sins) {
                    if (!prev.contains(s)) prev.add(s);
                }
            }
        }
        // Reconstruye mapa presentable “Enfermedad” -> lista de síntomas
        Map<String, List<String>> presentable = new LinkedHashMap<>();
        for (String k : mapa.keySet()) {
            String bonito = capitalize(k);
            presentable.put(bonito, mapa.get(k));
        }
        return presentable;
    }

    // ---------- Helpers de texto ----------
    private static boolean toBool(String s) {
        if (s == null) return false;
        String v = s.trim().toUpperCase(Locale.ROOT);
        return v.equals("SI") || v.equals("S") || v.equals("TRUE") || v.equals("1");
    }

    private static String unescape(String s) { return s.replace("\\\"", "\""); }

    private static String stripQuotes(String s) {
        String t = s.trim();
        if (t.startsWith("\"") && t.endsWith("\"") && t.length() >= 2) {
            return t.substring(1, t.length() - 1);
        }
        return t;
    }

    /** divide por comas respetando comillas */
    private static List<String> splitQuoted(String row) {
        List<String> out = new ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean inQ = false;
        for (int i = 0; i < row.length(); i++) {
            char c = row.charAt(i);
            if (c == '"') {
                inQ = !inQ;
                cur.append(c);
            } else if (c == ',' && !inQ) {
                String s = cur.toString().trim();
                if (!s.isEmpty()) out.add(s);
                cur.setLength(0);
            } else {
                cur.append(c);
            }
        }
        String s = cur.toString().trim();
        if (!s.isEmpty()) out.add(s);
        return out;
    }

    private static String capitalize(String s) {
        if (s == null || s.isBlank()) return s;
        return s.substring(0, 1).toUpperCase(Locale.ROOT) + s.substring(1);
    }

    private record Quad(String a, String b, String c, String d) {}
}
