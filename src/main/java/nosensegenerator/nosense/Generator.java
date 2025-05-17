package nosensegenerator.nosense;

import java.util.ArrayList;

public class Generator {
    private ArrayList<String> templates;
    private Noun nouns;
    private Verb verbs;
    private Adjective adjectives;
    private String FilePath = "src/main/resources/terms/sentences.txt";

    public Generator() {
        this.templates = FileHandler.load(this.FilePath);
        this.nouns = new Noun();
        this.verbs = new Verb();
        this.adjectives = new Adjective();
    }

    public String generateTemplateSentence() {
        int generatedNumber = (int) (Math.random() * (templates.size() - 1));
        String template = templates.get(generatedNumber);

        return template;
    }

    public Sentence fillTemplateSentence(String template, Sentence inputSentence, String time) {
        String filledTemplate = template;
        while (filledTemplate.contains("[sentence]")) {
            filledTemplate = filledTemplate.replace("[sentence]", this.generateTemplateSentence());
        }
        ArrayList<String> inputVerbs = inputSentence.getVerbs(time);
        ArrayList<String> inputNouns = inputSentence.getNouns();
        ArrayList<String> inputAdjectives = inputSentence.getAdjectives();

        while (filledTemplate.contains("[noun]")) {
            if (Math.random() < 0.5 && inputNouns.size() > 0) {
                filledTemplate = filledTemplate.replaceFirst("[noun]",
                        inputNouns.get((int) (Math.random() * (inputNouns.size() - 1))));
            } else {
                filledTemplate = filledTemplate.replaceFirst("[noun]", nouns.getnoun());
            }
        }
        while (filledTemplate.contains("[verb]")) {
            if (Math.random() < 0.5 && inputVerbs.size() > 0) {
                filledTemplate = filledTemplate.replaceFirst("[verb]",
                        inputVerbs.get((int) (Math.random() * (inputVerbs.size() - 1))));
            } else {
                filledTemplate = filledTemplate.replaceFirst("[verb]", verbs.getverb(time));
            }
        }
        while (filledTemplate.contains("[adjective]")) {
            if (Math.random() < 0.5 && inputAdjectives.size() > 0) {
                filledTemplate = filledTemplate.replaceFirst("[adjective]",
                        inputAdjectives.get((int) (Math.random() * (inputAdjectives.size() - 1))));
            } else {
                filledTemplate = filledTemplate.replaceFirst("[adjective]", adjectives.getAdjective());
            }
        }
        return new Sentence(filledTemplate);
    }

    public void saveFromSentence(Sentence input) {
        nouns.save(input.getNouns());
        adjectives.save(input.getAdjectives());
        verbs.save("PAST", input.getVerbs("PAST"));
        verbs.save("PRESENT", input.getVerbs("PRESENT"));
        verbs.save("FUTURE", input.getVerbs("FUTURE"));
    }
}
