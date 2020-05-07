package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.CodeGeneration.Utility.ArduinoSystem;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class BlockStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node, ArduinoSystem arduinoSystem) {

        StringBuilder block = new StringBuilder();

        block.append("{\n");

        arduinoSystem.Indent++;
        block.append(GenerateCodeForLinkedList(node.FirstChild, arduinoSystem));
        arduinoSystem.Indent--;

        block.append(addIndent(arduinoSystem.Indent));
        block.append("}\n");

        return block.toString();
    }
}
