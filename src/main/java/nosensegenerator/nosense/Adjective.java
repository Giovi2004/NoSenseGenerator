package nosensegenerator.nosense;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Scanner;

public class Adjective{

    private String adjective;
    private ArrayList<String> adjectives = new ArrayList<>();
    private int generatedNumber = 0;
    private String FilePath = "src/main/resources/terms/nouns.txt";

    public Adjective(){
        
        try (Scanner scanner = new Scanner(new File(FilePath))){
            while (scanner.hasNextLine()){
                adjectives.add(scanner.nextLine());
            }
            scanner.close();
        } 
        catch (FileNotFoundException e){
            System.err.println("File not found: " + e.getMessage());
        }
    }
    public String getAdjective(){
        generatedNumber = (int)(Math.random() * (adjectives.size()-1));
        adjective = adjectives.get(generatedNumber);
        
        return adjective;
    }
    public void save( ArrayList<String> adjectivesForFile){
        if(adjectivesForFile.isEmpty()){
            System.out.println("The list of adjectives is empty.");
            return;
        }
        for(int i = 0; i < adjectivesForFile.size(); i++){
            for(int j = 0; j < adjectives.size(); j++){
                if(!adjectivesForFile.get(i).equals(adjectives.get(j))){
                    adjectives.add(adjectivesForFile.get(i));
                    try(Writer writer = new FileWriter(FilePath, true)){
                        writer.write(adjectivesForFile.get(i) + "\n");
                        writer.close();
                    } 
                    catch (java.io.IOException e){
                        System.err.println("Error writing to file: " + e.getMessage());
                    } 
                }
            }            
        }
    }
}