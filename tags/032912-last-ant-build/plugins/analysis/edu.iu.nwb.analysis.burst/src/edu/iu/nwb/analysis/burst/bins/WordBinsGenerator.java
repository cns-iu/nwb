package edu.iu.nwb.analysis.burst.bins;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.Set;

import org.osgi.service.log.LogService;

import edu.iu.nwb.analysis.burst.BurstException;
import edu.iu.nwb.analysis.burst.Constants;
import edu.iu.nwb.analysis.burst.DocumentRetriever;
import edu.iu.nwb.analysis.burst.DocumentRetrieverFactory;
import edu.iu.nwb.analysis.burst.batcher.BatchFactory;

import prefuse.data.Table;

public class WordBinsGenerator {
	public static final SimpleDateFormat YEAR_FORMAT = new SimpleDateFormat("yyyy");
	
	private String textColumnTitle;
	private String textSeparator;
	private String documentColumnTitle;
	private String dateColumnTitle;
	private String batchBy;
	private SimpleDateFormat dateFormat;
	private Table data;
	private LogService logger;
	private boolean ignoreEmpty;
	private int batchByUnits = 1;
	
	public WordBinsGenerator(
			LogService logger,
			Table data,
			Dictionary<String, Object> parameters) {
		
		this.logger = logger;
		this.data = data;
		this.documentColumnTitle = (String) parameters.get(Constants.DOCUMENT_COLUMN);
		this.textColumnTitle = (String) parameters.get(Constants.TEXT_COLUMN);
		this.textSeparator = (String) parameters.get(Constants.TEXT_SEPARATOR_COLUMN);
		this.dateColumnTitle = (String) parameters.get(Constants.DATE_COLUMN);
		this.dateFormat = 
			new SimpleDateFormat((String) parameters.get(Constants.DATE_FORMAT_COLUMN));
		this.batchBy = (String) parameters.get(Constants.BATCH_BY_COLUMN);
		this.ignoreEmpty = (Boolean) parameters.get(Constants.IGNORE_EMPTY_COLUMN);
		this.setBatchByUnits((Integer) parameters.get(Constants.BATCH_BY_UNITS_COLUMN));
	}
	
	public WordBins generateWordBins() throws BurstException {

		/* Fill up missing years and create a empty WordBins */
		WordBins wordBins = createWordBins();
		
		/* Generate */
		generateWordBinsEntries(wordBins);
		
		return wordBins;
	}
	
	/*
	 * Create document count based on date (SortedMap dates) and add words token based on 
	 * bin (SortedMap wordsMap)
	 */
	private void generateWordBinsEntries(WordBins wordBins) {
		Set<Object> documents = new HashSet<Object>();
		DocumentRetriever retriever = 
			DocumentRetrieverFactory.createForColumn(this.documentColumnTitle);
		for (int row = 0; row < this.data.getRowCount(); row++) {
			String dateString = this.data.getString(row, this.dateColumnTitle);
			if (dateString != null && dateString.length() > 0) {
				
				/* Extract unique word tokens from the text field */
				String text = this.data.getString(row, this.textColumnTitle);
				Collection<String> words = extractUniqueWords(text, this.textSeparator);
				
				/* Ignore document that has empty text field */
				if (this.ignoreEmpty && words.size() == 0) {
					continue;
				}
				
				/* Extract date */
				try {
					/*  
					 * Avoid re-process duplicated documents. 
					 * Treat null documentId as different documents.
					 */
					Object documentId = 
						retriever.retrieve(this.data, row, this.documentColumnTitle);
					if (documentId == null || !documents.contains(documentId)) {
	
						/* Add document */
						if (documentId != null) {
							documents.add(documentId);
						}
						
						/* Add all words to its bin based on the given date */
						Date date = dateFormat.parse(dateString);
						wordBins.addADocument(words, date);
					}
				} catch (ParseException e) {
					/* log and let user know the date */
					this.logger.log(LogService.LOG_WARNING, "Problems parsing value " 
							+ dateString 
							+ ", verify the given date format '" 
							+ dateFormat.toLocalizedPattern() 
							+ "' matches format in file.", e);
				}
			}
		}
	}
	
	private WordBins createWordBins() throws BurstException {		
		Date startDate = null;
		Date endDate = null;
		String dateStringExample = null;
		
		/* Extract all the date values */
		for (int row = 0; row < this.data.getRowCount(); row++) {
			String dateString = this.data.getString(row, this.dateColumnTitle);
			if (dateString != null && dateString.length() > 0) {
				try {
					Date date = this.dateFormat.parse(dateString);
					/* 
					 * If this is first entry, init the start and end dates to the date. Else
					 * compare the date with the start and end dates; and replace accordingly.
					 */
					if (startDate == null) {
						startDate = date;
						endDate = date;
					} else {
						if (date.after(endDate)) {
							endDate = date;
						} else if (date.before(startDate)) {
							startDate = date;
						}
					}
				} catch (ParseException e) {
					/* Just continue and do nothing for now. We will handle it later. */
					if (dateStringExample == null) {
						dateStringExample = dateString;
					}
					continue;
				}
			}
		}
		
		/* No matched date is found. Stop the algorithm and throw exception */
		if (startDate == null || endDate == null) {
			String errorMsg = "No valid date is found. Please make sure the given date format '"
				+ dateFormat.toLocalizedPattern() 
				+ "' matches the date format of the selected date column.";
			
			if (dateStringExample != null) {
				errorMsg += " An example date value is '" + dateStringExample + "'.";
			}
			
			errorMsg += " More information are available at [url]"
				+ "http://java.sun.com/j2se/1.4.2/docs/api/java/text/SimpleDateFormat.html[/url].";
			throw new BurstException(errorMsg);
		}
		
		WordBins wordBins;
		/* Create WordBins with bin size equals to year length from startDate and endDate */
		try {
			wordBins = new WordBins(
				BatchFactory.getBatchFactory(this.batchBy).getBatcher(
						startDate, endDate, this.batchByUnits));
		} catch (ArithmeticException e) {
			throw new OutOfMemoryError(e.getMessage());
		}
		
		return wordBins;
	}
	
	private Collection<String> extractUniqueWords(String text, String separator) {
		HashSet<String> wordSet = new HashSet<String>();
		
		if (text != null) {
			String[] strings = text.split("\\" + separator);
			for (String word : strings) {
				word = word.trim();
				if (word.length() > 0) {
					wordSet.add(word);
				}
			}
		}
		
		return wordSet;
	}
	
	private void setBatchByUnits(int batchByUnits) {
		this.batchByUnits = Math.max(1, batchByUnits);
	}
}
