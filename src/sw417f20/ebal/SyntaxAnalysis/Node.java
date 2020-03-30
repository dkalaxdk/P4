package sw417f20.ebal.SyntaxAnalysis;

import sw417f20.ebal.ContextAnalysis.Symbol;

// This class is inspired by the data structure
// outlined in Crafting a Compiler by Fischer et. al.
public class Node {
    public AST.NodeType Type;
    public String Value;
    public Symbol.SymbolType Datatype;

    public Node Next;
    public Node FirstSibling;
    public Node FirstChild;
    public Node Parent;

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

    // Makes the input node the sibling of this node.
    // Also makes sure connect the two list of siblings, as well
    // as changing the parent to this node's parent.
    public void MakeSiblings(Node otherNode) {

        // If the input child is null, do nothing
        if (otherNode == null) {
            return;
        }

        // Get a reference to this node's list of siblings.
        // Doesn't have to be the first sibling, as we just need
        // to get to the end.
        Node mySiblings = this;

        // Find the last sibling in the chain
        while (mySiblings.Next != null) {
            mySiblings = mySiblings.Next;
        }

        // Get a reference to the input node's list of siblings
        Node otherSiblings = otherNode.FirstSibling;

        // If the input node doesn't have any siblings,
        // only the input node is added to the chain of siblings
        if (otherSiblings == null) {
            mySiblings.Next = otherNode;

            otherNode.FirstSibling = mySiblings.FirstSibling;
            otherNode.Parent = mySiblings.Parent;
        }

        // Otherwise, connect the input node's list of siblings
        // to this node's list of siblings
        else {
            mySiblings.Next = otherSiblings;

            // The input node's first sibling and parent, is set to
            // this node's first sibling and parent
            otherSiblings.FirstSibling = mySiblings.FirstSibling;
            otherSiblings.Parent = mySiblings.Parent;

            // Do the same for the rest of the input node's siblings
            // (In the first iteration, Next refers to otherNode's old FirstSibling's Next,
            // so that we can iterate through the list of siblings from the beginning)
            while (otherSiblings.Next != null) {
                otherSiblings = otherSiblings.Next;
                otherSiblings.FirstSibling = mySiblings.FirstSibling;
                otherSiblings.Parent = mySiblings.Parent;
            }
        }
    }
}
