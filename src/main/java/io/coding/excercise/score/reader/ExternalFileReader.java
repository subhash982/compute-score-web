/**
 * 
 */
package io.coding.excercise.score.reader;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.coding.excercise.score.calculator.FirstNameScoreCalculator;
import io.coding.excercise.score.calculator.FullNameScoreCalculator;
import io.coding.excercise.score.constants.ScoreConstants;
import io.coding.excercise.score.exception.ComputeScoreException;
import io.coding.excercise.score.helper.FileProcessingHelper;

/**
 * @author subha
 *
 */
@Component
public class ExternalFileReader {

	@Autowired
	private FileProcessingHelper fileProcessingHelper;

	/**
	 * Read the the large file into chunk and merge all the chunks into one after
	 * sorting.
	 * 
	 * @param filePath
	 * @param targetPath
	 * @param scoreAlgorithm
	 * @return
	 */
	public Stream<String> read(String filePath, String targetPath, String scoreAlgorithm) {
		try {
			// Create Temporary directory if not exist to store the intermediate computation
			fileProcessingHelper.createDirectoryPath(ScoreConstants.TEMP_PATH);

			// Split the large file into chunks and sort the individual chunk
			int chunks = fileProcessingHelper.splitAndSortFileIntoChunks(filePath, targetPath,
					withSorting(scoreAlgorithm));

			// Merge All the sorted chucks
			String targetFileName = targetPath + ScoreConstants.MERGED_FILE_NAME;
			fileProcessingHelper.mergeAllTheFileChunks(targetFileName, targetPath, chunks, withSorting(scoreAlgorithm));

			// Return the stream of the name on the large file
			return Files.lines(Paths.get(targetFileName), StandardCharsets.UTF_8);

		} catch (Exception ex) {
			throw new ComputeScoreException(ex);
		}
	}

	/**
	 * Return comparator for sorting based on the algorithm type
	 * 
	 * @param scoringAlgorithm
	 * @return
	 */
	private Comparator<String> withSorting(String scoreAlgorithm) {
		if (ScoreConstants.SCORE_BY_FIRST_NAME.endsWith(scoreAlgorithm)) {
			return FirstNameScoreCalculator.withSorting();
		} else if (ScoreConstants.SCORE_BY_FULL_NAME.endsWith(scoreAlgorithm)) {
			return FullNameScoreCalculator.withSorting();
		} else {
			throw new ComputeScoreException(
					"Scoring algorithm is not supported. Possible value can be from [scoreByFirstName, scoreByFullName] ");
		}
	}

}
