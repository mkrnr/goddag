package de.mkrnr.goddag;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.collections4.set.ListOrderedSet;

public abstract class Node {

    private ListOrderedSet<Node> parents;
    private ListOrderedSet<Node> children;
    private HashMap<String, String> properties;
    private int id;

    public Node() {
	this.parents = new ListOrderedSet<Node>();
	this.children = new ListOrderedSet<Node>();

	this.properties = new HashMap<String, String>();
    }

    public Node(int id) {
	this();
	this.id = id;
    }

    public void addChild(int childIndex, Node childNode) {
	this.children.add(childIndex, childNode);
	childNode.parents.add(this);
    }

    public void addChild(Node childNode) {
	this.children.add(childNode);
	childNode.parents.add(this);
    }

    public void addParent(Node parentNode) {
	this.parents.add(parentNode);
	parentNode.children.add(this);
    }

    public void addProperty(String key, String value) {
	this.properties.put(key, value);

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

    public boolean hasParent(Node parentNode) {
	return this.parents.contains(parentNode);
    }

    public boolean hasProperty(String key) {
	return this.properties.containsKey(key);
    }

    public boolean removeChild(Node childNode) {
	return this.children.remove(childNode) && childNode.parents.remove(this);
    }

    public boolean removeParent(Node parentNode) {
	return this.parents.remove(parentNode) && parentNode.children.remove(this);
    }

    @Override
    public String toString() {
	String string = "";
	for (Entry<String, String> property : this.getProperties().entrySet()) {
	    string += property.getKey() + ":" + property.getValue() + ", ";
	}
	if (string.endsWith(", ")) {
	    string = string.replaceAll(", $", "");
	}
	return string;
    }

    private int getId() {
	return this.id;
    }

}
