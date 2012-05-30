package edu.iu.sci2.visualization.geomaps.viz.ps;

import java.awt.geom.Point2D;
import java.util.Collection;

import org.antlr.stringtemplate.StringTemplate;

import edu.iu.sci2.visualization.geomaps.viz.PageLayout;
import edu.iu.sci2.visualization.geomaps.viz.legend.LabeledReference;

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
		
		StringBuilder ps = new StringBuilder();

		if (!hasPrintedDefinitions) {
			StringTemplate definitionsTemplate =
				GeoMapViewPS.TEMPLATE_GROUP.getInstanceOf("legendDefinitions");
			
			ps.append(definitionsTemplate.toString());
			
			this.hasPrintedDefinitions = true;
		}
		
		ps.append("gsave" + "\n");
		ps.append(String.format("%f %f moveto" + "\n", lowerLeft.x, lowerLeft.y));
		
		ps.append(PSUtility.findscalesetfont(pageLayout.titleFont()) + "\n");
		ps.append(PSUtility.setgray(0.0) + "\n");
		ps.append("(Legend) show" + "\n");
		ps.append("grestore" + "\n");
		
		for (LabeledReference labeledReference : labeledReferences) {
			ps.append(labeledReference.toPostScript());
			ps.append("\n");
		}

		return ps.toString();
	}
}
