package sw417f20.ebal.CodeGeneration.Utility;

/**
 * Class that represents a slave during code generation.
 */
public class Slave extends ArduinoBoard{

    public int EventHandlerCount = 0;
    public StringBuilder EventHandlers;

    public StringBuilder ReceiveEvent;

    private final String Name;
    private final int ID;

    /**
     * Constructor of a slave.
     * @param name  The name the slave should have.
     * @param id    The ID the slave should have.
     */
    public Slave(String name, int id) {
        this.Name = name;
        this.ID = id;

        EventHandlers = new StringBuilder();
        ReceiveEvent = new StringBuilder();
    }

    /**
     * Getter for the slaves name.
     * @return      Returns the name of a slave.
     */
    public String GetName() {
        return Name;
    }

    @Override
    public String toString() {
        StringBuilder slaveBuilder = new StringBuilder();

        // EBAL libraries
        slaveBuilder.append(libraries);

        // Global variables
        slaveBuilder
                .append(PinDeclarations.toString())
                .append("\n")
                .append(EventDeclarations.toString())
                .append("\n")
                .append(VariableDeclarations.toString())
                .append("\n");

        // Setup
        slaveBuilder
                .append("void setup() {\n")
                .append(PinInstantiations.toString())
                .append("\n")
                .append(EventInstantiations.toString())
                .append("\n")
                .append("\tWire.begin(").append(ID).append(");\n")
                .append("\tSerial.begin(9600);\n")
                .append("\tWire.onReceive(receiveEvent);\n")
                .append("}\n\n");

        // EventHandlers
        slaveBuilder
                .append(EventHandlers.toString())
                .append("\n");

        // ReceiveEvent
        slaveBuilder
                .append("void receiveEvent(int howMany) {\n")
                .append("\tchar eventID = Wire.read();\n\n")
                .append(ReceiveEvent.toString())
                .append("}\n\n");

        // Loop
        slaveBuilder
                .append("void loop() {\n")
                .append(Loop.toString())
                .append("}\n");

        return slaveBuilder.toString();
    }
}
