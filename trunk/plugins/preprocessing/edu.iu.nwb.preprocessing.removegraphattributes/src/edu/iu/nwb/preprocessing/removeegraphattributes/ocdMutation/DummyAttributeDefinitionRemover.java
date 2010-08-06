package edu.iu.nwb.preprocessing.removeegraphattributes.ocdMutation;

import org.cishell.reference.service.metatype.BasicObjectClassDefinition;
import org.cishell.utilities.MutateParameterUtilities;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

/* Remove the AttributeDefinition with the given ID
 * from a given ObjectClassDefinition.
 * 
 * This is useful when we want an ObjectClassDefinition with only mutated
 * parameters.  If no AttributeDefinitions are specified in the plugin's
 * METADATA.XML, then a ParameterMutator will receive
 * a null ObjectClassDefinition in it's mutateParameters method.
 * 
 * Specify a dummy AttributeDefinition in the METADATA.XML, then remove it
 * with this tool.
 */
public class DummyAttributeDefinitionRemover {	
	public static final String DEFAULT_DUMMY_ATTRIBUTE_DEFINITION_ID = "DUMMY_AD";

	private String dummyAttributeDefinitionID;
	
	public DummyAttributeDefinitionRemover() {
		this(DEFAULT_DUMMY_ATTRIBUTE_DEFINITION_ID);
	}
	
	public DummyAttributeDefinitionRemover(String dummyAttributeDefinitionID) {
		this.dummyAttributeDefinitionID = dummyAttributeDefinitionID;
	}
	
	public ObjectClassDefinition removeFrom(
			ObjectClassDefinition dummiedParameters) {
		BasicObjectClassDefinition properParameters =
			MutateParameterUtilities.createNewParameters(dummiedParameters);

		AttributeDefinition[] ads =
			dummiedParameters.getAttributeDefinitions(
					ObjectClassDefinition.ALL);
		for (int ii = 0; ii < ads.length; ii++) {
			AttributeDefinition ad = ads[ii];

			if (!dummyAttributeDefinitionID.equals(ad.getName())) {
				properParameters.addAttributeDefinition(
						ObjectClassDefinition.REQUIRED, ad);
			}
		}

		return properParameters;
	}
}
