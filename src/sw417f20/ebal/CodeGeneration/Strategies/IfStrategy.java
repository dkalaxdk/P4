package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.SyntaxAnalysis.Node;

public class IfStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node) {

        // Get the expression from the if statement and the block
        String expression = node.FirstChild.generateCode();
        String block = node.FirstChild.Next.generateCode();

        // Get the else block
        Node elseBlock = node.FirstChild.Next.Next;

        String afterIf = "";
        StringBuilder ifStmt = new StringBuilder();

        // Add the appropriate amount of indentation if it is the first "if" in a chain of if-elseif
        if (node.Parent.Type != Node.NodeType.If) {
            ifStmt.append(addIndent(arduinoSystem.Indent));
        }

        // Add the if expression and the block to the if statement
        ifStmt.append("if (").append(expression).append(") ");
        ifStmt.append(block);

        // If there is an else block, generate code for it (this method again)
        if (elseBlock != null && !elseBlock.isEmpty()) {
            afterIf = addIndent(arduinoSystem.Indent) + "else " + elseBlock.generateCode();
        }

        // Add the else block or just the empty string to the if statement
        ifStmt.append(afterIf);

        return ifStmt.toString();
    }
}
