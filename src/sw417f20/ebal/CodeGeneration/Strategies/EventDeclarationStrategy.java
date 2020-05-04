package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.CodeGeneration.Utility.ArduinoSystem;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class EventDeclarationStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node, ArduinoSystem arduinoSystem) {
//        String content = "";
//        //TODO skal laves om så den passer med måden vi håndtere events på.
//
//        Node nextChild = node.FirstChild.Next;
//
//        //Initialization if there is an expression as the next node.
//        if(!nextChild.IsEmpty()) {
//            //The value that is assigned to the event
//            String eventValue = node.FirstChild.Next.FirstChild.Next.Value;
//
//            //The event is represented as a char array.
//            content += "char " + node.FirstChild.Value + "[4]";
//            content += " = \"" + eventValue + "\";\n";
//        }
//        else {
//            //Declare the identifier, the event is represented as a char array
//            content += content += "char " + node.FirstChild.Value + "[4];\n";
//        }
//        return content;

        arduinoSystem.AddEventDeclaration(node);

        String eventName = node.FirstChild.Value;
        String expression = node.FirstChild.Next.FirstChild.Next.GenerateCode(arduinoSystem);

        return eventName + ".createEvent(" + expression + ");";
    }
}
