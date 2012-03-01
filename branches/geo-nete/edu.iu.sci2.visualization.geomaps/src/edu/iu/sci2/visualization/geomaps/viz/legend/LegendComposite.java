package edu.iu.sci2.visualization.geomaps.viz.legend;

import java.util.Collection;
import java.util.List;

import org.antlr.stringtemplate.StringTemplate;

import com.google.common.collect.Lists;

import edu.iu.sci2.visualization.geomaps.GeoMapsAlgorithm;
import edu.iu.sci2.visualization.geomaps.viz.Constants;
import edu.iu.sci2.visualization.geomaps.viz.ps.PostScriptable;

public class LegendComposite {
	public static final double DEFAULT_LOWER_LEFT_X_IN_POINTS =
		Constants.LEGEND_PAGE_AREA_LOWER_LEFT_X_IN_POINTS;
	public static final double DEFAULT_LOWER_LEFT_Y_IN_POINTS =
		Constants.LEGEND_PAGE_AREA_LOWER_LEFT_Y_IN_POINTS
		+ (0.75 * Constants.LEGEND_PAGE_AREA_HEIGHT_IN_POINTS); //Constants.LEGEND_PAGE_AREA_DIMENSION_IN_POINTS.getHeight());
	public static final double DEFAULT_WIDTH_IN_POINTS = Constants.LEGEND_PAGE_AREA_WIDTH_IN_POINTS; //Constants.LEGEND_PAGE_AREA_DIMENSION_IN_POINTS.getWidth();

	private final List<PostScriptable> components;
	
	private boolean hasPrintedDefinitions;

	public LegendComposite() {
		this.components = Lists.newLinkedList();
		
		this.hasPrintedDefinitions = false;
	}

	public String toPostScript() {
		String s = "";

		if (!hasPrintedDefinitions) {
			StringTemplate definitionsTemplate =
				GeoMapsAlgorithm.TEMPLATE_GROUP.getInstanceOf("legendDefinitions");
			
			s += definitionsTemplate.toString();
			
			this.hasPrintedDefinitions = true;
		}
		
		for (PostScriptable postScriptable : components) {
			s += postScriptable.toPostScript();
			s += "\n";
		}

		return s;
	}

	public boolean add(PostScriptable postScriptable) {
		if (postScriptable != null) {
			return components.add(postScriptable);
		}
		
		return false;
	}
	
	public boolean addAll(Collection<? extends PostScriptable> legendComponents) {
		return components.addAll(legendComponents);
	}
}
