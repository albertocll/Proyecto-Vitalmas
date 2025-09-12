package ui;

import persistence.JsonStorage;
import repository.EnfermedadRepository;
import repository.EnfermedadSintomaRepository;
import repository.SintomaRepository;
import service.EnfermedadService;
import service.EnfermedadSintomaService;
import service.SintomaService;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import api.model.Enfermedad;
import api.model.Sintoma;

public class Menu {

    private final EnfermedadService enfService;
    private final SintomaService sinService;
    private final EnfermedadSintomaService linkService;

    public Menu() {
        // Carpeta de datos: ~/.vitalmas
        Path base = Paths.get(System.getProperty("user.home"), ".vitalmas");
        JsonStorage storage = new JsonStorage(base);

        EnfermedadRepository enfRepo = new EnfermedadRepository(storage);
        SintomaRepository sinRepo = new SintomaRepository(storage);
        EnfermedadSintomaRepository linkRepo = new EnfermedadSintomaRepository(storage);

        this.enfService = new EnfermedadService(enfRepo);
        this.sinService = new SintomaService(sinRepo);
        this.linkService = new EnfermedadSintomaService(enfService, sinService, linkRepo);
    }

    public static void main(String[] args) {
        new Menu().run();
    }

    private void run() {
        int op;
        do {
            System.out.println("\n=== VITALMAS ===");
            System.out.println("1) Listar enfermedades");
            System.out.println("2) Buscar enfermedad por nombre");
            System.out.println("3) Alta de enfermedad");
            System.out.println("4) Listar síntomas de una enfermedad");
            System.out.println("5) Vincular síntoma a enfermedad");
            System.out.println("6) Estadísticas");
            System.out.println("0) Salir");
            op = Console.readInt("Opción: ", 0, 6);
            switch (op) {
                case 1 -> listarEnfermedades();
                case 2 -> buscarEnfermedad();
                case 3 -> altaEnfermedad();
                case 4 -> listarSintomasDeEnfermedad();
                case 5 -> vincularSintoma();
                case 6 -> estadisticas();
                case 0 -> System.out.println("Saliendo.");
            }
        } while (op != 0);
    }

    private void listarEnfermedades() {
        System.out.println("\n-- Enfermedades --");
        enfService.listar().forEach(System.out::println);
    }

    private void buscarEnfermedad() {
        String nombre = Console.readLine("Nombre exacto: ");
        var res = enfService.buscarPorNombre(nombre);
        System.out.println(res.map(Object::toString).orElse("No encontrada."));
    }

    private void altaEnfermedad() {
        String nombre = Console.readLine("Nombre: ");
        String riesgo = Console.readLine("Nivel de riesgo (Bajo/Medio/Alto): ");
        boolean requiereOp = Console.readYesNo("¿Requiere operación?");
        String tratamiento = Console.readLine("Tratamiento: ");
        boolean ok = enfService.alta(new Enfermedad(nombre, riesgo, requiereOp, tratamiento));
        System.out.println(ok ? "Creada." : "No creada (duplicada o inválida).");
    }

    private void listarSintomasDeEnfermedad() {
        String nombre = Console.readLine("Nombre de la enfermedad: ");
        var lista = linkService.listarSintomas(nombre);
        if (lista.isEmpty()) {
            System.out.println("Sin vínculos o enfermedad inexistente.");
        } else {
            lista.forEach(System.out::println);
        }
    }

    private void vincularSintoma() {
        String enf = Console.readLine("Enfermedad: ");
        String sin = Console.readLine("Síntoma: ");

        // Alta rápida del síntoma si no existe
        sinService.buscarPorNombre(sin).orElseGet(() -> {
            sinService.alta(new Sintoma(sin, Console.readLine("Descripción síntoma: ")));
            return null;
        });

        int code = linkService.vincular(enf, sin);
        String msg = switch (code) {
            case 1 -> "Vínculo creado.";
            case 0 -> "Ya estaba vinculado.";
            case -1 -> "La enfermedad no existe.";
            case -2 -> "El síntoma no existe.";
            default -> "Código desconocido: " + code;
        };
        System.out.println(msg);
    }

    private void estadisticas() {
        System.out.println("\n-- Estadísticas --");
        int totalEnf = enfService.listar().size();
        int totalSin = sinService.listar().size();

        int totalLinks = 0;
        Map<String, Integer> contadorSintomas = new HashMap<>();
        for (var e : enfService.listar()) {
            var lista = linkService.listarSintomas(e.getNombre());
            totalLinks += lista.size();
            for (var s : lista) {
                contadorSintomas.merge(s.getNombre(), 1, Integer::sum);
            }
        }

        System.out.println("Enfermedades: " + totalEnf);
        System.out.println("Síntomas: " + totalSin);
        System.out.println("Vínculos: " + totalLinks);

        System.out.println("\nTop 5 síntomas más frecuentes:");
        contadorSintomas.entrySet().stream()
                .sorted((a, b) -> b.getValue() - a.getValue())
                .limit(5)
                .forEach(e -> System.out.println(" - " + e.getKey() + " (" + e.getValue() + " enfermedades)"));
    }
}
