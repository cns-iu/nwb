package edu.iu.nwb.util.nwbfile.pipe;

import static org.junit.Assert.*;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import edu.iu.nwb.util.nwbfile.NWBFileParserAdapter;
import edu.iu.nwb.util.nwbfile.NWBFileProperty;

public class NodeAttributeRemoverTest {
	static class NodeAttributeSaver extends NWBFileParserAdapter {
		public Map<String, String> schema;
		public Map<String, Object> lastAttributes;

		@Override
		public void setNodeSchema(LinkedHashMap<String, String> schema) {
			this.schema = schema;
		}

		@Override
		public void addNode(int id, String label, Map<String, Object> attributes) {
			this.lastAttributes = attributes;
		}

	}

	@Test
	public void testAttributeRemoval() {
		NodeAttributeSaver mock = new NodeAttributeSaver();
		NodeAttributeRemover remover = new NodeAttributeRemover(mock,
				"dontwant");

		remover.setNodeSchema(Maps.newLinkedHashMap(ImmutableMap.of("dontwant",
				NWBFileProperty.TYPE_STRING, "want",
				NWBFileProperty.TYPE_STRING)));
		assertEquals("Remover did not remove unwanted attribute from schema",
				ImmutableMap.of("want", NWBFileProperty.TYPE_STRING),
				mock.schema);

		remover.addNode(1, "a node", ImmutableMap.<String, Object> of(
				"dontwant", "yuck", "want", "yum"));
		assertEquals("Remover did not remove unwanted attribute from node",
				ImmutableMap.of("want", "yum"),
				mock.lastAttributes);
	}

}
