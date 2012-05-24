package edu.iu.nwb.util.nwbfile.pipe;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.common.base.Joiner;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import edu.iu.nwb.util.nwbfile.NWBFileParserHandler;

abstract class NodeAttributeComputer extends NodeAttributeAdder {
	protected final Map<String, String> schemaUpdates;
	protected final FieldMakerFunction computer;
		
	NodeAttributeComputer(NWBFileParserHandler delegate, Map<String, String> schemaUpdates,
			FieldMakerFunction computer) {
		super(delegate, schemaUpdates);
		
		this.schemaUpdates = schemaUpdates;
		this.computer = computer;
	}

	
	abstract Map<String, Object> update(Map<String, Object> attributes);


	@Override
	public void addNode(int id, String label, Map<String, Object> attributes) {
		super.addNode(id, label, update(Maps.newHashMap(attributes)));
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this)
				.add("schemaUpdates", schemaUpdates)
				.add("computer", computer)
				.add("next", nextToString())
				.toString();
	}


	/**
	 * @see ParserPipe#addComputedNodeAttributes(Map, FieldMakerFunction)
	 */
	static final class AdditiveNodeAttributeComputer extends NodeAttributeComputer {
		public AdditiveNodeAttributeComputer(NWBFileParserHandler delegate,
				Map<String, String> schemaUpdates, FieldMakerFunction computer) {
			super(delegate, schemaUpdates, computer);
		}
		
		@Override
		public void setNodeSchema(LinkedHashMap<String, String> schema) {
			Preconditions.checkArgument(
					AdditiveNodeAttributeComputer.keysDistinct(schemaUpdates, schema),
					"The new attribute names declared (%s) must not collide with any existing " +
							"attribute names in the schema (%s).",
					Joiner.on(", ").join(schemaUpdates.keySet()),
					Joiner.on(", ").join(schema.keySet()));
			
			super.setNodeSchema(schema);
		}

		@Override
		public Map<String, Object> update(Map<String, Object> original) {
			if (! AdditiveNodeAttributeComputer.keysDistinct(schemaUpdates, original)) {
				throw new RuntimeException(String.format(
						"The new attribute names declared (%s) must not collide with any " +
								"existing attribute names for this node (%s).",
						Joiner.on(", ").join(schemaUpdates.keySet()),
						Joiner.on(", ").join(original.keySet())));
			}
			
			Map<String, Object> computed = computer.compute(original);
			
			if (! schemaUpdates.keySet().containsAll(computed.keySet())) {
				throw new RuntimeException(String.format(
						"All new attribute names computed (%s) must be among those declared in " +
								"the schema updates (%s).",
						Joiner.on(", ").join(computed.keySet()),
						Joiner.on(", ").join(schemaUpdates.keySet())));
			}
					
			
			Map<String, Object> result =
					Maps.newHashMapWithExpectedSize(original.size() + computed.size());
			result.putAll(original);
			result.putAll(computed);
			
			return result;
		}

		private static boolean keysDistinct(Map<?, ?> map1, Map<?, ?> map2) {
			Collection<?> commonKeys = Sets.intersection(map1.keySet(), map2.keySet());
			
			return commonKeys.isEmpty();
		}
	}
}
