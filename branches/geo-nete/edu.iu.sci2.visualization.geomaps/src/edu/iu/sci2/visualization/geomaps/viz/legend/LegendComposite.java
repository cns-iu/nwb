package edu.iu.sci2.visualization.geomaps.viz.legend;

import java.util.Collection;
import java.util.List;

import org.antlr.stringtemplate.StringTemplate;

import com.google.common.collect.Lists;

import edu.iu.sci2.visualization.geomaps.GeoMapsAlgorithm;
import edu.iu.sci2.visualization.geomaps.viz.ps.PostScriptable;

public class LegendComposite {
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
