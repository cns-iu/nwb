package edu.iu.sci2.visualization.geomaps.geo.projection;

import java.util.EnumSet;

import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.ProjectedCRS;
import org.opengis.referencing.operation.MathTransform;

import com.google.common.base.Function;
import com.google.common.base.Objects;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.Maps;

import edu.iu.sci2.visualization.geomaps.geo.shapefiles.Shapefile;

public enum KnownProjectedCRSDescriptor implements ProjectedCRSDescriptor {
	ECKERT_IV("Eckert IV", new EPSGCode("EPSG:54012")),
	WINKEL_TRIPEL("Winkel Tripel",
			new WKT("PROJCS[\"World_Winkel_Tripel_NGS\"," +
					"GEOGCS[\"GCS_WGS_1984\"," +
					"DATUM[\"D_WGS_1984\"," +
					"SPHEROID[\"WGS_1984\",6378137.0,298.257223563]]," +
					"PRIMEM[\"Greenwich\",0.0]," +
					"UNIT[\"Degree\",0.0174532925199433]]," +
					"PROJECTION[\"Winkel_Tripel\"]," +
					"PARAMETER[\"standard_parallel_1\",40.0]," +
					"UNIT[\"Meter\",1.0]]")),
	MERCATOR("Mercator", new EPSGCode("EPSG:2965")),
	ALBERS("Albers Equal-Area Conic", new EPSGCode("EPSG:3083")),
	LAMBERT("Lambert Conformal Conic", new EPSGCode("EPSG:102004"));
	
	/* "if the math transform should be created even when there is no information available for a
	 * datum shift."
	 * Suppresses errors due to unspecified Bursa-Wolf parameters when* transforming between
	 * distinct datums.  Practically, this should not result in errors large enough to significantly
	 * distort drawing. */
	public static final boolean REQUEST_LENIENT_TRANSFORM = true;
	
	public static final ImmutableBiMap<String, KnownProjectedCRSDescriptor> FOR_NAME =
			ImmutableBiMap.copyOf(Maps.uniqueIndex(
					EnumSet.allOf(KnownProjectedCRSDescriptor.class),
						new Function<KnownProjectedCRSDescriptor, String>() {
							public String apply(KnownProjectedCRSDescriptor shapefile) {
								return shapefile.displayName();
							}
						}));
	public static KnownProjectedCRSDescriptor forName(String displayName) {
		// TODO What to do about null?
		return FOR_NAME.get(displayName);
	}

	private final String displayName;
	private final ProjectedCRSDescriptor projectedCrsDescriptor;
	
	private KnownProjectedCRSDescriptor(String displayName, ProjectedCRSDescriptor crsMaker) {
		this.displayName = displayName;
		this.projectedCrsDescriptor = crsMaker;
	}
	

	public String displayName() {
		return displayName;
	}

	public ProjectedCRS asProjectedCRS() throws NoSuchAuthorityCodeException, FactoryException {
		return projectedCrsDescriptor.asProjectedCRS();
	}

	public MathTransform getTransformFrom(CoordinateReferenceSystem sourceCrs) throws NoSuchAuthorityCodeException, FactoryException {
		return CRS.findMathTransform(
				Objects.firstNonNull(sourceCrs, Shapefile.DEFAULT_SOURCE_CRS), // TODO ?
				asProjectedCRS(),
				REQUEST_LENIENT_TRANSFORM);
	}

	
	private static final class EPSGCode implements ProjectedCRSDescriptor {
		private final String code;
	
		public EPSGCode(String code) {
			this.code = code;
		}
		
		public ProjectedCRS asProjectedCRS() throws NoSuchAuthorityCodeException, FactoryException {
			return CRS.getProjectedCRS(CRS.decode(code));
		}
	}

	
	private static final class WKT implements ProjectedCRSDescriptor {
		private final String wkt;
		
		public WKT(String wkt) {
			this.wkt = wkt;
		}
	
		public ProjectedCRS asProjectedCRS() throws FactoryException {
			return CRS.getProjectedCRS(CRS.parseWKT(wkt));
		}		
	}
}
