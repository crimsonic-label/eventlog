package pl.atd.eventlog;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("withoutCommandLineRunner")
public class EventServiceTest {

    @Autowired
    private EventService eventService;

    @Test
    public void shouldInsertEvent(){

        EventLog eventLog = new EventLog("test", EventLog.EventLogState.STARTED, System.currentTimeMillis());
        eventService.saveEventLog(eventLog);
        Optional<Event> eventOptional = eventService.findById("test");

        assertTrue("Event not found", eventOptional.isPresent());
        Event event = eventOptional.get();
        assertEquals("Wrong event id","test", event.getId());
    }
    @Test
    public void shouldUpdateEvent(){

        EventLog eventLog = new EventLog("test", EventLog.EventLogState.STARTED, System.currentTimeMillis());
        eventService.saveEventLog(eventLog);

        eventLog = new EventLog("test", EventLog.EventLogState.FINISHED, System.currentTimeMillis());
        eventService.saveEventLog(eventLog);

        Optional<Event> eventOptional = eventService.findById("test");

        assertTrue("Event not found", eventOptional.isPresent());
        Event event = eventOptional.get();
        assertEquals("Wrong event id","test", event.getId());

        assertNotNull("Start timestamp not set", event.getStart());
        assertNotNull("Finish timestamp not set", event.getFinish());
        assertNotNull("Duration not set", event.getDuration());
        assertNotNull("Alert should not be null", event.getAlert());

        assertEquals(1, eventService.getEventsCount());
    }
}