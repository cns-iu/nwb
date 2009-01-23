package edu.iu.scipolicy.converter.nsf.db_to_prefuse;

public class NSFConstants {
	// Award table/field names.
	public static final String AWARD_TABLE = "Award";
	public static final String AWARD_NUMBER_FIELD = "AwardNumber";
	public static final String AWARD_TITLE_FIELD = "Title";
	public static final String AWARD_NSF_ORGANNIZATION_FIELD = "NSFOrganization";
	public static final String AWARD_START_DATE = "StartDate";
	public static final String AWARD_LAST_AMENDMENT_DATE_FIELD = "LastAmendmentDate";
	public static final String AWARD_INSTRUMENT_FIELD = "AwardInstrument";
	public static final String AWARD_PROGRAM_MANAGER_FIELD = "ProgramManager";
	public static final String AWARD_EXPIRATION_DATE_FIELD = "ExpirationDate";
	public static final String AWARDED_AMOUNT_TO_DATE_FIELD = "AwardedAmountToDate";
	public static final String AWARD_NSF_DIRECTORATE_FIELD = "NSFDirectorate";
	public static final String AWARD_ABSTRACT_FIELD = "Abstract";
	
	// Primary Investigator table/field names.
	public static final String PI_TABLE = "PI";
	public static final String PI_PERSONAL_NAMES_FIELD = "PersonalNames";
	public static final String PI_FAMILY_NAMES_FIELD = "FamilyNames";
	public static final String PI_SUFFIXES_FIELD = "Suffixes";
	public static final String PI_FULL_NAME_FIELD = "FullName";
	
	// Many to many PI to Award (?).
	public static final String PI_TO_AWARD_TABLE = "PI_To_Award";
	// Enumerated "main PI" or "co-PI".
	public static final String PI_TO_AWARD_INVESTIGATOR_POSITION_FIELD = "InvestigatorPosition";
	// Russell: state? is this ever different from organization state?
	public static final String PI_TO_AWARD_STATE_FIELD = "State";
	// Russell: move to PI?
	public static final String PI_TO_AWARD_EMAIL_FIELD = "Email";
	
	// Organization table/field names.
	public static final String ORGANIZATION_TABLE = "Organization";
	public static final String ORGANIZATION_NAME_FIELD = "Name";
	public static final String ORGANIZATION_STREET_ADDRESS_FIELD = "StreetAddress";
	public static final String ORGANIZATION_CITY_FIELD = "City";
	public static final String ORGANIZATION_STATE_FIELD = "State";
	public static final String ORGANIZATION_ZIP_FIELD = "Zip";
	public static final String ORGANIZATION_PHONE_FIELD = "Phone";

	// Program table/field names.
	public static final String PROGRAM_TABLE = "Program";
	public static final String PROGRAM_NAME_FIELD = "Name";
	public static final String PROGRAM_ELEMENT_CODE_FIELD = "ElementCode";
	
	// Program Reference Code table/field names.
	public static final String PROGRAM_REFERENCE_CODE_TABLE = "Program_Reference_Code";
	public static final String PROGRAM_REFERENCE_CODE_NAME_FIELD = "Name";
	
	// Field of Application table/field names.
	public static final String FIELD_OF_APPLICATION_TABLE = "Field_Of_Application";
	public static final String FIELD_OF_APPLICATION_NAME_FIELD = "Name";
}