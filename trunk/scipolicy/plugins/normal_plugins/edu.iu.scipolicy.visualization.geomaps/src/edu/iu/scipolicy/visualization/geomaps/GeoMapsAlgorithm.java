package edu.iu.scipolicy.visualization.geomaps;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Dictionary;
import java.util.Properties;

import org.antlr.stringtemplate.StringTemplateGroup;
import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.utilities.FileUtilities;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.ProjectedCRS;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import edu.iu.scipolicy.visualization.geomaps.utility.Constants;

public class GeoMapsAlgorithm implements Algorithm {
	public static final String STRING_TEMPLATE_FILE_PATH =		 
		"/edu/iu/scipolicy/visualization/geomaps/stringtemplates/group.st";

	public static final String POSTSCRIPT_MIME_TYPE = "file:text/ps";

	public static final String OUTPUT_FILE_EXTENSION = "eps";
	
	public static final String SHAPEFILE_ID = "shapefile";
	
	public static final String PROJECTION_ID = "projection";
	public static final String AUTHOR_NAME_ID = "authorName";
	
	
	public static StringTemplateGroup group = loadTemplates();
	private Data[] data;
	@SuppressWarnings("unchecked") // TODO
	private Dictionary parameters;
	private AnnotationMode annotationMode;
	public static LogService logger;

	@SuppressWarnings("unchecked") // TODO
	public GeoMapsAlgorithm(Data[] data, Dictionary parameters, CIShellContext context, AnnotationMode annotationMode) {
		this.data = data;
		this.parameters = parameters;
		this.annotationMode = annotationMode;
		
		GeoMapsAlgorithm.logger = (LogService) context.getService(LogService.class.getName());
	}

	public Data[] execute() throws AlgorithmExecutionException {
		File temporaryPostScriptFile;
		try {
			temporaryPostScriptFile = FileUtilities.createTemporaryFileInDefaultTemporaryDirectory("TEMP-POSTSCRIPT", OUTPUT_FILE_EXTENSION);
		} catch (IOException e) {
			throw new AlgorithmExecutionException(e);
		}

		Data inDatum = this.data[0];
		Table inTable = (Table) inDatum.getData();
		String dataLabel = (String) inDatum.getMetadata().get(DataProperty.LABEL);
		
		final ClassLoader loader = getClass().getClassLoader();
		String shapefileKey = (String) parameters.get(SHAPEFILE_ID);
		String shapefilePath = Constants.SHAPEFILES.get(shapefileKey);	
		final URL shapefileURL = loader.getResource(shapefilePath);
		final ProjectedCRS projectedCRS = getProjectedCRS();
		
		String featureNameKey = Constants.FEATURE_NAME_KEY.get(shapefileKey);
		
		String authorName = (String) parameters.get(AUTHOR_NAME_ID);
		
		ShapefileToPostScriptWriter postScriptWriter =
			new ShapefileToPostScriptWriter(shapefileURL, projectedCRS, featureNameKey);
		
		annotationMode.applyAnnotations(postScriptWriter, inTable, parameters);
		try {
			postScriptWriter.writePostScriptToFile(temporaryPostScriptFile, authorName, dataLabel);
		} catch (IOException e) {
			throw new AlgorithmExecutionException(e);
		}

		Data[] outData = formOutData(temporaryPostScriptFile, inDatum);

		return outData;
	}
	
	private static StringTemplateGroup loadTemplates() {
		return new StringTemplateGroup(
				new InputStreamReader(
					GeoMapsAlgorithm.class.getResourceAsStream(
						STRING_TEMPLATE_FILE_PATH)));
	}
	
	private ProjectedCRS getProjectedCRS() throws AlgorithmExecutionException {
		final ClassLoader loader = getClass().getClassLoader();
		final InputStream wellKnownTextInputStream = loader.getResourceAsStream("/edu/iu/scipolicy/visualization/geomaps/projection/wellKnownTexts.properties");

		final Properties wellKnownTexts = new Properties();
		try {
			wellKnownTexts.load(wellKnownTextInputStream);
		} catch (FileNotFoundException e) {
			throw new AlgorithmExecutionException(
				"Error finding the file that describes available map projections: " + e.getMessage(), e);
		} catch (IOException e) {
			throw new AlgorithmExecutionException(
				"Error accessing the file that describes available map projections: " + e.getMessage(), e);
		}

		String projectionName = (String) parameters.get(PROJECTION_ID);
		String projectionWKTKey = Constants.PROJECTIONS.get(projectionName);
		try {
			return (ProjectedCRS) CRS.parseWKT(wellKnownTexts.getProperty(projectionWKTKey));

			/*
			 * You will need to attach the EPSG database (and maybe even its
			 * extensions) if you wish to specify the projection using an EPSG
			 * code. You may know a better way, but I would do this by creating
			 * a small GeoTools standalone project (see
			 * http://docs.codehaus.org/display/GEOTDOC/03+First+Project ) and
			 * specifying the epsg and epsg-extension dependencies in the
			 * pom.xml. Let maven acquire the necessary jars (as on the page
			 * given), then create a new geolibs plugin from those.
			 * 
			 * projectedCRS = (ProjectedCRS) CRS.decode(MERCATOR_EPSG_CODE);
			 */
		} catch (FactoryException e) {
			throw new AlgorithmExecutionException(e);
		}
	}

	
	@SuppressWarnings("unchecked") // TODO
	private Data[] formOutData(File postScriptFile, Data inDatum) {
		Dictionary inMetaData = inDatum.getMetadata();

		Data postScriptData = new BasicData(postScriptFile, POSTSCRIPT_MIME_TYPE);

		Dictionary postScriptMetaData = postScriptData.getMetadata();

		postScriptMetaData.put(DataProperty.LABEL, "PostScript: "
				+ inMetaData.get(DataProperty.LABEL));
		postScriptMetaData.put(DataProperty.PARENT, inDatum);
		postScriptMetaData.put(DataProperty.TYPE,
				DataProperty.VECTOR_IMAGE_TYPE);

		return new Data[] { postScriptData };
	}
}