package edu.iu.sci2.visualization.geomaps.viz.ps;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

import org.cishell.utilities.FileUtilities;
import org.geotools.feature.FeatureIterator;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.referencing.operation.TransformException;
import org.osgi.service.log.LogService;

import com.google.common.base.Optional;
import com.google.common.collect.Iterables;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

import edu.iu.sci2.visualization.geomaps.GeoMapsAlgorithm;
import edu.iu.sci2.visualization.geomaps.geo.shapefiles.Shapefile.Inset;
import edu.iu.sci2.visualization.geomaps.viz.Circle;
import edu.iu.sci2.visualization.geomaps.viz.PageLayout;
import edu.iu.sci2.visualization.geomaps.viz.model.GeoMap;

public class GeoMapViewPS {
	/* Percentage of the data range to add to each side of the map as a buffer.
	 * Between 0 and 1.
	 */
	public static final double MAP_BOUNDING_BOX_BUFFER_RATIO = 0.1;
	public static final String INDENT = "  ";
	
	private final GeoMap geoMap;
	private final PageLayout pageLayout;
	private final Optional<HowToRead> howToRead;
	private final GeoMapViewPageArea geoMapViewPageArea;

	public GeoMapViewPS(GeoMap geoMap, PageLayout pageLayout, Optional<HowToRead> howToRead) throws ShapefilePostScriptWriterException {
		try {
			this.geoMap = geoMap;
			this.pageLayout = pageLayout;
			this.howToRead = howToRead;
			
			this.geoMapViewPageArea = new GeoMapViewPageArea(calculateMapBoundingRectangle(), pageLayout);
		} catch (TransformException e) {
			throw new ShapefilePostScriptWriterException(e);
		}
	}

	
	public File writeToPSFile(String dataLabel)
				throws IOException, TransformException {		
		File psFile = FileUtilities.createTemporaryFileInDefaultTemporaryDirectory(
				"geoMaps", GeoMapsAlgorithm.OUTPUT_FILE_EXTENSION);
		
		BufferedWriter out = new BufferedWriter(new FileWriter(psFile));

		GeoMapsAlgorithm.logger.log(LogService.LOG_INFO, "Printing PostScript.." + "\n");
		
		out.write((new DSCProlog(geoMap.getTitle(), pageLayout.pageDimensions()).toPostScript()));
		
		out.write(GeoMapsAlgorithm.TEMPLATE_GROUP.getInstanceOf("utilityDefinitions").toString());
		out.write("\n");
		
		out.write((new PageFooter(new Point2D.Double(
				pageLayout.pageDimensions().getWidth() / 2.0,
				0.75 * pageLayout.pageMargin()),
				pageLayout)).toPostScript() + "\n");
		
		out.write("% Save the default clipping path so we can clip the map safely" + "\n");
		out.write("gsave" + "\n");
		out.write("\n");

		out.write(geoMapViewPageArea.toPostScript());
		out.write("\n");
		
		FeaturePrinter featurePrinter = new FeaturePrinter(geoMap, geoMapViewPageArea);
		featurePrinter.printFeatures(out, geoMap.getFeatureViews());
		
		// Label insets
		for (Inset inset : geoMap.getInsets()) {
			Coordinate labelLowerLeftCoordinate = inset.getLabelLowerLeft();
			Coordinate projectedAndInset = geoMap.projectUsingInset(labelLowerLeftCoordinate, inset);
			Point2D.Double labelLowerLeftPoint = geoMapViewPageArea.displayPointFor(projectedAndInset);
			out.write(" gsave ");
			out.write(PSUtility.findscalesetfont(pageLayout.contentFont()));
			out.write(PSUtility.xy(labelLowerLeftPoint) + " moveto ");
			out.write(String.format(" (%s) show ", inset.makeLabel()));
			out.write(" grestore ");
		}
		

		out.write(GeoMapsAlgorithm.TEMPLATE_GROUP.getInstanceOf("circlePrinterDefinitions").toString());

		out.write("% Circle annotations" + "\n");		
		out.write("gsave" + "\n");
		out.write("\n");
		
		double circleLineWidth = Circle.DEFAULT_CIRCLE_LINE_WIDTH;
		out.write(INDENT + circleLineWidth + " setlinewidth" + "\n");
		out.write("\n");

		for (Circle circle : geoMap.getCircles()) {
			out.write(circle.toPostScript(geoMap, geoMapViewPageArea));
		}

		out.write("grestore" + "\n");
		out.write("\n");
		
		
		out.write("% Restore the default clipping path" + "\n");
		out.write("grestore" + "\n");
		out.write("\n");
		
		
		if (pageLayout.headerLowerLeft().isPresent()) {
			PageHeader pageHeader = new PageHeader(geoMap.getTitle(), pageLayout.headerLowerLeft().get(), pageLayout,
					String.format("Generated from %s", PSUtility.escapeForPostScript(dataLabel)),
					timestamp());
			out.write(pageHeader.toPostScript());
			out.write("\n");
		}
		
		out.write(geoMap.getLegendarium().toPostScript());
		out.write("\n");
		
		if (howToRead.isPresent()) {
			out.write(howToRead.get().toPostScript());
		}
		
		out.write("showpage" + "\n");

		out.close();

		GeoMapsAlgorithm.logger.log(LogService.LOG_INFO, "Done.");
		
		return psFile;
	}
	
	private static String timestamp() {
		return DateTimeFormat.forPattern("MMM dd, yyyy | hh:mm:ss aa z").print(new DateTime());
	}

	/**
	 * Given a latitude and longitude in a {@link Coordinate} object, projects it onto the
	 * current map as well as possible.
	 * @throws TransformException	When the underlying transform fails.
	 */
	public Point2D.Double coordinateToPagePoint(Coordinate coordinate) throws TransformException {
		Collection<Coordinate> projectedCoordinates = geoMap.projectAndInset(coordinate);
		
		if (projectedCoordinates.isEmpty()) {
			throw new RuntimeException(String.format(
					"The coordinate \"%s\" was not projected to any display point.", coordinate));
		}
		Coordinate aProjectedCoordinate = Iterables.getFirst(projectedCoordinates, null);
		
		if (projectedCoordinates.size() > 1) {
			GeoMapsAlgorithm.logger.log(
					LogService.LOG_WARNING,
					String.format(
							"The coordinate \"%s\" projected to multiple display points.  " +
							"This can happen if the insets defined for this base map indicate " +
							"overlapping bounding boxes.  " +
							"A point has been chosen arbitrarily from the possibilities.",
							coordinate));
		}
		
		return geoMapViewPageArea.displayPointFor(aProjectedCoordinate);
	}
	
	/**
	 * Identify extreme values for the X and Y dimensions among the projected features from our featureCollection.
	 * Note that this is <em>after</em> Geometry preparation (cropping and projecting).
	 */	
	private Rectangle2D.Double calculateMapBoundingRectangle() throws TransformException {	
		Rectangle2D.Double rectangle = null;

		FeatureIterator<SimpleFeature> it = geoMap.getShapefile().viewOfFeatureCollection().features();
		while (it.hasNext()) {
			SimpleFeature feature = it.next();
			String featureName = geoMap.getShapefile().extractFeatureName(feature);
			Collection<Geometry> geometries;
			try {
				geometries = geoMap.projectAndInset((Geometry) feature.getDefaultGeometry());
			} catch (IllegalArgumentException e) { // TODO Still necessary?
				/* This seems to happen intermittently with version 2.7.4 of geolibs/Geotools for
				 * one subgeometry of Minnesota in Shapefile.UNITED_STATES. */
				System.err.println(String.format(
						"Skipping a geometry of feature %s due to IllegalArgumentException during " +
						"projection (%s).",
						featureName, e.getMessage()));
				continue;
			}

			for (Geometry geometry : geometries) {
				for (int ii = 0; ii < geometry.getNumGeometries(); ii++) {
					Geometry subgeometry = geometry.getGeometryN(ii);
					
					Coordinate[] coordinates = subgeometry.getCoordinates();
					
					for (Coordinate coordinate : coordinates) {
						Point2D.Double point = new Point2D.Double(coordinate.x, coordinate.y);
						
						if (rectangle == null) {
							rectangle = new Rectangle2D.Double(point.x, point.y, 0, 0);
						}
						
						rectangle.add(point);
					}
				}
			}
		}
		it.close();
		
		return rectangle;
	}

	public static class ShapefilePostScriptWriterException extends Exception {
		private static final long serialVersionUID = -4207770884445237065L;
	
		public ShapefilePostScriptWriterException(String message, Throwable cause) {
			super(message, cause);
		}

		public ShapefilePostScriptWriterException(Throwable cause) {
			super(cause);
		}
	}
}