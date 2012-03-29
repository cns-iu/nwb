import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Dictionary;
import java.util.Hashtable;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.utilities.FileUtilities;

import edu.iu.cns.utilities.testing.LogOnlyCIShellContext;
import edu.iu.epic.visualization.linegraph.LineGraphAlgorithm;
import edu.iu.epic.visualization.linegraph.LineGraphAlgorithmFactory;
import edu.iu.nwb.converter.prefusecsv.reader.PrefuseCsvReader;


public final class Main {
	public static final String TEST_TIME_STEP_COLUMN_NAME = "Date";
	public static final String TEST_LINE_COLUMN_NAME_1 = "Open";
	public static final String TEST_LINE_COLUMN_NAME_2 = "High";
	public static final String TEST_LINE_COLUMN_NAME_3 = "Low";
	public static final String TEST_LINE_COLUMN_NAME_4 = "Close";
	public static final String TEST_LINE_COLUMN_NAME_5 = "Volume";
	
	public static final String TEST_DATA_PATH = "/";
	public static final String TEST_DATASET_1_PATH = TEST_DATA_PATH + "TestDataset1.csv";
	
	private Main() { }
	
	public static void main(String[] arguments) {
		try {
			AlgorithmFactory algorithmFactory = new LineGraphAlgorithmFactory();
			CIShellContext ciShellContext = new LogOnlyCIShellContext();
			Dictionary<String, Object> parameters = constructParameters();

			runAlgorithmOnTestDataset1(algorithmFactory, ciShellContext, parameters);
		} catch (Exception algorthmExecutionException) {
			algorthmExecutionException.printStackTrace();
		}
	}

	private static Dictionary<String, Object> constructParameters() {
		Dictionary<String, Object> parameters = new Hashtable<String, Object>();
		parameters.put(
				LineGraphAlgorithmFactory.TIME_STEP_ID, TEST_TIME_STEP_COLUMN_NAME);
		parameters.put(
			LineGraphAlgorithmFactory.BASE_LINE_ID + TEST_LINE_COLUMN_NAME_1,
			Boolean.TRUE);
		parameters.put(
			LineGraphAlgorithmFactory.BASE_LINE_ID + TEST_LINE_COLUMN_NAME_2,
			Boolean.TRUE);
		parameters.put(
			LineGraphAlgorithmFactory.BASE_LINE_ID + TEST_LINE_COLUMN_NAME_3,
			Boolean.TRUE);
		parameters.put(
			LineGraphAlgorithmFactory.BASE_LINE_ID + TEST_LINE_COLUMN_NAME_4,
			Boolean.TRUE);
		parameters.put(
			LineGraphAlgorithmFactory.BASE_LINE_ID + TEST_LINE_COLUMN_NAME_5,
			Boolean.FALSE);

		return parameters;
	}

	private static void runAlgorithmOnTestDataset1(
			AlgorithmFactory algorithmFactory,
			CIShellContext ciShellContext,
			Dictionary<String, Object> parameters)
			throws AlgorithmExecutionException, IOException, URISyntaxException {
		Data[] testDataset1 = createTestData(TEST_DATASET_1_PATH);
		Algorithm algorithm = algorithmFactory.createAlgorithm(
			testDataset1, parameters, ciShellContext);
		System.err.println("Executing algorithm on Test Dataset1...");
		algorithm.execute();
		System.err.println("...Done.");
	}

	// TODO: Make this a utility in edu.iu.cns.utilities.testing?
	private static Data[] createTestData(String testDataPath)
			throws AlgorithmExecutionException, IOException, URISyntaxException {
		//File file = loadFileFromClassPath(LineGraphAlgorithm.class, testDataPath);
		File file =
			FileUtilities.safeLoadFileFromClasspath(LineGraphAlgorithm.class, testDataPath);
		Data fileData = new BasicData(file, LineGraphAlgorithm.CSV_MIME_TYPE);

		return convertFileDataToTableData(fileData);
	}

	// TODO: Make this a utility in edu.iu.cns.utilities.testing?
	private static Data[] convertFileDataToTableData(Data fileData)
			throws AlgorithmExecutionException {
		PrefuseCsvReader csvReader = new PrefuseCsvReader(new Data[] { fileData });

		return csvReader.execute();
	}
}