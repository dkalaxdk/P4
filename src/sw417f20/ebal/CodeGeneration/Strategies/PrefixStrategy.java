package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.SyntaxAnalysis.Node;

public class PrefixStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node) {

        if (node.Type == Node.NodeType.PrefixNot){
            return "!";
        }
        else if (node.Type == Node.NodeType.PrefixMinus){
            return "-";
        }
        else {
            return "ERROR";
        }
    }
}
