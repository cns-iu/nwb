package edu.iu.nwb.analysis.burst;

import java.util.Dictionary;
/**
 * This is a re-implementation version of the old Java code base. Few 
 * attentions while before start reading the code.
 * 1. Read the dynamicburst.c which is the origin C implementation.
 * 2. The indexing of the automaton states are reversed compare to the 
 * paper.
 * 3. computeStates() are implemented by converting from orginal C code.
 * 4. A little different from original implementation is we generate the
 * empty bin to fill up the gates between years. In the original 
 * implementation, batches are treated as continuous, even the year is 
 * differences. In other words, we create empty bin to make the batches 
 * continuous based on year. 
 * 
 * Please refer to the dynamicburst.c for the origin implementation of
 * the computeStates. There are a lot of comments added into the 
 * dynamic.c while I reading the C implementation (The original C code
 * don't have any comments). The WordBins is not included in the original
 * C code. It is created based on the description in the paper. please 
 * refer to the batch solution in the Enumerating section.
 * 
 * @author kongch
 * 
 */

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

import edu.iu.nwb.analysis.burst.bins.WordBin;
import edu.iu.nwb.analysis.burst.bins.WordBins;
import edu.iu.nwb.analysis.burst.bins.WordBinsGenerator;

import prefuse.data.Schema;
import prefuse.data.Table;

public class Burst implements Algorithm {
	public static final int MIN_SLENGTH = 0;
	public static final double POWER_THRESH = 0;
	public static final double HUGEN = 1000000.0;
	public static final double DTRANS = 1.0;
	
	/* Input columns' name */
	public static final String DATE_COLUMN = "date";
	public static final String DATE_FORMAT_COLUMN = "format";
	public static final String TEXT_COLUMN = "text";
	public static final String TEXT_SEPARATOR_COLUMN = "separator";
	public static final String IGNORE_EMPTY_COLUMN = "ignore";
	public static final String BURSTING_STATES_COLUMN = "states";
	public static final String RATIO_COLUMN = "ratio";
	public static final String GAMMA_COLUMN = "gamma";
	public static final String DOCUMENT_COLUMN = "document";
	
	/* Output columns' name */
	public static final String WORD_COLUMN = "Word";
	public static final String LEVEL_COLUMN = "Level";
	public static final String WEIGHT_COLUMN = "Weight";
	public static final String LENGTH_COLUMN = "Length";
	public static final String START_COLUMN = "Start";
	public static final String END_COLUMN = "End";
	
	/* Updated parameters */
	private double gamma = 1.0; // parameter that controls the ease with which the automaton can change states. 'trans' in C code
	private int inputStates = 1; // the higher  bursting states
	private double densityScaling = 2; // density scaling which will affect the probability of the burst happens 
	private LogService logger;
	private Data[] data;
	private Dictionary<String, Object> parameters;
	
	
	public Burst(Data[] data, Dictionary<String, Object> parameters, CIShellContext context) {
		this.data = data;
		this.parameters = parameters;
		this.logger = (LogService)context.getService(LogService.class.getName());
	}
	
	public Data[] execute() throws AlgorithmExecutionException {

		String dateColumnTitle = (String) this.parameters.get(DATE_COLUMN);
		String textColumnTitle = (String) this.parameters.get(TEXT_COLUMN);
		String documentColumnTitle = (String) this.parameters.get(DOCUMENT_COLUMN);
		boolean ignoreEmpty = (Boolean) this.parameters.get(IGNORE_EMPTY_COLUMN);
		String textSeparator = (String) this.parameters.get(TEXT_SEPARATOR_COLUMN);
		String dateFormatString = (String) this.parameters.get(DATE_FORMAT_COLUMN);
		
		this.inputStates = ((Integer) this.parameters.get(BURSTING_STATES_COLUMN));
		this.densityScaling = ((Double) this.parameters.get(RATIO_COLUMN));
		this.gamma = ((Double) this.parameters.get(GAMMA_COLUMN));
		
		Table data = (Table) this.data[0].getData();		
		checkColumns(data, dateColumnTitle, textColumnTitle);
		
		/*
		 * WordBins contains a list of WordBin where each WordBin contains a word 
		 * and n number of bins. Each bin represent a time slot and hold the number
		 * of related douments that contains the word in a specified time slot.
		 */
		WordBinsGenerator wordBinsGenerator = new WordBinsGenerator(
				this.logger,
				data, 
				documentColumnTitle,
				textColumnTitle, 
				textSeparator, 
				dateColumnTitle, 
				dateFormatString, 
				ignoreEmpty);
		
		WordBins wordBins = wordBinsGenerator.generateWordBins();
		int binSize = wordBins.getBinSize();
		Schema resultsSchema = new Schema(new String[]{WORD_COLUMN, LEVEL_COLUMN, WEIGHT_COLUMN, LENGTH_COLUMN, START_COLUMN, END_COLUMN}, new Class[]{String.class, int.class, double.class, int.class, String.class, String.class});
		Table results = resultsSchema.instantiate();
		
		/* Compute the burst, word by word and save it into results table */
		for(String word : wordBins.getWordSet()) {
			WordBin wordBin = wordBins.getWordBin(word);
			try {
				/* 
				 * A cell represents a specified date burst information of a token. 
				 * Compute all the burst score by date for a token 
				 */
				Cell[] cells = this.computeStates(binSize, wordBin.getBin(), wordBins.getBinDocumentCount());
				
				/* process the results for a token by date */
				for(int i = 0; i < binSize; i++) {
					Cell currentCell = cells[i];
					
					for(int level = currentCell.candidate.length - 2; level >= 0; level--) {
						if(isValidBurstCandidate(currentCell, level, i)) {
							
							/* Generate result for CSV output */
							Result result = generateResult(currentCell, word, level, i, wordBins);
							
							this.logger.log(LogService.LOG_INFO, word + " starts: " + result.getStart() + " ends: " + result.getEnd());
							
							/* Add result to row */
							int row = results.addRow();
							results.setString(row, "Word", result.getWord());
							results.setDouble(row, "Weight", result.getWeight());
							results.setInt(row, "Length", result.getLength());
							results.setInt(row, "Level", result.getLevel());
							results.setString(row, "Start", result.getStart());
							results.setString(row, "End", result.getEnd());
						}
					}
					
				}
			} catch (BurstException e) {
				throw new AlgorithmExecutionException(e.getMessage(), e);
			}
		}
		
		Data output = new BasicData(results, Table.class.getName());
		Dictionary<String, Object> metadata = output.getMetadata();
		metadata.put(DataProperty.LABEL, "Burst detection analysis (" + dateColumnTitle + ", " + textColumnTitle + "): maximum burst level " + inputStates);
		metadata.put(DataProperty.PARENT, this.data[0]);
		metadata.put(DataProperty.TYPE, DataProperty.MATRIX_TYPE);
		
		return new Data[]{ output };
	}
	
	private Result generateResult(Cell cell, String word, int level, int startIndex, WordBins wordBins) {
		String startString = String.valueOf(wordBins.getBinDate(startIndex));
		String endString;
		
		int binSize = wordBins.getBinSize();
		int endIndex = cell.breakpoint[level];
		if(endIndex < binSize - 1) {
			endIndex -= 1;
			endString = String.valueOf(wordBins.getBinDate(endIndex));
		} else {
			endString = "";
		}
		
		/* 
		 * The length is the bin length. It is not equal to the 
		 * (end year - start year) since we might lost data between 
		 * year.
		 */
		int length = endIndex - startIndex + 1;
		int state = this.inputStates - level;
		
		return new Result(word, state, cell.totalPower[level], length, startString, endString);
	}
	
	private boolean isValidBurstCandidate(Cell currentCell, int level, int binIndex) {
		return (currentCell.candidate[level]
		        &&  currentCell.breakpoint[level] - binIndex + 1 >= MIN_SLENGTH 
		        && currentCell.totalPower[level] >= POWER_THRESH);
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
		
		double transCost = computeTransCost(n);
		
		int levels = this.inputStates + 1;
		
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
					/* 
					 * This try to accumulate all level's weight into the lower burst level. 
					 * This have created double standard of total_power value of lower level
					 * with the higher levels. Based on the paper, total_power (weight) is 
					 * the rectangle area of the time period with the level.
					 * 
					 * currentCell.totalPower[p] += currentCell.power[k];
					 */
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
				cell.cost[k] = binomW(1.0 / fRate[k], entry[j], binBase[j]);
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
					/* 
					 * The '< d' have changed to '<= d' due to we are interested on lower burst level that
					 * give the same cost. Ideally, there will not exist two levels that contains the same 
					 * cost. It only happens if all costs is zero where there is not data in this bin.
					 */
					if(m > k && (tmpD = currentCell.cost[k] + previousCell.total[m] + transCost * (m - k)) <= d) {
						d = tmpD;
						q = m;
					} else if(m <= k && (tmpD = currentCell.cost[k] + previousCell.total[m]) <= d) {
						d = tmpD;
						q = m;
					}
				}
				currentCell.total[k] = d;
				currentCell.previous[k] = q;
				
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

		/* 
		 * Change it to the same. Based on the paper. It didn't make sense to have 
		 * first level ratio different from other level. This sound cheating.
		 */
		for(int j = levels - 2; j >= 0; j--) {
			fRate[j] = fRate[j+1]/this.densityScaling;
		}
		return fRate;
	}

	private double computeTransCost(int n) {
		double transCost = this.gamma * Math.log(n + 1) - Math.log(DTRANS);
		
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
			return -1 * (logChoose(n,k) + k * Math.log(probability) + (n - k) * Math.log(1.0 - probability));
		}
	}
}
