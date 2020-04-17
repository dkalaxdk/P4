package sw417f20.ebal.SyntaxAnalysis;

import sw417f20.ebal.CodeGeneration.Strategies.CodeGenerationStrategy;

// This class is inspired by the data structure
// outlined in Crafting a Compiler by Fischer et. al.
public class Node {
    public AST.NodeType Type;
    public String Value;

    // The next element in the linked list of siblings
    public Node Next;
    // Reference to head of singly linked list of siblings
    public Node FirstSibling;
    // The first child node.
    // Acts as head of a singly linked list of children.
    public Node FirstChild;
    public Node Parent;
    //TODO: Make private?
    public CodeGenerationStrategy CodeGenerationStrategy;

    public Node(AST.NodeType type) {
        this.Type = type;
        this.Value = "";
    }

    public Node(AST.NodeType type, String value) {
        this.Type = type;
        this.Value = value;
    }

    @Override
    public String toString() {
        return Type.toString() + (!Value.isEmpty() ? " : " + Value : "");
    }

    // Adds the input child to this node's list of children.
    // Also sets the child's parent to be this, and its first sibling to be
    // this node's first child.
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
        Node mySiblings = this;

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

    // Just makes checks for empty nodes shorter.
    public boolean IsEmpty() {
        return this.Type == AST.NodeType.Empty;
    }

    // Calls the provided codeGen strategy, and returns the result.
    // Is the common interface for code generation.
    public String GenerateCode() {
        if(CodeGenerationStrategy != null) {
            return CodeGenerationStrategy.GenerateCode(this);
        }
        else {
            return "";
        }
    }
}
