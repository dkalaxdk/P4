package sw417f20.ebal.CodeGeneration.Utility;

/**
 * Class that represents a slave during code generation.
 */
public class Slave {
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
