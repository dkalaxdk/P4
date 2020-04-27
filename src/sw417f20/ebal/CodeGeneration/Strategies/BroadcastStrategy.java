package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.CodeGeneration.Utility.Slave;
import sw417f20.ebal.SyntaxAnalysis.Node;

import java.util.ArrayList;

public class BroadcastStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node) {
        String content = "";
        //The event used as a parameter.
        String event = node.Next.Value;
        //A list of all the slaves that have event as events.
        ArrayList<Slave> slaves = eventDictionary.GetEventAssociatedSlaves(event);

        //iterating through the list of slaves
        for (Slave slave : slaves) {
            //Sending the event by the use of the slaves ID and the char array that represents the event.
            content += "Wire.beginTransmission(" + slave.getID() + ");\n";
            content += "Wire.write(" + event + ");\n";
            content += "Wire.endTransmission();\n";
        }

        return content;
    }
}
