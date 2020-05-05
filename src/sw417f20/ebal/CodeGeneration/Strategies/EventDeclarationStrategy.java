package sw417f20.ebal.CodeGeneration.Strategies;

import sw417f20.ebal.CodeGeneration.Utility.ArduinoSystem;
import sw417f20.ebal.CodeGeneration.Utility.Event;
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
            default:
                // Throw error?
        }

        event.SetType(eventType);

        arduinoSystem.Master.AddEventDeclaration(event);

        for (int i : event.AssociatedSlaves) {
            arduinoSystem.SlaveList.get(i).AddEventDeclaration(event);
        }

        String expression = node.FirstChild.Next.FirstChild.Next.GenerateCode(arduinoSystem);

        return addIndentation(arduinoSystem.Indentation) + eventName + ".createEvent(" + expression + ");\n";
    }
}
