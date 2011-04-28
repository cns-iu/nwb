package edu.iu.nwb.analysis.burst;

/**
 * Result model for CSV output.
 * @author kongch
 */
public class Result {
	private String word;
	private String start;
	private String end;
	private double weight;
	private int length;
	private int level;
	
	public Result(
			String word, int level, double weight, int length, String start, String end) {
		this.word = word;
		this.level = level;
		this.weight = weight;
		this.length = length;
		this.start = start;
		this.end = end;
	}

	public String getWord() {
		return word;
	}

	public double getWeight() {
		return weight;
	}

	public int getLength() {
		return length;
	}

	public String getStart() {
		return start;
	}

	public String getEnd() {
		return end;
	}

	public int getLevel() {
		return level;
	}
}
