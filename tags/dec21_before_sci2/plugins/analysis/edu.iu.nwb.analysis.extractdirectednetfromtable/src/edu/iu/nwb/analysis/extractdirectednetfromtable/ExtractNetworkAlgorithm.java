package edu.iu.nwb.analysis.extractdirectednetfromtable;

import java.util.Dictionary;
import java.util.Properties;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.ProgressMonitor;
import org.cishell.framework.algorithm.ProgressTrackable;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.utilities.BasicDataPlus;
import org.osgi.service.log.LogService;

import prefuse.data.Graph;
import prefuse.data.Table;
import edu.iu.nwb.analysis.extractnetfromtable.components.GraphContainer;
import edu.iu.nwb.analysis.extractnetfromtable.components.InvalidColumnNameException;
import edu.iu.nwb.analysis.extractnetfromtable.components.PropertyHandler;

public class ExtractNetworkAlgorithm implements Algorithm, ProgressTrackable {
	public static final String CSV_MIME_TYPE = "file:text/csv";

	private Data[] data;
	private boolean isBipartite;
	private String outDataLabel;

	private String sourceColumnName;
	private String targetColumnName;
	private String delimiter;
	private Object aggregationFunctionFilePath;
	private LogService logger;
	private ProgressMonitor progressMonitor;

	public ExtractNetworkAlgorithm(Data[] data, Dictionary parameters, CIShellContext context,
			boolean isBipartite, String outDataLabel) {
		this.data = data;
		this.isBipartite = isBipartite;
		this.outDataLabel = outDataLabel;

		this.sourceColumnName = (String) parameters
				.get(ExtractNetworkAlgorithmFactory.SOURCE_COLUMN_NAME_PARAMETER_ID);
		this.targetColumnName = (String) parameters
				.get(ExtractNetworkAlgorithmFactory.TARGET_COLUMN_NAME_PARAMETER_ID);
		this.delimiter = (String) parameters
				.get(ExtractNetworkAlgorithmFactory.DELIMITER_PARAMETER_ID);
		this.aggregationFunctionFilePath = parameters
				.get(ExtractNetworkAlgorithmFactory.AGGREGATION_FUNCTION_FILE_PARAMETER_ID);

		this.logger = (LogService) context.getService(LogService.class.getName());
	}

	public Data[] execute() throws AlgorithmExecutionException {
		final Table dataTable = (Table) data[0].getData();
		Properties functions = null;

		if (aggregationFunctionFilePath != null) {
			functions =
				PropertyHandler.getProperties(aggregationFunctionFilePath.toString(), logger);
		}

		try {
			GraphContainer graphContainer = GraphContainer.initializeGraph(
				dataTable,
				sourceColumnName,
				targetColumnName,
				true,
				functions,
				logger,
				progressMonitor);

			Graph network = graphContainer.buildGraph(
				sourceColumnName, targetColumnName, delimiter, isBipartite, logger);

			BasicDataPlus outData = new BasicDataPlus(network, data[0]);
			outData.markAsModified();
			outData.setType(DataProperty.NETWORK_TYPE);
			outData.setLabel(outDataLabel);

			return new Data[] { outData };
		} catch (InvalidColumnNameException e) {
			throw new AlgorithmExecutionException(e.getMessage(), e);
		}
	}

	public ProgressMonitor getProgressMonitor() {
		return progressMonitor;
	}

	public void setProgressMonitor(ProgressMonitor monitor) {
		this.progressMonitor = monitor;
	}

//	public static void main(String[] args) {
//		try {
//			File inFile = new File("C:\\Documents and Settings\\jrbibers\\Desktop\\extract bipartite network tests\\dead simple\\mode.csv");
////			File inFile = new File("C:\\Documents and Settings\\jrbibers\\Desktop\\extract bipartite network tests\\dead simple\\ab12pipe34foo.csv");
////			File inFile = new File(
////					"C:\\Documents and Settings\\jrbibers\\Desktop\\extract bipartite network tests\\BethPlale-cleaned.csv");
//
//			Dictionary parameters = new Hashtable();
//			 parameters.put("sourceColumn", "Column A");
//			 parameters.put("targetColumn", "Column B");
////			parameters.put("sourceColumn", "Principal Investigator");
////			parameters.put("targetColumn", "Co-PI Name(s)");
//			parameters.put("delimiter", "|");
////			parameters.put("aff", "C:\\Documents and Settings\\jrbibers\\Desktop\\extract bipartite network tests\\dead simple\\ab123foosumWithEdges.properties");
//			parameters.put("aff", "C:\\Documents and Settings\\jrbibers\\Desktop\\extract bipartite network tests\\dead simple\\mode.properties");
////			parameters
////					.put(
////							"aff",
////							"C:\\Documents and Settings\\jrbibers\\Desktop\\extract bipartite network tests\\awarded-amount-sum.properties");
//
//			AlgorithmFactory algorithmFactory =
//				new ExtractBipartiteNetworkAlgorithmFactory();
////				new ExtractDirectedNetworkAlgorithmFactory();
//
//			Data data = new BasicData(inFile, CSV_MIME_TYPE);
//
//			PrefuseCsvReader prefuseCSVReader = new PrefuseCsvReader(new Data[] { data });
//			Data[] convertedData = prefuseCSVReader.execute();
//
//			CIShellContext ciContext = new CIShellContext() {
//				public Object getService(String service) {
//					return new LogService() {
//						public void log(int level, String message) {
//							System.err.println("[Level " + level + "]: " + message);
//						}
//
//						public void log(ServiceReference sr, int level, String message) {
//							log(level, message);
//						}
//
//						public void log(int level, String message, Throwable cause) {
//							log(level, message + " from cause " + cause.getMessage());
//						}
//
//						public void log(ServiceReference sr, int level, String message,
//								Throwable cause) {
//							log(level, message, cause);
//						}
//					};
//				}
//			};
//
//			Algorithm algorithm = algorithmFactory.createAlgorithm(convertedData, parameters,
//					ciContext);
//
//			System.out.println("Executing.. ");
//			Data[] outData = algorithm.execute();
//			// Graph outGraph = (Graph) outData[0].getData();
//
//			System.out.println("Output file contents =============");
//			PrefuseGraphMLWriter graphWriter = new PrefuseGraphMLWriter(new Data[] { outData[0] });
//			File outFile = (File) (graphWriter.execute())[0].getData();
//
//			BufferedReader reader = new BufferedReader(new FileReader(outFile));
//			String line;
//			while (null != (line = reader.readLine())) {
//				System.out.println(line);
//			}
//			System.out.println("==================================");
//
//			System.out.println(".. Done.");
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.exit(-1);
//		}
//
//		System.exit(0);
//	}
}