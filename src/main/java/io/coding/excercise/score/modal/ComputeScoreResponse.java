/**
 * 
 */
package io.coding.excercise.score.modal;

/**
 * @author subhash
 *
 */
public class ComputeScoreResponse {
	private String fileName;
	private long score;
	private long fileSizeSize;

	public ComputeScoreResponse(String fileName, long score, long fileSizeSize) {
		super();
		this.fileName = fileName;
		this.score = score;
		this.fileSizeSize = fileSizeSize;
	}

	public String getFileName() {
		return fileName;
	}

	public long getScore() {
		return score;
	}

	public long getFileSizeSize() {
		return fileSizeSize;
	}

}
