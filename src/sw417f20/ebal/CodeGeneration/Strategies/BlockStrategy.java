package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.CodeGeneration.Utility.ArduinoSystem;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class BlockStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node, ArduinoSystem arduinoSystem) {

        StringBuilder block = new StringBuilder();

        block.append("{\n");

        arduinoSystem.Indentation++;
        block.append(GenerateCodeForLinkedList(node.FirstChild, arduinoSystem));
        arduinoSystem.Indentation--;

        block.append(addIndentation(arduinoSystem.Indentation));
        block.append("}\n");


        return block.toString();
    }
}
