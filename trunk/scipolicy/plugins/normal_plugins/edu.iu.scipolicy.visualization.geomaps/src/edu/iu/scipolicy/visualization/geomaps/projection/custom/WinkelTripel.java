package edu.iu.scipolicy.visualization.geomaps.projection.custom;

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
 * Created from a template in the GeoTools documentation at:
 * 		http://docs.codehaus.org/display/GEOTDOC/How+to+add+new+projections
 * which was retrieved August 27, 2009.
 * 
 * Code for the actual math of the transformation was adapted from the
 * implementation of Winkel Tripel in Proj 4.6.1.  In particular, the file
 * PJ_aitoff.c in http://download.osgeo.org/proj/proj-4.6.1.tar.gz which was
 * retrieved August 28, 2009.
 * 
 */
public class WinkelTripel extends MapProjection {
	private static final long serialVersionUID = 1L;
	
	public static final double COS_PHI_1 = 2.0 / Math.PI;


	public static final class Provider extends AbstractProvider {
		private static final long serialVersionUID = 1L;

		public static final ParameterDescriptorGroup PARAMETERS =
			createDescriptorGroup(
				new NamedIdentifier[] {
						new NamedIdentifier(Citations.OGC, "Winkel_Tripel"),
						new NamedIdentifier(Citations.GEOTIFF, "Winkel_Tripel"),
						new NamedIdentifier(Citations.GEOTOOLS, "Winkel_Tripel") },
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
			// Not actually cylindrical
			return CylindricalProjection.class;
		}

		@Override
		public MathTransform createMathTransform(
				final ParameterValueGroup parameters)
					throws ParameterNotFoundException {
			return new WinkelTripel(parameters);
		}
	}

	protected WinkelTripel(final ParameterValueGroup parameters)
			throws ParameterNotFoundException {
		// Fetch parameters
		super(parameters);
	}

	@Override
	protected Point2D transformNormalized(
			double lam, double phi, Point2D ptDst) {		
		double c, d;
		double x, y;
		
		// First, the Aitoff part
		if(0 != (d = Math.acos(Math.cos(phi) * Math.cos(c = 0.5 * lam)))) {
			x = 2. * d * Math.cos(phi) * Math.sin(c) * (y = 1. / Math.sin(d));
			y *= d * Math.sin(phi);
		} else {
			x = y = 0.;
		}
		
		// Finally, the Winkel Tripel part
		x = (x + lam * COS_PHI_1) * 0.5;
		y = (y + phi) * 0.5;
		
		if (ptDst == null) {
			ptDst = new Point2D.Double();
		}
		
		ptDst.setLocation(x, y);
		
		return ptDst;
	}

	@Override
	protected Point2D inverseTransformNormalized(
			double x, double y, Point2D ptDst) {
		System.out.println(
				"Warning: Winkel Tripel has been asked for an inverse "
				+ "transform.  This code has not been implemented!");
		
		return null;
	}

	@Override
	public ParameterDescriptorGroup getParameterDescriptors() {
		return Provider.PARAMETERS;
	}
}
