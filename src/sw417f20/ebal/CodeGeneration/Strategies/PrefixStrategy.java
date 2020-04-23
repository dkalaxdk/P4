package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.SyntaxAnalysis.Node;

public class PrefixStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node) {
        String content = "";

        if (node.Type == Node.NodeType.PrefixNot){
            content = "!";
        }
        else if (node.Type == Node.NodeType.PrefixMinus){
            content = "-";
        }

        return content;
    }
}
