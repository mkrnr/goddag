package de.mkrnr.goddag;

public class NonterminalNode<T> extends Node {

    private T type;

    public NonterminalNode(T type, int id) {
	super(id);
	this.type = type;
    }

    public T getType() {
	return this.type;
    }

    @Override
    public String toString() {
	String string = "NonterminalNode: " + this.type + " (";
	string += super.toString();
	string += ")";
	return string;
    }
}
