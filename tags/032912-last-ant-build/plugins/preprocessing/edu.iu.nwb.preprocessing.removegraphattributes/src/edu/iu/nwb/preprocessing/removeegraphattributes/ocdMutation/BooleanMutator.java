package edu.iu.nwb.preprocessing.removeegraphattributes.ocdMutation;

import java.util.Collection;
import java.util.Iterator;

import org.cishell.reference.service.metatype.BasicAttributeDefinition;
import org.cishell.reference.service.metatype.BasicObjectClassDefinition;
import org.cishell.utilities.MutateParameterUtilities;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

/* For each name in names, add a Boolean AttributeDefinition (whose description
 * is created by DescriptionCreator) to the given
 * ObjectClassDefinition .
 */
public class BooleanMutator {	
	private Collection<String> names;
	private DescriptionCreator descriptionCreator;


	public BooleanMutator(
			Collection<String> names, DescriptionCreator descriptionCreator) {
		this.names = names;
		this.descriptionCreator = descriptionCreator;
	}
	
	
	public ObjectClassDefinition addOptionsTo(ObjectClassDefinition oldOCD) {
		BasicObjectClassDefinition newOCD =
			MutateParameterUtilities.createNewParameters(oldOCD);
		
		// Bring over all the old AttributeDefinitions
		AttributeDefinition[] attributeDefinitions =
			oldOCD.getAttributeDefinitions(ObjectClassDefinition.ALL);
		for (int ii = 0; ii < attributeDefinitions.length; ii++) {
			newOCD.addAttributeDefinition(
					ObjectClassDefinition.REQUIRED, attributeDefinitions[ii]);
		}
		
		// Add a Boolean AttributeDefinition for each name in names
		for (Iterator<String> optionIt = names.iterator(); optionIt.hasNext();) {
			String name = (String) optionIt.next();
			
			AttributeDefinition attributeDefinition =
				new BasicAttributeDefinition(
					name,
					name,
					descriptionCreator.createFromName(name),
					AttributeDefinition.BOOLEAN);
			
			newOCD.addAttributeDefinition(
					ObjectClassDefinition.REQUIRED, attributeDefinition);
		}
	
		return newOCD;
	}
	
	
	public interface DescriptionCreator {
		String createFromName(String name);
	}
}
