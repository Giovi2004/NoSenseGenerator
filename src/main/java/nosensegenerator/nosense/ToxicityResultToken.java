package nosensegenerator.nosense;
/*
 * This class saves and gives the confidence or confidence persentage of the toxicity results.
 */
public class ToxicityResultToken {

    private String name;
    private Double confidence;
    /**
     * Constructor for ToxicityResultToken.
     *
     * @param name       The name of the toxicity result.
     * @param confidence The confidence level of the toxicity result, as a percentage (0.0 to 1.0).
     */
    public ToxicityResultToken(String name, Double confidence) {
        this.name = name;
        this.confidence = confidence;
    }

    public String getName() {
        return name;
    }

    public Double getConfidence() {
        return confidence;
    }
    // Returns the confidence percentage as an integer.
    public int getConfidencePercentage() {
        return (int) (confidence * 100);
    }
}
