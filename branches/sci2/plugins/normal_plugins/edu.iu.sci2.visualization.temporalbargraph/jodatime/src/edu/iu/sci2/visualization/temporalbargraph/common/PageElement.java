package edu.iu.sci2.visualization.temporalbargraph.common;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;

public class PageElement {
	protected String element;
	protected String elementDefinitions;
	protected String pagePositionDefinition;

	public static final String PAGE_POSITION_DEFINITION_STRING_TEMPLATE_FILE_PATH = "/edu/iu/sci2/visualization/temporalbargraph/common/stringtemplates/utilities.st";
	protected static StringTemplateGroup pagePositionDefinitionsGroup = new StringTemplateGroup(
			new InputStreamReader(
					PageElement.class
							.getResourceAsStream(PAGE_POSITION_DEFINITION_STRING_TEMPLATE_FILE_PATH)));

	public PageElement(String name, double leftPosition, double bottomPosition,
			StringTemplate element, StringTemplate elementDefinitions) {
		this(getPagePositionDefinition(name, leftPosition, bottomPosition), element.toString(), elementDefinitions.toString());
	}

	public PageElement(String name, double leftPosition, double bottomPosition,
			String element, String elementDefinitions) {
		this(getPagePositionDefinition(name, leftPosition, bottomPosition), element, elementDefinitions);
	}
	
	private PageElement(String pagePositionDefinition, String element, String elementDefinitions){
		this.pagePositionDefinition = pagePositionDefinition;
		this.element = element;
		this.elementDefinitions = elementDefinitions;
	}

	protected static String getPagePositionDefinition(String name, double leftPosition, double bottomPosition){
		Map<String, String> pagePositionDefinitionAttributes = new HashMap<String, String>();
		pagePositionDefinitionAttributes.put("element", name);
		pagePositionDefinitionAttributes.put("left",
				Double.toString(leftPosition));
		pagePositionDefinitionAttributes.put("bottom",
				Double.toString(bottomPosition));
		return pagePositionDefinitionsGroup
				.getInstanceOf("pagePositionDefinitions",
						pagePositionDefinitionAttributes).toString();
	}
	public String renderDefinitionsPostscript() {
		StringBuilder definitions = new StringBuilder();
		definitions.append(pagePositionDefinition);
		definitions.append(elementDefinitions);
		return definitions.toString();
	}

	public String renderPostscript() {
		return element;
	}
	
}
