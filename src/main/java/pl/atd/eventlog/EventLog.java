package pl.atd.eventlog;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Wither;

/**
 * Object for JSON data
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventLog {

    public enum EventLogState {
        STARTED,
        FINISHED
    }

    private String id;

    @Wither
    private EventLogState state;

    @Wither
    private Long timestamp;

    private String type;

    private String host;

    public EventLog(String id, EventLogState state, Long timestamp) {
        this.id = id;
        this.state = state;
        this.timestamp = timestamp;
    }

    public EventLog(EventLog eventLog) {
        this.id = eventLog.getId();
        this.state = eventLog.getState();
        this.timestamp = eventLog.getTimestamp();
        this.type = eventLog.getType();
        this.host = eventLog.getHost();
    }
}
