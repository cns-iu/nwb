package edu.iu.sci2.visualization.geomaps.geo.projection;

import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.ProjectedCRS;
import org.opengis.referencing.operation.MathTransform;

import com.google.common.base.Objects;

import edu.iu.sci2.visualization.geomaps.geo.shapefiles.Shapefile;
import edu.iu.sci2.visualization.geomaps.utility.NicelyNamedEnums.NicelyNamed;

public enum KnownProjectedCRSDescriptor implements ProjectedCRSDescriptor, NicelyNamed {
	ECKERT_IV("Eckert IV", "Eckert IV", new EPSGCode("EPSG:54012")),
//	WINKEL_TRIPEL("Winkel Tripel", "Winkel Tripel", // TODO findCentralMeridian is broken on Winkel
//			new WKT("PROJCS[\"World_Winkel_Tripel_NGS\"," +
//					"GEOGCS[\"GCS_WGS_1984\"," +
//					"DATUM[\"D_WGS_1984\"," +
//					"SPHEROID[\"WGS_1984\",6378137.0,298.257223563]]," +
//					"PRIMEM[\"Greenwich\",0.0]," +
//					"UNIT[\"Degree\",0.0174532925199433]]," +
//					"PROJECTION[\"Winkel_Tripel\"]," +
//					"PARAMETER[\"standard_parallel_1\",40.0]," +
//					"UNIT[\"Meter\",1.0]]")),
	MERCATOR("Mercator", "Mercator", new EPSGCode("EPSG:3395")),
	ALBERS("Albers Equal-Area Conic", "Albers equal-area conic", new EPSGCode("EPSG:3083")),
	LAMBERT("Lambert Conformal Conic", "Lambert conformal conic", new EPSGCode("EPSG:102004"));
	
	/* "if the math transform should be created even when there is no information available for a
	 * datum shift."
	 * Suppresses errors due to unspecified Bursa-Wolf parameters when* transforming between
	 * distinct datums.  Practically, this should not result in errors large enough to significantly
	 * distort drawing. */
	public static final boolean REQUEST_LENIENT_TRANSFORM = true;

	private final String niceNameTitleCase;
	private final String niceNamePlain;
	private final ProjectedCRSDescriptor projectedCrsDescriptor;

	private KnownProjectedCRSDescriptor(String niceNameTitleCase, String niceNamePlain, ProjectedCRSDescriptor crsMaker) {
		this.niceNameTitleCase = niceNameTitleCase;
		this.niceNamePlain = niceNamePlain;
		this.projectedCrsDescriptor = crsMaker;
	}
	

	@Override
	public String getNiceName() {
		return getNiceNameTitleCase();
	}
	
	public String getNiceNameTitleCase() {
		return niceNameTitleCase;
	}
	
	public String getNiceNamePlain() {
		return niceNamePlain;
	}

	@Override
	public ProjectedCRS asProjectedCRS() throws NoSuchAuthorityCodeException, FactoryException {
		return projectedCrsDescriptor.asProjectedCRS();
	}

	/**
	 * If {@code sourceCrs} is null, {@link Shapefile#FALLBACK_SOURCE_CRS} is assumed.
	 */
	public MathTransform getTransformFrom(CoordinateReferenceSystem sourceCrs) throws NoSuchAuthorityCodeException, FactoryException {
		return CRS.findMathTransform(
				Objects.firstNonNull(sourceCrs, Shapefile.FALLBACK_SOURCE_CRS),
				asProjectedCRS(),
				REQUEST_LENIENT_TRANSFORM);
	}

	
	private static final class EPSGCode implements ProjectedCRSDescriptor {
		private final String code;
	
		public EPSGCode(String code) {
			this.code = code;
		}
		
		@Override
		public ProjectedCRS asProjectedCRS() throws NoSuchAuthorityCodeException, FactoryException {
			return CRS.getProjectedCRS(CRS.decode(code));
		}
	}

	
//	private static final class WKT implements ProjectedCRSDescriptor {
//		private final String wkt;
//		
//		public WKT(String wkt) {
//			this.wkt = wkt;
//		}
//	
//		@Override
//		public ProjectedCRS asProjectedCRS() throws FactoryException {
//			return CRS.getProjectedCRS(CRS.parseWKT(wkt));
//		}		
//	}
}
