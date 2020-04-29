package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.SyntaxAnalysis.Node;

public class WriteStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node) {
        String content = "";
        //Finding the pinNumber through definitionReference.
        String pinNumber = node.Next.DefinitionReference.FirstChild.Next.FirstChild.Next.Next.Next.GenerateCode();
        //Finding the pinType through definitionReference.
        Node pinType = node.Next.DefinitionReference.FirstChild.Next.FirstChild.Next;
        String output = node.Next.GenerateCode();

        content += pinType.GenerateCode() + "Write(" + pinNumber + "," + output +");\n";

        return content;
    }
}
