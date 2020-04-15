package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.SyntaxAnalysis.AST;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class PrefixStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node) {
        String content = "";

        if (node.Type == AST.NodeType.PrefixNot){
            content = "!";
        }
        else if (node.Type == AST.NodeType.PrefixMinus){
            content = "-";
        }

        return content;
    }
}
