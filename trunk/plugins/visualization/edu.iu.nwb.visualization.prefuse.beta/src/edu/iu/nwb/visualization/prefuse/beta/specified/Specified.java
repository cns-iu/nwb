package edu.iu.nwb.visualization.prefuse.beta.specified;

import java.util.Arrays;
import java.util.List;
import org.cishell.utilities.ArrayUtilities;
import org.cishell.reference.service.metatype.BasicAttributeDefinition;
import org.osgi.service.metatype.AttributeDefinition;

import edu.iu.nwb.visualization.prefuse.beta.common.Constants;
import edu.iu.nwb.visualization.prefuse.beta.common.PrefuseBetaAlgorithmFactory;
import edu.iu.nwb.visualization.prefuse.beta.common.PrefuseBetaVisualization;
/**
 * @author Weixia(Bonnie) Huang 
 */
public class Specified extends PrefuseBetaAlgorithmFactory {
	public static final List X_IDS_TO_GUESS =
		Arrays.asList(new String[]{
			"x", "X", "xpos", "xPos", "XPos", "XPOS"
		});
	
	public static final List Y_IDS_TO_GUESS =
		Arrays.asList(new String[]{
			"y", "Y", "ypos", "yPos", "YPos", "YPOS"
		});
	
	protected PrefuseBetaVisualization getVisualization() {
    	return new SpecifiedVisualization();
	}
	
	protected AttributeDefinition[] createAttributeDefinitions(
			AttributeDefinition[] oldAttributeDefinitions,
			String[] nodeAttributes,
			String[] edgeAttributes) {
		final int numAttributeDefinitions = oldAttributeDefinitions.length;
		AttributeDefinition[] newAttributeDefinitions =
			new AttributeDefinition[numAttributeDefinitions];
		
		for (int ii = 0; ii < numAttributeDefinitions; ii++) {
			String id = oldAttributeDefinitions[ii].getID();
		
			if (Constants.X_ID.equals(id)) {
				/* If we find a what looks to be an appropriate attribute for x,
				 * we swap it to the front of the array so that it will be
				 * given to the user as a default choice for x.
				 */
				ArrayUtilities.swapFirstMatchToFront(
						nodeAttributes,
						X_IDS_TO_GUESS);
				
				newAttributeDefinitions[ii] =
					new BasicAttributeDefinition(
							Constants.X_ID,
							"X",
							"The label of the x dimension",
							AttributeDefinition.STRING,
							(String[]) ArrayUtilities.clone(nodeAttributes),
							(String[]) ArrayUtilities.clone(nodeAttributes));
			} else if (Constants.Y_ID.equals(id)) {
				/* If we find a what looks to be an appropriate attribute for y,
				 * we swap it to the front of the array so that it will be
				 * given to the user as a default choice for y.
				 */
				ArrayUtilities.swapFirstMatchToFront(
						nodeAttributes,
						Y_IDS_TO_GUESS);
				
				newAttributeDefinitions[ii] =
					new BasicAttributeDefinition(
							Constants.Y_ID,
							"Y",
							"The label of the y dimension",
							AttributeDefinition.STRING,
							(String[]) ArrayUtilities.clone(nodeAttributes),
							(String[]) ArrayUtilities.clone(nodeAttributes));
			} else {
				newAttributeDefinitions[ii] = oldAttributeDefinitions[ii];
			}
		}
		
		return newAttributeDefinitions;
	}
}
