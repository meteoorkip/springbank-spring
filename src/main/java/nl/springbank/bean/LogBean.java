package nl.springbank.bean;

import javax.persistence.*;
import java.sql.Timestamp;

import static javax.persistence.GenerationType.AUTO;

/**
 * Bean representing a log.
 *
 * @author Sven Konings
 */
@Entity
@Table(name = "log")
public class LogBean {
    /*
     * Table values
     */
    /** The log identifier. */
    @Id
    @Column(name = "log_id")
    @GeneratedValue(strategy = AUTO)
    private Long logId;

    /** The timestamp of the log. */
    @Column(name = "timestamp")
    private Timestamp timestamp;

    /** The event of the log. */
    @Column(name = "event", columnDefinition = "TEXT")
    private String event;

    /*
     * Bean methods
     */
    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
