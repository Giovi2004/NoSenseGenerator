package nosensegenerator.nosense;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Noun{
    private String noun;
    public Noun(){
        String FilePath = "src/main/resources/terms/nouns.txt";
        
        try (Scanner scanner = new Scanner(new File(FilePath))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                System.out.println(line);
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        }
    };
    public String getNoun(){
        return noun;
    };

}