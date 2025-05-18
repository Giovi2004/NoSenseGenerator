package nosensegenerator.nosense;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class GeneratorTest {
    @BeforeAll
    public static void setUp() {

    }

    @Test
    public void testGenerateTemplateSentence() {
        ArrayList<String> sentencesFromFile = FileHandler.load("src/main/resources/terms/sentences.txt");
        Generator generator = new Generator();
        String generatedTemplate = generator.generateTemplateSentence();
        assertThat(sentencesFromFile, hasItem(generatedTemplate));
    }

    @Test
    public void testFillTemplateSentence() {
        Generator generator = new Generator();
        String template = "The [noun] [verb] under the [adjective] [noun]";
        Sentence inputSentence = new Sentence("This is a nice test sentence.");
        ArrayList<AnalysisResultToken> expected = new ArrayList<>();
        expected.add(new AnalysisResultToken(0, "This", "DET", "NSUBJ", 1, "TENSE_UNKNOWN"));
        expected.add(new AnalysisResultToken(1, "is", "VERB", "ROOT", 1, "PRESENT"));
        expected.add(new AnalysisResultToken(2, "a", "DET", "DET", 4, "TENSE_UNKNOWN"));
        expected.add(new AnalysisResultToken(3, "nice", "ADJ", "ADJ", 4, "TENSE_UNKNOWN"));
        expected.add(new AnalysisResultToken(4, "test", "NOUN", "NN", 4, "TENSE_UNKNOWN"));
        expected.add(new AnalysisResultToken(5, "sentence", "NOUN", "ATTR", 1, "TENSE_UNKNOWN"));
        expected.add(new AnalysisResultToken(6, ".", "PUNCT", "P", 1, "TENSE_UNKNOWN"));
        inputSentence.setAnalysisResultTokens(expected);
        testSentence(template, "PRESENT", inputSentence, generator);
        testSentence(template, "FUTURE", inputSentence, generator);
        testSentence(template, "PAST", inputSentence, generator);

    }

    public void testSentence(String template, String time, Sentence inputSentence, Generator generator) {
        ArrayList<String> verbs = FileHandler.load("src/main/resources/terms/verbs" + time + ".txt");
        ArrayList<String> nouns = FileHandler.load("src/main/resources/terms/nouns.txt");
        ArrayList<String> adjectives = FileHandler.load("src/main/resources/terms/adjectives.txt");

        Sentence filledTemplate = generator.fillTemplateSentence(template, inputSentence, time);
        String filledText = filledTemplate.getText();
        ArrayList<String> filledWords = new ArrayList<>();
        String[] words = filledText.replaceAll("[^a-zA-Z\\s]", "").split("\\s+");
        for (int i = 0; i < words.length; i++) {
            if (words[i].equalsIgnoreCase("will") && i + 1 < words.length) {
                filledWords.add(words[i] + " " + words[i + 1]);
                i++;
            } else {
                filledWords.add(words[i]);
            }
        }
        Set<String> allowedWords = new HashSet<>();
        allowedWords.add("The");
        allowedWords.add("under");
        allowedWords.add("the");
        allowedWords.add("is");
        allowedWords.add("a");
        allowedWords.add("test");
        allowedWords.add("sentence");
        allowedWords.add("This");
        allowedWords.add("nice");
        for (String verb : verbs)
            allowedWords.add(verb);
        for (String noun : nouns)
            allowedWords.add(noun);
        for (String adj : adjectives)
            allowedWords.add(adj);

        for (String word : filledWords) {
            if (!word.isEmpty()) {
                assertTrue(allowedWords.contains(word));
            }
        }
        assertFalse(filledWords.contains("[noun]"));
        assertFalse(filledWords.contains("[verb]"));
        assertFalse(filledWords.contains("[adjective]"));
    }

    @Test
    public void testSaveFromSentence() {
        Generator generator = new Generator();
        Sentence inputSentence = new Sentence("This is a test sentence.");
        ArrayList<AnalysisResultToken> expected = new ArrayList<>();
        expected.add(new AnalysisResultToken(0, "testadj", "ADJ", "NSUBJ", 1, "TENSE_UNKNOWN"));
        expected.add(new AnalysisResultToken(1, "testpresent", "VERB", "ROOT", 1, "PRESENT"));
        expected.add(new AnalysisResultToken(1, "testpast", "VERB", "ROOT", 1, "PAST"));
        expected.add(new AnalysisResultToken(2, "testnoun", "NOUN", "NN", 4, "TENSE_UNKNOWN"));
        expected.add(new AnalysisResultToken(3, "testfuture", "VERB", "ATTR", 1, "FUTURE"));
        inputSentence.setAnalysisResultTokens(expected);
        generator.saveFromSentence(inputSentence);
        // cleaning files after test
        ArrayList<String> loadedNouns = FileHandler.load("src/main/resources/terms/nouns.txt");
        assertEquals(loadedNouns.get(loadedNouns.size() - 1), "testnoun");

        try {
            List<String> lines = Files.readAllLines(Paths.get("src/main/resources/terms/nouns.txt"));
            if (lines.size() >= 2) {
                lines.remove(lines.size() - 1);
                Files.write(Paths.get("src/main/resources/terms/nouns.txt"), lines);
                System.out.println("Penultimate line deleted successfully.");
            }

        } catch (Exception e) {
        }
        ArrayList<String> loadedAdjectives = FileHandler.load("src/main/resources/terms/adjectives.txt");
        assertEquals(loadedAdjectives.get(loadedAdjectives.size() - 1), "testadj");
        try {
            List<String> lines = Files.readAllLines(Paths.get("src/main/resources/terms/adjectives.txt"));
            if (lines.size() >= 2) {
                lines.remove(lines.size() - 1);
                Files.write(Paths.get("src/main/resources/terms/adjectives.txt"), lines);
                System.out.println("Penultimate line deleted successfully.");
            }

        } catch (Exception e) {
        }
        ArrayList<String> loadedVerbs = FileHandler.load("src/main/resources/terms/verbsPast.txt");
        assertEquals(loadedVerbs.get(loadedVerbs.size() - 1), "testpast");
        try {
            List<String> lines = Files.readAllLines(Paths.get("src/main/resources/terms/verbsPast.txt"));
            if (lines.size() >= 2) {
                lines.remove(lines.size() - 1);
                Files.write(Paths.get("src/main/resources/terms/verbsPast.txt"), lines);
                System.out.println("Penultimate line deleted successfully.");
            }

        } catch (Exception e) {
        }
        ArrayList<String> loadedVerbsPresent = FileHandler.load("src/main/resources/terms/verbsPresent.txt");
        assertEquals(loadedVerbsPresent.get(loadedVerbsPresent.size() - 1), "testpresent");
        try {
            List<String> lines = Files.readAllLines(Paths.get("src/main/resources/terms/verbsPresent.txt"));
            if (lines.size() >= 2) {
                lines.remove(lines.size() - 1);
                Files.write(Paths.get("src/main/resources/terms/verbsPresent.txt"), lines);
                System.out.println("Penultimate line deleted successfully.");
            }

        } catch (Exception e) {
        }
        ArrayList<String> loadedVerbsFuture = FileHandler.load("src/main/resources/terms/verbsFuture.txt");
        assertEquals(loadedVerbsFuture.get(loadedVerbsFuture.size() - 1), "testfuture");
        try {
            List<String> lines = Files.readAllLines(Paths.get("src/main/resources/terms/verbsFuture.txt"));
            if (lines.size() >= 2) {
                lines.remove(lines.size() - 1);
                Files.write(Paths.get("src/main/resources/terms/verbsFuture.txt"), lines);
                System.out.println("Penultimate line deleted successfully.");
            }

        } catch (Exception e) {
        }

    }
}