package edu.iu.scipolicy.utilities.nsf;

public class NSF_Database_FieldNames {
	
	public static final String NSF_DATABASE_MIME_TYPE = "db:nsf";
	
	// Entity Type Information
	public static final String NSF_FILE_HUMAN_READABLE_NAME = "NSF Files";
	public static final String NSF_FILE_TABLE_NAME = "NSF_FILE";

	public static final String ORGANIZATION_HUMAN_READABLE_NAME = "Organizations";
	public static final String ORGANIZATION_TABLE_NAME = "ORGANIZATION";

	public static final String PERSON_HUMAN_READABLE_NAME = "People";
	public static final String PERSON_TABLE_NAME = "PERSON";
	
	public static final String FIELD_OF_APPLICATION_HUMAN_READABLE_NAME = "Field of Applications";
	public static final String FIELD_OF_APPLICATION_TABLE_NAME = "FIELD_OF_APPLICATION";
	
	public static final String PROGRAM_HUMAN_READABLE_NAME = "Programs";
	public static final String PROGRAM_TABLE_NAME = "PROGRAM";
	
	public static final String AWARD_HUMAN_READABLE_NAME = "Awards";
	public static final String AWARD_TABLE_NAME = "AWARD";
	
	// Entity Relationship Type Information
	public static final String INVESTIGATOR_ORGANIZATIONS_HUMAN_READABLE_NAME 
									= "Investigator Organizations";
	
	public static final String INVESTIGATOR_ORGANIZATIONS_TABLE_NAME = "INVESTIGATOR_ORGANIZATIONS";

	public static final String INVESTIGATOR_HUMAN_READABLE_NAME = "Investigators";
	public static final String INVESTIGATOR_TABLE_NAME = "INVESTIGATORS";
	
	public static final String AWARD_FIELD_OF_APPLICATION_HUMAN_READABLE_NAME 
									= "Award Field Of Applications";
	
	public static final String AWARD_FIELD_OF_APPLICATION_TABLE_NAME 
									= "AWARD_FIELD_OF_APPLICATIONS";

	public static final String PROGRAM_MANAGER_HUMAN_READABLE_NAME = "Program Managers";
	public static final String PROGRAM_MANAGER_TABLE_NAME = "PROGRAM_MANAGERS";
	
	public static final String PROGRAM_NAME_AND_ELEMENT_CODES_HUMAN_READABLE_NAME 
									= "Program Name And Element Codes";
	
	public static final String PROGRAM_NAME_AND_ELEMENT_CODES_TABLE_NAME 
									= "PROGRAM_NAME_AND_ELEMENT_CODES";

	public static final String PROGRAM_REFERENCE_CODES_HUMAN_READABLE_NAME 
									= "Program Reference Codes";
	
	public static final String PROGRAM_REFERENCE_CODES_TABLE_NAME = "PROGRAM_REFERENCE_CODES";
	
	public static final String AWARD_OCCURRENCES_HUMAN_READABLE_NAME = "Award Occurrences";
	public static final String AWARD_OCCURRENCES_TABLE_NAME = "AWARD_OCCURRENCES";
	
	// NSF File Entity Information
	public static final String FILE_NAME = "FILE_NAME";
	public static final String FILE_TYPE = "FILE_TYPE";
	public static final String FILE_MD5_CHECKSUM = "FILE_MD5_CHECKSUM";

	// Organization Entity Information
	public static final String ORGANIZATION_NAME = "ORGANIZATION_NAME";
	public static final String ORGANIZATION_PHONE = "ORGANIZATION_PHONE";
	public static final String ORGANIZATION_STREET_ADDRESS = "ORGANIZATION_STREET_ADDRESS";
	public static final String ORGANIZATION_CITY = "ORGANIZATION_CITY";	
	public static final String ORGANIZATION_STATE = "ORGANIZATION_STATE";
	public static final String ORGANIZATION_ZIP = "ORGANIZATION_ZIP";


	// FieldOfApplication Entity Information
	public static final String ORIGINAL_INPUT_FIELD = "ORIGINAL_INPUT_FIELD";
	public static final String EXTRACTED_NUMERIC_FIELD = "EXTRACTED_NUMERIC_FIELD";
	public static final String EXTRACTED_TEXT_FIELD = "EXTRACTED_TEXT_FIELD";
	
	// Program Entity Information
	public static final String PROGRAM_NAME = "PROGRAM_NAME";
	public static final String FUNDING_CODE = "FUNDING_CODE";

	// Person Entity Information
	public static final String LAST_NAME = "LAST_NAME";
	public static final String FIRST_NAME = "FIRST_NAME";
	public static final String MIDDLE_INITIAL = "MIDDLE_INITIAL";
	public static final String ORIGINAL_INPUT_NAME = "ORIGINAL_INPUT_NAME";
	public static final String FORMATTED_FULL_NAME = "FORMATTED_FULL_NAME";

	// Award Entity Information
	public static final String DIGITAL_OBJECT_IDENTIFIER = "DIGITAL_OBJECT_IDENTIFIER";
	public static final String TITLE = "TITLE";
	public static final String AWARD_NUMBER = "AWARD_NUMBER";
	public static final String START_DATE = "START_DATE";
	public static final String RAW_START_DATE = "RAW_START_DATE";
	public static final String EXPIRATION_DATE = "EXPIRATION_DATE";
	public static final String RAW_EXPIRATION_DATE = "RAW_EXPIRATION_DATE";
	public static final String LAST_AMENDMENT_DATE = "LAST_AMENDMENT_DATE";
	public static final String RAW_LAST_AMENDMENT_DATE = "RAW_LAST_AMENDMENT_DATE";
	public static final String AWARDED_AMOUNT_TO_DATE = "AWARDED_AMOUNT_TO_DATE";
	public static final String RAW_AWARDED_AMOUNT_TO_DATE = "RAW_AWARDED_AMOUNT_TO_DATE";
	public static final String AWARD_INSTRUMENT = "AWARD_INSTRUMENT";
	public static final String NSF_DIRECTORATE = "NSF_DIRECTORATE";
	public static final String NSF_ORGANIZATION = "NSF_ORGANIZATION";
	public static final String ABSTRACT_TEXT = "ABSTRACT_TEXT";
	
	// AwardOccurence (Award - NSFFile) Relationship Information
	public static final String AWARD_OCCURRENCES_AWARD_FOREIGN_KEY = "AO_AWARD_FK";
	public static final String AWARD_OCCURRENCES_NSF_FILE_FOREIGN_KEY = "AO_NSF_FILE_FK";

	// AwardFieldOfApplication (FieldOfApplication - Award) Relationship Information
	public static final String FIELD_OF_APPLICATIONS_FIELD_OF_APPLICATION_FOREIGN_KEY 
									= "FOAS_FIELD_OF_APPLICATION_FK";
	public static final String FIELD_OF_APPLICATIONS_AWARD_FOREIGN_KEY = "FOAS_AWARD_FK";

	// Investigator (Person - Award) Relationship Information
	public static final String INVESTIGATOR_PERSON_FOREIGN_KEY = "I_PERSON_FK";
	public static final String INVESTIGATOR_AWARD_FOREIGN_KEY = "I_AWARD_FK";
	public static final String EMAIL_ADDRESS = "EMAIL_ADDRESS";
	public static final String STATE = "STATE";
	public static final String IS_MAIN_PI = "IS_MAIN_PI";
	
	// InvestigatorOrganization (Investigator - Organization) Relationship Information
	public static final String INVESTIGATOR_ORGANIZATIONS_INVESTIGATOR_FOREIGN_KEY 
									= "IOS_INVESTIGATOR_FK";
	public static final String INVESTIGATOR_ORGANIZATIONS_ORGANIZATION_FOREIGN_KEY 
									= "IOS_ORGANIZATION_FK";
	
	// ProgramManager (Person - Award) Relationship Information
	public static final String PROGRAM_MANAGER_PERSON_FOREIGN_KEY = "PM_PERSON_FK";
	public static final String PROGRAM_MANAGER_AWARD_FOREIGN_KEY = "PM_AWARD_FK";
	
	// ProgramNameAndElementCode (Program - Award) Relationship Information
	public static final String PROGRAM_NAME_AND_ELEMENT_CODES_PROGRAM_FOREIGN_KEY 
									= "PNEC_PROGRAM_FK";
	public static final String PROGRAM_NAME_AND_ELEMENT_CODES_AWARD_FOREIGN_KEY = "PNEC_AWARD_FK";	
	
	// ProgramReferenceCode (Program - Award) Relationship Information
	public static final String PROGRAM_REFERENCE_CODES_PROGRAM_FOREIGN_KEY = "PRC_PROGRAM_FK";
	public static final String PROGRAM_REFERENCE_CODES_AWARD_FOREIGN_KEY = "PRC_AWARD_FK";	
	
	// Commonly-Found Entity Relationship Table Information
	public static final String ORDER_LISTED_KEY = "ORDER_LISTED";
}