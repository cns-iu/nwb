package edu.iu.sci2.visualization.geomaps.viz.model;

import java.util.Collection;

import org.geotools.factory.Hints;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

import edu.iu.sci2.visualization.geomaps.geo.projection.GeometryProjector;
import edu.iu.sci2.visualization.geomaps.geo.projection.GeometryProjector.GeometryProjectorException;
import edu.iu.sci2.visualization.geomaps.geo.projection.KnownProjectedCRSDescriptor;
import edu.iu.sci2.visualization.geomaps.geo.shapefiles.Shapefile;
import edu.iu.sci2.visualization.geomaps.geo.shapefiles.Shapefile.Inset;
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

	public Coordinate project(Coordinate coordinate) throws TransformException {
		Geometry coordinatePointGeometry = geometryFactory.createPoint(coordinate);
		
		return project(coordinatePointGeometry).getCoordinate();
	}

	public Geometry project(Geometry geometry) throws TransformException {
		Inset inset = shapefile.inset(geometry);
		Geometry projectedGeometry = geometryProjector.projectGeometry(geometry);
		if (inset != null) {
			projectedGeometry = inset.inset(projectedGeometry, geometryProjector);
		}
		
		return projectedGeometry;
	}
}