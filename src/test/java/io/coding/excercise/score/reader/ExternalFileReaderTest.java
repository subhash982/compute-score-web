/**
 * 
 */
package io.coding.excercise.score.reader;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.time.Instant;
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
import io.coding.excercise.score.helper.FileProcessingHelper;

/**
 * @author subha
 *
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = { ComputeScoreWebApplication.class })
@ActiveProfiles({"test"})
public class ExternalFileReaderTest {

	@Autowired
	private ExternalFileReader externalFileReader;
	
	@Autowired
	private FileProcessingHelper fileProcessingHelper;

	private String filepath = "testdata";


	@BeforeEach
	public void setup() {
		fileProcessingHelper.createDirectoryPath(ScoreConstants.TEMP_PATH);
	}

	@Test
	@DisplayName("Verify file reader using external memory")
	public void testExternalFileReader() {
		String targetPath = ScoreConstants.TEMP_PATH + File.separator + String.valueOf(Instant.now().getEpochSecond());
		String filePath = filepath + File.separator + "SmallDataSetFile.txt";
		String expected = "BARBARA,HAI,JERE,LINDA,LYNWOOD,MARY,PATRICIA,SHON,VINCENZO";
		Stream<String> dataStream = externalFileReader.read(filePath, targetPath,ScoreConstants.SCORE_BY_FIRST_NAME);
		String actual = dataStream.collect(Collectors.joining(","));
		assertEquals(expected, actual);
	}

}
