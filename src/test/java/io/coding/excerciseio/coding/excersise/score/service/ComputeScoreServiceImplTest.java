/**
 * 
 */
package io.coding.excerciseio.coding.excersise.score.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;

import io.coding.excercise.ComputeScoreWebApplication;
import io.coding.excercise.score.constants.ScoreConstants;
import io.coding.excercise.score.exception.ComputeScoreException;
import io.coding.excercise.score.service.ComputeScoreService;

/**
 * @author subha
 *
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT,classes = {ComputeScoreWebApplication.class})
@ActiveProfiles({"test"})
public class ComputeScoreServiceImplTest {
	
	@Autowired
	private ComputeScoreService computeScoreService;
	
	private String filepath = "testdata";

	@BeforeEach
	public void setup() {
		
	}

	@Test
	@DisplayName("Compute score with large data set (> 50KB)")
	public void testComputeOnLargeDataSetSuccess() {
		String filePath = filepath + File.separator + "LargeDataSetFile.txt";
		long computeScore = computeScoreService.compute(filePath, ScoreConstants.SCORE_BY_FIRST_NAME);
		assertEquals(1073712919, computeScore);
	}

	@Test
	@DisplayName("Compute score with mid size data set (~40KB)")
	public void testComputeOnMidSizeDataSetSuccess() {
		String filePath = filepath + File.separator + "SmallDataSetFile.txt";
		long computeScore = computeScoreService.compute(filePath, ScoreConstants.SCORE_BY_FIRST_NAME);
		assertEquals(3194, computeScore);
	}

	@Test
	@DisplayName("Compute score with small size data set (~3-4KB)")
	public void testComputeOnSmallDataSetSuccess() {
		String filePath = filepath + File.separator + "MediumDataSetFile.txt";
		long computeScore = computeScoreService.compute(filePath, ScoreConstants.SCORE_BY_FIRST_NAME);
		assertEquals(871198282, computeScore);
	}

	@Test
	@DisplayName("Compute score with empty data (0KB)")
	public void testComputeOnBlankDataSetSuccess() {
		String filePath = filepath + File.separator + "BlankDataSetFile.txt";
		long computeScore = computeScoreService.compute(filePath, ScoreConstants.SCORE_BY_FIRST_NAME);
		assertEquals(0, computeScore);
	}

	@Test
	@DisplayName("Verify the compute exception on invalid file.")
	public void testComputeOnInvalidInputFailure() {
		String filePath = filepath + File.separator + "Invalid.txt";
		assertThrows(ComputeScoreException.class, () -> {
			computeScoreService.compute(filePath, ScoreConstants.SCORE_BY_FIRST_NAME);
		});
	}
}
