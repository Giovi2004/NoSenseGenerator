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
public class VerbTest {
    
    @Test
    public void testGetVerb() {
        Verb verb = new Verb();
        ArrayList<String> wordsFromFilePresent = FileHandler.load("src/main/resources/terms/verbsPresent.txt");
        ArrayList<String> wordsFromFilePast = FileHandler.load("src/main/resources/terms/verbsPast.txt");
        ArrayList<String> wordsFromFileFuture = FileHandler.load("src/main/resources/terms/verbsFuture.txt");
        String generatedVerbPresent = verb.getVerb("PRESENT");
        String generatedVerbPast = verb.getVerb("PAST");
        String generatedVerbFuture = verb.getVerb("FUTURE");
        String generatedVerb = verb.getVerb("");
        assertThat(wordsFromFilePast, hasItem(generatedVerbPast));
        assertThat(wordsFromFilePresent, hasItem(generatedVerbPresent));
        assertThat(wordsFromFileFuture, hasItem(generatedVerbFuture));
        assertThat(wordsFromFilePresent, hasItem(generatedVerb));
    }
    @Test
    public void testSave() {
        Verb verb = new Verb();

        ArrayList<String> verbsForFile = new ArrayList<>();
        verbsForFile.add("testVerb");
        verb.save("PRESENT",verbsForFile);
        ArrayList<String> loadedVerbsPresent = FileHandler.load("src/main/resources/terms/verbsPresent.txt");
        assertEquals(loadedVerbsPresent.get(loadedVerbsPresent.size()-1), "testVerb");
        verb.save("PAST",verbsForFile);
        ArrayList<String> loadedVerbsPast = FileHandler.load("src/main/resources/terms/verbsPast.txt");
        assertEquals(loadedVerbsPast.get(loadedVerbsPast.size()-1), "testVerb");
        verb.save("FUTURE",verbsForFile);
        ArrayList<String> loadedVerbsFuture = FileHandler.load("src/main/resources/terms/verbsFuture.txt");
        assertEquals(loadedVerbsFuture.get(loadedVerbsFuture.size()-1), "testVerb");
        verb.save("",verbsForFile);
        ArrayList<String> loadedVerbs = FileHandler.load("src/main/resources/terms/verbsPresent.txt");
        assertEquals(loadedVerbs.get(loadedVerbs.size()-1), "testVerb");

        try {
            List<String> lines = Files.readAllLines(Paths.get("src/main/resources/terms/verbsPresent.txt"));
            if (lines.size() >= 2) {
                lines.remove(lines.size() - 1);
                lines.remove(lines.size() - 1);
                Files.write(Paths.get("src/main/resources/terms/verbsPresent.txt"), lines);
            }
        } catch (Exception e) {}
        try {
            List<String> lines = Files.readAllLines(Paths.get("src/main/resources/terms/verbsPast.txt"));
            if (lines.size() >= 2) {
                lines.remove(lines.size() - 1);
                Files.write(Paths.get("src/main/resources/terms/verbsPast.txt"), lines);
            }
        }catch (Exception e) {}
        try{
            List<String> lines = Files.readAllLines(Paths.get("src/main/resources/terms/verbsFuture.txt"));
            if (lines.size() >= 2) {
                lines.remove(lines.size() - 1);
                Files.write(Paths.get("src/main/resources/terms/verbsFuture.txt"), lines);
            }
        }catch (Exception e) {}
    }
}