package edu.iu.scipolicy.visualization.scimap.fields;

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
import org.cishell.utilities.NumberUtilities;
import org.cishell.utilities.StringUtilities;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import prefuse.data.Tuple;
import edu.iu.nwb.converter.prefusecsv.reader.PrefuseCsvReader;
import edu.iu.scipolicy.visualization.scimap.fields.testing.LogOnlyCIShellContext;

public class ScienceMapAlgorithm implements Algorithm {
	public static final int MIN_UCSD_AREA = 1;
	public static final int MAX_UCSD_AREA = 554;
	
	public static final String POSTSCRIPT_MIME_TYPE = "file:text/ps";
	public static final String CSV_MIME_TYPE = "file:text/csv";

	public static StringTemplateGroup group = loadTemplates();
	public static final String STRING_TEMPLATE_PATH =
		"/edu/iu/scipolicy/visualization/scimap/fields/templates/group.st";
	
	private static StringTemplateGroup loadTemplates() {
		return new StringTemplateGroup(
				new InputStreamReader(
					ScienceMapAlgorithm.class.getResourceAsStream(STRING_TEMPLATE_PATH)));
	}

	private Data[] data;
	private Table table;
	private String inDataLabel;
	private String nodeIDColumnName;
	private String nodeLabelColumnName;
	private String nodeValueColumnName;
	private String dataDisplayName;
	private LogService logger;		


	public ScienceMapAlgorithm(
			Data[] data, Dictionary<String, Object> parameters, CIShellContext context) {
		this.data = data;
		
		this.table = (Table) data[0].getData();

		this.inDataLabel =
			(String) data[0].getMetadata().get(DataProperty.LABEL);
		
		this.nodeIDColumnName =
			(String) parameters.get(
					ScienceMapAlgorithmFactory.NODE_ID_COLUMN_NAME_ID);
		this.nodeLabelColumnName =
			(String) parameters.get(
					ScienceMapAlgorithmFactory.NODE_LABEL_COLUMN_NAME_ID);
		this.nodeValueColumnName =
			(String) parameters.get(
					ScienceMapAlgorithmFactory.NODE_VALUE_COLUMN_NAME_ID);
		this.dataDisplayName =
			(String) parameters.get(ScienceMapAlgorithmFactory.DATA_DISPLAY_NAME_ID);
		
		this.logger = (LogService) context.getService(LogService.class.getName());
	}

	
	@SuppressWarnings("unchecked") // Raw Iterator from table.tuples()
	public Data[] execute() throws AlgorithmExecutionException {
//		Map<String, Integer> journalCounts = new HashMap<String, Integer>();
//		for (Iterator<Tuple> rows = table.tuples(); rows.hasNext();) {
//			Tuple row = rows.next();
//
//			if (row.canGetString(journalColumnName)) {
//				String journalName = row.getString(journalColumnName);
//				
//				incrementHitCount(journalCounts, journalName);
//			} else {
//				String message =
//					"Could not read value in column " + journalColumnName + ".";
//				throw new AlgorithmExecutionException(message);
//			}
//		}
		Map<Integer, Integer> ucsdAreaTotals = new HashMap<Integer, Integer>();
		Map<Integer, String> ucsdAreaLabels = new HashMap<Integer, String>();
		int goodRecordCount = 0;
		int unclassifiedRecordCount = 0;
		int noValueCount = 0;
		int badUCSDAreaCount = 0;
		Map<String, Integer> unclassifiedLabelCounts = new HashMap<String, Integer>();
		for (Iterator<Tuple> rows = table.tuples(); rows.hasNext();) {
			Tuple row = rows.next();
			
			int value = 0;
			try {
				value = NumberUtilities.interpretObjectAsDouble(row.get(nodeValueColumnName)).intValue();
			} catch (NumberFormatException e) {
				value = 0;
				noValueCount++;
			}
			
			if (value < 0) {
				noValueCount++;
				continue;
			}
			
			String label =
				StringUtilities.interpretObjectAsString(row.get(nodeLabelColumnName));						
			
			try {
				int ucsdArea = NumberUtilities.interpretObjectAsDouble(row.get(nodeIDColumnName)).intValue();
				
				if ((label == null) || (label.trim().length() == 0)) {
					label = "Area " + String.valueOf(ucsdArea);
				}
				
				String oldLabel = "";
				if (ucsdAreaLabels.containsKey(ucsdArea)) {
					oldLabel = ucsdAreaLabels.get(ucsdArea).trim() + "; ";
				}
				
				ucsdAreaLabels.put(ucsdArea, oldLabel + label);
				
				if (MIN_UCSD_AREA <= ucsdArea && ucsdArea <= MAX_UCSD_AREA) {
					int oldValue = 0;
					if (ucsdAreaTotals.containsKey(ucsdArea)) {
						oldValue = ucsdAreaTotals.get(ucsdArea);
					}
					
					ucsdAreaTotals.put(ucsdArea, oldValue + value);

					goodRecordCount++;
				} else {
					badUCSDAreaCount++;
				}
			} catch (NumberFormatException e) {
				if ((label == null) || (label.trim().length() == 0)) {
					label = "Unidentified Area";
				}
				
				int oldValue = 0;
				if (ucsdAreaTotals.containsKey(label)) {
					oldValue = unclassifiedLabelCounts.get(label);
				}
				
				unclassifiedLabelCounts.put(label, oldValue + value);
				
				unclassifiedRecordCount++;
			}
		}
		
		if (badUCSDAreaCount > 0) {
			logger.log(
					LogService.LOG_WARNING,
					noValueCount + " records specified an invalid UCSD area and were skipped.");
		}
		
		if (noValueCount > 0) {
			logger.log(
					LogService.LOG_WARNING,
					noValueCount + " records specified no valid value and were treated as specifying zero.");
		}
		
//		final double requestedMaxValue = 20.0;
//		final double valueScalar = requestedMaxValue / maxValue;
		
//		for (Entry<Integer, Double> nodeTotal : nodeTotals.entrySet()) {
//			nodeTotal.setValue(valueScalar * nodeTotal.getValue());
//		}
		
		
		

//		Analysis analysis =
//			new Analysis(journalCounts, dataDisplayName, inDataLabel);
//		MapOfScience map = new MapOfScience(nodeTotals);
		Analysis analysis =
			new Analysis(ucsdAreaTotals, ucsdAreaLabels, unclassifiedLabelCounts, dataDisplayName, inDataLabel, goodRecordCount, unclassifiedRecordCount, nodeValueColumnName);

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
	
//	private static <K> void incrementHitCount(
//			Map<K, Integer> counts, K hitKey) {
//		/* Lazily initialize the counts
//		 * (on the first occurrence of the journal name,
//		 * set the count to 1).
//		 */
//		if (counts.containsKey(hitKey)) {
//			counts.put(hitKey, counts.get(hitKey) + 1);
//		} else {
//			counts.put(hitKey, 1);
//		}
//	}

	@SuppressWarnings("unchecked") // Raw Dictionary
	private BasicData wrapPostScript(File postScript, String inFileName) {
		BasicData postScriptData =
			new BasicData(postScript, POSTSCRIPT_MIME_TYPE);
		Dictionary<String, Object> metadata = postScriptData.getMetadata();

		metadata.put(DataProperty.LABEL,
				"FieldsScienceMap_" + FileUtilities.extractFileName(inFileName) + ".ps");
		metadata.put(DataProperty.TYPE, DataProperty.VECTOR_IMAGE_TYPE);
		metadata.put(DataProperty.PARENT, data[0]);

		return postScriptData;
	}

	public static void main(String[] args) {
		try {
			File inFile = new File("C:\\Documents and Settings\\jrbibers\\Desktop\\nih demo\\scienceMap\\USDA-mapping-fake-values.csv");
			Data data = new BasicData(inFile, CSV_MIME_TYPE);

			PrefuseCsvReader prefuseCSVReader =
				new PrefuseCsvReader(
						new Data[]{ data });
			Data[] convertedData = prefuseCSVReader.execute();

			Dictionary<String, Object> parameters = new Hashtable<String, Object>();
			parameters.put(
				ScienceMapAlgorithmFactory.NODE_ID_COLUMN_NAME_ID, "UCSD Map Field Name");
			parameters.put(
				ScienceMapAlgorithmFactory.NODE_LABEL_COLUMN_NAME_ID, "Knowledge Areas");
			parameters.put(
				ScienceMapAlgorithmFactory.NODE_VALUE_COLUMN_NAME_ID, "Value");
			parameters.put(
				ScienceMapAlgorithmFactory.DATA_DISPLAY_NAME_ID, "Knowledge Areas");

			AlgorithmFactory algorithmFactory = new ScienceMapAlgorithmFactory();
			CIShellContext ciContext = new LogOnlyCIShellContext();
			Algorithm algorithm = algorithmFactory.createAlgorithm(
				convertedData, parameters, ciContext);

			System.out.println("Executing.. ");
			Data[] psFileData = algorithm.execute();
			System.out.println(".. Done.");			
			
			File psFile = (File) psFileData[0].getData();
			System.out.println(psFile.getAbsolutePath());
			
//			Desktop desktop = Desktop.getDesktop(); // TODO Remove and put back to Java 5.
//			desktop.open(psFile);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}

		System.exit(0);
	}
}