package pl.atd.eventlog;

import org.junit.Test;

import static org.junit.Assert.*;

public class EventTest {

    @Test
    public void shouldSetTimeForStartLog(){
        Event event = new Event();
        long timestamp = System.currentTimeMillis();
        event.setTime(true, timestamp);

        assertEquals("Wrong start timestamp", timestamp, event.getStart().longValue());
        assertNull("Finish timestamp is not null", event.getFinish());
        assertNull("Duration is not null", event.getDuration());
        assertNull("Alert is not null", event.getAlert());
    }
    @Test
    public void shouldSetTimeForFinishLog(){
        Event event = new Event();
        long timestamp = System.currentTimeMillis();
        event.setTime(false, timestamp);

        assertEquals("Wrong finish timestamp", timestamp, event.getFinish().longValue());
        assertNull("Finish timestamp is not null", event.getStart());
        assertNull("Duration is not null", event.getDuration());
        assertNull("Alert is not null", event.getAlert());
    }
    @Test
    public void shouldCountDurationForNormalOrder() {
        Event event = new Event();
        long startTimestamp = System.currentTimeMillis();
        event.setTime(true, startTimestamp);

        long finishTimestamp = startTimestamp+3;
        event.setTime(false, finishTimestamp);

        assertEquals("Wrong start timestamp", startTimestamp, event.getStart().longValue());
        assertEquals("Wrong finish time", finishTimestamp, event.getFinish().longValue());
        assertEquals("Wrong duration", 3L, event.getDuration().longValue());
        assertFalse("Alert is true", event.getAlert());
    }
    @Test
    public void shouldCountDurationForReverseOrder() {
        Event event = new Event();
        long finishTimestamp = System.currentTimeMillis();
        event.setTime(false, finishTimestamp);

        long startTimestamp = finishTimestamp-2;
        event.setTime(true, startTimestamp);

        assertEquals("Wrong start timestamp", startTimestamp, event.getStart().longValue());
        assertEquals("Wrong finish time", finishTimestamp, event.getFinish().longValue());
        assertEquals("Wrong duration", 2L, event.getDuration().longValue());
        assertFalse("Alert is true", event.getAlert());
    }
    @Test
    public void shouldRaiseAlert() {
        Event event = new Event();
        long startTimestamp = System.currentTimeMillis();
        event.setTime(true, startTimestamp);

        long finishTimestamp = startTimestamp+5;
        event.setTime(false, finishTimestamp);

        assertEquals("Wrong start timestamp", startTimestamp, event.getStart().longValue());
        assertEquals("Wrong finish time", finishTimestamp, event.getFinish().longValue());
        assertEquals("Wrong duration", 5L, event.getDuration().longValue());
        assertTrue("Alert is false", event.getAlert());
    }
}
