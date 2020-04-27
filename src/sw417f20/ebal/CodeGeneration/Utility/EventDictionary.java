package sw417f20.ebal.CodeGeneration.Utility;

import sw417f20.ebal.SyntaxAnalysis.Node;
import sw417f20.ebal.CodeGeneration.NodeList;

import java.util.Dictionary;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Class that implements a dictionary of events which can be addressed by their names.
 */
public class EventDictionary {
    /**
     * The dictionary that EventDictionary uses to store the events in.
     */
    private final Dictionary<String, Event> eventDictionary; //TODO Find ud af hvordan man beholder events der hedder det samme
    /**
     * Constructor of a EventDictionary.
     * @param node          The root node of the AST to be read for events.
     */
    public EventDictionary(Node node) {
        eventDictionary = new Hashtable<>();
        MakeEventDictionary(node);
    }

    /**
     * Method that links events and slaves in an AST.
     * @param node          The root node of the AST whose events and slaves should be linked.
     */
    public void MakeEventDictionary(Node node) {
        NodeList nodeList = new NodeList();
        int slaveID = 0;
        int eventID = 0;

        //Visit slaves.
        Node currentSlaveNode = node.FirstChild.Next;
        while (!currentSlaveNode.IsEmpty()) {
            nodeList.Visit(currentSlaveNode);
            currentSlaveNode = currentSlaveNode.Next;
        }

        for (Node slaveNode : nodeList.nodeList) {
            Slave slave = new Slave(slaveNode.FirstChild.Value, slaveID++);

            //If There are no EventHandlers, continue to the next slave
            if (slaveNode.FirstChild.Next.Next.IsEmpty()) continue;

            Node eventNode = slaveNode.FirstChild.Next.Next;
            Event event = new Event(eventNode.FirstChild.Value, eventID++, "" + 0);
            event.AddSlave(slave);
            AddEvent(event.GetName(), event);

            while (!eventNode.Next.IsEmpty()) {
                eventNode = eventNode.Next;
                event = new Event(eventNode.FirstChild.Value, eventID++, "" + 0);
                event.AddSlave(slave);
                AddEvent(event.GetName(), event);
            }
        }
    }

    /**
     * Method that puts an existing event into the dictionary.
     * @param eventName     Name of the event (Key).
     * @param event         The actual event (Value).
     */
    public void AddEvent(String eventName, Event event) {
        eventDictionary.put(eventName, event);
    }

    /**
     * Method that edits the value of an event.
     * @param eventName     Name of the event to be edited (Key).
     * @param eventValue    The value the events value should be changed to.
     */
    public void EditEventValue(String eventName, String eventValue) {
        eventDictionary.get(eventName).SetValue(eventValue);
    }

    /**
     * Getter for the ID of an event.
     * @param eventName     Name of the event to get from (Key).
     * @return              Returns a string representing the events ID.
     */
    public int GetEventID(String eventName) {
        return eventDictionary.get(eventName).GetID();
    }

    /**
     * Getter for the value of an event.
     * @param eventName     Name of the event to get from (Key).
     * @return              Returns a string representing the events value.
     */
    public String GetEventValue(String eventName) {
        return eventDictionary.get(eventName).GetValue();
    }

    /**
     * Getter for the list of slaves associated with an event.
     * @param eventName     Name of the event to get from (Key).
     * @return              Returns an ArrayList of slaves that has an EventHandler for the event.
     */
    public ArrayList<Slave> GetEventAssociatedSlaves(String eventName) {
        return eventDictionary.get(eventName).GetAssociatedSlaves();
    }
}
