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
import org.cishell.utilities.DataFactory;
import org.geotools.factory.FactoryRegistryException;
import org.opengis.referencing.operation.TransformException;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import edu.iu.nwb.converter.prefusecsv.reader.PrefuseCsvReader;
import edu.iu.sci2.visualization.geomaps.data.scaling.Scaling;
import edu.iu.sci2.visualization.geomaps.data.scaling.ScalingException;
import edu.iu.sci2.visualization.geomaps.geo.shapefiles.Shapefile;
import edu.iu.sci2.visualization.geomaps.testing.LogOnlyCIShellContext;
import edu.iu.sci2.visualization.geomaps.testing.StdErrLogService;
import edu.iu.sci2.visualization.geomaps.viz.AnnotationMode;
import edu.iu.sci2.visualization.geomaps.viz.CircleDimension;
import edu.iu.sci2.visualization.geomaps.viz.VizDimension;
import edu.iu.sci2.visualization.geomaps.viz.legend.LegendCreationException;
import edu.iu.sci2.visualization.geomaps.viz.ps.GeoMapException;
import edu.iu.sci2.visualization.geomaps.viz.ps.GeoMapViewPS;
import edu.iu.sci2.visualization.geomaps.viz.ps.GeoMapViewPS.ShapefilePostScriptWriterException;

/*
 * TODO:
 *
 * File extension is "ps" even though the metadata says "eps".  We'd have to write
 * some (trivial) new code that takes the eps MIME type to file-ext eps.  Worth it?
 * Note: Joseph has written a prototype for this; ask him to commit.
 * 
 * The legend components currently will include data in its extrema even when that
 * piece of data isn't visible on the map.  For example, if the map is of the United
 * States, but the input data included figures for Egypt, then if the Egypt data
 * represents extremes in the data, then that will be reflected in the legend even
 * though you can't see Egypt on the output map.  Like if Egypt has a circle size
 * of 10000, and all the United States figures are less than 100, then the circle size
 * legend component will show the maximum as 10000 and show a circle of that size even
 * though no circle of that size is visible.  But is this generally wrong?  Perhaps
 * the user wants the Egyptian extreme to skew the US visualizations.
 * Need an executive call on this one.
 */
public class GeoMapsAlgorithm<G, D extends Enum<D> & VizDimension> implements Algorithm {
	public static final String CSV_MIME_TYPE = "file:text/csv";
	public static final String POSTSCRIPT_MIME_TYPE = "file:text/ps";

	public static StringTemplateGroup group = loadTemplates();
	public static final String STRING_TEMPLATE_FILE_PATH =
		"/edu/iu/sci2/visualization/geomaps/viz/stringtemplates/group.st";
	
	public static final String SHAPEFILE_ID = "shapefile";
	public static final String PROJECTION_ID = "projection";
	public static final String AUTHOR_NAME_ID = "authorName";

	public static final String TEST_DATUM_PATH =
		"/edu/iu/sci2/visualization/geomaps/testing/25mostPopulousNationsWithGDPs.csv";

	public static final boolean LET_USER_CHOOSE_PROJECTION = false;

	private final Data[] data;
	private final Dictionary<String, Object> parameters;
	private final AnnotationMode<G, D> annotationMode;
	private final String outputAlgorithmName;
	// TODO reduce visibility
	public static LogService logger = new StdErrLogService();
	
	public GeoMapsAlgorithm(
			Data[] data,
			Dictionary<String, Object> parameters,
			AnnotationMode<G, D> annotationMode,
			String outputAlgorithmName,
			LogService logService) {
		this.data = data;
		this.parameters = parameters;
		this.annotationMode = annotationMode;
		this.outputAlgorithmName = outputAlgorithmName;
		
		GeoMapsAlgorithm.logger = logService;
	}

	
	@Override
	public Data[] execute() throws AlgorithmExecutionException {
		try {
			Data inDatum = this.data[0];
			Table inTable = (Table) inDatum.getData();
			String dataLabel = (String) inDatum.getMetadata().get(DataProperty.LABEL);
			String authorName = (String) parameters.get(AUTHOR_NAME_ID);
			
			GeoMapViewPS postScriptWriter =
					annotationMode.createPSWriter(inTable, parameters);
			File geoMap = postScriptWriter.writePostScriptToFile(authorName, dataLabel);

			Data[] outData = new Data[] {
					DataFactory.forFile(
							geoMap,
							POSTSCRIPT_MIME_TYPE,
							DataProperty.VECTOR_IMAGE_TYPE,
							inDatum,
							outputAlgorithmName)
			};

			return outData;
		} catch (IOException e) {
			throw new AlgorithmExecutionException(
					"Error creating PostScript file: " + e.getMessage(), e);
		} catch (TransformException e) {
			throw new AlgorithmExecutionException(
					"Error transforming features: " + e.getMessage(), e);
		} catch (ScalingException e) {
			throw new AlgorithmExecutionException("Error scaling data: " + e.getMessage(), e);
		} catch (LegendCreationException e) {
			throw new AlgorithmExecutionException("Error creating legend: " + e.getMessage(), e);
		} catch (ShapefilePostScriptWriterException e) {
			throw new AlgorithmExecutionException("TODO: " + e.getMessage(), e);
		} catch (FactoryRegistryException e) {
			throw new AlgorithmExecutionException("TODO: " + e.getMessage(), e);
		} catch (GeoMapException e) {
			throw new AlgorithmExecutionException("TODO: " + e.getMessage(), e);
		}
	}


	private static StringTemplateGroup loadTemplates() {
		return new StringTemplateGroup(
				new InputStreamReader(
					GeoMapsAlgorithm.class.getResourceAsStream(STRING_TEMPLATE_FILE_PATH)));
	}
	
	public static void main(String[] args) {
		try {
			Dictionary<String, Object> parameters =	new Hashtable<String, Object>();
			parameters.put(GeoMapsAlgorithm.SHAPEFILE_ID, Shapefile.WORLD.niceName());
//			parameters.put("projection", KnownProjectedCRSDescriptor.ALBERS.displayName());
			parameters.put("authorName", "Joseph Biberstine");

			URL testFileURL = GeoMapsAlgorithm.class.getResource(TEST_DATUM_PATH);
			File inFile = new File(testFileURL.toURI());
			AlgorithmFactory algorithmFactory;
			algorithmFactory = prepareFactoryForRegionsTest(parameters);
			algorithmFactory = prepareFactoryForCirclesTest(parameters);
			
			Data data = new BasicData(inFile, CSV_MIME_TYPE);
			
			PrefuseCsvReader prefuseCSVReader =	new PrefuseCsvReader(new Data[]{ data });
			Data[] convertedData = prefuseCSVReader.execute();
			
			CIShellContext ciContext = new LogOnlyCIShellContext();
			Algorithm algorithm =
				algorithmFactory.createAlgorithm(
						convertedData, parameters, ciContext);

			System.out.println("Executing.. ");
			Data[] outData = algorithm.execute();
			File outFile = (File) outData[0].getData();
			System.out.println(outFile.getAbsolutePath());
			System.out.println(".. Done.");
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}

		System.exit(0);
	}

	private static AlgorithmFactory prepareFactoryForCirclesTest(
			Dictionary<String, Object> parameters) {
		parameters.put("latitude", "Latitude");
		parameters.put("longitude", "Longitude");
		parameters.put("circleAreaColumnName", "Population (thousands)");
		parameters.put("circleAreaScaling", Scaling.Logarithmic.toString());
		parameters.put("outerColorColumnName", CircleDimension.OUTER_COLOR.getColumnNameParameterDisablingToken()); //"Population (thousands)");//CircleAnnotationMode.OUTER_COLOR_DISABLING_TOKEN);//"GDP (billions USD)");
		parameters.put("outerColorScaling", Scaling.Linear.toString());
		parameters.put("outerColorRange", "Yellow to Blue");
		parameters.put("innerColorColumnName", "Population (thousands)");
		parameters.put("innerColorScaling", Scaling.Linear.toString());
		parameters.put("innerColorRange", "Green to Red");
		AlgorithmFactory algorithmFactory = new GeoMapsCirclesFactory();
		return algorithmFactory;
	}


	private static AlgorithmFactory prepareFactoryForRegionsTest(
			Dictionary<String, Object> parameters) {
		parameters.put("featureName", "Country");
		parameters.put("featureColorColumnName", "Population (thousands)");
		parameters.put("featureColorScaling", Scaling.Logarithmic.toString());
		parameters.put("featureColorRange", "Yellow to Blue");
		AlgorithmFactory algorithmFactory =	new GeoMapsRegionsFactory();
		return algorithmFactory;
	}
}