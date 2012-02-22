package edu.iu.sci2.visualization.geomaps.metatype;

import org.cishell.utilities.mutateParameter.dropdown.DropdownMutator;

import com.google.common.collect.Lists;

import edu.iu.sci2.visualization.geomaps.GeoMapsAlgorithm;
import edu.iu.sci2.visualization.geomaps.geo.projection.KnownProjectedCRSDescriptor;
import edu.iu.sci2.visualization.geomaps.geo.shapefiles.Shapefile;

public class Shapefiles {
	// TODO This is only used by circles?  Use for regions and network too..
	public static void addShapefileAndProjectionParameters(DropdownMutator mutator) {
		mutator.add(GeoMapsAlgorithm.SHAPEFILE_ID, Shapefile.PRETTY_NAME_TO_SHAPEFILE.keySet());
		
		if (GeoMapsAlgorithm.LET_USER_CHOOSE_PROJECTION) {
			mutator.add(GeoMapsAlgorithm.PROJECTION_ID,
				Lists.newArrayList(KnownProjectedCRSDescriptor.FOR_NAME.keySet()));
		} else {
			mutator.ignore(GeoMapsAlgorithm.PROJECTION_ID);
		}
	}
}
