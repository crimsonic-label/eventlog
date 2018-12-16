package pl.atd.eventlog;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Event repository operations
 */

@Component
@Transactional
public class EventService {

    private EventRepository repository;

    public EventService(EventRepository repository) {
        this.repository = repository;
    }

    /**
     * save event log data in event repository
     * if the other event log was already saved - the second log updates the firs
     * start and finish logs can come in any order
     * @param eventLog the data for event (start or end)
     */
    public void saveEventLog(EventLog eventLog) {
        // check if the other state log was saved (with the same id)
        Optional<Event> eventOptional = repository.findById(eventLog.getId());
        Event event = eventOptional.orElseGet(() -> new Event(eventLog.getId(), eventLog.getType(), eventLog.getHost()));
        event.setTime(eventLog.getState().equals(EventLog.EventLogState.STARTED), eventLog.getTimestamp());
        repository.save(event);
    }

    /**
     * get total events
     * @return the total count
     */
    public long getEventsCount() {
        return repository.countEvents();
    }

    /**
     * get total alerts
     * @return the count of events with alert
     */
    public long getAlertsCount() {
        return repository.eventsWithAlert();
    }

    /**
     * find event by id (used in tests)
     * @param id the event identifier
     * @return optional (event is present when exists in database)
     */
    public Optional<Event> findById(String id) {
        return repository.findById(id);
    }
}
