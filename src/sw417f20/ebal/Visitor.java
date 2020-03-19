package sw417f20.ebal;

public abstract class Visitor {

    public abstract void Visit(Node node);

    public void VisitChildren(Node node) {
        Visit(node.FirstChild);

        Node children = node.FirstChild;

        while (children.Next != null) {
            Visit(children.Next);
            children = children.Next;
        }
    }

    public void VisitSiblings(Node node) {
        Visit(node.FirstSibling);

        Node siblings = node.FirstSibling;

        while (siblings.Next != null) {
            Visit(siblings.Next);
            siblings = siblings.Next;
        }
    }
}
