package nosensegenerator.nosense;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Scanner;

public class Verb{

    private String verb;
    private ArrayList<String> verbs = new ArrayList<>();
    private int generatedNumber = 0;
    private String FilePath = "src/main/resources/terms/nouns.txt";

    public Verb(){
        
        try (Scanner scanner = new Scanner(new File(FilePath))){
            while (scanner.hasNextLine()){
                verbs.add(scanner.nextLine());
            }
            scanner.close();
        } 
        catch (FileNotFoundException e){
            System.err.println("File not found: " + e.getMessage());
        }
    }
    public String getverb(){
        generatedNumber = (int)(Math.random() * (verbs.size()-1));
        verb = verbs.get(generatedNumber);
        
        return verb;
    }
    public void save( ArrayList<String> verbsForFile){
        if(verbsForFile.isEmpty()){
            System.out.println("The list of verbs is empty.");
            return;
        }
        for(int i = 0; i < verbsForFile.size(); i++){
            for(int j = 0; j < verbs.size(); j++){
                if(!verbsForFile.get(i).equals(verbs.get(j))){
                    verbs.add(verbsForFile.get(i));
                    try(Writer writer = new FileWriter(FilePath, true)){
                        writer.write(verbsForFile.get(i) + "\n");
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