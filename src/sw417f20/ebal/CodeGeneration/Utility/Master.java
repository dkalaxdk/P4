package sw417f20.ebal.CodeGeneration.Utility;

import sw417f20.ebal.SyntaxAnalysis.Node;

import java.util.ArrayList;
import java.util.Dictionary;

public class Master extends ArduinoBoard {

    public ArrayList<String> AssociatedSlaves;

    private int listenerCount = 0;
    public ArrayList<String> Listeners; // Copy EBAL listeners. Name: pin + Listener + listenerCount


    @Override
    public void AddBlock(Node node) {

    }

    @Override
    public void AddPinDeclaration(Node node) {

    }

    @Override
    public void AddEventDeclaration(Node node) {

    }
}
