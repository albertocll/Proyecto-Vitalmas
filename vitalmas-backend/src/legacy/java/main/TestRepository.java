package main;

import api.model.Enfermedad;
import repository.EnfermedadRepository;

public class TestRepository {
    public static void main(String[] args) {
        EnfermedadRepository repo = new EnfermedadRepository();

        for (Enfermedad e : repo.getEnfermedades()) {
            System.out.println(e);
        }
    }
}
