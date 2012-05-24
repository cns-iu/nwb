package edu.iu.sci2.visualization.geomaps.viz.model;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.Set;

import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.referencing.operation.TransformException;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

import edu.iu.sci2.visualization.geomaps.LogStream;
import edu.iu.sci2.visualization.geomaps.geo.projection.GeometryProjector;
import edu.iu.sci2.visualization.geomaps.geo.projection.GeometryProjector.GeometryProjectorException;
import edu.iu.sci2.visualization.geomaps.geo.projection.KnownProjectedCRSDescriptor;
import edu.iu.sci2.visualization.geomaps.geo.shapefiles.Shapefile;
import edu.iu.sci2.visualization.geomaps.geo.shapefiles.Shapefile.Inset;
import edu.iu.sci2.visualization.geomaps.geo.shapefiles.Shapefile.Inset.InsetCreationException;
import edu.iu.sci2.visualization.geomaps.geo.shapefiles.Shapefile.Inset.Request;
import edu.iu.sci2.visualization.geomaps.viz.Circle;
import edu.iu.sci2.visualization.geomaps.viz.FeatureView;
import edu.iu.sci2.visualization.geomaps.viz.PageLayout;
import edu.iu.sci2.visualization.geomaps.viz.legend.LabeledReference;
import edu.iu.sci2.visualization.geomaps.viz.legend.Legendarium;
import edu.iu.sci2.visualization.geomaps.viz.ps.HowToRead;

public class GeoMap {	
	private final String title;
	private final Shapefile shapefile;
	private final KnownProjectedCRSDescriptor knownProjectedCRSDescriptor;
	private final Collection<FeatureView> featureViews;
	private final Collection<Circle> circles;
	
	private final Legendarium legendarium;
	private final GeometryProjector geometryProjector;

	private final Collection<Inset> insets;

	private final Optional<HowToRead> howToRead;

	public GeoMap(
			String title,
			Shapefile shapefile,
			KnownProjectedCRSDescriptor knownProjectedCRSDescriptor,
			Collection<FeatureView> featureViews,
			Collection<Circle> circles,
			Collection<LabeledReference> legends,
			PageLayout pageLayout,
			Optional<HowToRead> howToRead) throws GeoMapException {
		this.title = title;
		this.shapefile = shapefile;
		this.knownProjectedCRSDescriptor = knownProjectedCRSDescriptor;
		this.featureViews = featureViews;
		this.circles = circles;
		this.howToRead = howToRead;
		
		this.legendarium =
				Legendarium.containing(pageLayout.legendariumLowerLeft(), pageLayout, legends);
		
		try {
			this.geometryProjector =
					new GeometryProjector(shapefile.detectNativeCRS(), knownProjectedCRSDescriptor);
		} catch (GeometryProjectorException e) {
			throw new GeoMapException("Failed to create map projection.", e);
		}
		
		Set<Inset> insets = Sets.newHashSet();
		for (Request request : shapefile.getInsetRequests()) {
			try {
				insets.add(Inset.forRequest(request, shapefile, geometryProjector));
			} catch (InsetCreationException e) {
				LogStream.WARNING.send(e, "Failed to create map inset for feature named \"%s\".",
						request.getFeatureName());
			}
		}
		this.insets = insets;
	}

	public GeometryProjector getGeometryProjector() {
		return geometryProjector;
	}

	public Legendarium getLegendarium() {
		return legendarium;
	}

	public Shapefile getShapefile() {
		return shapefile;
	}

	public KnownProjectedCRSDescriptor getKnownProjectedCRSDescriptor() {
		return knownProjectedCRSDescriptor;
	}

	public Collection<FeatureView> getFeatureViews() {
		return featureViews;
	}

	public Collection<Circle> getCircles() {
		return circles;
	}
	
	public Optional<HowToRead> getHowToRead() {
		return howToRead;
	}

	public String getTitle() {
		return title;
	}

	public Collection<Inset> getInsets() {
		return insets;
	}
	
	/**
	 * The views of the projection of this geometry under all applicable insets.
	 */
	public Collection<Geometry> cropTransformAndInset(Geometry naturalGeometry) throws TransformException {
		return inset(naturalGeometry, geometryProjector.cropAndTransformGeometry(naturalGeometry));
	}

	/**
	 * The views of the projection of this (longitude, latitude) coordinate under all applicable
	 * insets.
	 */
	public Collection<Point> transformAndInset(Coordinate naturalCoordinate) throws TransformException {
		Set<Point> projectedPoints = Sets.newHashSet();
		for (Point point : insetCoordinate(
				naturalCoordinate, geometryProjector.transformCoordinate(naturalCoordinate))) {
			projectedPoints.add(point);
		}
		
		if (projectedPoints.isEmpty()) { // TODO ?
			throw new AssertionError(String.format("Coordinate %s not present in projection.",
					naturalCoordinate));
		}
		
		return projectedPoints;
	}

	/**
	 * The inset of the projection of this (longitude, latitude) coordinate.
	 */
	public Point transformAndInset(Coordinate naturalCoordinate, Inset inset)
			throws TransformException {
		return inset.insetPoint(geometryProjector.transformCoordinate(naturalCoordinate));
	}
	
	private Collection<Point> insetCoordinate(Coordinate naturalCoordinate, Point projectedPoint) {
		Collection<Point> points = Sets.newHashSet();
		for (Inset inset : insets) {
			if (inset.shouldInsetCoordinate(naturalCoordinate)) {
				points.add(inset.insetPoint(projectedPoint));
			}
		}
		
		/* Insettable regions may overlap, so the above loop may result in zero, one, or more than
		 * one views of the same projected points. When the given point is not part of any inset,
		 * we need to explicitly give the un-inset point. */
		if (points.isEmpty()) {
			points.add(projectedPoint);
		}
		
		if (points.isEmpty()) { // TODO ?
			throw new AssertionError(String.format("Coordinate %s not present in projection.",
					naturalCoordinate));
		}
		
		return points;
	}

	private Collection<Geometry> inset(Geometry naturalGeometry, Geometry projectedGeometry) {
		Collection<Geometry> geometries = Sets.newHashSet();
		for (Inset inset : insets) {
			if (inset.shouldInset(naturalGeometry)) {
				geometries.add(inset.inset(projectedGeometry));
			}
		}
		
		/* Insettable regions may overlap, so the above loop may result in zero,
		 * one, or more than one views of the same projected geometry. When the
		 * given geometry is not part of any inset, we need to explicit return
		 * it.
		 */
		if (geometries.isEmpty()) {
			return ImmutableSet.of(projectedGeometry);
		}
		
		return geometries;
	}

	/**
	 * Identify extreme values for the X and Y dimensions among the projected features from our
	 * featureCollection. This is <em>after</em> cropping, transforming, and insetting.
	 */
	public Rectangle2D.Double calculateMapBoundingRectangle() throws TransformException {	
		Rectangle2D.Double rectangle = null;
	
		FeatureIterator<SimpleFeature> it = shapefile.viewOfFeatureCollection().features();
		try {
			while (it.hasNext()) {
				SimpleFeature feature = it.next();
				Collection<Geometry> geometries = ImmutableSet.of();
				try {
					geometries = cropTransformAndInset((Geometry) feature.getDefaultGeometry());
				} catch (IllegalArgumentException e) { // TODO Still necessary?
					/* This seems to happen intermittently with version 2.7.4 of geolibs/Geotools
					 * for one subgeometry of Minnesota in Shapefile.UNITED_STATES. */
					LogStream.DEBUG.send(String.format(
							"Skipping a geometry of feature %s due to IllegalArgumentException " +
							"during projection (%s).",
							shapefile.extractFeatureName(feature), e.getMessage()));
					continue;
				}
		
				for (Geometry geometry : geometries) {
					final int numberOfSubgeometries = geometry.getNumGeometries();
					
					for (int ii = 0; ii < numberOfSubgeometries; ii++) {
						for (Coordinate coordinate : geometry.getGeometryN(ii).getCoordinates()) {
							Point2D.Double point = new Point2D.Double(coordinate.x, coordinate.y);
							
							if (rectangle == null) {
								rectangle = new Rectangle2D.Double(point.x, point.y, 0, 0);
							}
							
							rectangle.add(point);
						}
					}
				}
			}
		} finally {
			it.close();
		}
		
		return rectangle;
	}	
}