package sw417f20.ebal;

public class PrintVisitor extends Visitor {

    @Override
    public void Visit(Node node) {
        System.out.print(node.NodeName);

        if (node.NodeToken != null) {
            System.out.print(": " + node.NodeToken.content);
        }
        System.out.println();

        if (node.FirstChild != null) {
            Visit(node.FirstChild);

            Node children = node.FirstChild;

            while (children.Next != null) {
                Visit(children.Next);
                children = children.Next;
            }
        }
    }
}
