package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.SyntaxAnalysis.Node;

public class IOTypeStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node) {

        if (node.Type == Node.NodeType.Input){
            return "INPUT";
        }
        else if (node.Type == Node.NodeType.Output){
            return "OUTPUT";
        }
        else {
            return "ERROR";
        }
    }
}
