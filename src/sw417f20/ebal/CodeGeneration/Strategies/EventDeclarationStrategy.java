package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.CodeGeneration.Utility.ArduinoSystem;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class EventDeclarationStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node, ArduinoSystem arduinoSystem) {

        arduinoSystem.AddEventDeclaration(node);

        String eventName = node.FirstChild.Value;
        String expression = node.FirstChild.Next.FirstChild.Next.GenerateCode(arduinoSystem);

        return addIndentation(arduinoSystem.Indentation) + eventName + ".createEvent(" + expression + ");\n";
    }
}
