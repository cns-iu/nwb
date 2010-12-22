package edu.iu.sci2.utilities.nsf;

import java.util.ArrayList;
import java.util.List;

public class NSF_CSV_FieldNames {
	
	@SuppressWarnings("serial")
	public static class CSV {
		public static final int DEFAULT_TOTAL_NSF_FIELDS = 26;
		
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

		public static List<String> getNsfCsvFields() {
			return NSF_CSV_FIELDS;
		}
	}
}
