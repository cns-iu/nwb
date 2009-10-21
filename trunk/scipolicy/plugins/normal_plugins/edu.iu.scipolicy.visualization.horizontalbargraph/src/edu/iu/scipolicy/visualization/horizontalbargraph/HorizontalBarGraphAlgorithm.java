package edu.iu.scipolicy.visualization.horizontalbargraph;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Dictionary;
import java.util.Hashtable;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.utilities.DateUtilities;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import edu.iu.nwb.converter.prefusecsv.reader.PrefuseCsvReader;
import edu.iu.scipolicy.visualization.horizontalbargraph.testing.LogOnlyCIShellContext;

public class HorizontalBarGraphAlgorithm implements Algorithm {
	public static final double DEFAULT_PAGE_WIDTH = 8.5;
	public static final double DEFAULT_PAGE_HEIGTH = 11.0;
	
	public static final String LABEL_FIELD_ID = "label";
	public static final String START_DATE_FIELD_ID = "start_date";
	public static final String END_DATE_FIELD_ID = "end_date";
	public static final String SIZE_BY_FIELD_ID = "size_by";
	public static final String UNIT_OF_TIME_FIELD_ID = "unit_of_time";
	public static final String MINIMUM_UNIT_OF_TIME_FIELD_ID =
		"minimum_unit_of_time";
	public static final String DATE_FORMAT_FIELD_ID = "date_format";
	public static final String PAGE_WIDTH_FIELD_ID = "page_width";
	public static final String PAGE_HEIGHT_FIELD_ID = "page_height";
	public static final String SHOULD_SCALE_OUTPUT_FIELD_ID =
		"should_scale_output";

	public static final String CSV_MIME_TYPE = "file:text/csv";
	public static final String TEST_DATA_PATH =
		"/edu/iu/scipolicy/visualization/horizontalbargraph/testing/";
	public static final String CORNELL_TEST_DATA_PATH =
		TEST_DATA_PATH + "Cornell.csv";
	public static final String INDIANA_TEST_DATA_PATH =
		TEST_DATA_PATH + "Indiana.csv";
	public static final String MICHIGAN_TEST_DATA_PATH =
		TEST_DATA_PATH + "Michigan.csv";
	
    private Data inputData;
    private Table inputTable;
    private String labelKey;
    private String startDateKey;
    private String endDateKey;
    private String amountKey;
    private UnitOfTime unitOfTime;
    private int minimumUnitOfTime;
    private String startDateFormat;
    private String endDateFormat;
    private double pageWidth = DEFAULT_PAGE_WIDTH;
    private double pageHeight = DEFAULT_PAGE_HEIGTH;
    private boolean shouldScaleOutput = false;
    
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
        this.unitOfTime = (UnitOfTime)parameters.get(UNIT_OF_TIME_FIELD_ID);
        this.minimumUnitOfTime = Math.max(
        	((Integer)parameters.get(
        		MINIMUM_UNIT_OF_TIME_FIELD_ID)).intValue(),
        	1);;
        this.startDateFormat = (String)parameters.get(DATE_FORMAT_FIELD_ID);
        this.endDateFormat = (String)parameters.get(DATE_FORMAT_FIELD_ID);
        this.pageWidth =
        	((Double)parameters.get(PAGE_WIDTH_FIELD_ID)).doubleValue();
        this.pageHeight =
        	((Double)parameters.get(PAGE_HEIGHT_FIELD_ID)).doubleValue();
        this.shouldScaleOutput =
        	((Boolean)parameters.get(SHOULD_SCALE_OUTPUT_FIELD_ID)).
        		booleanValue();
        
        this.logger =
        	(LogService)ciShellContext.getService(LogService.class.getName());
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	/*
    	 *	for each dateset specified:
    	 *		extract the records
    	 *			fix dates, trunctate text, other preprocessing things...
    	 *			keep track of minimum "height"; note that things missing a
    	 *			 start or end date will have updating "heights" as new data
    	 *			 comes in
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
    	 *	----
    	 *
    	 *	Then there are VisualItems; make a visual item factory, set the
    	 *	 scaling factor
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
    
    	TableRecordExtractor extractor = new TableRecordExtractor();
    
    	RecordCollection recordCollection = extractor.extractRecords(
    		this.inputTable,
    		this.labelKey,
    		this.startDateKey,
    		this.endDateKey,
    		this.amountKey,
    		this.startDateFormat,
    		this.endDateFormat,
    		this.logger);
    	
    	System.err.println(recordCollection.calculateMinimumAmountPerUnitOfTime(this.unitOfTime, this.minimumUnitOfTime));
    	System.err.println(recordCollection.calculateTotalAmountPerUnitOfTime(this.unitOfTime, this.minimumUnitOfTime));
    	System.err.println("earliest start date: " + recordCollection.getMinimumStartDate());
    	System.err.println("latest end date: " + recordCollection.getMaximumEndDate());
    	
        return new Data[0];
    }
    
    public static void main(String[] arguments) {
    	try {
    		AlgorithmFactory algorithmFactory =
    			new HorizontalBarGraphAlgorithmFactory();
    		CIShellContext ciShellContext = new LogOnlyCIShellContext();
    		Dictionary<String, Object> parameters = constructParameters();
    		
    		runAlgorithmOnCornell(
    			algorithmFactory, ciShellContext, parameters);
    		
    		runAlgorithmOnIndiana(
    			algorithmFactory, ciShellContext, parameters);
    		
    		runAlgorithmOnMichigan(
    			algorithmFactory, ciShellContext, parameters);
    		
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
    	parameters.put(UNIT_OF_TIME_FIELD_ID, UnitOfTime.YEARS);
    	parameters.put(MINIMUM_UNIT_OF_TIME_FIELD_ID, new Integer(1));
    	parameters.put(
    		DATE_FORMAT_FIELD_ID, DateUtilities.MONTH_DAY_YEAR_DATE_FORMAT);
    	parameters.put(PAGE_WIDTH_FIELD_ID, new Double(8.5));
    	parameters.put(PAGE_HEIGHT_FIELD_ID, new Double(11.0));
    	parameters.put(SHOULD_SCALE_OUTPUT_FIELD_ID, new Boolean(true));
    	
    	return parameters;
    }
    
    private static void runAlgorithmOnCornell(
    		AlgorithmFactory algorithmFactory,
    		CIShellContext ciShellContext,
    		Dictionary<String, Object> parameters)
    		throws AlgorithmExecutionException, URISyntaxException {
    	Data[] cornellTestData = createTestData(CORNELL_TEST_DATA_PATH);
    	Algorithm algorithm = algorithmFactory.createAlgorithm(
    		cornellTestData, parameters, ciShellContext);
    	System.out.println("Executing algorithm on Cornell...");
    	algorithm.execute();
    	System.out.println("...Done.");
    }
    
    private static void runAlgorithmOnIndiana(
    		AlgorithmFactory algorithmFactory,
    		CIShellContext ciShellContext,
    		Dictionary<String, Object> parameters)
    		throws AlgorithmExecutionException, URISyntaxException {
    	Data[] indianaTestData = createTestData(INDIANA_TEST_DATA_PATH);
    	Algorithm algorithm = algorithmFactory.createAlgorithm(
    		indianaTestData, parameters, ciShellContext);
    	System.out.println("Executing algorithm on Indiana...");
    	algorithm.execute();
    	System.out.println("...Done.");
    }
    
    private static void runAlgorithmOnMichigan(
    		AlgorithmFactory algorithmFactory,
    		CIShellContext ciShellContext,
    		Dictionary<String, Object> parameters)
    		throws AlgorithmExecutionException, URISyntaxException {
    	Data[] michiganTestData = createTestData(MICHIGAN_TEST_DATA_PATH);
    	Algorithm algorithm = algorithmFactory.createAlgorithm(
    		michiganTestData, parameters, ciShellContext);
    	System.out.println("Executing algorithm on Michigan...");
    	algorithm.execute();
    	System.out.println("...Done.");
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