package edu.iu.sci2.visualization.bipartitenet.algorithm;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.cishell.framework.algorithm.AlgorithmCreationFailedException;

import com.google.common.collect.Sets;

import edu.iu.nwb.util.nwbfile.GetNWBFileMetadata;
import edu.iu.nwb.util.nwbfile.NWBFileProperty;

class NWBFileExaminer extends GetNWBFileMetadata {
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
					"Bipartite Graph algorithm requires output of the Extract Bipartite Graph algorithm:"
							+ " 'bipartitetype' node attribute required.");
		}

		if (!NWBFileProperty.TYPE_STRING.equals(nodeSchema.get(TYPE_COLUMN))) {
			throw new AlgorithmCreationFailedException(
					"Bipartite Graph algorithm requires output of the Extract Bipartite Graph algorithm:"
							+ " 'bipartitetype' node attribute must be of type 'string'.");
		}
	}

	@Override
	public void addNode(int id, String label, Map<String, Object> attributes) {
		super.addNode(id, label, attributes);
		bipartiteTypes.add((String) attributes.get(TYPE_COLUMN));
	}
}
