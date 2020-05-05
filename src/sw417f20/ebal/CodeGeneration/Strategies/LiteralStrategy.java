package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.CodeGeneration.Utility.ArduinoSystem;
import sw417f20.ebal.ContextAnalysis.Symbol;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class LiteralStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node, ArduinoSystem arduinoSystem) {

        String prefix = node.FirstChild.GenerateCode(arduinoSystem);

        // Check for division or modulo by zero;
        if (node.FirstSibling != null) {
            Node operator = node.FirstSibling.Next;

            boolean a = operator.Type == Node.NodeType.Modulo || operator.Type == Node.NodeType.Divide;
            boolean b = node.DataType == Symbol.SymbolType.INT && Integer.parseInt(node.Value) == 0;
            boolean c = node.DataType == Symbol.SymbolType.FLOAT && Float.parseFloat(node.Value) == 0.0;

            if (a && (b || c)) {
                System.exit(666);
            }
        }

        return prefix + node.Value;
    }
}
