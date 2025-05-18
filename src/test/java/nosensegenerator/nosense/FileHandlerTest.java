package nosensegenerator.nosense;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class FileHandlerTest {

    @BeforeAll
    public static void setUp(){
        try {
            Files.write(Paths.get("src/test/java/nosensegenerator/nosense/test.txt"), "test\n".getBytes());
        } catch (Exception e) {}
    }
         

    @Test
    public void saveloadTest() {
        ArrayList<String> List = new ArrayList<>();
        List.add("test");
        ArrayList<String> sampleList = new ArrayList<>();
        sampleList.add("test");
        sampleList.add("test1");
        sampleList.add("test2");
        sampleList.add("test3");

        FileHandler.save(List, sampleList, "src/test/java/nosensegenerator/nosense/test.txt");
        assertThat(FileHandler.load("src/test/java/nosensegenerator/nosense/test.txt") , hasItems("test", "test1", "test2", "test3"));
               
    }
    @Test
        public void saveVoidListTest() {
        ArrayList<String> List = new ArrayList<>();
        List.add("test");
        ArrayList<String> sampleList = new ArrayList<>();

        FileHandler.save(List, sampleList, "src/test/java/nosensegenerator/nosense/test.txt");
        assertThat(FileHandler.load("src/test/java/nosensegenerator/nosense/test.txt") , hasItem("test"));
    }
    
}