package edu.iu.sci2.visualization.geomaps.projection.custom;

import java.awt.geom.Point2D;

import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.referencing.operation.projection.MapProjection;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterDescriptorGroup;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.operation.CylindricalProjection;
import org.opengis.referencing.operation.MathTransform;

/*
 * The Eckert IV map projection
 * 
 * Created from a template in the GeoTools documentation at:
 * 		http://docs.codehaus.org/display/GEOTDOC/How+to+add+new+projections
 * which was retrieved August 27, 2009.
 * 
 * Code for the actual math of the transformation was adapted from the
 * implementation of Eckert IV in Proj 4.6.1.  In particular, the file
 * PJ_eck4.c in http://download.osgeo.org/proj/proj-4.6.1.tar.gz which was
 * retrieved August 28, 2009.
 * 
 */
public class EckertIV extends MapProjection {
	private static final long serialVersionUID = 1L;
	
	public static final double C_x = 0.42223820031577120149;
	public static final double C_y = 1.32650042817700232218;
	public static final double RC_y = 0.75386330736002178205;
	public static final double C_p = 3.57079632679489661922;
	// Apparently, (2 / (4 + Math.PI))
	public static final double RC_p = 0.28004957675577868795;
	public static final double EPS = 1e-7;
	public static final int NITER = 6;
	

	public static final class Provider extends AbstractProvider {
		private static final long serialVersionUID = 1L;

		public static final ParameterDescriptorGroup PARAMETERS =
			createDescriptorGroup(
				new NamedIdentifier[] {
						new NamedIdentifier(Citations.OGC, "Eckert_IV"),
						new NamedIdentifier(Citations.GEOTIFF, "Eckert_IV"),
						new NamedIdentifier(Citations.GEOTOOLS, "Eckert_IV") },
				new ParameterDescriptor[] { SEMI_MAJOR, SEMI_MINOR,
						CENTRAL_MERIDIAN, SCALE_FACTOR, FALSE_EASTING,
						FALSE_NORTHING
						// Add or remove parameters here
				}
			);

		public Provider() {
			super(PARAMETERS);
		}

		@SuppressWarnings("unchecked") // Raw Class
		@Override
		public Class getOperationType() {
			// Actually, pseudo-cylindrical
			return CylindricalProjection.class;
		}

		@Override
		public MathTransform createMathTransform(
				final ParameterValueGroup parameters)
					throws ParameterNotFoundException {
			return new EckertIV(parameters);
		}
	}

	protected EckertIV(final ParameterValueGroup parameters)
			throws ParameterNotFoundException {
		// Fetch parameters
		super(parameters);
	}

	@Override
	protected Point2D transformNormalized(
			double lam, double phi, Point2D ptDst) {		
		double p = C_p * Math.sin(phi);
		double V = phi * phi;
		phi *= 0.895168 + V * ( 0.0218849 + V * 0.00826809 );
		
		double ii;
		for (ii = NITER; ii > 0; --ii) {
			double c = Math.cos(phi);
			double s = Math.sin(phi);
			phi -= (V = (phi + s * (c + 2.0) - p) / (1.0 + c * (c + 2.0) - s * s));
			
			if (Math.abs(V) < EPS) {
				break;
			}
		}
		
		double x;
		double y;
		if (ii == 0) {
			x = C_x * lam;
			y = ((phi < 0.0) ? (-C_y) : (C_y));
		} else {
			x = C_x * lam * (1.0 + Math.cos(phi));
			y = C_y * Math.sin(phi);
		}
		
		if (ptDst == null) {
			ptDst = new Point2D.Double();
		}
		
		ptDst.setLocation(x, y);
		
		return ptDst;
	}

	/* TODO Warning: This method has not been tested!  At present we have no
	 * use for it, but implementation is necessary and we may as well fill in
	 * what should be the correct code.
	 */
	@Override
	protected Point2D inverseTransformNormalized(
			double x, double y, Point2D ptDst) {
		System.out.println(
				"Warning: Eckert IV has been asked for an inverse "
				+ "transform.  This code has not been tested!");
		
		double c;
		
		double latitude = Math.asin(y / C_y);
		double longitude = x / (C_x * (1.0 + (c = Math.cos(latitude))));
		latitude = Math.asin((latitude + Math.sin(latitude) * (c + 2.0)) / C_p);
		
		if (ptDst == null) {
			ptDst = new Point2D.Double();
		}
		
		ptDst.setLocation(longitude, latitude);
		
		return ptDst;
	}

	@Override
	public ParameterDescriptorGroup getParameterDescriptors() {
		return Provider.PARAMETERS;
	}
}
