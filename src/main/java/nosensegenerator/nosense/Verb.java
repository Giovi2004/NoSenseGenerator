package nosensegenerator.nosense;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Verb{
    public Verb() {
        String percorsoFile = "src/main/resources/terms/verbs.json";
        
        try (Scanner scanner = new Scanner(new File(percorsoFile))) {
            while (scanner.hasNextLine()) {
                String linea = scanner.nextLine();
                System.out.println(linea);
            }
        } catch (FileNotFoundException e) {
            System.err.println("File non trovato: " + e.getMessage());
        }
    }

    public String getVerb() {
        return verb;
    }
    private String verb;
}