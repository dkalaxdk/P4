package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.SyntaxAnalysis.AST;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class IOTypeStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node) {
        String content = "";

        if (node.Type == AST.NodeType.Input){
            content = "INPUT";
        }
        else if (node.Type == AST.NodeType.Output){
            content = "OUTPUT";
        }

        return content;
    }
}
