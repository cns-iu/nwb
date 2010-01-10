package edu.iu.scipolicy.loader.nsf.db.model.entity;

import java.util.Dictionary;
import java.util.Hashtable;

import org.cishell.utilities.StringUtilities;

import edu.iu.cns.database.loader.framework.DerbyFieldType;
import edu.iu.cns.database.loader.framework.Entity;
import edu.iu.cns.database.loader.framework.Schema;
import edu.iu.cns.database.loader.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.scipolicy.loader.nsf.db.NSFDatabase;

public class FieldOfApplication extends Entity<FieldOfApplication> implements Comparable<FieldOfApplication>{

	public static final Schema<FieldOfApplication> SCHEMA = new Schema<FieldOfApplication>(
			true,
			NSFDatabase.ORIGINAL_INPUT_FIELD, DerbyFieldType.TEXT,
			NSFDatabase.EXTRACTED_NUMERIC_FIELD, DerbyFieldType.TEXT,
			NSFDatabase.EXTRACTED_TEXT_FIELD, DerbyFieldType.TEXT
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
		attributes.put(NSFDatabase.ORIGINAL_INPUT_FIELD, originalInputField);
		attributes.put(NSFDatabase.EXTRACTED_NUMERIC_FIELD, extractedNumericField);
		attributes.put(NSFDatabase.EXTRACTED_TEXT_FIELD, extractedTextField);

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
		if (!StringUtilities.isEmptyOrWhiteSpace(this.extractedNumericField)
			&& !StringUtilities.isEmptyOrWhiteSpace(this.extractedTextField)
			&& !StringUtilities.isEmptyOrWhiteSpace(otherItem.getExtractedNumericField())
			&& !StringUtilities.isEmptyOrWhiteSpace(otherItem.getExtractedTextField())
			) {
			return (
					StringUtilities.validAndEquivalentIgnoreCase(
							this.extractedNumericField, 
							otherItem.getExtractedNumericField())
					&& StringUtilities.validAndEquivalentIgnoreCase(
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