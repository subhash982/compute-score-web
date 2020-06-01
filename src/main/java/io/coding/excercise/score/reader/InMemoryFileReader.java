/**
 * 
 */
package io.coding.excercise.score.reader;

import java.util.Arrays;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.coding.excercise.score.helper.FileProcessingHelper;

/**
 * @author subha
 *
 */
@Component
public class InMemoryFileReader {

	@Autowired
	private FileProcessingHelper filePRocessingHelper;

	/**
	 * Read the file in-memory and return the stream of names.
	 * 
	 * @param filePath
	 * @param scoreAlgorithm
	 * @return
	 */
	public Stream<String> read(String filePath, String scoreAlgorithm) {
		String fileContent = filePRocessingHelper.readFileContent(filePath);
		if (fileContent.trim().length() > 0) {
			return Arrays.stream(fileContent.split(","));
		}
		return Stream.empty();
	}
}
