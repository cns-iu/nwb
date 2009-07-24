package edu.iu.nwb.visualization.drl;

import java.io.File;
import java.util.Dictionary;
import java.util.LinkedHashMap;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.DataValidator;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.cishell.utilities.MapUtilities;
import org.cishell.utilities.MutateParameterUtilities;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.metatype.ObjectClassDefinition;

import edu.iu.nwb.util.nwbfile.GetMetadataAndCounts;
import edu.iu.nwb.util.nwbfile.NWBFileProperty;
import edu.iu.nwb.util.nwbfile.NWBFileUtilities;
import edu.iu.nwb.util.nwbfile.NWBMetadataParsingException;

public class VxOrdAlgorithmFactory implements AlgorithmFactory,
											  DataValidator,
											  ParameterMutator {
    public static final String USE_NO_EDGE_WEIGHT_TOKEN =
    	"None (all edges treated equally)";
	public static final String EDGE_WEIGHT_ID = "edgeWeight";
	private BundleContext bContext;
    
    protected void activate(ComponentContext ctxt) {
    	bContext = ctxt.getBundleContext();
    }
    
    public Algorithm createAlgorithm(Data[] data,
    								 Dictionary parameters,
    								 CIShellContext context) {
        return new VxOrdAlgorithm(data, parameters, context, bContext);
    }
    
    // Ensure the input file is a valid, undirected NWB network.
	public String validate(Data[] data) {
		File nwbFile = (File) data[0].getData();
		
		GetMetadataAndCounts metadata;
		try {
			metadata = NWBFileUtilities.parseMetadata(nwbFile);
		}
		catch (NWBMetadataParsingException e) {
			return "Invalid NWB file";
		}
		
		if (metadata.getUndirectedEdgeSchema() != null) {
			return "";
		}
		else {
			return "DrL can only process undirected networks."; 
		}
	}
    
	
	/* Replace edgeWeight parameter with a drop-down list containing the
	 * numeric edge attribute keys on the input NWB file as well as
	 * an option (USE_NO_EDGE_WEIGHT_TOKEN) to ignore any weights on the
	 * network, assuming instead that all equal 1.
	 */
	public ObjectClassDefinition mutateParameters(
			Data[] data,
			ObjectClassDefinition parameters) {
		File nwbFile = (File) data[0].getData();
		
		GetMetadataAndCounts metadata;
		try {
			metadata = NWBFileUtilities.parseMetadata(nwbFile);
		} catch (NWBMetadataParsingException e) {
			return parameters;
		}
		
		LinkedHashMap edgeSchema = metadata.getUndirectedEdgeSchema();
		
		String[] numericAttributeTypes = (String[])
			NWBFileProperty.NUMERIC_ATTRIBUTE_TYPES.toArray(new String[0]);
		
		/* Get the numeric edge attributes, excepting SKIP, and including
		 * the option to use no edge weight.
		 */
		String[] edgeWeightOptions =
			MapUtilities.getValidKeysOfTypesInMap(
				edgeSchema,
				numericAttributeTypes,
				NWBFileUtilities.DEFAULT_NUMBER_KEYS_TO_SKIP,
				new String[]{ USE_NO_EDGE_WEIGHT_TOKEN });
		
		return MutateParameterUtilities.mutateToDropdown(
				parameters,
				EDGE_WEIGHT_ID,
				edgeWeightOptions,
				edgeWeightOptions);
	}
}