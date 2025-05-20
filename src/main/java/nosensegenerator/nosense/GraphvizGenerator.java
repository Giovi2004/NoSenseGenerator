package nosensegenerator.nosense;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GraphvizGenerator {

    private static final String TAB = "    ";
    private static final String FILE_PATH =
        System.getProperty("java.io.tmpdir") + "/(fileName).dot";
    private static final String NODE_TEMPLATE =
        "(index) [nojustify=true shape=box (label)];\n";
    private static final String LABEL_TEMPLATE =
        "label=<\n" +
        TAB +
        TAB +
        "<TABLE BORDER=\"0\" CELLBORDER=\"1\" CELLSPACING=\"0\" CELLPADDING=\"4\">\n" +
        TAB +
        TAB +
        TAB +
        "<TR><TD><FONT COLOR=\"red\">(dependencyLabel)</FONT></TD></TR>\n" +
        TAB +
        TAB +
        TAB +
        "<TR><TD><FONT POINT-SIZE=\"12\">(text)</FONT></TD></TR>\n" +
        TAB +
        TAB +
        TAB +
        "<TR><TD><FONT FACE=\"Courier\">(tag)</FONT></TD></TR>\n" +
        TAB +
        TAB +
        "</TABLE>\n" +
        TAB +
        ">";
    private static final String EDGE_TEMPLATE =
        "(tail):(tailPort) -> (head):(headPort) ([options]);\n";
    private static final String CONTROL_NODE =
        "(index) [shape=point, width=0.01, height=0.01, label=\"\", color=(color)];\n";
    private static final String GRAPH_TEMPLATE =
        "digraph G {\n" +
        TAB +
        "graph [splines=true];\n" +
        TAB +
        "nodesep=1;\n" +
        TAB +
        "ranksep=2;\n" +
        "(nodes)\n" +
        "(dependencies)\n" +
        TAB +
        "{\n" +
        TAB +
        TAB +
        "rank=min;\n" +
        "(controlNodes)\n" +
        TAB +
        "}\n" +
        TAB +
        "{\n" +
        TAB +
        TAB +
        "rank=same;\n" +
        TAB +
        TAB +
        "edge[style=invis];\n" +
        "(subgraph)" +
        TAB +
        "}\n" +
        "}";

    private static final List<String> colors = List.of(
        "aqua",
        "aquamarine",
        "blue",
        "brown",
        "orange",
        "red",
        "yellow",
        "turquoise",
        "gold",
        "green",
        "magenta",
        "cyan",
        "navy",
        "violet",
        "teal",
        "orangered",
        "royalblue",
        "crimson",
        "deeppink",
        "deepskyblue",
        "lime",
        "olive",
        "forestgreen",
        "fuchsia",
        "indigo",
        "webpurple"
    );

    public static void GenerateDependencyGraph(
        ArrayList<AnalysisResultToken> tokens,
        String fileName
    ) {
        String graph = "";
        String nodes = "";
        String dependencies = "";
        String subgraph = "";
        String controlNodes = "";
        int color_index = 0;

        for (AnalysisResultToken token : tokens) {
            nodes = nodes + TAB + FillNodeTemplate(token);
        }

        for (AnalysisResultToken token : tokens) {
            if (token.getDependencyToken() != token.getIndex()) {
                dependencies =
                    dependencies +
                    TAB +
                    ConnectWithControlNode(token, colors.get(color_index));
                controlNodes =
                    controlNodes +
                    TAB +
                    TAB +
                    FillControlNode(token, colors.get(color_index));

                color_index = getNextColor(color_index);
            }
        }

        for (int index = 0; index < tokens.size() - 1; index++) {
            subgraph =
                subgraph +
                TAB +
                TAB +
                FillEdgeTemplate(
                    Integer.toString(tokens.get(index).getIndex()),
                    Integer.toString(tokens.get(index + 1).getIndex()),
                    "e",
                    "w",
                    ""
                );
        }

        graph = FillGraphTemplate(nodes, dependencies, subgraph, controlNodes);

        String path = FILE_PATH.replace("(fileName)", fileName);

        // Try-with-resources ensures the FileWriter is closed automatically
        try (FileWriter writer = new FileWriter(path)) {
            writer.write(graph);
            System.out.println("Graphviz content written to " + path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String FillNodeTemplate(AnalysisResultToken token) {
        String _node = NODE_TEMPLATE;

        _node = _node.replace("(index)", Integer.toString(token.getIndex()));
        _node = _node.replace("(label)", FillLabelTemplate(token));

        return _node;
    }

    private static String FillLabelTemplate(AnalysisResultToken token) {
        String _label = LABEL_TEMPLATE;

        _label = _label.replace(
            "(dependencyLabel)",
            token.getDependencyLabel()
        );
        _label = _label.replace("(text)", token.getText());
        _label = _label.replace("(tag)", token.getTag());

        return _label;
    }

    private static String FillEdgeTemplate(
        String tail,
        String head,
        String tailPort,
        String headPort,
        String options
    ) {
        String _edge = EDGE_TEMPLATE;

        _edge = _edge.replace("(tail)", tail);
        _edge = _edge.replace("(head)", head);
        _edge = _edge.replace("(tailPort)", tailPort);
        _edge = _edge.replace("(headPort)", headPort);
        _edge = _edge.replace("([options])", options);

        return _edge;
    }

    private static String FillControlNode(
        AnalysisResultToken token,
        String color
    ) {
        String _controlNode = CONTROL_NODE;

        _controlNode = _controlNode.replace(
            "(index)",
            Integer.toString(token.getDependencyToken()) +
            Integer.toString(token.getIndex())
        );
        _controlNode = _controlNode.replace("(color)", color);

        return _controlNode;
    }

    private static String ConnectWithControlNode(
        AnalysisResultToken token,
        String color
    ) {
        String _left = FillEdgeTemplate(
            Integer.toString(token.getDependencyToken()),
            Integer.toString(token.getDependencyToken()) +
            Integer.toString(token.getIndex()),
            "n",
            "_",
            "[arrowhead=none, dir=back, arrowtail=invodot, color=" +
            color +
            ", penwidth=1.5]"
        );
        String _right = FillEdgeTemplate(
            Integer.toString(token.getDependencyToken()) +
            Integer.toString(token.getIndex()),
            Integer.toString(token.getIndex()),
            "_",
            "n",
            "[color=" + color + ", penwidth=1.5]"
        );

        return _left + TAB + _right;
    }

    private static String FillGraphTemplate(
        String _nodes,
        String _dependencies,
        String _subgraph,
        String _controlNodes
    ) {
        String _graph = GRAPH_TEMPLATE;

        _graph = _graph.replace("(nodes)", _nodes);
        _graph = _graph.replace("(dependencies)", _dependencies);
        _graph = _graph.replace("(subgraph)", _subgraph);
        _graph = _graph.replace("(controlNodes)", _controlNodes);

        return _graph;
    }

    private static int getNextColor(int _color_index) {
        return (_color_index + 1) % colors.size();
    }
}
