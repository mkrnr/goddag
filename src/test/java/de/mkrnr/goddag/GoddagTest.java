package de.mkrnr.goddag;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GoddagTest {

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    private Goddag goddag;
    private Node rootNode;
    private ArrayList<Node> leafNodes;
    private ArrayList<Node> nonterminalNodes;

    @Test
    public void insertNodeBetweenBeginningTest() {

        int childNodePosition = 0;
        Node childNode = this.leafNodes.get(childNodePosition);

        this.addTwoNodesBetween(this.rootNode, childNode);
    }

    @Test
    public void insertNodeBetweenEndTest() {

        int childNodePosition = this.leafNodes.size() - 1;
        Node childNode = this.leafNodes.get(childNodePosition);

        this.addTwoNodesBetween(this.rootNode, childNode);
    }

    @Test
    public void insertNodeBetweenMiddleTest() {

        int childNodePosition = 5;
        Node childNode = this.leafNodes.get(childNodePosition);

        this.addTwoNodesBetween(this.rootNode, childNode);
    }

    @Test
    public void serializationTest() {

        int childNodePosition = 5;
        Node childNode = this.leafNodes.get(childNodePosition);

        this.addTwoNodesBetween(this.rootNode, childNode);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.registerTypeAdapter(Goddag.class, Goddag.getJsonSerializer());
        gsonBuilder.registerTypeAdapter(Goddag.class, Goddag.getJsonDeserializer());
        gsonBuilder.registerTypeAdapter(Node.class, Node.getJsonSerializer());
        Gson gson = gsonBuilder.create();

        String goddagAsJsonString = gson.toJson(this.goddag);
        System.out.println("serialized goddag:");
        System.out.println(goddagAsJsonString);

        Goddag deserializedGoddag = gson.fromJson(goddagAsJsonString, Goddag.class);
        System.out.println();
        System.out.println("deserialized goddag:");
        System.out.println(deserializedGoddag);

    }

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        this.goddag = new Goddag();
        this.rootNode = this.goddag.createNonterminalNode("root");
        this.goddag.setRootNode(this.rootNode);
        this.leafNodes = new ArrayList<Node>();
        for (int i = 0; i < 10; i++) {
            Node leafNode = this.goddag.createLeafNode("leaf " + i);
            leafNode.addProperty("p1", "0.5");
            leafNode.addProperty("p2", "0.8");
            this.leafNodes.add(leafNode);
        }
        this.nonterminalNodes = new ArrayList<Node>();
        for (int i = 0; i < 10; i++) {
            this.nonterminalNodes.add(this.goddag.createNonterminalNode("nonterminal " + i));
        }

        for (int i = 0; i < 10; i++) {
            this.rootNode.addChild(this.leafNodes.get(i));
        }

    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    private void addTwoNodesBetween(Node parentNode, Node childNode) {

        int childNodePosition = this.rootNode.getChildPosition(childNode);

        Node firstNodeToAdd = this.nonterminalNodes.get(0);

        this.goddag.insertNodeBetween(this.rootNode, childNode, firstNodeToAdd);

        assertEquals(firstNodeToAdd, this.rootNode.getChildren().get(childNodePosition));
        assertTrue(firstNodeToAdd.hasChild(childNode));

        Node secondNodeToAdd = this.nonterminalNodes.get(1);

        this.goddag.insertNodeBetween(this.rootNode, childNode, secondNodeToAdd);

        assertEquals(secondNodeToAdd, this.rootNode.getChildren().get(childNodePosition + 1));
        assertTrue(secondNodeToAdd.hasChild(childNode));

    }

}
