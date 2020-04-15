package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.SyntaxAnalysis.Node;

public class IdentifierStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node) {
        String content = "";
        String prefix = node.FirstChild.GenerateCode();

        content += prefix;
        content += node.Value;

        return content;
    }
}
