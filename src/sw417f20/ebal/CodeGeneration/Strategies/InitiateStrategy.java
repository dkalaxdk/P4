package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.SyntaxAnalysis.Node;

public class InitiateStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node) {
        String content = "";

        // Generate and append code for PinDeclarations
        content += GenerateCodeForLinkedList(node.FirstChild);

        return content;
    }
}
