package pl.atd.eventlog;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * Component to read a file with event logs data
 */

@Component
public class LogFileReader {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);
    // ObjectMapper is thread safe
    private ObjectMapper mapper = new ObjectMapper();
    private EventService eventService;

    public LogFileReader(EventService eventService) {
        this.eventService = eventService;
    }

    /**
     * read the file for a path
     * @param logFilePath the path of event log file, every line is a event log JSON
     */
    public void readFile(Path logFilePath){
        //System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "20");
        try (Stream<String> stream = Files.lines(logFilePath).parallel() ){
            stream.forEach(this::saveEventData);
        } catch (IOException e) {
            logger.error("Creating stream from file", e);
        }
        logger.info("Import finished. Total {} events, with alert {}", eventService.getEventsCount(), eventService.getAlertsCount());
    }

    /**
     * save the event log data in repository
     * @param line a line with event log JSON
     */
    protected void saveEventData(String line){
        try {
            EventLog eventLog = mapper.readValue(line, EventLog.class);
            logger.debug(eventLog.toString());
            eventService.saveEventLog(eventLog);
        } catch (IOException e) {
            logger.error("Cannot convert event log: {}", line, e);
        }
    }

}
