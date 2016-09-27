package de.mkrnr.goddag.visual;

import de.mkrnr.goddag.Node;

public class GraphNode {
    private org.graphstream.graph.Node graphStreamNode;
    private Node node;

    public GraphNode(org.graphstream.graph.Node graphStreamNode, Node node) {
        this.graphStreamNode = graphStreamNode;
        this.node = node;
    }

    public org.graphstream.graph.Node getGraphStreamNode() {
        return this.graphStreamNode;
    }

    public int getId() {
        return this.node.getId();
    }

    public Node getNode() {
        return this.node;
    }

    public int getX() {
        return this.graphStreamNode.getAttribute("x");
    }

    @Override
    public int hashCode() {
        return this.node.getId();
    }

}
