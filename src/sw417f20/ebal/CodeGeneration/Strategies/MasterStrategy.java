package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.CodeGeneration.Utility.ArduinoSystem;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class MasterStrategy extends CodeGenerationStrategy {
    private final int debounce = 300;
    @Override
    public String GenerateCode(Node node, ArduinoSystem arduinoSystem) {
//        String content = "";
//        content += "const long debounce = " + debounce + ";";
//        //TODO Husk at sætte h filer ind
//        content += "void setup(){\n";
//        content += node.FirstChild.GenerateCode(system);
//        content += "Wire.begin();\n";
//        content += "}\n";
//
//        content += "void loop() {\n";
//        //pinValue will be overwritten each time a new listener is called
//        content += "int pinValue;\n";
//        // Generates code for all the EventCreators
//        content += GenerateCodeForLinkedList(node.FirstChild.Next, system);
//        content += "}\n";

//        return content;

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
