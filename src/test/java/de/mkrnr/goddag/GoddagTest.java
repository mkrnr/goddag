package de.mkrnr.goddag;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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
    private NonterminalNode<String> rootNode;
    private ArrayList<LeafNode> leafNodes;
    private ArrayList<NonterminalNode<String>> nonterminalNodes;

    @Test
    public void insertNodeBetweenBeginningTest() {

	int childNodePosition = 0;
	LeafNode childNode = this.leafNodes.get(childNodePosition);

	this.addTwoNodesBetween(this.rootNode, childNode);
    }

    @Test
    public void insertNodeBetweenEndTest() {

	int childNodePosition = this.leafNodes.size() - 1;
	LeafNode childNode = this.leafNodes.get(childNodePosition);

	this.addTwoNodesBetween(this.rootNode, childNode);
    }

    @Test
    public void insertNodeBetweenMiddleTest() {

	int childNodePosition = 5;
	LeafNode childNode = this.leafNodes.get(childNodePosition);

	this.addTwoNodesBetween(this.rootNode, childNode);
    }

    @Test
    public void leafNodeIteratorTest() {

	Iterator<LeafNode> leafNodeIterator = this.goddag.getLeafNodeIterator();

	this.checkLeafNodeIterator(leafNodeIterator, 10);

    }

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
	this.goddag = new Goddag();
	this.rootNode = this.goddag.createNonterminalNode("root");
	this.goddag.setRootNode(this.rootNode);
	this.leafNodes = new ArrayList<LeafNode>();
	for (int i = 0; i < 10; i++) {
	    this.leafNodes.add(this.goddag.createLeafNode("leaf " + i));
	}
	this.nonterminalNodes = new ArrayList<NonterminalNode<String>>();
	for (int i = 0; i < 10; i++) {
	    this.nonterminalNodes.add(this.goddag.createNonterminalNode("leaf " + i));
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

    private void addTwoNodesBetween(NonterminalNode<String> parentNode, LeafNode childNode) {

	int childNodePosition = this.rootNode.getChildPosition(childNode);

	NonterminalNode<String> firstNodeToAdd = this.nonterminalNodes.get(0);

	this.goddag.insertNodeBetween(this.rootNode, childNode, firstNodeToAdd);

	assertEquals(firstNodeToAdd, this.rootNode.getChildren().get(childNodePosition));
	assertTrue(firstNodeToAdd.hasChild(childNode));

	NonterminalNode<String> secondNodeToAdd = this.nonterminalNodes.get(1);

	this.goddag.insertNodeBetween(this.rootNode, childNode, secondNodeToAdd);

	assertEquals(secondNodeToAdd, this.rootNode.getChildren().get(childNodePosition + 1));
	assertTrue(secondNodeToAdd.hasChild(childNode));

    }

    private void checkLeafNodeIterator(Iterator<LeafNode> leafNodeIterator, int expectedNumberOfLeafNodes) {

	int actualNumberOfLeafNodes = 0;
	while (leafNodeIterator.hasNext()) {
	    actualNumberOfLeafNodes++;
	    leafNodeIterator.next();
	}

	assertEquals(expectedNumberOfLeafNodes, actualNumberOfLeafNodes);
    }

}
