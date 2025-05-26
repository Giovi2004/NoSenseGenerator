package nosensegenerator.nosense;

import java.util.ArrayList;

/**
 * This class is responsible for generating sentences based on templates.
 * It uses predefined templates and fills them with random nouns, verbs, and adjectives.
 * It also provides functionality to save terms from sentences into their respective lists.
 */
public class Generator {
    private ArrayList<String> templates;
    private Nouns nouns;
    private Verbs verbs;
    private Adjectives adjectives;
    private String filePath = "src/main/resources/terms/sentences.txt";

    /**
     * Constructor for Generator.
     * Initializes the templates, nouns, verbs, and adjectives by loading them from files.
     */
    public Generator() {
        this.templates = FileHandler.load(this.filePath);
        this.nouns = new Nouns();
        this.verbs = new Verbs();
        this.adjectives = new Adjectives();
    }
    /**
     * Returns a random template sentence from the loaded templates.
     */
    public String generateTemplateSentence() {
        int generatedNumber = (int) (Math.random() * (templates.size() - 1));
        String template = templates.get(generatedNumber);

        return template;
    }
    /**
     * Fills a template sentence with random nouns, verbs, and adjectives.
     * It replaces placeholders in the template with actual words from the respective lists.
     *
     * @param template The template sentence to fill.
     * @param inputSentence The input sentence to use for filling.
     * @param time The tense of the verbs to use (PAST, PRESENT, FUTURE).
     */
    public Sentence fillTemplateSentence(String template, Sentence inputSentence, String time) {
        String filledTemplate = template;
        while (filledTemplate.contains("[sentence]")) {
            filledTemplate = filledTemplate.replace("[sentence]", this.generateTemplateSentence());
        }
        ArrayList<String> inputVerbs = inputSentence.getVerbs(time);
        ArrayList<String> inputNouns = inputSentence.getNouns();
        ArrayList<String> inputAdjectives = inputSentence.getAdjectives();
        // Replace nouns in the template with random words
        while (filledTemplate.contains("[noun]")) {
            if (Math.random() < 0.5 && inputNouns.size() > 0) {
                filledTemplate = filledTemplate.replaceFirst("\\[noun\\]",
                        inputNouns.get((int) (Math.random() * inputNouns.size())));
            } else {
                filledTemplate = filledTemplate.replaceFirst("\\[noun\\]", nouns.getNoun());
            }
        }
        // Replace verbs in the template with random words
        while (filledTemplate.contains("[verb]")) {
            if (Math.random() < 0.5 && inputVerbs.size() > 0) {
                filledTemplate = filledTemplate.replaceFirst("\\[verb\\]",
                        inputVerbs.get((int) (Math.random() * (inputVerbs.size() - 1))));
            } else {
                filledTemplate = filledTemplate.replaceFirst("\\[verb\\]", verbs.getVerb(time));
            }
        }
        // Replace adjectives in the template with random words
        while (filledTemplate.contains("[adjective]")) {
            if (Math.random() < 0.5 && inputAdjectives.size() > 0) {
                filledTemplate = filledTemplate.replaceFirst("\\[adjective\\]",
                        inputAdjectives.get((int) (Math.random() * (inputAdjectives.size() - 1))));
            } else {
                filledTemplate = filledTemplate.replaceFirst("\\[adjective\\]", adjectives.getAdjective());
            }
        }
        filledTemplate = filledTemplate.toLowerCase();
        filledTemplate = filledTemplate.substring(0, 1).toUpperCase() + filledTemplate.substring(1);
        return new Sentence(filledTemplate);
    }
    /**
     * Saves the terms from the provided sentence to their respective lists and files.
     * It saves nouns, adjectives, and verbs in different tenses.
     *
     * @param input The sentence containing terms to save.
     * @return The number of terms saved.
     */
    public int saveFromSentence(Sentence input) {
        int savedCount = 0;
        savedCount+=nouns.save(input.getNouns());
        savedCount+=adjectives.save(input.getAdjectives());
        savedCount+=verbs.save("PAST", input.getVerbs("PAST"));
        savedCount+=verbs.save("PRESENT", input.getVerbs("PRESENT"));
        savedCount+=verbs.save("FUTURE", input.getVerbs("FUTURE"));
        return savedCount;
    }
}
