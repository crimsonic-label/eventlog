package pl.atd.eventlog;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.nio.file.Paths;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class LogFileReaderTest {

    @Mock
    private EventService eventService;

    @InjectMocks
    private LogFileReader logFileReader;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void shouldSaveEventForJson() throws Exception {
        EventLog eventLog = new EventLog("scsmbstgra", EventLog.EventLogState.STARTED, System.currentTimeMillis());
        logFileReader.saveEventData(objectMapper.writeValueAsString(eventLog));
        verify(eventService).saveEventLog(eventLog);
    }
    @Test
    public void shouldNotSaveForWrongJson() {
        logFileReader.saveEventData("{wrongJson}");
        verify(eventService, never()).saveEventLog(any());
    }
    @Test
    public void shouldReadFile(){
        String resourcePath = getClass().getClassLoader().getResource("events.log").getPath();
        logFileReader.readFile(Paths.get(resourcePath));
        verify(eventService, times(6)).saveEventLog(any());
    }
}
