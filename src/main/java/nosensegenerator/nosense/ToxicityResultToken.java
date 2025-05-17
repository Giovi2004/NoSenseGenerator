package nosensegenerator.nosense;

public class ToxicityResultToken {
    private String name;
    private Double confidence;
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
}
