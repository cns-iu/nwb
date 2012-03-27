package edu.iu.sci2.visualization.geomaps;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Dictionary;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmCreationFailedException;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.utilities.FileUtilities;
import org.geotools.factory.FactoryRegistryException;
import org.opengis.referencing.operation.TransformException;
import org.osgi.service.log.LogService;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
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
	private static final String outputAlgorithmName = "NetMap";
	private static final String IS_ANCHOR_FIELD = "isAnchor";
	private static final String X_POS_FIELD = "x";
	private static final String Y_POS_FIELD = "y";
	
	private final Data[] data;
	private final Dictionary<String, Object> parameters;
	private final CIShellContext ciShellContext;
	private final LogService logger;
	private final String latitudeAttrib;
	private final String longitudeAttrib;
	private final Shapefile shapefile;
	
	private GeoMapViewPS postScriptWriter;

	public GeoMapsNetworkAlgorithm(Data[] data, Dictionary<String, Object> parameters,
			CIShellContext ciShellContext)  {
		this.data = data;
		this.parameters = parameters;
		this.ciShellContext = ciShellContext;
		
		this.logger = (LogService) this.ciShellContext.getService(LogService.class.getName());
		
		this.latitudeAttrib = (String) parameters.get(GeoMapsNetworkFactory.Parameter.LATITUDE.id());
		this.longitudeAttrib = (String) parameters.get(GeoMapsNetworkFactory.Parameter.LONGITUDE.id());
		this.shapefile = NicelyNamedEnums.getConstantNamed(
				Shapefile.class,
				(String) parameters.get(GeoMapsNetworkFactory.Parameter.SHAPEFILE_KEY.id()));
		
		if (latitudeAttrib.equals(longitudeAttrib)) {
			throw new AlgorithmCreationFailedException("Latitude and longitude attributes must be distinct");
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
			
			KnownProjectedCRSDescriptor knownProjectedCRSDescriptor = shapefile.getDefaultProjectedCrs();
			if (Parameters.LET_USER_CHOOSE_PROJECTION) {
				knownProjectedCRSDescriptor = NicelyNamedEnums.getConstantNamed(
						KnownProjectedCRSDescriptor.class,
						(String) parameters.get(Parameters.PROJECTION_ID));
			}

			ImmutableCollection<AnchorPoint> anchorPoints = shapefile.getAnchorPoints();
			List<Circle> anchorPointsAsCircles = drawAnchorPoints(anchorPoints);

			GeoMap geoMap = new GeoMap(
					"Networks",
					shapefile,
					knownProjectedCRSDescriptor,
					ImmutableSet.<FeatureView>of(),
					anchorPointsAsCircles,
					ImmutableSet.<LabeledReference>of(),
					getPageLayout());
			
			// TODO How to read for Networks?
			postScriptWriter = new GeoMapViewPS(geoMap, getPageLayout(), Optional.<HowToRead>absent());
			
			File geoMapFile = postScriptWriter.writeToPSFile("");

			File outNetwork = processNetwork(anchorPoints, inFile);

			Data[] outData = formOutData(geoMapFile, outNetwork, inDatum);

			return outData;
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
	 * @param anchorPoints
	 * @param inFile
	 * @return
	 * @throws AlgorithmExecutionException
	 */
	private File processNetwork(Collection<AnchorPoint> anchorPoints,
			File inFile) throws AlgorithmExecutionException {
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

			for (AnchorPoint anchorPoint : anchorPoints) {
				pipe.injectNode(anchorPoint.getDisplayName(),
						ImmutableMap.of(
							this.longitudeAttrib, anchorPoint.getCoordinate().x,
							this.latitudeAttrib, anchorPoint.getCoordinate().y,
							GeoMapsNetworkAlgorithm.IS_ANCHOR_FIELD, 1)); // 1: yes, these are anchors
			}
			
			ParserStage handler = pipe.addNodeAttribute(IS_ANCHOR_FIELD, NWBFileProperty.TYPE_INT, 0) // 0: default: not an anchor
				.addComputedNodeAttribute(Y_POS_FIELD, NWBFileProperty.TYPE_FLOAT, getLatitudeToYComputer())
				.addComputedNodeAttribute(X_POS_FIELD, NWBFileProperty.TYPE_FLOAT, getLongitudeToXComputer())
				.removeNodeAttribute(this.longitudeAttrib)
				.removeNodeAttribute(this.latitudeAttrib)
				.outputToFile(outFile);

			NWBFileParser parser = new NWBFileParser(inFile);
			parser.parse(handler);
			
			return outFile;
		} catch (IOException e) {
			throw new AlgorithmExecutionException("Error creating or parsing NWB file: "
					+ e.getMessage(), e);
		} catch (ParsingException e) {
			throw new AlgorithmExecutionException("Error parsing NWB file: "
					+ e.getMessage(), e);
		}
	}

	private FieldMakerFunction getLongitudeToXComputer() {
		return new FieldMakerFunction() {			
			@Override
			public Object compute(String field, Map<String, Object> attributes) {
				Coordinate longLat = new Coordinate(getDoubleValue(attributes.get(longitudeAttrib)),
						getDoubleValue(attributes.get(latitudeAttrib)));
				try {
					Point2D.Double pagePoint = postScriptWriter.coordinateToPagePoint(longLat);
					return pagePoint.x;
				} catch (TransformException e) {
					return null;
				}
			}
		};
		
	}
	
	private static double getDoubleValue(Object o) {
		return ((Number) o).doubleValue();
	}
	
	private FieldMakerFunction getLatitudeToYComputer() {
		return new FieldMakerFunction() {
			
			@Override
			public Object compute(String field, Map<String, Object> attributes) {
				Coordinate longLat = new Coordinate(getDoubleValue(attributes.get(longitudeAttrib)),
						getDoubleValue(attributes.get(latitudeAttrib)));
				try {
					Point2D.Double pagePoint = postScriptWriter.coordinateToPagePoint(longLat);
					return pagePoint.y;
				} catch (TransformException e) {
					// This is only in one of the two X/Y methods because both methods are called on
					// each incoming point, and everything gets computed twice.  :-(
					// So we don't want to double-log the info.
					logger.log(LogService.LOG_INFO,
							"Leaving out point at " + longLat.toString() +
							": outside projection bounds");
					return null;
				}
			}
		};
	}

	private static Data[] formOutData(File postScriptFile, File outNetwork,
			Data inDatum) {
		Dictionary<String, Object> inMetaData = inDatum.getMetadata();

		Data postScriptData = new BasicData(postScriptFile,
				GeoMapsAlgorithm.POSTSCRIPT_MIME_TYPE);

		Dictionary<String, Object> postScriptMetaData = postScriptData
				.getMetadata();

		String inFileDisplayName = FileUtilities.extractFileName(inMetaData
				.get(DataProperty.LABEL).toString());
		postScriptMetaData.put(DataProperty.LABEL, outputAlgorithmName
				+ "_map_" + inFileDisplayName);
		postScriptMetaData.put(DataProperty.PARENT, inDatum);
		postScriptMetaData.put(DataProperty.TYPE,
				DataProperty.VECTOR_IMAGE_TYPE);

		Data nwbData = new BasicData(outNetwork, "file:text/nwb");
		Dictionary<String, Object> nwbMetaData = nwbData.getMetadata();
		nwbMetaData.put(DataProperty.LABEL, outputAlgorithmName + "_net_"
				+ inFileDisplayName);
		nwbMetaData.put(DataProperty.PARENT, inDatum);
		nwbMetaData.put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);

		return new Data[] { postScriptData, nwbData };
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
}
