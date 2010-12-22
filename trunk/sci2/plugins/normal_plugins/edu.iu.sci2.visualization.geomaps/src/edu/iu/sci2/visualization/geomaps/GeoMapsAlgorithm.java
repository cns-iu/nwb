package edu.iu.sci2.visualization.geomaps;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
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
import org.cishell.utilities.FileUtilities;
import org.opengis.referencing.operation.TransformException;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import edu.iu.nwb.converter.prefusecsv.reader.PrefuseCsvReader;
import edu.iu.sci2.visualization.geomaps.testing.LogOnlyCIShellContext;
import edu.iu.sci2.visualization.geomaps.utility.Constants;

public class GeoMapsAlgorithm implements Algorithm {
	public static final String CSV_MIME_TYPE = "file:text/csv";
	public static final String POSTSCRIPT_MIME_TYPE = "file:text/ps";

	public static StringTemplateGroup group = loadTemplates();
	public static final String STRING_TEMPLATE_FILE_PATH =		 
		"/edu/iu/sci2/visualization/geomaps/stringtemplates/group.st";
	
	public static final String SHAPEFILE_ID = "shapefile";	
	public static final String PROJECTION_ID = "projection";
	public static final String AUTHOR_NAME_ID = "authorName";

	public static final String TEST_DATUM_PATH =
		"/edu/iu/sci2/visualization/geomaps/testing/25mostPopulousNationsWithGDPs.csv";

	public static final boolean SHOULD_LET_USER_CHOOSE_PROJECTION = false;

	private Data[] data;
	private Dictionary<String, Object> parameters;
	private AnnotationMode annotationMode;
	private String outputAlgorithmName;
	// TODO: WTF?  public static?
	public static LogService logger;
	
	public GeoMapsAlgorithm(
			Data[] data,
			Dictionary<String, Object> parameters,
			CIShellContext context,
			AnnotationMode annotationMode, String outputAlgorithmName) {
		this.data = data;
		this.parameters = parameters;
		this.annotationMode = annotationMode;
		this.outputAlgorithmName = outputAlgorithmName;
		
		GeoMapsAlgorithm.logger = (LogService) context.getService(LogService.class.getName());
	}

	
	public Data[] execute() throws AlgorithmExecutionException {
		try {
			Data inDatum = this.data[0];
			Table inTable = (Table) inDatum.getData();
			String dataLabel =
				(String) inDatum.getMetadata().get(DataProperty.LABEL);
			
			String shapefileKey = (String) parameters.get(SHAPEFILE_ID);
			String shapefilePath = Constants.SHAPEFILES.get(shapefileKey);
			URL shapefileURL = getClass().getResource(shapefilePath);
			
			String featureNameKey = Constants.FEATURE_NAME_KEY.get(shapefileKey);

			String projectionName;
			if (SHOULD_LET_USER_CHOOSE_PROJECTION) {
				projectionName = (String) parameters.get(PROJECTION_ID);
			} else {
				if (shapefileKey.equals(Constants.COUNTRIES_SHAPEFILE_KEY)) {
					projectionName = Constants.ECKERT_IV_DISPLAY_NAME;
				} else if (shapefileKey.equals(Constants.US_STATES_SHAPEFILE_KEY)) {
					projectionName = Constants.ALBERS_EQUAL_AREA_DISPLAY_NAME;
				} else {
					projectionName = Constants.MERCATOR_DISPLAY_NAME;
				}
			}

			String authorName = (String) parameters.get(AUTHOR_NAME_ID);
			
			ShapefileToPostScriptWriter postScriptWriter;
			postScriptWriter =
				new ShapefileToPostScriptWriter(
						shapefileURL, projectionName, featureNameKey);
			
			/* applyAnnotations side-effects postScriptWriter
			 * to set annotation data and LegendComponents.
			 */
			annotationMode.applyAnnotations(
					postScriptWriter, inTable, parameters);
			File geoMap =
				postScriptWriter.writePostScriptToFile(
						projectionName, authorName, dataLabel);

			Data[] outData = formOutData(geoMap, inDatum);

			return outData;			
		} catch (IOException e) {
			throw new AlgorithmExecutionException(
					"Error creating PostScript file: " + e.getMessage(), e);
		} catch (TransformException e) {
			throw new AlgorithmExecutionException(
					"Error transforming features: " + e.getMessage(), e);
		}

		
	}
	
	private static StringTemplateGroup loadTemplates() {
		return new StringTemplateGroup(
				new InputStreamReader(
					GeoMapsAlgorithm.class.getResourceAsStream(
						STRING_TEMPLATE_FILE_PATH)));
	}
	
	@SuppressWarnings("unchecked") // metadata are raw Dictionarys
	private Data[] formOutData(File postScriptFile, Data inDatum) {
		Dictionary<String, Object> inMetaData = inDatum.getMetadata();

		Data postScriptData =
			new BasicData(postScriptFile, POSTSCRIPT_MIME_TYPE);

		Dictionary<String, Object> postScriptMetaData =
			postScriptData.getMetadata();

		postScriptMetaData.put(DataProperty.LABEL, outputAlgorithmName + "_" + FileUtilities.extractFileName(inMetaData.get(DataProperty.LABEL).toString()));
		postScriptMetaData.put(DataProperty.PARENT, inDatum);
		postScriptMetaData.put(DataProperty.TYPE,
				DataProperty.VECTOR_IMAGE_TYPE);

		return new Data[] { postScriptData };
	}


	public static void main(String[] args) {
//		File outFile = null;
		
		try {
			Dictionary<String, Object> parameters =
				new Hashtable<String, Object>();
			parameters.put("shapefile", "Countries");
			parameters.put("projection", "Albers Equal-Area Conic");
			parameters.put("authorName", "Joseph Biberstine");

			URL testFileURL = GeoMapsAlgorithm.class.getResource(TEST_DATUM_PATH);
			File inFile = new File(testFileURL.toURI());
//			File inFile = new File("C:\\Documents and Settings\\jrbibers\\Desktop\\nianli-10-16-12\\uspto_country_1976-1979_geocoded.csv");
			parameters.put("latitude", "Latitude");
			parameters.put("longitude", "Longitude");
			parameters.put("circleArea", "Population (thousands)");
			parameters.put("circleAreaScaling", "Linear");
			parameters.put("outerColorQuantity", "GDP (billions USD)");//CircleAnnotationMode.USE_NO_OUTER_COLOR_TOKEN);//"GDP (billions USD)");
			parameters.put("outerColorScaling", "Linear");
			parameters.put("outerColorRange", "Yellow to Blue");
			parameters.put("innerColorQuantity", "Population (thousands)");
			parameters.put("innerColorScaling", "Linear");
			parameters.put("innerColorRange", "Green to Red");
			AlgorithmFactory algorithmFactory =
				new GeoMapsCirclesFactory();
			
////			File inFile = new File("C:\\Documents and Settings\\jrbibers\\Desktop\\NIH-MIDAS-grants-pivot-by-state-to-total-award-amount_unabbreviated.csv");
////			File inFile = new File("C:\\Documents and Settings\\jrbibers\\Desktop\\nianli-10-16-12\\uspto_country_f_00-08.csv");
////			parameters.put("featureName", "country_name");
////			parameters.put("featureColorQuantity", "total");
////			parameters.put("featureColorScaling", "Logarithmic");
////			parameters.put("featureColorRange", "Yellow to Blue");
//			File inFile = new File("C:\\Documents and Settings\\jrbibers\\Desktop\\geomaps\\statePopulations.csv");
//			parameters.put("featureName", "State");
//			parameters.put("featureColorQuantity", "Population");
//			parameters.put("featureColorScaling", "Logarithmic");
//			parameters.put("featureColorRange", "Yellow to Blue");
//			AlgorithmFactory algorithmFactory =
//				new GeoMapsRegionsFactory();
			
			Data data = new BasicData(inFile, CSV_MIME_TYPE);
			
			PrefuseCsvReader prefuseCSVReader =
				new PrefuseCsvReader(new Data[]{ data });
			Data[] convertedData = prefuseCSVReader.execute();
			
			CIShellContext ciContext = new LogOnlyCIShellContext();
			Algorithm algorithm =
				algorithmFactory.createAlgorithm(
						convertedData, parameters, ciContext);

			System.out.println("Executing.. ");
			/*Data[] outData = */algorithm.execute();
//			outFile = (File) outData[0].getData();
			System.out.println(".. Done.");
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
//		try {
//			System.out.println("Opening output..");
//			Desktop.getDesktop().open(outFile);
//			System.out.println(".. Done.");
//		} catch (IOException e) {
//			e.printStackTrace();
//			System.exit(-1);
//		}

		System.exit(0);
	}
}