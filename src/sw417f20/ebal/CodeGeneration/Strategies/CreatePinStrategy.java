package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.SyntaxAnalysis.Node;

public class CreatePinStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node) {
        String content = "";
        String pinNumber = node.Next.Next.Next.GenerateCode();
        String pinType = node.Next.Next.GenerateCode();

        //Creating a pin with the pinMode function.
        content += "pinMode(" + pinNumber + "," + pinType + ");\n";

        return content;
    }
}
