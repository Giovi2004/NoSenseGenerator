package nosensegenerator.nosense;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class GraphvizGeneratorTest {

    private static final String FILE_PATH = System.getProperty("java.io.tmpdir");
    private static final String EXPECTED_FILE = "testGraph.dot";

    @Test
    public void testGenerateGraphviz() {
        String fileName = "testGraph";
        ArrayList<AnalysisResultToken> tokens = new ArrayList<>(List.of(
                new AnalysisResultToken(0, "dog", "NOUN", "nsubj", 1, "TENSE_UNKNOWN"),
                new AnalysisResultToken(1, "eats", "VERB", "ROOT", 0, "PRESENT"),
                new AnalysisResultToken(2, "happy", "ADJ", "amod", 1, "TENSE_UNKNOWN"),
                new AnalysisResultToken(3, "ate", "VERB", "ROOT", 0, "PAST"),
                new AnalysisResultToken(4, "will eat", "VERB", "ROOT", 0, "FUTURE")));

        GraphvizGenerator.GenerateDependencyGraph(tokens, fileName);

        try {
            String dot = new String(Files.readAllBytes(Paths.get(FILE_PATH + "/" + fileName + ".dot")));
            Path path = Paths.get(getClass().getResource(EXPECTED_FILE).toURI());
            String expectedString = new String(Files.readAllBytes(path));
            assertEquals(expectedString, dot);
        } catch (Exception e) {
            System.err.println("Error reading DOT file: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
