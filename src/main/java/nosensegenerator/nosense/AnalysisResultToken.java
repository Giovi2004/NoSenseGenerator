package nosensegenerator.nosense;

/**
 * This class represents a token in the analysis result.
 * It contains information about the token's index, text, tag, tense, dependency label, and dependency token.
 */
public class AnalysisResultToken {
    private int index;
    private String text;
    private String tag;
    private String tense;
    private String dependencyLabel;
    private int dependencyToken;
    /**
     * Constructor for AnalysisResultToken.
     * Initializes the token with the provided index, text, tag, dependency label, dependency token, and tense.
     *
     * @param index The index of the token in the analysis result.
     * @param text The text of the token.
     * @param tag The part-of-speech tag of the token.
     * @param dependencyLabel The dependency label of the token.
     * @param dependencyToken The index of the token that this token depends on.
     * @param tense The tense of the verb in the token (e.g., PAST, PRESENT, FUTURE).
     */
    public AnalysisResultToken(int index, String text, String tag, String dependencyLabel, int dependencyToken, String tense) {
        this.index = index;
        this.text = text;
        this.tag = tag;
        this.tense = (tense.equals("TENSE_UNKNOWN")) ? "" : tense;
        this.dependencyLabel = dependencyLabel;
        this.dependencyToken = dependencyToken;
    }
    
    public int getIndex() {
        return index;
    }
    public String getText() {
        return text;
    }
    public String getTag() {
        return tag;
    }
    public String getTense() {
        return tense;
    }
    public String getDependencyLabel() {
        return dependencyLabel;
    }
    public int getDependencyToken() {
        return dependencyToken;
    }
}
