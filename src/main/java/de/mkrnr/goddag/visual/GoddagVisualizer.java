package de.mkrnr.goddag.visual;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.graphstream.graph.Graph;
import org.graphstream.graph.IdAlreadyInUseException;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.Viewer;

import de.mkrnr.goddag.Goddag;
import de.mkrnr.goddag.Node;

public class GoddagVisualizer {

    public static void main(String args[]) {
        Goddag goddag = new Goddag();

        Node rootNode = goddag.createNonterminalNode("root");
        goddag.setRootNode(rootNode);

        for (int i = 0; i < 10; i++) {
            Node leafNode = goddag.createLeafNode(i + " test");
            rootNode.addChild(leafNode);
        }

        Node leafNode5 = rootNode.getChildren().get(5);
        Node leafNode6 = rootNode.getChildren().get(6);
        Node leafNode7 = rootNode.getChildren().get(7);

        Node firstNameNode1 = goddag.createNonterminalNode("firstName1");
        firstNameNode1.addProperty("probability", "1.0");
        goddag.insertNodeBetween(rootNode, leafNode5, firstNameNode1);

        Node lastNameNode1 = goddag.createNonterminalNode("lastName1");
        lastNameNode1.addProperty("probability", "0.5");
        goddag.insertNodeBetween(rootNode, leafNode6, lastNameNode1);

        Node firstNameNode2 = goddag.createNonterminalNode("firstName2");
        firstNameNode2.addProperty("probability", "0.5");
        goddag.insertNodeBetween(rootNode, leafNode6, firstNameNode2);

        Node lastNameNode2 = goddag.createNonterminalNode("lastName2");
        lastNameNode2.addProperty("probability", "1.0");
        goddag.insertNodeBetween(rootNode, leafNode7, lastNameNode2);

        Node authorNode1 = goddag.createNonterminalNode("author1");
        Node authorNode2 = goddag.createNonterminalNode("author2");
        goddag.insertNodeBetween(rootNode, firstNameNode1, authorNode1);
        goddag.insertNodeBetween(rootNode, lastNameNode1, authorNode1);
        goddag.insertNodeBetween(rootNode, firstNameNode2, authorNode2);
        goddag.insertNodeBetween(rootNode, lastNameNode2, authorNode2);

        System.out.println(goddag);

        GoddagVisualizer goddagVisualizer = new GoddagVisualizer();
        goddagVisualizer.visualize(goddag);

    }

    private Graph graph;
    private Map<Integer, GraphNode> graphNodes;

    protected String styleSheet = "node { text-alignment: at-right; text-padding: 3px, 2px; text-background-mode: rounded-box; text-background-color: #EB2; text-color: #222;}";
    private int numberOfLeaves;

    public GoddagVisualizer() {
        System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
    }

    public void visualize(Goddag goddag) {
        this.graph = new SingleGraph("Test");

        this.graphNodes = new HashMap<Integer, GraphNode>();
        this.graph.addAttribute("ui.stylesheet", this.styleSheet);

        this.numberOfLeaves = goddag.getLeafNodes().size();

        Node rootNode = goddag.getRootNode();

        GraphNode rootGraphNode = this.addNode(rootNode, this.numberOfLeaves / 2, 0);

        this.addNodeChildren(goddag, rootGraphNode, -1);

        Viewer viewer = this.graph.display(false);
        ViewPanel view = viewer.getDefaultView();
        // view.resizeFrame(800, 600);
        view.getCamera().setViewCenter(rootGraphNode.getX(), 0, 0);
        double viewPercent = (double) 10 / this.numberOfLeaves;

        view.getCamera().setViewPercent(viewPercent);

    }

    private void addEdge(GraphNode parentGraphNode, GraphNode childGraphNode) {
        String edgeLabel = parentGraphNode.getId() + "->" + childGraphNode.getId();
        try {
            this.graph.addEdge(edgeLabel, parentGraphNode.getGraphStreamNode(), childGraphNode.getGraphStreamNode(),
                    true);
        } catch (IdAlreadyInUseException e) {
            System.out.println("did not add edge: " + edgeLabel);
        }
    }

    private GraphNode addNode(Node node, int x, int y) {
        if (this.graphNodes.containsKey(node.getId())) {
            return this.graphNodes.get(node.getId());
        } else {
            org.graphstream.graph.Node graphStreamNode = this.graph.addNode(String.valueOf(node.getId()));
            graphStreamNode.addAttribute("ui.label", node.getLabel());
            graphStreamNode.addAttribute("x", x);
            graphStreamNode.addAttribute("y", y);

            GraphNode graphNode = new GraphNode(graphStreamNode, node);
            this.graphNodes.put(node.getId(), graphNode);
            return graphNode;
        }

    }

    private void addNodeChildren(Goddag goddag, GraphNode parentGraphNode, int y) {

        List<Node> children = parentGraphNode.getNode().getChildren();

        int x = parentGraphNode.getX();
        for (Node childNode : children) {
            GraphNode childGraphNode = this.addNode(childNode, x, y);
            this.addEdge(parentGraphNode, childGraphNode);
            this.addNodeChildren(goddag, childGraphNode, y - 1);
            x += 1;
        }
    }

}
