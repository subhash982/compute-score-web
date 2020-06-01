package io.coding.excercise.score.helper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static io.coding.excercise.score.constants.ScoreConstants.*;
import java.io.File;
import java.util.Comparator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;

import io.coding.excercise.ComputeScoreWebApplication;
import io.coding.excercise.score.calculator.FirstNameScoreCalculator;
import io.coding.excercise.score.calculator.FullNameScoreCalculator;
import io.coding.excercise.score.exception.ComputeScoreException;
import io.coding.excercise.score.helper.FileProcessingHelper;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = { ComputeScoreWebApplication.class })
@ActiveProfiles({"test"})
public class FileProcessingHelperTest {
	@Autowired
	private FileProcessingHelper fileProcessingHelper;

	@Value("${file.upload-dir}")
	private String tempFilePath;

	private String filepath = "testdata";

	@BeforeEach
	public void setup() {
		fileProcessingHelper.createDirectoryPath(TEMP_PATH);
		
	}
	
	@AfterEach
	public void cleanup() {
		fileProcessingHelper.deleteAllMatchingFiles(TEMP_PATH);
	}

	@Test
	@DisplayName("Verify the number of chunkc created from large file(> 50KB)")
	public void testSplitAndSortFileIntoChunksSuccess() {
		String splitedFilePrefix = TEMP_PATH + File.separator
				+ String.valueOf(System.currentTimeMillis());
		String filePath = filepath + File.separator + "LargeDataSetFile.txt";
		int count = fileProcessingHelper.splitAndSortFileIntoChunks(filePath, splitedFilePrefix, withSorting(SCORE_BY_FIRST_NAME));
		// Clean all the chunk file
		fileProcessingHelper.deleteAllMatchingFiles(splitedFilePrefix);
		// Each chunk size is defined to 20KB so from 50KB file chunk count will be 3
		assertEquals(count, 3);
	}

	@Test
	@DisplayName("Verify error creating chunks with null input")
	public void testSplitAndSortFileIntoChunksFailure() {
		String splitedFilePrefix = TEMP_PATH + File.separator
				+ String.valueOf(System.currentTimeMillis());
		assertThrows(ComputeScoreException.class, () -> {
			fileProcessingHelper.splitAndSortFileIntoChunks(null, splitedFilePrefix, withSorting(SCORE_BY_FIRST_NAME));
		});
	}

	@Test
	@DisplayName("Verify chunk sorting and storing into the temp file fetatue")
	public void testSortAndWriteChunkContentToFileSuccess() {
		String splitedFilePrefix = TEMP_PATH + File.separator
				+ String.valueOf(System.currentTimeMillis());
		String sortedContent = "BARBARAHAIJERELINDALYNWOODMARYPATRICIASHONVINCENZO";
		String unsortedContent = "MARY,PATRICIA,LINDA,BARBARA,VINCENZO,SHON,LYNWOOD,JERE,HAI";

		fileProcessingHelper.sortAndWriteChunkContentToFile(unsortedContent, splitedFilePrefix, 0, withSorting(SCORE_BY_FIRST_NAME));
		// Read the entire file content
		String actualSortedContent = fileProcessingHelper.readFileContent(splitedFilePrefix + "_" + 0 + ".txt");

		// Clean all the chunk file
		fileProcessingHelper.deleteAllMatchingFiles(splitedFilePrefix);

		assertEquals(sortedContent, actualSortedContent);
	}

	@Test
	@DisplayName("Verify merging chunks into a single file")
	public void testMergeAllTheFileChunksSuccess() {
		String splitedFilePrefix = filepath + File.separator + "SplitFileChunk";
		String targetFile = tempFilePath + File.separator + MERGED_FILE_NAME;
		String expectedContent = "AARONABBEYABBIEABBYABIGAILABRAHAMADAADAHADALINEADDIEKANDICEKANDYLASHAUNLASHAUNDAPIAPILARPINKIEZOEZOILAZOLA";
		fileProcessingHelper.mergeAllTheFileChunks(targetFile, splitedFilePrefix, 2, withSorting(SCORE_BY_FIRST_NAME));

		// Read the entire file content
		String actualSortedContent = fileProcessingHelper.readFileContent(targetFile);

		// Clean all the chunk file
		fileProcessingHelper.deleteAllMatchingFiles(targetFile);

		assertEquals(actualSortedContent, expectedContent);
	}
	
	/**
	 * Return comparator for sorting based on the algorithm type
	 * 
	 * @param scoringAlgorithm
	 * @return
	 */
	private Comparator<String> withSorting(String scoreAlgorithm) {
		if (SCORE_BY_FIRST_NAME.endsWith(scoreAlgorithm)) {
			return FirstNameScoreCalculator.withSorting();
		} else if (SCORE_BY_FULL_NAME.endsWith(scoreAlgorithm)) {
			return FullNameScoreCalculator.withSorting();
		} else {
			throw new ComputeScoreException(
					"Scoring algorithm is not supported. Possible value can be from [scoreByFirstName, scoreByFullName] ");
		}
	}

}
