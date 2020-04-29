package sw417f20.ebal.CodeGeneration.Utility;

import sw417f20.ebal.SyntaxAnalysis.Node;
import sw417f20.ebal.CodeGeneration.NodeList;

import java.util.Dictionary;
import java.util.Hashtable;

/**
 * Class that implements a dictionary of slaves which can be accessed by their names.
 */
public class SlaveDictionary {
    private final Dictionary<String, Slave> slaveDictionary;

    /**
     * Constructor of a SlaveDictionary.
     * @param node      The root node of the AST to be read for events.
     */
    public SlaveDictionary(Node node) {
        slaveDictionary = new Hashtable<>();
        MakeSlaveDictionary(node);
    }

    /**
     * Method that makes a slave dictionary from the slaves name and ID
     * @param node      The root node of the AST from where the slaves should be taken.
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

        for (Node slaveNode : nodeList.GetList()) {
            String slaveName = slaveNode.FirstChild.Value;
            Slave slave = new Slave(slaveName, slaveID++);
            AddSlave(slave);
        }
    }

    /**
     * Method for adding a slave to the SlaveDictionary.
     * @param slave     The slave to be added to the dictionary.
     */
    public void AddSlave(Slave slave) {
        slaveDictionary.put(slave.GetName(), slave);
    }

    /**
     * Getter for a slaves ID.
     * @param slaveName The name of the slave to return the ID for.
     * @return          Returns the ID of a slave.
     */
    public int GetSlaveID(String slaveName) {
        return slaveDictionary.get(slaveName).getID();
    }
}
