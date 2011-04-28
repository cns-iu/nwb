package edu.iu.nwb.analysis.burst.bins;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import edu.iu.nwb.analysis.burst.batcher.Batcher;


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
	private Batcher batcher;
	private int binSize;
	private int[] binDocumentCounts;
	
	public WordBins(Batcher batcher) {
		this.batcher = batcher;
		this.binSize = this.batcher.getSize();
		this.binDocumentCounts = new int[this.binSize];
		this.wordToBin = new HashMap<String, WordBin>();
		System.out.println("Bin size: " + String.valueOf(this.binSize));
	}
	
	public void setWordBinValue(String word, Date binDate, int value) {
		
		if (!this.wordToBin.containsKey(word)) {
			this.wordToBin.put(word, new WordBin(word, this.binSize));
		}
		WordBin wordBin = this.wordToBin.get(word);
		wordBin.set(getBinIndex(binDate), value);
	}
	
	public void addADocument(Collection<String> words, Date binDate) {
		for (String word : words) {
			this.addWordToBin(word, binDate);
		}
		this.increaseDocumentCount(binDate);
	}
		
	private void addWordToBin(String word, Date binDate) {
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
	
	public String getDateStringByIndex(int binIndex) {
		return this.batcher.getDateStringByIndex(binIndex);
	}
	
	public int getBinIndex(Date binDate) {
		return this.batcher.getIndexByDate(binDate);
	}
	
	private void increaseDocumentCount(Date binDate) {
		this.binDocumentCounts[getBinIndex(binDate)]++;
	}
	
	public int[] getBinDocumentCount() {
		return this.binDocumentCounts;
	}
	
	public int getBinSize() {
		return this.binSize;
	}
}
