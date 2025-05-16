package nosensegenerator.nosense;

import java.util.ArrayList;

public class Verb{

    private String verb;
    private ArrayList<String> verbs = new ArrayList<>();
    private int generatedNumber = 0;
    private String FilePath = "src/main/resources/terms/nouns.txt";

    public Verb(){
        verbs = FileHandler.load(FilePath);
    }
    public String getverb(){
        generatedNumber = (int)(Math.random() * (verbs.size()-1));
        verb = verbs.get(generatedNumber);
        
        return verb;
    }
    public void save( ArrayList<String> verbsForFile){
        FileHandler.save(verbs, verbsForFile, FilePath);
    }
}