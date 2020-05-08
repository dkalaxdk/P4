package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.CodeGeneration.Utility.ArduinoSystem;
import sw417f20.ebal.CodeGeneration.Utility.Slave;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class SlaveStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node, ArduinoSystem arduinoSystem) {

        Node child = node.FirstChild.Next;

        Slave slave = arduinoSystem.SlaveList.get(node.ArduinoID);

        while (child.Type != Node.NodeType.Initiate) {
            String declaration = child.GenerateCode(arduinoSystem);
            slave.VariableDeclarations.append(declaration);
            child = child.Next;
        }

        Node initiate = child;
        initiate.GenerateCode(arduinoSystem);

        Node eventHandlers = child.Next;
        GenerateCodeForLinkedList(eventHandlers, arduinoSystem);

        return "";
    }
}
