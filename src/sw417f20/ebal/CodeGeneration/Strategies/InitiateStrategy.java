package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.CodeGeneration.Utility.ArduinoSystem;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class InitiateStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node) {

        Node pinDeclarations = node.FirstChild.FirstChild;
        GenerateCodeForLinkedList(pinDeclarations);

        return "";
    }
}
