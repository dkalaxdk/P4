package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.SyntaxAnalysis.Node;

public class ListenerStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node) {
        String content = "";
        String pinName = node.FirstChild.Value;
        //Finding the pinType through definitionReference.
        Node pinType = node.FirstChild.DefinitionReference.FirstChild.Next.FirstChild.Next;
        //Finding the pinNumber through definitionReference.
        String pinNumber = node.FirstChild.DefinitionReference.FirstChild.Next.FirstChild.Next.Next.Next.GenerateCode();

        //Instantiating a pinName with corresponding pin number.
        content += "int " + pinName + " = " + pinNumber +";\n";

        content += "pinValue" + " = " + pinType.GenerateCode() +"Read(" + pinName + ");\n";

        // Generate code for block
        content += node.FirstChild.Next.GenerateCode();
        return content;
    }
}
