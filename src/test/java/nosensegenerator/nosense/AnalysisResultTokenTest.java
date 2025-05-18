package nosensegenerator.nosense;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AnalysisResultTokenTest {
    private static AnalysisResultToken analysisResultToken;
    @BeforeAll
    public static void setUp() {
        analysisResultToken = new AnalysisResultToken(0, "text", "tag", "label", 1, "tense");
    }
    @Test
    public void testGetIndex() {
        assertEquals(0, analysisResultToken.getIndex());
    }
    @Test
    public void testGetText() {
        assertEquals("text", analysisResultToken.getText());
    }
    @Test
    public void testGetTag() {
        assertEquals("tag", analysisResultToken.getTag());
    }
    @Test
    public void testGetTense() {
        assertEquals("tense", analysisResultToken.getTense());
    }
    @Test
    public void testGetDependencyLabel() {
        assertEquals("label", analysisResultToken.getDependencyLabel());
    }
    @Test
    public void testGetDependencyToken() {
        assertEquals(1, analysisResultToken.getDependencyToken());
    }

}