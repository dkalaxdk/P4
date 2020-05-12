package sw417f20.ebal.CodeGeneration.Utility;

import sw417f20.ebal.SyntaxAnalysis.Node;
import java.util.*;

public class ArduinoSystem {

    private final Node root;

    public Master Master;
    public ArrayList<Slave> SlaveList;
    public HashMap<String, Event> EventMap;

    public int Indent = 0;

    public ArduinoSystem(Node root) {
        Master = new Master();
        SlaveList = new ArrayList<>();
        EventMap = new HashMap<>();

        this.root = root;

        FindSlaves(root);
        FindHandledEvents(root);
    }

    /**
     * Generate code for the system
     */
    public void Generate() {
        root.generateCode();
    }

    /**
     * Uses the Master's and the Slaves' toString to make a HashMap that represents the system
     * @return HashMap with the system's master and slaves
     */
    public HashMap<String, String> Print() {

        HashMap<String, String> files = new HashMap<>();

        files.put("master", Master.toString());

        for (Slave slave : SlaveList) {
            files.put("slave_" + slave.GetName(), slave.toString());
        }

        return files;
    }

    // Find the slaves in the AST,
    // add them to the system's list of slaves
    // and give them an ID
    private void FindSlaves(Node root) {
        Node slaves = root.FirstChild.Next;
        int counter = 0;

        while (!slaves.isEmpty()) {
            SlaveList.add(new Slave(slaves.FirstChild.Value, counter++));
            slaves = slaves.Next;
        }
    }

    // Find the events that are handled in EventHandlers,
    // add them to the system's list of events and give them a unique ID
    private void FindHandledEvents(Node root) {
        Node slaves = root.FirstChild.Next;
        int eventCounter = 0;
        int slaveCounter = 0;

        while (!slaves.isEmpty()) {

            Node eventHandlers = slaves.FirstChild;

            // Skip past declarations
            while (eventHandlers.Type != Node.NodeType.Initiate) {
                eventHandlers = eventHandlers.Next;
            }

            // Skip past Initiate
            eventHandlers = eventHandlers.Next;

            while (!eventHandlers.isEmpty()) {
                String eventName = eventHandlers.FirstChild.Value;
                Event n = EventMap.get(eventName);

                // If we don't already have the event in the system, add it
                if (n == null) {
                    EventMap.put(eventName, new Event(eventName, eventCounter++));
                }

                // Add the current slave to the event's list of associated slaves
                EventMap.get(eventName).AssociatedSlaves.add(slaveCounter);

                eventHandlers = eventHandlers.Next;
            }

            slaveCounter++;
            slaves = slaves.Next;
        }
    }
}
