package edu.iu.scipolicy.visualization.scimap.journals;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.antlr.stringtemplate.StringTemplateGroup;
import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.utilities.FileUtilities;

import prefuse.data.Table;
import prefuse.data.Tuple;
import edu.iu.nwb.converter.prefusecsv.reader.PrefuseCsvReader;
import edu.iu.scipolicy.visualization.scimap.journals.testing.LogOnlyCIShellContext;

public class ScienceMapAlgorithm implements Algorithm {	
	public static final String POSTSCRIPT_MIME_TYPE = "file:text/ps";
	public static final String CSV_MIME_TYPE = "file:text/csv";

	public static StringTemplateGroup group = loadTemplates();
	public static final String STRING_TEMPLATE_PATH =
		"/edu/iu/scipolicy/visualization/scimap/journals/templates/group.st";
	private static StringTemplateGroup loadTemplates() {
		return new StringTemplateGroup(
				new InputStreamReader(
					ScienceMapAlgorithm.class.getResourceAsStream(STRING_TEMPLATE_PATH)));
	}

	private Data[] data;
	private Dictionary<String, Object> parameters;
	private Table table;
	private String inDataLabel;	


	public ScienceMapAlgorithm(
			Data[] data, Dictionary<String, Object> parameters) {
		this.data = data;
		this.parameters = parameters;
		
		this.table = (Table) data[0].getData();

		this.inDataLabel =
			(String) data[0].getMetadata().get(DataProperty.LABEL);
	}

	
	@SuppressWarnings("unchecked") // Raw Iterator from table.tuples()
	public Data[] execute() throws AlgorithmExecutionException {
		String journalColumnName =
			(String) parameters.get(
					ScienceMapAlgorithmFactory.JOURNAL_COLUMN_ID);

		Map<String, Integer> journalCounts = new HashMap<String, Integer>();
		for (Iterator<Tuple> rows = table.tuples(); rows.hasNext();) {
			Tuple row = rows.next();

			if (row.canGetString(journalColumnName)) {
				String journalName = row.getString(journalColumnName);
				
				incrementHitCount(journalCounts, journalName);
			} else {
				String message =
					"Could not read value in column " + journalColumnName + ".";
				throw new AlgorithmExecutionException(message);
			}
		}
		
		String dataDisplayName =
			(String) parameters.get(ScienceMapAlgorithmFactory.DATA_DISPLAY_NAME_ID);

		Analysis analysis =
			new Analysis(journalCounts, dataDisplayName, inDataLabel);

		File psFile;
		try {
			psFile =
				FileUtilities.createTemporaryFileInDefaultTemporaryDirectory(
						"scienceMap", "ps");
			
			Writer writer = new FileWriter(psFile);
			writer.write(analysis.getPostScript());
			writer.close();
			
			return new Data[]{ wrapPostScript(psFile, dataDisplayName) };
		} catch (IOException e) {
			String message =
				"Failed to create PostScript file: " + e.getMessage();
			throw new AlgorithmExecutionException(message, e);
		}
	}
	
	private static <K> void incrementHitCount(
			Map<K, Integer> counts, K hitKey) {
		/* Lazily initialize the counts
		 * (on the first occurrence of the journal name,
		 * set the count to 1).
		 */
		if (counts.containsKey(hitKey)) {
			counts.put(hitKey, counts.get(hitKey) + 1);
		} else {
			counts.put(hitKey, 1);
		}
	}

	@SuppressWarnings("unchecked") // Raw Dictionary
	private BasicData wrapPostScript(File postScript, String inFileName) {
		BasicData postScriptData =
			new BasicData(postScript, POSTSCRIPT_MIME_TYPE);
		Dictionary<String, Object> metadata = postScriptData.getMetadata();

		metadata.put(DataProperty.LABEL,
				"JournalsScienceMap_" + FileUtilities.extractFileName(inFileName) + ".ps");
		metadata.put(DataProperty.TYPE, DataProperty.VECTOR_IMAGE_TYPE);
		metadata.put(DataProperty.PARENT, data[0]);

		return postScriptData;
	}

	public static void main(String[] args) {
		try {
			File inFile = new File("C:\\Documents and Settings\\jrbibers\\Desktop\\nih demo\\scienceMap\\CTSA2005-2009.csv");
			Data data = new BasicData(inFile, CSV_MIME_TYPE);

			PrefuseCsvReader prefuseCSVReader =
				new PrefuseCsvReader(
						new Data[]{ data });
			Data[] convertedData = prefuseCSVReader.execute();

			Dictionary<String, Object> parameters =
				new Hashtable<String, Object>();
			parameters.put(ScienceMapAlgorithmFactory.JOURNAL_COLUMN_ID, "Journal Title Abbr");
			parameters.put(ScienceMapAlgorithmFactory.DATA_DISPLAY_NAME_ID, "CTSA, 2005-2009");

			AlgorithmFactory algorithmFactory =
				new ScienceMapAlgorithmFactory();
			CIShellContext ciContext = new LogOnlyCIShellContext();
			Algorithm algorithm =
				algorithmFactory.createAlgorithm(
						convertedData, parameters, ciContext);

			System.out.println("Executing.. ");
			algorithm.execute();
			System.out.println(".. Done.");
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}

		System.exit(0);
	}
}