package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.SyntaxAnalysis.Node;

public class PinDeclarationStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node) {
        String content = "int ";
        String pinNumber = node.FirstChild.Next.FirstChild.Next.Next.Next.GenerateCode();

        content += node.FirstChild.Value + " = " + pinNumber +";\n";

        content = node.FirstChild.Next.GenerateCode();

        return content;
    }
}
