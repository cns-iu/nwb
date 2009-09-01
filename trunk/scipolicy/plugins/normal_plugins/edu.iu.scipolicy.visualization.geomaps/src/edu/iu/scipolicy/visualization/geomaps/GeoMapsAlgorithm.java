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
	public static final String STRING_TEMPLATE_FILE_PATH =		 
		"/edu/iu/scipolicy/visualization/geomaps/stringtemplates/group.st";

	public static final String POSTSCRIPT_MIME_TYPE = "file:text/ps";
	
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
		try {
			Data inDatum = this.data[0];
			Table inTable = (Table) inDatum.getData();
			String dataLabel = (String) inDatum.getMetadata().get(DataProperty.LABEL);
			
			final ClassLoader loader = getClass().getClassLoader();
			String shapefileKey = (String) parameters.get(SHAPEFILE_ID);
			String shapefilePath = Constants.SHAPEFILES.get(shapefileKey);	
			final URL shapefileURL = loader.getResource(shapefilePath);
			
			String featureNameKey = Constants.FEATURE_NAME_KEY.get(shapefileKey);
			String projectionName = (String) parameters.get(PROJECTION_ID);
			String authorName = (String) parameters.get(AUTHOR_NAME_ID);
			
			ShapefileToPostScriptWriter postScriptWriter;
			postScriptWriter =
				new ShapefileToPostScriptWriter(shapefileURL, projectionName, featureNameKey);
			
			/* applyAnnotations side-effects postScriptWriter
			 * to set annotation data and LegendComponents.
			 */
			annotationMode.applyAnnotations(postScriptWriter, inTable, parameters);
			File geoMap = postScriptWriter.writePostScriptToFile(projectionName, authorName, dataLabel);

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