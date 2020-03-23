package sw417f20.ebal.Visitors;

import sw417f20.ebal.SyntaxAnalysis.AST;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class PrintVisitor extends Visitor {

    private int indent = 0;

    @Override
    public void Visit(Node node) {
        if (node.Type != AST.NodeType.Empty) {
            PrintNode(node);
        }

//        PrintNode(node);

        indent++;
        if (node.FirstChild != null) {
            VisitChildren(node);
        }
        indent--;

    }

    // Prints the input node with the correct amount of indent
    private void PrintNode(Node node) {
        for (int i = 0; i <= indent; i++) {
            System.out.print("\t");
        }
        System.out.print(node);

        System.out.println();
    }
}
