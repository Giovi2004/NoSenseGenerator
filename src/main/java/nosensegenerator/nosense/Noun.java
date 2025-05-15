package nosensegenerator.nosense;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Scanner;

public class Noun{

    private String noun;
    private ArrayList<String> nouns = new ArrayList<>();
    private int generatedNumber = 0;
    private String FilePath = "src/main/resources/terms/nouns.txt";

    public Noun(){
        
        try (Scanner scanner = new Scanner(new File(FilePath))){
            while (scanner.hasNextLine()){
                nouns.add(scanner.nextLine());
            }
            scanner.close();
        } 
        catch (FileNotFoundException e){
            System.err.println("File not found: " + e.getMessage());
        }
    }
    public String getnoun(){
        generatedNumber = (int)(Math.random() * (nouns.size()-1));
        noun = nouns.get(generatedNumber);
        
        return noun;
    }
    public void save( ArrayList<String> nounsForFile){
        if(nounsForFile.isEmpty()){
            System.out.println("The list of nouns is empty.");
            return;
        }
        for(int i = 0; i < nounsForFile.size(); i++){
            for(int j = 0; j < nouns.size(); j++){
                if(!nounsForFile.get(i).equals(nouns.get(j))){
                    nouns.add(nounsForFile.get(i));
                    try(Writer writer = new FileWriter(FilePath, true)){
                        writer.write(nounsForFile.get(i) + "\n");
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