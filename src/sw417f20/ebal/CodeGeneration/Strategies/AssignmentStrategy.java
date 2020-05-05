package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.CodeGeneration.Utility.ArduinoSystem;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class AssignmentStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node, ArduinoSystem arduinoSystem) {

        String identifier = node.FirstChild.Value;
        String expression = node.FirstChild.Next.GenerateCode(arduinoSystem);

        return addIndentation(arduinoSystem.Indentation) + identifier + " = " + expression + ";";
    }
}
