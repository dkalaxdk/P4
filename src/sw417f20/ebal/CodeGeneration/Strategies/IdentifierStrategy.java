package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.CodeGeneration.Utility.ArduinoSystem;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class IdentifierStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node, ArduinoSystem arduinoSystem) {
        String content = "";
        String prefix = node.FirstChild.GenerateCode(arduinoSystem);

        //If there is a prefix it is added to content before the value in expressions.
        content += prefix;
        content += node.Value;

        return content;
    }
}
