package sw417f20.ebal.Visitors;

import sw417f20.ebal.SyntaxAnalysis.Node;

public abstract class Visitor {

    public abstract void Visit(Node node);

    public void VisitChildren(Node node) {
        if (node.FirstChild == null) {
            return;
        }

        Visit(node.FirstChild);

        Node children = node.FirstChild;

        while (children.Next != null) {
            Visit(children.Next);
            children = children.Next;
        }
    }

    public void VisitSiblings(Node node) {
        if (node.FirstSibling == null) {
            return;
        }

        Visit(node.FirstSibling);

        Node siblings = node.FirstSibling;

        while (siblings.Next != null) {
            Visit(siblings.Next);
            siblings = siblings.Next;
        }
    }
}
