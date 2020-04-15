package Tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sw417f20.ebal.SyntaxAnalysis.AST;
import sw417f20.ebal.SyntaxAnalysis.Node;
import sw417f20.ebal.SyntaxAnalysis.Token;

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

        Sibling1_1 = new Node(AST.NodeType.IntDeclaration);
        Sibling1_2 = new Node(AST.NodeType.IntDeclaration);
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
        Node node = new Node(AST.NodeType.Empty, new Token(Token.Type.ERROR, ""));

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
        Node node = new Node(AST.NodeType.Empty, new Token(Token.Type.ERROR, testString));

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
    void MakeSiblings_InputParentIsNotNull_InputParentFirstChildSetToNull() {
        // Arrange

        // Act
        Child1.MakeSiblings(Child2);

        // Assert
        assertNull(ParentNode2.FirstChild);
    }

    // #############################################################################################################

    // This is first sibling, other is first sibling

    // This last sibling's next set to other
    @Test
    void MakeSiblings_ThisIsFirstSibling_OtherIsFirstSibling_ThisLastSiblingNextSetToOther() {
        // Arrange

        // Act
        Child1.MakeSiblings(Child2);

        // Assert
        assertTrue(IsLastSiblingNextSetToOther(Child2));
    }


    // Other's parent updated
    @Test
    void MakeSiblings_ThisIsFirstSibling_OtherIsFirstSibling_OtherParentSetToThisNodeParent() {
        // Arrange

        // Act
        Child1.MakeSiblings(Child2);

        // Assert
        assertTrue(IsOtherParentSetToThisNodeParent(Child2));
    }


    // Other's first sibling set to this
    @Test
    void MakeSiblings_ThisIsFirstSibling_OtherIsFirstSibling_OtherFirstSiblingSetToThisNode() {
        // Arrange

        // Act
        Child1.MakeSiblings(Child2);

        // Assert
        assertTrue(IsOtherFirstSiblingSetToThisNode(Child2));
    }


    // Other's sibling's parent set to this's parent
    @Test
    void MakeSiblings_ThisIsFirstSibling_OtherIsFirstSibling_OtherSiblingParentSetToThisNodeParent() {
        // Arrange

        // Act
        Child1.MakeSiblings(Child2);

        // Assert
        assertTrue(IsOtherSiblingParentSetToThisNodeParent());
    }

    // Other's sibling's first sibling set to this
    @Test
    void MakeSiblings_ThisIsFirstSibling_OtherIsFirstSibling_OtherSiblingFirstSiblingSetToThisNode() {
        // Arrange

        // Act
        Child1.MakeSiblings(Child2);

        // Assert
        assertTrue(IsOtherSiblingFirstSiblingSetToThisNode());
    }

    // #############################################################################################################

    // This is first sibling, other is middle sibling

    // This last sibling's next set to other's first sibling
    @Test
    void MakeSiblings_ThisIsFirstSibling_OtherIsMiddleSibling_ThisLastSiblingNextSetToOtherFirstSibling() {
        // Arrange

        // Act
        Child1.MakeSiblings(Sibling2_1);

        // Assert
        assertTrue(IsLastSiblingNextSetToOtherFirstSibling());
    }

    // Other's parent updated
    @Test
    void MakeSiblings_ThisIsFirstSibling_OtherIsMiddleSibling_OtherParentSetToThisNodeParent() {
        // Arrange

        // Act
        Child1.MakeSiblings(Sibling2_1);

        // Assert
        assertTrue(IsOtherParentSetToThisNodeParent(Sibling2_1));
    }

    // Other's first sibling set to this
    @Test
    void MakeSiblings_ThisIsFirstSibling_OtherIsMiddleSibling_OtherFirstSiblingSetToThisNode() {
        // Arrange

        // Act
        Child1.MakeSiblings(Sibling2_1);

        // Assert
        assertTrue(IsOtherFirstSiblingSetToThisNode(Sibling2_1));
    }

    // Other's sibling's parent set to this's parent
    @Test
    void MakeSiblings_ThisIsFirstSibling_OtherIsMiddleSibling_OtherSiblingParentSetToThisNodeParent() {
        // Arrange

        // Act
        Child1.MakeSiblings(Sibling2_1);

        // Assert
        assertTrue(IsOtherSiblingParentSetToThisNodeParent());
    }

    // Other's sibling's first sibling set to this
    @Test
    void MakeSiblings_ThisIsFirstSibling_OtherIsMiddleSibling_OtherSiblingFirstSiblingSetToThisNode() {
        // Arrange

        // Act
        Child1.MakeSiblings(Sibling2_1);

        // Assert
        assertTrue(IsOtherSiblingFirstSiblingSetToThisNode());
    }

    // #############################################################################################################

    // This is middle sibling, other is first sibling

    // This last sibling's next set to other
    // Other's parent updated
    // Other's first sibling set to this's first sibling
    // Other's sibling's parent set to this's parent
    // Other's sibling's first sibling set to this's first sibling
    @Test
    void MakeSiblings_ThisIsMiddleSibling_OtherIsFirstSibling_ThisLastSiblingNextSetToOther() {
        // Arrange

        // Act
        Sibling1_1.MakeSiblings(Child2);

        // Assert
        assertTrue(IsLastSiblingNextSetToOther(Child2));
    }

    // Other's parent updated
    @Test
    void MakeSiblings_ThisIsMiddleSibling_OtherIsFirstSibling_OtherParentSetToThisNodeParent() {
        // Arrange

        // Act
        Sibling1_1.MakeSiblings(Child2);

        // Assert
        assertTrue(IsOtherParentSetToThisNodeParent(Child2));
    }

    // Other's first sibling set to this's first sibling
    @Test
    void MakeSiblings_ThisIsMiddleSibling_OtherIsFirstSibling_OtherFirstSiblingSetToThisNodeFirstSibling() {
        // Arrange

        // Act
        Sibling1_1.MakeSiblings(Child2);

        // Assert
        assertTrue(IsOtherFirstSiblingSetToThisNodeFirstSibling(Child2));
    }

    // Other's sibling's parent set to this's parent
    @Test
    void MakeSiblings_ThisIsMiddleSibling_OtherIsFirstSibling_OtherSiblingParentSetToThisNodeParent() {
        // Arrange

        // Act
        Sibling1_1.MakeSiblings(Child2);

        // Assert
        assertTrue(IsOtherSiblingParentSetToThisNodeParent());
    }

    // Other's sibling's first sibling set to this's first sibling
    @Test
    void MakeSiblings_ThisIsMiddleSibling_OtherIsFirstSibling_OtherSiblingFirstSiblingSetToThisNodeSibling() {
        // Arrange

        // Act
        Sibling1_1.MakeSiblings(Child2);

        // Assert
        assertTrue(IsOtherSiblingFirstSiblingSetToThisNodeSibling(Child2));
    }

    // #############################################################################################################

    // This is middle sibling, other is middle sibling

    // This last sibling's next set to other's first sibling
    @Test
    void MakeSiblings_ThisIsMiddleSibling_OtherIsMiddleSibling_ThisLastSiblingNextSetToOther() {
        // Arrange

        // Act
        Sibling1_1.MakeSiblings(Sibling2_1);

        // Assert
        assertTrue(IsLastSiblingNextSetToOther(Child2));
    }


    // Other's parent updated
    @Test
    void MakeSiblings_ThisIsMiddleSibling_OtherIsMiddleSibling_OtherParentSetToThisNodeParent() {
        // Arrange

        // Act
        Sibling1_1.MakeSiblings(Sibling2_1);

        // Assert
        assertTrue(IsOtherParentSetToThisNodeParent(Sibling2_1));
    }

    // Other's first sibling set to this's first sibling
    @Test
    void MakeSiblings_ThisIsMiddleSibling_OtherIsMiddleSibling_OtherFirstSiblingSetToThisNodeFirstSibling() {
        // Arrange

        // Act
        Sibling1_1.MakeSiblings(Sibling2_1);

        // Assert
        assertTrue(IsOtherFirstSiblingSetToThisNodeFirstSibling(Sibling2_1));
    }


    // Other's sibling's parent set to this's parent
    @Test
    void MakeSiblings_ThisIsMiddleSibling_OtherIsMiddleSibling_OtherSiblingParentSetToThisNodeParent() {
        // Arrange

        // Act
        Sibling1_1.MakeSiblings(Sibling2_1);

        // Assert
        assertTrue(IsOtherSiblingParentSetToThisNodeParent());
    }

    // Other's sibling's first sibling set to this's first sibling
    @Test
    void MakeSiblings_ThisIsMiddleSibling_OtherIsMiddleSibling_OtherSiblingFirstSiblingSetToThisNodeSibling() {
        // Arrange

        // Act
        Sibling1_1.MakeSiblings(Sibling2_1);

        // Assert
        assertTrue(IsOtherSiblingFirstSiblingSetToThisNodeSibling(Sibling2_1));
    }

    // This last sibling's next set to other
    boolean IsLastSiblingNextSetToOther(Node other) {
        return Sibling1_2.Next == other;
    }

    // Other's parent updated
    boolean IsOtherParentSetToThisNodeParent(Node other) {
        return other.Parent == ParentNode1;
    }

    // Other's first sibling set to this
    boolean IsOtherFirstSiblingSetToThisNode(Node other) {
        return other.FirstSibling == Child1;
    }

    // Other's sibling's parent set to this's parent
    boolean IsOtherSiblingParentSetToThisNodeParent() {
        return Sibling2_2.Parent == ParentNode1;
    }

    // Other's sibling's first sibling set to this
    boolean IsOtherSiblingFirstSiblingSetToThisNode() {
        return Sibling2_2.FirstSibling == Child1;
    }

    // This last sibling's next set to other's first sibling
    boolean IsLastSiblingNextSetToOtherFirstSibling() {
        return Sibling1_2.Next == Child2;
    }

    // Other's first sibling set to this's first sibling
    boolean IsOtherFirstSiblingSetToThisNodeFirstSibling(Node other) {
        return other.FirstSibling == Sibling1_1.FirstSibling;
    }

    // Other's sibling's first sibling set to this's first sibling
    boolean IsOtherSiblingFirstSiblingSetToThisNodeSibling(Node other) {
        return Sibling2_2.FirstSibling == Sibling1_1.FirstSibling;
    }
}