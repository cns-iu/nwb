package edu.iu.sci2.visualization.geomaps.viz.legend;

import java.awt.geom.Point2D;
import java.util.Collection;

import org.antlr.stringtemplate.StringTemplate;

import edu.iu.sci2.visualization.geomaps.GeoMapsAlgorithm;
import edu.iu.sci2.visualization.geomaps.viz.PageLayout;
import edu.iu.sci2.visualization.geomaps.viz.ps.PSUtility;

public class Legendarium {
	private final Point2D.Double lowerLeft;
	private final PageLayout pageLayout;
	private final Collection<LabeledReference> labeledReferences;
	
	private boolean hasPrintedDefinitions;

	private Legendarium(Point2D.Double lowerLeft, PageLayout pageLayout, Collection<LabeledReference> labeledReferences) {
		this.lowerLeft = lowerLeft;
		this.pageLayout = pageLayout;
		this.labeledReferences = labeledReferences;
		this.hasPrintedDefinitions = false;		
	}
	public static Legendarium containing(Point2D.Double lowerLeft, PageLayout pageLayout, Collection<LabeledReference> labeledReferences) {
		return new Legendarium(lowerLeft, pageLayout, labeledReferences);
	}
	

	public String toPostScript() {
		if (labeledReferences.isEmpty()) {
			return " ";
		}
		
		String s = "";

		if (!hasPrintedDefinitions) {
			StringTemplate definitionsTemplate =
				GeoMapsAlgorithm.TEMPLATE_GROUP.getInstanceOf("legendDefinitions");
			
			s += definitionsTemplate.toString();
			
			this.hasPrintedDefinitions = true;
		}
		
		s += "gsave" + "\n";
		s += String.format("%f %f moveto" + "\n", lowerLeft.x, lowerLeft.y);
		
		s += PSUtility.findscalesetfont(pageLayout.titleFont()) + "\n";
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
