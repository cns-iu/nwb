package edu.iu.epic.visualization.linegraph;

import java.awt.Color;
import java.io.File;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.swing.JFrame;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.utilities.FileUtilities;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import prefuse.util.collections.IntIterator;
import stencil.adapters.java2D.Adapter;
import stencil.adapters.java2D.Panel;
import stencil.explore.PropertyManager;
import stencil.streams.Tuple;
import stencil.util.BasicTuple;
import edu.iu.cns.utilities.testing.LogOnlyCIShellContext;
import edu.iu.nwb.converter.prefusecsv.reader.PrefuseCsvReader;

public class LineGraphAlgorithm implements Algorithm {
	public static final String BASE_STENCIL_PATH =
		"/edu/iu/epic/visualization/linegraph/stencil/";
	public static final String LINE_GRAPH_STENCIL_PATH =
		BASE_STENCIL_PATH + "lineGraph.stencil";
	
	public static final String X_AXIS_NAME_KEY = "x_axis_name";
	public static final String Y_AXIS_NAME_KEY = "y_axis_name";
	
	// TODO: Change these to the real deal when the time is right.
	public static final String STENCIL_STREAM_NAME = "Stocks";
	public static final String STENCIL_X_AXIS_NAME = "Date";
	public static final String STENCIL_Y_AXIS_NAME = "Open";
	
	public static final String CSV_MIME_TYPE = "file:text/csv";
	
	public static final String TEST_DATA_PATH =
		"/edu/iu/epic/visualization/linegraph/testing/";
	public static final String TEST_DATASET_1_PATH =
		TEST_DATA_PATH + "TestDataset1.csv";

    private Data inputData;
    private Table inputTable;
    private String xAxisName;
    private String yAxisName;
    
    private LogService logger;
    
    public LineGraphAlgorithm(Data[] data,
    				  Dictionary parameters,
    				  CIShellContext ciShellContext) {
        this.inputData = data[0];
        this.inputTable = (Table)data[0].getData();
        
        this.xAxisName = (String)parameters.get(X_AXIS_NAME_KEY);
        this.yAxisName = (String)parameters.get(Y_AXIS_NAME_KEY);
        
        this.logger =
        	(LogService)ciShellContext.getService(LogService.class.getName());
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	PropertyManager.loadProperties(
    		new String[0], PropertyManager.stencilConfig);

    	File stencilProgramFile = null;
    	
    	try {
    		stencilProgramFile = FileUtilities.loadFileFromClassPath(
    			LineGraphAlgorithm.class, LINE_GRAPH_STENCIL_PATH);
    	} catch (URISyntaxException stencilProgramNotFoundException) {
    		// TODO: This message is not intended to not suck.  Rewrite.
    		String exceptionMessage =
    			"A serious error occurred when loading the Stencil to " +
    			"visualize your data.  Please send us your logs.";
    	}

    	try {
    		Panel stencilPanel = createStencilPanel(stencilProgramFile);
			JFrame frame = new JFrame();
			frame.setContentPane(stencilPanel);

			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.pack();
			frame.setSize(800, 600);
			frame.setBackground(Color.BLACK);
			stencilPanel.setSize(800, 600);
			stencilPanel.preRun();
			frame.setVisible(true);

			for (IntIterator rows = this.inputTable.rows(); rows.hasNext(); ) {
				int rowIndex = rows.nextInt();
				
				stencilPanel.processTuple(
					createTuple(this.inputTable, rowIndex));
			}
    	} catch (Exception stencilInterpreterCreationException) {
    		// stencilInterpreterCreationException.printStackTrace();
    		// TODO: Improve this exception message.
    		String exceptionMessage =
    			"A problem occurred when visualization your inputData.  " +
    			"Please be patient while this error message improves.";
    		
    		throw new AlgorithmExecutionException(
    			exceptionMessage, stencilInterpreterCreationException);
    	}
    	
        return new Data[0];
    }
    
    private static Panel createStencilPanel(
    		String stencilProgramString) throws Exception {
    	Adapter displayAdapter = Adapter.INSTANCE;
    	Panel panel = displayAdapter.compile(stencilProgramString);
    	
    	return panel;
    }
    
    private static Panel createStencilPanel(
    		File stencilProgramFile) throws Exception {
    	String stencilProgramString = FileUtilities.readEntireTextFile(
    		stencilProgramFile);
    	
    	return createStencilPanel(stencilProgramString);
    }
    
    // TODO: Better error handling of null/invalid values in the two columns?
    private Tuple createTuple(Table table, int rowIndex) {
    	prefuse.data.Tuple row = table.getTuple(rowIndex);

    	return new BasicTuple(
    		STENCIL_STREAM_NAME,
    		Arrays.asList(
    			new String[] { STENCIL_X_AXIS_NAME, STENCIL_Y_AXIS_NAME }),
    		Arrays.asList(new String[] {
    			row.get(this.xAxisName).toString(),
    			row.get(this.yAxisName).toString() }));
    }
    
    public static void main(String[] arguments) {
    	try {
    		AlgorithmFactory algorithmFactory =
    			new LineGraphAlgorithmFactory();
    		CIShellContext ciShellContext = new LogOnlyCIShellContext();
    		Dictionary<String, Object>  parameters = constructParameters();
    		
    		runAlgorithmOnTestDataset1(
    			algorithmFactory, ciShellContext, parameters);
    	} catch (Exception algorthmExecutionException) {
    		algorthmExecutionException.printStackTrace();
    	}
    }
    
    /*private static Tuple createRandomTuple(int index) {
    	return new BasicTuple(
    		"Stocks",
    		Arrays.asList(new String[] { "Date", "Open" }),
    		Arrays.asList(new String[] { "" + index, "" + Math.random() }));
    }*/
    
    private static Dictionary<String, Object> constructParameters() {
    	Dictionary<String, Object> parameters =
    		new Hashtable<String, Object>();
    	parameters.put(X_AXIS_NAME_KEY, "Date");
    	parameters.put(Y_AXIS_NAME_KEY, "Open");
    	
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
    	File file = FileUtilities.loadFileFromClassPath(
    		LineGraphAlgorithm.class, testDataPath);
    	Data fileData = new BasicData(file, CSV_MIME_TYPE);
    	
    	return convertFileDataToTableData(fileData);
    }

    // TODO: Make this a utility in edu.iu.cns.utilities.testing?
    private static Data[] convertFileDataToTableData(Data fileData)
    		throws AlgorithmExecutionException {
    	PrefuseCsvReader csvReader =
    		new PrefuseCsvReader(new Data[] { fileData });
    	
    	return csvReader.execute();
    }
}