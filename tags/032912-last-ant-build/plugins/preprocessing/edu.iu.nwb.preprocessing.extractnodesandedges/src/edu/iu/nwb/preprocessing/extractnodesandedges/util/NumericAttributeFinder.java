package edu.iu.nwb.preprocessing.extractnodesandedges.util;

import static edu.iu.nwb.util.nwbfile.NWBFileUtilities.findNumericAttributes;
import static edu.iu.nwb.util.nwbfile.NWBFileUtilities.getConsistentEdgeAttributes;
import static edu.iu.nwb.util.nwbfile.NWBFileUtilities.getNodeSchema;
import static edu.iu.nwb.util.nwbfile.NWBFileUtilities.parseMetadata;
import static edu.iu.nwb.util.nwbfile.NWBFileUtilities.removeRequiredEdgeProps;
import static edu.iu.nwb.util.nwbfile.NWBFileUtilities.removeRequiredNodeProps;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.cishell.framework.algorithm.AlgorithmCreationFailedException;
import org.cishell.framework.data.Data;
import org.cishell.utilities.MutateParameterUtilities;
import org.osgi.service.metatype.ObjectClassDefinition;

import edu.iu.nwb.util.nwbfile.NWBMetadataParsingException;

public class NumericAttributeFinder {
	public static ObjectClassDefinition mutateParametersForEdges(Data[] data,
			ObjectClassDefinition parameters) {
		Map<String, String> attributes;
		try {
			attributes = removeRequiredEdgeProps(getConsistentEdgeAttributes(parseMetadata((File) data[0]
					.getData())));
			List<String> numericAttributes = findNumericAttributes(attributes);

			if (numericAttributes.isEmpty()) {
				throw new UnsuitableFileException("No numeric attributes found on all edges");
			}
			return MutateParameterUtilities.mutateToDropdown(parameters,
					"numericAttribute", numericAttributes, numericAttributes);
		} catch (NWBMetadataParsingException e) {
			throw new AlgorithmCreationFailedException(e);
		} catch (UnsuitableFileException e) {
			throw new AlgorithmCreationFailedException(e);
		}
	}

	public static ObjectClassDefinition mutateParametersForNodes(Data[] data,
			ObjectClassDefinition parameters) {
		Map<String, String> attributes;
		try {
			attributes = removeRequiredNodeProps(getNodeSchema((File) data[0]
					.getData()));
			List<String> numericAttributes = findNumericAttributes(attributes);

			if (numericAttributes.isEmpty()) {
				throw new UnsuitableFileException("No numeric attributes found on nodes");
			}
			return MutateParameterUtilities.mutateToDropdown(parameters,
					"numericAttribute", numericAttributes, numericAttributes);
		} catch (NWBMetadataParsingException e) {
			throw new AlgorithmCreationFailedException(e);
		} catch (UnsuitableFileException e) {
			throw new AlgorithmCreationFailedException(e);
		}
	}
}
