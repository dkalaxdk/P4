package Tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sw417f20.ebal.SyntaxAnalysis.AST;
import sw417f20.ebal.SyntaxAnalysis.Node;
import sw417f20.ebal.SyntaxAnalysis.Token;

import static org.junit.jupiter.api.Assertions.*;

class NodeTest {

    @BeforeEach
    void setUp() {
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


    @Test
    void MakeSiblings_ThisIsFirstSibling_Other() {
        // Arrange
        Node node1 = new Node(AST.NodeType.Empty);
        Node node2 = new Node(AST.NodeType.Master);

        // Act

        // Assert
    }






    // Don't update FirstChild
    // Don't update FirstSibling
    @Test
    void MakeSiblings_InputIsFirstSibling() {
        // Arrange
        Node node1 = new Node(AST.NodeType.Empty);
        Node node2 = new Node(AST.NodeType.Master);

        // Act

        // Assert
    }

    // Update FirstChild
    // Update FirstSibling
    @Test
    void MakeSiblings_InputIsNotFirstSibling() {
        // Arrange
        Node node1 = new Node(AST.NodeType.Empty);
        Node node2 = new Node(AST.NodeType.Master);

        // Act

        // Assert
    }

    // Don't update FirstChild
    @Test
    void MakeSiblings_InputIsFirstChild() {
        // Arrange
        Node node1 = new Node(AST.NodeType.Empty);
        Node node2 = new Node(AST.NodeType.Master);

        // Act

        // Assert
    }

    // Update FirstChild
    @Test
    void MakeSiblings_InputIsNotFirstChild() {
        // Arrange
        Node node1 = new Node(AST.NodeType.Empty);
        Node node2 = new Node(AST.NodeType.Master);

        // Act

        // Assert
    }
}