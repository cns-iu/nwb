package edu.iu.epic.visualization.linegraph;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
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
import stencil.explore.PropertyManager;
import stencil.streams.Tuple;
import stencil.util.BasicTuple;
import edu.iu.cns.utilities.testing.LogOnlyCIShellContext;
import edu.iu.epic.visualization.linegraph.utilities.StencilRunner;
import edu.iu.epic.visualization.linegraph.utilities.StencilRunnerCreationException;
import edu.iu.nwb.converter.prefusecsv.reader.PrefuseCsvReader;

public class LineGraphAlgorithm implements Algorithm {
	public static final String STENCIL_STREAM_NAME = "Stocks";
	public static final String STENCIL_X_AXIS_NAME = "Date";
	public static final String STENCIL_Y_AXIS_NAME = "Open";
	
	public static final String BASE_STENCIL_PATH =
		"/edu/iu/epic/visualization/linegraph/stencil/";
	public static final String LINE_GRAPH_STENCIL_PATH =
		BASE_STENCIL_PATH + "lineGraph.stencil";
	
	public static final String X_AXIS_NAME_KEY = "x_axis_name";
	public static final String Y_AXIS_NAME_KEY = "y_axis_name";
	
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
    	// TODO: Stencil loader/runner/etc?
    	PropertyManager.loadProperties(
    		new String[0], PropertyManager.stencilConfig);

    	File stencilProgramFile = null;
    	
    	try {
    		stencilProgramFile = loadFileFromClassPath(
    			LineGraphAlgorithm.class, LINE_GRAPH_STENCIL_PATH);
    	} catch (URISyntaxException stencilProgramNotFoundException) {
    		// TODO: This message is not intended to not suck.  Rewrite.
    		String exceptionMessage =
    			"A serious error occurred when loading the Stencil to " +
    			"visualize your data.  Please send us your logs.";
    	}
    	
    	// TODO: Solve the javaw.exe hanging problem?

    	try {
    		/*CrappyStencilRunner stencilRunner = CrappyStencilRunner.createStencilRunner(
    			stencilProgramFile,
    			this.xAxisName,
    			this.yAxisName,
    			this.inputTable);
    		Display display = stencilRunner.getDisplay();
    		Shell shell = stencilRunner.getShell();

			stencilRunner.runStencil();
			
			shell.open();
			
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
			
			display.dispose();*/

			/*Adapter displayAdapter = Adapter.INSTANCE;
			//Panel stencilPanel = createStencilPanel(stencilProgramFile);
			Panel stencilPanel = displayAdapter.compile(
				FileUtilities.readEntireTextFile(stencilProgramFile));
			JFrame frame = new JFrame();
			//frame.add(stencilPanel);
			Container contentPane = frame.getContentPane();
			java.awt.Panel userControlPanel = new java.awt.Panel();
			JSplitPane splitPane = new JSplitPane(
				JSplitPane.HORIZONTAL_SPLIT, userControlPanel, stencilPanel);
			contentPane.add(splitPane);

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
			}*/
    	
    		StencilRunner stencilRunner =
    			StencilRunner.createStencilRunner(stencilProgramFile);
    		JFrame frame = stencilRunner.createFrame();
    		// TODO: TupleStream?
    		
    		for (IntIterator rows = this.inputTable.rows(); rows.hasNext(); ) {
				int rowIndex = rows.nextInt();
				
				stencilRunner.processStencilTuple(
					createTuple(this.inputTable, rowIndex));
			}
    	} catch (StencilRunnerCreationException
    				stencilRunnerCreationException) {
    		// TODO: Improve this exception message.
    		String exceptionMessage =
    			"A problem occurred when visualization your data.  " +
    			"Please be patient while this error message improves.";
    		
    		throw new AlgorithmExecutionException(
    			exceptionMessage, stencilRunnerCreationException);
    	} catch (Exception exception) {
    		System.err.println(exception.getMessage());
    		exception.printStackTrace();
    	}
    	
        return new Data[0];
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
    
    private static Dictionary<String, Object> constructParameters() {
    	Dictionary<String, Object> parameters =
    		new Hashtable<String, Object>();
    	parameters.put(X_AXIS_NAME_KEY, STENCIL_X_AXIS_NAME);
    	parameters.put(Y_AXIS_NAME_KEY, STENCIL_Y_AXIS_NAME);
    	
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
    	File file = loadFileFromClassPath(
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
    
    private static File loadFileFromClassPath(Class clazz, String filePath)
    		throws URISyntaxException {
    	URL fileURL = clazz.getResource(filePath);
    	
    	return new File(fileURL.toURI());
    }
}