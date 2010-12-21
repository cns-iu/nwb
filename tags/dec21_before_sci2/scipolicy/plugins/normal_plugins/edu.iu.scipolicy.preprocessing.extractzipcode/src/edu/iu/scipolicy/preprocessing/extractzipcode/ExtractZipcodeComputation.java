package edu.iu.scipolicy.preprocessing.extractzipcode;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.cishell.utilities.TableUtilities;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import prefuse.data.util.TableIterator;

/**
 * 
 * After getting the input file following algorithm is implemented,
 * 1. Get the address for each row & begin parsing for zip codes in following manner,
 * 		a.  Save all groups of digits along with their start position, end position &
 * 			length of the group of digits.
 * 		b.	Since the zip code, presumably, will be in the later portion of the address 
 * 			string, traverse the collected zip code candidates in the reverse fashion.
 * 		c.	If there is a 5 digit group then,
 * 			(i) 	Consider it as the primary zip code
 * 			(ii) 	The extension of the ZIP code follows the primary zip code, so check
 * 					if the previous group has length 4 and if so, then check if its
 * 					distance from primary zip is less than or equal to 2. If yes, than
 * 					consider this as the extension of the zip code.
 * 			(iii)	If there is no 4 digit group satisfying the above conditions then return 
 * 					null as the extension value.
 * 		d. If there is no 5 digit group then return null for the primary zip code value. In
 *		   this case, display a warning to the user that no ZIP code was found for this 
 *		   particular address string.
 *	 			  
 * @author cdtank
 *
 */
public class ExtractZipcodeComputation {
	
	private static final int PRIMARY_TO_EXTENSION_DISTANCE_TOLERANCE_VALUE = 2;
	private static final int EXTENSION_ZIPCODE_LENGTH = 4;
	private static final int PRIMARY_ZIPCODE_LENGTH = 5;
	
	
	private int totalAddressesConsidered = 0, totalZipcodesExtracted = 0;

	private LogService logger;

	private static final String[] ZIPCODE_COLUMN_NAME_SUGGESTIONS = {"ZIP code", "ZIP"};
	
	private boolean truncate;
	private String addressColumnName;
	private Table originalTable, outputTable;

	private String outputTableZipcodeColumnName;

	public ExtractZipcodeComputation(boolean truncate, String addressColumnName, Table table,
			LogService logger) {
		
		this.truncate = truncate;
		this.addressColumnName = addressColumnName;
		this.originalTable = table;
		this.logger = logger;
		
		processTable();
	
	}

	private void processTable() {
		outputTable = originalTable.getSchema().instantiate();
		outputTableZipcodeColumnName = TableUtilities.formNonConflictingNewColumnName(
			originalTable.getSchema(), ZIPCODE_COLUMN_NAME_SUGGESTIONS);
		
		outputTable.addColumn(outputTableZipcodeColumnName, String.class);
		
		logger.log(LogService.LOG_INFO, "ZIP code added to \""
										+ outputTableZipcodeColumnName + "\" column.");
		
		TableIterator addressColumnIterator = originalTable.iterator();
		
		/*
		 * Use regular expression to find out all the groups which containing numbers.
		 * */
		Pattern zipcodePattern = Pattern.compile("(\\d)+");
		Matcher zipcodeMatcher;
		List<ZipcodeCandidate> zipcodeCandidatesInfo;
		Zipcode zipcode = null;
		String outputZipcode = "";
		
		while (addressColumnIterator.hasNext()) {			
			int currentRowNumber = Integer.parseInt(addressColumnIterator.next().toString());
			

			String currentAddress = originalTable
										.getString(currentRowNumber, addressColumnName);
			
			/*
			 * In some test cases Empty Address Inputs we are getting "null" objects as opposed to
			 * "" i.e. empty Strings. To handle this case we are testing against null objects.
			 * */
			if (currentAddress != null) {

				zipcodeMatcher = zipcodePattern.matcher(currentAddress);

				zipcodeCandidatesInfo = new ArrayList<ZipcodeCandidate>();
				
				/*
				 * Iterate over the address string and collect all the groups of numbers.
				 * */
				while (zipcodeMatcher.find()) {
					zipcodeCandidatesInfo
						.add(new ZipcodeCandidate(currentAddress, zipcodeMatcher.group(), 
							 zipcodeMatcher.start(), 
							 zipcodeMatcher.end(), 
							 zipcodeMatcher.group().length()));
				}
				
				/*
				 * Get the probable ZipcodeCandidate, if any, from the list of zipcode candidates. 
				 * */
				zipcode = selectZipcodeFromExtractedCandidates(zipcodeCandidatesInfo);
			} else {
				zipcode = new Zipcode(null, null);
			}
				
			
			/*
			 * Check if the primary zipcode value is null, if yes then it means no zipcode was 
			 * found in the address string. Display accordingly.
			 * 
			 * */
			if (zipcode.getPrimaryZipcode() == null) {
				outputZipcode = "";
				String message;
				if (currentAddress == null) {
					message = addressColumnName + " was empty in row " + currentRowNumber + ".";
				} else {
					message = "No ZIP code found in '" + currentAddress 
						+ "' in row " + currentRowNumber + ".";
				}
				message += " Leaving the " + outputTableZipcodeColumnName 
					+ " column empty for this row.";
				
				logger.log(LogService.LOG_WARNING, message);
			} else {
				/*
				 * At least primary ZIP code is present check if the user wanted truncated ZIP code
				 * i.e. 5 digit instead of 9 digit long form.
				 * */
				if (truncate) {
					outputZipcode = zipcode.getPrimaryZipcode();
				} else {
					/*
					 * Since the user wants long form check if there is any extended ZIP code
					 * in the input data & then proceed accordingly.
					 * */
					if (zipcode.getExtensionZipcode() != null) {
						outputZipcode = zipcode.getPrimaryZipcode() 
										+ "-"
										+ zipcode.getExtensionZipcode();
					} else {
							outputZipcode = zipcode.getPrimaryZipcode(); 
					}
				}
				totalZipcodesExtracted++;
			}
			
			/*
			 * Add the new row to the new table by copying the original row & then adding 
			 * 2 new columns to it.
			 * */
			outputTable.addRow();
			TableUtilities
				.copyTableRow(currentRowNumber, currentRowNumber, outputTable, originalTable);
			outputTable.setString(currentRowNumber, outputTableZipcodeColumnName, outputZipcode);
			
			totalAddressesConsidered++;
		}
	}

	/**
	 * @param zipcodeCandidatesInfo
	 * @return 
	 */
	private Zipcode selectZipcodeFromExtractedCandidates(
			List<ZipcodeCandidate> zipcodeCandidatesInfo) {
		int zipcodeCandidatesIndexUpperBound = zipcodeCandidatesInfo.size() - 1;
		
		Zipcode zipcode = new Zipcode(null, null);
		
		/*
		 * Iterate over the zip code candidates in reverse manner, i.e. process the groups
		 * found last first, since ZIP codes are likely to be found in the end.
		 * */
		for (int candidateIndex = zipcodeCandidatesIndexUpperBound; 
				candidateIndex >= 0; 
				candidateIndex--) {
			ZipcodeCandidate currentZipcodeCandidate = zipcodeCandidatesInfo.get(candidateIndex);
			if (currentZipcodeCandidate.getLength() == PRIMARY_ZIPCODE_LENGTH) {
				
				zipcode.setPrimaryZipcode(
						zipcodeCandidatesInfo.get(candidateIndex).getZipCodeCandidate());

				/*
				 * 1. Condition to make sure that we are not looking for a group if the primary
				 * 		ZIP code was the last group i.e. Avoid ArrayOutOfIndex Exception.
				 * 2. Condition to make sure that only candidates whose length is 4 are considered.
				 * 3. Condition to make sure that the distance between Primary & Extension ZIP 
				 * 	  codes is not more than the tolerance level.
				 * */
				if (candidateIndex + 1 <= zipcodeCandidatesIndexUpperBound 
						&& zipcodeCandidatesInfo.get(candidateIndex + 1).getLength() 
							== EXTENSION_ZIPCODE_LENGTH
							&& zipcodeCandidatesInfo.get(candidateIndex + 1).getStartPosition() 
							   - zipcodeCandidatesInfo.get(candidateIndex + 1).getStartPosition() 
							   	<= PRIMARY_TO_EXTENSION_DISTANCE_TOLERANCE_VALUE) {
					
					zipcode.setExtensionZipcode(
							zipcodeCandidatesInfo.get(candidateIndex + 1).getZipCodeCandidate());
				}
				return zipcode;
			} 
		}
		return zipcode;
	}
	
	/**
	 * @return the outputTable
	 */
	public Table getOutputTable() {
		return outputTable;
	}

	/**
	 * @return the totalAddressesConsidered
	 */
	public int getTotalAddressesConsidered() {
		return totalAddressesConsidered;
	}

	/**
	 * @return the totalZipcodesExtracted
	 */
	public int getTotalZipcodesExtracted() {
		return totalZipcodesExtracted;
	}
	
}
