package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.SyntaxAnalysis.Node;

public class BlockStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node) {
        String content = "";
        content += "{\n";
        content += GenerateCodeForLinkedList(node.FirstChild);
        content += "}\n";
        return content;
    }
}
