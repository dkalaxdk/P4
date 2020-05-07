package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.CodeGeneration.Utility.ArduinoSystem;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class SlaveStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node, ArduinoSystem arduinoSystem) {

        Node initiate = node.FirstChild.Next;
        initiate.GenerateCode(arduinoSystem);

        Node eventHandlers = node.FirstChild.Next.Next;
        GenerateCodeForLinkedList(eventHandlers, arduinoSystem);

        return "";
    }
}
