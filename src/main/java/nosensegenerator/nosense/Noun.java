// This class generates a random noun from a file
package nosensegenerator.nosense;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Noun{

    private String noun;
    private int LineCounter = 0;
    private int generatedNumber = 0;

    // Constructor
    public Noun(){
        String FilePath = "src/main/resources/terms/nouns.txt";
        
        try (Scanner scanner = new Scanner(new File(FilePath))){
            while (scanner.hasNextLine()){
                LineCounter++;
            }
            // Generate a random number between 0 and LineCounter
            generatedNumber = (int)(Math.random() * LineCounter);
            // Reset the scanner to read the file again
            scanner.close();
            Scanner scanner2 = new Scanner(new File(FilePath));
            // Loop through the file to get the noun at the generated number
            for(int i=0; i<LineCounter; i++){
                if(i==(generatedNumber -1)){
                    noun = scanner2.nextLine();
                }
            }
            scanner2.close();
        } 
        catch (FileNotFoundException e){
            System.err.println("File not found: " + e.getMessage());
        }
    }
    // Method to get the generated noun
    public String getNoun(){
        return noun;
    }
}