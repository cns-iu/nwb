package edu.iu.sci2.visualization.bipartitenet.model;

import static org.junit.Assert.*;

import java.util.LinkedHashMap;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;

import edu.iu.nwb.util.nwbfile.NWBFileProperty;

public class NodeHandlerTest {

	@Test
	public void testCreation() {
		new NodeHandler("asdf", "jkl", "semi", Ordering.arbitrary(), Ordering.arbitrary());
	}

	@Test
	public void testAddNode() {
		NodeHandler nh = new NodeHandler("asdf", null, "semi", Ordering.arbitrary(), Ordering.arbitrary());
		
		nh.addNode(1, "hi", ImmutableMap.of("asdf", "semi"));
	}
	
	@Test
	public void testSetSchema() {
		NodeHandler nh = new NodeHandler("asdf", null, "semi", Ordering.arbitrary(), Ordering.arbitrary());
		LinkedHashMap<String,String> schema = Maps.newLinkedHashMap();
		schema.put("id", NWBFileProperty.TYPE_INT);
		schema.put("label", NWBFileProperty.TYPE_STRING);
		schema.put("asdf", NWBFileProperty.TYPE_STRING);
		nh.setNodeSchema(schema);
		assertEquals(schema, nh.getNodeSchema());
	}
	
	
	@Test
	public void testGetLeftNodes() {
		NodeHandler nh = new NodeHandler("asdf", null, "semi", Ordering.arbitrary(), Ordering.arbitrary());
		
		nh.addNode(1, "hi", ImmutableMap.of("asdf", "semi"));
		ImmutableList<Node> leftNodes = nh.getLeftNodes();
		assertNotNull(leftNodes);
		assertEquals(1, leftNodes.size());
		
		Node n = leftNodes.get(0);
		assertEquals("hi", n.getLabel());
	}
	
	@Test
	public void testNodesAreSorted() {
		NodeHandler nh = new NodeHandler("asdf", null, "semi", Node.LABEL_ORDERING, Ordering.arbitrary());
		
		nh.addNode(1, "c", ImmutableMap.of("asdf", "semi"));
		nh.addNode(2, "b", ImmutableMap.of("asdf", "semi"));
		nh.addNode(3, "a", ImmutableMap.of("asdf", "semi"));
		
		ImmutableList<Node> nodes = nh.getLeftNodes();
		assertNotNull(nodes);
		assertEquals(3, nodes.size());
		
		assertEquals("a", nodes.get(0).getLabel());
		assertEquals("b", nodes.get(1).getLabel());
		assertEquals("c", nodes.get(2).getLabel());
	}
	
	@Test
	public void testNumericSorting() {
		NodeHandler nh = new NodeHandler("asdf", null, "semi", Node.NUMERIC_LABEL_ORDERING, Ordering.arbitrary());
		
		nh.addNode(1, "1234", ImmutableMap.of("asdf", "semi"));
		nh.addNode(2, "33", ImmutableMap.of("asdf", "semi"));
		nh.addNode(3, "8", ImmutableMap.of("asdf", "semi"));
		
		ImmutableList<Node> nodes = nh.getLeftNodes();
		assertNotNull(nodes);
		assertEquals(3, nodes.size());
		
		assertEquals(nh.getNodeById(3), nodes.get(0));
		assertEquals(nh.getNodeById(2), nodes.get(1));
		assertEquals(nh.getNodeById(1), nodes.get(2));
	}
	
	
	@Test
	public void testGetRightNodes() {
		NodeHandler nh = new NodeHandler("asdf", null, "semi", Ordering.arbitrary(), Ordering.arbitrary());
		
		nh.addNode(1, "hi", ImmutableMap.of("asdf", "qwerty"));
		ImmutableList<Node> nodes = nh.getRightNodes();
		assertNotNull(nodes);
		assertEquals(1, nodes.size());
		
		Node n = nodes.get(0);
		assertEquals("hi", n.getLabel());
	}
	
	@Test
	public void testGetNodeById() {
		NodeHandler nh = new NodeHandler("asdf", null, "semi", Ordering.arbitrary(), Ordering.arbitrary());
		
		nh.addNode(1, "hi", ImmutableMap.of("asdf", "semi"));
		assertNotNull(nh.getNodeById(1));
	}
}
