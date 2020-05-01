package sw417f20.ebal.CodeGeneration.Utility;

import sw417f20.ebal.SyntaxAnalysis.Node;

import java.util.ArrayList;

public abstract class ArduinoBoard {
    // Global
    public ArrayList<String> PinDeclarations;
    public ArrayList<String>  EventDeclarations;


    // Setup
    public ArrayList<String> PinInstantiations; //createPin
    public ArrayList<String> EventInstantiations; // Set id

    // Loop
    public ArrayList<String> Loop; // Read all pins


    // Listener, EventHandler
    public abstract void AddBlock(Node node);

    public abstract void AddPinDeclaration(Node node);

    public abstract void AddEventDeclaration(Node node);

}
