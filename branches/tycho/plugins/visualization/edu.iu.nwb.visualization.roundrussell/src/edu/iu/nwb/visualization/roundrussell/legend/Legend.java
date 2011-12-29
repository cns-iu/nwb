package edu.iu.nwb.visualization.roundrussell.legend;

import java.util.ArrayList;
import java.util.List;

import org.antlr.stringtemplate.StringTemplate;
import org.cishell.framework.algorithm.AlgorithmExecutionException;

import edu.iu.nwb.visualization.roundrussell.RoundRussellAlgorithm;
import edu.iu.nwb.visualization.roundrussell.utility.Constants;

public class Legend {	
	public static final double DEFAULT_LOWER_LEFT_X_IN_POINTS = 
		Constants.LEGEND_PAGE_AREA_LOWER_LEFT_X_IN_POINTS;
	
	public static final double DEFAULT_LOWER_LEFT_Y_IN_POINTS =
		Constants.LEGEND_PAGE_AREA_LOWER_LEFT_Y_IN_POINTS 
		+ (0.5 * Constants.LEGEND_PAGE_AREA_HEIGHT_IN_POINTS);
	
	
	public static final double DEFAULT_WIDTH_IN_POINTS = Constants.LEGEND_PAGE_AREA_WIDTH_IN_POINTS;
	
	private List<LegendComponent> components;
	
	private boolean hasPrintedDefinitions;

	public Legend() {
		this.components = new ArrayList<LegendComponent>();
		
		this.hasPrintedDefinitions = false;
		
	}

	public String toPostScript() throws AlgorithmExecutionException {
		String s = "";

		if (!hasPrintedDefinitions) {
			StringTemplate definitionsTemplate =
				RoundRussellAlgorithm.group.getInstanceOf("legendDefinitions");
			
			s += definitionsTemplate.toString();
			
			this.hasPrintedDefinitions = true;
		}
		
		
		
		for ( LegendComponent legendComponent : components ) {
			s += legendComponent.toPostScript();
			s += "\n";
		}

		return s;
	}

	public void add(LegendComponent legendComponent) {
		components.add(legendComponent);
	}
}
