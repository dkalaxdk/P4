package sw417f20.ebal.CodeGeneration.Utility;

public abstract class ArduinoBoard {
    // Global
    public StringBuilder PinDeclarations;
    public StringBuilder EventDeclarations;
    public StringBuilder VariableDeclarations;

    // Setup
    public StringBuilder PinInstantiations;
    public StringBuilder EventInstantiations;

    // Loop
    public StringBuilder Loop;

    // Include the EBAL c++ libraries
    protected String libraries = "#include <ebal.h> \n#include <ebalEvent.h> \n#include <ebalPin.h>\n\n";

    public ArduinoBoard() {
        PinDeclarations = new StringBuilder();
        EventDeclarations = new StringBuilder();
        VariableDeclarations = new StringBuilder();

        PinInstantiations = new StringBuilder();
        EventInstantiations = new StringBuilder();

        Loop = new StringBuilder();
    }
}
