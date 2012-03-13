package edu.iu.sci2.visualization.geomaps.viz.legend;

import java.util.Collection;

import org.antlr.stringtemplate.StringTemplate;

import edu.iu.sci2.visualization.geomaps.GeoMapsAlgorithm;
import edu.iu.sci2.visualization.geomaps.viz.PageLayout;
import edu.iu.sci2.visualization.geomaps.viz.ps.GeoMapViewPS;
import edu.iu.sci2.visualization.geomaps.viz.ps.PSUtility;

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
		
		s += "gsave" + "\n";
		s += String.format("%f %f moveto" + "\n", PageLayout.LEGENDARIUM_LOWER_LEFT.x, PageLayout.LEGENDARIUM_LOWER_LEFT.y);
		
		s += PSUtility.findscalesetfont(GeoMapViewPS.TITLE_FONT) + "\n";
		s += PSUtility.setgray(0.0) + "\n"; // TODO
		s += "(Legend) show" + "\n";
		s += "grestore" + "\n";
		
		for (LabeledReference labeledReference : labeledReferences) {
			s += labeledReference.toPostScript();
			s += "\n";
		}

		return s;
	}
}
