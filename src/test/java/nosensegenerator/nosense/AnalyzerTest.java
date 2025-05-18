package nosensegenerator.nosense;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AnalyzerTest {

    @Test
    void analyzeSyntaxTest() {
        // Test the analyzeSyntax method
        String sentence = "This is a test sentence.";
        ArrayList<AnalysisResultToken> result = Analyzer.analyzeSyntax(sentence);
        ArrayList<AnalysisResultToken> expected = new ArrayList<>();
        expected.add(new AnalysisResultToken(0,"this", "DET", "NSUBJ", 1,"TENSE_UNKNOWN"));
        expected.add(new AnalysisResultToken(1,"is", "VERB", "ROOT", 1,"PRESENT"));
        expected.add(new AnalysisResultToken(2,"a", "DET", "DET", 4,"TENSE_UNKNOWN"));
        expected.add(new AnalysisResultToken(3,"test", "NOUN", "NN", 4,"TENSE_UNKNOWN"));
        expected.add(new AnalysisResultToken(4,"sentence", "NOUN", "ATTR", 1,"TENSE_UNKNOWN"));
        expected.add(new AnalysisResultToken(5,".", "PUNCT", "P", 1,"TENSE_UNKNOWN"));
        for (int i = 0; i < result.size(); i++) {
            AnalysisResultToken token = result.get(i);
            AnalysisResultToken expectedToken = expected.get(i);
            assertEquals(token.getIndex(), expectedToken.getIndex());
            assertEquals(token.getText(), expectedToken.getText());
            assertEquals(token.getTag(), expectedToken.getTag());
            assertEquals(token.getDependencyLabel(), expectedToken.getDependencyLabel());
            assertEquals(token.getDependencyToken(), expectedToken.getDependencyToken());
            assertEquals(token.getTense(), expectedToken.getTense());
        }
    }
    @Test
    void analyzeToxicityTest() {
        // Test the analyzeToxicity method
        String sentence = "This is a test sentence.";
        ArrayList<ToxicityResultToken> result = Analyzer.analyzeToxicity(sentence);
        ArrayList<ToxicityResultToken> expected = new ArrayList<>();
        expected.add(new ToxicityResultToken("Toxic", 0.018770736));
        expected.add(new ToxicityResultToken("Insult", 0.012034906));
        expected.add(new ToxicityResultToken("Profanity", 0.0052250074));
        expected.add(new ToxicityResultToken("Derogatory", 0.0047925273));
        expected.add(new ToxicityResultToken("Sexual", 0.0024701601));
        expected.add(new ToxicityResultToken("Death, Harm & Tragedy", 0.005982054));
        expected.add(new ToxicityResultToken("Violent", 0.0025839794));
        expected.add(new ToxicityResultToken("Firearms & Weapons", 0.0));
        expected.add(new ToxicityResultToken("Public Safety", 0.010695187));
        expected.add(new ToxicityResultToken("Health", 0.03495007));
        expected.add(new ToxicityResultToken("Religion & Belief", 0.012507818));
        expected.add(new ToxicityResultToken("Illicit Drugs", 0.00955414));
        expected.add(new ToxicityResultToken("War & Conflict", 0.008333334));
        expected.add(new ToxicityResultToken("Politics", 0.0028191702));
        expected.add(new ToxicityResultToken("Finance", 0.007633588));
        expected.add(new ToxicityResultToken("Legal", 0.0029382957));
        for (int i = 0; i < result.size(); i++) {
            ToxicityResultToken token = result.get(i);
            ToxicityResultToken expectedToken = expected.get(i);
            assertEquals(token.getName(), expectedToken.getName());
            assertEquals(token.getConfidence(), expectedToken.getConfidence());
        }
    }
    
}
