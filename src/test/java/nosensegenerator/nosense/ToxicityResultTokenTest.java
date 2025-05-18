package nosensegenerator.nosense;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ToxicityResultTokenTest {
    private static ToxicityResultToken toxicityResultToken;
    @BeforeAll
    public static void setUp() {
        toxicityResultToken = new ToxicityResultToken("test",0.5);
    }
    @Test
    public void testGetName() {
        assertEquals("test", toxicityResultToken.getName());
    }
    @Test
    public void testGetText() {
        assertEquals(0.5, toxicityResultToken.getConfidence());
    }
    @Test
    public void testGetConfidencePercentage() {
        assertEquals(50.0, toxicityResultToken.getConfidencePercentage());
    }
}