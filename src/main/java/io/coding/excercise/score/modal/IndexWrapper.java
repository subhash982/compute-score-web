/**
 * 
 */
package io.coding.excercise.score.modal;

/**
 * @author subha
 *
 */
public class IndexWrapper {
	private String name;
	private long index;

	public IndexWrapper(String name, long index) {
		this.name = name;
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public long getIndex() {
		return index;
	}
	
}
