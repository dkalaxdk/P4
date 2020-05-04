package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.CodeGeneration.Utility.ArduinoSystem;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class SlaveStrategy extends CodeGenerationStrategy {
    private int CommandSize = 5;
    @Override
    public String GenerateCode(Node node, ArduinoSystem arduinoSystem) {
//        String content = "";
//        String slaveName = node.FirstChild.Value;
//
//        int slaveID = slaveDictionary.GetSlaveID(slaveName);
//
//        content += "#include <Wire.h>\n";
//        content += "const int commandSize =" + CommandSize + ";\n"; //TODO: Determine command size
//        content += "char input[commandSize];\n";
//
//        content += "void setup(){\n";
//        content += "Wire.begin(" + slaveID + ");\n";
////        content += "Serial.begin(9600);\n";
//        content += "Wire.onReceive(receiveEvent);\n";
//        //generate code for the initiate block
//        content += node.FirstChild.Next.GenerateCode(arduinoSystem);
//        content += "}\n";
//
//        content += "void loop() {\n";
//            // Is this needed
//        content += "}\n";
//        //TODO: Determine how to distribute events
//        content += "void receiveEvent(int eventSize)\n" +
//                "{\n" +
//                "  for (int i = 0; i < commandSize-1 || i < eventSize; i++) {\n" +
//                "    char c = Wire.read();\n" +
//                "    input[i] = c;\n" +
//                "  }\n";
//
//        // Generate and append code for all the siblings of the first node
//        // These nodes should all be EventHandlers
//        content += GenerateCodeForLinkedList(node.FirstChild.Next.Next, arduinoSystem);

//        content += "}";
//        return content;

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
