package edu.iu.sci2.visualization.geomaps.viz.ps;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.cishell.utilities.FileUtilities;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.referencing.operation.TransformException;
import org.osgi.service.log.LogService;

import com.google.common.base.Optional;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

import edu.iu.sci2.visualization.geomaps.GeoMapsAlgorithm;
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
		File psFile =
			FileUtilities.createTemporaryFileInDefaultTemporaryDirectory("geoMaps", GeoMapsAlgorithm.OUTPUT_FILE_EXTENSION);
		
		BufferedWriter out = new BufferedWriter(new FileWriter(psFile));

		writeCodeHeader(out, psFile.getName(), pageLayout);
		
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
		
		FeaturePrinter featurePrinter =
			new FeaturePrinter(
					geoMap.getShapefile(),
					geoMap.getShapefile().viewOfFeatureCollection(),
					geoMap.getGeometryProjector(),
					geoMapViewPageArea,
					geoMap.getShapefile().getFeatureAttributeName());
		featurePrinter.printFeatures(out, geoMap.getFeatureViews());

		out.write(GeoMapsAlgorithm.TEMPLATE_GROUP.getInstanceOf("circlePrinterDefinitions").toString());
		
		
		out.write("% Circle annotations" + "\n");		
		out.write("gsave" + "\n");
		out.write("\n");
		
		double circleLineWidth = Circle.DEFAULT_CIRCLE_LINE_WIDTH;
		out.write(INDENT + circleLineWidth + " setlinewidth" + "\n");
		out.write("\n");

		for (Circle circle : geoMap.getCircles()) {
			out.write(circle.toPostScript(geoMap.getGeometryProjector(), geoMapViewPageArea));
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
		Calendar cal = Calendar.getInstance();
	    SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy | hh:mm:ss aa");
	    return sdf.format(cal.getTime());
	}

	/**
	 * Given a latitude and longitude in a {@link Coordinate} object, projects it onto the
	 * current map as well as possible.
	 * @throws TransformException 
	 */
	public Point2D.Double coordinateToPagePoint(Coordinate coordinate) throws TransformException {
		return geoMapViewPageArea.displayPointFor(geoMap.project(coordinate));
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
			Geometry geometry;
			try {				
				geometry = geoMap.project(
						geoMap.getShapefile().inset(
								featureName,
								(Geometry) feature.getDefaultGeometry()));
			} catch (IllegalArgumentException e) {
				// TODO Is there a way to repair the geometry?  Can't even reliably reproduce the problem..
				/* This seems to happen intermittently with version 2.7.4 of geolibs/Geotools for
				 * one subgeometry of Minnesota in Shapefile.UNITED_STATES. */
				System.err.println(String.format(
						"Skipping a geometry of feature %s due to IllegalArgumentException during " +
						"projection (%s).",
						featureName, e.getMessage()));
				continue;
			}

			for (int gg = 0; gg < geometry.getNumGeometries(); gg++) {
				Geometry subgeometry = geometry.getGeometryN(gg);

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
		it.close();
		
		return rectangle;
	}

	private static void writeCodeHeader(
			BufferedWriter out, String outputPSFileName, PageLayout pageLayout) throws IOException {
		GeoMapsAlgorithm.logger.log(LogService.LOG_INFO, "Printing PostScript.." + "\n");

		out.write((new DSCProlog(outputPSFileName, pageLayout.pageDimensions()).toPostScript()));
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