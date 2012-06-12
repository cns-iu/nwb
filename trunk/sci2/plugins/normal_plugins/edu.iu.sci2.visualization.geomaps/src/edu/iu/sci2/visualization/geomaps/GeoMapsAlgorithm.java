package edu.iu.sci2.visualization.geomaps;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Map.Entry;

import org.antlr.stringtemplate.StringTemplate;
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

import prefuse.data.Table;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.common.io.Files;

import edu.iu.nwb.converter.prefusecsv.reader.PrefuseCsvReader;
import edu.iu.sci2.visualization.geomaps.data.scaling.Scaling;
import edu.iu.sci2.visualization.geomaps.geo.projection.KnownProjectedCRSDescriptor;
import edu.iu.sci2.visualization.geomaps.geo.shapefiles.Shapefile;
import edu.iu.sci2.visualization.geomaps.metatype.Parameters;
import edu.iu.sci2.visualization.geomaps.viz.AnnotationMode;
import edu.iu.sci2.visualization.geomaps.viz.CircleDimension;
import edu.iu.sci2.visualization.geomaps.viz.FeatureDimension;
import edu.iu.sci2.visualization.geomaps.viz.PageLayout;
import edu.iu.sci2.visualization.geomaps.viz.VizDimension;
import edu.iu.sci2.visualization.geomaps.viz.legend.LegendCreationException;
import edu.iu.sci2.visualization.geomaps.viz.model.GeoMap;
import edu.iu.sci2.visualization.geomaps.viz.model.GeoMapException;
import edu.iu.sci2.visualization.geomaps.viz.model.RegionAnnotationMode;
import edu.iu.sci2.visualization.geomaps.viz.ps.GeoMapViewPS;
import edu.iu.sci2.visualization.geomaps.viz.ps.GeoMapViewPS.ShapefilePostScriptWriterException;

/* The codings and legends are determined using all available data, not just data that is actually
 * shown. */
public class GeoMapsAlgorithm<G, D extends Enum<D> & VizDimension> implements Algorithm {
	private static final String BASE_TITLE = "Geospatial Visualization";
	
	private static final String CSV_MIME_TYPE = "file:text/csv";
	static final String POSTSCRIPT_MIME_TYPE = "file:text/ps";
	static final String OUTPUT_FILE_EXTENSION = "ps"; // Though we generate EPS specifically

	private final Data[] data;
	private final Dictionary<String, Object> parameters;
	private final PageLayout pageLayout;
	private final AnnotationMode<G, D> annotationMode;
	private final String subtitle;
	private final StringTemplate templateForHowToRead;
	private final String dataLabel;
	
	public GeoMapsAlgorithm(
			Data[] data,
			Dictionary<String, Object> parameters,
			PageLayout pageLayout,
			AnnotationMode<G, D> annotationMode,
			String dataLabel,
			String subtitle,
			StringTemplate templateForHowToRead) {
		this.data = data;
		this.parameters = parameters;
		this.pageLayout = pageLayout;
		this.annotationMode = annotationMode;
		this.dataLabel = dataLabel;
		this.subtitle = subtitle;
		this.templateForHowToRead = templateForHowToRead;
	}

	
	@Override
	public Data[] execute() throws AlgorithmExecutionException {
		try {
			Data inDatum = this.data[0];
			Table inTable = (Table) inDatum.getData();
			String dataLabel = Strings.nullToEmpty(this.dataLabel);
			
			String fullTitle = String.format("%s (%s)", BASE_TITLE, subtitle);
			
			GeoMap geoMap = annotationMode.createGeoMap(inTable, parameters, pageLayout, fullTitle,
					subtitle, templateForHowToRead);
			
			GeoMapViewPS geoMapView = new GeoMapViewPS(geoMap, pageLayout);
			File geoMapFile = geoMapView.writeToPSFile(dataLabel, OUTPUT_FILE_EXTENSION);

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


	public static void main(String[] args) {
//		Set<KnownProjectedCRSDescriptor> projections =
//				EnumSet.of(
////						KnownProjectedCRSDescriptor.WORLD_EQUIDISTANT_CYLINDRICAL_SPHERE
//						KnownProjectedCRSDescriptor.ALBERS
//						);
//		KnownProjectedCRSDescriptor[] projectionsArray =
//				projections.toArray(new KnownProjectedCRSDescriptor[0]);
		
//		Example.ALASKA_CIRCLE_OVERLAY_INSET_TEST.run(PageLayout.PRINT);
		Example.WORLD_CIRCLES.run(PageLayout.PRINT);
//		Example.WORLD_CIRCLES.run(PageLayout.WEB);
		Example.US_REGIONS.run(PageLayout.PRINT);//, projectionsArray);
//		Example.US_REGIONS.run(PageLayout.WEB);
		Example.DUPLICATE_REGIONS.run(PageLayout.PRINT);
		Example.DUPLICATE_CIRCLES.run(PageLayout.PRINT);
	}


	private static final String EXAMPLE_FILE_URL_STEM = "/edu/iu/sci2/visualization/geomaps/testing/";
	private enum Example {
		WORLD_CIRCLES(
				Shapefile.WORLD,
				GeoMapsAlgorithm.class.getResource(EXAMPLE_FILE_URL_STEM +
						"area-population-and-population-density-of-20-most-populous-cities.csv"),
				"20 most populous cities",
				ImmutableMap.<PageLayout, Class<? extends AlgorithmFactory>>of(
						PageLayout.PRINT, GeoMapsCirclesFactory.Print.class,
						PageLayout.WEB, GeoMapsCirclesFactory.Web.class),
				ImmutableMap.<String, Object>builder()
						.put(GeoMapsNetworkFactory.Parameter.LATITUDE.id(), "Latitude")
						.put(GeoMapsNetworkFactory.Parameter.LONGITUDE.id(), "Longitude")
						.put(CircleDimension.AREA.getColumnNameParameterId(),
								// CircleDimension.AREA.getColumnNameParameterDisablingToken()
								"Population")
						.put(CircleDimension.AREA.getScalingParameterId(),
								Scaling.Linear.toString())
						.put(CircleDimension.OUTER_COLOR.getColumnNameParameterId(),
								// CircleDimension.OUTER_COLOR.getColumnNameParameterDisablingToken()
								"Area (sq. km.)")
						.put(CircleDimension.OUTER_COLOR.getScalingParameterId(),
								Scaling.Logarithmic.toString())
						.put(CircleDimension.OUTER_COLOR.getRangeParameterId(), "White to Black")
						.put(CircleDimension.INNER_COLOR.getColumnNameParameterId(),
//								CircleDimension.INNER_COLOR.getColumnNameParameterDisablingToken()
								"Population density (people per sq. km.)")
						.put(CircleDimension.INNER_COLOR.getScalingParameterId(),
								Scaling.Linear.toString())
						.put(CircleDimension.INNER_COLOR.getRangeParameterId(),
								"Yellow to Blue")
						.build()
				),
		US_REGIONS(
				Shapefile.UNITED_STATES,
				GeoMapsAlgorithm.class.getResource(EXAMPLE_FILE_URL_STEM + "us-state-populations.csv"),
				"U.S. state populations",
				ImmutableMap.<PageLayout, Class<? extends AlgorithmFactory>>of(
						PageLayout.PRINT, GeoMapsRegionsFactory.Print.class,
						PageLayout.WEB, GeoMapsRegionsFactory.Web.class),
				ImmutableMap.<String, Object>builder()
						.put(RegionAnnotationMode.FEATURE_NAME_ID, "State")
						.put(FeatureDimension.REGION_COLOR.getColumnNameParameterId(),
//								FeatureDimension.REGION_COLOR.getColumnNameParameterDisablingToken()
								"Population")
						.put(FeatureDimension.REGION_COLOR.getScalingParameterId(),
								Scaling.Linear.toString())
						.put(FeatureDimension.REGION_COLOR.getRangeParameterId(),
								"Yellow to Blue")
						.build()
				),
		ALASKA_CIRCLE_OVERLAY_INSET_TEST(
				Shapefile.UNITED_STATES,
				GeoMapsAlgorithm.class.getResource(EXAMPLE_FILE_URL_STEM +
						"alaska-circle-overlay-inset-test.csv"),
				"Alaska circle overlay inset test",
				ImmutableMap.<PageLayout, Class<? extends AlgorithmFactory>>of(
						PageLayout.PRINT, GeoMapsCirclesFactory.Print.class,
						PageLayout.WEB, GeoMapsCirclesFactory.Web.class),
				ImmutableMap.<String, Object>builder()
						.put(GeoMapsNetworkFactory.Parameter.LATITUDE.id(), "Latitude")
						.put(GeoMapsNetworkFactory.Parameter.LONGITUDE.id(), "Longitude")
						.put(CircleDimension.AREA.getColumnNameParameterId(),
								CircleDimension.AREA.getColumnNameParameterDisablingToken())
						.put(CircleDimension.AREA.getScalingParameterId(),
								Scaling.Linear.toString())
						.put(CircleDimension.OUTER_COLOR.getColumnNameParameterId(),
								CircleDimension.OUTER_COLOR.getColumnNameParameterDisablingToken())
						.put(CircleDimension.OUTER_COLOR.getScalingParameterId(),
								Scaling.Logarithmic.toString())
						.put(CircleDimension.OUTER_COLOR.getRangeParameterId(),
								"White to Black")
						.put(CircleDimension.INNER_COLOR.getColumnNameParameterId(),
								CircleDimension.INNER_COLOR.getColumnNameParameterDisablingToken())
						.put(CircleDimension.INNER_COLOR.getScalingParameterId(),
								Scaling.Linear.toString())
						.put(CircleDimension.INNER_COLOR.getRangeParameterId(),
								"Yellow to Blue")
						.build()
				),
		CONGRESSIONAL_CIRCLES(
				Shapefile.UNITED_STATES,
				GeoMapsAlgorithm.class.getResource(EXAMPLE_FILE_URL_STEM +
						"congressional-districts.csv"),
				"congressional districts",
				ImmutableMap.<PageLayout, Class<? extends AlgorithmFactory>>of(
						PageLayout.PRINT, GeoMapsCirclesFactory.Print.class,
						PageLayout.WEB, GeoMapsCirclesFactory.Web.class),
				ImmutableMap.<String, Object>builder()
						.put(GeoMapsNetworkFactory.Parameter.LATITUDE.id(), "Latitude")
						.put(GeoMapsNetworkFactory.Parameter.LONGITUDE.id(), "Longitude")
						.put(CircleDimension.AREA.getColumnNameParameterId(),
								// CircleDimension.AREA.getColumnNameParameterDisablingToken()
								"Circle Size")
						.put(CircleDimension.AREA.getScalingParameterId(),
								Scaling.Linear.toString())
						.put(CircleDimension.OUTER_COLOR.getColumnNameParameterId(),
								CircleDimension.OUTER_COLOR.getColumnNameParameterDisablingToken())
						.put(CircleDimension.OUTER_COLOR.getScalingParameterId(),
								Scaling.Logarithmic.toString())
						.put(CircleDimension.OUTER_COLOR.getRangeParameterId(), "White to Black")
						.put(CircleDimension.INNER_COLOR.getColumnNameParameterId(),
//										CircleDimension.INNER_COLOR.getColumnNameParameterDisablingToken()
								"Circle Size")
						.put(CircleDimension.INNER_COLOR.getScalingParameterId(),
								Scaling.Linear.toString())
						.put(CircleDimension.INNER_COLOR.getRangeParameterId(),
								"Yellow to Blue")
						.build()
				),
		DUPLICATE_REGIONS(
				Shapefile.UNITED_STATES,
				GeoMapsAlgorithm.class.getResource(EXAMPLE_FILE_URL_STEM + "duplicates.csv"),
				"duplicate regions",
				ImmutableMap.<PageLayout, Class<? extends AlgorithmFactory>>of(
						PageLayout.PRINT, GeoMapsRegionsFactory.Print.class,
						PageLayout.WEB, GeoMapsRegionsFactory.Web.class),
				ImmutableMap.<String, Object>builder()
						.put(RegionAnnotationMode.FEATURE_NAME_ID, "State")
						.put(FeatureDimension.REGION_COLOR.getColumnNameParameterId(),
//										FeatureDimension.REGION_COLOR.getColumnNameParameterDisablingToken()
								"Value")
						.put(FeatureDimension.REGION_COLOR.getScalingParameterId(),
								Scaling.Linear.toString())
						.put(FeatureDimension.REGION_COLOR.getRangeParameterId(),
								"Yellow to Blue")
						.build()
				),
		DUPLICATE_CIRCLES(
				Shapefile.UNITED_STATES,
				GeoMapsAlgorithm.class.getResource(EXAMPLE_FILE_URL_STEM + "duplicates.csv"),
				"duplicate circles",
				ImmutableMap.<PageLayout, Class<? extends AlgorithmFactory>>of(
						PageLayout.PRINT, GeoMapsCirclesFactory.Print.class,
						PageLayout.WEB, GeoMapsCirclesFactory.Web.class),
				ImmutableMap.<String, Object>builder()
						.put(GeoMapsNetworkFactory.Parameter.LATITUDE.id(), "Latitude")
						.put(GeoMapsNetworkFactory.Parameter.LONGITUDE.id(), "Longitude")
						.put(CircleDimension.AREA.getColumnNameParameterId(),
								// CircleDimension.AREA.getColumnNameParameterDisablingToken()
								"Value")
						.put(CircleDimension.AREA.getScalingParameterId(),
								Scaling.Linear.toString())
						.put(CircleDimension.OUTER_COLOR.getColumnNameParameterId(),
								CircleDimension.OUTER_COLOR.getColumnNameParameterDisablingToken())
						.put(CircleDimension.OUTER_COLOR.getScalingParameterId(),
								Scaling.Logarithmic.toString())
						.put(CircleDimension.OUTER_COLOR.getRangeParameterId(), "White to Black")
						.put(CircleDimension.INNER_COLOR.getColumnNameParameterId(),
								CircleDimension.INNER_COLOR.getColumnNameParameterDisablingToken())
						.put(CircleDimension.INNER_COLOR.getScalingParameterId(),
								Scaling.Linear.toString())
						.put(CircleDimension.INNER_COLOR.getRangeParameterId(), "Yellow to Blue")
						.build()
				);
		
		private final Shapefile shapefile;
		private final URL csvFileURL;
		private final String dataLabel;
		private final ImmutableMap<PageLayout, Class<? extends AlgorithmFactory>> algorithmFactoryClassForPageLayout;
		private final ImmutableMap<String, Object> baseParameters;

		private Example(
				Shapefile shapefile,
				URL tableFileURL,
				String dataLabel,
				ImmutableMap<PageLayout, Class<? extends AlgorithmFactory>> algorithmFactoryClassForPageLayout,
				ImmutableMap<String, Object> baseParameters) {
			this.shapefile = shapefile;
			this.csvFileURL = tableFileURL;
			this.dataLabel = dataLabel;
			this.algorithmFactoryClassForPageLayout = algorithmFactoryClassForPageLayout;
			this.baseParameters = baseParameters;
		}
		
		private void run(String outputFilenamePrefix, PageLayout pageLayout,
				Dictionary<String, Object> parameters) {
			try {
				LogStream.DEBUG.send("parameters are " + parameters);
				Algorithm algorithm =
						createAlgorithm(
								algorithmFactoryClassForPageLayout.get(pageLayout),
								csvFileURL,
								dataLabel,
								parameters);
	
				LogStream.DEBUG.send("Executing.. ");
				Data[] outData = algorithm.execute();
				File outFileWithRawName = (File) outData[0].getData();
				File outFile = File.createTempFile(outputFilenamePrefix, ".ps");
				Files.copy(outFileWithRawName, outFile);
				
				LogStream.DEBUG.send(outFile.getAbsolutePath());
				LogStream.DEBUG.send(".. Done.");
	
	//			// Test with Ghostscript
	//			File copy = File.createTempFile("geo-viz-copy-", ".ps");
	//			Files.copy(outFile, copy);
	//			final String FULL_PATH_TO_GSVIEW_EXECUTABLE =
	//					"C:\\Users\\jrbibers\\Applications\\Ghostscript\\gsview\\gsview32.exe";
	//			ProcessBuilder gsProcess =new ProcessBuilder(
	//					FULL_PATH_TO_GSVIEW_EXECUTABLE, copy.getAbsolutePath());
	//			gsProcess.start();
				
				// Test with system's default PS handler
				Desktop.getDesktop().open(outFile);
			} catch (AlgorithmExecutionException e) {
				LogStream.ERROR.send(e);
			} catch (InstantiationException e) {
				LogStream.ERROR.send(e);
			} catch (IllegalAccessException e) {
				LogStream.ERROR.send(e);
			} catch (URISyntaxException e) {
				LogStream.ERROR.send(e);
			} catch (IOException e) {
				LogStream.ERROR.send(e);
			}
		}
		
		private void run(PageLayout pageLayout, KnownProjectedCRSDescriptor... explicitProjections) {
			Dictionary<String, Object> parameters =
					assembleParameters(shapefile, this.baseParameters);
			
			if (explicitProjections.length == 0) {
				run("geo-viz-", pageLayout, parameters);
			} else {
				for (KnownProjectedCRSDescriptor projection : explicitProjections) {
					Dictionary<String, Object> freshParameters =
							new Hashtable<String, Object>((Hashtable<String, Object>) parameters);
					freshParameters.put(Parameters.PROJECTION_ID, projection.getNiceName());
					run("geo-viz-" + projection.getNiceName() + "-", pageLayout, freshParameters);
				}
			}
		}

		private static Dictionary<String, Object> assembleParameters(
				Shapefile shapefile, ImmutableMap<String, Object> baseParameters) {
			Dictionary<String, Object> parameters =	new Hashtable<String, Object>();
			parameters.put(GeoMapsNetworkFactory.Parameter.SHAPEFILE_KEY.id(), shapefile.getNiceName());
			
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
				String dataLabel,
				Dictionary<String, Object> parameters)
						throws InstantiationException, IllegalAccessException, URISyntaxException,
								AlgorithmExecutionException {
			AlgorithmFactory algorithmFactory = algorithmFactoryClass.newInstance();
			Data[] prefuseTableData = convertToPrefuseTableData(csvFileURL);
			prefuseTableData[0].getMetadata().put(DataProperty.LABEL, dataLabel);
			
			CIShellContext ciContext = new CIShellContext() {
				@Override
				public Object getService(String service) {
					throw new AssertionError("This mock CIShellContext provides no services.");
				}
			};
			
			return algorithmFactory.createAlgorithm(prefuseTableData, parameters, ciContext);
		}
	}
}
