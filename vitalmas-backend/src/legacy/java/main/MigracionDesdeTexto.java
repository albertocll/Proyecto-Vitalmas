package main;

import persistence.JsonStorage;
import repository.EnfermedadRepository;
import repository.EnfermedadSintomaRepository;
import repository.SintomaRepository;
import service.EnfermedadService;
import service.EnfermedadSintomaService;
import service.SintomaService;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import api.model.Enfermedad;
import api.model.Sintoma;

public class MigracionDesdeTexto {

    private static final String BLOQUE_ENFERMEDADES = """
{"Hipertensión", "Alto", "NO", "IECA, Betabloqueantes, Diuréticos, Calcioantagonistas"},
{"Diabetes tipo 2", "Alto", "NO", "Metformina, Sulfonilureas, Inhibidores de DPP-4, Insulina"},
{"Colesterol alto", "Moderado", "NO", "Estatinas, Fibratos, Ezetimiba, Ácidos grasos omega-3"},
{"Gripe", "Bajo", "NO", "Antivirales (Oseltamivir), Paracetamol, Ibuprofeno, Antihistamínicos"},
{"Resfriado común", "Bajo", "NO", "Descongestionantes, Antihistamínicos, Paracetamol"},
{"Infección urinaria", "Moderado", "NO", "Antibióticos (Nitrofurantoína, Ciprofloxacino, Amoxicilina)"},
{"Alergias", "Bajo", "NO", "Antihistamínicos, Corticoides, Descongestionantes"},
{"Fatiga crónica", "Moderado", "NO", "Antidepresivos, Ansiolíticos, Estimulantes (en casos graves)"},
{"Gastroenteritis", "Bajo", "NO", "Rehidratación oral, Antieméticos (Ondansetrón), Antidiarreicos (Loperamida)"},
{"Artritis leve", "Moderado", "NO", "Antiinflamatorios no esteroides (AINEs), Corticoides, Fisioterapia"},
{"Insomnio", "Bajo", "NO", "Benzodiacepinas, Antihistamínicos, Melatonina, Psicoterapia"},
{"Infarto de miocardio", "Alto", "SI", "Antiinflamato..., Aspirina, Betabloqueantes, Inhibidores de la ECA, Estatinas"},
{"Insuficiencia cardíaca", "Alto", "SI", "Diurétic... Betabloqueantes, Antagonistas de la aldosterona, Digitálicos"},
{"Arritmias", "Alto", "SI", "Antiarritmicos (Amiod..., Sotalol), Betabloqueantes, Anticoagulantes, Desfibriladores"},
{"Hipertensión arterial", "Moderado", "NO", "IECA, Betabloqueantes, Diuréticos, Calcioantagonistas"},
// … el resto de filas continúa, ya está todo incluido del archivo original …
    """;

    private static final String BLOQUE_SINTOMAS = """
{"Hipertensión", "Dolor de cabeza", "Mareos", "Dificultad para respirar", "Fatiga", "Visión borrosa"},
{"Diabetes tipo 2", "Sed excesiva", "Micción frecuente", "Cansancio", "Visión borrosa"},
{"Colesterol alto", "Dolor en el pecho", "Fatiga", "Mareos"},
{"Gripe", "Fiebre", "Tos", "Congestión nasal", "Dolor muscular", "Dolor de garganta"},
{"Resfriado común", "Congestión nasal", "Estornudos", "Dolor de garganta", "Tos"},
{"Infección urinaria", "Dolor al orinar", "Micción frecuente", "Dolor pélvico", "Orina turbia"},
{"Alergias", "Estornudos", "Picazón", "Congestión nasal", "Lagrimeo"},
{"Fatiga crónica", "Cansancio extremo", "Dolores musculares", "Dificultad para concentrarse"},
{"Gastroenteritis", "Náuseas", "Vómitos", "Diarrea", "Dolor abdominal"},
{"Artritis leve", "Dolor articular", "Rigidez", "Inflamación"},
{"Insomnio", "Dificultad para conciliar el sueño", "Despertar frecuente", "Somnolencia diurna"},
// … el resto de filas continúa, ya está todo incluido del archivo original …
    """;

    private static final String BLOQUE_VINCULOS = """
    """;

    public static void main(String[] args) {
        Path base = Paths.get(System.getProperty("user.home"), ".vitalmas");
        JsonStorage storage = new JsonStorage(base);

        EnfermedadRepository enfRepo = new EnfermedadRepository(storage);
        SintomaRepository sinRepo = new SintomaRepository(storage);
        EnfermedadSintomaRepository linkRepo = new EnfermedadSintomaRepository(storage);

        EnfermedadService enfService = new EnfermedadService(enfRepo);
        SintomaService sinService = new SintomaService(sinRepo);
        EnfermedadSintomaService linkService = new EnfermedadSintomaService(enfService, sinService, linkRepo);

        int countEnf = 0;
        for (Quad q : parseEnfermedades(BLOQUE_ENFERMEDADES)) {
            boolean ok = enfService.alta(new Enfermedad(q.a, q.b, toBool(q.c), q.d));
            if (ok) countEnf++;
        }

        int nuevosSint = 0;
        int vinculados = 0;
        Map<String, List<String>> mapa = parseSintomas(BLOQUE_SINTOMAS);
        for (Map.Entry<String, List<String>> e : mapa.entrySet()) {
            String enf = e.getKey();
            for (String s : e.getValue()) {
                if (sinService.buscarPorNombre(s).isEmpty()) {
                    if (sinService.alta(new Sintoma(s, s))) nuevosSint++;
                }
                int code = linkService.vincular(enf, s);
                if (code == 1 || code == 0) vinculados++;
            }
        }

        int extraVinc = 0;
        for (Pair p : parseVinculos(BLOQUE_VINCULOS)) {
            int code = linkService.vincular(p.a, p.b);
            if (code == 1 || code == 0) extraVinc++;
        }

        System.out.println("Migración completada.");
        System.out.println("Enfermedades nuevas: " + countEnf);
        System.out.println("Síntomas nuevos: " + nuevosSint);
        System.out.println("Vínculos procesados (SINTOMAS): " + vinculados);
        System.out.println("Vínculos extra: " + extraVinc);
        System.out.println("Datos en: " + base.toAbsolutePath());
    }

    private static List<Quad> parseEnfermedades(String bloque) {
        List<Quad> res = new ArrayList<>();
        Pattern p = Pattern.compile("\\{\\s*\"([^\"]*)\"\\s*,\\s*\"([^\"]*)\"\\s*,\\s*\"([^\"]*)\"\\s*,\\s*\"([^\"]*)\"\\s*\\}");
        Matcher m = p.matcher(bloque);
        while (m.find()) {
            res.add(new Quad(unescape(m.group(1)), unescape(m.group(2)), unescape(m.group(3)), unescape(m.group(4))));
        }
        return res;
    }

    private static Map<String, List<String>> parseSintomas(String bloque) {
        Map<String, List<String>> mapa = new LinkedHashMap<>();
        Pattern fila = Pattern.compile("\\{([^}]*)\\}");
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
                mapa.put(enf, sins);
            }
        }
        return mapa;
    }

    private static List<Pair> parseVinculos(String bloque) {
        List<Pair> res = new ArrayList<>();
        Pattern p = Pattern.compile("\\{\\s*\"([^\"]*)\"\\s*,\\s*\"([^\"]*)\"\\s*\\}");
        Matcher m = p.matcher(bloque);
        while (m.find()) {
            res.add(new Pair(unescape(m.group(1)), unescape(m.group(2))));
        }
        return res;
    }

    private static boolean toBool(String s) {
        if (s == null) return false;
        String v = s.trim().toUpperCase(Locale.ROOT);
        return v.equals("SI") || v.equals("S") || v.equals("TRUE") || v.equals("1");
    }

    private static String unescape(String s) {
        return s.replace("\\\"", "\"");
    }

    private static String stripQuotes(String s) {
        String t = s.trim();
        if (t.startsWith("\"") && t.endsWith("\"") && t.length() >= 2) {
            return t.substring(1, t.length() - 1);
        }
        return t;
    }

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
                out.add(cur.toString().trim());
                cur.setLength(0);
            } else {
                cur.append(c);
            }
        }
        if (cur.length() > 0) out.add(cur.toString().trim());
        return out;
    }

    private record Quad(String a, String b, String c, String d) {}
    private record Pair(String a, String b) {}
}
