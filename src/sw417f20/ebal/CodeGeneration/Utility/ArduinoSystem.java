package sw417f20.ebal.CodeGeneration.Utility;

import sw417f20.ebal.CodeGeneration.OutputFileGenerator;
import sw417f20.ebal.ContextAnalysis.Symbol;
import sw417f20.ebal.SyntaxAnalysis.Node;

import java.io.IOException;
import java.util.*;

public class ArduinoSystem {
    private Node root;

    public Master master;

    public ArrayList<Slave> slaveList;

    public Dictionary<String, Event> eventDictionary;

    public int Indentation = 0;

    public ArduinoSystem(Node root) {
        master = new Master();
        slaveList = new ArrayList<Slave>();
        this.root = root;

        eventDictionary = new Hashtable<>();

        getSlaves(root);
        getEvents(root);
    }

    public void Generate() {
        root.GenerateCode(this);

        Print();
    }

    private void getSlaves(Node root) {
        Node slaves = root.FirstChild.Next;
        int counter = 0;

        while (!slaves.IsEmpty()) {
            slaveList.add(new Slave(slaves.FirstChild.Value, counter++, slaves));
            slaves = slaves.Next;
        }
    }

    private void getEvents(Node root) {
        Node slaves = root.FirstChild.Next;
        int eventCounter = 0;
        int slaveCounter = 0;

        while (!slaves.IsEmpty()) {

            Node eventHandlers = slaves.FirstChild.Next.Next;

            while (!eventHandlers.IsEmpty()) {
                String eventName = eventHandlers.FirstChild.Value;
                Event n = eventDictionary.get(eventName);

                // If we don't find an event
                if (n == null) {
                    eventDictionary.put(eventName, new Event(eventName, eventCounter++));
                }

                eventDictionary.get(eventName).AssociatedSlaves.add(slaveCounter);

                eventHandlers = eventHandlers.Next;
            }

            slaveCounter++;
            slaves = slaves.Next;
        }
    }


    public void AddPinDeclaration(Node node) {

        if (node.ArduinoID == -1) {
            master.AddPinDeclaration(node, this);
        }
        else {
            slaveList.get(node.ArduinoID).AddPinDeclaration(node, this);
        }
    }

    public void AddEventDeclaration(Node node) {
        String eventName = node.FirstChild.Value;
        Event event = eventDictionary.get(eventName);

        if (event == null) {
            event = new Event(eventName, -1);
            eventDictionary.put(eventName, event);
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

        master.AddEventDeclaration(event);

        for (int i : event.AssociatedSlaves) {
            slaveList.get(i).AddEventDeclaration(event);
        }
    }

    public void AddListener(Node node) {
        master.AddBlock(node, this);
    }

    public void AddEventHandler(Node node) {
        slaveList.get(node.ArduinoID).AddBlock(node, this);
    }


    public void Print() {
        OutputFileGenerator generator = new OutputFileGenerator();

        try {
            generator.AddFile("master", master.toString());

            for (Slave slave : slaveList) {
                generator.AddFile("slave" + slave.getID(), slave.toString());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }


    }
}
