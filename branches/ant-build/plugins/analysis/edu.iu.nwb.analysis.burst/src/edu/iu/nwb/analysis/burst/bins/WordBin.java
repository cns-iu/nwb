package edu.iu.nwb.analysis.burst.bins;

/**
 * A WordBin represents a word that contains a set of bins where
 * each bin is the number of related documents that contains the 
 * word in a specified time slot.
 * @author kongch
 */
public class WordBin {
	private String word;
	private int[] bins;
	
	public WordBin(String word, int size) {
		this.word = word;
		this.bins = new int[size];
	}
	
	public String getWord() {
		return this.word;
	}
	
	public int get(int binNumber) {
		return this.bins[binNumber];
	}
	
	public void set(int binNumber, int value) {
		this.bins[binNumber] = value;
	}
	
	public int[] getBin() {
		return bins;
	}
}
