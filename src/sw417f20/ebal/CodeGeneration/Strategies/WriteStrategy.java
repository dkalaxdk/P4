package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.CodeGeneration.Utility.ArduinoSystem;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class WriteStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node, ArduinoSystem arduinoSystem) {

        String pinName = node.Next.Value;
        String value = node.Next.Next.Value;

        return addIndent(arduinoSystem.Indent) + pinName + ".write(" + value + ");\n";
    }
}
