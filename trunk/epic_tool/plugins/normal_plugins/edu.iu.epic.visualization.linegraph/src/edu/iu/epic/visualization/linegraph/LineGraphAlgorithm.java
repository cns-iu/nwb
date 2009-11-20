package edu.iu.epic.visualization.linegraph;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
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

import prefuse.data.Schema;
import prefuse.data.Table;
import edu.iu.cns.utilities.testing.LogOnlyCIShellContext;
import edu.iu.epic.visualization.linegraph.core.StencilGUI;
import edu.iu.epic.visualization.linegraph.stencil.hack.PropertiesSource;
import edu.iu.epic.visualization.linegraph.utilities.StencilData;
import edu.iu.epic.visualization.linegraph.utilities.StencilException;
import edu.iu.epic.visualization.linegraph.utilities.TableStreamSource;
import edu.iu.nwb.converter.prefusecsv.reader.PrefuseCsvReader;

public class LineGraphAlgorithm implements Algorithm {
	public static final String WINDOW_TITLE = "Line Graph Visualization";
	
	public static final String TIME_STEP_COLUMN_NAME_KEY = "timeStepColumn";
	public static final String BASE_LINE_COLUMN_NAME_KEY = "line_";
	
	public static final String STENCIL_STREAM_NAME = "Data";
	public static final String STENCIL_TIMESTEP_NAME = "Timestep";
	public static final String STENCIL_LINE_NAME = "Line";
	public static final String STENCIL_VALUE_NAME = "Value";

	public static final String TEST_TIME_STEP_COLUMN_NAME = "Date";
	public static final String TEST_LINE_COLUMN_NAME_1 = "Open";
	public static final String TEST_LINE_COLUMN_NAME_2 = "High";
	public static final String TEST_LINE_COLUMN_NAME_3 = "Low";
	public static final String TEST_LINE_COLUMN_NAME_4 = "Close";
	public static final String TEST_LINE_COLUMN_NAME_5 = "Volume";

	public static final String BASE_STENCIL_PATH = "/edu/iu/epic/visualization/linegraph/stencil/";
	public static final String LINE_GRAPH_STENCIL_PATH = BASE_STENCIL_PATH + "lineGraph.stencil";
	public static final String STENCIL_CONFIGURATION_PATH =
		BASE_STENCIL_PATH + "Stencil.properties";

	public static final String CSV_MIME_TYPE = "file:text/csv";

	public static final String TEST_DATA_PATH = "/edu/iu/epic/visualization/linegraph/testing/";
	public static final String TEST_DATASET_1_PATH = TEST_DATA_PATH + "TestDataset1.csv";

	private Table inputTable;
	
	private String timeStepColumnName;
	private List<String> lineColumnNames;

	private LogService logger;

	public LineGraphAlgorithm(
			Data[] data, Dictionary<String, Object> parameters, CIShellContext ciShellContext) {
		this.inputTable = (Table)data[0].getData();
		
		this.timeStepColumnName = (String)parameters.get(TIME_STEP_COLUMN_NAME_KEY);
		this.lineColumnNames = extractLineColumnNames(parameters, this.inputTable);

		this.logger = (LogService)ciShellContext.getService(LogService.class.getName());
	}

	public Data[] execute() throws AlgorithmExecutionException {
		StencilData stencilData = collectStencilData();
		
		StencilGUI stencilGUI = createStencilGUI(stencilData);

		showStencilGUI(stencilGUI);

		runStencilGUI(stencilGUI);

		// TODO: Make Export parameters work (will need Joseph Cottem to update
		// stencil I think).
		
		return new Data[0];
	}

	// TODO: Make this a utility?
	private List<String> extractLineColumnNames(
			Dictionary<String, Object> parameters, Table table) {
		List<String> lineColumnNames = new ArrayList<String>();
		Schema schema = table.getSchema();

		for (int ii = 0; ii < schema.getColumnCount(); ii++) {
			String columnName = schema.getColumnName(ii);
			String keyName = BASE_LINE_COLUMN_NAME_KEY + columnName;
			Boolean value = (Boolean)parameters.get(keyName);
			
			if ((value != null) && value.booleanValue()) {
				lineColumnNames.add(columnName);
			}
		}
		
		return lineColumnNames;
	}
	
	private StencilData collectStencilData() throws AlgorithmExecutionException {
		// Get the Stencil script.

		//File stencilFile = getStencilFile();
		//String stencilScript = extractStencilScript(stencilFile);
		
		InputStream stencilInputStream = getStencilInputStream();
		String stencilScript = extractStencilScript(stencilInputStream);

		// Get the Stencil streams.

		List<TableStreamSource> streamSources = createStreamSources();

		// Return the Stencil script and streams together as "Stencil data".

		final StencilData stencilData = new StencilData(stencilScript, streamSources);

		return stencilData;
	}

	private StencilGUI createStencilGUI(StencilData stencilData)
			throws AlgorithmExecutionException {
		try {
			// TODO: "YOUR DATA"
			PropertiesSource configurationSource =
				new PropertiesSource.InputStreamPropertiesSource(
					loadStreamFromClassPath(LineGraphAlgorithm.class, STENCIL_CONFIGURATION_PATH));

			return new StencilGUI(
				configurationSource, stencilData, "Line Graph Visualization - YOUR DATA");
		} catch (StencilException stencilException) {
			throw new AlgorithmExecutionException(stencilException);
		}
	}

	private void showStencilGUI(final StencilGUI stencilGUI) throws AlgorithmExecutionException {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					try {
						stencilGUI.show();
					} catch (Exception e) {
						/*
						 * Wrap all Exceptions as RuntimeExceptions, and rethrow the inner
						 *  exception on the other side.
						 */
						throw new RuntimeException(e);
					}
				}
			});
		} catch (InvocationTargetException invocationTargetException) {
			// TODO: This may not be the behavior we want.
			throw new AlgorithmExecutionException(
				invocationTargetException.getMessage(), invocationTargetException);
		} catch (InterruptedException interruptedException) {
			throw new AlgorithmExecutionException(
				interruptedException.getMessage(), interruptedException);
		}
	}
	
	private void runStencilGUI(final StencilGUI stencilGUI) throws AlgorithmExecutionException {
		try {
			stencilGUI.run();
		} catch (StencilException stencilException) {
			throw new AlgorithmExecutionException(stencilException.getMessage(), stencilException);
		}
	}
	
	private String extractStencilScript(File stencilFile)
			throws AlgorithmExecutionException {
		try {
			return FileUtilities.readEntireTextFile(stencilFile);
		} catch (IOException ioException) {
			String exceptionMessage =
				"A problem occurred while trying to read the source Stencil file.";

			throw new AlgorithmExecutionException(exceptionMessage, ioException);
		}
	}

	private String extractStencilScript(InputStream stencilFileStream)
			throws AlgorithmExecutionException {
		try {
			return FileUtilities.readEntireInputStream(stencilFileStream);
		} catch (IOException ioException) {
			String exceptionMessage =
				"A problem occurred while trying to read the source Stencil file.";

			throw new AlgorithmExecutionException(exceptionMessage, ioException);
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

	public InputStream getStencilInputStream() throws AlgorithmExecutionException {
		return loadStreamFromClassPath(LineGraphAlgorithm.class, LINE_GRAPH_STENCIL_PATH);
	}

	public List<TableStreamSource> createStreamSources() {
		List<TableStreamSource> streams = new ArrayList<TableStreamSource>();

		for (String lineColumnName : this.lineColumnNames) {
			TableStreamSource stream = new TableStreamSource(
				this.inputTable,
				this.timeStepColumnName,
				lineColumnName,
				STENCIL_STREAM_NAME,
				STENCIL_TIMESTEP_NAME,
				STENCIL_LINE_NAME,
				STENCIL_VALUE_NAME);
			streams.add(stream);
		}

		/*TableStreamSource testStream1 = new TableStreamSource(
			LineGraphAlgorithm.this.inputTable,
			TEST_TIME_STEP_COLUMN_NAME,
			TEST_LINE_COLUMN_NAME_1,
			STENCIL_STREAM_NAME,
			STENCIL_TIMESTEP_NAME,
			STENCIL_LINE_NAME,
			STENCIL_VALUE_NAME);
		streams.add(testStream1);

		TableStreamSource testStream2 = new TableStreamSource(
			LineGraphAlgorithm.this.inputTable,
			TEST_TIME_STEP_COLUMN_NAME,
			TEST_LINE_COLUMN_NAME_2,
			STENCIL_STREAM_NAME,
			STENCIL_TIMESTEP_NAME,
			STENCIL_LINE_NAME,
			STENCIL_VALUE_NAME);
		streams.add(testStream2);

		TableStreamSource testStream3 = new TableStreamSource(
			LineGraphAlgorithm.this.inputTable,
			TEST_TIME_STEP_COLUMN_NAME,
			TEST_LINE_COLUMN_NAME_3,
			STENCIL_STREAM_NAME,
			STENCIL_TIMESTEP_NAME,
			STENCIL_LINE_NAME,
			STENCIL_VALUE_NAME);
		streams.add(testStream3);

		TableStreamSource testStream4 = new TableStreamSource(
			LineGraphAlgorithm.this.inputTable,
			TEST_TIME_STEP_COLUMN_NAME,
			TEST_LINE_COLUMN_NAME_4,
			STENCIL_STREAM_NAME,
			STENCIL_TIMESTEP_NAME,
			STENCIL_LINE_NAME,
			STENCIL_VALUE_NAME);
		streams.add(testStream4);*/

		return streams;
	}

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
		parameters.put(TIME_STEP_COLUMN_NAME_KEY, TEST_TIME_STEP_COLUMN_NAME);
		parameters.put(BASE_LINE_COLUMN_NAME_KEY + TEST_LINE_COLUMN_NAME_1, Boolean.TRUE);
		parameters.put(BASE_LINE_COLUMN_NAME_KEY + TEST_LINE_COLUMN_NAME_2, Boolean.TRUE);
		parameters.put(BASE_LINE_COLUMN_NAME_KEY + TEST_LINE_COLUMN_NAME_3, Boolean.TRUE);
		parameters.put(BASE_LINE_COLUMN_NAME_KEY + TEST_LINE_COLUMN_NAME_4, Boolean.TRUE);
		parameters.put(BASE_LINE_COLUMN_NAME_KEY + TEST_LINE_COLUMN_NAME_5, Boolean.FALSE);

		return parameters;
	}

	private static void runAlgorithmOnTestDataset1(
			AlgorithmFactory algorithmFactory,
			CIShellContext ciShellContext,
			Dictionary<String, Object> parameters)
			throws AlgorithmExecutionException, URISyntaxException {
		Data[] testDataset1 = createTestData(TEST_DATASET_1_PATH);
		Algorithm algorithm = algorithmFactory.createAlgorithm(
			testDataset1, parameters, ciShellContext);
		System.err.println("Executing algorithm on Test Dataset1...");
		algorithm.execute();
		System.err.println("...Done.");
	}

	// TODO: Make this a utility in edu.iu.cns.utilities.testing?
	private static Data[] createTestData(String testDataPath)
			throws AlgorithmExecutionException, URISyntaxException {
		File file = loadFileFromClassPath(LineGraphAlgorithm.class, testDataPath);
		Data fileData = new BasicData(file, CSV_MIME_TYPE);

		return convertFileDataToTableData(fileData);
	}

	// TODO: Make this a utility in edu.iu.cns.utilities.testing?
	private static Data[] convertFileDataToTableData(Data fileData)
			throws AlgorithmExecutionException {
		PrefuseCsvReader csvReader = new PrefuseCsvReader(new Data[] { fileData });

		return csvReader.execute();
	}

	private static File loadFileFromClassPath(Class clazz, String filePath)
			throws URISyntaxException {
		URL fileURL = clazz.getResource(filePath);
		System.err.println(fileURL.toURI());

		return new File(fileURL.toURI());
	}
	
	private static InputStream loadStreamFromClassPath(Class clazz, String filePath) {
		return clazz.getResourceAsStream(filePath);
	}
}