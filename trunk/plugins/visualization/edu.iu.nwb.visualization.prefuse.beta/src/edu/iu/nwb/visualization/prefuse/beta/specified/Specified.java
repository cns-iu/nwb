package edu.iu.nwb.visualization.prefuse.beta.specified;

import org.cishell.framework.data.Data;
import org.cishell.reference.service.metatype.BasicAttributeDefinition;
import org.cishell.reference.service.metatype.BasicObjectClassDefinition;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

import edu.iu.nwb.visualization.prefuse.beta.common.Constants;
import edu.iu.nwb.visualization.prefuse.beta.common.PrefuseBetaAlgorithmFactory;
import edu.iu.nwb.visualization.prefuse.beta.common.PrefuseBetaVisualization;



/**
 * @author Weixia(Bonnie) Huang 
 */
public class Specified extends PrefuseBetaAlgorithmFactory {
	protected PrefuseBetaVisualization getVisualization() {
    	return new SpecifiedVisualization();
	}
	
	protected AttributeDefinition[] createAttributeDefinitions(AttributeDefinition[] oldAttributeDefinitions,
															   String[] nodeAttributesArray,
															   String[] edgeAttributesArray)
	{
		final int numAttributeDefinitions = oldAttributeDefinitions.length;
		AttributeDefinition[] newAttributeDefinitions = new AttributeDefinition [numAttributeDefinitions];
		
		for (int ii = 0; ii < numAttributeDefinitions; ii++)
		{
			String id = oldAttributeDefinitions[ii].getID();
		
			if(id.equals(Constants.x))
			{
				newAttributeDefinitions[ii] = new BasicAttributeDefinition(Constants.x,
					"X", "The label of the x dimension", AttributeDefinition.STRING,
					nodeAttributesArray, nodeAttributesArray);
			}
			else if(id.equals(Constants.y))
			{
				newAttributeDefinitions[ii] = new BasicAttributeDefinition(Constants.y, "Y",
					"The label of the y dimension", AttributeDefinition.STRING,
					nodeAttributesArray, nodeAttributesArray);
			}
			else
				newAttributeDefinitions[ii] = oldAttributeDefinitions[ii];
		}
		
		return newAttributeDefinitions;
	}
}
