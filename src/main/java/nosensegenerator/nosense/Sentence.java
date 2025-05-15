package nosensegenerator.nosense;

import java.util.ArrayList;

public class Sentence {
    private String text;
    private ArrayList<String> verbs;
    private ArrayList<String> nouns;
    private ArrayList<String> adjectives;
    //aggiungere i gli attributi per il resto dei risultati del analisi
    
    public Sentence(String text) {
        this.text = text;
    }
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public ArrayList<String> getVerbs() {
        return verbs;
    }
    public void setVerbs(ArrayList<String> verbs) {
        this.verbs = verbs;
    }
    public ArrayList<String> getNouns() {
        return nouns;
    }
    public void setNouns(ArrayList<String> nouns) {
        this.nouns = nouns;
    }
    public ArrayList<String> getAdjectives() {
        return adjectives;
    }
    public void setAdjectives(ArrayList<String> adjectives) {
        this.adjectives = adjectives;
    }
    

}
