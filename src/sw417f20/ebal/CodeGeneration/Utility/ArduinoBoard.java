package sw417f20.ebal.CodeGeneration.Utility;

import sw417f20.ebal.SyntaxAnalysis.Node;

import java.util.ArrayList;

public abstract class ArduinoBoard {
    // Global
    public StringBuilder PinDeclarations;
    public StringBuilder EventDeclarations;

    // Setup
    public StringBuilder PinInstantiations;
    public StringBuilder EventInstantiations;

    // Loop
    public StringBuilder Loop;

    protected String libraries = "#include <ebal.h> \n#include <ebalEvent.h> \n#include <ebalPin.h>\n\n";

    public ArduinoBoard() {
        PinDeclarations = new StringBuilder();
        EventDeclarations = new StringBuilder();

        PinInstantiations = new StringBuilder();
        EventInstantiations = new StringBuilder();

        Loop = new StringBuilder();
    }
}
