package sw417f20.ebal.CodeGeneration.Utility;

public class Event {
    private String name;
    private String id;
    private String value;
    private Slave[] slaves;

    public Event(String name, String id, String value) {
        this.name = name;
        this.id = id;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Slave[] getSlaves() {
        return slaves;
    }

    public void setSlaves(Slave[] slaves) {
        this.slaves = slaves;
    }
}
