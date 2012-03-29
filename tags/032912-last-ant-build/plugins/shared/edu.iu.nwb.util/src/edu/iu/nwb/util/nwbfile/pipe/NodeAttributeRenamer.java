package edu.iu.nwb.util.nwbfile.pipe;

import java.util.LinkedHashMap;
import java.util.Map;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import edu.iu.nwb.util.nwbfile.NWBFileParserHandler;


/**
 * The behavior is not defined when you're renaming A to B, but B is already an attribute.
 * @author thgsmith
 *
 */
class NodeAttributeRenamer extends ParserStage {
	public static NodeAttributeRenamer create(NWBFileParserHandler handler,
			Map<String, String> renames) {
		return new NodeAttributeRenamer(handler, renames);
	}

	private ImmutableMap<String, String> renames;

	private NodeAttributeRenamer(NWBFileParserHandler handler, Map<String, String> renames) {
		super(handler);
		this.renames = ImmutableMap.copyOf(renames);
	}
	
	@Override
	public void setNodeSchema(LinkedHashMap<String, String> schema) {
		super.setNodeSchema(renameKeys(schema));
	}

	private <T> LinkedHashMap<String, T> renameKeys(
			Map<String, T> toModify) {
		LinkedHashMap<String, T> modified = Maps.newLinkedHashMap();
		
		// this will be in order if the input is a LinkedHashMap:
		for (Map.Entry<String, T> entry : toModify.entrySet()) {
			String field = entry.getKey();
			if (renames.containsKey(field)) {
				modified.put(renames.get(field), entry.getValue());
			} else {
				modified.put(field, entry.getValue());
			}
		}
		return modified;
	}

	@Override
	public void addNode(int id, String label, Map<String, Object> attributes) {
		super.addNode(id, label, renameKeys(attributes));
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this)
				.add("renames", renames)
				.add("next", nextToString())
				.toString();
	}
}
