package sw417f20.ebal.CodeGeneration.Utility;

/**
 * Class that represents a slave during code generation.
 */
public class Slave {
    private String Name;
    private int ID;

    public Slave(String name, int id) {
        this.Name = name;
        this.ID = id;
    }

    public String GetName() {
        return Name;
    }

    public void SetName(String name) {
        this.Name = name;
    }

    public int getID() {
        return ID;
    }

    public void setID(int id) {
        this.ID = id;
    }
}
