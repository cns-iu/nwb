package edu.iu.nwb.analysis.burst.bins;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import org.osgi.service.log.LogService;

import edu.iu.nwb.analysis.burst.DocumentRetriever;
import edu.iu.nwb.analysis.burst.DocumentRetrieverFactory;

import prefuse.data.Table;

public class WordBinsGenerator {
	public static final SimpleDateFormat YEAR_FORMAT = new SimpleDateFormat("yyyy");
	
	private String textColumnTitle;
	private String textSeparator;
	private String documentColumnTitle;
	private String dateColumnTitle;
	private SimpleDateFormat dateFormat;
	private boolean ignoreEmpty;
	private Table data;
	private LogService logger;
	
	public WordBinsGenerator(
			LogService logger,
			Table data,
			String documentColumnTitle,
			String textColumnTitle, 
			String textSeparator, 
			String dateColumnTitle, 
			String dateFormatString, 
			boolean ignoreEmpty) {
		
		this.logger = logger;
		this.data = data;
		this.documentColumnTitle = documentColumnTitle;
		this.textColumnTitle = textColumnTitle;
		this.textSeparator = textSeparator;
		this.dateColumnTitle = dateColumnTitle;
		this.dateFormat = new SimpleDateFormat(dateFormatString);
		this.ignoreEmpty = ignoreEmpty;
	}
	
	public WordBins generateWordBins() {

		/* Fill up missing years and create a empty WordBins */
		WordBins wordBins = createWordBins();
		
		/*Generate */
		generateWordBinsEntries(wordBins);
		
		return wordBins;
	}
	
	/*
	 * Create document count based on date (SortedMap dates) and add words token based on 
	 * bin (SortedMap wordsMap)
	 */
	private void generateWordBinsEntries(WordBins wordBins) {
		Set<Object> documents = new HashSet<Object>();
		DocumentRetriever retriever = DocumentRetrieverFactory.createForColumn(this.documentColumnTitle);
		for(int row = 0; row < this.data.getRowCount(); row++) {
			String dateString = this.data.getString(row, this.dateColumnTitle);
			if(dateString.length() > 0) {
				
				/* Extract unique word tokens from the text field */
				String text = this.data.getString(row, this.textColumnTitle);
				Collection<String> words = extractUniqueWords(text, this.textSeparator);
				
				/* Ignore document that has empty text field */
				if(this.ignoreEmpty && words.size() == 0) {
					continue;
				}
				
				/* Extract Year */
				try {
					Date date = dateFormat.parse(dateString);
					int year = Integer.valueOf(YEAR_FORMAT.format(date));
					
					/*  Avoid re-process duplicated documents. Treat null documentId as different documents */
					Object documentId = retriever.retrieve(this.data, row, this.documentColumnTitle);
					if(documentId == null || !documents.contains(documentId)) {
	
						/* Add document */
						if(documentId != null) {
							documents.add(documentId);
						}
						
						/* Add all words to its bin based on the given year */
						wordBins.addADocument(words, year);
					}
				} catch (ParseException e) {
					/* log and let user know the date */
					this.logger.log(LogService.LOG_WARNING, "Problems parsing value " + dateString + ", verify chosen date format " + dateFormat.toLocalizedPattern() + " matches format in file.", e);
				}
			}
		}
	}
	
	private WordBins createWordBins() {
		
		/* Extract all the date values */
		Set<Integer> yearSet= new TreeSet<Integer>();
		for(int row = 0; row < this.data.getRowCount(); row++) {
			String dateString = this.data.getString(row, this.dateColumnTitle);
			if(dateString.length() > 0) {
				try {
					Date date = this.dateFormat.parse(dateString);
					yearSet.add(Integer.valueOf(YEAR_FORMAT.format(date)));
				} catch (ParseException e) {
					/* Just continue and do nothing for now. We will handle it later. */
					continue;
				}
			}
		}
		
		Integer[] years = yearSet.toArray(new Integer[0]);
		/* Extract the earliest and latest date */
		int startYear = years[0];
		int endYear = years[years.length - 1];
		
		/* Create WordBins with bin size equals to year length from startDate and endDate */
		WordBins wordBins = new WordBins(startYear, endYear);
		
		return wordBins;
	}
	
	private Collection<String> extractUniqueWords(String text, String separator) {
		HashSet<String> wordSet = new HashSet<String>();
		
		if(text != null) {
			String[] strings = text.split("\\" + separator);
			for (String word : strings) {
				word = word.trim();
				if(word.length() > 0) {
					wordSet.add(word);
				}
			}
		}
		
		return wordSet;
	}
}
