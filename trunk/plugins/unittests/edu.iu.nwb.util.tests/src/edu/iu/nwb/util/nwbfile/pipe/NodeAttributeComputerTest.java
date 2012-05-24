package edu.iu.nwb.util.nwbfile.pipe;

import java.io.Reader;
import java.io.StringReader;
import java.util.Map;

import org.cishell.utilities.NumberUtilities;
import org.easymock.EasyMock;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import edu.iu.nwb.util.nwbfile.NWBFileParser;
import edu.iu.nwb.util.nwbfile.NWBFileParserHandler;
import edu.iu.nwb.util.nwbfile.NWBFileProperty;
import edu.iu.nwb.util.nwbfile.pipe.NodeAttributeComputer.AdditiveNodeAttributeComputer;

public class NodeAttributeComputerTest {
	private static final ImmutableMap<String, String> SCHEMA_UPDATES =
			ImmutableMap.of(
					"sum", NWBFileProperty.TYPE_INT,
					"difference", NWBFileProperty.TYPE_INT);
	private static final FieldMakerFunction SUM_AND_DIFFERENCE = new FieldMakerFunction() {
		public Map<String, Object> compute(Map<String, Object> input) {
			int a = NumberUtilities.interpretObjectAsInteger(input.get("a"));
			int b = NumberUtilities.interpretObjectAsInteger(input.get("b"));
			
			return ImmutableMap.<String, Object>of(
					"sum", a + b,
					"difference", a - b);
		}
	};
	

	@Test
	public void testSumAndDifference() throws Exception {
		NWBFileParserHandler mock = EasyMock.createNiceMock(NWBFileParserHandler.class);
		// Check that node schema is updated to include new attributes
		mock.setNodeSchema(Maps.newLinkedHashMap(
				ImmutableMap.<String, String>builder()
						.putAll(NWBFileProperty.NECESSARY_NODE_ATTRIBUTES)
						.put("a", NWBFileProperty.TYPE_INT)
						.put("b", NWBFileProperty.TYPE_INT)
						.putAll(SCHEMA_UPDATES)
						.build()));
		// Check that node 1 has correct sum and difference values
		mock.addNode(1, "five three",
				ImmutableMap.<String, Object>of(
						"a", 5,
						"b", 3,
						"sum", 8,
						"difference", 2));
		// Check that node 2 has correct sum and difference values
		mock.addNode(2, "seven two",
				ImmutableMap.<String, Object>of(
						"a", 7,
						"b", 2,
						"sum", 9,
						"difference", 5));
		EasyMock.replay(mock);

		
		NWBFileParser parser = new NWBFileParser(getTestNWB());
		parser.parse(new AdditiveNodeAttributeComputer(mock, SCHEMA_UPDATES, SUM_AND_DIFFERENCE));

		EasyMock.verify(mock);
	}
	
	private static Reader getTestNWB() {
		StringBuilder s = new StringBuilder();

		s.append("*Nodes 2\n");
		s.append("id*int	label*string	a*int	b*int\n");
		s.append("1	\"five three\"	5	3\n");
		s.append("2	\"seven two\"	7	2\n");
		s.append("*DirectedEdges 0\n");
		s.append("source*int	target*int	weight*float\n");

		return new StringReader(s.toString());
	}
}
