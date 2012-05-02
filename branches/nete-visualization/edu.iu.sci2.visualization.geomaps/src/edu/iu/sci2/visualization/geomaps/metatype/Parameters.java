package edu.iu.sci2.visualization.geomaps.metatype;

import java.util.List;

import org.cishell.utilities.ToCaseFunction;
import org.cishell.utilities.mutateParameter.dropdown.DropdownMutator;

import com.google.common.collect.Ordering;

import edu.iu.sci2.visualization.geomaps.geo.projection.KnownProjectedCRSDescriptor;
import edu.iu.sci2.visualization.geomaps.geo.shapefiles.Shapefile;
import edu.iu.sci2.visualization.geomaps.utility.NicelyNamedEnums;
import edu.iu.sci2.visualization.geomaps.utility.SubstringOrderings;

public class Parameters {
	public static final String SHAPEFILE_ID = "shapefile";
	public static final String PROJECTION_ID = "projection";
	public static final boolean LET_USER_CHOOSE_PROJECTION = false;
	
	public static final Ordering<String> LATITUDISHNESS =
			SubstringOrderings.explicit("y", "l", "lt", "lat", "lat.", "latitude")
				.onResultOf(ToCaseFunction.LOWER);
	
	public static final Ordering<String> LONGITUDISHNESS =
			SubstringOrderings.explicit("x", "l", "ln", "lg", "long", "lng", "longitude")
				.onResultOf(ToCaseFunction.LOWER);
	
	
	public static void addLatitudeParameter(
			DropdownMutator mutator, List<String> columnNames, String parameterId) {
		mutator.add(parameterId, LATITUDISHNESS.reverse().sortedCopy(columnNames));
	}

	public static void addLongitudeParameter(
			DropdownMutator mutator, List<String> columnNames, String parameterId) {
		mutator.add(parameterId, LONGITUDISHNESS.reverse().sortedCopy(columnNames));
	}

	public static void addShapefileAndProjectionParameters(DropdownMutator mutator) {
		mutator.add(Parameters.SHAPEFILE_ID, NicelyNamedEnums.allNiceNamesOf(Shapefile.class));
		
		if (Parameters.LET_USER_CHOOSE_PROJECTION) {
			mutator.add(Parameters.PROJECTION_ID,
					NicelyNamedEnums.allNiceNamesOf(KnownProjectedCRSDescriptor.class));
		} else {
			mutator.ignore(Parameters.PROJECTION_ID);
		}
	}
}
