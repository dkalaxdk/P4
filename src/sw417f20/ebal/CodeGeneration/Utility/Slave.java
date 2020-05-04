package sw417f20.ebal.CodeGeneration.Utility;

import sw417f20.ebal.SyntaxAnalysis.Node;

import java.util.ArrayList;

/**
 * Class that represents a slave during code generation.
 */
public class Slave extends ArduinoBoard{

    private int eventHandlerCount = 0;
    public ArrayList<String> EventHandlers; // Name: event + EventHandler + eventHandlerCount

    public ArrayList<String> ReceiveEvent;

    @Override
    public void AddBlock(Node node, ArduinoSystem arduinoSystem) {
        String eventName = node.FirstChild.Value;

        Event event = arduinoSystem.eventDictionary.get(eventName);

        String eventHandlerName = eventName + "EventHandler" + eventHandlerCount++;

        String block = node.FirstChild.Next.GenerateCode(arduinoSystem);

        this.EventHandlers.add("void " + eventHandlerName + "() " + block);

        this.ReceiveEvent.add("if (eventID == " + event.GetName() + ".getID()) {\n" +
                event.GetName() + ".createEvent();\n" +
                eventHandlerName + "();\n" +
                "}\n");
    }

    @Override
    public void AddEventDeclaration(Event event) {
        this.EventDeclarations.add(event.GetType() + " " + event.GetName() + ";");

        this.EventInstantiations.add(event.GetName() + ".setID(" + event.GetID() + ");");
    }

    @Override
    public String toString() {
        StringBuilder slaveBuilder = new StringBuilder();

        slaveBuilder.append(libraries);

        slaveBuilder.append(AddArray(PinDeclarations));
        slaveBuilder.append(AddArray(EventDeclarations));

        slaveBuilder.append("void setup() {\n");
        slaveBuilder.append(AddArray(PinInstantiations));
        slaveBuilder.append(AddArray(EventInstantiations));
        slaveBuilder.append("Wire.begin(").append(ID).append(");\n");
        slaveBuilder.append("Wire.onReceive(receiveEvent);\n");
        slaveBuilder.append("\n}\n");

        slaveBuilder.append(AddArray(EventHandlers));

        slaveBuilder.append("void receiveEvent(int howMany) {\n");
        slaveBuilder.append("char eventID = Wire.read();\n");
        slaveBuilder.append(AddArray(ReceiveEvent));
        slaveBuilder.append("\n}\n");

        slaveBuilder.append("void loop() {\n");
        slaveBuilder.append(AddArray(Loop));
        slaveBuilder.append("\n}\n");

        return slaveBuilder.toString();
    }

    private String Name;
    private int ID;
    public Node SlaveNode;

    /**
     * Constructor of a slave.
     * @param name  The name the slave should have.
     * @param id    The ID the slave should have.
     */
    public Slave(String name, int id, Node node) {
        this.Name = name;
        this.ID = id;
        this.SlaveNode = node;

        EventHandlers = new ArrayList<>();
        ReceiveEvent = new ArrayList<>();
    }

    /**
     * Getter for the slaves name.
     * @return      Returns the name of a slave.
     */
    public String GetName() {
        return Name;
    }

    /**
     * Setter for the slaves name.
     * @param name  The name the slaves name should be set to.
     */
    public void SetName(String name) {
        this.Name = name;
    }

    /**
     * Getter for the slaves ID.
     * @return      Returns the ID of a slave.
     */
    public int getID() {
        return ID;
    }

    /**
     * Setter for the slaves ID.
     * @param id    The ID the slaves ID should be set to.
     */
    public void setID(int id) {
        this.ID = id;
    }


}
