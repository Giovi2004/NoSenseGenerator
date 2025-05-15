package nosensegenerator.nosense;

import java.util.List;

public class Sentence {
    private String text;
    private List<String> verbs;
    private List<String> nouns;
    private List<String> adjectives;
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
    public List<String> getVerbs() {
        return verbs;
    }
    public void setVerbs(List<String> verbs) {
        this.verbs = verbs;
    }
    public List<String> getNouns() {
        return nouns;
    }
    public void setNouns(List<String> nouns) {
        this.nouns = nouns;
    }
    public List<String> getAdjectives() {
        return adjectives;
    }
    public void setAdjectives(List<String> adjectives) {
        this.adjectives = adjectives;
    }
    

}
