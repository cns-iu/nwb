package edu.iu.sci2.visualization.geomaps.viz.ps;

import java.io.InputStreamReader;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;

import com.google.common.collect.ImmutableMap;

import edu.iu.sci2.visualization.geomaps.GeoMapsAlgorithm;
import edu.iu.sci2.visualization.geomaps.utility.Dimension;

/** 
 * DSC = Document Structuring Conventions, the standards that a piece of
 * PostScript code must obey to be valid Encapsulated PostScript.
 * 
 * The DSC Prolog is a set of PostScript comments at the start of the file
 * that describe some aspects of the content.
 */
public class DSCProlog implements PostScriptable {
	public static final String STRING_TEMPLATE_FILE_PATH =
			"/edu/iu/sci2/visualization/geomaps/viz/stringtemplates/dscProlog.stg";
	public static StringTemplateGroup TEMPLATE_GROUP = loadTemplateGroup();
	
	private final String title;
	private final Dimension<Double> pageDimensions;
	
	
	public DSCProlog(String title, Dimension<Double> pageDimensions) {
		this.title = title;
		this.pageDimensions = pageDimensions;
	}
	
	
	@Override
	public String toPostScript() {
		StringTemplate template = TEMPLATE_GROUP.getInstanceOf("dscProlog");
		template.setAttributes(ImmutableMap.of(
				"title", title,
				"width", pageDimensions.getWidth(),
				"height", pageDimensions.getHeight()));

		return template.toString();
	}
	
	private static StringTemplateGroup loadTemplateGroup() {
		return new StringTemplateGroup(
				new InputStreamReader(
					GeoMapsAlgorithm.class.getResourceAsStream(STRING_TEMPLATE_FILE_PATH)));
	}
}
