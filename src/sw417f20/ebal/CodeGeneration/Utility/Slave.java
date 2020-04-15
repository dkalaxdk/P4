package sw417f20.ebal.CodeGeneration.Utility;

/**
 * Class that represents a slave during code generation.
 */
public class Slave {
    private String Name;
    private String ID;

    public Slave(String name, String id) {
        this.Name = name;
        this.ID = id;
    }

    public String GetName() {
        return Name;
    }

    public void SetName(String name) {
        this.Name = name;
    }

    public String getID() {
        return ID;
    }

    public void setID(String id) {
        this.ID = id;
    }
}
