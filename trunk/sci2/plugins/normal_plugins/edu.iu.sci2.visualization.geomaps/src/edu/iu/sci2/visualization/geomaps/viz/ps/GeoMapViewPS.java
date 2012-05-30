package edu.iu.sci2.visualization.geomaps.viz.ps;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;

import org.antlr.stringtemplate.StringTemplateGroup;
import org.cishell.utilities.FileUtilities;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.opengis.referencing.operation.TransformException;

import com.google.common.collect.Iterables;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Point;

import edu.iu.sci2.visualization.geomaps.GeoMapsAlgorithm;
import edu.iu.sci2.visualization.geomaps.LogStream;
import edu.iu.sci2.visualization.geomaps.geo.shapefiles.Shapefile.Inset;
import edu.iu.sci2.visualization.geomaps.viz.Circle;
import edu.iu.sci2.visualization.geomaps.viz.PageLayout;
import edu.iu.sci2.visualization.geomaps.viz.model.GeoMap;

public class GeoMapViewPS {
	static final StringTemplateGroup TEMPLATE_GROUP = loadTemplates();

	private static final String STRING_TEMPLATE_RESOURCE_PATH =
			"/edu/iu/sci2/visualization/geomaps/viz/stringtemplates/geomap.stg";
	
	private final GeoMap geoMap;
	private final PageLayout pageLayout;
	private final GeoMapViewPageArea geoMapViewPageArea;


	public GeoMapViewPS(GeoMap geoMap, PageLayout pageLayout) throws ShapefilePostScriptWriterException {
		try {
			this.geoMap = geoMap;
			this.pageLayout = pageLayout;
			
			Rectangle2D.Double dataRectangle = geoMap.calculateMapBoundingRectangle();
			Rectangle2D.Double displayRectangle = pageLayout.calculateMapRectangle(dataRectangle);
			
			this.geoMapViewPageArea = new GeoMapViewPageArea(dataRectangle, displayRectangle);
		} catch (TransformException e) {
			throw new ShapefilePostScriptWriterException(e);
		}
	}

	
	public File writeToPSFile(String dataLabel, String fileExtension)
				throws IOException, TransformException {		
		File psFile = FileUtilities.createTemporaryFileInDefaultTemporaryDirectory(
				"geoMaps", fileExtension);
		
		BufferedWriter out = new BufferedWriter(new FileWriter(psFile));
		try {
			LogStream.INFO.send("Printing PostScript.." + "\n");
			
			out.write((new DSCProlog(geoMap.getTitle(), pageLayout.pageDimensions()).toPostScript()));
			
			out.write(GeoMapViewPS.TEMPLATE_GROUP.getInstanceOf("utilityDefinitions").toString());
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
				Point projectedAndInset = geoMap.transformAndInset(labelLowerLeftCoordinate, inset);
				Point2D.Double labelLowerLeftPoint =
						geoMapViewPageArea.displayPointFor(projectedAndInset.getCoordinate());
				out.write(" gsave ");
				out.write(PSUtility.findscalesetfont(pageLayout.contentFont()));
				out.write(PSUtility.xy(labelLowerLeftPoint) + " moveto ");
				out.write(String.format(" (%s) show ", inset.makeLabel()));
				out.write(" grestore ");
			}
			
	
			out.write(GeoMapViewPS.TEMPLATE_GROUP.getInstanceOf("circlePrinterDefinitions").toString());
	
			out.write("% Circle annotations" + "\n");		
			out.write("gsave" + "\n");
			out.write("\n");
			
			double circleLineWidth = Circle.DEFAULT_CIRCLE_LINE_WIDTH;
			out.write(PostScriptable.INDENT + circleLineWidth + " setlinewidth" + "\n");
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
			
			if (geoMap.getHowToRead().isPresent()) {
				out.write(geoMap.getHowToRead().get().toPostScript());
			}
			
			out.write("showpage" + "\n");
		} finally {
			out.close();
		}

		LogStream.INFO.send("Done.");
		
		return psFile;
	}
	
	private static String timestamp() {
		return DateTimeFormat.forPattern("MMM dd, yyyy | hh:mm:ss aa z").print(new DateTime());
	}

	/**
	 * One of the several possible page positions (in points) of the projection of this (longitude,
	 * latitude) coordinate.
	 * 
	 * <p>
	 * Because the coordinate may be part of zero, one, or multiple insets, it may be represented
	 * by any number of page positions. When there are no representations, the page position is
	 * absent. When there is exactly one representation, that is returned. When there are multiple
	 * representations, one of them is picked arbitrarily.
	 */
	public Point2D.Double transformAndInsetToPagePoint(Coordinate coordinate) throws TransformException {
		Collection<Point> projectedPoints = geoMap.transformAndInset(coordinate);
		
		if (projectedPoints.isEmpty()) {
			throw new AssertionError(String.format("Coordinate %s not present in projection.", coordinate)); // TODO
		}
		Point someProjectedPoint = Iterables.getFirst(projectedPoints, null);
		
		if (projectedPoints.size() > 1) {
			LogStream.WARNING.send("The coordinate \"%s\" projected to multiple display points.  "
					+ "This can happen if the insets defined for this base map have "
					+ "overlapping bounding boxes.  "
					+ "A point has been chosen arbitrarily from the possibilities.", coordinate);
		}
		
		return geoMapViewPageArea.displayPointFor(someProjectedPoint.getCoordinate());
	}
	
	public static StringTemplateGroup loadTemplates() {
		return new StringTemplateGroup(
				new InputStreamReader(
					GeoMapsAlgorithm.class.getResourceAsStream(STRING_TEMPLATE_RESOURCE_PATH)));
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