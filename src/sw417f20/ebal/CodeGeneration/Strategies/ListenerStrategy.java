package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.SyntaxAnalysis.Node;

public class ListenerStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node) {
        String content = "";
        String pinName = node.FirstChild.Value;
        //Finding the pinType trough definitionReference.
        Node pinType = node.FirstChild.DefinitionReference.FirstChild.Next.FirstChild.Next;
        //Finding the pinNumber trough definitionReference.
        String pinNumber = node.FirstChild.DefinitionReference.FirstChild.Next.FirstChild.Next.Next.Next.GenerateCode();

        //Instantiating a pinName with corresponding pin number.
        content += "int " + pinName + " = " + pinNumber +";\n";

        //If the pinType is PWM then it needs to add digitalRead to content.
        if (pinType.Type == Node.NodeType.PWM){
            content += "pinValue" + " = digitalRead(" + pinName + ");\n";
        }
        else {
            content += "pinValue" + " = " + pinType.GenerateCode() +"Read(" + pinName + ");\n";
        }

        // Generate code for block
        content += node.FirstChild.Next.GenerateCode();
        return content;
    }
}
