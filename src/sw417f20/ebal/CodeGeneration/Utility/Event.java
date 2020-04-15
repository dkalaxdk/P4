package sw417f20.ebal.CodeGeneration.Utility;

import java.util.ArrayList;

/**
 * Class that represents an event during code generation.
 */
public class Event {
    private String Name;
    private String ID;
    private String Value;
    private ArrayList<Slave> UsingSlaves;

    public Event(String name, String id, String value) {
        this.Name = name;
        this.ID = id;
        this.Value = value;
        UsingSlaves = new ArrayList<>();
    }

    public String GetName() {
        return Name;
    }

    public void SetName(String name) {
        this.Name = name;
    }

    public String GetID() {
        return ID;
    }

    public void SetID(String ID) {
        this.ID = ID;
    }

    public String GetValue() {
        return Value;
    }

    public void SetValue(String value) {
        this.Value = value;
    }

    public ArrayList<Slave> GetUsingSlaves() {
        return UsingSlaves;
    }

    public void AddSlave(Slave slave) {
        UsingSlaves.add(slave);
    }
}
