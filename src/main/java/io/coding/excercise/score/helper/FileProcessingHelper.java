/**
 * 
 */
package io.coding.excercise.score.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import io.coding.excercise.score.constants.ScoreConstants;
import io.coding.excercise.score.exception.ComputeScoreException;

/**
 * @author subha
 *
 */
@Component
public class FileProcessingHelper {
	private static final Logger logger = LoggerFactory.getLogger(FileProcessingHelper.class);

	@Value("${file.chunk.size}")
	private int fileChunkSize;
	
	@Value("${file.upload-dir}")
	private String fileStoragePath;
	/**
	 * Split the large files into the chunks and sort each chunk individually.
	 * 
	 * @param filePath
	 * @param splitedFilePrefix
	 * @return number of file chunks
	 */
	public int splitAndSortFileIntoChunks(String filePath, String splitedFilePrefix, Comparator<String> comparator) {
		int chunkCount = 0;
		try (FileInputStream fis = new FileInputStream(new File(filePath))) {
			byte[] buffer = new byte[fileChunkSize];
			String reminder = "";
			String readString = "";
			String namesToSort = "";
			while (fis.read(buffer) > 0) {
				/*
				 * Read the names from file and remove double quotes,line chars, tab chars or
				 * carriage returns
				 */
				readString = reminder + new String(buffer);
				readString = readString.trim().replaceAll(ScoreConstants.SPECIAL_CHAR_EXP, "");

				// find the last comma index and calculate complete name list
				int lastCommaIndex = readString.lastIndexOf(",");
				if (lastCommaIndex != -1) {
					namesToSort = readString.substring(0, lastCommaIndex);
					if (readString.length() == lastCommaIndex + 1) {
						reminder = "";
					} else {
						reminder = readString.substring(lastCommaIndex + 1);
					}
				} else {
					namesToSort = readString;
				}

				sortAndWriteChunkContentToFile(namesToSort, splitedFilePrefix, chunkCount, comparator);
				chunkCount++;
				// Reset the buffer
				buffer = new byte[fileChunkSize];
			}

			// If reminder part is still there then adjust that to last slice
			if (reminder != null && reminder.trim().length() > 0) {
				sortAndWriteChunkContentToFile(namesToSort + "," + reminder, splitedFilePrefix, chunkCount - 1,
						comparator);
			}

		} catch (Exception ex) {
			throw new ComputeScoreException("Error in spliting and sorting the large file into chunks.", ex);
		}
		return chunkCount;
	}

	/**
	 * Sort the each chunk content and store them into temporary files for further
	 * processing.
	 * 
	 * @param unsortedNames
	 * @param splitedFilePrefix
	 * @param chunk
	 */
	public void sortAndWriteChunkContentToFile(String unsortedNames, String splitedFilePrefix, int chunk,
			Comparator<String> comparator) {
		FileWriter fw = null;
		PrintWriter pw = null;
		try {
			String sortedNames = Arrays.stream(unsortedNames.trim().split(",")).sorted(comparator)
					.collect(Collectors.joining("\n"));
			fw = new FileWriter(splitedFilePrefix + "_" + chunk + ".txt");
			pw = new PrintWriter(fw);
			pw.write(sortedNames);
			pw.close();
			fw.close();
		} catch (IOException ex) {
			throw new ComputeScoreException("Error in sorting and storing the chuks [" + chunk + "]", ex);
		}

	}

	/**
	 * Merge and sort all the chunk into a single file for the score computation.
	 * 
	 * @param targetFileName
	 * @param splitedFilePrefix
	 * @param chunks
	 * @param comparator
	 */
	public void mergeAllTheFileChunks(String targetFileName, String splitedFilePrefix, int chunks,
			Comparator<String> comparator) {
		try {
			// Initialize variable to hold the temporary computation
			String[] topElement = new String[chunks];
			BufferedReader[] brs = new BufferedReader[chunks];

			/*
			 * Initialize the top element of each chunk and create buffer reader for each
			 * chunk to read the content from each chunk
			 */
			for (int i = 0; i < chunks; i++) {
				brs[i] = new BufferedReader(new FileReader(splitedFilePrefix + "_" + i + ".txt"));
				String name = brs[i].readLine();
				if (name != null) {
					topElement[i] = name;
				} else {
					topElement[i] = null;
				}
			}

			// variable to track the number of chunks to be proceed
			int numOfChunksToBeProcessed = chunks;

			// Create writer to store the final merged content to merged file
			FileWriter fw = new FileWriter(targetFileName);
			PrintWriter pw = new PrintWriter(fw);

			// Perform the sorting and merging all the chunks
			while (numOfChunksToBeProcessed > 0) {
				String topMostName = topElement[0];
				int minFile = 0;
				for (int j = 1; j < chunks; j++) {
					/*
					 * If chunk is completed processed then corresponding topElement value will be
					 * set to null. so if chunk is processed then reinitialize the top most element.
					 * 
					 */
					if (topMostName == null) {
						topMostName = topElement[j];
						minFile = j;
					} else {
						// Perform comparison and find minimum element.
						if (topElement[j] != null && comparator.compare(topMostName, topElement[j]) >= 0) {
							topMostName = topElement[j];
							minFile = j;
						}
					}

				}
				// Write the sorted name to merged file
				pw.println(topMostName);
				// Advance the buffer read to next line
				String nextName = brs[minFile].readLine();
				if (nextName != null) {
					topElement[minFile] = nextName;
				} else {
					topElement[minFile] = null;
					numOfChunksToBeProcessed--;
				}
			}
			// Close all the the open resources
			for (int i = 0; i < chunks; i++) {
				brs[i].close();
			}
			pw.close();
			fw.close();
		} catch (Exception ex) {
			logger.error("Error in merging and sorting the chuks.", ex);
		}
	}

	/**
	 * Create the directory path store the intermediate result if it doesn't exists.
	 * 
	 */
	public void createDirectoryPath(String directoryPath) {
		try {
			Files.createDirectory(Paths.get(directoryPath));
		} catch (IOException e) {
			logger.debug("Temp directory is already created");
		}
	}

	/**
	 * Clean up all the temporary file created during the score computation for the
	 * large file data set.
	 * 
	 * @param fileNamePrefix
	 */
	public void deleteAllMatchingFiles(String fileNamePrefix) {
		try (Stream<Path> folderPath = Files.walk(Paths.get(ScoreConstants.TEMP_PATH))) {
			folderPath.map(Path::toFile).filter(fileName -> fileName.getAbsolutePath().indexOf(fileNamePrefix) != -1)
					.forEach(File::delete);
		} catch (IOException e) {
			logger.error("Error in cleaning the temp directory for the prefix [" + fileNamePrefix + "]");
		}
	}

	/**
	 * Read all the file content into a string from the file and remove all the
	 * double quotes.
	 * 
	 * @param filePath File Path
	 * @return File content
	 */
	public String readFileContent(String filePath) {
		StringBuilder builder = new StringBuilder();
		try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
			stream.forEach(s -> builder.append(s.trim().replaceAll(ScoreConstants.SPECIAL_CHAR_EXP, "").toUpperCase()));
		} catch (IOException ex) {
			throw new ComputeScoreException("Error in reading file content", ex);
		}

		return builder.toString();
	}
	
	public String storeFile(MultipartFile file) {
		String fileName = null;
		try {
			fileName = StringUtils.cleanPath(file.getOriginalFilename());

			// Copy file to the target location (Replacing existing file with the same name)
			Path targetLocation = Paths.get(this.fileStoragePath).resolve(fileName);
			Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
			fileName = targetLocation.toFile().getAbsolutePath();

		} catch (IOException ex) {
			throw new ComputeScoreException("Error in storing the file before starting computation.");
		}
		return fileName;
	}
	
	/**
	 * Only allow the csv and text file.
	 * 
	 * @param filename Input File name
	 * @return true if valid file name
	 */
	public boolean isValidFile(String filename) {
		String extension = Optional.ofNullable(filename).filter(f -> f.contains("."))
				.map(f -> f.substring(filename.lastIndexOf(".") + 1)).orElse("");

		return ScoreConstants.CSV_EXTN.equalsIgnoreCase(extension)
				|| ScoreConstants.TXT_EXTN.equalsIgnoreCase(extension);
	}

}
