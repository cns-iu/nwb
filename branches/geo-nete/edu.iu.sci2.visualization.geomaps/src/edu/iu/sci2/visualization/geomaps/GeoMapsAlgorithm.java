package edu.iu.sci2.visualization.geomaps;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map.Entry;

import org.antlr.stringtemplate.StringTemplate;
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

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;

import edu.iu.nwb.converter.prefusecsv.reader.PrefuseCsvReader;
import edu.iu.sci2.visualization.geomaps.data.scaling.Scaling;
import edu.iu.sci2.visualization.geomaps.geo.shapefiles.Shapefile;
import edu.iu.sci2.visualization.geomaps.metatype.Parameters;
import edu.iu.sci2.visualization.geomaps.testing.LogOnlyCIShellContext;
import edu.iu.sci2.visualization.geomaps.testing.StdErrLogService;
import edu.iu.sci2.visualization.geomaps.viz.AnnotationMode;
import edu.iu.sci2.visualization.geomaps.viz.PageLayout;
import edu.iu.sci2.visualization.geomaps.viz.VizDimension;
import edu.iu.sci2.visualization.geomaps.viz.legend.LegendCreationException;
import edu.iu.sci2.visualization.geomaps.viz.model.GeoMap;
import edu.iu.sci2.visualization.geomaps.viz.model.GeoMapException;
import edu.iu.sci2.visualization.geomaps.viz.ps.GeoMapViewPS;
import edu.iu.sci2.visualization.geomaps.viz.ps.GeoMapViewPS.ShapefilePostScriptWriterException;
import edu.iu.sci2.visualization.geomaps.viz.ps.HowToRead;

/* TODO The codings and legends are determined using all available data, not just data that is
 * actually shown.  Is this bad?   
 */
public class GeoMapsAlgorithm<G, D extends Enum<D> & VizDimension> implements Algorithm {
	public static final String BASE_TITLE = "Geospatial Visualization";
	
	public static final String CSV_MIME_TYPE = "file:text/csv";
	public static final String POSTSCRIPT_MIME_TYPE = "file:text/ps";
	public static final String OUTPUT_FILE_EXTENSION = "ps"; // Though we generate EPS specifically

	public static StringTemplateGroup TEMPLATE_GROUP = loadTemplates();
	public static final String STRING_TEMPLATE_FILE_PATH =
		"/edu/iu/sci2/visualization/geomaps/viz/stringtemplates/geomap.stg";
	
	private final Data[] data;
	private final Dictionary<String, Object> parameters;
	private final PageLayout pageLayout;
	private final AnnotationMode<G, D> annotationMode;
	private final String subtitle;
	private final StringTemplate templateForHowToRead;
	
	public static LogService logger = new StdErrLogService();
	
	public GeoMapsAlgorithm(
			Data[] data,
			Dictionary<String, Object> parameters,
			PageLayout pageLayout,
			AnnotationMode<G, D> annotationMode,
			String subtitle,
			StringTemplate templateForHowToRead,
			LogService logService) {
		this.data = data;
		this.parameters = parameters;
		this.pageLayout = pageLayout;
		this.annotationMode = annotationMode;
		this.subtitle = subtitle;
		this.templateForHowToRead = templateForHowToRead;
		
		GeoMapsAlgorithm.logger = logService;
	}

	
	@Override
	public Data[] execute() throws AlgorithmExecutionException {
		try {
			Data inDatum = this.data[0];
			Table inTable = (Table) inDatum.getData();
			String dataLabel = (String) inDatum.getMetadata().get(DataProperty.LABEL);
			
			String fullTitle = String.format("%s (%s)", BASE_TITLE, subtitle);
			
			GeoMap geoMap = annotationMode.createGeoMap(inTable, parameters, pageLayout, fullTitle);
			
			String mapKind = subtitle.toLowerCase();
			templateForHowToRead.setAttribute("mapKind", mapKind);
			templateForHowToRead.setAttribute("baseMapDescription", geoMap.getShapefile().makeMapDescription());
			templateForHowToRead.setAttribute("projectionName", geoMap.getKnownProjectedCRSDescriptor().getNiceNamePlain());
			templateForHowToRead.setAttribute("hasInsets", geoMap.getShapefile().hasInsets());
			templateForHowToRead.setAttribute("partType", geoMap.getShapefile().getComponentDescriptionPlain());
			String howToReadText = templateForHowToRead.toString().trim();
			Optional<HowToRead> howToRead =
					(pageLayout.howToReadLowerLeft().isPresent())
					? (Optional.of(new HowToRead(pageLayout.howToReadLowerLeft().get(), pageLayout, howToReadText, mapKind)))
					: (Optional.<HowToRead>absent());
			
			GeoMapViewPS geoMapView = new GeoMapViewPS(geoMap, pageLayout, howToRead);
			File geoMapFile = geoMapView.writeToPSFile(dataLabel);

			Data[] outData = new Data[] {
					DataFactory.forFile(
							geoMapFile,
							POSTSCRIPT_MIME_TYPE,
							DataProperty.VECTOR_IMAGE_TYPE,
							inDatum,
							fullTitle)
			};

			return outData;
		} catch (IOException e) {
			throw new AlgorithmExecutionException(
					"Error creating PostScript file: " + e.getMessage(), e);
		} catch (TransformException e) {
			throw new AlgorithmExecutionException(
					"Error transforming features: " + e.getMessage(), e);
		} catch (LegendCreationException e) {
			throw new AlgorithmExecutionException("Error creating legend: " + e.getMessage(), e);
		} catch (ShapefilePostScriptWriterException e) {
			throw new AlgorithmExecutionException("Error visualizing geo map: " + e.getMessage(), e);
		} catch (FactoryRegistryException e) {
			throw new AlgorithmExecutionException("Geography error: " + e.getMessage(), e);
		} catch (GeoMapException e) {
			throw new AlgorithmExecutionException("Error creating geo map: " + e.getMessage(), e);
		}
	}


	private static StringTemplateGroup loadTemplates() {
		return new StringTemplateGroup(
				new InputStreamReader(
					GeoMapsAlgorithm.class.getResourceAsStream(STRING_TEMPLATE_FILE_PATH)));
	}
	
	
	public static void main(String[] args) {
		Example.WORLD_CIRCLES.run(PageLayout.PRINT);
//		Example.WORLD_CIRCLES.run(PageLayout.WEB);
//		Example.US_REGIONS.run(PageLayout.PRINT);
//		Example.US_REGIONS.run(PageLayout.WEB);
	}


	private static final String EXAMPLE_FILE_URL_STEM = "/edu/iu/sci2/visualization/geomaps/testing/";
	private enum Example {
		WORLD_CIRCLES(
				Shapefile.WORLD,
				GeoMapsAlgorithm.class.getResource(EXAMPLE_FILE_URL_STEM + "25mostPopulousNationsWithGDPs.csv"),
				ImmutableMap.<PageLayout, Class<? extends AlgorithmFactory>>of(
						PageLayout.PRINT, GeoMapsCirclesFactory.class,
						PageLayout.WEB, GeoMapsWebCirclesFactory.class),
				ImmutableMap.<String, Object>builder()
						.put("latitude", "Latitude")
						.put("longitude", "Longitude")
						
						.put("circleAreaColumnName",
								// CircleDimension.AREA.getColumnNameParameterDisablingToken()
								"GDP (Billions USD)"
								)
						.put("circleAreaScaling", Scaling.Linear.toString())
						
						.put("outerColorColumnName",
								// CircleDimension.OUTER_COLOR.getColumnNameParameterDisablingToken()
								"Population (Thousands)"
								)
						.put("outerColorScaling", Scaling.Linear.toString())
						.put("outerColorRange", "Yellow to Red")
						
						.put("innerColorColumnName",
								// CircleDimension.INNER_COLOR.getColumnNameParameterDisablingToken()
								"Population (Thousands)"
								)
						.put("innerColorScaling", Scaling.Linear.toString())
						.put("innerColorRange", "White to Black")
						.build()
				),
		US_REGIONS(
				Shapefile.UNITED_STATES,
				GeoMapsAlgorithm.class.getResource(EXAMPLE_FILE_URL_STEM + "us-state-populations.csv"),
				ImmutableMap.<PageLayout, Class<? extends AlgorithmFactory>>of(
						PageLayout.PRINT, GeoMapsRegionsFactory.class,
						PageLayout.WEB, GeoMapsWebRegionsFactory.class),
				ImmutableMap.<String, Object>builder()
						.put("featureName", "State")
						.put("featureColorColumnName",
//								FeatureDimension.REGION_COLOR.getColumnNameParameterDisablingToken()
								"Population"
								)
						.put("featureColorScaling", Scaling.Linear.toString())
						.put("featureColorRange", "Yellow to Blue")
						.build()
				);
		
		private final Shapefile shapefile;
		private final URL csvFileURL;
		private final ImmutableMap<PageLayout, Class<? extends AlgorithmFactory>> algorithmFactoryClassForPageLayout;
		private final ImmutableMap<String, Object> baseParameters;

		private Example(
				Shapefile shapefile,
				URL tableFileURL,
				ImmutableMap<PageLayout, Class<? extends AlgorithmFactory>> algorithmFactoryClassForPageLayout,
				ImmutableMap<String, Object> baseParameters) {
			this.shapefile = shapefile;
			this.csvFileURL = tableFileURL;
			this.algorithmFactoryClassForPageLayout = algorithmFactoryClassForPageLayout;
			this.baseParameters = baseParameters;
		}
		
		private void run(PageLayout pageLayout) {
			try {
				Dictionary<String, Object> parameters =
						assembleParameters(shapefile, this.baseParameters);	
				Algorithm algorithm =
						createAlgorithm(
								algorithmFactoryClassForPageLayout.get(pageLayout),
								csvFileURL,
								parameters);
	
				System.out.println("Executing.. ");
				Data[] outData = algorithm.execute();
				File outFile = (File) outData[0].getData();
				System.out.println(outFile.getAbsolutePath());
				System.out.println(".. Done.");
				Desktop.getDesktop().open(outFile);
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(-1);
			}
		}

		private static Dictionary<String, Object> assembleParameters(
				Shapefile shapefile, ImmutableMap<String, Object> baseParameters) {
			Dictionary<String, Object> parameters =	new Hashtable<String, Object>();
			parameters.put(Parameters.SHAPEFILE_ID, shapefile.getNiceName());
			
			for (Entry<String, Object> entry : baseParameters.entrySet()) {
				parameters.put(entry.getKey(), entry.getValue());
			}
			
			return parameters;
		}

		private static Data[] convertToPrefuseTableData(URL csvFileURL) throws URISyntaxException,
				AlgorithmExecutionException {
			File inFile = new File(csvFileURL.toURI());
			Data data = new BasicData(inFile, CSV_MIME_TYPE);				
			PrefuseCsvReader prefuseCSVReader =	new PrefuseCsvReader(new Data[]{ data });
			
			return prefuseCSVReader.execute();
		}

		private static Algorithm createAlgorithm(
				Class<? extends AlgorithmFactory> algorithmFactoryClass,
				URL csvFileURL,
				Dictionary<String, Object> parameters)
						throws InstantiationException, IllegalAccessException, URISyntaxException,
								AlgorithmExecutionException {
			AlgorithmFactory algorithmFactory = algorithmFactoryClass.newInstance();
			Data[] prefuseTableData = convertToPrefuseTableData(csvFileURL);			
			CIShellContext ciContext = new LogOnlyCIShellContext();
			
			return algorithmFactory.createAlgorithm(prefuseTableData, parameters, ciContext);
		}
	}
}