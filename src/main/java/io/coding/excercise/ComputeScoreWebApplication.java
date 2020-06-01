package io.coding.excercise;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author subhash
 *
 */
@SpringBootApplication
public class ComputeScoreWebApplication implements CommandLineRunner {

	private static Logger logger = LoggerFactory.getLogger(ComputeScoreWebApplication.class);

	@Value("${file.upload-dir}")
	private String fileStoragePath;

	public static void main(String[] args) {
		SpringApplication.run(ComputeScoreWebApplication.class, args);
	}

	/**
	 * Create the file directory path to store the files intermediate procession if
	 * it doesn't exist.
	 * 
	 * @param args
	 */
	@Override
	public void run(String... args) throws Exception {
		try {
			Files.createDirectories(Paths.get(this.fileStoragePath));
		} catch (Exception ex) {
			logger.info("File upload directory path already exist");
		}

	}

}
