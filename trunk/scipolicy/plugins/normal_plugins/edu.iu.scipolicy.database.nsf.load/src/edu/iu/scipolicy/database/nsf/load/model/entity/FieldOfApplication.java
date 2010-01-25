package edu.iu.scipolicy.database.nsf.load.model.entity;

import java.util.Dictionary;
import java.util.Hashtable;

import org.cishell.utilities.StringUtilities;

import edu.iu.cns.database.load.framework.DerbyFieldType;
import edu.iu.cns.database.load.framework.Entity;
import edu.iu.cns.database.load.framework.Schema;
import edu.iu.cns.database.load.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.scipolicy.utilities.nsf.NSF_Database_FieldNames;

public class FieldOfApplication extends Entity<FieldOfApplication> implements Comparable<FieldOfApplication>{

	public static final Schema<FieldOfApplication> SCHEMA = new Schema<FieldOfApplication>(
			true,
			NSF_Database_FieldNames.ORIGINAL_INPUT_FIELD, DerbyFieldType.TEXT,
			NSF_Database_FieldNames.EXTRACTED_NUMERIC_FIELD, DerbyFieldType.TEXT,
			NSF_Database_FieldNames.EXTRACTED_TEXT_FIELD, DerbyFieldType.TEXT
			);
	
	private String originalInputField;
	private String extractedNumericField;
	private String extractedTextField;

	public FieldOfApplication(DatabaseTableKeyGenerator keyGenerator,
			  				  String originalInputField) {
		this(keyGenerator, "", "", originalInputField);
	}
	
	public FieldOfApplication(DatabaseTableKeyGenerator keyGenerator,
							  String extractedNumericField,
							  String extractedTextField, 
							  String originalInputField) {
		super(keyGenerator, createAttributes(originalInputField, extractedNumericField, extractedTextField));
		this.extractedTextField = extractedTextField;
		this.extractedNumericField = extractedNumericField;
		this.originalInputField = originalInputField;
	}


	private static Dictionary<String, Comparable<?>> createAttributes(String originalInputField,
													   String extractedNumericField,
													   String extractedTextField) {
		Dictionary<String, Comparable<?>> attributes = new Hashtable<String, Comparable<?>>();
		attributes.put(NSF_Database_FieldNames.ORIGINAL_INPUT_FIELD, originalInputField);
		attributes.put(NSF_Database_FieldNames.EXTRACTED_NUMERIC_FIELD, extractedNumericField);
		attributes.put(NSF_Database_FieldNames.EXTRACTED_TEXT_FIELD, extractedTextField);

		return attributes;
	}

	public String getOriginalInputField() {
		return originalInputField;
	}

	public String getExtractedNumericField() {
		return extractedNumericField;
	}

	public String getExtractedTextField() {
		return extractedTextField;
	}

	@Override
	public void merge(FieldOfApplication otherItem) {
		this.extractedNumericField = StringUtilities.simpleMerge(this.extractedNumericField, 
																 otherItem.getExtractedNumericField());
		this.extractedTextField = StringUtilities.simpleMerge(this.extractedTextField, 
															  otherItem.getExtractedTextField());
		this.originalInputField = StringUtilities.simpleMerge(this.originalInputField, 
															  otherItem.getOriginalInputField());
	}

	@Override
	public boolean shouldMerge(FieldOfApplication otherItem) {
		
		/*
		 * We need to make sure that none of the attributes are empty
		 * to make a valid comparison.
		 * */
		if (!StringUtilities.isNull_Empty_OrWhitespace(this.extractedNumericField)
			&& !StringUtilities.isNull_Empty_OrWhitespace(this.extractedTextField)
			&& !StringUtilities.isNull_Empty_OrWhitespace(otherItem.getExtractedNumericField())
			&& !StringUtilities.isNull_Empty_OrWhitespace(otherItem.getExtractedTextField())
			) {
			return (
					StringUtilities.areValidAndEqualIgnoreCase(
							this.extractedNumericField, 
							otherItem.getExtractedNumericField())
					&& StringUtilities.areValidAndEqualIgnoreCase(
							this.extractedTextField,
							otherItem.getExtractedTextField())
					);	
		} else {
			return false;
		}
		
	}

	public int compareTo(FieldOfApplication o) {
		// TODO Auto-generated method stub
		return 0;
	}
}