package edu.iu.scipolicy.visualization.geomaps;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.ProjectedCRS;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import edu.iu.scipolicy.utilities.FileUtilities;

public class GeoMapsAlgorithm implements Algorithm {
	public static final String OUTPUT_FILE_EXTENSION = "eps";
	
	public static final String SHAPEFILE_ID = "shapefile";
	public static final Map<String, String> PROJECTIONS;
	static {
		// Values should correspond to .shp files in the shapefiles package
		Map<String, String> t = new HashMap<String, String>();
		t.put("US States", "/edu/iu/scipolicy/visualization/geomaps/shapefiles/statesp020.shp");
		t.put("Countries", "/edu/iu/scipolicy/visualization/geomaps/shapefiles/countries.shp");
		SHAPEFILES = Collections.unmodifiableMap(t);
	}	
	public static final Map<String, String> FEATURE_NAME_KEY;
	static {
		/* Values should correspond to feature-identifying attribute of the 
		 *respective shapefile
		 */
		Map<String, String> t = new HashMap<String, String>();
		t.put("US States", "STATE");
		t.put("Countries", "NAME");
		FEATURE_NAME_KEY = Collections.unmodifiableMap(t);
	}
	
	public static final String PROJECTION_ID = "projection";
	public static final Map<String, String> SHAPEFILES;
	static {
		// Values should correspond to keys in projection/wellKnownTexts.properties
		Map<String, String> t = new HashMap<String, String>();
		t.put("Mercator", "mercator");
		t.put("Albers Equal-Area Conic", "albersEqualArea");
		t.put("Lambert Conformal Conic", "lambertConformalConic");
		PROJECTIONS = Collections.unmodifiableMap(t);
	}
	
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
		
		final ClassLoader loader = getClass().getClassLoader();
		String shapefileKey = (String) parameters.get(SHAPEFILE_ID);
		String shapefilePath = GeoMapsAlgorithm.SHAPEFILES.get(shapefileKey);	
		final URL shapefileURL = loader.getResource(shapefilePath);
		final ProjectedCRS projectedCRS = getProjectedCRS();
		
		String featureNameKey = GeoMapsAlgorithm.FEATURE_NAME_KEY.get(shapefileKey);
		
		ShapefileToPostScriptWriter postScriptWriter =
			new ShapefileToPostScriptWriter(shapefileURL, projectedCRS, featureNameKey);
		
		annotationMode.applyAnnotations(postScriptWriter, inTable, parameters);
		try {
			postScriptWriter.writePostScriptToFile(temporaryPostScriptFile);
		} catch (IOException e) {
			throw new AlgorithmExecutionException(e);
		}

		Data[] outData = formOutData(temporaryPostScriptFile, inDatum);

		return outData;
	}
	
	private ProjectedCRS getProjectedCRS() throws AlgorithmExecutionException {
		final ClassLoader loader = getClass().getClassLoader();
		final InputStream wellKnownTextInputStream = loader.getResourceAsStream("/edu/iu/scipolicy/visualization/geomaps/projection/wellKnownTexts.properties");

		final Properties wellKnownTexts = new Properties();
		try {
			wellKnownTexts.load(wellKnownTextInputStream);
		} catch (final FileNotFoundException fnfe) {
			logger.log(LogService.LOG_ERROR, fnfe.getMessage(), fnfe);
		} catch (final IOException ie) {
			logger.log(LogService.LOG_ERROR, ie.getMessage(), ie);
		}

		String projectionName = (String) parameters.get(PROJECTION_ID);
		String projectionWKTKey = GeoMapsAlgorithm.PROJECTIONS.get(projectionName);
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

		Data postScriptData = new BasicData(postScriptFile, "file:text/ps");

		Dictionary postScriptMetaData = postScriptData.getMetadata();

		postScriptMetaData.put(DataProperty.LABEL, "PostScript: "
				+ inMetaData.get(DataProperty.LABEL));
		postScriptMetaData.put(DataProperty.PARENT, inDatum);
		postScriptMetaData.put(DataProperty.TYPE,
				DataProperty.VECTOR_IMAGE_TYPE);

		return new Data[] { postScriptData };
	}
}