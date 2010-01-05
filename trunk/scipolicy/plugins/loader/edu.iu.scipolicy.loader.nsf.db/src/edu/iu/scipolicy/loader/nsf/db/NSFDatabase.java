package edu.iu.scipolicy.loader.nsf.db;

public class NSFDatabase {
	
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
	public static final String INVESTIGATOR_ORGANIZATIONS_HUMAN_READABLE_NAME = "Investigator Organizations";
	public static final String INVESTIGATOR_ORGANIZATIONS_TABLE_NAME = "INVESTIGATOR_ORGANIZATIONS";

	public static final String INVESTIGATOR_HUMAN_READABLE_NAME = "Investigators";
	public static final String INVESTIGATOR_TABLE_NAME = "INVESTIGATOR";
	
	public static final String AWARD_FIELD_OF_APPLICATION_HUMAN_READABLE_NAME = "Investigators";
	public static final String AWARD_FIELD_OF_APPLICATION_TABLE_NAME = "INVESTIGATOR";

	public static final String PROGRAM_MANAGER_HUMAN_READABLE_NAME = "Program Managers";
	public static final String PROGRAM_MANAGER_TABLE_NAME = "PROGRAM_MANAGER";
	
	public static final String PROGRAM_NAME_AND_ELEMENT_CODES_HUMAN_READABLE_NAME = "Program Name And Element Codes";
	public static final String PROGRAM_NAME_AND_ELEMENT_CODES_TABLE_NAME = "PROGRAM_NAME_AND_ELEMENT_CODES";

	public static final String PROGRAM_REFERENCE_CODES_HUMAN_READABLE_NAME = "Program Reference Codes";
	public static final String PROGRAM_REFERENCE_CODES_TABLE_NAME = "PROGRAM_REFERENCE_CODES";
	
	public static final String AWARD_OCCURRENCES_HUMAN_READABLE_NAME = "Award Occurrences";
	public static final String AWARD_OCCURRENCES_TABLE_NAME = "AWARD_OCCURRENCES";
	
	// NSF File Entity Information
	public static final String FILE_NAME = "file_name";
	public static final String FILE_TYPE = "file_type";
	public static final String FILE_MD5_CHECKSUM = "file_md5_checksum";

	// Organization Entity Information
	public static final String ORGANIZATION_NAME = "organization_name";
	public static final String ORGANIZATION_PHONE = "organization_phone";
	public static final String ORGANIZATION_STREET_ADDRESS = "organization_street_address";
	public static final String ORGANIZATION_CITY = "organization_city";	
	public static final String ORGANIZATION_STATE = "organization_state";
	public static final String ORGANIZATION_ZIP = "organization_zip";


	// FieldOfApplication Entity Information
	public static final String ORIGINAL_INPUT_FIELD = "original_input_field";
	public static final String EXTRACTED_NUMERIC_FIELD = "extracted_numeric_field";
	public static final String EXTRACTED_TEXT_FIELD = "extracted_text_field";
	
	// Program Entity Information
	public static final String PROGRAM_NAME = "program_name";
	public static final String FUNDING_CODE = "funding_code";

	// Person Entity Information
	public static final String LAST_NAME = "last_name";
	public static final String FIRST_NAME = "first_name";
	public static final String MIDDLE_INITIAL = "middle_initial";
	public static final String ORIGINAL_INPUT_NAME = "original_input_name";
	public static final String FORMATTED_FULL_NAME = "formatted_full_name";

	// Award Entity Information
	public static final String DIGITAL_OBJECT_IDENTIFIER = "digital_object_identifier";
	public static final String TITLE = "title";
	public static final String AWARD_NUMBER = "award_number";
	public static final String START_DATE = "start_date";
	public static final String EXPIRATION_DATE = "expiration_date";
	public static final String LAST_AMMENDMENT_DATE = "last_ammendment_date";
	public static final String AWARDED_AMOUNT_TO_DATE = "awarded_amount_to_date";
	public static final String AWARD_INSTRUMENT = "award_instrument";
	public static final String NSF_DIRECTORATE = "nsf_directorate";
	public static final String NSF_ORGANIZATION = "nsf_organization";
	public static final String ABSTRACT_TEXT = "abstract_text";
	
	// Investigator (Person - Award) Relationship Information
	public static final String EMAIL_ADDRESS = "email_address";
	public static final String STATE = "state";
	public static final String IS_MAIN_PI = "is_main_pi";

	// Commonly-Found Entity Relationship Table Information
	public static final String ORDER_LISTED_KEY = "order_listed";
}