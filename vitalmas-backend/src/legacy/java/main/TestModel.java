package main;

import api.model.Enfermedad;

public class TestModel {
    public static void main(String[] args) {
        Enfermedad gripe = new Enfermedad("Gripe", "Bajo", false, "Reposo y líquidos");
        Enfermedad cancer = new Enfermedad("Cáncer", "Alto", true, "Quimioterapia");

        System.out.println(gripe);
        System.out.println(cancer);
    }
}
