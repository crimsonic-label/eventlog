package pl.atd.eventlog;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.LongStream;

public class FileGenerator {

	private static final Logger logger = LoggerFactory.getLogger(FileGenerator.class);
	private static final long RECORDS_NUMBER = 1000000L;
	// list buffer size to postpone logs savings - to change logs order
	private static final int EVENTS_WAITING = 100;
	private ObjectMapper mapper = new ObjectMapper();

	/**
	 * Test to generate a test file with event log data
	 * with random logs order
	 */
	@Test
	public void generateFile() {
		Path path = Paths.get("events.test.log");
		String seriesId = "s" + System.currentTimeMillis() + "-";

		// store events for further writing
        ArrayList<EventLog> waitingEvents = new ArrayList<>(EVENTS_WAITING);

        Random random = new Random();
        logger.info("Generating {} event logs...", RECORDS_NUMBER);

		try(BufferedWriter writer = Files.newBufferedWriter(path)) {
			LongStream.range(0,RECORDS_NUMBER).forEach(l->{
			    logger.debug("creating a event log for id {}", seriesId + l);

				// adding two logs (start and end) when list not full
			    if(waitingEvents.size()<EVENTS_WAITING) {
			        EventLog startEventLog;
			        if(random.nextBoolean()) {
                        startEventLog = new EventLog(seriesId + l , EventLog.EventLogState.STARTED, System.currentTimeMillis() );
                    } else {
                        startEventLog = new EventLog(seriesId + l, EventLog.EventLogState.STARTED, System.currentTimeMillis(),
								"APPLICATION_LOG",  String.valueOf(random.nextInt(10000)));
                    }

			        waitingEvents.add(startEventLog);
			        waitingEvents.add(new EventLog(startEventLog).withState(EventLog.EventLogState.FINISHED).withTimestamp(
			        		System.currentTimeMillis()+random.nextInt(8)));
                }

                // wait with saving to file until the list is full (to get a little random order)
                if(waitingEvents.size()==EVENTS_WAITING) {
					writeEventLog(waitingEvents, random, writer);
					writeEventLog(waitingEvents, random, writer);
                }
			});

			// save the remaining logs
			while(waitingEvents.size()>0) {
				writeEventLog(waitingEvents, random, writer);
			}

			logger.info("Writing done with {} records!", RECORDS_NUMBER);
		} catch (IOException e) {
			logger.error("Cannot open file to write", e);
		}
	}

	/**
	 * write an event log to file and remove it from a list
	 * @param waitingEvents a list of event logs to be written
	 * @param random random generator
	 * @param writer buffered writer to write the event log JSON
	 */
	private void writeEventLog(ArrayList<EventLog> waitingEvents, Random random, BufferedWriter writer) {
		// remove and get from the list
		EventLog toAdd = waitingEvents.remove(random.nextInt(waitingEvents.size()));
		try {
			// write as a JSON
			writer.write(mapper.writeValueAsString(toAdd) + "\n");
		} catch (IOException e) {
			logger.error("Cannot write to file", e);
		}
	}
}

