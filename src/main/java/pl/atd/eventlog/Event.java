package pl.atd.eventlog;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Database entity
 */

@Entity
@Data
@NoArgsConstructor
public class Event {
    @Id
    private String id;

    private Long start;

    private Long finish;

    private String type;

    private String host;

    private Long duration;

    private Boolean alert;

    public Event(String id, String type, String host) {
        this.id = id;
        this.type = type;
        this.host = host;
    }

    /**
     * save or update time information
     * when the second time is beeing set, then count duration and alert
     * @param isStart true if event log was a start log, false if it is a finish log
     * @param timestamp the timestamp
     */
    public void setTime(boolean isStart, long timestamp) {
        if(isStart){
            start = timestamp;
        } else {
            finish = timestamp;
        }
        if(start!=null && finish!=null) {
            duration = finish - start;
            alert = duration > 4;
        }
    }
}
