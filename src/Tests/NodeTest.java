package Tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sw417f20.ebal.SyntaxAnalysis.AST;
import sw417f20.ebal.SyntaxAnalysis.Node;

import static org.junit.jupiter.api.Assertions.*;

class NodeTest {
    private Node ParentNode1;
    private Node ParentNode2;

    private Node Child1;
    private Node Child2;

    private Node Sibling1_1;
    private Node Sibling1_2;
    private Node Sibling2_1;
    private Node Sibling2_2;


    @BeforeEach
    void setUp() {
        ParentNode1 = new Node(AST.NodeType.Master);
        ParentNode2 = new Node(AST.NodeType.Slave);

        Child1 = new Node(AST.NodeType.Listener);
        Child2 = new Node(AST.NodeType.EventHandler);

        Sibling1_1 = new Node(AST.NodeType.Declaration);
        Sibling1_2 = new Node(AST.NodeType.Declaration);
        Sibling2_1 = new Node(AST.NodeType.Assignment);
        Sibling2_2 = new Node(AST.NodeType.Assignment);

        ParentNode1.FirstChild = Child1;
        Child1.Parent = ParentNode1;

        Child1.Next = Sibling1_1;
        Sibling1_1.FirstSibling = Child1;
        Sibling1_1.Parent = ParentNode1;
        Sibling1_1.Next = Sibling1_2;
        Sibling1_2.FirstSibling = Child1;
        Sibling1_2.Parent = ParentNode1;


        ParentNode2.FirstChild = Child2;
        Child2.Parent = ParentNode2;

        Child2.Next = Sibling2_1;
        Sibling2_1.FirstSibling = Child2;
        Sibling2_1.Parent = ParentNode2;
        Sibling2_1.Next = Sibling2_2;
        Sibling2_2.FirstSibling = Child2;
        Sibling2_2.Parent = ParentNode2;
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void toString_ValueIsEmpty_ReturnsTypeName() {
        // Arrange
        Node node = new Node(AST.NodeType.Empty, "");

        // Act
        String expected = AST.NodeType.Empty.toString();
        String actual = node.toString();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void toString_ValueIsNotEmpty_ReturnsTypeNameAndValue() {
        // Arrange
        String testString = "TestValue";
        Node node = new Node(AST.NodeType.Empty, testString);

        // Act
        String expected = AST.NodeType.Empty.toString() + " : " + testString;
        String actual = node.toString();

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    void AddChild_InputIsNull_InputNotAdded() {
        // Arrange
        Node node = new Node(AST.NodeType.Empty);

        // Act
        node.AddChild(null);

        // Assert
        assertNull(node.FirstChild);
    }

    @Test
    void AddChild_FirstChildIsNull_FirstChildSetToInput() {
        // Arrange
        Node parent = new Node(AST.NodeType.Empty);
        Node child = new Node(AST.NodeType.Master);

        // Act
        parent.AddChild(child);

        // Assert
        assertSame(parent.FirstChild, child);
    }

    @Test
    void AddChild_FirstChildIsNull_InputParentUpdated() {
        // Arrange
        Node parent = new Node(AST.NodeType.Empty);
        Node child = new Node(AST.NodeType.Master);

        // Act
        parent.AddChild(child);

        // Assert
        assertSame(child.Parent, parent);
    }

    @Test
    void AddChild_FirstChildIsNull_InputNodeIsNotFirstSibling_InputFirstSiblingUpdatedParent() {
        // Arrange
        Node parent = new Node(AST.NodeType.Empty);
        Node child1 = new Node(AST.NodeType.Master);
        Node child2 = new Node(AST.NodeType.Slave);

        child1.MakeSiblings(child2);

        // Act
        parent.AddChild(child2);

        // Assert
        assertSame(parent.FirstChild.Parent, parent);
    }

    @Test
    void AddChild_FirstChildIsNull_InputNodeIsNotFirstSibling_ParentUpdatedFirstChild() {
        // Arrange
        Node parent = new Node(AST.NodeType.Empty);
        Node child1 = new Node(AST.NodeType.Master);
        Node child2 = new Node(AST.NodeType.Slave);

        child1.Next = child2;
        child2.FirstSibling = child1;

        // Act
        parent.AddChild(child2);

        // Assert
        assertSame(parent.FirstChild, child1);
    }

    @Test
    void AddChild_FirstChildIsNull_InputNodeIsNotFirstSibling_InputSiblingsUpdatedParent() {
        // Arrange
        Node parent = new Node(AST.NodeType.Empty);
        Node child1 = new Node(AST.NodeType.Master);
        Node child2 = new Node(AST.NodeType.Slave);
        Node child3 = new Node(AST.NodeType.Listener);

        child1.MakeSiblings(child2);
        child1.MakeSiblings(child3);

        child2.FirstSibling = child1;
        child3.FirstSibling = child1;

        // Act
        parent.AddChild(child2);

        // TODO: Muligt fix: Giv hver node med som parameter ned igennem parseren
        // Assert
        assertTrue(parent.FirstChild.Next.Parent == parent && parent.FirstChild.Next.Next.Parent == parent);
    }

    @Test
    void AddChild_FirstChildIsNull_InputNodeIsFirstSibling_InputSiblingsUpdatedParent() {
        // Arrange
        Node parent = new Node(AST.NodeType.Empty);
        Node child1 = new Node(AST.NodeType.Master);
        Node child2 = new Node(AST.NodeType.Slave);

        child1.MakeSiblings(child2);

        // Act
        parent.AddChild(child1);

        // Assert
        assertSame(parent.FirstChild.Next, child2);
    }

    @Test
    void AddChild_FirstChildIsNotNull_InputAddedToChainEnd() {
        // Arrange
        Node parent = new Node(AST.NodeType.Empty);
        Node child1 = new Node(AST.NodeType.Master);
        Node child2 = new Node(AST.NodeType.Slave);

        parent.FirstChild = child1;

        // Act
        parent.AddChild(child2);

        // Assert
        assertSame(parent.FirstChild.Next, child2);
    }




    @Test
    void MakeSiblings_InputIsNull_InputNotAdded() {
        // Arrange
        Node node1 = new Node(AST.NodeType.Empty);

        // Act
        node1.MakeSiblings(null);

        // Assert
        assertNull(node1.Next);
    }

    // This is first sibling

    @Test
    void MakeSiblings_ThisIsFirstSibling_OtherIsFirstSibling() {
        // Arrange

        // Act
        Child1.MakeSiblings(Child2);

        // Assert

    }

    @Test
    void MakeSiblings_ThisIsFirstSibling_OtherIsMiddleSibling() {
        // Arrange

        // Act
        Child1.MakeSiblings(Sibling2_1);

        // Assert
    }

    // This is middle sibling

    @Test
    void MakeSiblings_ThisIsMiddleSibling_OtherIsFirstSibling() {
        // Arrange

        // Act
        Sibling1_1.MakeSiblings(Child2);

        // Assert
    }

    @Test
    void MakeSiblings_ThisIsMiddleSibling_OtherIsMiddleSibling() {
        // Arrange

        // Act
        Sibling1_1.MakeSiblings(Sibling2_1);

        // Assert
    }

    @Test
    void MakeSiblings_ThisIsParentNode_OtherIsParentNode() {
        // Arrange

        // Act
        ParentNode1.MakeSiblings(ParentNode2);

        // Assert
    }
}