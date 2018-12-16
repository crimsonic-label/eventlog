package pl.atd.eventlog;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface EventRepository extends CrudRepository<Event, String> {
    @Query("SELECT COUNT(e) FROM Event e")
    Long countEvents();

    @Query("SELECT COUNT(e) From Event e WHERE e.alert='true'")
    Long eventsWithAlert();
}
