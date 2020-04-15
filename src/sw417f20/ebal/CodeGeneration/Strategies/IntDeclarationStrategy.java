package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.SyntaxAnalysis.Node;

public class IntDeclarationStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node) {
        String content = "int ";

        // Add the identifier
        content += node.FirstChild.Value;

        Node nextChild = node.FirstChild.Next;
        if(!nextChild.IsEmpty()) {
            // Assign a value if there is an expression defined.
            content += " = ";
            content += nextChild.GenerateCode();
        }
        else {
            content += ";";
        }
        content += "\n";
        return content;
    }
}
