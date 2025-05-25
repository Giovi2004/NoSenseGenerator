package nosensegenerator.nosense;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import org.junit.jupiter.api.Test;

public class GraphvizRendererTest {

    private static final String TMP_PATH = System.getProperty("java.io.tmpdir");
    private static final String FILE_NAME = "testGraph";

    @Test
    public void testRenderDependencyGraph() {
        try {
            Path source = Paths.get(getClass().getResource("testGraph.dot").toURI());
            Path targetDir = Paths.get(TMP_PATH);

            Files.copy(source, targetDir.resolve("testGraph.dot"), java.nio.file.StandardCopyOption.REPLACE_EXISTING);

            String result = GraphvizRenderer.RenderDependencyGraph(FILE_NAME);

            Path expectedImagePath = Paths.get(getClass().getResource("testGraph.png").toURI());
            Path actualImagePath = Paths.get(TMP_PATH + result);

            BufferedImage expectedImage = ImageIO.read(expectedImagePath.toFile());
            BufferedImage actualImage = ImageIO.read(actualImagePath.toFile());

            assertEquals(expectedImage.getWidth(), actualImage.getWidth(), "Widths differ");
            assertEquals(expectedImage.getHeight(), actualImage.getHeight(), "Heights differ");

            for (int y = 0; y < expectedImage.getHeight(); y++) {
                for (int x = 0; x < expectedImage.getWidth(); x++) {
                    assertEquals(expectedImage.getRGB(x, y), actualImage.getRGB(x, y),
                            "Pixel differs at (" + x + "," + y + ")");
                }
            }

            System.out.println("Rendered graph to: " + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
