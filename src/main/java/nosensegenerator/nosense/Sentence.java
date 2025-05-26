package nosensegenerator.nosense;

import java.util.ArrayList;

/*
 * This class calls for the right method of other classes to get the needed parts for the sentence.
 */
public class Sentence {

    private String text;
    private ArrayList<AnalysisResultToken> analysisResultTokens;
    private ArrayList<ToxicityResultToken> toxicityResultTokens;
    //constructor
    public Sentence(String text) {
        this.text = text;
        analysisResultTokens = new ArrayList<>();
        toxicityResultTokens = new ArrayList<>();
    }
    
    public String getText() {
        return text;
    }

    /**
     * Sets the text of the sentence.
     * 
     * @param text The text to set.
     */
    public void setText(String text) {
        this.text = text;
    }

    //Returns the verbs at the  right tense.
    public ArrayList<String> getVerbs(String tense) {
        ArrayList<String> verbs = new ArrayList<>();
        if (!tense.equals("PRESENT" ) && !tense.equals("PAST" ) && !tense.equals("FUTURE" )) {
            for (AnalysisResultToken token : analysisResultTokens) {
                if (token.getTag().equals("VERB")) {
                    verbs.add(token.getText());
                }
            }
        } else {
            for (AnalysisResultToken token : analysisResultTokens) {
                if (token.getTag().equals("VERB") && token.getTense().equals(tense)) {
                    verbs.add(token.getText());
                }
            }
        }

        return verbs;
    }

    /**
     * Method that returns the nouns.
     */
    public ArrayList<String> getNouns() {
        ArrayList<String> nouns = new ArrayList<>();
        for (AnalysisResultToken token : analysisResultTokens) {
            if (token.getTag().equals("NOUN")) {
                nouns.add(token.getText());
            }
        }
        return nouns;
    }
    /**
     * Method that returns the adjectives.
     */
    public ArrayList<String> getAdjectives() {
        ArrayList<String> adjectives = new ArrayList<>();
        for (AnalysisResultToken token : analysisResultTokens) {
            if (token.getTag().equals("ADJ")) {
                adjectives.add(token.getText());
            }
        }
        return adjectives;
    }
    /**Give and set the analysis result of Tokens*/ 
    public ArrayList<AnalysisResultToken> getAnalysisResultTokens() {
        return analysisResultTokens;
    }

    public void setAnalysisResultTokens(
            ArrayList<AnalysisResultToken> analysisResultTokens) {
        this.analysisResultTokens = analysisResultTokens;
    }
    /**Give and set the toxicity result of Tokens*/
    public ArrayList<ToxicityResultToken> getToxicityResultTokens() {
        return toxicityResultTokens;
    }

    public void setToxicityResultTokens(
            ArrayList<ToxicityResultToken> toxicityResultTokens) {
        this.toxicityResultTokens = toxicityResultTokens;
    }
    /**
     * Method to check if the text of the sentence is blank.
     * 
     * @return true if the text is blank, false otherwise.
     */

    public boolean isTextBlank() {
        return text.isBlank();
    }
}
