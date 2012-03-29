package edu.iu.cns.graphstream.common;

import java.util.HashMap;
import java.util.Map;
import java.util.Collection;

import org.graphstream.graph.Element;

public class Utilities {
	public static Map<String, Object> getElementAttributes(Element element) {
		Map<String, Object> attributes = new HashMap<String, Object>();

		for (String attributeName : element.getAttributeKeySet()) {
			attributes.put(attributeName, element.getAttribute(attributeName));
		}

		return attributes;
	}

	public static void addNodeAttribute(
			AnnotatedGraph graph, String attributeName, String attributeType) {
		graph.getNodeSchema().put(attributeName, attributeType);
		addAttributeToElements(graph.getNodeSet(), attributeName);
	}

	public static void addAttributeToElements(
			Collection<? extends Element> elements, String attributeName) {
		for (Element element : elements) {
			element.addAttribute(attributeName, new Object[0]);
		}
	}
}