package main;

import api.model.Sintoma;
import repository.SintomaRepository;
import service.SintomaService;

public class TestSintomaService {
    public static void main(String[] args) {
        SintomaRepository repo = new SintomaRepository();
        SintomaService service = new SintomaService(repo);

        System.out.println("Listado inicial:");
        service.listar().forEach(System.out::println);

        System.out.println("\nBuscar exacto 'Fiebre':");
        System.out.println(service.buscarPorNombre("Fiebre").orElse(null));

        System.out.println("\nBuscar contiene 'do':");
        service.buscarPorNombreContiene("do").forEach(System.out::println);

        System.out.println("\nAlta 'Cefalea':");
        System.out.println("Creada: " + service.alta(new Sintoma("Cefalea", "Dolor de cabeza")));

        System.out.println("\nIntento duplicado 'fiebre':");
        System.out.println("Creada: " + service.alta(new Sintoma("fiebre", "X")));

        System.out.println("\nListado final:");
        service.listar().forEach(System.out::println);
    }
}
