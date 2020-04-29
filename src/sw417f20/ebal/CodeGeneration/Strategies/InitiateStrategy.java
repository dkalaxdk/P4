package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.SyntaxAnalysis.Node;

public class InitiateStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node) {

        // Generate code for a block
        return node.FirstChild.GenerateCode();
    }
}
