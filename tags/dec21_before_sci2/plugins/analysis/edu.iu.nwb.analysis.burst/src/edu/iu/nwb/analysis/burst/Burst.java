package edu.iu.nwb.analysis.burst;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

import prefuse.data.Schema;
import prefuse.data.Table;

public class Burst implements Algorithm {
	private static final int MIN_SLENGTH = 0;
	private static final double POWER_THRESH = 0;
	Data[] data;
	Dictionary parameters;
	CIShellContext context;
	private DateFormat format;
	
	private static double HUGEN = 1000000.0;
	private static double DTRANS = 1.0;
	
	//get these from params
	private double trans = 1.0;
	private int inputLevels = 1;
	private double baseRatio = 2;
	private double increaseRatio = 2;
	
	private LogService logger;
	
	
	public Burst(Data[] data, Dictionary parameters, CIShellContext context) {
		this.data = data;
		this.parameters = parameters;
		this.context = context;

	}

	public Data[] execute() throws AlgorithmExecutionException {

		logger = (LogService)context.getService(LogService.class.getName());
		/* double gamma = ((Double) parameters.get("gamma")).doubleValue();
		double generalRatio = ((Double) parameters.get("ratio")).doubleValue();
		double firstRatio = ((Double) parameters.get("first")).doubleValue();
		int states = ((Integer) parameters.get("states")).intValue() + 1; */
		String dateColumn = (String) parameters.get("date");
		String textColumn = (String) parameters.get("text");
		String documentColumn = (String) parameters.get("document");
		boolean ignoreEmpty = (Boolean) parameters.get("ignore");
		String separator = (String) parameters.get("separator");
		String formatString = (String) parameters.get("format");
		format = new SimpleDateFormat(formatString);
		
		inputLevels = ((Integer) parameters.get("states"));
		baseRatio = ((Double) parameters.get("ratio"));
		increaseRatio = ((Double) parameters.get("first"));
		trans = ((Double) parameters.get("gamma"));
		
		Table data = (Table) this.data[0].getData();
		
		checkColumns(data, dateColumn, textColumn);
		
		DocumentRetriever retriever = DocumentRetrieverFactory.createForColumn(documentColumn);
		
		Map<Date, String> datePairs = new HashMap<Date, String>();
		SortedMap<Date, List<String>> wordsMap = new TreeMap<Date, List<String>>();
		SortedMap<Date, Integer> dates = new TreeMap<Date, Integer>();
		Map<String, int[]> entryMap = new HashMap<String, int[]>();
		Set<Object> documents = new HashSet<Object>();
				
		for(int row = 0; row < data.getRowCount(); row++) {
			String dateString = data.getString(row, dateColumn);
			if(!"".equals(dateString)) {
				try {
					Date rowDate = format.parse(dateString);
					datePairs.put(rowDate, dateString);
					String rowText = data.getString(row, textColumn);
					List<String> rowWords = this.words(rowText, separator);
					if(ignoreEmpty && rowWords.size() == 0) {
						continue;
					}
					if(!wordsMap.containsKey(rowDate)) {
						wordsMap.put(rowDate, new ArrayList<String>());
						dates.put(rowDate, 0);
					}
					wordsMap.get(rowDate).addAll(rowWords);
					Object document = retriever.retrieve(data, row, documentColumn);
					if(document == null || !documents.contains(document)) {
						dates.put(rowDate, dates.get(rowDate) + 1);
						if(document != null) {
							documents.add(document);
						}
					}
				} catch (ParseException e) {
					logger.log(LogService.LOG_WARNING, "Problems parsing value " + dateString + ", verify chosen date format " + formatString + " matches format in file.", e);
				}
			}
		}
		
		
		int numDates = dates.size();
		int currentDate = 0;
		int[] binBase = new int[numDates];
		for(Integer count : dates.values()) {
			binBase[currentDate] = count;
			currentDate++;
		}
		
		currentDate = 0;
		for(List<String> words : wordsMap.values()) {
			for(String word : words) {
				if(!entryMap.containsKey(word)) {
					entryMap.put(word, new int[numDates]);
				}
				
				entryMap.get(word)[currentDate] += 1;
			}
			currentDate++;
		}
		
		Date[] dateArray = dates.keySet().toArray(new Date[0]);
		
		Schema resultsSchema = new Schema(new String[]{"Word", "Length", "Weight", "Strength", "Start", "End"}, new Class[]{String.class, int.class, double.class, double.class, String.class, String.class});
		Table results = resultsSchema.instantiate();
		
		for(String word : entryMap.keySet()) {
			int[] entry = entryMap.get(word);
			Cell[] cells;
			try {
				cells = this.computeStates(numDates, entry, binBase);
				double totalPower = 0;
				for(int j = 0; j < numDates; j++) {
					Cell currentCell = cells[j];
					
					for(int k = currentCell.candidate.length - 2; k >= 0; k--) {
						if(currentCell.candidate[k]
						        && currentCell.breakpoint[k] - j + 1 >= MIN_SLENGTH
						        && currentCell.totalPower[k] >= POWER_THRESH) {
						        //&& k != currentCell.minRateClass) { the original includes !SHARP_OPT or this, and sharp opt is never used
							int startIndex = j;
							String start = datePairs.get(dateArray[startIndex]);
							int endIndex;
							String end;
							if(currentCell.breakpoint[k] < numDates - 1) {
								endIndex = currentCell.breakpoint[k] - 1;
								end = datePairs.get(dateArray[endIndex]);
							} else {
								endIndex = currentCell.breakpoint[k] + 1;
								end = "";
							}
							if(!currentCell.subordinate[k]) {
								totalPower += currentCell.totalPower[k];
							}
							if(k == currentCell.minRateClass) {
								
							}
							logger.log(LogService.LOG_INFO,word + " starts: " + start + " ends: " + end);
							int row = results.addRow();
							results.setString(row, "Word", word);
							results.setInt(row, "Length", endIndex - startIndex + 1);
							results.setDouble(row, "Weight", currentCell.totalPower[k]);
							results.setDouble(row, "Strength", totalPower);
							results.setString(row, "Start", start);
							results.setString(row, "End", end);
						}
					}
					
				}
			} catch (BurstException e) {
				throw new AlgorithmExecutionException(e.getMessage(), e);
			}
		}
		
		Data output = new BasicData(results, Table.class.getName());
		Dictionary metadata = output.getMetadata();
		metadata.put(DataProperty.LABEL, "Burst detection analysis (" + dateColumn + ", " + textColumn + "): maximum burst level " + inputLevels);
		metadata.put(DataProperty.PARENT, this.data[0]);
		metadata.put(DataProperty.TYPE, DataProperty.MATRIX_TYPE);
		
		return new Data[]{ output };
	}

	private void checkColumns(Table data, String dateColumn, String textColumn) throws AlgorithmExecutionException {
		checkForColumn(data, dateColumn);
		checkForColumn(data, textColumn);
	}

	private void checkForColumn(Table data, String dateColumn)
			throws AlgorithmExecutionException {
		if(!data.canGetString(dateColumn)) {
			throw new AlgorithmExecutionException("The column '" + dateColumn + "' does not exist or cannot be accessed as a string.");
		}
	}
	
	
	private Cell[] computeStates(int n, int[] entry, int[] binBase) throws BurstException {
		//logger.log(LogService.LOG_INFO, "## " + bin_k + " " + bin_n + " " + expected);
		
		
		double transCost = computeTransCost(n);
		
		int levels = inputLevels + 1;
		
		Cell[] cells = computeCosts(n, levels, entry, binBase);
		
		
		
		
		int q = computeTotals(cells, n, transCost, levels);
		computePathAndMark(cells, n, levels, q);
		
		int[] leftBarrier = new int[levels];
		
		for(int k = 0; k < levels; k++) {
			leftBarrier[k] = -1;
		}
		
		for(int j = 0; j < n; j++) {
			Cell currentCell = cells[j];
			
			for(int k = 0; k < levels - 1; k++) {
				if(currentCell.mark[k]) {
					leftBarrier[k] = j;
				}
			}
			
			for(int k = 0; k < currentCell.path; k++) {
				if(leftBarrier[k] >= 0) {
					Cell barrierCell = cells[leftBarrier[k]];
					barrierCell.breakpoint[k] = j;
					barrierCell.candidate[k] = true;
					currentCell.endCandidate[k] = 1;
					leftBarrier[k] = -1;
				}
			}
			
			for(int k = currentCell.path; k < levels - 1; k++) {
				if(leftBarrier[k] >= 0) {
					Cell barrierCell = cells[leftBarrier[k]];
					barrierCell.power[k] += currentCell.cost[k+1] - currentCell.cost[k];
					barrierCell.totalPower[k] += currentCell.cost[levels - 1] - currentCell.cost[k];
				}
			}
		}
		Cell lastCell = cells[n - 1];
		
		for(int k = 0; k < levels - 1; k++) {
			if(leftBarrier[k] >= 0) {
				Cell barrierCell = cells[leftBarrier[k]];
				barrierCell.breakpoint[k] = n - 1;
				barrierCell.candidate[k] = true;
				lastCell.endCandidate[k] = 1;
				leftBarrier[k] = -1;
			}
		}
		
		for(int j = 0; j < n - 1; j++) {
			Cell currentCell = cells[j];
			
			int p = -1;
			q = -1;
			for(int k = 0; k < levels - 1; k++) {
				if(currentCell.candidate[k]) {
					p = k;
					if(q < 0) {
						q = k;
					}
				}
			}
			if(p < 0) {
				continue;
			}
			
			currentCell.minRateClass = q;
			for(int k = 0; k < p; ++k) {
				if(currentCell.candidate[k]) {
					currentCell.totalPower[p] += currentCell.power[k];
					currentCell.subordinate[k] = true;
				}
			}
		}
		
		return cells;
	}

	private Cell[] computeCosts(int n, int levels, int[] entry, int[] binBase) throws BurstException {
		double expected = computeExpected(n, entry, binBase);
		
		double[] fRate = initializeFRate(expected, levels);

		
		Cell[] cells = new Cell[n];
		
		for(int j = 0; j < n; j++) {
			Cell cell = cells[j] = new Cell(levels);
			for(int k = 0; k < levels; k++) {
				//logger.log(LogService.LOG_INFO, "" + fRate[k] + " " + entry[j] + " " + binBase[j]);
				cell.cost[k] = binomW(1.0 / fRate[k], entry[j], binBase[j]);
				//logger.log(LogService.LOG_INFO, "## " + j + " " + k + " = " + cell.cost[k]);
			}
		}
		return cells;
	}

	private int computeTotals(Cell[] cells, int n, double transCost, int levels) {
		
		Cell firstCell = cells[0];
		Cell lastCell = cells[n - 1];
		
		for(int k = 0; k < levels; k++) {
			firstCell.total[k] = firstCell.cost[k] + transCost * (levels - 1 - k);
		}
		
		for(int j = 1; j < n; j++) {
			Cell currentCell = cells[j];
			Cell previousCell = cells[j - 1];
			for(int k = 0; k < levels; k++) {
				double d = currentCell.cost[k] + previousCell.total[0];
				int q = 0;
				double tmpD;
				for(int m = 1; m < levels; m++) {
					if(m > k && (tmpD = currentCell.cost[k] + previousCell.total[m] + transCost * (m - k)) < d) {
						d = tmpD;
						q = m;
					} else if(m <= k && (tmpD = currentCell.cost[k] + previousCell.total[m]) < d) {
						d = tmpD;
						q = m;
					}
				}
				currentCell.total[k] = d;
				currentCell.previous[k] = q;
				//logger.log(LogService.LOG_INFO, "##" + this.word + " @ " + j + " " + k + " : " + currentCell.total[k] + " (" + currentCell.previous[k] + ")");
				
			}
		}
		
		
		int q = 0;
		for(int k = 0; k < levels; k++) {
			double d = lastCell.total[0];
			q = 0;
			for(int m = 1; m < levels; m++) {
				if(lastCell.total[m] < d) {
					d = lastCell.total[m];
					q = m;
				}
			}
		}
		return q;
	}

	private void computePathAndMark(Cell[] cells, int n, int levels, int q) {
		
		Cell firstCell = cells[0];
		Cell lastCell = cells[n - 1];
		
		lastCell.path = q;
		
		for(int j = n - 2; j >= 0; j--) {
			Cell nextCell = cells[j+1];
			Cell currentCell = cells[j];
			currentCell.path = nextCell.previous[nextCell.path];
		}
		
		for(int k = firstCell.path; k < levels - 1; k++) {
			firstCell.mark[k] = true;
		}
		
		
		for(int j = 1; j < n; j++) {
			Cell currentCell = cells[j];
			Cell previousCell = cells[j - 1];
			for(int k = currentCell.path; k < previousCell.path; k++) {
				currentCell.mark[k] = true;
			}
		}
	}

	private double[] initializeFRate(double expected, int levels) {
		double[] fRate = new double[levels];

		fRate[levels - 1] = expected;
		fRate[levels - 2] = expected / baseRatio;

		for(int j = levels - 3; j >= 0; j--) {
			fRate[j] = fRate[j+1]/increaseRatio;
		}
		return fRate;
	}

	private double computeTransCost(int n) {
		double transCost = trans * Math.log(n + 1) - Math.log(DTRANS);
		
		if(transCost < 0.0) {
			transCost = 0.0;
		}
		return transCost;
	}

	private double computeExpected(int n, int[] entry, int[] binBase) throws BurstException {
		int bin_n = 0;
		int bin_k = 0;
		
		for(int i = 0; i < n; i++) {
			bin_k += entry[i];
			bin_n += binBase[i];
		}
		
		if(bin_n == 0 || bin_k ==0) {
			throw new BurstException("A word bursted on is never used; this should be impossible, please notify the algorithm author");
		}
		
		double expected = (double) bin_n/ (double) bin_k;
		return expected;
	}
	
	private double logChoose(int n, int k) {
			int index;
			double value = 0.0;
			
			for(index=n; index > n-k; --index) {
				value += Math.log(index);
			}
			
			for(index=1; index <= k; ++index) {
				value -= Math.log(index);
			}
			
			return value;
	}
	
	private double binomW(double probability, int k, int n) {
		if(probability >= 1.0) {
			return HUGEN;
		} else {
			
			return -1 * (logChoose(n,k) + k * Math.log(probability) + (n-k) * Math.log(1.0 - probability));
		}
	}

	

	private List<String> words(String text, String separator) {
		//String[] strings = text.replaceAll("\\p{Punct}", "").toLowerCase().split("\\s");
		
		List<String> words = new ArrayList<String>();
		if(text == null) {
			return words;
		}
		
		String[] strings = text.split("\\" + separator);
		for(String word : strings) {
			word = word.trim();
			if(word.length() > 0) {
				words.add(word);
			}
		}

		return words;
	}
}