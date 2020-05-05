package sw417f20.ebal.CodeGeneration.Utility;

import sw417f20.ebal.SyntaxAnalysis.Node;

import java.util.ArrayList;

/**
 * Class that represents a slave during code generation.
 */
public class Slave extends ArduinoBoard{

    public int EventHandlerCount = 0;
    public ArrayList<String> EventHandlers;

    public ArrayList<String> ReceiveEvent;

    @Override
    public void AddEventDeclaration(Event event) {
        this.EventDeclarations.add(event.GetType() + " " + event.GetName() + ";\n");

        this.EventInstantiations.add(event.GetName() + ".setID(" + event.GetID() + ");\n");
    }

    @Override
    public String toString() {
        StringBuilder slaveBuilder = new StringBuilder();

        slaveBuilder.append(libraries);

        slaveBuilder.append(AddArray(PinDeclarations));
        slaveBuilder.append("\n");
        slaveBuilder.append(AddArray(EventDeclarations));
        slaveBuilder.append("\n");

        slaveBuilder.append("void setup() {\n");
        indentation++;
        slaveBuilder.append(AddArray(PinInstantiations));
        slaveBuilder.append("\n");
        slaveBuilder.append(AddArray(EventInstantiations));
        slaveBuilder.append("\n");
        indentation--;
        slaveBuilder.append("\tWire.begin(").append(ID).append(");\n");
        slaveBuilder.append("\tWire.onReceive(receiveEvent);\n");
        slaveBuilder.append("}\n\n");

        slaveBuilder.append(AddArray(EventHandlers));
        slaveBuilder.append("\n");

        indentation++;
        slaveBuilder.append("void receiveEvent(int howMany) {\n");
        slaveBuilder.append("\tchar eventID = Wire.read();\n\n");
        slaveBuilder.append(AddArray(ReceiveEvent));
        slaveBuilder.append("}\n\n");

        slaveBuilder.append("void loop() {\n");
        slaveBuilder.append(AddArray(Loop));
        slaveBuilder.append("}\n");
        indentation--;

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
