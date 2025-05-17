package nosensegenerator.nosense;

import java.util.ArrayList;

public class Sentence {
    private String text;
    private ArrayList<AnalysisResultToken> analysisResultTokens;
    private ArrayList<ToxicityResultToken> toxicityResultTokens;

    public Sentence(String text) {
        this.text = text;
        analysisResultTokens = new ArrayList<>();
        toxicityResultTokens = new ArrayList<>();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ArrayList<String> getVerbs(String tense) {
        ArrayList<String> verbs = new ArrayList<>();
        for (AnalysisResultToken token : analysisResultTokens) {
            if (token.getTag().equals("VERB") && token.getTense().equals(tense)) {
                verbs.add(token.getText());
            }
        }
        return verbs;
    }

    public ArrayList<String> getNouns() {
        ArrayList<String> nouns = new ArrayList<>();
        for (AnalysisResultToken token : analysisResultTokens) {
            if (token.getTag().equals("NOUN")) {
                nouns.add(token.getText());
            }
        }
        return nouns;
    }

    public ArrayList<String> getAdjectives() {
        ArrayList<String> adjectives = new ArrayList<>();
        for (AnalysisResultToken token : analysisResultTokens) {
            if (token.getTag().equals("ADJ")) {
                adjectives.add(token.getText());
            }
        }
        return adjectives;
    }

    public ArrayList<AnalysisResultToken> getAnalysisResultTokens() {
        return analysisResultTokens;
    }

    public void setAnalysisResultTokens(ArrayList<AnalysisResultToken> analysisResultTokens) {
        this.analysisResultTokens = analysisResultTokens;
    }

    public ArrayList<ToxicityResultToken> getToxicityResultTokens() {
        return toxicityResultTokens;
    }

    public void setToxicityResultTokens(ArrayList<ToxicityResultToken> toxicityResultTokens) {
        this.toxicityResultTokens = toxicityResultTokens;
    }

}
