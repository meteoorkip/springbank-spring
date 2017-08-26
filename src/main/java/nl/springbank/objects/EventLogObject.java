package nl.springbank.objects;

import nl.springbank.bean.LogBean;

/**
 * @author Sven Konings
 */
public class EventLogObject {
    private String timestamp;
    private String eventLog;

    public EventLogObject(LogBean log) {
        this.timestamp = log.getTimestamp().toString();
        this.eventLog = log.getEvent();
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getEventLog() {
        return eventLog;
    }

    public void setEventLog(String eventLog) {
        this.eventLog = eventLog;
    }
}
