package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.SyntaxAnalysis.Node;

public class EmptyStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node) {
        return "";
    }
}
