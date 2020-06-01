/**
 * 
 */
package io.coding.excercise.score.service;

import java.io.File;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.coding.excercise.score.calculator.ScoreCalculatorBuilder;
import io.coding.excercise.score.constants.ScoreConstants;
import io.coding.excercise.score.exception.ComputeScoreException;
import io.coding.excercise.score.helper.FileProcessingHelper;
import io.coding.excercise.score.reader.ExternalFileReader;
import io.coding.excercise.score.reader.InMemoryFileReader;

/**
 * @author subha
 *
 */
@Component
public class ComputeScoreServiceImpl implements ComputeScoreService {

	private static final Logger logger = LoggerFactory.getLogger(ComputeScoreServiceImpl.class);
	@Autowired
	private FileProcessingHelper fileProcessingHelper;

	@Autowired
	private InMemoryFileReader inMemoryFileReader;

	@Autowired
	private ExternalFileReader externalFileReader;
	
	@Value("${max.file.size}")
	private long maxFileSize;


	/**
	 * Read all the data from the file and compute the score by delegating to the
	 * corresponding algorithm.
	 * 
	 * @param filePath
	 * @param scoringAlgorith
	 * @return Computed Score
	 */
	@Override
	public Long compute(String filePath, String scoreAlgorithm) {
		long computedScore = 0;
		if (Paths.get(filePath).toFile().exists()) {
			boolean isLargeDataSet = false;
			String targetPath = null;
			long fileSize = Paths.get(filePath).toFile().length();
			Stream<String> dataStream = null;
			if (fileSize > maxFileSize) {
				isLargeDataSet = true;
				targetPath = ScoreConstants.TEMP_PATH + File.separator + String.valueOf(Instant.now().getEpochSecond());
				logger.debug("Performing the in-memory score computation");
				dataStream = externalFileReader.read(filePath, targetPath, scoreAlgorithm);

			} else {
				logger.debug("Performing the computation using the external sorting");
				dataStream = inMemoryFileReader.read(filePath, scoreAlgorithm);
			}

			computedScore = ScoreCalculatorBuilder.buildWith(dataStream, true, scoreAlgorithm).calculate();
			//Cleanup all the temporary file created by processing large data set
			if (isLargeDataSet) {
				fileProcessingHelper.deleteAllMatchingFiles(targetPath);
			}

		} else {
			throw new ComputeScoreException("Input file is missing." + filePath);
		}

		return computedScore;

	}

}
