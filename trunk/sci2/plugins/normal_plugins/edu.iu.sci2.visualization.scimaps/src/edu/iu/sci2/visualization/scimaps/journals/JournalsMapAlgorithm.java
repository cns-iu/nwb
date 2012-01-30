package edu.iu.sci2.visualization.scimaps.journals;

import static edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState.inch;

import java.awt.Dimension;
import java.io.File;
import java.util.Collection;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import oim.vivo.scimapcore.journal.Category;
import oim.vivo.scimapcore.journal.Journal;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;

import prefuse.data.Table;
import prefuse.data.Tuple;

import com.google.common.collect.ImmutableSet;

import edu.iu.nwb.converter.prefusecsv.reader.PrefuseCsvReader;
import edu.iu.sci2.visualization.scimaps.MapOfScience;
import edu.iu.sci2.visualization.scimaps.rendering.print2012.Print2012;
import edu.iu.sci2.visualization.scimaps.rendering.web2012.Web2012;
import edu.iu.sci2.visualization.scimaps.tempvis.VisualizationRunner;
import edu.iu.sci2.visualization.scimaps.testing.LogOnlyCIShellContext;

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
	private String inDataLabel;
	Boolean webVersion;

	public JournalsMapAlgorithm(Data[] data, String journalColumnName,
			float scalingFactor, String dataDisplayName, Boolean webVersion) {
		this.parentData = data[0];
		this.table = (Table) data[0].getData();
		this.inDataLabel = (String) data[0].getMetadata().get(
				DataProperty.LABEL);
		this.journalColumnName = journalColumnName;
		this.scalingFactor = scalingFactor;
		this.dataDisplayName = dataDisplayName;
		this.webVersion = webVersion;
	}

	public Data[] execute() throws AlgorithmExecutionException {
		Map<String, Integer> journalNameAndHitCount = getJournalNameAndHitCount(
				table, journalColumnName);
		if (webVersion) {
			// Webversion
			MapOfScience mapOfScience = new MapOfScience(journalNameAndHitCount);
			Dimension dimensions = new Dimension((int) 1280, (int) 960);

			Web2012 web2012 = new Web2012(mapOfScience, dimensions,
					scalingFactor);

			VisualizationRunner visualizationRunner = new VisualizationRunner(
					web2012.getVisualization());

			visualizationRunner.setUp();
			visualizationRunner.run();
			return datafy(mapOfScience, parentData);

		} else {
			// Printversion
			MapOfScience mapOfScience = new MapOfScience(journalNameAndHitCount);
			Dimension dimensions = new Dimension((int) inch(11f),
					(int) inch(8.5f));

			Print2012 print2012 = new Print2012(mapOfScience, dataDisplayName,
					dimensions, scalingFactor);

			VisualizationRunner visualizationRunner = new VisualizationRunner(
					print2012.getVisualization());

			// TODO: Do setUp() and run() ever actually need to be separate
			// methods?
			visualizationRunner.setUp();
			visualizationRunner.run();
			return datafy(mapOfScience, parentData);
		}
			
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
			counts.put(hitKey, counts.get(hitKey) + 1);
		} else {
			counts.put(hitKey, 1);
		}
	}

	public static double calculateListFontSize(Collection<Category> categories,
			int numOfJournals) {
		return Math.min(8, 260 / (numOfJournals + categories.size() * 1.5 + 1));
	}

	private static Data[] datafy(MapOfScience mapOfScience, Data parentData) {

		Set<Journal> foundJournals = mapOfScience.getMappedJournals();
		Set<Journal> unfoundJournals = mapOfScience.getUnmappedJournals();

		Table foundTable = makeJournalFrequencyTable(ImmutableSet
				.copyOf(foundJournals));
		Data foundData = datafy(foundTable, "Journals located", parentData);
		Table unfoundTable = makeJournalFrequencyTable(ImmutableSet
				.copyOf(unfoundJournals));
		Data unfoundData = datafy(unfoundTable, "Journals not located",
				parentData);

		return new Data[] { foundData, unfoundData };
	}

	public static <K> Data datafy(Table table, String label, Data parentData) {
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