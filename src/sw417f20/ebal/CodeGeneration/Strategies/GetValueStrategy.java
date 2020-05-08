package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.CodeGeneration.Utility.ArduinoSystem;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class GetValueStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node) {

        String name = node.Next.Value;

        return name + ".getValue()";
    }
}
