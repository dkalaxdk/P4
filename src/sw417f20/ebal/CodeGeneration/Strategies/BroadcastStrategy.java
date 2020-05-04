package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.CodeGeneration.Utility.ArduinoSystem;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class BroadcastStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node, ArduinoSystem arduinoSystem) {
//        String content = "";
        //The event used as a parameter.
        //TODO event skal måske laves om hvis vi håndtere events på en anden måde.
//        String event = node.Next.Value;
        //A list of all the slaves that have event as events.
//        ArrayList<Slave> slaves = eventDictionary.GetEventAssociatedSlaves(event);

        //iterating through the list of slaves
//        for (Slave slave : slaves) {
//            //Sending the event by the use of the slaves ID and the char array that represents the event.
//            content += "Wire.beginTransmission(" + slave.getID() + ");\n";
//            content += "Wire.write(" + event + ");\n";
//            content += "Wire.endTransmission();\n";
//        }

        String eventName = node.Next.Value;

        return addIndentation(arduinoSystem.Indentation) + eventName + ".broadcast();\n";
    }
}
