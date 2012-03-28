package edu.iu.sci2.visualization.geomaps.geo.projection;

import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.ProjectedCRS;

/**
 * A description of a {@link ProjectedCRS projected coordinate reference system} rich enough to
 * create the ProjectedCRS it describes.
 */
public interface ProjectedCRSDescriptor {
	/**
	 * The {@link ProjectedCRS} identified by this descriptor.
	 */
	ProjectedCRS asProjectedCRS() throws NoSuchAuthorityCodeException, FactoryException;
}