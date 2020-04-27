package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.SyntaxAnalysis.Node;

public class PinDeclarationStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node) {
        String content = "";

        //Makes code for the createPin function.
        content += node.FirstChild.Next.GenerateCode();

        return content;
    }
}
