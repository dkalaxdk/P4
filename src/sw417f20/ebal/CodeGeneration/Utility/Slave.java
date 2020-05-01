package sw417f20.ebal.CodeGeneration.Utility;

import java.util.ArrayList;
import java.util.Dictionary;

/**
 * Class that represents a slave during code generation.
 */
public class Slave {
    // Global
    public ArrayList<String>  PinDeclarations;
    public ArrayList<String>  EventDeclarations;


    // Setup
    public ArrayList<String> PinInstantiations; //createPin
    public ArrayList<String> EventInstantiations; // Set id


    private int eventHandlerCount = 0;
    public ArrayList<String> EventHandlers; // Name: event + EventHandler + eventHandlerCount

    public ArrayList<String> ReceiveEvent;

    public ArrayList<String> Loop; // Read all pins


    private String Name;
    private int ID;

    /**
     * Constructor of a slave.
     * @param name  The name the slave should have.
     * @param id    The ID the slave should have.
     */
    public Slave(String name, int id) {
        this.Name = name;
        this.ID = id;
    }

    /**
     * Getter for the slaves name.
     * @return      Returns the name of a slave.
     */
    public String GetName() {
        return Name;
    }

    /**
     * Setter for the slaves name.
     * @param name  The name the slaves name should be set to.
     */
    public void SetName(String name) {
        this.Name = name;
    }

    /**
     * Getter for the slaves ID.
     * @return      Returns the ID of a slave.
     */
    public int getID() {
        return ID;
    }

    /**
     * Setter for the slaves ID.
     * @param id    The ID the slaves ID should be set to.
     */
    public void setID(int id) {
        this.ID = id;
    }
}
