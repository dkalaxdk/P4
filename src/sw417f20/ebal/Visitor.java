package sw417f20.ebal;

public abstract class Visitor {

    public void Visit(Node node) {
        if (node.FirstChild != null) {
            Visit(node.FirstChild);

            Node children = node.FirstChild;

            while (children.Next != null) {
                Visit(children.Next);
                children = children.Next;
            }

            System.out.println(node.Content);
        }
    }

}
