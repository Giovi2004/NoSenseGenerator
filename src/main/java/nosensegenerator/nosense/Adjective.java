package nosensegenerator.nosense;

import java.util.ArrayList;

public class Adjective{

    private String adjective;
    private ArrayList<String> adjectives = new ArrayList<>();
    private int generatedNumber = 0;
    private String FilePath = "src/main/resources/terms/nouns.txt";

    public Adjective(){
        adjectives = FileHandler.load(FilePath);
    }
    public String getAdjective(){
        generatedNumber = (int)(Math.random() * (adjectives.size()-1));
        adjective = adjectives.get(generatedNumber);
        
        return adjective;
    }
    public void save( ArrayList<String> adjectivesForFile){
        FileHandler.save(adjectives, adjectivesForFile, FilePath);
    }
}