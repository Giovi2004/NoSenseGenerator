public class verb extends word {
    private String verb;
    private String tense;
    private String aspect;

    public verb(String verb, String tense, String aspect) {
        this.verb = verb;
        this.tense = tense;
        this.aspect = aspect;
    }

    public String getVerb() {
        return verb;
    }

    public void setVerb(String verb) {
        this.verb = verb;
    }

    public String getTense() {
        return tense;
    }

    public void setTense(String tense) {
        this.tense = tense;
    }

    public String getAspect() {
        return aspect;
    }

    public void setAspect(String aspect) {
        this.aspect = aspect;
    }
}