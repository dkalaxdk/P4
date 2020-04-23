package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.CodeGeneration.CodeGeneration;
import sw417f20.ebal.CodeGeneration.Utility.Slave;
import sw417f20.ebal.SyntaxAnalysis.AST;
import sw417f20.ebal.SyntaxAnalysis.Node;

import java.util.ArrayList;

public class AssignmentStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node) {
        String content = "";
        String identifier = node.FirstChild.Value;
        String expression = node.FirstChild.Next.GenerateCode();

        if(node.FirstChild.Type == AST.NodeType.Broadcast){
            String eventValue = node.FirstChild.Next.FirstChild.Next.Value;

            content += "char " + node.FirstChild.Value + "[4]";
            content += " = \"" + eventValue + "\";\n";
        }
        else{
            content += identifier + " = " + expression + ";\n";
        }

        return content;
    }
}
