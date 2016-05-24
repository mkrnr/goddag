package de.mkrnr.goddag;

import java.lang.reflect.Type;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class GoddagSerializer implements JsonSerializer<Goddag> {

    public JsonElement serialize(Goddag src, Type typeOfSrc, JsonSerializationContext context) {
	JsonObject obj = new JsonObject();
	obj.add("nonterminalNodes", context.serialize(src.nonterminalNodes));
	obj.add("leafNodes", context.serialize(src.leafNodes));
	obj.addProperty("rootNode", src.rootNode.id);
	obj.addProperty("currentId", src.currentId);

	// private ArrayList<Node> nonterminalNodes;
	// private ArrayList<LeafNode> leafNodes;
	// private Node rootNode;
	// private int currentId;

	return obj;
    }
}
