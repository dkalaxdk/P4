package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.CodeGeneration.Utility.ArduinoSystem;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class IntDeclarationStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node, ArduinoSystem arduinoSystem) {
        StringBuilder decl = new StringBuilder("int ");

        // Add the identifier
        decl.append(node.FirstChild.Value);

        Node nextChild = node.FirstChild.Next;
        //Initialization if there is an expression as the next node.
        if(!nextChild.IsEmpty()) {
            // Assign a value if there is an expression defined.
            decl.append(" = ");
            decl.append(nextChild.GenerateCode(arduinoSystem));
        }

        decl.append(";\n");

        return addIndent(arduinoSystem.Indent) + decl.toString();
    }
}
