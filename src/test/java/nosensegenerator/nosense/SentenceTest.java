package nosensegenerator.nosense;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SentenceTest {
    private static Sentence sentence;
    private static AnalysisResultToken token;
    private static AnalysisResultToken token2;
    private static AnalysisResultToken token3;
    private static AnalysisResultToken token4;
    private static AnalysisResultToken token5;
    @BeforeAll
    public static void setUp() {
        sentence = new Sentence("This is a test sentence.");
        token = new AnalysisResultToken(0, "test", "NOUN", "label", 1, "tense");
        token2 = new AnalysisResultToken(1, "test2", "VERB", "label2", 2, "FUTURE");
        token3 = new AnalysisResultToken(2, "test3", "ADJ", "label3", 3, "tense3");
        token4 = new AnalysisResultToken(1, "test4", "VERB", "label2", 2, "PRESENT");
        token5 = new AnalysisResultToken(2, "test5", "VERB", "label3", 3, "PAST");
    }
    @Test
    public void testGetText() {
        assertEquals("This is a test sentence.", sentence.getText());
    }
    @Test
    public void testSetText() {
        sentence.setText("This is a new test sentence.");
        assertEquals("This is a new test sentence.", sentence.getText());
    }
    @Test
    public void testGetSetAnalysisResultTokens() {
        ArrayList<AnalysisResultToken> tokens = new ArrayList<>();
        tokens.add(token);
        tokens.add(token2);
        tokens.add(token3);
        tokens.add(token4);
        tokens.add(token5);
        sentence.setAnalysisResultTokens(tokens);
        assertEquals(tokens, sentence.getAnalysisResultTokens());
    }
    @Test
    public void testGetVerbs() {
        ArrayList<AnalysisResultToken> tokens = new ArrayList<>();
        tokens.add(token);
        tokens.add(token2);
        tokens.add(token3);
        tokens.add(token4);
        tokens.add(token5);
        sentence.setAnalysisResultTokens(tokens);
        ArrayList<String> verbs = sentence.getVerbs("FUTURE");
        assertEquals(1, verbs.size());
        assertThat(verbs, hasItem("test2"));
        ArrayList<String> verbs2 = sentence.getVerbs("PRESENT");
        assertEquals(1, verbs2.size());
        assertThat(verbs2, hasItem("test4"));
        ArrayList<String> verbs3 = sentence.getVerbs("PAST");
        assertEquals(1, verbs3.size());
        assertThat(verbs3, hasItem("test5"));
        ArrayList<String> verbs4 = sentence.getVerbs("tense");
        assertEquals(0, verbs4.size());
    }

    @Test
    public void testGetNouns() {
        ArrayList<AnalysisResultToken> tokens = new ArrayList<>();
        tokens.add(token);
        tokens.add(token2);
        tokens.add(token3);
        tokens.add(token4);
        tokens.add(token5);
        sentence.setAnalysisResultTokens(tokens);
        ArrayList<String> nouns = sentence.getNouns();
        assertEquals(1, nouns.size());
        assertThat(nouns, hasItem("test"));
    }
    @Test
    public void testGetAdjectives() {
        ArrayList<AnalysisResultToken> tokens = new ArrayList<>();
        tokens.add(token);
        tokens.add(token2);
        tokens.add(token3);
        tokens.add(token4);
        tokens.add(token5);
        sentence.setAnalysisResultTokens(tokens);
        ArrayList<String> adjectives = sentence.getAdjectives();
        assertEquals(1, adjectives.size());
        assertThat(adjectives, hasItem("test3"));
    }
    @Test
    public void testSetGetToxicityResultTokens() {
        ArrayList<ToxicityResultToken> tokens = new ArrayList<>();
        ToxicityResultToken token1 = new ToxicityResultToken("test", 0.5);
        tokens.add(token1);
        sentence.setToxicityResultTokens(tokens);
        assertEquals(tokens, sentence.getToxicityResultTokens());
    }

}