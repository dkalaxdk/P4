package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.SyntaxAnalysis.Node;

public class BlockStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node) {

        StringBuilder block = new StringBuilder();

        block.append("{\n");

        arduinoSystem.Indent++;
        block.append(GenerateCodeForLinkedList(node.FirstChild));
        arduinoSystem.Indent--;

        block.append(AddIndent(arduinoSystem.Indent));
        block.append("}\n");

        return block.toString();
    }
}
