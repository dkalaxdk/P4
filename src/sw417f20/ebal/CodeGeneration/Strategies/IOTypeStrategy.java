package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.SyntaxAnalysis.Node;

public class IOTypeStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node) {
        String content = "";

        if (node.Type == Node.NodeType.Input){
            content = "INPUT";
        }
        else if (node.Type == Node.NodeType.Output){
            content = "OUTPUT";
        }

        return content;
    }
}
