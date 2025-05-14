package nosensegenerator.nosense;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
public class Adjective{
    private String adjective;
    
    public Adjective() {
        String FilePath = "src/main/resources/terms/adjectives.txt";
        
        try (Scanner scanner = new Scanner(new File(FilePath))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                System.out.println(line);
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        }
    }
    
    public String getAdjective() {
        return adjective;
    }
}