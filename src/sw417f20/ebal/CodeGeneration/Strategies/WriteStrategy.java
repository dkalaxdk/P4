package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.CodeGeneration.Utility.ArduinoSystem;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class WriteStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node, ArduinoSystem arduinoSystem) {
//        String content = "";
//        //Finding the pinNumber through definitionReference.
//        String pinNumber = node.Next.DefinitionReference.FirstChild.Next.FirstChild.Next.Next.Next.GenerateCode(arduinoSystem);
//        //Finding the pinType through definitionReference.
//        Node pinType = node.Next.DefinitionReference.FirstChild.Next.FirstChild.Next;
//        String output = node.Next.GenerateCode(arduinoSystem);
//
//        content += pinType.GenerateCode(arduinoSystem) + "Write(" + pinNumber + "," + output +");\n";
//
//        return content;

        String pinName = node.Next.Value;
        String value = node.Next.Next.Value;

        return addIndentation(arduinoSystem.Indentation) + pinName + ".write(" + value + ");\n";
    }
}
