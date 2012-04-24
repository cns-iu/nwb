package edu.iu.sci2.visualization.bipartitenet.algorithm;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.cishell.framework.algorithm.AlgorithmCreationFailedException;

import com.google.common.collect.Sets;

import edu.iu.nwb.util.nwbfile.GetNWBFileMetadata;
import edu.iu.nwb.util.nwbfile.NWBFileProperty;

class BipartiteNWBFileExaminer extends GetNWBFileMetadata {
	private static final String TYPE_COLUMN = "bipartitetype";
	private final Set<String> bipartiteTypes = Sets.newHashSet();

	public static String getTypeColumn() {
		return TYPE_COLUMN;
	}

	public Set<String> getBipartiteTypes() {
		if (bipartiteTypes.size() > 2) {
			throw new AlgorithmCreationFailedException(
					"More than 2 node types found in supposedly-bipartite graph: "
							+ bipartiteTypes.toString());
		}
		return Sets.newHashSet(bipartiteTypes);
	}

	@Override
	public void setNodeSchema(LinkedHashMap<String, String> nodeSchema) {
		super.setNodeSchema(nodeSchema);

		if (!nodeSchema.containsKey(TYPE_COLUMN)) {
			throw new AlgorithmCreationFailedException(
					"Bipartite Graph algorithm requires the 'bipartitetype' node attribute.");
		}

		if (!NWBFileProperty.TYPE_STRING.equals(nodeSchema.get(TYPE_COLUMN))) {
			throw new AlgorithmCreationFailedException(
					"Bipartite Graph algorithm requires that the 'bipartitetype' node" +
					" attribute be of type '" + NWBFileProperty.TYPE_STRING + "'.");
		}
	}

	@Override
	public void addNode(int id, String label, Map<String, Object> attributes) {
		super.addNode(id, label, attributes);
		String type = (String) attributes.get(TYPE_COLUMN);
		if (type == null) {
			throw new AlgorithmCreationFailedException(String.format(
					"Node '%s' has null bipartitetype, but all nodes must have a type", label));
		}
		bipartiteTypes.add(type);
	}
}
