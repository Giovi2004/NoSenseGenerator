package nosensegenerator.nosense;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

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
            String expectedString = "digraph G {\n" + //
                                "    graph [splines=true];\n" + //
                                "    nodesep=1;\n" + //
                                "    ranksep=2;\n" + //
                                "    0 [nojustify=true shape=box label=<\n" + //
                                "        <TABLE BORDER=\"0\" CELLBORDER=\"1\" CELLSPACING=\"0\" CELLPADDING=\"4\">\n" + //
                                "            <TR><TD><FONT COLOR=\"red\">nsubj</FONT></TD></TR>\n" + //
                                "            <TR><TD><FONT POINT-SIZE=\"12\">dog</FONT></TD></TR>\n" + //
                                "            <TR><TD><FONT FACE=\"Courier\">NOUN</FONT></TD></TR>\n" + //
                                "        </TABLE>\n" + //
                                "    >];\n" + //
                                "    1 [nojustify=true shape=box label=<\n" + //
                                "        <TABLE BORDER=\"0\" CELLBORDER=\"1\" CELLSPACING=\"0\" CELLPADDING=\"4\">\n" + //
                                "            <TR><TD><FONT COLOR=\"red\">ROOT</FONT></TD></TR>\n" + //
                                "            <TR><TD><FONT POINT-SIZE=\"12\">eats</FONT></TD></TR>\n" + //
                                "            <TR><TD><FONT FACE=\"Courier\">VERB</FONT></TD></TR>\n" + //
                                "        </TABLE>\n" + //
                                "    >];\n" + //
                                "    2 [nojustify=true shape=box label=<\n" + //
                                "        <TABLE BORDER=\"0\" CELLBORDER=\"1\" CELLSPACING=\"0\" CELLPADDING=\"4\">\n" + //
                                "            <TR><TD><FONT COLOR=\"red\">amod</FONT></TD></TR>\n" + //
                                "            <TR><TD><FONT POINT-SIZE=\"12\">happy</FONT></TD></TR>\n" + //
                                "            <TR><TD><FONT FACE=\"Courier\">ADJ</FONT></TD></TR>\n" + //
                                "        </TABLE>\n" + //
                                "    >];\n" + //
                                "    3 [nojustify=true shape=box label=<\n" + //
                                "        <TABLE BORDER=\"0\" CELLBORDER=\"1\" CELLSPACING=\"0\" CELLPADDING=\"4\">\n" + //
                                "            <TR><TD><FONT COLOR=\"red\">ROOT</FONT></TD></TR>\n" + //
                                "            <TR><TD><FONT POINT-SIZE=\"12\">ate</FONT></TD></TR>\n" + //
                                "            <TR><TD><FONT FACE=\"Courier\">VERB</FONT></TD></TR>\n" + //
                                "        </TABLE>\n" + //
                                "    >];\n" + //
                                "    4 [nojustify=true shape=box label=<\n" + //
                                "        <TABLE BORDER=\"0\" CELLBORDER=\"1\" CELLSPACING=\"0\" CELLPADDING=\"4\">\n" + //
                                "            <TR><TD><FONT COLOR=\"red\">ROOT</FONT></TD></TR>\n" + //
                                "            <TR><TD><FONT POINT-SIZE=\"12\">will eat</FONT></TD></TR>\n" + //
                                "            <TR><TD><FONT FACE=\"Courier\">VERB</FONT></TD></TR>\n" + //
                                "        </TABLE>\n" + //
                                "    >];\n" + //
                                "\n" + //
                                "    1:n -> 10:_ [arrowhead=none, dir=back, arrowtail=invodot, color=aqua, penwidth=1.5];\n" + //
                                "    10:_ -> 0:n [color=aqua, penwidth=1.5];\n" + //
                                "    0:n -> 01:_ [arrowhead=none, dir=back, arrowtail=invodot, color=aquamarine, penwidth=1.5];\n" + //
                                "    01:_ -> 1:n [color=aquamarine, penwidth=1.5];\n" + //
                                "    1:n -> 12:_ [arrowhead=none, dir=back, arrowtail=invodot, color=blue, penwidth=1.5];\n" + //
                                "    12:_ -> 2:n [color=blue, penwidth=1.5];\n" + //
                                "    0:n -> 03:_ [arrowhead=none, dir=back, arrowtail=invodot, color=brown, penwidth=1.5];\n" + //
                                "    03:_ -> 3:n [color=brown, penwidth=1.5];\n" + //
                                "    0:n -> 04:_ [arrowhead=none, dir=back, arrowtail=invodot, color=orange, penwidth=1.5];\n" + //
                                "    04:_ -> 4:n [color=orange, penwidth=1.5];\n" + //
                                "\n" + //
                                "    {\n" + //
                                "        rank=min;\n" + //
                                "        10 [shape=point, width=0.02, height=0.02, label=\"\", color=aqua];\n" + //
                                "        01 [shape=point, width=0.02, height=0.02, label=\"\", color=aquamarine];\n" + //
                                "        12 [shape=point, width=0.02, height=0.02, label=\"\", color=blue];\n" + //
                                "        03 [shape=point, width=0.02, height=0.02, label=\"\", color=brown];\n" + //
                                "        04 [shape=point, width=0.02, height=0.02, label=\"\", color=orange];\n" + //
                                "\n" + //
                                "    }\n" + //
                                "    {\n" + //
                                "        rank=same;\n" + //
                                "        edge[style=invis];\n" + //
                                "        0:e -> 1:w ;\n" + //
                                "        1:e -> 2:w ;\n" + //
                                "        2:e -> 3:w ;\n" + //
                                "        3:e -> 4:w ;\n" + //
                                "    }\n" + //
                                "}";
            assertEquals(expectedString, dot);
        } catch (Exception e) {
            System.err.println("Error reading DOT file: " + e.getMessage());
            e.printStackTrace();
            fail("Test failed due to exception: " + e.getMessage());
        }
    }

}
