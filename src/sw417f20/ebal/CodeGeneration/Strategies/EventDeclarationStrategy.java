package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.CodeGeneration.Utility.ArduinoSystem;
import sw417f20.ebal.CodeGeneration.Utility.Event;
import sw417f20.ebal.CodeGeneration.Utility.Master;
import sw417f20.ebal.CodeGeneration.Utility.Slave;
import sw417f20.ebal.ContextAnalysis.Symbol;
import sw417f20.ebal.SyntaxAnalysis.Node;

public class EventDeclarationStrategy extends CodeGenerationStrategy {
    @Override
    public String GenerateCode(Node node, ArduinoSystem arduinoSystem) {

        String eventName = node.FirstChild.Value;
        Event event = arduinoSystem.EventDictionary.get(eventName);

        if (event == null) {
            event = new Event(eventName, -1);
            arduinoSystem.EventDictionary.put(eventName, event);
        }

        Node call = node.FirstChild.Next;
        Symbol.SymbolType type = call.FirstChild.Next.DataType;

        String eventType = "";

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

        event.SetType(eventType);

        addEventToMaster(arduinoSystem.Master, event, arduinoSystem);

        for (int i : event.AssociatedSlaves) {
            addEventToSlave(arduinoSystem.SlaveList.get(i), event, arduinoSystem);
        }

        String expression = node.FirstChild.Next.FirstChild.Next.GenerateCode(arduinoSystem);

        return addIndent(arduinoSystem.Indent) + eventName + ".createEvent(" + expression + ");\n";
    }

    private void addEventToMaster(Master master, Event event, ArduinoSystem arduinoSystem) {
        master.EventDeclarations
                .append(event.GetType())
                .append(" ")
                .append(event.GetName())
                .append(";\n");

        master.EventInstantiations
                .append("\t")
                .append(event.GetName())
                .append(".setID(")
                .append(event.GetID())
                .append(");\n");

        for (int i : event.AssociatedSlaves) {
            master.AssociatedSlaves
                    .append("\t")
                    .append(event.GetName())
                    .append(".addSlave(")
                    .append(i)
                    .append(");\n");
        }
    }

    private void addEventToSlave(Slave slave, Event event, ArduinoSystem arduinoSystem) {
        slave.EventDeclarations
                .append(event.GetType())
                .append(" ")
                .append(event.GetName())
                .append(";\n");

        slave.EventInstantiations
                .append("\t")
                .append(event.GetName())
                .append(".setID(")
                .append(event.GetID())
                .append(");\n");
    }
}
