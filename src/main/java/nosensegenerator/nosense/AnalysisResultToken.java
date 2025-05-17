package nosensegenerator.nosense;

public class AnalysisResultToken {
    private int index;
    private String text;
    private String tag;
    private String tense;
    private String dependencyLabel;
    private int dependencyToken;
    public AnalysisResultToken(int index, String text, String tag, String dependencyLabel, int dependencyToken, String tense) {
        this.index = index;
        this.text = text;
        this.tag = tag;
        this.tense = (tense != "TENSE_UNKNOWN") ? tense : "";
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
