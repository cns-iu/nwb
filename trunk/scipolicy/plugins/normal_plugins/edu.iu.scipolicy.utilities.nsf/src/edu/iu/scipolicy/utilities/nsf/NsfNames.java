package edu.iu.scipolicy.utilities.nsf;

import java.util.ArrayList;
import java.util.List;

public class NsfNames {
	
	
	public static class DB {
		public static final String AWARD_TABLE = "AWARD";
		
		public static final String AWARD_NUMBER = "AWARD_NUMBER";
		public static final String AWARD_TITLE = "TITLE";
		public static final String AWARD_START_DATE = "START_DATE";
		public static final String AWARD_EXPIRATION_DATE = "EXPIRATION_DATE";
		public static final String AWARDED_AMOUNT_TO_DATE = "AWARDED_AMOUNT_TO_DATE";
	}
	
	public static class CSV {
		public static final String AWARD_NUMBER = "Award Number";
		public static final String AWARD_TITLE = "Title";
		public static final String AWARD_START_DATE = "Start Date";
		public static final String AWARD_EXPIRATION_DATE = "Expiration Date";
		public static final String AWARD_LAST_AMENDMENT_DATE = "Last Amendment Date";
		public static final String AWARDED_AMOUNT_TO_DATE = "Awarded Amount to Date";
		public static final String AWARD_INSTRUMENT = "Award Instrument";
		public static final String NSF_DIRECTORATE = "NSF Directorate";
		public static final String NSF_ORGANIZATION = "NSF Organization";
		public static final String FIELD_OF_APPLICATIONS = "Field Of Application(s)";
		public static final String ABSTRACT = "Abstract";
		
		public static final String CO_PI_NAMES = "Co-PI Name(s)";
		
		public static final String PRINCIPAL_INVESTIGATOR_NAME = "Principal Investigator";
		public static final String PRINCIPAL_INVESTIGATOR_STATE = "State";
		public static final String PRINCIPAL_INVESTIGATOR_EMAIL = "PI Email Address";
		
		public static final String PROGRAM_NAMES = "Program(s)";
		public static final String PROGRAM_ELEMENT_CODES = "Program Element Code(s)";
		public static final String PROGRAM_REFERENCE_CODES = "Program Reference Code(s)";
		public static final String PROGRAM_MANAGER = "Program Manager";
		
		
		
		public static final String ORGANIZATION_NAME = "Organization";
		public static final String ORGANIZATION_PHONE = "Organization Phone";
		public static final String ORGANIZATION_STREET_ADDRESS = "Organization Street Address";
		public static final String ORGANIZATION_STATE = "Organization State";
		public static final String ORGANIZATION_ZIP = "Organization Zip";
		public static final String ORGANIZATION_CITY = "Organization City";
		
		public static final List<String> NSF_CSV_FIELDS = new ArrayList<String>() {{
			add(AWARD_NUMBER);
			add(AWARD_TITLE);
			add(AWARD_START_DATE);
			add(AWARD_EXPIRATION_DATE);
			add(AWARD_LAST_AMENDMENT_DATE);
			add(AWARDED_AMOUNT_TO_DATE);
			add(AWARD_INSTRUMENT);
			add(NSF_DIRECTORATE);
			add(NSF_ORGANIZATION);
			add(FIELD_OF_APPLICATIONS);
			add(ABSTRACT);
			
			add(CO_PI_NAMES);
			
			add(PRINCIPAL_INVESTIGATOR_NAME);
			add(PRINCIPAL_INVESTIGATOR_STATE);
			add(PRINCIPAL_INVESTIGATOR_EMAIL);
			
			add(PROGRAM_NAMES);
			add(PROGRAM_ELEMENT_CODES);
			add(PROGRAM_REFERENCE_CODES);
			add(PROGRAM_MANAGER);
			
			add(ORGANIZATION_NAME);
			add(ORGANIZATION_PHONE);
			add(ORGANIZATION_STREET_ADDRESS);
			add(ORGANIZATION_STATE);
			add(ORGANIZATION_ZIP);
			add(ORGANIZATION_CITY);
		}};
		
	}
	
	//Reference for when we implement more stuff
	
	// Award table/field names.
//	public static final String AWARD_NUMBER_FIELD = "AWARD_NUMBER";
//	public static final String AWARD_TITLE_FIELD = "TITLE";
//	public static final String AWARD_NSF_ORGANNIZATION_FIELD = "NSF Organization";
//	public static final String AWARD_START_DATE = "START_DATE";
//	public static final String AWARD_LAST_AMENDMENT_DATE_FIELD = "Last Amendment Date";
//	public static final String AWARD_INSTRUMENT_FIELD = "Award Instrument";
//	public static final String AWARD_PROGRAM_MANAGER_FIELD = "Program Manager";
//	public static final String AWARD_EXPIRATION_DATE_FIELD = "EXPIRATION_DATE";
//	public static final String AWARDED_AMOUNT_TO_DATE_FIELD = "AWARDED_AMOUNT_TO_DATE";
//	public static final String AWARD_NSF_DIRECTORATE_FIELD = "NSF Directorate";
//	public static final String AWARD_ABSTRACT_FIELD = "Abstract";
//	
//	// Primary Investigator table/field names.
//	public static final String PI_TABLE = "PI";
//	public static final String PI_PERSONAL_NAMES_FIELD = "Personal Names";
//	public static final String PI_FAMILY_NAMES_FIELD = "Family Names";
//	public static final String PI_SUFFIXES_FIELD = "Suffixes";
//	public static final String PI_FULL_NAME_FIELD = "FullName";
//	
//	// Many to many PI to Award (?).
//	public static final String PI_TO_AWARD_TABLE = "PI_To_Award";
//	// Enumerated "main PI" or "co-PI".
//	public static final String PI_TO_AWARD_INVESTIGATOR_POSITION_FIELD = "InvestigatorPosition";
//	// Russell: state? is this ever different from organization state?
//	public static final String PI_TO_AWARD_STATE_FIELD = "State";
//	// Russell: move to PI?
//	public static final String PI_TO_AWARD_EMAIL_FIELD = "Email";
//	
//	// Organization table/field names.
//	public static final String ORGANIZATION_TABLE = "Organization";
//	public static final String ORGANIZATION_NAME_FIELD = "Name";
//	public static final String ORGANIZATION_STREET_ADDRESS_FIELD = "Street Address";
//	public static final String ORGANIZATION_CITY_FIELD = "City";
//	public static final String ORGANIZATION_STATE_FIELD = "State";
//	public static final String ORGANIZATION_ZIP_FIELD = "Zip";
//	public static final String ORGANIZATION_PHONE_FIELD = "Phone";
//
//	// Program table/field names.
//	public static final String PROGRAM_TABLE = "Program";
//	public static final String PROGRAM_NAME_FIELD = "Name";
//	public static final String PROGRAM_ELEMENT_CODE_FIELD = "Element Code";
//	
//	// Program Reference Code table/field names.
//	public static final String PROGRAM_REFERENCE_CODE_TABLE = "Program Reference Code";
//	public static final String PROGRAM_REFERENCE_CODE_NAME_FIELD = "Name";
//	
//	// Field of Application table/field names.
//	public static final String FIELD_OF_APPLICATION_TABLE = "Field Of Application";
//	public static final String FIELD_OF_APPLICATION_NAME_FIELD = "Name";
}
