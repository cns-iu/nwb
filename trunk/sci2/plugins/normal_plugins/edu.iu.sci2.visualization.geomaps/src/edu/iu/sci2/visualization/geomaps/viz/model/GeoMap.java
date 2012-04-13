package edu.iu.sci2.visualization.geomaps.viz.model;

import java.util.Collection;
import java.util.Set;

import org.geotools.factory.Hints;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.referencing.operation.TransformException;
import org.osgi.service.log.LogService;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import edu.iu.sci2.visualization.geomaps.GeoMapsAlgorithm;
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

public class GeoMap {
	private static final GeometryFactory DEFAULT_GEOMETRY_FACTORY = JTSFactoryFinder.getGeometryFactory(new Hints(Hints.CRS, DefaultGeographicCRS.WGS84));
	
	private final String title;
	private final Shapefile shapefile;
	private final KnownProjectedCRSDescriptor knownProjectedCRSDescriptor;
	private final Collection<FeatureView> featureViews;
	private final Collection<Circle> circles;
	
	private final Legendarium legendarium;
	private final GeometryFactory geometryFactory;
	private final GeometryProjector geometryProjector;

	private final Collection<Inset> insets;

	public GeoMap(
			String title,
			Shapefile shapefile,
			KnownProjectedCRSDescriptor knownProjectedCRSDescriptor,
			Collection<FeatureView> featureViews,
			Collection<Circle> circles,
			Collection<LabeledReference> legends,
			PageLayout pageLayout) throws GeoMapException {
		this.title = title;
		this.shapefile = shapefile;
		this.knownProjectedCRSDescriptor = knownProjectedCRSDescriptor;
		this.featureViews = featureViews;
		this.circles = circles;
		
		this.legendarium = Legendarium.containing(pageLayout.legendariumLowerLeft(), pageLayout,
				legends);
		
		this.geometryFactory = DEFAULT_GEOMETRY_FACTORY;
		
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
				GeoMapsAlgorithm.logger.log(LogService.LOG_WARNING, String.format(
						"Failed to create map inset for feature named \"%s\".",
						request.getFeatureName()));
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

	public String getTitle() {
		return title;
	}

	/**
	 * All applicable insets are applied.
	 */
	public Collection<Coordinate> projectAndInset(Coordinate naturalCoordinate) throws TransformException {
		Geometry coordinatePointGeometry = geometryFactory.createPoint(naturalCoordinate);
		
		Collection<Coordinate> projectedCoordinates = Sets.newHashSet();
		for (Geometry geometry : projectAndInset(coordinatePointGeometry)) {
			projectedCoordinates.add(geometry.getCoordinate());
		}
		
		return projectedCoordinates;
	}
	
	/**
	 * The provided inset (and only that inset) is applied.
	 */
	public Coordinate projectUsingInset(Coordinate naturalCoordinate, Inset inset) throws TransformException {
		Geometry naturalCoordinatePointGeometry = geometryFactory.createPoint(naturalCoordinate);
		Geometry projectedCoordinatePointGeometry =
				geometryProjector.projectGeometry(naturalCoordinatePointGeometry);
		
		return inset.inset(projectedCoordinatePointGeometry).getCoordinate();
	}

	/**
	 * All applicable insets are applied.
	 */
	public Collection<Geometry> projectAndInset(Geometry naturalGeometry) throws TransformException {
		return inset(naturalGeometry, geometryProjector.projectGeometry(naturalGeometry));
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

	public Collection<Inset> getInsets() {
		return insets;
	}	
}