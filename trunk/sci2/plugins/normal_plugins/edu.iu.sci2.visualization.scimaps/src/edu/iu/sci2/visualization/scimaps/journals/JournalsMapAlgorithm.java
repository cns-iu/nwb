package edu.iu.sci2.visualization.scimaps.journals;

import static edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState.inch;

import java.awt.Dimension;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import oim.vivo.scimapcore.journal.Category;
import oim.vivo.scimapcore.journal.Journal;

import org.apache.xmlgraphics.java2d.GraphicContext;
import org.apache.xmlgraphics.java2d.ps.PSDocumentGraphics2D;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import prefuse.data.Tuple;

import com.google.common.collect.ImmutableSet;

import edu.iu.sci2.visualization.scimaps.MapOfScience;
import edu.iu.sci2.visualization.scimaps.rendering.print2012.Print2012;
import edu.iu.sci2.visualization.scimaps.rendering.web2012.Web2012;
import edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState;
import edu.iu.sci2.visualization.scimaps.tempvis.RenderableVisualization;
import edu.iu.sci2.visualization.scimaps.tempvis.VisualizationRunner;

public class JournalsMapAlgorithm implements Algorithm {
	public static final String OUT_FREQUENCY_COLUMN_NAME = "Frequency";
	public static final String OUT_JOURNAL_COLUMN_NAME = "Journal name";
	public static final String POSTSCRIPT_MIME_TYPE = "file:text/ps";
	public static final String CSV_MIME_TYPE = "file:text/csv";

	private Data parentData;
	private Table table;
	private String journalColumnName;
	private float scalingFactor;
	private String dataDisplayName;
	boolean webVersion;
	private boolean showWindow;
	private LogService logger;

	public JournalsMapAlgorithm(Data[] data, String journalColumnName,
			float scalingFactor, String dataDisplayName, boolean webVersion, boolean showWindow, LogService logger) {
		this.parentData = data[0];
		this.table = (Table) data[0].getData();
		this.journalColumnName = journalColumnName;
		this.scalingFactor = scalingFactor;
		this.dataDisplayName = dataDisplayName;
		this.webVersion = webVersion;
		this.showWindow = showWindow;
		this.logger = logger;
	}

	public Data[] execute() throws AlgorithmExecutionException {
		Map<String, Integer> journalNameAndHitCount = getJournalNameAndHitCount(
				this.table, this.journalColumnName);
		RenderableVisualization visualization = null;
		MapOfScience mapOfScience = new MapOfScience(journalNameAndHitCount);

		if (this.webVersion) {
			// Webversion
			Dimension dimensions = new Dimension(1280, 960);
			Web2012 web2012 = new Web2012(mapOfScience, dimensions,
					this.scalingFactor);
			visualization = web2012.getVisualization();
		} else {
			// Printversion
			Dimension dimensions = new Dimension((int) inch(11f),
					(int) inch(8.5f));
			Print2012 print2012 = new Print2012(mapOfScience, this.dataDisplayName,
					dimensions, this.scalingFactor);
			visualization = print2012.getVisualization();
		}
		if (this.showWindow) {
			VisualizationRunner visualizationRunner = new VisualizationRunner(
					visualization);
			// TODO: Do setUp() and run() ever actually need to be separate
			// methods?
			visualizationRunner.setUp();
			visualizationRunner.run();
		}
		return datafy(mapOfScience, visualization, this.parentData, this.logger);
	}

	private static Map<String, Integer> getJournalNameAndHitCount(
			Table myTable, String myJournalColumnName)
			throws AlgorithmExecutionException {
		Map<String, Integer> journalCounts = new HashMap<String, Integer>();

		for (Iterator<Tuple> rows = myTable.tuples(); rows.hasNext();) {
			Tuple row = rows.next();

			if (row.canGetString(myJournalColumnName)) {
				String journalName = row.getString(myJournalColumnName);
				incrementHitCount(journalCounts, journalName);
			} else {
				String message = "Error reading table: Could not read value in column "
						+ myJournalColumnName
						+ " for row number"
						+ row.getRow() + ".";
				throw new AlgorithmExecutionException(message);
			}
		}
		return journalCounts;
	}

	/**
	 * @param counts
	 *            is side-effected
	 */
	private static <K> void incrementHitCount(Map<K, Integer> counts, K hitKey) {
		if (counts.containsKey(hitKey)) {
			counts.put(hitKey, Integer.valueOf(counts.get(hitKey).intValue() + 1));
		} else {
			counts.put(hitKey, Integer.valueOf(1));
		}
	}

	public static double calculateListFontSize(Collection<Category> categories,
			int numOfJournals) {
		return Math.min(8, 260 / (numOfJournals + categories.size() * 1.5 + 1));
	}

	private static Data[] datafy(MapOfScience mapOfScience, RenderableVisualization visualization, Data parentData, LogService logger) {

		Set<Journal> foundJournals = mapOfScience.getMappedJournals();
		Set<Journal> unfoundJournals = mapOfScience.getUnmappedJournals();

		Table foundTable = makeJournalFrequencyTable(ImmutableSet
				.copyOf(foundJournals));
		Data foundData = datafy(foundTable, "Journals located", parentData);
		Table unfoundTable = makeJournalFrequencyTable(ImmutableSet
				.copyOf(unfoundJournals));
		Data unfoundData = datafy(unfoundTable, "Journals not located",
				parentData);
		try {
			Data visualizationData = createData(visualization, parentData);
			return new Data[] { foundData, unfoundData, visualizationData };
		} catch (DataCreationException e) {
			logger.log(LogService.LOG_ERROR, e.getMessage());
		}
		
		return new Data[] { foundData, unfoundData};
	}

	private static Data createData(RenderableVisualization visualization, Data parentData) throws DataCreationException {
		try {
		File outFile = File.createTempFile("Visualization-", ".ps");
		OutputStream out = new FileOutputStream(outFile);
		PSDocumentGraphics2D g2d = new PSDocumentGraphics2D(false);
		g2d.setGraphicContext(new GraphicContext());
			g2d.setupDocument(out, Double.valueOf(visualization.getDimension().getWidth()).intValue(), Double.valueOf(visualization.getDimension().getHeight()).intValue());
		g2d.setClip(0, 0, Double.valueOf(visualization.getDimension().getWidth()).intValue(), Double.valueOf(visualization.getDimension().getHeight()).intValue());
		visualization.render(new GraphicsState(g2d), visualization.getDimension());
		g2d.finish();
		out.close();
		
		Data outData = new BasicData(outFile, "file:text/ps");
		Dictionary<String, Object> metadata = outData.getMetadata();
		metadata.put(DataProperty.LABEL, "Scimaps Visualization");
		metadata.put(DataProperty.TYPE, DataProperty.VECTOR_IMAGE_TYPE);
		metadata.put(DataProperty.PARENT, parentData);
		
		return outData;
		} catch (IOException e) {
			throw new DataCreationException("There was a problem creating the postscript file", e);
		}
	}

	public static class DataCreationException extends Exception {
		/**
		 * 
		 */
		private static final long serialVersionUID = 3544771808601245002L;

		public DataCreationException(String message) {
			super(message);
		}

		public DataCreationException(String string, Exception e) {
			super(string, e);
		}
	}

	public static Data datafy(Table table, String label, Data parentData) {
		Data tableData = new BasicData(table, table.getClass().getName());
		tableData.getMetadata().put(DataProperty.LABEL, label);
		tableData.getMetadata().put(DataProperty.TYPE, DataProperty.TABLE_TYPE);
		tableData.getMetadata().put(DataProperty.PARENT, parentData);

		return tableData;
	}

	public static Table makeJournalFrequencyTable(Set<Journal> journals) {
		Table table = new Table();
		table.addColumn(OUT_JOURNAL_COLUMN_NAME, String.class);
		table.addColumn(OUT_FREQUENCY_COLUMN_NAME, int.class);

		for (Journal journal : journals) {
			int row = table.addRow();
			table.setString(row, OUT_JOURNAL_COLUMN_NAME,
					journal.getJournalName());
			table.setInt(row, OUT_FREQUENCY_COLUMN_NAME,
					(int) journal.getJournalHitCount());
		}

		return table;
	}

}