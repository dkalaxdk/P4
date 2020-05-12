package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.CodeGeneration.Utility.Event;
import sw417f20.ebal.CodeGeneration.Utility.Slave;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class EventHandlerStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node) {

        // Get the slave that has this EventHandler from the system
        Slave slave = arduinoSystem.SlaveList.get(node.ArduinoID);

        // Get the name of the event that is handled
        String eventName = node.FirstChild.Value;

        // Get the event from the system
        Event event = arduinoSystem.EventMap.get(eventName);

        // Create the unique EventHandler name that is used in the Arduino
        String eventHandlerName = eventName + "EventHandler" + slave.EventHandlerCount++;

        // Generate code for the EventHandler's block
        String block = node.FirstChild.Next.generateCode();

        // Add the EventHandler to the slave's list of EventHandlers with the correct signature
        slave.EventHandlers
                .append("void ")
                .append(eventHandlerName)
                .append("() ")
                .append(block)
                .append("\n");

        // Add the EventHandler to the receiveEvent function
        // so it is called when the correct event is received
        slave.ReceiveEvent.append("\tif (eventID == ").append(event.GetName()).append(".getID()) {\n")
                .append("\t\t").append(event.GetName()).append(".createEvent();\n")
                .append("\t\t").append(eventHandlerName).append("();\n")
                .append("\t}\n");

        return "";
    }
}
