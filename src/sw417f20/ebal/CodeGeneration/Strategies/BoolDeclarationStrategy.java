package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.CodeGeneration.Utility.ArduinoSystem;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class BoolDeclarationStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node, ArduinoSystem arduinoSystem) {
        //Add the type
        StringBuilder decl = new StringBuilder("bool ");

        //Add the identifier
        decl.append(node.FirstChild.Value);

        Node nextChild = node.FirstChild.Next;
        //Initialization if there is an expression as the next node.
        if(!nextChild.IsEmpty()) {
            decl.append(" = ");
            decl.append(nextChild.GenerateCode(arduinoSystem));
        }

        decl.append(";\n");
        return addIndentation(arduinoSystem.Indentation) + decl.toString();
    }
}
