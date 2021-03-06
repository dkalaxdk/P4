package sw417f20.ebal.SyntaxAnalysis;

import sw417f20.ebal.ContextAnalysis.Strategies.SemanticsCheckerStrategy;
import sw417f20.ebal.ContextAnalysis.Symbol;

import sw417f20.ebal.CodeGeneration.Strategies.CodeGenerationStrategy;
import sw417f20.ebal.Exceptions.SemanticsException;

// This class iss inspired by the data structure
// outlined in Crafting a Compiler by Fischer et. al.

public class Node {
    public NodeType Type;
    public String Value;
    public int LineNumber = -1;
    public Symbol.SymbolType DataType;
    public int ArduinoID;

    // The next element in the linked list of siblings.
    public Node Next;

    // Reference to head of singly linked list of siblings.
    public Node FirstSibling;

    // Reference to this node's first child.
    // Can reach this node's other children through FirstChild's Next.
    // Acts as the head of a singly linked list of children.
    public Node FirstChild;

    // Reference to this node's parent.
    public Node Parent;

    public SemanticsCheckerStrategy SemanticsCheckerStrategy;
    public CodeGenerationStrategy CodeGenerationStrategy;

    private Node(NodeType type) {
        this.Type = type;
        this.Value = "";
    }

    private Node(NodeType type, Token token) {
        this.Type = type;
        this.Value = token.Content;
    }

    private Node(NodeType type, int lineNumber) {
        this.Type = type;
        this.Value = "";
        LineNumber = lineNumber;
    }

    private Node(NodeType type, Token token, int lineNumber) {
        this.Type = type;
        this.Value = token.Content;
        this.LineNumber = lineNumber;
    }

    public static Node MakeNode(Token token, int lineNumber) {

        switch (token.type) {
            case IDENTIFIER:
                return new Node(NodeType.Identifier, token, lineNumber);

            case LIT_Bool:
                return new Node(NodeType.BoolLiteral, token, lineNumber);

            case LIT_Int:
                return new Node(NodeType.IntLiteral, token, lineNumber);

            case LIT_Float:
                return new Node(NodeType.FloatLiteral, token, lineNumber);

            default:
                return new Node(NodeType.Error);
        }
    }

    public static Node MakeNode(NodeType nodeType) {
        return new Node(nodeType);
    }

    public static Node MakeNode(NodeType nodeType, int lineNumber) {
        return new Node(nodeType, lineNumber);
    }

    public static Node MakeNode(NodeType nodeType, Token token) {
        return new Node(nodeType, token);
    }

    @Override
    public String toString() {
        return Type.toString() +
                (!Value.isEmpty() ? " : " + Value : "") +
                ((LineNumber != -1) ? " : " + LineNumber : "");
    }

    /**
     * Adds the input child to this node's list of children.
     * Also sets the child's parent to be this, and its first sibling to be
     * this node's first child.
     * @param child The node that is added to this node's list of children
     */
    public void AddChild(Node child) {

        // If the input child is null, do nothing
        if (child == null) {
            return;
        }

        // If this node already has a child,
        // make the input child a sibling of the first child
        if (this.FirstChild != null) {
            this.FirstChild.MakeSiblings(child);
        }
        // Otherwise, it is the first child of this node
        else {
            child.Parent = this;
            this.FirstChild = child;

            // If the input child has siblings, but is not the FirstSibling, connect them to this node.
            if (child.FirstSibling != null) {
                this.FirstChild = child.FirstSibling;
                Node otherSiblings = child.FirstSibling;

                while (otherSiblings != null) {
                    otherSiblings.Parent = this;
                    otherSiblings = otherSiblings.Next;
                }
            }
            // If the input child is the first sibling, go from the input child's Next
            else if (child.Next != null) {
                Node otherSiblings = child.Next;

                while (otherSiblings != null) {
                    otherSiblings.Parent = this;
                    otherSiblings = otherSiblings.Next;
                }
            }
        }
    }

    /**
     * Makes this and this input node siblings, by changing the parent of the input node,
     * and making the sibling of the input node, this node's siblings
     * @param otherNode The node that is to be made siblings with this node
     */
    public void MakeSiblings(Node otherNode) {

        // If the input node is null, do nothing
        if (otherNode == null) {
            return;
        }

        // Remove connection from other's old parent to their first child
        if (otherNode.Parent != null) {
            otherNode.Parent.FirstChild = null;
        }

        // Get a reference to this node's list of siblings
        Node mySiblings = this; //todo Maybe firstsibling?

        // Get to the end of this node's list of siblings
        while (mySiblings.Next != null) {
            mySiblings = mySiblings.Next;
        }

        // Get a reference to other's list of siblings
        Node otherSiblings;

        // Other node is first sibling
        if (otherNode.FirstSibling == null) {
            // This node's last sibling's next set to other
            mySiblings.Next = otherNode;

            // Other node's list of siblings
            // is updated starting from other node
            otherSiblings = otherNode;
        }

        // Other is not first sibling
        else {
            // This last sibling's next set to other's first sibling
            mySiblings.Next = otherNode.FirstSibling;

            // Other node's list of siblings
            // is updated starting from other node's first sibling
            otherSiblings = otherNode.FirstSibling;
        }

        // Get a reference to this node's list of siblings
        Node firstSibling;

        // This is first sibling
        if (this.FirstSibling == null) {
            firstSibling = this;
        }

        // This is not first sibling
        else {
            firstSibling = this.FirstSibling;
        }

        // Update other and other's sibling's first sibling and parent
        while (otherSiblings != null) {
            otherSiblings.Parent = this.Parent;
            otherSiblings.FirstSibling = firstSibling;
            otherSiblings = otherSiblings.Next;
        }
    }

    /**
     * Enum for all the types of nodes available
     */
    public enum NodeType {
        Prog, Master, Slave, Initiate, Listener, EventHandler, Block,

        // Declarations
        PinDeclaration, FloatDeclaration, IntDeclaration, BoolDeclaration, EventDeclaration,

        Assignment, If, Call, Expression,

        Identifier,

        // Literals
        IntLiteral, FloatLiteral, BoolLiteral,

        // Pin types
        Digital, Analog, PWM,

        // IO types
        Input, Output,

        // Filter types
        Constant, Debounce, Range,

        // Function
        Broadcast, Write, GetValue, FilterNoise, CreateEvent, CreatePin,

        // Operator
        LessThan, GreaterThan, NotEqual, Equals, GreaterOrEqual, LessOrEqual, And, Or,
        Plus, Minus, Times, Divide, Modulo,

        // Prefixes
        PrefixNot, PrefixMinus,

        Error, Empty
    }

    /**
     * Just makes checks for empty nodes shorter.
     * @return True if the node is of type Empty
     */
    public boolean IsEmpty() {
        return this.Type == Node.NodeType.Empty;
    }

    // Calls the provided semantics checker strategy.
    // Is the common interface for semantics checking.
    public void CheckSemantics() throws SemanticsException {
        if (SemanticsCheckerStrategy != null) {
            SemanticsCheckerStrategy.CheckSemantics(this);
        } else {
            throw new NullPointerException("Semantics strategy not defined");
        }
    }

    // Calls the provided codeGen strategy, and returns the result.
    // Is the common interface for code generation.
    public String GenerateCode() {
        if (CodeGenerationStrategy != null) {
            return CodeGenerationStrategy.GenerateCode(this);
        }
        else {
            return "";
        }
    }
}
