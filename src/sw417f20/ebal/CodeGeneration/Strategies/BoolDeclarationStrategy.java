package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.CodeGeneration.Utility.ArduinoSystem;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class BoolDeclarationStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node, ArduinoSystem arduinoSystem) {
        //Add the type
        String content = "bool ";

        //Add the identifier
        content += node.FirstChild.Value;

        Node nextChild = node.FirstChild.Next;
        //Initialization if there is an expression as the next node.
        if(!nextChild.IsEmpty()) {
            content += " = ";
            content += nextChild.GenerateCode(arduinoSystem);
        }

        content += ";\n";
        return addIndentation(arduinoSystem.Indentation) + content;
    }
}
