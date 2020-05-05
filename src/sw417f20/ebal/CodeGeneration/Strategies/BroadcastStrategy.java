package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.CodeGeneration.Utility.ArduinoSystem;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class BroadcastStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node, ArduinoSystem arduinoSystem) {
        String eventName = node.Next.Value;

        return addIndentation(arduinoSystem.Indentation) + eventName + ".broadcast();\n";
    }
}
