package sw417f20.ebal.CodeGeneration.Utility;

import sw417f20.ebal.SyntaxAnalysis.Node;
import sw417f20.ebal.CodeGeneration.NodeList;

import java.util.Dictionary;
import java.util.Hashtable;

public class SlaveDictionary {
    private final Dictionary<String, Slave> slaveDictionary;

    public SlaveDictionary(Node node) {
        slaveDictionary = new Hashtable<>();
        MakeSlaveDictionary(node);
    }

    /**
     * Method that makes a slave dictionary from the slaves name and ID
     * @param node The root node of the AST whose events and slaves should be linked.
     */
    public void MakeSlaveDictionary(Node node){
        NodeList nodeList = new NodeList();
        int slaveID = 0;

        //Visit slaves.
        Node currentSlaveNode = node.FirstChild.Next;
        while (!currentSlaveNode.IsEmpty()) {
            nodeList.Visit(currentSlaveNode);
            currentSlaveNode = currentSlaveNode.Next;
        }

        for (Node slaveNode : nodeList.nodeList) {
            String slaveName = slaveNode.FirstChild.Value;
            Slave slave = new Slave(slaveName, slaveID++);
            AddSlave(slave);
        }
    }

    public void AddSlave(Slave slave) {
        slaveDictionary.put(slave.GetName(), slave);
    }

    public int GetSlaveID(String slaveName) {
        return slaveDictionary.get(slaveName).getID();
    }
}
