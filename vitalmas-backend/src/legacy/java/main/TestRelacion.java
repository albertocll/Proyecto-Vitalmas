package main;

import api.model.Enfermedad;
import api.model.Sintoma;
import repository.EnfermedadRepository;
import repository.EnfermedadSintomaRepository;
import repository.SintomaRepository;
import service.EnfermedadService;
import service.EnfermedadSintomaService;
import service.SintomaService;

public class TestRelacion {
    public static void main(String[] args) {
        // Repositorios base
        EnfermedadRepository enfRepo = new EnfermedadRepository();
        SintomaRepository sinRepo = new SintomaRepository();

        // Servicios base
        EnfermedadService enfService = new EnfermedadService(enfRepo);
        SintomaService sinService = new SintomaService(sinRepo);

        // Repositorio y servicio de vínculos
        EnfermedadSintomaRepository linkRepo = new EnfermedadSintomaRepository();
        EnfermedadSintomaService linkService =
                new EnfermedadSintomaService(enfService, sinService, linkRepo);

        // Datos adicionales para pruebas
        enfService.alta(new Enfermedad("Gripe", "Bajo", false, "Reposo y líquidos"));
        sinService.alta(new Sintoma("Congestión", "Obstrucción nasal"));

        // Vinculaciones
        System.out.println("Vincular Gripe - Fiebre: " + linkService.vincular("Gripe", "Fiebre"));         // 1 o 0
        System.out.println("Vincular Gripe - Tos: " + linkService.vincular("Gripe", "Tos"));               // 1 o 0
        System.out.println("Vincular Gripe - Congestión: " + linkService.vincular("Gripe", "Congestión")); // 1 o 0
        System.out.println("Vincular Gripe - fiebre (dup): " + linkService.vincular("Gripe", "fiebre"));   // 0

        // Listado
        System.out.println("\nSíntomas de Gripe:");
        linkService.listarSintomas("Gripe").forEach(System.out::println);

        // Desvincular
        System.out.println("\nDesvincular Gripe - Tos: " + linkService.desvincular("Gripe", "Tos"));
        System.out.println("Síntomas de Gripe (tras desvincular):");
        linkService.listarSintomas("Gripe").forEach(System.out::println);

        // Errores
        System.out.println("\nVincular ENF inexistente: " + linkService.vincular("NoExiste", "Fiebre")); // -1
        System.out.println("Vincular SINT inexistente: " + linkService.vincular("Gripe", "Xxx"));        // -2
    }
}
