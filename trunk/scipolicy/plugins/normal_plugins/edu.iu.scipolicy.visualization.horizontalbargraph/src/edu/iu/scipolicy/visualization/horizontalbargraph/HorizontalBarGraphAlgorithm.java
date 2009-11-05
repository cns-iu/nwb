package edu.iu.scipolicy.visualization.horizontalbargraph;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Dictionary;
import java.util.Hashtable;

import org.antlr.stringtemplate.StringTemplateGroup;
import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.utilities.DateUtilities;
import org.cishell.utilities.FileUtilities;
import org.joda.time.DateTime;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import edu.iu.cns.utilities.testing.LogOnlyCIShellContext;
import edu.iu.nwb.converter.prefusecsv.reader.PrefuseCsvReader;
import edu.iu.scipolicy.visualization.horizontalbargraph.layout.BasicLayout;
import edu.iu.scipolicy.visualization.horizontalbargraph.record.RecordCollection;
import edu.iu.scipolicy.visualization.horizontalbargraph.record.TableRecordExtractor;

public class HorizontalBarGraphAlgorithm implements Algorithm {
	/* TODO: Make and test edge case datasets.
	 * No non-zero amounts, no valid amounts, no items with non-zero duration,
	 *  you get the idea.
	 */
	
	public static final String LABEL_FIELD_ID = "label";
	public static final String START_DATE_FIELD_ID = "start_date";
	public static final String END_DATE_FIELD_ID = "end_date";
	public static final String SIZE_BY_FIELD_ID = "size_by";
	public static final String DATE_FORMAT_FIELD_ID = "date_format";

	public static final String POST_SCRIPT_MIME_TYPE = "file:text/ps";
	public static final String EPS_FILE_EXTENSION = "eps";
	
	public static final String CSV_MIME_TYPE = "file:text/csv";
	public static final String TEST_DATA_PATH =
		"/edu/iu/scipolicy/visualization/horizontalbargraph/testing/";
	public static final String CNS_TEST_DATA_PATH = TEST_DATA_PATH + "CNS.csv";
	public static final String CORNELL_TEST_DATA_PATH =
		TEST_DATA_PATH + "Cornell.csv";
	public static final String INDIANA_TEST_DATA_PATH =
		TEST_DATA_PATH + "Indiana.csv";
	public static final String MICHIGAN_TEST_DATA_PATH =
		TEST_DATA_PATH + "Michigan.csv";

	
	public static final String STRING_TEMPLATE_BASE_FILE_PATH =
		"/edu/iu/scipolicy/visualization/horizontalbargraph/stringtemplates/";
	public static final String STRING_TEMPLATE_FILE_PATH =
		STRING_TEMPLATE_BASE_FILE_PATH + "horizontal_bar_graph.st";
	public static final StringTemplateGroup horizontalBarGraphGroup =
		loadTemplates();
	
    private Data inputData;
    private Table inputTable;
    private String labelKey;
    private String startDateKey;
    private String endDateKey;
    private String amountKey;
    private String startDateFormat;
    private String endDateFormat;
    
    private LogService logger;
    
    public HorizontalBarGraphAlgorithm(
    		Data[] data,
    		Dictionary<String, Object> parameters,
    		CIShellContext ciShellContext) {
        this.inputData = data[0];
        this.inputTable = (Table)data[0].getData();
        
        this.labelKey = (String)parameters.get(LABEL_FIELD_ID);
        this.startDateKey = (String)parameters.get(START_DATE_FIELD_ID);
        this.endDateKey = (String)parameters.get(END_DATE_FIELD_ID);
        this.amountKey = (String)parameters.get(SIZE_BY_FIELD_ID);
        this.startDateFormat = (String)parameters.get(DATE_FORMAT_FIELD_ID);
        this.endDateFormat = (String)parameters.get(DATE_FORMAT_FIELD_ID);
        
        this.logger =
        	(LogService)ciShellContext.getService(LogService.class.getName());
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	/*
    	 *	for each dateset specified:
    	 *		extract the records
    	 *			fix dates, trunctate text, other preprocessing things...
    	 *			keep track of minimum amount/unit of time; note that things
    	 *			 missing a start or end date will have updating "heights"
    	 *			 as new data comes in
    	 *			keep track of number of items (or just store in a container
    	 *			 that knows its size)
    	 *			keep track of total amount per unit of time
    	 *			keep track of earliest start date and latest end date
    	 *
    	 *	all of the above is being calculated by
    	 *	 TableRecordExtractor and RecordCollection.
    	 *	It can be asked for a SortedSet of items (probably have it return
    	 *	 Collection, for flexibility)
    	 *	It can be asked for the minimum amount/unit of time, # items, etc
    	 *
    	 *	Then there are VisualItems; make a visual item factory, set the
    	 *	 scaling factor, which is the minimum amount/unit of time
    	 *	For determining horizontal coordinates, interpolate (start and end)
    	 *	 dates given the earliest start date and latest end date
    	 *	
    	 *	calculate the scale (and rotation) to apply by calculating
    	 *	 the total height and total width, and using the rules as talked
    	 *	 about before.
    	 *	Apply the scale and rotation by emitting them to the postscript
    	 *	
    	 *	draw the axes; translate to the starting point for the first item
    	 *	have the visual item draw itself. translate up. have the next draw
    	 *	 itself. et cetera. done.
    	 */
    
    	TableRecordExtractor extractor = new TableRecordExtractor(this.logger);
    
    	RecordCollection recordCollection = extractor.extractRecords(
    		this.inputTable,
    		this.labelKey,
    		this.startDateKey,
    		this.endDateKey,
    		this.amountKey,
    		this.startDateFormat,
    		this.endDateFormat);
    	
    	DateTime startDate = recordCollection.getMinimumStartDate();
    	DateTime endDate = recordCollection.getMaximumEndDate();

    	double minimumAmountPerUnitOfTime =
    		recordCollection.calculateMinimumAmountPerUnitOfTime(
    			UnitOfTime.YEARS);
    	BasicLayout layout = new BasicLayout(
    		startDate,
    		endDate,
    		minimumAmountPerUnitOfTime);
    	PostScriptCreator postScriptCreator = new PostScriptCreator(
    		horizontalBarGraphGroup,
    		layout,
    		(String)this.inputData.getMetadata().get(DataProperty.LABEL),
    		recordCollection);
    	String postScript = postScriptCreator.toString();
    	
    	// System.err.println(postScript);
    	
        File temporaryPostScriptFile =
    		writePostScriptCodeToTemporaryFile(
	    		postScript, "horizontal-line-graph");
		
		return formOutData(temporaryPostScriptFile, inputData);
    }
    
    private static StringTemplateGroup loadTemplates() {
    	return new StringTemplateGroup(
    		new InputStreamReader(
    			HorizontalBarGraphAlgorithm.class.getResourceAsStream(
    				STRING_TEMPLATE_FILE_PATH)));
    }
    
    private File writePostScriptCodeToTemporaryFile(
    		String postScriptCode, String temporaryFileName)
    		throws AlgorithmExecutionException {
    	File temporaryPostScriptFile = null;
    	
    	try {
    		temporaryPostScriptFile =
    			FileUtilities.createTemporaryFileInDefaultTemporaryDirectory(
    				temporaryFileName, EPS_FILE_EXTENSION);
    	} catch (IOException postScriptFileCreationException) {
    		String exceptionMessage =
    			"Error creating temporary PostScript file.";
    		
    		throw new AlgorithmExecutionException(
    			exceptionMessage, postScriptFileCreationException);
    	}
    	
    	// TODO: Make variable names shorter?
    	
		try {		
			FileWriter temporaryPostScriptFileWriter =
				new FileWriter(temporaryPostScriptFile);
			
			temporaryPostScriptFileWriter.write(postScriptCode);
			temporaryPostScriptFileWriter.flush();
			temporaryPostScriptFileWriter.close();
		}
		catch (IOException postScriptFileWritingException) {
			String exceptionMessage =
				"Error writing PostScript out to temporary file";
			
			throw new AlgorithmExecutionException(
				exceptionMessage, postScriptFileWritingException);
		}
		
		return temporaryPostScriptFile;
    }
    
    private Data[] formOutData(File postScriptFile, Data singleInData) {
    	Dictionary inMetaData = singleInData.getMetadata();
    	
		Data postScriptData =
			new BasicData(postScriptFile, POST_SCRIPT_MIME_TYPE);

		Dictionary postScriptMetaData = postScriptData.getMetadata();

		postScriptMetaData.put(
			DataProperty.LABEL,
			"PostScript: " + inMetaData.get(DataProperty.LABEL));
		postScriptMetaData.put(DataProperty.PARENT, singleInData);
		postScriptMetaData.put(
			DataProperty.TYPE, DataProperty.VECTOR_IMAGE_TYPE);
    	
        return new Data[] { postScriptData };
    }
    
    public static void main(String[] arguments) {
    	try {
    		AlgorithmFactory algorithmFactory =
    			new HorizontalBarGraphAlgorithmFactory();
    		CIShellContext ciShellContext = new LogOnlyCIShellContext();
    		Dictionary<String, Object> parameters = constructParameters();
    		
    		/*runAlgorithmOnCNS(algorithmFactory, ciShellContext, parameters);
    		
    		runAlgorithmOnCornell(
    			algorithmFactory, ciShellContext, parameters);
    		
    		runAlgorithmOnIndiana(
    			algorithmFactory, ciShellContext, parameters);
    		
    		/*runAlgorithmOnMichigan(
    			algorithmFactory, ciShellContext, parameters);*/
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    private static Dictionary<String, Object> constructParameters() {
    	Dictionary<String, Object> parameters =
    		new Hashtable<String, Object>();
    	parameters.put(LABEL_FIELD_ID, "Title");
    	parameters.put(START_DATE_FIELD_ID, "Start Date");
    	parameters.put(END_DATE_FIELD_ID, "Expiration Date");
    	parameters.put(SIZE_BY_FIELD_ID, "Awarded Amount to Date");
    	parameters.put(
    		DATE_FORMAT_FIELD_ID, DateUtilities.MONTH_DAY_YEAR_DATE_FORMAT);
    	
    	return parameters;
    }
    
    private static void runAlgorithmOnCNS(
    		AlgorithmFactory algorithmFactory,
    		CIShellContext ciShellContext,
    		Dictionary<String, Object> parameters)
    		throws AlgorithmExecutionException, URISyntaxException {
    	Data[] cnsTestData = createTestData(CNS_TEST_DATA_PATH);
    	Algorithm algorithm = algorithmFactory.createAlgorithm(
    		cnsTestData, parameters, ciShellContext);
    	//System.out.println("Executing algorithm on CNS...");
    	algorithm.execute();
    	//System.out.println("...Done.");
    }
    
    private static void runAlgorithmOnCornell(
    		AlgorithmFactory algorithmFactory,
    		CIShellContext ciShellContext,
    		Dictionary<String, Object> parameters)
    		throws AlgorithmExecutionException, URISyntaxException {
    	Data[] cornellTestData = createTestData(CORNELL_TEST_DATA_PATH);
    	Algorithm algorithm = algorithmFactory.createAlgorithm(
    		cornellTestData, parameters, ciShellContext);
    	//System.out.println("Executing algorithm on Cornell...");
    	algorithm.execute();
    	//System.out.println("...Done.");
    }
    
    private static void runAlgorithmOnIndiana(
    		AlgorithmFactory algorithmFactory,
    		CIShellContext ciShellContext,
    		Dictionary<String, Object> parameters)
    		throws AlgorithmExecutionException, URISyntaxException {
    	Data[] indianaTestData = createTestData(INDIANA_TEST_DATA_PATH);
    	Algorithm algorithm = algorithmFactory.createAlgorithm(
    		indianaTestData, parameters, ciShellContext);
    	//System.out.println("Executing algorithm on Indiana...");
    	algorithm.execute();
    	//System.out.println("...Done.");
    }
    
    private static void runAlgorithmOnMichigan(
    		AlgorithmFactory algorithmFactory,
    		CIShellContext ciShellContext,
    		Dictionary<String, Object> parameters)
    		throws AlgorithmExecutionException, URISyntaxException {
    	Data[] michiganTestData = createTestData(MICHIGAN_TEST_DATA_PATH);
    	Algorithm algorithm = algorithmFactory.createAlgorithm(
    		michiganTestData, parameters, ciShellContext);
    	//System.out.println("Executing algorithm on Michigan...");
    	algorithm.execute();
    	//System.out.println("...Done.");
    }
    
    private static Data[] createTestData(String testDataPath)
    		throws AlgorithmExecutionException, URISyntaxException {
    	URL fileURL =
    		HorizontalBarGraphAlgorithm.class.getResource(testDataPath);
    	File file = new File(fileURL.toURI());
    	Data fileData = new BasicData(file, CSV_MIME_TYPE);
    	
    	return convertFileDataToTableData(fileData);
    }
    
    private static Data[] convertFileDataToTableData(Data fileData)
    		throws AlgorithmExecutionException {
    	PrefuseCsvReader csvReader =
    		new PrefuseCsvReader(new Data[] { fileData });
    	
    	return csvReader.execute();
    }
}