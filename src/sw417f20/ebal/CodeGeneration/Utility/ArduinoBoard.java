package sw417f20.ebal.CodeGeneration.Utility;

import sw417f20.ebal.SyntaxAnalysis.Node;

import java.util.ArrayList;

public abstract class ArduinoBoard {
    // Global
    public ArrayList<String> PinDeclarations;
    public ArrayList<String> EventDeclarations;

    // Setup
    public ArrayList<String> PinInstantiations; //createPin
    public ArrayList<String> EventInstantiations; // Set id

    // Loop
    public ArrayList<String> Loop; // Read all pins

    protected String libraries = "#include <ebal.h> \n#include <ebalEvent.h> \n#include <ebalPin.h>\n\n";
    int indentation = 0;

    public ArduinoBoard() {
        PinDeclarations = new ArrayList<>();
        EventDeclarations = new ArrayList<>();

        PinInstantiations = new ArrayList<>();
        EventInstantiations = new ArrayList<>();

        Loop = new ArrayList<>();
    }


    // Listener, EventHandler
    public abstract void AddBlock(Node node, ArduinoSystem arduinoSystem);

    public void AddPinDeclaration(Node node, ArduinoSystem arduinoSystem) {
        String pinName = node.FirstChild.Value;

        this.PinDeclarations.add("ebalPin " + pinName + ";\n");

        Node call = node.FirstChild.Next;
        Node pinType = call.FirstChild.Next;
        Node ioType = pinType.Next;
        Node pinNumber = ioType.Next;

        this.PinInstantiations.add(pinName + ".createPin("
                + pinType.GenerateCode(arduinoSystem) + ", "
                + ioType.GenerateCode(arduinoSystem) + ", "
                + pinNumber.GenerateCode(arduinoSystem) + ");\n");

        this.Loop.add(pinName + ".readPin();\n");
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
