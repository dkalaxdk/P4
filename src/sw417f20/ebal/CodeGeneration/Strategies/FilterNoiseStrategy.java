package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.SyntaxAnalysis.Node;

public class FilterNoiseStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node) {

        String pinName = node.Next.Value;
        String filterType = node.Next.Next.generateCode();

        return pinName + ".filterNoise(" + filterType + ")";
    }
}
