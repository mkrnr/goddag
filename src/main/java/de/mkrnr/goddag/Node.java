package de.mkrnr.goddag;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.collections4.set.ListOrderedSet;

public class Node {

    String label;
    ListOrderedSet<Node> parents;
    ListOrderedSet<Node> children;
    HashMap<String, String> properties;
    int id;

    public Node() {
	this.parents = new ListOrderedSet<Node>();
	this.children = new ListOrderedSet<Node>();

	this.properties = new HashMap<String, String>();
    }

    public Node(String label, int id) {
	this();
	this.label = label;
	this.id = id;

    }

    public void addChild(int childIndex, Node childNode) {
	this.children.add(childIndex, childNode);
    }

    public void addChild(Node childNode) {
	this.children.add(childNode);
    }

    public void addParent(int i, Node nodeToAdd) {
	this.parents.add(i, nodeToAdd);

    }

    public void addParent(Node parentNode) {
	this.parents.add(parentNode);
    }

    public void addProperty(String key, String value) {
	this.properties.put(key, value);

    }

    public boolean disconnectChild(Node childNode) {
	return this.children.remove(childNode) && childNode.parents.remove(this);
    }

    public boolean disconnectParent(Node parentNode) {
	return this.parents.remove(parentNode) && parentNode.children.remove(this);
    }

    @Override
    public boolean equals(Object object) {
	if (object instanceof Node) {
	    Node node = (Node) object;
	    return this.id == node.getId();
	} else {
	    return false;
	}
    }

    public int getChildPosition(Node childNode) {
	return this.children.indexOf(childNode);
    }

    public List<Node> getChildren() {
	return this.children.asList();
    }

    public Node getFirstChild() {
	if (this.children.size() == 0) {
	    return null;
	} else {
	    return this.children.get(0);
	}
    }

    public String getLabel() {
	return this.label;
    }

    public Node getLastParent() {
	if (this.parents.size() == 0) {
	    return null;
	} else {
	    return this.parents.get(this.parents.size() - 1);
	}
    }

    public List<Node> getParents() {
	return this.parents.asList();
    }

    public HashMap<String, String> getProperties() {
	return this.properties;
    }

    public String getProperty(String key) {
	return this.properties.get(key);
    }

    public boolean hasChild(Node childNode) {
	return this.children.contains(childNode);
    }

    public boolean hasChildren() {
	return !this.children.isEmpty();
    }

    public boolean hasParent(Node parentNode) {
	return this.parents.contains(parentNode);
    }

    public boolean hasParents() {
	return !this.parents.isEmpty();
    }

    public boolean hasProperties() {
	return !this.properties.isEmpty();
    }

    public boolean hasProperty(String key) {
	return this.properties.containsKey(key);
    }

    @Override
    public String toString() {
	String string = "";

	string += this.label + " (";

	for (Entry<String, String> property : this.getProperties().entrySet()) {
	    string += property.getKey() + ":" + property.getValue() + ", ";
	}
	if (string.endsWith(", ")) {
	    string = string.replaceAll(", $", "");
	}
	string += ")";
	return string;
    }

    private int getId() {
	return this.id;
    }

}
