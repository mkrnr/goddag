package de.mkrnr.goddag;

import java.util.Iterator;

public class LeafNode extends Node implements Iterable<LeafNode> {

    private String label;
    private LeafNode nextLeafNode;

    public LeafNode(String label, int id) {
	super(id);
	this.label = label;
    }

    public String getLabel() {
	return this.label;
    }

    public LeafNode getNextLeafNode() {
	return this.nextLeafNode;
    }

    public boolean hasNextLeafNode() {
	return null != this.nextLeafNode;
    }

    public Iterator<LeafNode> iterator() {
	Iterator<LeafNode> iterator = new Iterator<LeafNode>() {

	    LeafNode currentLeafNode;

	    public boolean hasNext() {
		if (this.currentLeafNode == null) {
		    return LeafNode.this.hasNextLeafNode();
		} else {
		    return this.currentLeafNode.hasNextLeafNode();
		}
	    }

	    public LeafNode next() {
		if (this.currentLeafNode == null) {
		    this.currentLeafNode = LeafNode.this;
		} else {
		    this.currentLeafNode = this.currentLeafNode.getNextLeafNode();
		}
		return this.currentLeafNode;
	    }

	};
	return iterator;
    }

    public void setNextLeafNode(LeafNode nextLeafNode) {
	this.nextLeafNode = nextLeafNode;
    }

    @Override
    public String toString() {
	String string = "LeafNode: " + this.label + " (";
	string += super.toString();
	string += ")";
	return string;
    }

}
