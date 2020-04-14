package sw417f20.ebal.CodeGeneration.Utility;

import java.util.Dictionary;

public class EventDictionary {
    private Dictionary<String, String[]> dictionary; //TODO lav value om til en klasse
                                                    //TODO Find ud af hvordan man beholder events der hedder det samme

    public EventDictionary() { }

    public void AddEvent(String eventName, String eventID, String eventValue) {
        String[] idAndValue = { eventID.substring(0, 1), eventValue };

        dictionary.put(eventName, idAndValue);
    }

    public void EditEventValue(String eventName, String eventValue) {
        String id = dictionary.get(eventName)[0];
        dictionary.remove(eventName);
        AddEvent(eventName, id, eventValue);
    }

    public String getEventID(String eventName) {
        return dictionary.get(eventName)[0];
    }

    public String getEventValue(String eventName) {
        return dictionary.get(eventName)[1];
    }

    public Dictionary<String, String[]> getDictionary() {
        return dictionary;
    }
}
