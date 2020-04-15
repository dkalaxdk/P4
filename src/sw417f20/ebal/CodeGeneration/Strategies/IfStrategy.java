package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.SyntaxAnalysis.AST;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class IfStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node) {
        String content = "";
        String expression = node.FirstChild.GenerateCode();
        String block = node.FirstChild.Next.GenerateCode();
        Node thirdChild = node.Next.Next;

        content += "if(" + expression + ")\n";
        content += block;

        if (thirdChild.Type == AST.NodeType.Empty){
            return content;
        }
        else{
            content += "else" + thirdChild.GenerateCode();
        }

        return content;
    }
}
