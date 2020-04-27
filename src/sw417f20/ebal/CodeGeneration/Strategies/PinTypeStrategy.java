package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.SyntaxAnalysis.Node;

public class PinTypeStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node) {
        String content = "";

        if (node.Type == Node.NodeType.Digital){
            content += "digital";
        }
        else if (node.Type == Node.NodeType.Analog){
            content += "analog";
        }
        else if (node.Type == Node.NodeType.PWM){
            //Returns nothing since PWM should be either digital or analog depending on the situation.
            //It will be handled in ListenerStrategy and WriteStrategy.
            content += "";
        }

        return content;
    }
}
