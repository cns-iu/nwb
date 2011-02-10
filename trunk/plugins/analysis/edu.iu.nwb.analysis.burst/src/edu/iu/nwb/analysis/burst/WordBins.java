package edu.iu.nwb.analysis.burst;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A container of WordBin. WordBins contains all the data of the documents.
 * It defines the bin for all the WordBins under it. It also contains total
 * documents counted by bin.
 * 
 * Please note that the DocumentRetriever is using the old version Java 
 * implementation due to time limit. It is not neccesary to refaktor for now.
 * 
 * @author kongch
 *
 */
public class WordBins {
	private Map<String, WordBin> wordToBin;
	private int startDate;
	private int endDate;
	private int binSize;
	private int[] binDocumentCounts;
	
	public WordBins(int startDate, int endDate) {
		this.startDate = startDate;
		this.endDate = endDate;
		this.binSize = this.endDate - this.startDate + 1;
		this.binDocumentCounts = new int[this.binSize];
		this.wordToBin = new HashMap<String, WordBin>();
	}
	
	public void setWordBinValue(String word, int binDate, int value) {
		
		if (!this.wordToBin.containsKey(word)) {
			this.wordToBin.put(word, new WordBin(word, this.binSize));
		}
		WordBin wordBin = this.wordToBin.get(word);
		wordBin.set(getBinIndex(binDate), value);
	}
	
	public void addADocument(Collection<String> words, int binDate) {
		for (String word : words) {
			this.addWordToBin(word, binDate);
		}
		this.increaseDocumentCount(binDate);
	}
		
	private void addWordToBin(String word, int binDate) {
		if (!this.wordToBin.containsKey(word)) {
			this.wordToBin.put(word, new WordBin(word, this.binSize));
		}
		WordBin wordBin = this.wordToBin.get(word);
		int binNumber = getBinIndex(binDate);
		wordBin.set(binNumber, wordBin.get(binNumber) + 1);
	}
	
	public Set<String> getWordSet() {
		return this.wordToBin.keySet();
	}
	
	public WordBin getWordBin(String word) {
		return this.wordToBin.get(word);
	}
	
	public int getBinDate(int binIndex) {
		return this.startDate + binIndex;
	}
	
	public int getBinIndex(int binDate) {
		return binDate - this.startDate;
	}
	
	private void increaseDocumentCount(int binDate) {
		this.binDocumentCounts[getBinIndex(binDate)]++;
	}
	
	public int[] getBinDocumentCount() {
		return this.binDocumentCounts;
	}
	
	public int getBinSize() {
		return this.binSize;
	}
}
