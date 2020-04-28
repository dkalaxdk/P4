package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.SyntaxAnalysis.Node;

public class LiteralStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node) {
        String content = "";
        String prefix = (node.FirstChild != null) ? node.FirstChild.GenerateCode() : "";

        //If there is a prefix it is added to content before the value in expressions.
        content += prefix;
        content += node.Value;

        return content;
    }
}
