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
        expected.add(new AnalysisResultToken(0,"This", "DET", "NSUBJ", 1,"TENSE_UNKNOWN"));
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
    }
    
}
