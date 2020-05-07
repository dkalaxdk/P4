package sw417f20.ebal.CodeGeneration.Utility;

/**
 * Class that represents the master during code generation.
 */
public class Master extends ArduinoBoard {

    public int ListenerCount = 0;
    public StringBuilder Listeners;

    public StringBuilder AssociatedSlaves;

    public Master() {
        AssociatedSlaves = new StringBuilder();
        Listeners = new StringBuilder();
    }

    @Override
    public String toString() {
        StringBuilder masterBuilder = new StringBuilder();

        // EBAL libraries
        masterBuilder.append(libraries);

        // Global variables
        masterBuilder
                .append(PinDeclarations.toString())
                .append("\n")
                .append(EventDeclarations.toString())
                .append("\n");

        // Setup
        masterBuilder
                .append("void setup() {\n")
                .append(PinInstantiations.toString())
                .append("\n")
                .append(EventInstantiations.toString())
                .append("\n")
                .append(AssociatedSlaves.toString())
                .append("\n")
                .append("\tWire.begin();\n")
                .append("\tSerial.begin(9600);\n")
                .append("}\n\n");

        // Listeners
        masterBuilder
                .append(Listeners.toString())
                .append("\n");

        // Loop
        masterBuilder
                .append("void loop() {\n")
                .append(Loop.toString())
                .append("}\n");

        return masterBuilder.toString();
    }
}
