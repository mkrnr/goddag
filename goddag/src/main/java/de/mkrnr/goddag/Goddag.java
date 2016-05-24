package de.mkrnr.goddag;

import java.util.ArrayList;
import java.util.Iterator;

public class Goddag {

    public static void main(String[] args) {
	Goddag goddag = new Goddag();

	NonterminalNode<String> rootNode = goddag.createNonterminalNode("root");
	goddag.setRootNode(rootNode);

	for (int i = 0; i < 10; i++) {
	    LeafNode leafNode = goddag.createLeafNode(i + " test");
	    rootNode.addChild(leafNode);
	}

	LeafNode rootChildFive = (LeafNode) rootNode.getChildren().get(5);

	NonterminalNode<String> firstNameNode1 = goddag.createNonterminalNode("firstName1");
	firstNameNode1.addProperty("probability", "1.0");
	goddag.insertNodeBetween(rootNode, rootChildFive, firstNameNode1);

	NonterminalNode<String> lastNameNode1 = goddag.createNonterminalNode("lastName1");
	lastNameNode1.addProperty("probability", "0.5");
	goddag.insertNodeBetween(rootNode, rootChildFive.getNextLeafNode(), lastNameNode1);

	NonterminalNode<String> firstNameNode2 = goddag.createNonterminalNode("firstName2");
	firstNameNode2.addProperty("probability", "0.5");
	goddag.insertNodeBetween(rootNode, rootChildFive.getNextLeafNode(), firstNameNode2);

	NonterminalNode<String> lastNameNode2 = goddag.createNonterminalNode("lastName2");
	lastNameNode2.addProperty("probability", "1.0");
	goddag.insertNodeBetween(rootNode, rootChildFive.getNextLeafNode().getNextLeafNode(), lastNameNode2);

	NonterminalNode<String> authorNode1 = goddag.createNonterminalNode("author1");
	goddag.insertNodeBetween(rootNode, firstNameNode1, authorNode1);
	goddag.insertNodeBetween(rootNode, lastNameNode1, authorNode1);

	NonterminalNode<String> authorNode2 = goddag.createNonterminalNode("author2");
	goddag.insertNodeBetween(rootNode, firstNameNode2, authorNode2);
	goddag.insertNodeBetween(rootNode, lastNameNode2, authorNode2);

	System.out.println(goddag);
    }

    private ArrayList<Node> nodes;
    private int currentId;
    private LeafNode currentRightmostLeafNode;
    private Node rootNode;

    public Goddag() {
	this.nodes = new ArrayList<Node>();
	this.currentId = 0;
	this.currentRightmostLeafNode = null;
    }

    /**
     * Creates a LeafNode without adding any edges <br>
     * The newly creates LeafNode gets interlinked with the currently right most
     * LeafNode to allow the iteration over LeafNodes
     *
     * @param label
     * @return created LeafNode
     */
    public LeafNode createLeafNode(String label) {
	LeafNode newLeafNode = new LeafNode(label, this.currentId++);

	if (this.currentRightmostLeafNode != null) {
	    this.currentRightmostLeafNode.setNextLeafNode(newLeafNode);
	    newLeafNode.setPreviousLeafNode(this.currentRightmostLeafNode);
	}
	this.currentRightmostLeafNode = newLeafNode;

	this.nodes.add(newLeafNode);
	return newLeafNode;
    }

    public <T> NonterminalNode<T> createNonterminalNode(T label) {
	NonterminalNode<T> nonterminalNode = new NonterminalNode<T>(label, this.currentId++);
	this.nodes.add(nonterminalNode);
	return nonterminalNode;
    }

    /**
     * Get iterator that starts at the leftmost LeafNode
     *
     * @return iterator if a LeafNode can be reached by following the leftmost
     *         children from the root
     */
    public Iterator<LeafNode> getLeafNodeIterator() {
	return this.getFirstLeafNode().iterator();
    }

    public Node getRootNode() {
	return this.rootNode;
    }

    /**
     *
     * Inserts nodeToAdd between parentNode and childNode. <br>
     *
     * @param <T>
     *
     * @param parentNode
     * @param childNode
     * @param nodeToAdd
     */
    public <T> void insertNodeBetween(Node parentNode, Node childNode, NonterminalNode<T> nodeToAdd) {

	int childNodeChildOfParentNodePosition = this.getChildOfParentNodePosition(childNode, parentNode);
	int nodeToAddChildOfParentNodePosition = this.getChildOfParentNodePosition(nodeToAdd, parentNode);

	if (nodeToAddChildOfParentNodePosition < childNodeChildOfParentNodePosition) {
	    // nodeToAdd is added to left of the current path of childNode
	    nodeToAdd.addChild(childNode);
	    childNode.addParent(0, nodeToAdd);

	    if (!parentNode.hasChild(nodeToAdd)) {
		parentNode.addChild(childNodeChildOfParentNodePosition, nodeToAdd);
		nodeToAdd.addParent(parentNode);
	    }
	} else {
	    // nodeToAdd is added to right of the current path of childNode
	    nodeToAdd.addChild(0, childNode);
	    childNode.addParent(nodeToAdd);
	    if (!parentNode.hasChild(nodeToAdd)) {
		parentNode.addChild(childNodeChildOfParentNodePosition + 1, nodeToAdd);
		nodeToAdd.addParent(parentNode);
	    }
	}

	if (parentNode.hasChild(childNode)) {
	    parentNode.disconnectChild(childNode);
	}

    }

    public void setRootNode(Node rootNode) {
	this.rootNode = rootNode;
    }

    @Override
    public String toString() {
	String goddagString = "";
	goddagString += this.toString(this.rootNode, "");
	return goddagString;
    }

    /**
     * Get the position in which a node has to be added between childNode and
     * parentNode as a child of parentNode
     *
     * @param childNode
     * @param parentNode
     * @return
     */
    private int getChildOfParentNodePosition(Node childNode, Node parentNode) {
	Node childOfParentNodeOnPath = childNode;
	while ((childOfParentNodeOnPath != null) && !parentNode.hasChild(childOfParentNodeOnPath)) {
	    childOfParentNodeOnPath = childOfParentNodeOnPath.getLastParent();
	}
	if (childOfParentNodeOnPath == null) {
	    return parentNode.getChildren().size() - 1;
	} else {
	    int position = parentNode.getChildPosition(childOfParentNodeOnPath);
	    return position;
	}
    }

    private LeafNode getFirstLeafNode() {
	Node leftmostNode = this.rootNode;
	while (!(leftmostNode instanceof LeafNode)) {
	    leftmostNode = leftmostNode.getFirstChild();
	    if (leftmostNode == null) {
		return null;
	    }
	}
	return (LeafNode) leftmostNode;
    }

    private String toString(Node currentNode, String offset) {
	String string = offset + currentNode.toString() + "\n";
	for (Node child : currentNode.getChildren()) {
	    string += this.toString(child, offset + "\t");
	}

	return string;
    }
}
