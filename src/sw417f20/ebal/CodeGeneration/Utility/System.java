package sw417f20.ebal.CodeGeneration.Utility;

import sw417f20.ebal.SyntaxAnalysis.Node;

import java.util.ArrayList;

public class System {
    public Master master;

    public ArrayList<Slave> slaveList;


    public void AddPinDeclaration(Node node) {
        // if (node.ArduinoID == -1) { master.AddPinDeclaration } else ...
    }

    public void AddEventDeclaration(Node node) {
        master.AddEventDeclaration(node);

        // Associated slaves? slaves[node.ArduinoID]?
    }

    public void AddListener(Node node) {
        master.AddBlock(node);
    }

    public void AddEventHandler(Node node) {
        // slaves[node.ArduinoID].AddBlock(node);
    }
}
