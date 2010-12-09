package edu.iu.nwb.analysis.nodebetweennesscentrality;

import java.util.Arrays;
import java.util.Collection;
import java.util.Dictionary;
import java.util.LinkedHashMap;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmCreationFailedException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.cishell.reference.service.metatype.BasicAttributeDefinition;
import org.cishell.reference.service.metatype.BasicObjectClassDefinition;
import org.cishell.utilities.MutateParameterUtilities;
import org.cishell.utilities.StringUtilities;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

import edu.iu.cns.graphstream.common.AnnotatedGraph;
import edu.iu.nwb.util.nwbfile.NWBFileProperty;

public class NodeBetweennessCentralityAlgorithmFactory
		implements AlgorithmFactory, ParameterMutator {
	public static final String BOTH_EDGE_TYPES_FOUND_EXCEPTION_MESSAGE =
		"Both directed and undirected edges were found in this network. Only directed OR " +
		"undirected edges are allowed.";
	public static final String BOTH_EDGE_TYPES_NOT_FOUND_EXCEPTION_MESSAGE =
		"Neither directed nor undirected edges were found in this network. It must contain " +
		"either directed or undirected edges.";

	public static final String WEIGHT_KEY = "weight";
	public static final String CENTRALITY_ATTRIBUTE_NAME_KEY = "centralityAttributeName";

	public static final String NO_EDGE_WEIGHT_VALUE = "unweighted";

    public Algorithm createAlgorithm(
    		Data[] data, Dictionary<String, Object> parameters, CIShellContext ciShellContext) {
    	Data inputData = data[0];
    	AnnotatedGraph graph = (AnnotatedGraph) inputData.getData();
    	String weightAttributeName = (String) parameters.get(WEIGHT_KEY);
    	boolean isWeighted = NO_EDGE_WEIGHT_VALUE.equals(weightAttributeName);
    	String centralityAttributeName = (String) parameters.get(CENTRALITY_ATTRIBUTE_NAME_KEY);

        return new NodeBetweennessCentralityAlgorithm(
        	inputData, graph, weightAttributeName, isWeighted, centralityAttributeName);
    }

	public ObjectClassDefinition mutateParameters(
			Data[] data, ObjectClassDefinition oldParameters) {
		final AnnotatedGraph graph = (AnnotatedGraph) data[0].getData();
		LinkedHashMap<String, String> edgeSchema = getEdgeSchema(graph);

		BasicObjectClassDefinition newParameters =
			MutateParameterUtilities.createNewParameters(oldParameters);
		AttributeDefinition[] oldAttributes =
			oldParameters.getAttributeDefinitions(ObjectClassDefinition.ALL);

		// TODO: Use something more proprietary to AnnotatedGraph, as opposed to NWBFileProperty?
		Collection<String> numberKeysTypes = Arrays.asList(
			NWBFileProperty.TYPE_INT, NWBFileProperty.TYPE_FLOAT);
		Collection<String> numberKeysToSkip = Arrays.asList(
			NWBFileProperty.ATTRIBUTE_SOURCE, NWBFileProperty.ATTRIBUTE_TARGET);
		Collection<String> numberKeysToAdd = Arrays.asList(
			NO_EDGE_WEIGHT_VALUE);

		for (AttributeDefinition oldAttribute : oldAttributes) {
			String id = oldAttribute.getID();
			String name = oldAttribute.getName();
			String description = oldAttribute.getDescription();
			int type = oldAttribute.getType();
			String[] defaultValue = oldAttribute.getDefaultValue();
			AttributeDefinition newAttribute = oldAttribute;

			if (WEIGHT_KEY.equals(id)) {
				newAttribute = MutateParameterUtilities.formAttributeDefinitionFromMap(
					oldAttribute,
					edgeSchema,
					numberKeysTypes,
					numberKeysToSkip,
					numberKeysToAdd);
			} else if (CENTRALITY_ATTRIBUTE_NAME_KEY.equals(id)) {
				newAttribute = new BasicAttributeDefinition(
						id, name, description, type, defaultValue[0]) {
					@Override
					public String validate(String value) {
						String preValidation = super.validate(value);

						if (!StringUtilities.isNull_Empty_OrWhitespace(preValidation)) {
							return preValidation;
						}

						if (graph.getNodeSchema().containsKey(value)) {
							String errorMessage = String.format(
								"The input graph already contains the node property '%s'.",
								value);

							return errorMessage;
						}

						return null;
					}
				};
			}

			newParameters.addAttributeDefinition(ObjectClassDefinition.REQUIRED, newAttribute);
		}

		return newParameters;
	}

	private static LinkedHashMap<String, String> getEdgeSchema(AnnotatedGraph graph) {
		LinkedHashMap<String, String> directedEdgeSchema = graph.getDirectedEdgeSchema();
		LinkedHashMap<String, String> undirectedEdgeSchema = graph.getUndirectedEdgeSchema();

		if (directedEdgeSchema != null) {
			if (undirectedEdgeSchema != null) {
				throw new AlgorithmCreationFailedException(
					BOTH_EDGE_TYPES_FOUND_EXCEPTION_MESSAGE);
			} else {
				return directedEdgeSchema;
			}
		} else {
			if (undirectedEdgeSchema == null) {
				throw new AlgorithmCreationFailedException(
					BOTH_EDGE_TYPES_NOT_FOUND_EXCEPTION_MESSAGE);
			} else {
				return undirectedEdgeSchema;
			}
		}
	}
}