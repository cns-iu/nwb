package edu.iu.epic.visualization.linegraph;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import javax.swing.SwingUtilities;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.utilities.FileUtilities;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import stencil.streams.Tuple;
import stencil.util.BasicTuple;
import edu.iu.cns.utilities.testing.LogOnlyCIShellContext;
import edu.iu.epic.visualization.linegraph.utilities.StencilData;
import edu.iu.epic.visualization.linegraph.utilities.StencilException;
import edu.iu.epic.visualization.linegraph.utilities.StencilRunner;
import edu.iu.epic.visualization.linegraph.utilities.StreamSource;
import edu.iu.nwb.converter.prefusecsv.reader.PrefuseCsvReader;

public class LineGraphAlgorithm implements Algorithm {
	public static final String WINDOW_TITLE = "Line Graph Visualization";

	public static final String TEST_TIME_STEP_COLUMN_NAME = "Date";
	public static final String TEST_LINE_COLUMN_NAME_1 = "Open";
	public static final String TEST_LINE_COLUMN_NAME_2 = "High";
	public static final String TEST_LINE_COLUMN_NAME_3 = "Low";
	public static final String TEST_LINE_COLUMN_NAME_4 = "Close";
	public static final String TEST_LINE_COLUMN_NAME_5 = "Volume";
	public static final String TEST_STENCIL_STREAM_NAME = "Data";
	public static final String TEST_STENCIL_TIME_STEP_ID = "Timestep";
	public static final String TEST_LINE_COLUMN_ID = "Line";
	public static final String TEST_VALUE_ID = "Value";

	public static final String STENCIL_STREAM_NAME = "Stocks";
	public static final String STENCIL_X_AXIS_NAME = "Date";
	public static final String STENCIL_Y_AXIS_NAME = "Open";

	public static final String BASE_STENCIL_PATH = "/edu/iu/epic/visualization/linegraph/stencil/";
	public static final String LINE_GRAPH_STENCIL_PATH = BASE_STENCIL_PATH
			+ "lineGraph.stencil";

	public static final String X_AXIS_NAME_KEY = "x_axis_name";
	public static final String Y_AXIS_NAME_KEY = "y_axis_name";

	public static final String CSV_MIME_TYPE = "file:text/csv";

	public static final String TEST_DATA_PATH = "/edu/iu/epic/visualization/linegraph/testing/";
	public static final String TEST_DATASET_1_PATH = TEST_DATA_PATH
			+ "TestDataset1.csv";

	private Table inputTable;
	private String xAxisName;
	private String yAxisName;

	private LogService logger;

	public LineGraphAlgorithm(Data[] data, Dictionary parameters,
			CIShellContext ciShellContext) {
		this.inputTable = (Table) data[0].getData();

		this.xAxisName = (String) parameters.get(X_AXIS_NAME_KEY);
		this.yAxisName = (String) parameters.get(Y_AXIS_NAME_KEY);

		this.logger = (LogService) ciShellContext.getService(LogService.class
				.getName());
	}

	public Data[] execute() throws AlgorithmExecutionException {

		StencilData stencilData = collectStencilData();
		
		StencilRunner stencilRunner = createStencilRunner(stencilData);

		showStencilRunner(stencilRunner);

		stencilRunner.playWithLoadedData();

		// // TODO: Wire this up so it uses actual input data.
		// // TODO: Make export button nicer, and get EPS working?
		// // TODO: Check boxes to toggle lines.
		
		return new Data[0];
	}
	
	private StencilData collectStencilData() throws AlgorithmExecutionException {

		// get the stencil script

		File stencilFile = getStencilFile();
		String stencilScript = extractStencilScript(stencilFile);

		// get the stencil streams

		List<StreamSource> streamSources = getSampleStreamSourcesForTesting();

		// return the stencil script and streams together as "stencil data"

		final StencilData stencilData = new StencilData(stencilScript,
				streamSources);
		return stencilData;
	}

	private StencilRunner createStencilRunner(StencilData stencilData)
			throws AlgorithmExecutionException {
		try {
			return new StencilRunner(stencilData);
		} catch (StencilException e) {
			throw new AlgorithmExecutionException(e);
		}
	}

	private void showStencilRunner(final StencilRunner stencilRunner)
			throws AlgorithmExecutionException {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					try {
						stencilRunner.show();
					} catch (Exception e) {
						/*
						 * Wrap all Exceptions as RuntimeExceptions, and rethrow
						 * the inner exception on the other side.
						 */
						throw new RuntimeException(e);
					}
				}
			});
		} catch (InvocationTargetException e) {
			// TODO: This may not be the behavior we want
			throw new AlgorithmExecutionException(e.getMessage(), e);
		} catch (InterruptedException e) {
			throw new AlgorithmExecutionException(e);
		}
	}

	private String extractStencilScript(File stencilFile)
			throws AlgorithmExecutionException {
		try {
			return FileUtilities.readEntireTextFile(stencilFile);
		} catch (IOException e) {
			String message = "A problem occurred while trying to read the file "
					+ "\"" + stencilFile.getAbsolutePath() + "\".";
			throw new AlgorithmExecutionException(message, e);
		}

	}

	public File getStencilFile() throws AlgorithmExecutionException {
		try {
			return loadFileFromClassPath(LineGraphAlgorithm.class,
					LINE_GRAPH_STENCIL_PATH);
		} catch (URISyntaxException e) {
			String message = "Unable to load Stencil file " + "'"
					+ LINE_GRAPH_STENCIL_PATH + "'."
					+ "Cannot complete operation.";

			throw new AlgorithmExecutionException(message, e);
		}
	}

	public List<StreamSource> getSampleStreamSourcesForTesting() {

		List<StreamSource> streams = new ArrayList<StreamSource>();
		StreamSource testStream1 = new StreamSource(
				LineGraphAlgorithm.this.inputTable, TEST_TIME_STEP_COLUMN_NAME,
				TEST_LINE_COLUMN_NAME_1, TEST_STENCIL_STREAM_NAME,
				TEST_STENCIL_TIME_STEP_ID, TEST_LINE_COLUMN_ID, TEST_VALUE_ID);
		streams.add(testStream1);

		StreamSource testStream2 = new StreamSource(
				LineGraphAlgorithm.this.inputTable, TEST_TIME_STEP_COLUMN_NAME,
				TEST_LINE_COLUMN_NAME_2, TEST_STENCIL_STREAM_NAME,
				TEST_STENCIL_TIME_STEP_ID, TEST_LINE_COLUMN_ID, TEST_VALUE_ID);
		streams.add(testStream2);

		StreamSource testStream3 = new StreamSource(
				LineGraphAlgorithm.this.inputTable, TEST_TIME_STEP_COLUMN_NAME,
				TEST_LINE_COLUMN_NAME_3, TEST_STENCIL_STREAM_NAME,
				TEST_STENCIL_TIME_STEP_ID, TEST_LINE_COLUMN_ID, TEST_VALUE_ID);
		streams.add(testStream3);

		StreamSource testStream4 = new StreamSource(
				LineGraphAlgorithm.this.inputTable, TEST_TIME_STEP_COLUMN_NAME,
				TEST_LINE_COLUMN_NAME_4, TEST_STENCIL_STREAM_NAME,
				TEST_STENCIL_TIME_STEP_ID, TEST_LINE_COLUMN_ID, TEST_VALUE_ID);
		streams.add(testStream4);

		return streams;

	}

	public static void main(String[] arguments) {
		try {
			AlgorithmFactory algorithmFactory = new LineGraphAlgorithmFactory();
			CIShellContext ciShellContext = new LogOnlyCIShellContext();
			Dictionary<String, Object> parameters = constructParameters();

			runAlgorithmOnTestDataset1(algorithmFactory, ciShellContext,
					parameters);
		} catch (Exception algorthmExecutionException) {
			algorthmExecutionException.printStackTrace();
		}
	}

	// TODO: Better error handling of null/invalid values in the two columns?
	private Tuple createTuple(Table table, int rowIndex) {
		prefuse.data.Tuple row = table.getTuple(rowIndex);

		return new BasicTuple(STENCIL_STREAM_NAME, Arrays.asList(new String[] {
				STENCIL_X_AXIS_NAME, STENCIL_Y_AXIS_NAME }), Arrays
				.asList(new String[] { row.get(this.xAxisName).toString(),
						row.get(this.yAxisName).toString() }));
	}

	private static Dictionary<String, Object> constructParameters() {
		Dictionary<String, Object> parameters = new Hashtable<String, Object>();
		parameters.put(X_AXIS_NAME_KEY, STENCIL_X_AXIS_NAME);
		parameters.put(Y_AXIS_NAME_KEY, STENCIL_Y_AXIS_NAME);

		return parameters;
	}

	private static void runAlgorithmOnTestDataset1(
			AlgorithmFactory algorithmFactory, CIShellContext ciShellContext,
			Dictionary<String, Object> parameters)
			throws AlgorithmExecutionException, URISyntaxException {
		Data[] testDataset1 = createTestData(TEST_DATASET_1_PATH);
		Algorithm algorithm = algorithmFactory.createAlgorithm(testDataset1,
				parameters, ciShellContext);
		System.err.println("Executing algorithm on Test Dataset1...");
		algorithm.execute();
		System.err.println("...Done.");
	}

	// TODO: Make this a utility in edu.iu.cns.utilities.testing?
	private static Data[] createTestData(String testDataPath)
			throws AlgorithmExecutionException, URISyntaxException {
		File file = loadFileFromClassPath(LineGraphAlgorithm.class,
				testDataPath);
		Data fileData = new BasicData(file, CSV_MIME_TYPE);

		return convertFileDataToTableData(fileData);
	}

	// TODO: Make this a utility in edu.iu.cns.utilities.testing?
	private static Data[] convertFileDataToTableData(Data fileData)
			throws AlgorithmExecutionException {
		PrefuseCsvReader csvReader = new PrefuseCsvReader(
				new Data[] { fileData });

		return csvReader.execute();
	}

	private static File loadFileFromClassPath(Class clazz, String filePath)
			throws URISyntaxException {
		URL fileURL = clazz.getResource(filePath);

		return new File(fileURL.toURI());
	}
}