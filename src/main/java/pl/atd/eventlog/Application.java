package pl.atd.eventlog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
public class Application {

	private static final Logger logger = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	@Profile("!withoutCommandLineRunner")
	public CommandLineRunner commandLineRunner(LogFileReader reader){
		return (args) -> {
			Path inputFilePath;
		    if(args.length==0) {
                URL resource = getClass().getClassLoader().getResource("events.log");
                // run from within compiled jar - the file argument is required
                if(resource.toString().contains(".jar")) {
		            logger.error("Argument for event log file path is required");
		            return;
                }
                inputFilePath = Paths.get(resource.getPath());
            } else {
				inputFilePath = Paths.get(args[0]);
			}
            logger.info("Importing events form log file {} ...", inputFilePath);
            reader.readFile(inputFilePath);
        };
	}
}

