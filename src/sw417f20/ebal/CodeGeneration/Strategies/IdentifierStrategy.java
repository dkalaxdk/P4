package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.CodeGeneration.Utility.ArduinoSystem;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class IdentifierStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node) {

        String prefix = node.FirstChild.GenerateCode();

        return prefix + node.Value;
    }
}
