package de.mkrnr.goddag;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class Goddag {

    public static JsonSerializer<Goddag> getJsonSerializer() {

	return new JsonSerializer<Goddag>() {

	    public JsonElement serialize(Goddag src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject obj = new JsonObject();
		obj.add("nonterminalNodes", context.serialize(src.nonterminalNodes));
		obj.add("leafNodes", context.serialize(src.leafNodes));
		obj.addProperty("rootNode", src.rootNode.getId());
		obj.addProperty("currentId", src.currentId);

		return obj;
	    }
	};
    }

    public static void main(String[] args) {
	Goddag goddag = new Goddag();

	Node rootNode = goddag.createNonterminalNode("root");
	goddag.setRootNode(rootNode);

	for (int i = 0; i < 10; i++) {
	    Node leafNode = goddag.createLeafNode(i + " test");
	    rootNode.addChild(leafNode);
	}

	Node firstNameNode1 = goddag.createNonterminalNode("firstName1");
	firstNameNode1.addProperty("probability", "1.0");
	goddag.insertNodeBetween(rootNode, rootNode.getChildren().get(5), firstNameNode1);

	Node lastNameNode1 = goddag.createNonterminalNode("lastName1");
	lastNameNode1.addProperty("probability", "0.5");
	goddag.insertNodeBetween(rootNode, rootNode.getChildren().get(6), lastNameNode1);

	Node firstNameNode2 = goddag.createNonterminalNode("firstName2");
	firstNameNode2.addProperty("probability", "0.5");
	goddag.insertNodeBetween(rootNode, rootNode.getChildren().get(6), firstNameNode2);

	Node lastNameNode2 = goddag.createNonterminalNode("lastName2");
	lastNameNode2.addProperty("probability", "1.0");
	goddag.insertNodeBetween(rootNode, rootNode.getChildren().get(7), lastNameNode2);

	Node authorNode1 = goddag.createNonterminalNode("author1");
	goddag.insertNodeBetween(rootNode, firstNameNode1, authorNode1);
	goddag.insertNodeBetween(rootNode, lastNameNode1, authorNode1);

	Node authorNode2 = goddag.createNonterminalNode("author2");
	goddag.insertNodeBetween(rootNode, firstNameNode2, authorNode2);
	goddag.insertNodeBetween(rootNode, lastNameNode2, authorNode2);

	System.out.println(goddag);
    }

    private ArrayList<Node> nonterminalNodes;
    private ArrayList<Node> leafNodes;
    private Node rootNode;

    private int currentId;

    public Goddag() {
	this.nonterminalNodes = new ArrayList<Node>();
	this.currentId = 0;
	this.leafNodes = new ArrayList<Node>();
    }

    /**
     * Creates a LeafNode without adding any edges <br>
     * The newly creates LeafNode gets interlinked with the currently right most
     * LeafNode to allow the iteration over LeafNodes
     *
     * @param label
     * @return created LeafNode
     */
    public Node createLeafNode(String label) {
	Node leafNode = new Node(label, this.currentId++);

	this.leafNodes.add(leafNode);

	return leafNode;
    }

    public Node createNonterminalNode(String label) {
	Node nonterminalNode = new Node(label, this.currentId++);
	this.nonterminalNodes.add(nonterminalNode);
	return nonterminalNode;
    }

    /**
     * Get iterator that starts at the leftmost LeafNode
     *
     * @return iterator if a LeafNode can be reached by following the leftmost
     *         children from the root
     */
    public List<Node> getLeafNodes() {
	return this.leafNodes;
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
    public void insertNodeBetween(Node parentNode, Node childNode, Node nodeToAdd) {

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

    private String toString(Node currentNode, String offset) {
	String string = offset + currentNode.toString() + "\n";
	for (Node child : currentNode.getChildren()) {
	    string += this.toString(child, offset + "\t");
	}

	return string;
    }
}