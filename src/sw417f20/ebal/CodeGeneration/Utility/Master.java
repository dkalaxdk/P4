package sw417f20.ebal.CodeGeneration.Utility;

import java.util.ArrayList;
import java.util.Dictionary;

public class Master {
    // Global
    public ArrayList<String>  PinDeclarations;
    public ArrayList<String>  EventDeclarations;


    // Setup
    public ArrayList<String> PinInstantiations; //createPin
    public ArrayList<String> EventInstantiations; // Set id

    public ArrayList<String> AssociatedSlaves;

    private int listenerCount = 0;
    public ArrayList<String> Listeners; // Copy EBAL listeners. Name: pin + Listener + listenerCount

    public ArrayList<String> Loop; // Call listeners, read all pins





}
