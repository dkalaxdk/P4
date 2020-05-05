package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.CodeGeneration.Utility.ArduinoSystem;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class MasterStrategy extends CodeGenerationStrategy {
    private final int debounce = 300;
    @Override
    public String GenerateCode(Node node, ArduinoSystem arduinoSystem) {

        Node initiate = node.FirstChild;

        Node pinDeclarations = initiate.FirstChild.FirstChild;

        while (!pinDeclarations.IsEmpty()) {
            arduinoSystem.AddPinDeclaration(pinDeclarations);
            pinDeclarations = pinDeclarations.Next;
        }

        Node listeners = node.FirstChild.Next;

        while(!listeners.IsEmpty()) {
            arduinoSystem.AddListener(listeners);
            listeners = listeners.Next;
        }

        return "";
    }
}
