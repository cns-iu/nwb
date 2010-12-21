package edu.iu.scipolicy.database.nsf.load.utilities;

import org.cishell.utilities.StringUtilities;

import edu.iu.cns.database.load.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.scipolicy.database.nsf.load.model.entity.FieldOfApplication;

public class FieldOfApplicationParser {
	public static FieldOfApplication parseFieldOfApplication(
			DatabaseTableKeyGenerator fieldOfApplicationGenerator, String originalToken) {
		
		
		
		String[] fieldOfApplicationTokens;
		String extractedNumericField, extractedTextField;
		String originalFOA = originalToken;
		
		fieldOfApplicationTokens = originalToken.split("\\s+");
		
		if (fieldOfApplicationTokens.length != 1) {
			extractedNumericField = StringUtilities.simpleClean(
											fieldOfApplicationTokens[0]);
			extractedTextField = "";
			for (int ii = 1; ii < fieldOfApplicationTokens.length; ii++) {
				extractedTextField += fieldOfApplicationTokens[ii] + " ";
			}
			
			extractedTextField = StringUtilities.simpleClean(extractedTextField);

		} else {
			extractedNumericField = "";
			extractedTextField = "";
		}
		
		return new FieldOfApplication(
				fieldOfApplicationGenerator,
				extractedNumericField,
				extractedTextField, 
				originalFOA);
	}
}