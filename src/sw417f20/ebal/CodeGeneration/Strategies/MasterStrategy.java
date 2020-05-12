package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.SyntaxAnalysis.Node;

public class MasterStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node) {

        Node child = node.FirstChild;

        while (child.Type != Node.NodeType.Initiate) {
            String declaration = child.generateCode();
            arduinoSystem.Master.VariableDeclarations.append(declaration);
            child = child.Next;
        }

        Node initiate = child;
        initiate.generateCode();

        Node listeners = child.Next;
        GenerateCodeForLinkedList(listeners);

        return "";
    }
}
