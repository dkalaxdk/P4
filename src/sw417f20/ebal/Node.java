package sw417f20.ebal;

public class Node {

    public Token.Type Type;
    public String Content;

    public Node Next;
    public Node FirstSibling;
    public Node FirstChild;
    public Node Parent;

    public Node MakeSiblings(Node otherNode) {

        Node mySiblings = this;

        while (mySiblings.Next != null) {
            mySiblings = mySiblings.Next;
        }

        Node otherSiblings = otherNode.FirstSibling;
        mySiblings.Next = otherSiblings;

        otherSiblings.FirstSibling = mySiblings.FirstSibling;
        otherSiblings.Parent = mySiblings.Parent;

        while (otherSiblings.Next != null) {
            otherSiblings = otherSiblings.Next;
            otherSiblings.FirstSibling = mySiblings.FirstSibling;
            otherSiblings.Parent = mySiblings.Parent;
        }

        return otherSiblings;
    }

    public void AdoptChildren(Node otherNode) {
        if (this.FirstSibling != null) {
            this.FirstSibling.MakeSiblings(otherNode);
        }
        else {
            Node otherSiblings = otherNode.FirstSibling;
            this.FirstChild = otherSiblings;

            while (otherSiblings != null) {
                otherSiblings.Parent = this;
                otherSiblings = otherSiblings.Next;
            }
        }
    }
}
