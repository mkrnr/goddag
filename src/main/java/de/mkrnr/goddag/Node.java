package de.mkrnr.goddag;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.collections4.set.ListOrderedSet;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class Node {

    public static JsonSerializer<Node> getJsonSerializer() {

        return new JsonSerializer<Node>() {
            @Override
            public JsonElement serialize(Node src, Type typeOfSrc, JsonSerializationContext context) {
                JsonObject obj = new JsonObject();
                obj.addProperty("label", src.label.toString());
                obj.addProperty("id", src.id);

                if (src.hasParents()) {
                    obj.add("parents", context.serialize(this.getNodeIds(src.parents)));
                }
                if (src.hasChildren()) {
                    obj.add("children", context.serialize(this.getNodeIds(src.children)));
                }
                if (src.hasProperties()) {
                    obj.add("properties", context.serialize(src.properties));
                }

                return obj;
            }

            private List<Integer> getNodeIds(ListOrderedSet<Node> nodes) {
                List<Integer> nodeIds = new ArrayList<Integer>();
                for (Node node : nodes) {
                    nodeIds.add(node.id);
                }
                return nodeIds;
            }
        };
    }

    private String label;
    private ListOrderedSet<Node> parents;
    private ListOrderedSet<Node> children;
    private HashMap<String, String> properties;

    private int id;

    public Node(int id) {
        this();
        this.id = id;
    }

    public Node(String label, int id) {
        this(id);
        this.label = label;

    }

    private Node() {
        this.parents = new ListOrderedSet<Node>();
        this.children = new ListOrderedSet<Node>();

        this.properties = new HashMap<String, String>();
    }

    public void addChild(int childIndex, Node childNode) {
        this.children.add(childIndex, childNode);
    }

    public void addChild(Node childNode) {
        this.children.add(childNode);
    }

    public void addChildren(List<Node> newChildren) {
        this.children.addAll(newChildren);
    }

    public void addParent(int i, Node nodeToAdd) {
        this.parents.add(i, nodeToAdd);

    }

    public void addParent(Node parentNode) {
        this.parents.add(parentNode);
    }

    public void addParents(List<Node> newParents) {
        this.parents.addAll(newParents);
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

    public int getId() {
        return this.id;
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

    public void setLabel(String label) {
        this.label = label;

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

}
