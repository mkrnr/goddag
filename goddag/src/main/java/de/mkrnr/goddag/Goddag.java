package de.mkrnr.goddag;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

    /**
     *
     * Inserts nodeToAdd between parentNode and childNode. <br>
     * If parentNode and childNode are connected by an edge, this edge is
     * removed. At the position of childNode, nodeToAdd is inserted and
     * childNode is set as child node of nodeToAdd <br>
     * If parentNode and childNode are not connected by an edge, nodeToAdd is
     * added after the last child of parentNode that is on a path from
     * parentNode to childNode
     *
     * @param <T>
     *
     * @param parentNode
     * @param childNode
     * @param nodeToAdd
     */
    public <T> void insertNodeBetween(Node parentNode, Node childNode, NonterminalNode<T> nodeToAdd) {

	// check if parentNode has childNode as child
	if (parentNode.hasChild(childNode)) {
	    // if yes, get position of child in parent node
	    int childPosition = parentNode.getChildPosition(childNode);
	    // remove the child
	    parentNode.removeChild(childNode);
	    // insert nodeToAdd at the old position of childNode
	    parentNode.addChild(childPosition, nodeToAdd);
	} else {
	    // if not, check if there is path from parentNode to childNode
	    Node childOfParentNodeOnPath = childNode;
	    do {
		childOfParentNodeOnPath = childOfParentNodeOnPath.getLastParent();
		if (childOfParentNodeOnPath == null) {
		    // there is no path from parentNode to childNode

		    // thereby, remove links from all ancestors of parentNode to
		    // childNode
		    this.removeLinksToAncestors(parentNode.getParents(), childNode);
		    break;
		}
	    } while (!childOfParentNodeOnPath.hasParent(parentNode));

	    int nodeToAddPosition;
	    if (childOfParentNodeOnPath == null) {
		// there was no path from parentNode to childNode
		// thereby, set nodeToAddPosition to index of last child + 1
		nodeToAddPosition = parentNode.getChildren().size();
	    } else {
		// there was a path from parentNode to childNode
		// thereby, set nodeToAddPosition to childOfParentNodeOnPath +1
		nodeToAddPosition = parentNode.getChildPosition(childOfParentNodeOnPath) + 1;
	    }
	    parentNode.addChild(nodeToAddPosition, nodeToAdd);
	}
	nodeToAdd.addChild(childNode);
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

    private void removeLinksToAncestors(List<Node> ancestors, Node childNode) {
	// iterate over ancestors of parentNode to remove all connections to
	// childNode
	for (Node ancestor : ancestors) {
	    if (ancestor.hasChild(childNode)) {
		ancestor.removeChild(childNode);
	    }
	    this.removeLinksToAncestors(ancestor.getParents(), childNode);
	}
    }

    private String toString(Node currentNode, String offset) {
	String string = offset + currentNode.toString() + "\n";
	for (Node child : currentNode.getChildren()) {
	    string += this.toString(child, offset + "\t");
	}

	return string;
    }

    public Node getRootNode() {
	return this.rootNode;
    }
}
