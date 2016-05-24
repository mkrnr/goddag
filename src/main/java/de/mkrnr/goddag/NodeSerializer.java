package de.mkrnr.goddag;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.set.ListOrderedSet;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class NodeSerializer implements JsonSerializer<Node> {
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

    private List<Integer> getNodeIds(ListOrderedSet<Node> parents) {
	List<Integer> nodeIds = new ArrayList<Integer>();
	for (Node node : parents) {
	    nodeIds.add(node.id);
	}
	return nodeIds;
    }

}
