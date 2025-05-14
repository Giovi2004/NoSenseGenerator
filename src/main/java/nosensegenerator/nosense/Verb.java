package nosensegenerator.nosense;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Verb{
    public Verb() {
        String FilePath = "src/main/resources/terms/verbs.txt";
        
        try (Scanner scanner = new Scanner(new File(FilePath))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                System.out.println(line);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        }
    }

    public String getVerb() {
        return verb;
    }
    private String verb;
}