package nosensegenerator.nosense;

import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.parse.Parser;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GraphvizRenderer {

    private static final String DOT_FILE_PATH =
        "src/main/resources/static/graphs/(fileName).dot";
    private static final String PNG_FILE_PATH =
        "src/main/resources/static/images/(fileName).png";
    private static final String WEB_PATH = "/images/(fileName).png";

    public static String RenderDependencyGraph(String fileName)
        throws IOException {
        String graph_path = DOT_FILE_PATH.replace("(fileName)", fileName);
        String image_path = PNG_FILE_PATH.replace("(fileName)", fileName);

        // Load DOT string from file
        String dot = new String(Files.readAllBytes(Paths.get(graph_path)));

        Parser parser = new Parser();

        // Parse and render to PNG
        Graphviz.fromGraph(parser.read(dot))
            .render(Format.PNG)
            .toFile(new File(image_path));

        System.out.println("Graph rendered using Graphviz Java.");

        return WEB_PATH.replace("(fileName)", fileName);
    }
}
