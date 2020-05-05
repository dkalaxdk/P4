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

        slave.EventHandlers.add("void " + eventHandlerName + "() " + block);

        slave.ReceiveEvent.add("if (eventID == " + event.GetName() + ".getID()) {\n" +
                "\t\t" + event.GetName() + ".createEvent();\n" +
                "\t\t" + eventHandlerName + "();\n" +
                "\t}\n");

        return "";
    }
}
