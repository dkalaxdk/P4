package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.CodeGeneration.Utility.Slave;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class SlaveStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node) {

        Node child = node.FirstChild.Next;

        Slave slave = arduinoSystem.SlaveList.get(node.ArduinoID);

        while (child.Type != Node.NodeType.Initiate) {
            String declaration = child.generateCode();
            slave.VariableDeclarations.append(declaration);
            child = child.Next;
        }

        Node initiate = child;
        initiate.generateCode();

        Node eventHandlers = child.Next;
        GenerateCodeForLinkedList(eventHandlers);

        return "";
    }
}
