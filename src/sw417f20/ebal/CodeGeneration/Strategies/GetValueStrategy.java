package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.CodeGeneration.Utility.ArduinoSystem;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class GetValueStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node, ArduinoSystem arduinoSystem) {
//        //TODO find ud af hvordan man for get value fra en event
//        String content = "";
//        //TODO find ud af hvad pin nummer den har
//        String pinNumber = node.Next.GenerateCode(arduinoSystem);
//        //TODO find ud af om pin er digital eller analog
//        String pinType = "digital";
//        String output = node.Next.GenerateCode(arduinoSystem);
//
//        content += pinType + "Read(" + pinNumber + ");\n";
//
//        return content;

        String name = node.Next.Value;

        return name + ".getValue()";
    }
}
