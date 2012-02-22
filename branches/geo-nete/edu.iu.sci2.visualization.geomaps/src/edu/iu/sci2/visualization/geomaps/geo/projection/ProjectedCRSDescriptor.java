package edu.iu.sci2.visualization.geomaps.geo.projection;

import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.ProjectedCRS;

public interface ProjectedCRSDescriptor {
	ProjectedCRS asProjectedCRS() throws NoSuchAuthorityCodeException, FactoryException;
}