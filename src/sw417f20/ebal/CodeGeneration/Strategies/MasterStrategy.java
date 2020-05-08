package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.CodeGeneration.Utility.ArduinoSystem;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class MasterStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node, ArduinoSystem arduinoSystem) {

        Node child = node.FirstChild;

        while (child.Type != Node.NodeType.Initiate) {
            String declaration = child.GenerateCode(arduinoSystem);
            arduinoSystem.Master.VariableDeclarations.append(declaration);
            child = child.Next;
        }

        Node initiate = child;
        initiate.GenerateCode(arduinoSystem);

        Node listeners = child.Next;
        GenerateCodeForLinkedList(listeners, arduinoSystem);

        return "";
    }
}
