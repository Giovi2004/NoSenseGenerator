package nosensegenerator.nosense;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
public class Adjective{
    private String adjective;
    
    public Adjective() {
        String percorsoFile = "src/main/resources/terms/adjectives.json";
        
        try (Scanner scanner = new Scanner(new File(percorsoFile))) {
            while (scanner.hasNextLine()) {
                String linea = scanner.nextLine();
                System.out.println(linea);
            }
        } catch (FileNotFoundException e) {
            System.err.println("File non trovato: " + e.getMessage());
        }
    }
    
    public String getAdjective() {
        return adjective;
    }
}