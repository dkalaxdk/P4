package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.SyntaxAnalysis.AST;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class PinTypeStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node) {
        String content = "";

        if (node.Type == AST.NodeType.Digital){
            content += "digital";
        }
        else if (node.Type == AST.NodeType.Analog){
            content += "analog";
        }
        //TODO hvilken write og read har PWM
        else if (node.Type == AST.NodeType.PWM){
            content += "";
        }

        return content;
    }
}
