package sw417f20.ebal;

public class Node {
    public AST.NodeType Type;
    public String Value;

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

        if (child == null) {
            return;
        }

        if (this.FirstChild != null) {
            this.FirstChild.MakeSiblings(child);
        }
        else {
            Node otherSiblings = child.FirstSibling;
            this.FirstChild = child;

            while (otherSiblings != null) {
                otherSiblings.Parent = this;
                otherSiblings = otherSiblings.Next;
            }
        }

        // If the child isn't null, set this node as its parent
//        if (child != null) {
//            child.Parent = this;
//        }
//
//        else {
//            return;
//        }
//
//        // If this node doesn't have a child yet, input child is first child
//        if (FirstChild == null) {
//            FirstChild = child;
//        }
//
//        else {
//            // Get the list of children for this node
//            Node myChildren = FirstChild;
//
//            // Find the last child in the list
//            while (myChildren.Next != null) {
//                myChildren = myChildren.Next;
//            }
//
//            // Insert input child into the list at the end
//            myChildren.Next
//
//            // The input child's first sibling is the parent node's (this node) first child
////            child.FirstSibling = FirstChild;
//
//            FirstChild.MakeSiblings(child);
//        }
    }

    public void MakeSiblings(Node otherNode) {
        if (otherNode == null) {
            return;
        }

        Node mySiblings = this;

        while (mySiblings.Next != null) {
            mySiblings = mySiblings.Next;
        }

        Node otherSiblings = otherNode.FirstSibling;

        if (otherSiblings == null) {
            mySiblings.Next = otherNode;

            otherNode.FirstSibling = mySiblings.FirstSibling;
            otherNode.Parent = mySiblings.Parent;

            return;
        }

        else {
            mySiblings.Next = otherSiblings;

            otherSiblings.FirstSibling = mySiblings.FirstSibling;
            otherSiblings.Parent = mySiblings.Parent;

            while (otherSiblings.Next != null) {
                otherSiblings = otherSiblings.Next;
                otherSiblings.FirstSibling = mySiblings.FirstSibling;
                otherSiblings.Parent = mySiblings.Parent;
            }
        }




//        if (otherNode != null) {
//            otherNode.Parent = this.Parent;
//        }
//
//        else {
//            return;
//        }
//
//        if (FirstSibling == null) {
//            FirstSibling = otherNode;
//        }
//
//        else {
//            Node mySiblings = FirstSibling;
//
//            while (mySiblings.Next != null) {
//                mySiblings = mySiblings.Next;
//            }
//
//            mySiblings.Next = otherNode;
//
//            otherNode.FirstSibling = FirstSibling;
//        }
    }

//
//    public Node MakeSiblings(Node otherNode) {
//
//        Node mySiblings = this;
//
//        while (mySiblings.Next != null) {
//            mySiblings = mySiblings.Next;
//        }
//
//        Node otherSiblings = otherNode.FirstSibling;
//        mySiblings.Next = otherSiblings;
//
//        otherSiblings.FirstSibling = mySiblings.FirstSibling;
//        otherSiblings.Parent = mySiblings.Parent;
//
//        while (otherSiblings.Next != null) {
//            otherSiblings = otherSiblings.Next;
//            otherSiblings.FirstSibling = mySiblings.FirstSibling;
//            otherSiblings.Parent = mySiblings.Parent;
//        }
//
//        return otherSiblings;
//    }
//
}
