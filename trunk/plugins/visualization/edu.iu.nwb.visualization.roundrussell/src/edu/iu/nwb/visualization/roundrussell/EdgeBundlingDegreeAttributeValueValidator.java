package edu.iu.nwb.visualization.roundrussell;

import org.cishell.reference.service.metatype.BasicAttributeDefinition;

public class EdgeBundlingDegreeAttributeValueValidator extends BasicAttributeDefinition {

	public EdgeBundlingDegreeAttributeValueValidator(String id, String name,
			String description, int type, String defaultValue) {
		super(id, name, description, type, defaultValue);
	}
	
	public String validate(String value) {
		double edgeBundlingDegreeValue;
		try{
			edgeBundlingDegreeValue = Double.parseDouble(value);
		}
		catch (Exception e) {
			edgeBundlingDegreeValue = -1.0;
		}
		
		if(edgeBundlingDegreeValue < 0.0 || edgeBundlingDegreeValue > 1.0) {
			return "Edge bundling degree should be between 0.0 & 1.0.";
		}
		else {
			return "";
		}
	}

}
