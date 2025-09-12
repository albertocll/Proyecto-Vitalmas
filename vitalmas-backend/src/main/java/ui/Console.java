package ui;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Console {
    private static final Scanner SC = new Scanner(System.in);

    public static String readLine(String prompt) {
        System.out.print(prompt);
        String s = SC.nextLine();
        return s == null ? "" : s.trim();
    }

    public static int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                String line = SC.nextLine();
                int v = Integer.parseInt(line.trim());
                if (v < min || v > max) {
                    System.out.println("Valor fuera de rango (" + min + " - " + max + ").");
                    continue;
                }
                return v;
            } catch (NumberFormatException | InputMismatchException e) {
                System.out.println("Introduce un número válido.");
            }
        }
    }

    public static boolean readYesNo(String prompt) {
        while (true) {
            String s = readLine(prompt + " (s/n): ").toLowerCase();
            if (s.equals("s") || s.equals("si")) return true;
            if (s.equals("n") || s.equals("no")) return false;
            System.out.println("Responde 's' o 'n'.");
        }
    }
}
