package nosensegenerator.nosense;

import java.util.ArrayList;

public class Noun{

    private String noun;
    private ArrayList<String> nouns = new ArrayList<>();
    private int generatedNumber = 0;
    private String FilePath = "src/main/resources/terms/nouns.txt";

    public Noun(){
        nouns = FileHandler.load(FilePath);
    }
    public String getnoun(){
        generatedNumber = (int)(Math.random() * (nouns.size()-1));
        noun = nouns.get(generatedNumber);
        
        return noun;
    }
    public void save( ArrayList<String> nounsForFile){
        FileHandler.save(nouns, nounsForFile, FilePath);
    }
}