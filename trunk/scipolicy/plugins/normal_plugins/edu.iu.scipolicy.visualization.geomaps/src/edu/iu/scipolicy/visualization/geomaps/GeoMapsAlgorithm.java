package edu.iu.scipolicy.visualization.geomaps;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Dictionary;

import org.antlr.stringtemplate.StringTemplateGroup;
import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.opengis.referencing.operation.TransformException;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import edu.iu.scipolicy.visualization.geomaps.utility.Constants;

public class GeoMapsAlgorithm implements Algorithm {
	public static final String CSV_MIME_TYPE = "file:text/csv";
	public static final String POSTSCRIPT_MIME_TYPE = "file:text/ps";

	public static StringTemplateGroup group = loadTemplates();
	public static final String STRING_TEMPLATE_FILE_PATH =		 
		"/edu/iu/scipolicy/visualization/geomaps/stringtemplates/group.st";
	
	public static final String SHAPEFILE_ID = "shapefile";	
	public static final String PROJECTION_ID = "projection";
	public static final String AUTHOR_NAME_ID = "authorName";

	public static final String TEST_DATUM_PATH = "/edu/iu/scipolicy/visualization/geomaps/testFiles/25mostPopulousNationsWithGDPs.csv";

	private Data[] data;
	private Dictionary<String, Object> parameters;
	private AnnotationMode annotationMode;
	public static LogService logger;

	
	public GeoMapsAlgorithm(
			Data[] data,
			Dictionary<String, Object> parameters,
			CIShellContext context,
			AnnotationMode annotationMode) {
		this.data = data;
		this.parameters = parameters;
		this.annotationMode = annotationMode;
		
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
			String projectionName = (String) parameters.get(PROJECTION_ID);
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

		postScriptMetaData.put(DataProperty.LABEL, "PostScript: "
				+ inMetaData.get(DataProperty.LABEL));
		postScriptMetaData.put(DataProperty.PARENT, inDatum);
		postScriptMetaData.put(DataProperty.TYPE,
				DataProperty.VECTOR_IMAGE_TYPE);

		return new Data[] { postScriptData };
	}


//	public static void main(String[] args) {
////		File outFile = null;
//		
//		try {
//			File inFile = loadTestFile();
//			Data data = new BasicData(inFile, CSV_MIME_TYPE);
//
//			CIShellContext ciContext = createDummyCIShellContext();
//
//			PrefuseCsvReader prefuseCSVReader =
//				new PrefuseCsvReader(
//						new Data[]{ data },
//						new Hashtable<String, Object>(),
//						ciContext);
//			Data[] convertedData = prefuseCSVReader.execute();
//
//			Dictionary<String, Object> parameters =
//				new Hashtable<String, Object>();
//			parameters.put("shapefile", "Countries");
//			parameters.put("projection", "Eckert IV");
//			parameters.put("authorName", "Joseph Biberstine");
//			parameters.put("latitude", "Latitude");
//			parameters.put("longitude", "Longitude");
//			parameters.put("circleArea", "Population (thousands)");
//			parameters.put("circleAreaScaling", "Linear");
//			parameters.put("outerColorQuantity", "GDP (billions USD)");
//			parameters.put("outerColorScaling", "Linear");
//			parameters.put("outerColorRange", "Yellow to Blue");
//			parameters.put("innerColorQuantity", "Population (thousands)");
//			parameters.put("innerColorScaling", "Linear");
//			parameters.put("innerColorRange", "Green to Red");
//
//			AlgorithmFactory algorithmFactory =
//				new GeoMapsCirclesFactory();
//			Algorithm algorithm =
//				algorithmFactory.createAlgorithm(
//						convertedData, parameters, ciContext);
//
//			System.out.println("Executing.. ");
//			/*Data[] outData = */algorithm.execute();
////			outFile = (File) outData[0].getData();
//			System.out.println(".. Done.");
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.exit(-1);
//		}
//		
////		try {
////			System.out.println("Opening output..");
////			Desktop.getDesktop().open(outFile);
////			System.out.println(".. Done.");
////		} catch (IOException e) {
////			e.printStackTrace();
////			System.exit(-1);
////		}
//
//		System.exit(0);
//	}	
//	private static File loadTestFile() throws URISyntaxException {
//		URL url = GeoMapsAlgorithm.class.getResource(TEST_DATUM_PATH);
//	    return new File(url.toURI());
//	}
//	/* A CIShellContext whose getService always returns a LogService
//	 * that puts all messages on System.err.
//	 */
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