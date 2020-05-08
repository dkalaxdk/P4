package sw417f20.ebal.CodeGeneration.Utility;

import java.util.ArrayList;

/**
 * Class that represents an event during code generation.
 */
public class Event {

    private final String Name;
    private final int ID;
    private String Type;

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
     * Getter for the events ID.
     * @return      Returns the ID of the event.
     */
    public int GetID() {
        return ID;
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
}
