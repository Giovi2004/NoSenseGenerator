package nosensegenerator.nosense;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AdjectiveTest {
    
    @Test
    public void testGetAdjective() {
        ArrayList<String> wordsFromFile = FileHandler.load("src/main/resources/terms/adjectives.txt");
        Adjective adjective = new Adjective();
        String generatedAdjective = adjective.getAdjective();
        assertThat(wordsFromFile, hasItem(generatedAdjective));
    }
    @Test
    public void testSave() {
        Adjective adjective = new Adjective();
        ArrayList<String> adjectivesForFile = new ArrayList<>();
        adjectivesForFile.add("testAdjective");
        adjective.save(adjectivesForFile);
        ArrayList<String> loadedAdjectives = FileHandler.load("src/main/resources/terms/adjectives.txt");
        assertEquals(loadedAdjectives.get(loadedAdjectives.size()-1), "testAdjective");

        try {
            List<String> lines = Files.readAllLines(Paths.get("src/main/resources/terms/adjectives.txt"));
        if (lines.size() >= 2) {
            lines.remove(lines.size() - 1);
            Files.write(Paths.get("src/main/resources/terms/adjectives.txt"), lines);
        }

        } catch (Exception e) {}
        
    }
}