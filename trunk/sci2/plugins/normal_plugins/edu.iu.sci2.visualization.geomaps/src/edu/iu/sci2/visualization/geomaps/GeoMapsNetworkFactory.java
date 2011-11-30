package edu.iu.sci2.visualization.geomaps;

import java.io.File;
import java.util.Dictionary;
import java.util.LinkedHashMap;
import java.util.List;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmCreationFailedException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.cishell.utilities.ColumnNotFoundException;
import org.cishell.utilities.mutateParameter.dropdown.DropdownMutator;
import org.osgi.service.metatype.ObjectClassDefinition;

import edu.iu.nwb.util.nwbfile.NWBFileUtilities;
import edu.iu.nwb.util.nwbfile.NWBMetadataParsingException;
import edu.iu.sci2.visualization.geomaps.metatype.GeoCoordinateParameterFinder;
import edu.iu.sci2.visualization.geomaps.metatype.Shapefiles;

public class GeoMapsNetworkFactory implements AlgorithmFactory, ParameterMutator {
	public enum Parameter {
		// IDs must match with the values in OSGI-INF/metatype/METADATA.XML
		LATITUDE("latitude"), LONGITUDE("longitude"), SHAPEFILE_KEY("shapefile");
		private final String id;

		private Parameter(String id) {
			this.id = id;
		}
		
		public String getId() {
			return this.id;
		}
	}
	
	public Algorithm createAlgorithm(Data[] data,
			Dictionary<String, Object> parameters,
			CIShellContext ciShellContext) {
		return new GeoMapsNetwork(data, parameters, ciShellContext);
	}

	public ObjectClassDefinition mutateParameters(Data[] data,
			ObjectClassDefinition parameters) {
		try {
			File inFile = (File) data[0].getData();
			LinkedHashMap<String, String> nodeSchema = NWBFileUtilities.getNodeSchema(inFile);
			
			List<String> numericColumnNames = 
					NWBFileUtilities.findNumericAttributes(
							NWBFileUtilities.removeRequiredNodeProps(nodeSchema));
			
			DropdownMutator mutator = new DropdownMutator();
			
			Shapefiles.addShapefileAndProjectionParameters(mutator);
			GeoCoordinateParameterFinder.addLatitudeParameter(mutator, numericColumnNames, Parameter.LATITUDE.getId());
			GeoCoordinateParameterFinder.addLongitudeParameter(mutator, numericColumnNames, Parameter.LONGITUDE.getId());
			
			return mutator.mutate(parameters);
		} catch (ColumnNotFoundException e) {
			String message =
				"Table does not seem to have any purely numeric columns.  "
				+ "If your table does not have columns for the latitudes and longitudes of records,"
				+ " you may wish to use one of the geocoders under Analysis > Geospatial.";
			throw new AlgorithmCreationFailedException(message, e);					
		} catch (NWBMetadataParsingException e) {
			throw new AlgorithmCreationFailedException("Error reading input file: " + e.getMessage(), e);
		}
		
	}
}