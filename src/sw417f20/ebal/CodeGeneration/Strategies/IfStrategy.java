package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.CodeGeneration.Utility.ArduinoSystem;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class IfStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node, ArduinoSystem arduinoSystem) {

        String expression = node.FirstChild.GenerateCode(arduinoSystem);
        String block = node.FirstChild.Next.GenerateCode(arduinoSystem);

        Node elseBlock = node.FirstChild.Next.Next;
        String afterIf = "";

        StringBuilder ifStmt = new StringBuilder();

        if (node.Parent.Type != Node.NodeType.If) {
            ifStmt.append(addIndent(arduinoSystem.Indent));
        }

        ifStmt.append("if (").append(expression).append(") ");
        ifStmt.append(block);

        if (elseBlock != null && !elseBlock.IsEmpty()) {
            afterIf = addIndent(arduinoSystem.Indent) + "else " + elseBlock.GenerateCode(arduinoSystem);
        }

        ifStmt.append(afterIf);

        return ifStmt.toString();
    }
}
