/**
 * 
 */
package io.coding.excercise.score.reader;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;

import io.coding.excercise.ComputeScoreWebApplication;
import io.coding.excercise.score.constants.ScoreConstants;

/**
 * @author subha
 *
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = { ComputeScoreWebApplication.class })
@ActiveProfiles({"test"})
public class InMemoryFileReaderTest {
	
	@Autowired
	private InMemoryFileReader inMemoryFileReader;
	private String filepath = "testdata";
	
	
	@BeforeEach
	public void setup() {
	
	}

	@Test
	@DisplayName("Verify in-memory file reader")
	public void testInMemoryFileReader() {
		String filePath = filepath + File.separator + "SmallDataSetFile.txt";
		String expected = "MARY,PATRICIA,LINDA,BARBARA,VINCENZO,SHON,LYNWOOD,JERE,HAI";
		Stream<String> dataStream = inMemoryFileReader.read(filePath, ScoreConstants.SCORE_BY_FIRST_NAME);
		String actual=dataStream.collect(Collectors.joining(","));
		
		assertEquals(expected, actual);
	}
}
