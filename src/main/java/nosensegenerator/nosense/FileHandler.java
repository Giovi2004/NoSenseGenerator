package nosensegenerator.nosense;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Scanner;

public class FileHandler {

    public static void save(ArrayList<String> terms, ArrayList<String> termsForFile, String filePath){
        if(termsForFile.isEmpty()){
            System.out.println("The list of terms is empty.");
            return;
        }
        for(int i = 0; i < termsForFile.size(); i++){
            boolean flag = false;
            for(int j = 0; j < terms.size(); j++){
                if(!termsForFile.get(i).equals(terms.get(j))){
                    flag=true; 
                }
            }
            if(flag){
                terms.add(termsForFile.get(i));
                try(Writer writer = new FileWriter(filePath, true)){
                        writer.write(termsForFile.get(i) + "\n");
                        writer.close();
                    } 
                    catch (java.io.IOException e){
                        System.err.println("Error writing to file: " + e.getMessage());
                    }    
            }         
        }
    }

    public static ArrayList<String> load(String filePath){
        ArrayList<String> terms = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(filePath))){
            while (scanner.hasNextLine()){
                terms.add(scanner.nextLine());
            }
            scanner.close();
        } 
        catch (FileNotFoundException e){
            System.err.println("File not found: " + e.getMessage());
        }
        return terms;
    }
}    
