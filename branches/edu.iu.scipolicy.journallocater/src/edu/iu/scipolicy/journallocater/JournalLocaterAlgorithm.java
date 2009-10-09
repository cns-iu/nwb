package edu.iu.scipolicy.journallocater;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.antlr.stringtemplate.StringTemplateGroup;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.utilities.FileUtilities;

import prefuse.data.Table;
import prefuse.data.Tuple;

public class JournalLocaterAlgorithm implements Algorithm {
	public static final String POSTSCRIPT_MIME_TYPE = "file:text/ps";

	public static final String CSV_MIME_TYPE = "file:text/csv";

	public static final String FILE_PATH_SEPARATOR =
		System.getProperty("file.separator");

	public static StringTemplateGroup group = loadTemplates();

	private Data[] data;
	private Table table;
	private String inDataLabel;
	private Dictionary<String, Object> parameters;
	private String guessedInFilename;


	public JournalLocaterAlgorithm(Data[] data, Dictionary<String, Object> parameters) {
		this.parameters = parameters;

		this.data = data;
		this.table = (Table) data[0].getData();

		this.inDataLabel =
			(String) data[0].getMetadata().get(DataProperty.LABEL);

		this.guessedInFilename = guessFileNameFromDataLabel(inDataLabel);
	}

	private String guessFileNameFromDataLabel(String inDataLabel) {
		String fileSeparator = FILE_PATH_SEPARATOR;

		// If it's a single backslash, escape it (again) for regex.
		if ("\\".equals(fileSeparator)) {
			fileSeparator = "\\\\";
		}

		return grabFinalElement(inDataLabel.split(fileSeparator));
	}

	private static <T> T grabFinalElement(T[] array) {
		if (array.length > 1) {
			return array[array.length - 1];
		}

		return null;
	}

	private static StringTemplateGroup loadTemplates() {
		return new StringTemplateGroup(
				new InputStreamReader(
						JournalLocaterAlgorithm.class.getResourceAsStream(
						"/edu/iu/scipolicy/journallocater/templates/group.st")));
	}

	@SuppressWarnings("unchecked") // Raw Iterator from table.tuples()
	public Data[] execute() throws AlgorithmExecutionException {
		String journalColumnName =
			(String) parameters.get(
					JournalLocaterAlgorithmFactory.JOURNAL_COLUMN_ID);

		Map<String, Integer> journalCounts = new HashMap<String, Integer>();
		for (Iterator<Tuple> tuples = table.tuples(); tuples.hasNext();) {
			Tuple tuple = tuples.next();

			if (tuple.canGetString(journalColumnName)) {
				String journalName = tuple.getString(journalColumnName);

				if (journalCounts.containsKey(journalName)) {
					journalCounts.put(journalName, journalCounts.get(journalName) + 1);
				} else {
					journalCounts.put(journalName, 1);
				}
			} else {
				String message =
					"Could not read value in column " + journalColumnName + ".";
				throw new AlgorithmExecutionException(message);
			}
		}

		Analysis analysis =
			new Analysis(journalCounts, guessedInFilename, inDataLabel);

		File psFile;
		try {
			psFile =
				FileUtilities.createTemporaryFileInDefaultTemporaryDirectory(
						"journalLocater", "ps");
			
			Writer writer = new FileWriter(psFile);
			writer.write(analysis.getPostScript());
			writer.flush();
			writer.close();
			
			return new Data[]{ wrapPostScript(psFile, guessedInFilename) };
		} catch (IOException e) {
			String message =
				"Failed to create PostScript file: " + e.getMessage();
			throw new AlgorithmExecutionException(message, e);
		}
	}

	@SuppressWarnings("unchecked") // Raw Dictionary
	private BasicData wrapPostScript(File postScript, String inFileName) {
		BasicData postScriptData =
			new BasicData(postScript, POSTSCRIPT_MIME_TYPE);
		Dictionary<String, Object> metadata = postScriptData.getMetadata();

		metadata.put(DataProperty.LABEL,
				"PostScript: Journal references in " + inFileName);
		metadata.put(DataProperty.TYPE, DataProperty.VECTOR_IMAGE_TYPE);
		metadata.put(DataProperty.PARENT, data[0]);

		return postScriptData;
	}

//	public static void main(String[] args) {
////		File outFile = null;
//
//		try {
//			File inFile = new File("C:\\Documents and Settings\\jrbibers\\Desktop\\NIH-MIDAS_publications.csv");
//			Data data = new BasicData(inFile, CSV_MIME_TYPE);
//
//			CIShellContext ciContext = createDummyCIShellContext();
//			
//			PrefuseCsvReader prefuseCSVReader =
//				new PrefuseCsvReader(
//						new Data[]{ data },
//						new Hashtable<Object, Object>(),
//						ciContext);
//			Data[] convertedData = prefuseCSVReader.execute();
//
//			Dictionary<Object, Object> parameters =
//				new Hashtable<Object, Object>();
//			parameters.put("journalColumn", "Journal Title Abbr");
//
//			AlgorithmFactory algorithmFactory =
//				new JournalLocaterAlgorithmFactory();
//			Algorithm algorithm =
//				algorithmFactory.createAlgorithm(
//						convertedData, parameters, ciContext);
//
//			System.out.println("Executing.. ");
//			/*Data[] outData = */algorithm.execute();
//			//			outFile = (File) outData[0].getData();
//			System.out.println(".. Done.");
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.exit(-1);
//		}
//
//		//		try {
//		//			System.out.println("Opening output..");
//		//			Desktop.getDesktop().open(outFile);
//		//			System.out.println(".. Done.");
//		//		} catch (IOException e) {
//		//			e.printStackTrace();
//		//			System.exit(-1);
//		//		}
//
//		System.exit(0);
//	}
//	private static CIShellContext createDummyCIShellContext() {
//		return new CIShellContext() {
//			public Object getService(String service) {
//				return new LogService() {
//					public void log(int level, String message) {
//						System.err.println(message);
//					}
//	
//					public void log(int level, String message,
//							Throwable exception) {
//						log(level, message);
//					}
//	
//					public void log(ServiceReference sr, int level,
//							String message) {
//						log(level, message);
//					}
//	
//					public void log(ServiceReference sr, int level,
//							String message, Throwable exception) {
//						log(level, message);
//					}
//				};
//			}
//		};
//	}
}