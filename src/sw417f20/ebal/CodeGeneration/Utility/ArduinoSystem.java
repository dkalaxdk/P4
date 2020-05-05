package sw417f20.ebal.CodeGeneration.Utility;

import sw417f20.ebal.CodeGeneration.OutputFileGenerator;
import sw417f20.ebal.SyntaxAnalysis.Node;

import java.io.IOException;
import java.util.*;

public class ArduinoSystem {

    private Node root;

    public Master Master;

    public ArrayList<Slave> SlaveList;

    public Dictionary<String, Event> EventDictionary;

    public int Indentation = 0;

    public ArduinoSystem(Node root) {
        Master = new Master();
        SlaveList = new ArrayList<Slave>();
        this.root = root;

        EventDictionary = new Hashtable<>();

        FindSlaves(root);
        FindEvents(root);
    }

    public void Generate() {
        root.GenerateCode(this);

        Print();
    }

    private void FindSlaves(Node root) {
        Node slaves = root.FirstChild.Next;
        int counter = 0;

        while (!slaves.IsEmpty()) {
            SlaveList.add(new Slave(slaves.FirstChild.Value, counter++, slaves));
            slaves = slaves.Next;
        }
    }

    private void FindEvents(Node root) {
        Node slaves = root.FirstChild.Next;
        int eventCounter = 0;
        int slaveCounter = 0;

        while (!slaves.IsEmpty()) {

            Node eventHandlers = slaves.FirstChild.Next.Next;

            while (!eventHandlers.IsEmpty()) {
                String eventName = eventHandlers.FirstChild.Value;
                Event n = EventDictionary.get(eventName);

                // If we don't find an event
                if (n == null) {
                    EventDictionary.put(eventName, new Event(eventName, eventCounter++));
                }

                EventDictionary.get(eventName).AssociatedSlaves.add(slaveCounter);

                eventHandlers = eventHandlers.Next;
            }

            slaveCounter++;
            slaves = slaves.Next;
        }
    }


    public void Print() {
        OutputFileGenerator generator = new OutputFileGenerator();

        System.out.println();

        try {
            generator.AddFile("master", Master.toString());

            for (Slave slave : SlaveList) {
                generator.AddFile("slave_" + slave.GetName(), slave.toString());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }


    }
}
