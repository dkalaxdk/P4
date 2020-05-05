package sw417f20.ebal.CodeGeneration.Utility;

import sw417f20.ebal.SyntaxAnalysis.Node;

import java.util.ArrayList;

public abstract class ArduinoBoard {
    // Global
    public ArrayList<String> PinDeclarations;
    public ArrayList<String> EventDeclarations;

    // Setup
    public ArrayList<String> PinInstantiations;
    public ArrayList<String> EventInstantiations;

    // Loop
    public ArrayList<String> Loop;

    protected String libraries = "#include <ebal.h> \n#include <ebalEvent.h> \n#include <ebalPin.h>\n\n";
    int indentation = 0;

    public ArduinoBoard() {
        PinDeclarations = new ArrayList<>();
        EventDeclarations = new ArrayList<>();

        PinInstantiations = new ArrayList<>();
        EventInstantiations = new ArrayList<>();

        Loop = new ArrayList<>();
    }

    public abstract void AddEventDeclaration(Event event);

    protected String AddArray(ArrayList<String> list) {
        StringBuilder stringBuilder = new StringBuilder();

        for (String str : list) {
            for (int i = 0; i < indentation; i++) {
                stringBuilder.append("\t");
            }
            stringBuilder.append(str);
        }

        return stringBuilder.toString();
    }

}
