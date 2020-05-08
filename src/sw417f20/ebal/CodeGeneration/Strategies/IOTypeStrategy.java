package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.CodeGeneration.Utility.ArduinoSystem;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class IOTypeStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node) {

        if (node.Type == Node.NodeType.Input){
            return "input";
        }
        else if (node.Type == Node.NodeType.Output){
            return "output";
        }
        else {
            return "ERROR";
        }
    }
}
