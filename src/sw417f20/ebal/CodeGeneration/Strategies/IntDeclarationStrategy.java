package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.SyntaxAnalysis.Node;

public class IntDeclarationStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node) {

        // Add the type
        StringBuilder decl = new StringBuilder("int ");

        // Add the identifier
        decl.append(node.FirstChild.Value);

        Node nextChild = node.FirstChild.Next;

        // Initialization if there is an expression as the next node.
        if(!nextChild.IsEmpty()) {
            decl.append(" = ");
            decl.append(nextChild.GenerateCode());
        }

        decl.append(";\n");

        return AddIndent(arduinoSystem.Indent) + decl.toString();
    }
}
