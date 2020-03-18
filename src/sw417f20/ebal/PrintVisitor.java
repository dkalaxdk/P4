package sw417f20.ebal;

public class PrintVisitor extends Visitor {

    private int indent = 0;

    @Override
    public void Visit(Node node) {
        if (node.Type != AST.NodeType.Empty) {
            for (int i = 0; i <= indent; i++) {
                System.out.print("\t");
            }
            System.out.print(node);

            System.out.println();
        }

        indent++;
        if (node.FirstChild != null) {
            Visit(node.FirstChild);

            Node children = node.FirstChild;

            while (children.Next != null) {
                Visit(children.Next);
                children = children.Next;
            }
        }
        if (node.FirstSibling != null) {
            Visit(node.FirstSibling);

            Node siblings = node.FirstSibling;

            while (siblings.Next != null) {
                Visit(siblings.Next);
                siblings = siblings.Next;
            }
        }
        indent--;
    }

}
