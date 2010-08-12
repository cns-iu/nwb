package edu.iu.scipolicy.visualization.horizontalbargraph;

import java.io.File;
import java.io.InputStreamReader;
import java.util.Dictionary;

import org.antlr.stringtemplate.StringTemplateGroup;
import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.utilities.FileUtilities;
import org.joda.time.DateTime;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import edu.iu.cns.visualization.exception.VisualizationExportException;
import edu.iu.scipolicy.visualization.horizontalbargraph.layout.BasicLayout;
import edu.iu.scipolicy.visualization.horizontalbargraph.record.RecordCollection;
import edu.iu.scipolicy.visualization.horizontalbargraph.record.TableRecordExtractor;
import edu.iu.scipolicy.visualization.horizontalbargraph.record.exception.BadDatasetException;
import edu.iu.scipolicy.visualization.horizontalbargraph.utility.PreprocessedRecordInformation;
import edu.iu.scipolicy.visualization.horizontalbargraph.visualizationgeneration.generator.PostScriptGenerator;
import edu.iu.scipolicy.visualization.horizontalbargraph.visualizationgeneration.visualization.HorizontalBarGraphVisualization;

public class HorizontalBarGraphAlgorithm implements Algorithm {
	public static final String LABEL_FIELD_ID = "label";
	public static final String START_DATE_FIELD_ID = "start_date";
	public static final String END_DATE_FIELD_ID = "end_date";
	public static final String SIZE_BY_FIELD_ID = "size_by";
	public static final String MINIMUM_AMOUNT_PER_DAY_FOR_SCALING_FIELD_ID =
		"minimum_amount_per_day_for_scaling";
	public static final String SCALING_FUNCTION_FIELD_ID = "bar_scaling";
	public static final String DATE_FORMAT_FIELD_ID = "date_format";
	public static final String YEAR_LABEL_FONT_SIZE_FIELD_ID = "year_label_font_size";
	public static final String BAR_LABEL_FONT_SIZE_FIELD_ID = "bar_label_font_size";
	public static final String SCALE_TO_FIT_PAGE_ID = "scale_to_fit_page";
	public static final String COLORIZED_BY_FIELD_ID = "colorized_by";
	
	public static final String NO_COLORIZED_BY = "No Coloring";

	public static final String POST_SCRIPT_MIME_TYPE = "file:text/ps";
	public static final String EPS_FILE_EXTENSION = "eps";
	
	public static final String CSV_MIME_TYPE = "file:text/csv";
	public static final String TEST_DATA_PATH =
		"/edu/iu/scipolicy/visualization/horizontalbargraph/testing/";
	public static final String CNS_TEST_DATA_PATH = TEST_DATA_PATH + "CNS.csv";
	public static final String CORNELL_TEST_DATA_PATH = TEST_DATA_PATH + "Cornell.csv";
	public static final String INDIANA_TEST_DATA_PATH = TEST_DATA_PATH + "Indiana.csv";
	public static final String MICHIGAN_TEST_DATA_PATH = TEST_DATA_PATH + "Michigan.csv";

	
	public static final String STRING_TEMPLATE_BASE_FILE_PATH =
		"/edu/iu/scipolicy/visualization/horizontalbargraph/stringtemplates/";
	public static final String STRING_TEMPLATE_FILE_PATH =
		STRING_TEMPLATE_BASE_FILE_PATH + "horizontal_bar_graph.st";
	public static final StringTemplateGroup horizontalBarGraphGroup = loadTemplates();
	
    private Data inputData;
    private Table inputTable;
    private Metadata metadata;
    
    private LogService logger;
    
    public HorizontalBarGraphAlgorithm(
    		Data[] data, Dictionary<String, Object> parameters, CIShellContext ciShellContext) {
        this.inputData = data[0];
        this.inputTable = (Table)data[0].getData();
        this.metadata = new Metadata(this.inputData, parameters);
        this.logger = (LogService)ciShellContext.getService(LogService.class.getName());
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

    	try {
    		PreprocessedRecordInformation recordInformation =
    			new PreprocessedRecordInformation(this.inputTable, metadata, logger);

    		TableRecordExtractor extractor = new TableRecordExtractor(this.logger);

    		RecordCollection recordCollection = extractor.extractRecords(
    			recordInformation, this.inputTable, this.metadata, this.logger);

    		DateTime startDate = recordCollection.getMinimumDate();
    		DateTime endDate = recordCollection.getMaximumDate();

    		double minimumAmountPerUnitOfTime =
    			recordCollection.calculateMinimumAmountPerUnitOfTime(UnitOfTime.YEARS);
    		BasicLayout layout = new BasicLayout(
    			this.metadata.scaleToFitPage(),
    			startDate,
    			endDate,
    			minimumAmountPerUnitOfTime,
    			this.metadata.getYearLabelFontSize(),
    			this.metadata.getBarLabelFontSize());
    		PostScriptGenerator generator = new PostScriptGenerator(
    			horizontalBarGraphGroup, layout, this.metadata, recordCollection);
    		HorizontalBarGraphVisualization visualization = generator.generateVisualization();

//    		BoundingBox boundingBox = layout.calculateBoundingBox(visualization.getBars());
//    		int width = (int)boundingBox.getWidth();
//    		int height = (int)boundingBox.getHeight();
//    		AWTVisualizationRunner visualizationRunner = new AWTVisualizationRunner(
//    			visualization, new Dimension(width, height));
//    		visualizationRunner.setUp();
//    		visualizationRunner.run();
    		
    		try {
    			File temporaryPostScriptFile =
    				visualization.export(EPS_FILE_EXTENSION, "horizontal-bar-graph");
//    		String postScript = generator.toString();
//
//        	File temporaryPostScriptFile =
//        		writePostScriptCodeToTemporaryFile(postScript, "horizontal-bar-graph");

				return formOutData(temporaryPostScriptFile, inputData);
    		} catch (VisualizationExportException e) {
    			String exceptionMessage =
    				"An error occurred when trying to generate your visualization.  " +
    				" Please submit this entire message to the Help Desk: \"" +
    				e.getMessage() + "\"";

    			throw new AlgorithmExecutionException(exceptionMessage, e);
    		}
//    	} catch (AlgorithmExecutionException e) {
//    		throw e;
    	} catch (BadDatasetException e) {
    		throw new AlgorithmExecutionException(e.getMessage(), e);
    	}/* catch (Exception e) {
    		String exceptionMessage =
    			"An error occurred when trying to generate your visualization.  " +
    			" Please submit this entire message to the Help Desk: \"" + e.getMessage() + "\"";
    		throw new AlgorithmExecutionException(exceptionMessage, e);
    	}*/
    }
    
    private static StringTemplateGroup loadTemplates() {
    	return new StringTemplateGroup(new InputStreamReader(
    			HorizontalBarGraphAlgorithm.class.getResourceAsStream(STRING_TEMPLATE_FILE_PATH)));
    }
    
//    private File writePostScriptCodeToTemporaryFile(
//    		String postScriptCode, String temporaryFileName) throws AlgorithmExecutionException {
//    	File temporaryPostScriptFile = null;
//    	
//    	try {
//    		temporaryPostScriptFile = FileUtilities.createTemporaryFileInDefaultTemporaryDirectory(
//    			temporaryFileName, EPS_FILE_EXTENSION);
//    	} catch (IOException postScriptFileCreationException) {
//    		String exceptionMessage = "Error creating temporary PostScript file.";
//    		
//    		throw new AlgorithmExecutionException(
//    			exceptionMessage, postScriptFileCreationException);
//    	}
//    	
//		try {		
//			FileWriter temporaryPostScriptFileWriter = new FileWriter(temporaryPostScriptFile);
//			
//			temporaryPostScriptFileWriter.write(postScriptCode);
//			temporaryPostScriptFileWriter.flush();
//			temporaryPostScriptFileWriter.close();
//		}
//		catch (IOException postScriptFileWritingException) {
//			String exceptionMessage = "Error writing PostScript out to temporary file";
//			
//			throw new AlgorithmExecutionException(
//				exceptionMessage, postScriptFileWritingException);
//		}
//		
//		return temporaryPostScriptFile;
//    }
    
    @SuppressWarnings("unchecked")
    private Data[] formOutData(File postScriptFile, Data singleInData) {
    	Dictionary<String, Object> inMetaData = singleInData.getMetadata();
    	
		Data postScriptData = new BasicData(postScriptFile, POST_SCRIPT_MIME_TYPE);

		Dictionary<String, Object> postScriptMetaData = postScriptData.getMetadata();

		String label =
			"HorizontalBarGraph_" +
			FileUtilities.extractFileName(inMetaData.get(DataProperty.LABEL).toString()) +
			".ps";
		postScriptMetaData.put(DataProperty.LABEL, label);
		postScriptMetaData.put(DataProperty.PARENT, singleInData);
		postScriptMetaData.put(DataProperty.TYPE, DataProperty.VECTOR_IMAGE_TYPE);
    	
        return new Data[] { postScriptData };
    }
}