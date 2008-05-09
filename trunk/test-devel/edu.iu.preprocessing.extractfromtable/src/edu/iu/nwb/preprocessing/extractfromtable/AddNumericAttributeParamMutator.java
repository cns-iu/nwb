package edu.iu.nwb.preprocessing.extractfromtable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.cishell.reference.service.metatype.BasicAttributeDefinition;
import org.cishell.reference.service.metatype.BasicObjectClassDefinition;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

import prefuse.data.Table;

public class AddNumericAttributeParamMutator {

	public static ObjectClassDefinition mutateForTopNodesOrEdges(Table table, ObjectClassDefinition ocd) {
		String[] attributes = GraphUtil.getColumnNames(table);
		String[] numericAttributes = filterNumericAttributes(table, attributes);
		//make a new parameter object, so far identical to the old one.
		
		BasicObjectClassDefinition newOCD;
		try {
			newOCD = new BasicObjectClassDefinition(ocd.getID(), ocd.getName(), ocd.getDescription(), ocd.getIcon(16));
		} catch (IOException e) {
			newOCD = new BasicObjectClassDefinition(ocd.getID(), ocd.getName(), ocd.getDescription(), null);
		}
		
		//make the new parameter object the same as the old one...
		//except fill the column name attribute with values for the numeric attributes in the table
		
		AttributeDefinition[] paramAttributes = ocd.getAttributeDefinitions(ObjectClassDefinition.ALL);
		for(int ii = 0; ii < paramAttributes.length; ii++) {
			AttributeDefinition paramAttribute = paramAttributes[ii];
			if(paramAttribute.getID().equals("numericAttribute")) {
				newOCD.addAttributeDefinition(ObjectClassDefinition.REQUIRED,
						new BasicAttributeDefinition(paramAttribute.getID(), paramAttribute.getName(), paramAttribute.getDescription(), paramAttribute.getType()
								, numericAttributes , numericAttributes));
			}  else {
				newOCD.addAttributeDefinition(ObjectClassDefinition.REQUIRED, paramAttributes[ii]);
			}
		}
		
		//return our new parameters
		return newOCD;
	}
	
	private static String[] filterNumericAttributes(Table nodeTable, String[] nodeAttributes) {
		List numericAttributes = new ArrayList();
		for (int ii = 0; ii < nodeAttributes.length; ii++) {
			if (nodeTable.canGet(nodeAttributes[ii], Number.class)) {
				numericAttributes.add(nodeAttributes[ii]);
			}
		}
		return (String[]) numericAttributes.toArray(new String[numericAttributes.size()]);
		}
}
