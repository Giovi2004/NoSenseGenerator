package nosensegenerator.nosense;

public class Sentence {
    private String text;
    private String[] verbs;
    private String[] nouns;
    private String[] adjectives;
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
    public String[] getVerbs() {
        return verbs;
    }
    public void setVerbs(String[] verbs) {
        this.verbs = verbs;
    }
    public String[] getNouns() {
        return nouns;
    }
    public void setNouns(String[] nouns) {
        this.nouns = nouns;
    }
    public String[] getAdjectives() {
        return adjectives;
    }
    public void setAdjectives(String[] adjectives) {
        this.adjectives = adjectives;
    }
    

}
