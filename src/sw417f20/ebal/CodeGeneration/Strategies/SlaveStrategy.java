package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.CodeGeneration.Utility.ArduinoSystem;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class SlaveStrategy extends CodeGenerationStrategy {
    private int CommandSize = 5;
    @Override
    public String GenerateCode(Node node, ArduinoSystem arduinoSystem) {

        Node initiate = node.FirstChild.Next;

        Node pinDeclarations = initiate.FirstChild.FirstChild;

        while (!pinDeclarations.IsEmpty()) {
            arduinoSystem.AddPinDeclaration(pinDeclarations);
            pinDeclarations = pinDeclarations.Next;
        }

        Node eventHandlers = node.FirstChild.Next.Next;

        while(!eventHandlers.IsEmpty()) {
            arduinoSystem.AddEventHandler(eventHandlers);
            eventHandlers = eventHandlers.Next;
        }

        return "";
    }
}
