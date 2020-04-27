package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.SyntaxAnalysis.Node;

public class WriteStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node) {
        String content = "";
        //Finding the pinNumber trough definitionReference.
        String pinNumber = node.Next.DefinitionReference.FirstChild.Next.FirstChild.Next.Next.Next.GenerateCode();
        //Finding the pinType trough definitionReference.
        Node pinType = node.Next.DefinitionReference.FirstChild.Next.FirstChild.Next;
        String output = node.Next.GenerateCode();

        //If the pinType is PWM then it needs to add analogWrite to content.
        if (pinType.Type == Node.NodeType.PWM){
            content += "analogWrite(" + pinNumber + "," + output +");\n";
        }
        else{
            content += pinType.GenerateCode() + "Write(" + pinNumber + "," + output +");\n";
        }


        return content;
    }
}
