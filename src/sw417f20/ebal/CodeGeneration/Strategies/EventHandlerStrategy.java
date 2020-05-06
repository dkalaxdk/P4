package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.CodeGeneration.Utility.ArduinoSystem;
import sw417f20.ebal.CodeGeneration.Utility.Event;
import sw417f20.ebal.CodeGeneration.Utility.Slave;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class EventHandlerStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node, ArduinoSystem arduinoSystem) {

        Slave slave = arduinoSystem.SlaveList.get(node.ArduinoID);

        String eventName = node.FirstChild.Value;

        Event event = arduinoSystem.EventDictionary.get(eventName);

        String eventHandlerName = eventName + "EventHandler" + slave.EventHandlerCount++;

        String block = node.FirstChild.Next.GenerateCode(arduinoSystem);

        slave.EventHandlers
                .append("void ")
                .append(eventHandlerName)
                .append("() ")
                .append(block)
                .append("\n");

        slave.ReceiveEvent.append("\tif (eventID == ").append(event.GetName()).append(".getID()) {\n")
                .append("\t\t").append(event.GetName()).append(".createEvent();\n")
                .append("\t\t").append(eventHandlerName).append("();\n")
                .append("\t}\n");

        return "";
    }
}
