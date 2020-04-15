package sw417f20.ebal.CodeGeneration.Utility;

import sw417f20.ebal.CodeGeneration.NodeList;
import sw417f20.ebal.SyntaxAnalysis.Node;

import java.util.Dictionary;

/**
 * Class that implements a dictionary of events which can be addressed by their names.
 */
public class EventDictionary {
    /**
     * The dictionary that EventDictionary uses to store the events in.
     */
    private Dictionary<String, Event> dictionary; //TODO Find ud af hvordan man beholder events der hedder det samme

    /**
     * Constructor of EventDictionary.
     * <p>
     *     The constructor goes through an AST and finds all events
     *     and then links them with the slave that handles them.
     * </p>
     * @param node The root node of the AST to be read for events.
     */
    public EventDictionary(Node node) {
        int id = 0;
        NodeList nodeList = new NodeList();

        //Visit slaves
        nodeList.VisitSiblings(node.FirstChild);

        for (Node slave : nodeList.nodeList) {
            Node eventNode;
            Event event;

            //If There are no EventHandlers, continue to the next slave
            if (slave.FirstChild.Next.Next.IsEmpty()) continue;

            eventNode = slave.FirstChild.Next.Next;
            event = new Event(eventNode.FirstChild.Value, "" + id++, "" + 0);
            event.AddSlave(new Slave(slave.FirstChild.Value));
            AddEvent(event.GetName(), event);

            while (!eventNode.Next.IsEmpty()) {
                eventNode = eventNode.Next;
                event = new Event(eventNode.FirstChild.Value, "" + id++, "" + 0);
                event.AddSlave(new Slave(slave.FirstChild.Value));
                AddEvent(event.GetName(), event);
            }
        }
    }

    /**
     * Method that puts an existing event to the dictionary.
     * @param eventName
     * @param event
     */
    public void AddEvent(String eventName, Event event) {
        dictionary.put(eventName, event);
    }

    public void AddNewEvent(String eventName, String eventID, String eventValue) {
        dictionary.put(eventName, new Event(eventName, eventID, eventValue));
    }

    public void EditEventValue(String eventName, String eventValue) {
        dictionary.get(eventName).SetValue(eventValue);
    }

    public String GetEventID(String eventName) {
        return dictionary.get(eventName).GetID();
    }

    public String GetEventValue(String eventName) {
        return dictionary.get(eventName).GetValue();
    }
}
