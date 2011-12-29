package edu.iu.sci2.visualization.geomaps;

import java.io.File;
import java.io.IOException;
import java.util.Dictionary;
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
import org.opengis.referencing.operation.TransformException;
import org.osgi.service.log.LogService;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.vividsolutions.jts.geom.Coordinate;

import edu.iu.nwb.util.nwbfile.NWBFileParser;
import edu.iu.nwb.util.nwbfile.NWBFileProperty;
import edu.iu.nwb.util.nwbfile.ParsingException;
import edu.iu.nwb.util.nwbfile.pipe.FieldMakerFunction;
import edu.iu.nwb.util.nwbfile.pipe.ParserPipe;
import edu.iu.nwb.util.nwbfile.pipe.ParserStage;
import edu.iu.sci2.visualization.geomaps.legend.LegendComponent;
import edu.iu.sci2.visualization.geomaps.metatype.Shapefiles;
import edu.iu.sci2.visualization.geomaps.printing.Circle;
import edu.iu.sci2.visualization.geomaps.printing.colorstrategy.NullColorStrategy;
import edu.iu.sci2.visualization.geomaps.printing.colorstrategy.StrokeColorStrategy;
import edu.iu.sci2.visualization.geomaps.projection.GeometryProjector;
import edu.iu.sci2.visualization.geomaps.utility.Constants;

public class GeoMapsNetwork implements Algorithm {

	private static final int ANCHOR_CIRCLE_SIZE = 5;
	private Data[] data;
	private Dictionary<String, Object> parameters;
	private CIShellContext ciShellContext;
	private LogService logger;
	private final String latitudeAttrib;
	private final String longitudeAttrib;
	private static final String outputAlgorithmName = "NetMap";
	private static final String IS_ANCHOR_FIELD = "isAnchor";
	private static final String X_POS_FIELD = "x";
	private static final String Y_POS_FIELD = "y";
	private ShapefileToPostScriptWriter postScriptWriter;
	private String shapefileKey;

	public GeoMapsNetwork(Data[] data, Dictionary<String, Object> parameters,
			CIShellContext ciShellContext)  {
		this.data = data;
		this.parameters = parameters;
		this.ciShellContext = ciShellContext;
		
		this.logger = (LogService) this.ciShellContext.getService(LogService.class.getName());
		
		this.latitudeAttrib = (String) parameters.get(GeoMapsNetworkFactory.Parameter.LATITUDE.getId());
		this.longitudeAttrib = (String) parameters.get(GeoMapsNetworkFactory.Parameter.LONGITUDE.getId());
		this.shapefileKey = (String) parameters.get(GeoMapsNetworkFactory.Parameter.SHAPEFILE_KEY.getId());
		
		if (latitudeAttrib.equals(longitudeAttrib)) {
			throw new AlgorithmCreationFailedException("Latitude and longitude attributes must be distinct");
		}
	}

	public Data[] execute() throws AlgorithmExecutionException {
		try {
			Data inDatum = this.data[0];
			File inFile = (File) inDatum.getData();

			
			postScriptWriter = Shapefiles.getPostScriptWriter(parameters);

			ImmutableMap<String, Coordinate> anchorPoints = getAnchorPoints(shapefileKey);
			drawAnchorPoints(postScriptWriter, anchorPoints.values());

			File geoMap = postScriptWriter.writePostScriptToFile("", "");

			File outNetwork = processNetwork(anchorPoints, inFile);

			Data[] outData = formOutData(geoMap, outNetwork, inDatum);

			return outData;
		} catch (TransformException e) {
			throw new AlgorithmExecutionException(
					"Error transforming features: " + e.getMessage(), e);
		} catch (IOException e) {
			throw new AlgorithmExecutionException(
					"Error creating PostScript file: " + e.getMessage(), e);
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
	private File processNetwork(ImmutableMap<String,Coordinate> anchorPoints,
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

			for (Map.Entry<String, Coordinate> anchor : anchorPoints.entrySet()) {
				pipe.injectNode(anchor.getKey(),
						ImmutableMap.of(
							this.longitudeAttrib, anchor.getValue().x,
							this.latitudeAttrib, anchor.getValue().y,
							GeoMapsNetwork.IS_ANCHOR_FIELD, 1)); // 1: yes, these are anchors
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
			
			public Object compute(String field, Map<String, Object> attributes) {
				Coordinate longLat = new Coordinate(getDoubleValue(attributes.get(longitudeAttrib)),
						getDoubleValue(attributes.get(latitudeAttrib)));
				Coordinate pagePosition = postScriptWriter.latLongToPagePosition(longLat);
				if (pagePosition == null) {
					return null;
				}
				return pagePosition.x;
			}
		};
		
	}
	
	private double getDoubleValue(Object o) {
		return ((Number) o).doubleValue();
	}
	
	private FieldMakerFunction getLatitudeToYComputer() {
		return new FieldMakerFunction() {
			
			public Object compute(String field, Map<String, Object> attributes) {
				Coordinate longLat = new Coordinate(getDoubleValue(attributes.get(longitudeAttrib)),
						getDoubleValue(attributes.get(latitudeAttrib)));
				Coordinate pagePosition = postScriptWriter.latLongToPagePosition(longLat);
				if (pagePosition == null) {
					// This is only in one of the two X/Y methods because both methods are called on
					// each incoming point, and everything gets computed twice.  :-(
					// So we don't want to double-log the info.
					logger.log(LogService.LOG_INFO, "Leaving out point at " + longLat.toString() + ": outside projection bounds");
					return null;
				}
				return pagePosition.y;
			}
		};
	}

	private Data[] formOutData(File postScriptFile, File outNetwork,
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

	private void drawAnchorPoints(ShapefileToPostScriptWriter postScriptWriter,
			ImmutableCollection<Coordinate> anchorPoints) {

		List<Circle> circles = Lists.newArrayList();
		for (Coordinate coord : anchorPoints) {
			circles.add(new Circle(coord, ANCHOR_CIRCLE_SIZE, new NullColorStrategy(),
					new StrokeColorStrategy()));
		}

		LegendComponent noLegend = null;
		postScriptWriter.setCircleAnnotations("", circles, noLegend, noLegend, noLegend);
	}

	private ImmutableMap<String, Coordinate> getAnchorPoints(String shapefileKey)
			throws AlgorithmExecutionException {
		if (shapefileKey.equals(Constants.COUNTRIES_SHAPEFILE_KEY)) {
			return ImmutableMap.of(
				"Near Alaska", new Coordinate(-179,
					89 - GeometryProjector.NORTH_POLE_CROP_HEIGHT_IN_DEGREES),
				"Near Antarctica", new Coordinate(179, -89
					+ GeometryProjector.SOUTH_POLE_CROP_HEIGHT_IN_DEGREES));
		} else if (shapefileKey.equals(Constants.US_STATES_SHAPEFILE_KEY)) {
			return ImmutableMap.of(
				"Near Aleutian Islands", new Coordinate(-179, 50),
				"Near Puerto Rico", new Coordinate(-64, 16));
		} else {
			throw new AlgorithmExecutionException(
					String.format("No anchor points found for map %s", shapefileKey));
		}
	}

	public static void main(String[] args) {
//		PositionFactory.createPointArray();
	}
}
