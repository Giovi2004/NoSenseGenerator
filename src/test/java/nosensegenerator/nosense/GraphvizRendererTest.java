package nosensegenerator.nosense;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

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
    private static final int TOLERANCE = 10; // RGB tolerance value

    private boolean areColorsSimilar(int rgb1, int rgb2, int tolerance) {
        int r1 = (rgb1 >> 16) & 0xff;
        int g1 = (rgb1 >> 8) & 0xff;
        int b1 = rgb1 & 0xff;

        int r2 = (rgb2 >> 16) & 0xff;
        int g2 = (rgb2 >> 8) & 0xff;
        int b2 = rgb2 & 0xff;

        return Math.abs(r1 - r2) <= tolerance &&
               Math.abs(g1 - g2) <= tolerance &&
               Math.abs(b1 - b2) <= tolerance;
    }

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

            int differentPixels = 0;
            for (int y = 0; y < expectedImage.getHeight(); y++) {
                for (int x = 0; x < expectedImage.getWidth(); x++) {
                    if (!areColorsSimilar(expectedImage.getRGB(x, y), actualImage.getRGB(x, y), TOLERANCE)) {
                        differentPixels++;
                    }
                }
            }

            // Allow up to 1% of pixels to be different within the tolerance
            int maxDifferentPixels = (expectedImage.getWidth() * expectedImage.getHeight()) / 100;
            assertTrue(differentPixels <= maxDifferentPixels, 
                "Too many different pixels: " + differentPixels + " (max allowed: " + maxDifferentPixels + ")");

            System.out.println("Rendered graph to: " + result);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Test failed due to exception: " + e.getMessage());
        }
    }
}
