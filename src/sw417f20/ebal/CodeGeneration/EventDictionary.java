package sw417f20.ebal.CodeGeneration;

import java.util.Dictionary;

public class EventDictionary {
    private Dictionary<String, String> dictionary;

    public EventDictionary() { }

    public void AddEvent(String eventName, String eventValue) {
        dictionary.put(eventName, eventValue);
    }

    public String getEventValue(String eventName) {
        return dictionary.get(eventName);
    }

    public Dictionary<String, String> getDictionary() {
        return dictionary;
    }
}
