package nosensegenerator.nosense;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
 * This class builds a Graphviz DOT file from a list of AnalysisResultTokens.
 */
public class GraphvizGenerator {

    // Space to indent the DOT file
    private static final String TAB = "    ";

    // Path to the output DOT file (system temp directory)
    // Note: Replace (fileName) with the actual file name when calling
    // GenerateDependencyGraph
    private static final String FILE_PATH = System.getProperty("java.io.tmpdir") + "/(fileName).dot";

    // Templates for the various components of the DOT file
    // Node is the object that contains the important values of each word analyzed
    // by the google NLP API
    private static final String NODE_TEMPLATE = "(index) [nojustify=true shape=box (label)];\n";

    // This creates layout of the node, which is a table with three rows
    private static final String LABEL_TEMPLATE = "label=<\n" +
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
    // This is the template for the edges between nodes
    // The tail and head are the nodes that are connected, the tailPort and headPort
    // indicate where the edge has to connect to the node, it uses cardinal
    // directions (n, s, e, w, etc.) (_ is automatic)
    // The options are additional parameters for the edge, such as color or style
    private static final String EDGE_TEMPLATE = "(tail):(tailPort) -> (head):(headPort) ([options]);\n";

    // This node is used to have clearer edges between nodes, it is a colored dot,
    // used to control the edge curvature and height
    // The index for this type of node is the concatenation of the dependency token
    // and the index of the current token, so it is unique
    // Example: if the dependency token is 2 and the index is 3, the control node
    // will be named 23
    // So instead of 2 -> 3 it will be 2 -> 23 -> 3, the node 23 it will be placed
    // higher than the nodes 2 and 3, so the edge will NOT be straight
    private static final String CONTROL_NODE = "(index) [shape=point, width=0.02, height=0.02, label=\"\", color=(color)];\n";

    // This is the template for the whole graph, it contains the nodes,
    // dependencies, control nodes and subgraph
    // (nodes) used to draw the nodes with the token attibutes
    // (dependencies) used to draw the edges between nodes (with the control nodes
    // between them)
    // (controlNodes) used to draw the control nodes, which are the colored dots
    // that control the edge curvature and height, these nodes are in a subgraph
    // with rank=min, so they are placed at the top of the graph
    // (subgraph) used to draw invisible edges between nodes in a subgraph to
    // mantain the order of the words in the sentence, the attribute rank=same
    // places the nodes, in this subgraph, under the control nodes
    private static final String GRAPH_TEMPLATE = "digraph G {\n" +
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

    // List of colors used to paint the edges/control nodes to differentiate them
    // visually
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
            "webpurple");

    /**
     * Generates a Graphviz DOT file from a list of AnalysisResultTokens.
     *
     * @param tokens   The list of AnalysisResultTokens to generate the graph from.
     * @param fileName The name of the file to save the generated DOT content.
     */
    public static void GenerateDependencyGraph(
            ArrayList<AnalysisResultToken> tokens,
            String fileName) {
        String graph = "";
        String nodes = "";
        String dependencies = "";
        String subgraph = "";
        String controlNodes = "";
        int color_index = 0;

        // Create a node for each word in the sentence
        for (AnalysisResultToken token : tokens) {
            nodes = nodes + TAB + FillNodeTemplate(token);
        }

        // Connect the nodes with edges and control nodes
        // dependencyToken is the index of the token(word) that this token(word) depends
        // on, if it is equal to the index of the token, it means that it has NOT a
        // dependency
        for (AnalysisResultToken token : tokens) {
            if (token.getDependencyToken() != token.getIndex()) {
                dependencies = dependencies +
                        TAB +
                        ConnectWithControlNode(token, colors.get(color_index));
                controlNodes = controlNodes +
                        TAB +
                        TAB +
                        FillControlNode(token, colors.get(color_index));

                color_index = getNextColor(color_index);
            }
        }

        // Create the subgraph with invisible edges to maintain the order of the words
        // in the sentence
        for (int index = 0; index < tokens.size() - 1; index++) {
            subgraph = subgraph +
                    TAB +
                    TAB +
                    FillEdgeTemplate(
                            Integer.toString(tokens.get(index).getIndex()),
                            Integer.toString(tokens.get(index + 1).getIndex()),
                            "e",
                            "w",
                            "");
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

    // Fills the node template with the index and label of the token
    private static String FillNodeTemplate(AnalysisResultToken token) {
        String _node = NODE_TEMPLATE;

        _node = _node.replace("(index)", Integer.toString(token.getIndex()));
        _node = _node.replace("(label)", FillLabelTemplate(token));

        return _node;
    }

    // Fills the label template with the dependency label, text and tag of the token
    private static String FillLabelTemplate(AnalysisResultToken token) {
        String _label = LABEL_TEMPLATE;

        _label = _label.replace(
                "(dependencyLabel)",
                token.getDependencyLabel());
        _label = _label.replace("(text)", token.getText());
        _label = _label.replace("(tag)", token.getTag());

        return _label;
    }

    // Fills the edge template with tail, head, tailPort, headPort and options
    private static String FillEdgeTemplate(
            String tail,
            String head,
            String tailPort,
            String headPort,
            String options) {
        String _edge = EDGE_TEMPLATE;

        _edge = _edge.replace("(tail)", tail);
        _edge = _edge.replace("(head)", head);
        _edge = _edge.replace("(tailPort)", tailPort);
        _edge = _edge.replace("(headPort)", headPort);
        _edge = _edge.replace("([options])", options);

        return _edge;
    }

    // Fills the control node template with the index and color
    private static String FillControlNode(
            AnalysisResultToken token,
            String color) {
        String _controlNode = CONTROL_NODE;

        _controlNode = _controlNode.replace(
                "(index)",
                Integer.toString(token.getDependencyToken()) +
                        Integer.toString(token.getIndex()));
        _controlNode = _controlNode.replace("(color)", color);

        return _controlNode;
    }

    // Creates the connection of the current token with its dependency token using
    // control nodes and returns the string representation of the edges
    private static String ConnectWithControlNode(
            AnalysisResultToken token,
            String color) {
        String _left = FillEdgeTemplate(
                Integer.toString(token.getDependencyToken()),
                Integer.toString(token.getDependencyToken()) +
                        Integer.toString(token.getIndex()),
                "n",
                "_",
                "[arrowhead=none, dir=back, arrowtail=invodot, color=" +
                        color +
                        ", penwidth=1.5]");
        String _right = FillEdgeTemplate(
                Integer.toString(token.getDependencyToken()) +
                        Integer.toString(token.getIndex()),
                Integer.toString(token.getIndex()),
                "_",
                "n",
                "[color=" + color + ", penwidth=1.5]");

        return _left + TAB + _right;
    }

    // Helper method to fill the graph template with nodes, dependencies, subgraph
    // and control nodes
    private static String FillGraphTemplate(
            String _nodes,
            String _dependencies,
            String _subgraph,
            String _controlNodes) {
        String _graph = GRAPH_TEMPLATE;

        _graph = _graph.replace("(nodes)", _nodes);
        _graph = _graph.replace("(dependencies)", _dependencies);
        _graph = _graph.replace("(subgraph)", _subgraph);
        _graph = _graph.replace("(controlNodes)", _controlNodes);

        return _graph;
    }

    // Clicle through the colors list
    private static int getNextColor(int _color_index) {
        return (_color_index + 1) % colors.size();
    }
}
