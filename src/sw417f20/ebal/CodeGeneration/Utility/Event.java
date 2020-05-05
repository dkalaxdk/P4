package sw417f20.ebal.CodeGeneration.Utility;

import java.util.ArrayList;

/**
 * Class that represents an event during code generation.
 */
public class Event {
    private String Name;
    private int ID;
    private String Type;
//    private ArrayList<Slave> AssociatedSlaves;

    public ArrayList<Integer> AssociatedSlaves;

    /**
     * Constructor of an event.
     * @param name  The name the event should have.
     * @param id    The ID the event should have.
     */
    public Event(String name, int id) {
        this.Name = name;
        this.ID = id;
        AssociatedSlaves = new ArrayList<>();
    }

    /**
     * Getter for the events name.
     * @return      Returns the name of the event.
     */
    public String GetName() {
        return Name;
    }

    /**
     * Setter for the events name.
     * @param name  The name the events name should be set to.
     */
    public void SetName(String name) {
        this.Name = name;
    }

    /**
     * Getter for the events ID.
     * @return      Returns the ID of the event.
     */
    public int GetID() {
        return ID;
    }

    /**
     * Setter for the events ID
     * @param ID    The ID the events ID should be set to.
     */
    public void SetID(int ID) {
        this.ID = ID;
    }

    /**
     * Getter for the events value.
     * @return      Returns the value of the event.
     */
    public String GetType() {
        return Type;
    }

    /**
     * Setter for the events value.
     * @param type The value the events value should be set to.
     */
    public void SetType(String type) {
        this.Type = type;
    }

    /**
     * Getter for the events associated slaves.
     * <p>
     *     The associated slaves are set up using AddSlave in EventDictionary.java.
     * </p>
     * @return      Returns the events list of associated slaves.
     */
//    public ArrayList<Slave> GetAssociatedSlaves() {
//        return AssociatedSlaves;
//    }

    /**
     * Method for adding a slaves to the events associatedSlaves list.
     * @param slave The slaves to be added to the list.
     */
//    public void AddSlave(Slave slave) {
//        AssociatedSlaves.add(slave);
//    }
}
