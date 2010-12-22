package edu.iu.sci2.database.nsf.load.utilities;

import org.cishell.utilities.StringUtilities;

import edu.iu.cns.database.load.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.sci2.database.nsf.load.model.entity.Person;

public class PersonParser {
	public static Person parsePerson(
			DatabaseTableKeyGenerator personKeyGenerator, String originalToken) {
		
		
		
		String[] nameTokens;
		String lastName, firstName, formattedFullName;
		String middleInitial = "";
		String originalInputName = originalToken;
		
		// TODO: Considering adding additional/middle name fields.
		/*
		 * For Names which follow,
		 * 		"LastName, FirstName"
		 * 		"LastName, FirstInitial. FirstName"		(TODO: The FirstName may actually be MiddleName)
		 * 		"LastName, FirstName (AlternativeName)"
		 * Principal Investigators follow these syntax.
		 * */
		if (originalToken.contains(",")) {
			//TODO: Right now there is no further division of the first name i.e.
			//Everything after the first "," comes in FirstName. Should change this 
			//in the future.
			nameTokens = originalToken.split(",");
			
			if (nameTokens.length == 2) { 
				
				lastName = StringUtilities.simpleClean(
								StringUtilities.toSentenceCase(
										nameTokens[0]));
				firstName = StringUtilities.simpleClean(
								StringUtilities.toSentenceCase(
										nameTokens[1]));
			} else {
				originalInputName = "";
				for (String nameToken : nameTokens) {
					originalInputName += nameToken + " ";
				}
				lastName = "";
				firstName = "";
			}
			
		} else {
			
			/*
			 * For Names which follow,
			 * 		"FirstName LastName"
			 * 		"FirstName MiddleInitial. LastName"
			 * 		"FirstName MiddleInitial LastName"
			 * Program Manager, CO-PI names follow these syntax.
			 * */
			nameTokens = originalToken.split("\\s+");
			
			firstName = StringUtilities.simpleClean(
					StringUtilities.toSentenceCase(
							nameTokens[0]));
							
			if (nameTokens.length == 2) {
				lastName = StringUtilities.simpleClean(
								StringUtilities.toSentenceCase(
										nameTokens[1]));

			} else if (nameTokens.length == 3) {
				lastName = StringUtilities.simpleClean(
								StringUtilities.toSentenceCase(
										nameTokens[2]));
				
				String middleInitialCandidate = StringUtilities.simpleClean(
													StringUtilities.toSentenceCase(
															nameTokens[1]));
				
				/*
				 * Since we want to remove trailing "." character from MI.
				 * */
				middleInitial = middleInitialCandidate.substring(0, 1);
				
			} else {
				originalInputName = "";
				for (String nameToken : nameTokens) {
					originalInputName += nameToken + " ";
				}
				lastName = "";
				firstName = "";
			}
		}
		
		if (StringUtilities.allAreEmptyOrWhitespace(lastName, firstName, middleInitial)) {
			formattedFullName = "";
		} else {
			formattedFullName = lastName + ", " + firstName + " " + middleInitial;
		}
		
		return new Person(
				personKeyGenerator,
				lastName,
				firstName,
				middleInitial,
				formattedFullName,
				originalInputName);
	}
}