package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.ContextAnalysis.Symbol;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class LiteralStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node) {

        String prefix = node.FirstChild.GenerateCode();

        // Check for division or modulo by zero;
        if (node.FirstSibling != null) {
            Node operator = node.FirstSibling.Next;

            boolean a = operator.Type == Node.NodeType.Modulo || operator.Type == Node.NodeType.Divide;

            if (!a) {
                return prefix + node.Value;
            }

            boolean b = node.DataType == Symbol.SymbolType.INT && Integer.parseInt(node.Value) == 0;
            boolean c = node.DataType == Symbol.SymbolType.FLOAT && Float.parseFloat(node.Value) == 0.0;

            if (b || c) {
                System.exit(666);
            }
        }

        return prefix + node.Value;
    }
}
