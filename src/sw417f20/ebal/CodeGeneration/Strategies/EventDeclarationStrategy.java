package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.CodeGeneration.Utility.ArduinoSystem;
import sw417f20.ebal.CodeGeneration.Utility.Event;
import sw417f20.ebal.CodeGeneration.Utility.Master;
import sw417f20.ebal.CodeGeneration.Utility.Slave;
import sw417f20.ebal.ContextAnalysis.Symbol;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class EventDeclarationStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node) {

        // Get the event from the system
        String eventName = node.FirstChild.Value;
        Event event = arduinoSystem.EventMap.get(eventName);

        // If the event wasn't in the system, add it with an ID of -1,
        // because it is never handled by a slave
        if (event == null) {
            event = new Event(eventName, -1);
            arduinoSystem.EventMap.put(eventName, event);
        }

        // Get a reference to the createEvent call,
        // and the type of that function's input
        Node call = node.FirstChild.Next;
        Symbol.SymbolType type = call.FirstChild.Next.DataType;

        String eventType = "";

        // Get the correct event type based on the type of the parameter of createEvent
        switch (type) {
            case FLOAT:
                eventType = "floatEvent";
                break;
            case INT:
                eventType = "intEvent";
                break;
            case BOOL:
                eventType = "boolEvent";
                break;
        }

        // Give the event the found type
        event.SetType(eventType);

        // Add the event to the master
        addEventToMaster(arduinoSystem.Master, event);

        // Add the event to the slaves that handle it
        for (int i : event.AssociatedSlaves) {
            addEventToSlave(arduinoSystem.SlaveList.get(i), event);
        }

        // Get the expression that is the input of createEvent
        String expression = node.FirstChild.Next.FirstChild.Next.GenerateCode();

        return addIndent(arduinoSystem.Indent) + eventName + ".createEvent(" + expression + ");\n";
    }

    // Adds the input event to the master
    private void addEventToMaster(Master master, Event event) {

        // Add the event to the master's event declarations
        master.EventDeclarations
                .append(event.GetType())
                .append(" ")
                .append(event.GetName())
                .append(";\n");

        // Instantiate the event with an ID
        master.EventInstantiations
                .append("\t")
                .append(event.GetName())
                .append(".setID(")
                .append(event.GetID())
                .append(");\n");

        // Add the associated slaves to the event
        for (int i : event.AssociatedSlaves) {
            master.AssociatedSlaves
                    .append("\t")
                    .append(event.GetName())
                    .append(".addSlave(")
                    .append(i)
                    .append(");\n");
        }
    }

    // Adds the input event to the slave
    private void addEventToSlave(Slave slave, Event event) {

        // Add the event to the slave's event declarations
        slave.EventDeclarations
                .append(event.GetType())
                .append(" ")
                .append(event.GetName())
                .append(";\n");

        // Instantiate the event with an ID
        slave.EventInstantiations
                .append("\t")
                .append(event.GetName())
                .append(".setID(")
                .append(event.GetID())
                .append(");\n");
    }
}
