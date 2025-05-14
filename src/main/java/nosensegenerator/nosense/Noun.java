package nosensegenerator.nosense;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Noun{
    private String noun;
    public Noun(){
        String percorsoFile = "src/main/resources/terms/nouns.json";
        
        try (Scanner scanner = new Scanner(new File(percorsoFile))) {
            while (scanner.hasNextLine()) {
                String linea = scanner.nextLine();
                System.out.println(linea);
            }
        } catch (FileNotFoundException e) {
            System.err.println("File non trovato: " + e.getMessage());
        }
    };
    public String getNoun(){
        return noun;
    };

}