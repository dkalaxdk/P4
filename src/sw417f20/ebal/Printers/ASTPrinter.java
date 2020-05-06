package sw417f20.ebal.Printers;

import sw417f20.ebal.SyntaxAnalysis.Node;

public class ASTPrinter extends Visitor {

    private int indent = -1;

    @Override
    public void Visit(Node node) {
//        if (node.Type != Node.NodeType.Empty) {
//            PrintNode(node);
//        }

        PrintNode(node);

        if (node.FirstChild != null) {
            indent++;
            VisitChildren(node);
            indent--;
        }

    }

    // Prints the input node with the correct amount of indent
    private void PrintNode(Node node) {
        for (int i = 0; i <= indent; i++) {
            System.out.print("\t");
        }
        System.out.println(node);

//        System.out.println();
    }
}
