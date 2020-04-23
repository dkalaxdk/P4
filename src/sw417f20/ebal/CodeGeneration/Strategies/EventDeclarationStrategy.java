package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.CodeGeneration.Utility.Slave;
import sw417f20.ebal.SyntaxAnalysis.Node;

import java.util.ArrayList;

public class EventDeclarationStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node) {
        String content = "";

        Node nextChild = node.FirstChild.Next;
        if(!nextChild.IsEmpty()) {
            String eventValue = node.FirstChild.Next.FirstChild.Next.Value;

            content += "char " + node.FirstChild.Value + "[4]";
            content += " = \"" + eventValue + "\";\n";
        }
        else {
            content += content += "char " + node.FirstChild.Value + "[2];";
        }
        content += "\n";
        return content;
    }
}
