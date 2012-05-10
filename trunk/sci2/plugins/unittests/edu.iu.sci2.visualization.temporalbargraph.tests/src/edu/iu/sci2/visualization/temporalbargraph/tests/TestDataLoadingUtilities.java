package edu.iu.sci2.visualization.temporalbargraph.tests;
import java.io.File;

/**
 * A file loading utility class for getting different types of testing data for
 * {@link edu.iu.sci2.visualization.temporalbargraph.print.TemporalBarGraphAlgorithm}
 * 
 */
public class TestDataLoadingUtilities {
	// Suppress default constructor for noninstantiability
	private TestDataLoadingUtilities() {
		throw new AssertionError();
	}

	/**
	 * Get a file that represents standard data for testing
	 * {@linkplain edu.iu.sci2.visualization.temporalbargraph.print.TemporalBarGraphAlgorithm}.
	 * 
	 * @return a standard CSV {@link File}.
	 */
	public static File getStandardData() {
		final String FILENAME = "tbg_cornell.nsf.csv"; 
		return getTestFile(FILENAME);
	}

	/**
	 * Get a file that represents a very short (temporal) data span for testing
	 * {@linkplain edu.iu.sci2.visualization.temporalbargraph.print.TemporalBarGraphAlgorithm}.
	 * 
	 * @return A CSV {@link File}.
	 */
	public static File getShortData() {
		final String FILENAME = "tbg_one_year.csv";
		return getTestFile(FILENAME);
	}
	
	/**
	 * Get a file that represents a very long (temporal) data span for testing
	 * {@linkplain edu.iu.sci2.visualization.temporalbargraph.print.TemporalBarGraphAlgorithm}.
	 * 
	 * @return A CSV {@link File}.
	 */
	public static File getLongData() {
		final String FILENAME = "tbg_one_hundred_years.csv";
		return getTestFile(FILENAME);
	}
	
	/**
	 * Get a file that represents data that once was an issue representing dates correctly for
	 * {@linkplain edu.iu.sci2.visualization.temporalbargraph.print.TemporalBarGraphAlgorithm}.
	 * 
	 * @return A CSV {@link File}.
	 */
	public static File getDateIssueData() {
		final String FILENAME = "tbg_nete_dateissue.csv";
		return getTestFile(FILENAME);
	}
	
	/**
	 * Get a file that represents one record for testing
	 * {@linkplain edu.iu.sci2.visualization.temporalbargraph.print.TemporalBarGraphAlgorithm}.
	 * 
	 * @return A CSV {@link File}.
	 */
	public static File getOneRecordData() {
		final String FILENAME = "tbg_one_record.csv";
		return getTestFile(FILENAME);
	}
	
	/**
	 * Get a test file that is on the class path.
	 * 
	 * @param filename
	 *            The name of the file to be retrieved.
	 * @return A {@link File} from the given {@code filename}.
	 */
	private static File getTestFile(String filename) {
		File file = new File("sampledata" + File.separator + filename);
		return file;

	}
	
}
