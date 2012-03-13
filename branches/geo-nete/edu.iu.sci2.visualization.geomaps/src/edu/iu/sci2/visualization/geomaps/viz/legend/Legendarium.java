package edu.iu.sci2.visualization.geomaps.viz.legend;

import java.util.Collection;

import org.antlr.stringtemplate.StringTemplate;

import edu.iu.sci2.visualization.geomaps.GeoMapsAlgorithm;

public class Legendarium {
	private final Collection<LabeledReference> labeledReferences;
	private boolean hasPrintedDefinitions;

	private Legendarium(Collection<LabeledReference> labeledReferences) {
		this.labeledReferences = labeledReferences;
		this.hasPrintedDefinitions = false;		
	}
	public static Legendarium containing(Collection<LabeledReference> labeledReferences) {
		return new Legendarium(labeledReferences);
	}
	

	public String toPostScript() {
		String s = "";

		if (!hasPrintedDefinitions) {
			StringTemplate definitionsTemplate =
				GeoMapsAlgorithm.TEMPLATE_GROUP.getInstanceOf("legendDefinitions");
			
			s += definitionsTemplate.toString();
			
			this.hasPrintedDefinitions = true;
		}
		
		for (LabeledReference labeledReference : labeledReferences) {
			s += labeledReference.toPostScript();
			s += "\n";
		}

		return s;
	}
}
