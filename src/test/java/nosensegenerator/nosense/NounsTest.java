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
public class NounsTest {
    
    @Test
    public void testGetNoun() {
        ArrayList<String> wordsFromFile = FileHandler.load("src/main/resources/terms/nouns.txt");
        Nouns noun = new Nouns();
        String generatedNoun = noun.getNoun();
        assertThat(wordsFromFile, hasItem(generatedNoun));
    }
    @Test
    public void testSave() {
        Nouns noun = new Nouns();
        ArrayList<String> nounsForFile = new ArrayList<>();
        nounsForFile.add("testNoun");
        int savedCount=noun.save(nounsForFile);
        assertEquals(savedCount, 1);
        ArrayList<String> loadedNouns = FileHandler.load("src/main/resources/terms/nouns.txt");
        assertEquals(loadedNouns.get(loadedNouns.size()-1), "testNoun");

        try {
            List<String> lines = Files.readAllLines(Paths.get("src/main/resources/terms/nouns.txt"));
        if (lines.size() >= 2) {
            lines.remove(lines.size() - 1);
            Files.write(Paths.get("src/main/resources/terms/nouns.txt"), lines);
        }

        } catch (Exception e) {}
        
    }
}