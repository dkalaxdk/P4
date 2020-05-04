package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.CodeGeneration.Utility.ArduinoSystem;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class ListenerStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node, ArduinoSystem arduinoSystem) {
//        String content = "";
//        String pinName = node.FirstChild.Value;
//        //Finding the pinType through definitionReference.
//        Node pinType = node.FirstChild.DefinitionReference.FirstChild.Next.FirstChild.Next;
//        //Finding the pinNumber through definitionReference.
//        String pinNumber = node.FirstChild.DefinitionReference.FirstChild.Next.FirstChild.Next.Next.Next.GenerateCode(arduinoSystem);
//
//        //Instantiating a pinName with corresponding pin number.
//        content += "int " + pinName + " = " + pinNumber +";\n";
//
//        content += "pinValue" + " = " + pinType.GenerateCode(arduinoSystem) +"Read(" + pinName + ");\n";
//
//        // Generate code for block
//        content += node.FirstChild.Next.GenerateCode(arduinoSystem);
//        return content;



        return "";
    }
}
