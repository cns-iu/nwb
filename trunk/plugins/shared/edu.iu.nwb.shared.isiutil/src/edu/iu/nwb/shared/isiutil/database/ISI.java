package edu.iu.nwb.shared.isiutil.database;

public class ISI {
	// TODO: Refactor this and ISISchemas to be the same thing.

	public static final String ISI_DATABASE_MIME_TYPE = "db:isi";

	// Entity Type Information

	public static final String ISI_FILE_DISPLAY_NAME = "Files";
	public static final String ISI_FILE_TABLE_NAME = "FILES";

	public static final String PUBLISHER_DISPLAY_NAME = "Publishers";
	public static final String PUBLISHER_TABLE_NAME = "PUBLISHERS";

	public static final String SOURCE_DISPLAY_NAME = "Sources";
	public static final String SOURCE_TABLE_NAME = "SOURCES";

	public static final String REFERENCE_DISPLAY_NAME = "Citations";
	public static final String REFERENCE_TABLE_NAME = "CITATIONS";

	public static final String ADDRESS_DISPLAY_NAME = "Addresses";
	public static final String ADDRESS_TABLE_NAME = "ADDRESSES";

	public static final String KEYWORD_DISPLAY_NAME = "Keywords";
	public static final String KEYWORD_TABLE_NAME = "KEYWORDS";

	public static final String PERSON_DISPLAY_NAME = "People";
	public static final String PERSON_TABLE_NAME = "PEOPLE";

	public static final String PATENT_DISPLAY_NAME = "Patents";
	public static final String PATENT_TABLE_NAME = "PATENTS";

	public static final String DOCUMENT_DISPLAY_NAME = "Documents";
	public static final String DOCUMENT_TABLE_NAME = "DOCUMENTS";

	public static final String[] ENTITY_TABLE_DISPLAY_NAMES = new String[] {
		ISI_FILE_DISPLAY_NAME,
		PUBLISHER_DISPLAY_NAME,
		SOURCE_DISPLAY_NAME,
		REFERENCE_DISPLAY_NAME,
		ADDRESS_DISPLAY_NAME,
		KEYWORD_DISPLAY_NAME,
		PERSON_DISPLAY_NAME,
		PATENT_DISPLAY_NAME,
		DOCUMENT_DISPLAY_NAME,
	};

	public static final String[] ENTITY_TABLE_DATABASE_NAMES = new String[] {
		ISI_FILE_TABLE_NAME,
		PUBLISHER_TABLE_NAME,
		SOURCE_TABLE_NAME,
		REFERENCE_TABLE_NAME,
		ADDRESS_TABLE_NAME,
		KEYWORD_TABLE_NAME,
		PERSON_TABLE_NAME,
		PATENT_TABLE_NAME,
		DOCUMENT_TABLE_NAME,
	};

	// ISI File Entity Information
	public static final String FILE_FORMAT_VERSION_NUMBER = "FORMAT_VERSION_NUMBER";
	public static final String FILE_NAME = "NAME";
	public static final String FILE_TYPE = "FILE_TYPE";

	public static final String[] ISI_FILE_FIELD_NAMES =
		new String[] { FILE_FORMAT_VERSION_NUMBER, FILE_NAME, FILE_TYPE, };

	// Publisher Entity Information
	public static final String PUBLISHER_CITY = "CITY";
	public static final String PUBLISHER_NAME = "NAME";
	public static final String PUBLISHER_SOURCE = "SOURCE_ID";
	public static final String PUBLISHER_WEB_ADDRESS = "WEB_ADDRESS";

	public static final String[] PUBLISHER_FIELD_NAMES =
		new String[] { PUBLISHER_CITY, PUBLISHER_NAME, PUBLISHER_SOURCE, PUBLISHER_WEB_ADDRESS, };

	// Source Entity Information
	public static final String BOOK_SERIES_TITLE = "BOOK_SERIES_TITLE";
	public static final String BOOK_SERIES_SUBTITLE = "BOOK_SERIES_SUBTITLE";
	public static final String CONFERENCE_HOST = "CONFERENCE_HOST";
	public static final String CONFERENCE_LOCATION = "CONFERENCE_LOCATION";
	public static final String CONFERENCE_SPONSORS = "CONFERENCE_SPONSORS";
	public static final String CONFERENCE_TITLE = "CONFERENCE_TITLE";
	public static final String CONFERENCE_DATES = "CONFERENCE_DATES";
	public static final String FULL_TITLE = "FULL_TITLE";
	public static final String ISO_TITLE_ABBREVIATION = "ISO_TITLE_ABBREVIATION";
	public static final String ISSN = "ISSN";
	public static final String PUBLICATION_TYPE = "PUBLICATION_TYPE";
	public static final String TWENTY_NINE_CHARACTER_SOURCE_TITLE_ABBREVIATION =
		"TWENTY_NINE_CHARACTER_SOURCE_TITLE_ABBREVIATION";

	public static final String[] SOURCE_FIELD_NAMES = new String[] {
		BOOK_SERIES_TITLE,
		BOOK_SERIES_SUBTITLE,
		CONFERENCE_HOST,
		CONFERENCE_LOCATION,
		CONFERENCE_SPONSORS,
		CONFERENCE_TITLE,
		CONFERENCE_DATES,
		FULL_TITLE,
		ISO_TITLE_ABBREVIATION,
		ISSN,
		PUBLICATION_TYPE,
		TWENTY_NINE_CHARACTER_SOURCE_TITLE_ABBREVIATION,
	};

	// Reference Entity Information
	public static final String ANNOTATION = "ANNOTATION";
	public static final String REFERENCE_ARTICLE_NUMBER = "ARTICLE_NUMBER";
	public static final String REFERENCE_AUTHOR = "AUTHOR_ID";
	public static final String REFERENCE_DIGITAL_OBJECT_IDENTIFIER = "DIGITAL_OBJECT_IDENTIFIER";
	public static final String REFERENCE_OTHER_INFORMATION = "OTHER_INFORMATION";
	public static final String PAGE_NUMBER = "PAGE_NUMBER";
	public static final String PAPER = "DOCUMENT_ID";
	public static final String REFERENCE_STRING = "RAW_CITATION";
	public static final String SOURCE = "SOURCE_ID";
	public static final String YEAR = "YEAR";
	public static final String REFERENCE_WAS_STARRED = "STARRED";
	public static final String REFERENCE_VOLUME = "VOLUME";

	public static final String[] REFERENCE_FIELD_NAMES = new String[] {
		ANNOTATION,
		REFERENCE_AUTHOR,
		REFERENCE_DIGITAL_OBJECT_IDENTIFIER,
		PAGE_NUMBER,
		PAPER,
		REFERENCE_STRING,
		SOURCE,
		YEAR,
		REFERENCE_WAS_STARRED,
		REFERENCE_VOLUME,
	};

	// Address Entity Information
	public static final String ADDRESS_CITY = "CITY";
	public static final String COUNTRY = "COUNTRY";
	public static final String POSTAL_CODE = "POSTAL_CODE";
	public static final String RAW_ADDRESS = "RAW_ADDRESS";
	public static final String STATE_OR_PROVINCE = "STATE_OR_PROVINCE";
	public static final String STREET_ADDRESS = "STREET_ADDRESS";

	public static final String[] ADDRESS_FIELD_NAMES = new String[] {
		ADDRESS_CITY,
		COUNTRY,
		POSTAL_CODE,
		RAW_ADDRESS,
		STATE_OR_PROVINCE,
	};

	// Keyword Entity Information
	public static final String KEYWORD = "NAME";
	public static final String TYPE = "TYPE";

	public static final String[] KEYWORD_FIELD_NAMES = new String[] {
		KEYWORD,
		TYPE,
	};

	// Person Entity Information
	public static final String ADDITIONAL_NAME = "ADDITIONAL_NAME";
	public static final String FAMILY_NAME = "FAMILY_NAME";
	public static final String FIRST_INITIAL = "FIRST_INITIAL";
	public static final String FULL_NAME = "FULL_NAME";
	public static final String MIDDLE_INITIAL = "MIDDLE_INITIAL";
	public static final String PERSONAL_NAME = "FIRST_NAME";
	public static final String UNSPLIT_ABBREVIATED_NAME = "RAW_NAME";

	public static final String[] PERSON_FIELD_NAMES = new String[] {
		ADDITIONAL_NAME,
		FAMILY_NAME,
		FIRST_INITIAL,
		FULL_NAME,
		MIDDLE_INITIAL,
		PERSONAL_NAME,
		UNSPLIT_ABBREVIATED_NAME,
	};

	// Patent Entity Information
	public static final String PATENT_NUMBER = "NUMBER";

	public static final String[] PATENT_FIELD_NAMES = new String[] {
		PATENT_NUMBER,
	};

	// Document Entity Information
	public static final String ABSTRACT_TEXT = "ABSTRACT";
	public static final String ARTICLE_NUMBER = "DOCUMENT_NUMBER";
	public static final String BEGINNING_PAGE = "BEGINNING_PAGE";
	public static final String CITED_REFERENCE_COUNT = "CITED_CITATION_COUNT";
	public static final String DIGITAL_OBJECT_IDENTIFIER = "DIGITAL_OBJECT_IDENTIFIER";
	public static final String DOCUMENT_TYPE = "ISI_TYPE";
	public static final String DOCUMENT_VOLUME = "VOLUME";
	public static final String ENDING_PAGE = "ENDING_PAGE";
	public static final String FIRST_AUTHOR = "FIRST_AUTHOR_ID";
	public static final String FUNDING_AGENCY_AND_GRANT_NUMBER = "FUNDING_AGENCY_AND_GRANT_NUMBER";
	public static final String FUNDING_TEXT = "FUNDING_TEXT";
	public static final String ISBN = "ISBN";
	public static final String ISI_DOCUMENT_DELIVERY_NUMBER = "ISI_DOCUMENT_DELIVERY_NUMBER";
	public static final String ISI_UNIQUE_ARTICLE_IDENTIFIER = "ISI_UNIQUE_ARTICLE_IDENTIFIER";
	public static final String ISSUE = "ISSUE";
	public static final String LANGUAGE = "LANGUAGE";
	public static final String PAGE_COUNT = "PAGE_COUNT";
	public static final String PART_NUMBER = "PART_NUMBER";
	public static final String PUBLICATION_DATE = "PUBLICATION_DATE";
	public static final String PUBLICATION_YEAR = "PUBLICATION_YEAR";
	public static final String DOCUMENT_SOURCE = "SOURCE_ID";
	public static final String SPECIAL_ISSUE = "SPECIAL_ISSUE";
	public static final String SUBJECT_CATEGORY = "SUBJECT_CATEGORY";
	public static final String SUPPLEMENT = "SUPPLEMENT";
	public static final String TIMES_CITED = "TIMES_CITED";
	public static final String TITLE = "TITLE";
	public static final String CITE_AS = "CITE_AS";

	public static final String[] DOCUMENT_FIELD_NAMES = new String[] {
		ABSTRACT_TEXT,
		ARTICLE_NUMBER,
		BEGINNING_PAGE,
		CITED_REFERENCE_COUNT,
		DIGITAL_OBJECT_IDENTIFIER,
		DOCUMENT_TYPE,
		DOCUMENT_VOLUME,
		ENDING_PAGE,
		FIRST_AUTHOR,
		FUNDING_AGENCY_AND_GRANT_NUMBER,
		FUNDING_TEXT,
		ISBN,
		ISI_DOCUMENT_DELIVERY_NUMBER,
		ISI_UNIQUE_ARTICLE_IDENTIFIER,
		ISSUE,
		LANGUAGE,
		PAGE_COUNT,
		PART_NUMBER,
		PUBLICATION_DATE,
		PUBLICATION_YEAR,
		DOCUMENT_SOURCE,
		SPECIAL_ISSUE,
		SUBJECT_CATEGORY,
		SUPPLEMENT,
		TIMES_CITED,
		TITLE,
		CITE_AS,
	};

	// Relationship Type Information

	public static final String PUBLISHER_ADDRESSES_DISPLAY_NAME = "Publisher Addresses";
	public static final String PUBLISHER_ADDRESSES_TABLE_NAME = "PUBLISHER_ADDRESSES";

	public static final String REPRINT_ADDRESSES_DISPLAY_NAME = "Reprint Addresses";
	public static final String REPRINT_ADDRESSES_TABLE_NAME = "REPRINT_ADDRESS";

	public static final String RESEARCH_ADDRESSES_DISPLAY_NAME = "Research Addresses";
	public static final String RESEARCH_ADDRESSES_TABLE_NAME = "RESEARCH_ADDRESSES";

	public static final String DOCUMENT_KEYWORDS_DISPLAY_NAME = "Document Keywords";
	public static final String DOCUMENT_KEYWORDS_TABLE_NAME = "DOCUMENT_KEYWORDS";

	public static final String AUTHORS_DISPLAY_NAME = "Authors";
	public static final String AUTHORS_TABLE_NAME = "AUTHORS";

	public static final String EDITORS_DISPLAY_NAME = "Editors";
	public static final String EDITORS_TABLE_NAME = "EDITORS";

	public static final String CITED_PATENTS_DISPLAY_NAME = "Cited Patents";
	public static final String CITED_PATENTS_TABLE_NAME = "CITED_PATENTS";

	public static final String DOCUMENT_OCCURRENCES_DISPLAY_NAME = "Document Occurrences";
	public static final String DOCUMENT_OCCURRENCES_TABLE_NAME = "DOCUMENT_OCCURRENCES";

	public static final String CITED_REFERENCES_DISPLAY_NAME = "Cited Citations";
	public static final String CITED_REFERENCES_TABLE_NAME = "CITED_CITATIONS";

	public static final String[] RELATIONSHIP_TABLE_DISPLAY_NAMES = new String[] {
		PUBLISHER_ADDRESSES_DISPLAY_NAME,
		REPRINT_ADDRESSES_DISPLAY_NAME,
		RESEARCH_ADDRESSES_DISPLAY_NAME,
		DOCUMENT_KEYWORDS_DISPLAY_NAME,
		AUTHORS_DISPLAY_NAME,
		EDITORS_DISPLAY_NAME,
		CITED_PATENTS_DISPLAY_NAME,
		DOCUMENT_OCCURRENCES_DISPLAY_NAME,
		CITED_REFERENCES_DISPLAY_NAME,
	};

	public static final String[] RELATIONSHIP_TABLE_DATABASE_NAMES = new String[] {
		PUBLISHER_ADDRESSES_TABLE_NAME,
		REPRINT_ADDRESSES_TABLE_NAME,
		RESEARCH_ADDRESSES_TABLE_NAME,
		DOCUMENT_KEYWORDS_TABLE_NAME,
		AUTHORS_TABLE_NAME,
		EDITORS_TABLE_NAME,
		CITED_PATENTS_TABLE_NAME,
		DOCUMENT_OCCURRENCES_TABLE_NAME,
		CITED_REFERENCES_TABLE_NAME,
	};

	// Commonly-Found Entity Relationship Table Information
	public static final String ORDER_LISTED = "ORDER_LISTED";

	// Publisher Addresses Relationship Information

	public static final String PUBLISHER_ADDRESSES_PUBLISHER_FOREIGN_KEY =
		"PUBLISHER_ID";
	public static final String PUBLISHER_ADDRESSES_ADDRESS_FOREIGN_KEY =
		"ADDRESS_ID";

	public static final String[] PUBLISHER_ADDRESSES_FIELD_NAMES = new String[] {
		PUBLISHER_ADDRESSES_PUBLISHER_FOREIGN_KEY,
		PUBLISHER_ADDRESSES_ADDRESS_FOREIGN_KEY,
	};

	// Reprint Addresses Relationship Information
	
	public static final String REPRINT_ADDRESSES_DOCUMENT_FOREIGN_KEY =
		"DOCUMENT_ID";
	public static final String REPRINT_ADDRESSES_ADDRESS_FOREIGN_KEY =
		"ADDRESS_ID";

	public static final String[] REPRINT_ADDRESSES_FIELD_NAMES = new String[] {
		REPRINT_ADDRESSES_DOCUMENT_FOREIGN_KEY,
		REPRINT_ADDRESSES_DOCUMENT_FOREIGN_KEY,
	};

	// Research Addresses Relationship Information

	public static final String RESEARCH_ADDRESSES_DOCUMENT_FOREIGN_KEY =
		"DOCUMENT_ID";
	public static final String RESEARCH_ADDRESSES_ADDRESS_FOREIGN_KEY =
		"ADDRESS_ID";

	public static final String[] RESEARCH_ADDRESSES_FIELD_NAMES = new String[] {
		RESEARCH_ADDRESSES_DOCUMENT_FOREIGN_KEY,
		RESEARCH_ADDRESSES_ADDRESS_FOREIGN_KEY,
		ORDER_LISTED,
	};

	// Document Keywords Relationship Information

	public static final String DOCUMENT_KEYWORDS_DOCUMENT_FOREIGN_KEY =
		"DOCUMENT_ID";
	public static final String DOCUMENT_KEYWORDS_KEYWORD_FOREIGN_KEY =
		"KEYWORD_ID";

	public static final String[] DOCUMENT_KEYWORDS_FIELD_NAMES = new String[] {
		DOCUMENT_KEYWORDS_DOCUMENT_FOREIGN_KEY,
		DOCUMENT_KEYWORDS_KEYWORD_FOREIGN_KEY,
		ORDER_LISTED,
	};

	// Authors Relationship Information

	public static final String AUTHORS_DOCUMENT_FOREIGN_KEY = "DOCUMENT_ID";
	public static final String AUTHORS_PERSON_FOREIGN_KEY = "PERSON_ID";
	public static final String AUTHORS_EMAIL_ADDRESS = "EMAIL_ADDRESS";

	public static final String[] AUTHORS_FIELD_NAMES = new String[] {
		AUTHORS_DOCUMENT_FOREIGN_KEY,
		AUTHORS_PERSON_FOREIGN_KEY,
		AUTHORS_EMAIL_ADDRESS,
		ORDER_LISTED,
	};

	// Editors Relationship Information

	public static final String EDITORS_DOCUMENT_FOREIGN_KEY = "DOCUMENT_ID";
	public static final String EDITORS_PERSON_FOREIGN_KEY = "PERSON_ID";

	public static final String[] EDITORS_FIELD_NAMES = new String[] {
		EDITORS_DOCUMENT_FOREIGN_KEY,
		EDITORS_PERSON_FOREIGN_KEY,
		ORDER_LISTED,
	};

	// Cited Patents Relationship Information

	public static final String CITED_PATENTS_DOCUMENT_FOREIGN_KEY = "DOCUMENT_ID";
	public static final String CITED_PATENTS_PATENT_FOREIGN_KEY = "PATENT_ID";

	public static final String[] CITED_PATENTS_FIELD_NAMES = new String[] {
		CITED_PATENTS_DOCUMENT_FOREIGN_KEY,
		CITED_PATENTS_PATENT_FOREIGN_KEY,
	};

	// Document Occurrences Relationship Information

	public static final String DOCUMENT_OCCURRENCES_DOCUMENT_FOREIGN_KEY =
		"DOCUMENT_ID";
	public static final String DOCUMENT_OCCURRENCES_ISI_FILE_FOREIGN_KEY =
		"FILE_ID";

	public static final String[] DOCUMENT_OCCURRENCES_FIELD_NAMES = new String[] {
		DOCUMENT_OCCURRENCES_DOCUMENT_FOREIGN_KEY,
		DOCUMENT_OCCURRENCES_ISI_FILE_FOREIGN_KEY,
	};

	// Cited References Relationship Information

	public static final String CITED_REFERENCES_DOCUMENT_FOREIGN_KEY =
		"DOCUMENT_ID";
	public static final String CITED_REFERENCES_REFERENCE_FOREIGN_KEY =
		"CITATION_ID";

	public static final String[] CITED_REFERENCES_FIELD_NAMES = new String[] {
		CITED_REFERENCES_DOCUMENT_FOREIGN_KEY,
		CITED_REFERENCES_REFERENCE_FOREIGN_KEY,
	};


	// Some Meaningful Values.

	public static final int NULL_YEAR = -1;
	public static final int NULL_VOLUME = -1;
	public static final int NULL_PAGE_NUMBER = -1;
}