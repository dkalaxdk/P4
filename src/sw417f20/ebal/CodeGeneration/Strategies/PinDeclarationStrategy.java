package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.SyntaxAnalysis.Node;

public class PinDeclarationStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node) {

        //Makes code for the createPin function.
        return node.FirstChild.Next.GenerateCode();
    }
}
