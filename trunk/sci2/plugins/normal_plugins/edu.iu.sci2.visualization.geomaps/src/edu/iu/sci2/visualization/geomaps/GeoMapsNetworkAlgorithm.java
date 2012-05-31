package edu.iu.sci2.visualization.geomaps;

import java.awt.Desktop;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.Dictionary;
import java.util.EnumMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmCreationFailedException;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.utilities.DataFactory;
import org.cishell.utilities.NumberUtilities;
import org.geotools.factory.FactoryRegistryException;
import org.opengis.referencing.operation.TransformException;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.vividsolutions.jts.geom.Coordinate;

import edu.iu.nwb.util.nwbfile.NWBFileParser;
import edu.iu.nwb.util.nwbfile.NWBFileProperty;
import edu.iu.nwb.util.nwbfile.ParsingException;
import edu.iu.nwb.util.nwbfile.pipe.FieldMakerFunction;
import edu.iu.nwb.util.nwbfile.pipe.ParserPipe;
import edu.iu.nwb.util.nwbfile.pipe.ParserStage;
import edu.iu.sci2.visualization.geomaps.geo.projection.KnownProjectedCRSDescriptor;
import edu.iu.sci2.visualization.geomaps.geo.shapefiles.Shapefile;
import edu.iu.sci2.visualization.geomaps.geo.shapefiles.Shapefile.AnchorPoint;
import edu.iu.sci2.visualization.geomaps.metatype.Parameters;
import edu.iu.sci2.visualization.geomaps.utility.NicelyNamedEnums;
import edu.iu.sci2.visualization.geomaps.viz.Circle;
import edu.iu.sci2.visualization.geomaps.viz.CircleDimension;
import edu.iu.sci2.visualization.geomaps.viz.FeatureView;
import edu.iu.sci2.visualization.geomaps.viz.PageLayout;
import edu.iu.sci2.visualization.geomaps.viz.legend.LabeledReference;
import edu.iu.sci2.visualization.geomaps.viz.model.GeoMap;
import edu.iu.sci2.visualization.geomaps.viz.model.GeoMapException;
import edu.iu.sci2.visualization.geomaps.viz.ps.GeoMapViewPS;
import edu.iu.sci2.visualization.geomaps.viz.ps.GeoMapViewPS.ShapefilePostScriptWriterException;
import edu.iu.sci2.visualization.geomaps.viz.ps.HowToRead;
import edu.iu.sci2.visualization.geomaps.viz.strategy.CircleAreaStrategy;
import edu.iu.sci2.visualization.geomaps.viz.strategy.NullColorStrategy;
import edu.iu.sci2.visualization.geomaps.viz.strategy.Strategy;
import edu.iu.sci2.visualization.geomaps.viz.strategy.StrokeColorStrategy;

public class GeoMapsNetworkAlgorithm implements Algorithm {
	private static final int ANCHOR_CIRCLE_SIZE = 5;
	private static final String IS_ANCHOR_FIELD = "isAnchor";
	private static final String X_POS_FIELD = "x";
	private static final String Y_POS_FIELD = "y";
	
	private final Data[] data;
	private final String latitudeAttrib;
	private final String longitudeAttrib;
	private final Shapefile shapefile;
	private final KnownProjectedCRSDescriptor projection;

	public GeoMapsNetworkAlgorithm(Data[] data, Dictionary<String, Object> parameters)  {
		this.data = data;
		
		this.latitudeAttrib =
				(String) parameters.get(GeoMapsNetworkFactory.Parameter.LATITUDE.id());
		this.longitudeAttrib =
				(String) parameters.get(GeoMapsNetworkFactory.Parameter.LONGITUDE.id());
		
		this.shapefile = NicelyNamedEnums.getConstantNamed(
				Shapefile.class,
				(String) parameters.get(GeoMapsNetworkFactory.Parameter.SHAPEFILE_KEY.id()));
		
		this.projection = shapefile.getDefaultProjectedCrs();
		if (Parameters.LET_USER_CHOOSE_PROJECTION) {
			this.projection = NicelyNamedEnums.getConstantNamed(
					KnownProjectedCRSDescriptor.class,
					(String) parameters.get(Parameters.PROJECTION_ID));
		}
		
		if (latitudeAttrib.equals(longitudeAttrib)) {
			throw new AlgorithmCreationFailedException(
					"Latitude and longitude attributes must be distinct.");
		}
	}
	
	static PageLayout getPageLayout() {
		return PageLayout.PRINT;
	}

	@Override
	public Data[] execute() throws AlgorithmExecutionException {
		try {
			Data inDatum = this.data[0];
			File inFile = (File) inDatum.getData();

			ImmutableCollection<AnchorPoint> anchorPoints = shapefile.getAnchorPoints();
			List<Circle> anchorPointsAsCircles = drawAnchorPoints(anchorPoints);
			
			GeoMap geoMap = new GeoMap(
					"Networks",
					shapefile,
					projection,
					ImmutableSet.<FeatureView>of(),
					anchorPointsAsCircles,
					ImmutableSet.<LabeledReference>of(),
					getPageLayout(),
					Optional.<HowToRead>absent()); // TODO How to read for Networks?
			
			GeoMapViewPS geoMapView = new GeoMapViewPS(geoMap, getPageLayout());
			
			File outNetwork = processNetwork(inFile, anchorPoints, geoMapView);

			File geoMapFile = geoMapView.writeToPSFile("", GeoMapsAlgorithm.OUTPUT_FILE_EXTENSION);
			
			return new Data[] {
					DataFactory.forFile(outNetwork, NWBFileProperty.NWB_MIME_TYPE,
							DataProperty.NETWORK_TYPE, inDatum, "Laid out network"),
					DataFactory.forFile(geoMapFile, GeoMapsAlgorithm.POSTSCRIPT_MIME_TYPE,
							DataProperty.VECTOR_IMAGE_TYPE, inDatum, "Base map with anchor points") };
		} catch (TransformException e) {
			throw new AlgorithmExecutionException(
					"Error transforming features: " + e.getMessage(), e);
		} catch (IOException e) {
			throw new AlgorithmExecutionException(
					"Error creating PostScript file: " + e.getMessage(), e);
		} catch (ShapefilePostScriptWriterException e) {
			throw new AlgorithmExecutionException("Error visualizing geo map: " + e.getMessage(), e);
		} catch (FactoryRegistryException e) {
			throw new AlgorithmExecutionException("Geography error: " + e.getMessage(), e);
		} catch (GeoMapException e) {
			throw new AlgorithmExecutionException("Error creating geo map: " + e.getMessage(), e);
		}
	}

	/**
	 * Adds the anchor points to the network, and replaces the latitude and longitude attributes
	 * with new x and y position attributes ([XY}_POS_FIELD).
	 * @param inFile
	 * @param anchorPoints
	 * @param geoMapView 
	 * @return
	 * @throws AlgorithmExecutionException
	 */
	private File processNetwork(File inFile, Collection<AnchorPoint> anchorPoints,
			GeoMapViewPS geoMapView) throws AlgorithmExecutionException {
		try {
			File outFile = edu.iu.nwb.util.nwbfile.NWBFileUtilities
					.createTemporaryNWBFile();
			
			/*
			 * Here we construct a chain of "parser handlers".  The bottom layer is one that writes
			 * to our output file.  Above that are layers that add new fields to the output, etc.
			 * 
			 * At the end, we call NWBFileParser.parse(chain), which will cause the data in the
			 * input file to be processed through each step of the chain before being written to
			 * the output file.
			 */
			
			ParserPipe pipe = ParserPipe.create();

			// 0: default: not an anchor
			pipe.addNodeAttribute(IS_ANCHOR_FIELD, NWBFileProperty.TYPE_INT, 0);
			
			for (AnchorPoint anchorPoint : anchorPoints) {
				pipe.injectNode(
						anchorPoint.getDisplayName(),
						ImmutableMap.of(
								longitudeAttrib, anchorPoint.getCoordinate().x,
								latitudeAttrib, anchorPoint.getCoordinate().y,
								GeoMapsNetworkAlgorithm.IS_ANCHOR_FIELD, 1)); // 1: yes, these are anchors
			}
			
			pipe.addComputedNodeAttributes(
					ImmutableMap.of(
							X_POS_FIELD, NWBFileProperty.TYPE_FLOAT,
							Y_POS_FIELD, NWBFileProperty.TYPE_FLOAT),
					new LayoutFieldMakerFunction(geoMapView, longitudeAttrib, latitudeAttrib));

			new NWBFileParser(inFile).parse(pipe.outputToFile(outFile));
			
			return outFile;
		} catch (IOException e) {
			throw new AlgorithmExecutionException("Error creating or parsing NWB file: "
					+ e.getMessage(), e);
		} catch (ParsingException e) {
			throw new AlgorithmExecutionException("Error parsing NWB file: "
					+ e.getMessage(), e);
		}
	}

	
	private static List<Circle> drawAnchorPoints(Collection<AnchorPoint> anchorPoints) {
		List<Circle> circles = Lists.newArrayList();
		for (AnchorPoint anchorPoint : anchorPoints) {
			EnumMap<CircleDimension, Strategy> strategies = Maps.newEnumMap(CircleDimension.class);
			strategies.put(CircleDimension.AREA, CircleAreaStrategy.forArea(ANCHOR_CIRCLE_SIZE));
			strategies.put(CircleDimension.INNER_COLOR, new NullColorStrategy());
			strategies.put(CircleDimension.OUTER_COLOR, StrokeColorStrategy.theDefault());
			
			circles.add(new Circle(anchorPoint.getCoordinate(), strategies));
		}
		
		return circles;
	}
	
	
	public static void main(String[] args) {
		Example.NETWORK.run();
	}
	
	
	private static final class LayoutFieldMakerFunction implements FieldMakerFunction {
		private final GeoMapViewPS geoMapView;
		private final String longitudeAttrib;
		private final String latitudeAttrib;
		
		private LayoutFieldMakerFunction(
				GeoMapViewPS geoMapView, String longitudeAttrib, String latitudeAttrib) {
			this.geoMapView = geoMapView;
			this.longitudeAttrib = longitudeAttrib;
			this.latitudeAttrib = latitudeAttrib;
		}


		@Override
		public Map<String, Object> compute(Map<String, Object> attributes) {
			Preconditions.checkNotNull(attributes.get(longitudeAttrib));
			Preconditions.checkNotNull(attributes.get(latitudeAttrib));
			Coordinate longLat = new Coordinate(
					NumberUtilities.interpretObjectAsDouble(attributes.get(longitudeAttrib)),
					NumberUtilities.interpretObjectAsDouble(attributes.get(latitudeAttrib)));

			try {
				Point2D.Double pagePoint =
						geoMapView.transformAndInsetToPagePoint(longLat);
				
				return ImmutableMap.<String, Object>of(
						X_POS_FIELD, pagePoint.x,
						Y_POS_FIELD, pagePoint.y);
			} catch (TransformException e) {
				LogStream.WARNING.send(e, "Transform failure laying out node at coordinate %s.",
						longLat);
				
				return ImmutableMap.of();
			}
		}
	}
	
	
	private static final String EXAMPLE_FILE_URL_STEM =
			"/edu/iu/sci2/visualization/geomaps/testing/";
	private enum Example {
		NETWORK(
				Shapefile.WORLD,
				GeoMapsNetworkAlgorithm.class.getResource(
						EXAMPLE_FILE_URL_STEM + "Network I 00-02.net.nwb"),
				"network",
				GeoMapsNetworkFactory.class,
				ImmutableMap.<String, Object>builder()
						.put(GeoMapsNetworkFactory.Parameter.LATITUDE.id(), "latitude")
						.put(GeoMapsNetworkFactory.Parameter.LONGITUDE.id(), "longitude")
						.build()
				);
		
		private final Shapefile shapefile;
		private final URL networkFileURL;
		private final String dataLabel;
		private final Class<? extends AlgorithmFactory> algorithmFactoryClass;
		private final ImmutableMap<String, Object> baseParameters;

		private Example(
				Shapefile shapefile,
				URL networkFileURL,
				String dataLabel,
				Class<? extends AlgorithmFactory> algorithmFactoryClass,
				ImmutableMap<String, Object> baseParameters) {
			this.shapefile = shapefile;
			this.networkFileURL = networkFileURL;
			this.dataLabel = dataLabel;
			this.algorithmFactoryClass = algorithmFactoryClass;
			this.baseParameters = baseParameters;
		}
		
		private void run(String outputFilenamePrefix, Dictionary<String, Object> parameters) {
			try {
				LogStream.DEBUG.send("parameters are " + parameters);
				Algorithm algorithm;
				algorithm = createAlgorithm(algorithmFactoryClass, networkFileURL, dataLabel,
						parameters);

				LogStream.DEBUG.send("Executing.. ");
				Data[] outData = algorithm.execute();
				File psFileWithRawName = (File) outData[0].getData();
				File psFile = File.createTempFile(outputFilenamePrefix, ".ps");
				Files.copy(psFileWithRawName, psFile);
				LogStream.DEBUG.send(psFile.getAbsolutePath());

				File nwbFileWithRawName = (File) outData[1].getData();
				File nwbFile = File.createTempFile(outputFilenamePrefix, ".nwb");
				Files.copy(nwbFileWithRawName, nwbFile);
				LogStream.DEBUG.send(nwbFile.getAbsolutePath());

				LogStream.DEBUG.send(".. Done.");

				Desktop.getDesktop().open(nwbFile);
			} catch (InstantiationException e) {
				LogStream.ERROR.send(e);
			} catch (IllegalAccessException e) {
				LogStream.ERROR.send(e);
			} catch (URISyntaxException e) {
				LogStream.ERROR.send(e);
			} catch (AlgorithmExecutionException e) {
				LogStream.ERROR.send(e);
			} catch (IOException e) {
				LogStream.ERROR.send(e);
			}
		}
		
		private void run(KnownProjectedCRSDescriptor... explicitProjections) {
			Dictionary<String, Object> parameters =
					assembleParameters(shapefile, this.baseParameters);
			
			if (explicitProjections.length == 0) {
				run("geo-viz-", parameters);
			} else {
				for (KnownProjectedCRSDescriptor projection : explicitProjections) {
					Dictionary<String, Object> freshParameters =
							new Hashtable<String, Object>((Hashtable<String, Object>) parameters);
					freshParameters.put(Parameters.PROJECTION_ID, projection.getNiceName());
					run("geo-viz-" + projection.getNiceName() + "-", freshParameters);
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

		private static Algorithm createAlgorithm(
				Class<? extends AlgorithmFactory> algorithmFactoryClass,
				URL networkFileURL,
				String dataLabel,
				Dictionary<String, Object> parameters)
						throws InstantiationException, IllegalAccessException, URISyntaxException {
			AlgorithmFactory algorithmFactory = algorithmFactoryClass.newInstance();
			Data[] networkData = new Data[] { new BasicData(new File(networkFileURL.toURI()),
					"file:text/nwb") };
			networkData[0].getMetadata().put(DataProperty.LABEL, dataLabel);
			
			CIShellContext ciContext = new CIShellContext() { // TODO Replace with a better mock
				@Override
				public Object getService(String service) {
					return null;
				}
			};
			
			return algorithmFactory.createAlgorithm(networkData, parameters, ciContext);
		}
	}
}
