package edu.iu.epic.visualization.linegraph;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.SwingUtilities;

import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.Data;
import org.cishell.utilities.FileUtilities;

import prefuse.data.Table;
import edu.iu.epic.visualization.linegraph.core.StencilGUI;
import edu.iu.epic.visualization.linegraph.stencil.hack.PropertiesSource;
import edu.iu.epic.visualization.linegraph.utilities.StencilData;
import edu.iu.epic.visualization.linegraph.utilities.StencilException;
import edu.iu.epic.visualization.linegraph.utilities.TableStreamSource;

public class LineGraphAlgorithm implements Algorithm {
	public static final String WINDOW_TITLE = "Line Graph Visualization";
	
	public static final String STENCIL_STREAM_NAME = "Data";
	public static final String STENCIL_TIMESTEP_NAME = "Timestep";
	public static final String STENCIL_LINE_NAME = "Line";
	public static final String STENCIL_VALUE_NAME = "Value";

	public static final String BASE_STENCIL_PATH = "/edu/iu/epic/visualization/linegraph/stencil/";
	public static final String LINE_GRAPH_STENCIL_PATH = BASE_STENCIL_PATH + "lineGraph.stencil";
	public static final String STENCIL_CONFIGURATION_PATH =
		BASE_STENCIL_PATH + "Stencil.properties";

	public static final String CSV_MIME_TYPE = "file:text/csv";

	private String title;
	private Table inputTable;
	private String timeStepColumnName;
	private Collection<String> lineColumnNames;
	private ActiveAlgorithmHook activeAlgorithmHook;
	private StencilGUI stencilGUI;
	private Integer dummy = new Integer(0);

	public LineGraphAlgorithm(
			Table inputTable,
			String title,
			String timeStepColumnName,
			Collection<String> lineColumnNames,
			ActiveAlgorithmHook activeAlgorithmHook) {
		this.title = title;
		this.inputTable = inputTable;
		this.timeStepColumnName = timeStepColumnName;
		this.lineColumnNames = lineColumnNames;
		this.activeAlgorithmHook = activeAlgorithmHook;
	}

	public String getTitle() {
		return this.title;
	}

	public void addDataToGraph(
			String title,
			Table inputTable,
			String timeStepColumnName,
			Collection<String> lineColumnNames) throws AlgorithmExecutionException {
		try {
			while (this.stencilGUI == null) {
				this.dummy.wait();
			}
		} catch (InterruptedException e) {
			/* TODO: Should this be thrown as a new AlgorithmExecutionException? */
		}

		StencilData stencilDatum =
			collectStencilData(title, inputTable, timeStepColumnName, lineColumnNames);
		this.stencilGUI.addStencilDataToGraph(title, stencilDatum);
		
		/*
		 * To show the stencil gui in which the data is being added.
		 * */
		showStencilGUI();

		runStencilGUI();
	}

	public Data[] execute() throws AlgorithmExecutionException {
		this.activeAlgorithmHook.nowActive(this);

		// Get the Stencil script.
		InputStream stencilInputStream = getStencilInputStream();
		String stencilScript = extractStencilScript(stencilInputStream);

		StencilData initialData = collectStencilData(
			this.title, this.inputTable, this.timeStepColumnName, this.lineColumnNames);

		synchronized (this.dummy) {
			this.stencilGUI = createStencilGUI(stencilScript, initialData);
		}

		showStencilGUI();

		runStencilGUI();

		/* TODO: Make Export parameters work (will need Joseph Cottam to update stencil
		 * I think).
		 */

		return new Data[0];
	}
	
	private StencilData collectStencilData(
			String title,
			Table inputTable,
			String timeStepColumnName,
			Collection<String> lineColumnNames)
			throws AlgorithmExecutionException {
		// Get the Stencil streams.

		List<TableStreamSource> streamSources =
			createStreamSources(title, inputTable, timeStepColumnName, lineColumnNames);

		// Return the Stencil streams together as "Stencil data".

		final StencilData stencilDatum = new StencilData(streamSources);

		return stencilDatum;
	}

	private StencilGUI createStencilGUI(final String stencilScript, final StencilData initialData)
			throws AlgorithmExecutionException {
		try {
			final PropertiesSource configurationSource =
				new PropertiesSource.InputStreamPropertiesSource(
					loadStreamFromClassPath(LineGraphAlgorithm.class, STENCIL_CONFIGURATION_PATH));
			final Exception[] exceptionThrown = new Exception[1];
			final StencilGUI[] stencilGUI = new StencilGUI[1];
			
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					try {
						stencilGUI[0] = new StencilGUI(
							configurationSource,
							stencilScript,
							initialData,
							LineGraphAlgorithm.this.title,
							LineGraphAlgorithm.this,
							LineGraphAlgorithm.this.activeAlgorithmHook);
					} catch (StencilException stencilException) {
						exceptionThrown[0] = stencilException;
					}
				}
			});
			
			if (exceptionThrown[0] != null) {
				throw new AlgorithmExecutionException(
					exceptionThrown[0].getMessage(), exceptionThrown[0]);
			}

			return stencilGUI[0];
		} catch (InterruptedException stencilGUICreationInterruptedException) {
			throw new AlgorithmExecutionException(
				stencilGUICreationInterruptedException.getMessage(),
				stencilGUICreationInterruptedException);
		} catch (InvocationTargetException stencilGUICreationInvocationTargetException) {
			throw new AlgorithmExecutionException(
				stencilGUICreationInvocationTargetException.getMessage(),
				stencilGUICreationInvocationTargetException);
		}
	}

	private void showStencilGUI() throws AlgorithmExecutionException {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					try {
						LineGraphAlgorithm.this.stencilGUI.show();
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
	
	private void runStencilGUI() throws AlgorithmExecutionException {
		try {
			this.stencilGUI.run();
		} catch (StencilException stencilException) {
			throw new AlgorithmExecutionException(stencilException.getMessage(), stencilException);
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
			return loadFileFromClassPath(LineGraphAlgorithm.class, LINE_GRAPH_STENCIL_PATH);
		} catch (URISyntaxException e) {
			String message = String.format(
				"Unable to load Stencil file '%s'.  Cannot complete operation.",
				LINE_GRAPH_STENCIL_PATH);

			throw new AlgorithmExecutionException(message, e);
		}
	}

	public InputStream getStencilInputStream() throws AlgorithmExecutionException {
		return loadStreamFromClassPath(LineGraphAlgorithm.class, LINE_GRAPH_STENCIL_PATH);
	}

	public List<TableStreamSource> createStreamSources(
			String title,
			Table inputTable,
			String timeStepColumnName,
			Collection<String> lineColumnNames) {
		List<TableStreamSource> streams = new ArrayList<TableStreamSource>();

		for (String lineColumnName : lineColumnNames) {
			TableStreamSource stream = new TableStreamSource(
				title,
				inputTable,
				timeStepColumnName,
				lineColumnName,
				STENCIL_STREAM_NAME,
				STENCIL_TIMESTEP_NAME,
				STENCIL_LINE_NAME,
				STENCIL_VALUE_NAME);
			streams.add(stream);
		}

		return streams;
	}
	
	private static File loadFileFromClassPath(Class<LineGraphAlgorithm> clazz, String filePath)
			throws URISyntaxException {
		URL fileURL = clazz.getResource(filePath);

		return new File(fileURL.toURI());
	}

	private static InputStream loadStreamFromClassPath(
			Class<LineGraphAlgorithm> clazz, String filePath) {
		return clazz.getResourceAsStream(filePath);
	}
}