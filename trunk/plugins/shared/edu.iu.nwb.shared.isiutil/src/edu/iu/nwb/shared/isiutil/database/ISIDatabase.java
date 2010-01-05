package edu.iu.nwb.shared.isiutil.database;

public class ISIDatabase {
	public static final String ISI_DATABASE_MIME_TYPE = "db:isi";

	// Entity Type Information

	public static final String ISI_FILE_DISPLAY_NAME = "ISI Files";
	public static final String ISI_FILE_TABLE_NAME = "ISI_FILES";

	public static final String PUBLISHER_DISPLAY_NAME = "Publishers";
	public static final String PUBLISHER_TABLE_NAME = "PUBLISHER";

	public static final String SOURCE_DISPLAY_NAME = "Sources";
	public static final String SOURCE_TABLE_NAME = "SOURCE";

	public static final String REFERENCE_DISPLAY_NAME = "References";
	public static final String REFERENCE_TABLE_NAME = "REFERENCE";

	public static final String ADDRESS_DISPLAY_NAME = "Addresses";
	public static final String ADDRESS_TABLE_NAME = "ADDRESS";

	public static final String KEYWORD_DISPLAY_NAME = "Keywords";
	public static final String KEYWORD_TABLE_NAME = "KEYWORD";

	public static final String PERSON_DISPLAY_NAME = "People";
	public static final String PERSON_TABLE_NAME = "PERSON";

	public static final String PATENT_DISPLAY_NAME = "Patents";
	public static final String PATENT_TABLE_NAME = "PATENT";

	public static final String DOCUMENT_DISPLAY_NAME = "Documents";
	public static final String DOCUMENT_TABLE_NAME = "DOCUMENT";

	// ISI File Entity Information
	public static final String FILE_FORMAT_VERSION_NUMBER = "file_format_version_number";
	public static final String FILE_NAME = "file_name";
	public static final String FILE_TYPE = "file_type";

	// Publisher Entity Information
	public static final String PUBLISHER_CITY = "publisher_city";
	public static final String PUBLISHER_NAME = "name";
	public static final String PUBLISHER_SOURCE = "publisher_source";
	public static final String PUBLISHER_WEB_ADDRESS = "web_address";

	// Source Entity Information
	public static final String BOOK_SERIES_TITLE = "book_series_title";
	public static final String BOOK_SERIES_SUBTITLE = "book_series_subtitle";
	public static final String CONFERENCE_HOST = "conference_host";
	public static final String CONFERENCE_LOCATION = "conference_location";
	public static final String CONFERENCE_SPONSORS = "conference_sponsors";
	public static final String CONFERENCE_TITLE = "conference_title";
	public static final String FULL_TITLE = "full_title";
	public static final String ISO_TITLE_ABBREVIATION = "iso_title_abbreviation";
	public static final String ISSN = "issn";
	public static final String PUBLICATION_TYPE = "publication_type";
	public static final String TWENTY_NINE_CHARACTER_SOURCE_TITLE_ABBREVIATION =
		"twenty_nine_character_source_title_abbreviation";

	// Reference Entity Information
	public static final String ANNOTATION = "annotation";
	public static final String REFERENCE_AUTHOR = "reference_author";
	public static final String AUTHOR_WAS_STARRED = "author_was_starred";
	public static final String PAGE_NUMBER = "page_number";
	public static final String PAPER = "paper";
	public static final String REFERENCE_STRING = "reference_string";
	public static final String REFERENCE_VOLUME = "reference_volume";
	public static final String SOURCE = "source";
	public static final String YEAR = "year";

	// Address Entity Information
	public static final String ADDRESS_CITY = "address_city";
	public static final String COUNTRY = "country";
	public static final String POSTAL_CODE = "postal_code";
	public static final String RAW_ADDRESS = "raw_address";
	public static final String STATE_OR_PROVINCE = "state_or_province";
	public static final String STREET_ADDRESS = "street_address";

	// Keyword Entity Information
	public static final String KEYWORD = "keyword";
	public static final String TYPE = "type";

	// Person Entity Information
	public static final String ADDITIONAL_NAME = "additional_name";
	public static final String FAMILY_NAME = "family_name";
	public static final String FIRST_INITIAL = "first_initial";
	public static final String FULL_NAME = "full_name";
	public static final String MIDDLE_INITIAL = "middle_initial";
	public static final String PERSONAL_NAME = "personal_name";
	public static final String UNSPLIT_ABBREVIATED_NAME = "unsplit_name";

	// Patent Entity Information
	public static final String PATENT_NUMBER = "patent_number";

	// Document Entity Information
	public static final String ABSTRACT_TEXT = "abstract_text";
	public static final String ARTICLE_NUMBER = "article_number";
	public static final String BEGINNING_PAGE = "beginning_page";
	public static final String CITED_REFERENCE_COUNT = "cited_reference_count";
	public static final String CITED_YEAR = "cited_year";
	public static final String DIGITAL_OBJECT_IDENTIFIER = "digital_object_identifier";
	public static final String DOCUMENT_TYPE = "document_type";
	public static final String DOCUMENT_VOLUME = "document_volume";
	public static final String ENDING_PAGE = "ending_page";
	public static final String FIRST_AUTHOR = "first_author_fk";
	public static final String FUNDING_AGENCY_AND_GRANT_NUMBER = "funding_agency_and_grant_number";
	public static final String FUNDING_TEXT = "funding_text";
	public static final String ISBN = "isbn";
	public static final String ISI_DOCUMENT_DELIVERY_NUMBER = "isi_document_delivery_number";
	public static final String ISI_UNIQUE_ARTICLE_IDENTIFIER = "isi_unique_article_identifier";
	public static final String ISSUE = "issue";
	public static final String LANGUAGE = "language";
	public static final String PAGE_COUNT = "page_count";
	public static final String PART_NUMBER = "part_number";
	public static final String PUBLICATION_DATE = "publication_date";
	public static final String PUBLICATION_YEAR = "publication_year";
	public static final String SPECIAL_ISSUE = "special_issue";
	public static final String SUBJECT_CATEGORY = "subject_category";
	public static final String SUPPLEMENT = "supplement";
	public static final String TIMES_CITED = "times_cited";
	public static final String TITLE = "title";

	// Relationship Type Information

	public static final String PUBLISHER_ADDRESSES_DISPLAY_NAME = "Publisher Addresses";
	public static final String PUBLISHER_ADDRESSES_TABLE_NAME = "PUBLISHER_ADDRESSES";

	public static final String REPRINT_ADDRESSES_DISPLAY_NAME = "Reprint Addresses";
	public static final String REPRINT_ADDRESSES_TABLE_NAME = "REPRINT_ADDRESSES";

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

	public static final String CITED_REFERENCES_DISPLAY_NAME = "Cited References";
	public static final String CITED_REFERENCES_TABLE_NAME = "CITED_REFERENCES";

	// Commonly-Found Entity Relationship Table Information
	public static final String ORDER_LISTED = "order_listed";

	// Publisher Addresses Relationship Information

	public static final String PUBLISHER_ADDRESSES_PUBLISHER_FOREIGN_KEY =
		"publisher_address_publisher_fk";
	public static final String PUBLISHER_ADDRESSES_ADDRESS_FOREIGN_KEY =
		"publisher_address_address_fk";

	// Reprint Addresses Relationship Information
	
	public static final String REPRINT_ADDRESSES_PUBLISHER_FOREIGN_KEY =
		"reprint_address_document_fk";
	public static final String REPRINT_ADDRESSES_ADDRESS_FOREIGN_KEY =
		"reprint_address_address_fk";

	// Research Addresses Relationship Information

	public static final String RESEARCH_ADDRESSES_PUBLISHER_FOREIGN_KEY =
		"research_address_document_fk";
		//"document_fk";
	public static final String RESEARCH_ADDRESSES_ADDRESS_FOREIGN_KEY =
		"research_address_address_fk";

	// Document Keywords Relationship Information

	public static final String DOCUMENT_KEYWORDS_DOCUMENT_FOREIGN_KEY =
		"document_keywords_document_fk";
		//"document_fk";
	public static final String DOCUMENT_KEYWORDS_KEYWORD_FOREIGN_KEY =
		"document_keywords_keyword_fk";

	// Authors Relationship Information

	public static final String AUTHORS_DOCUMENT_FOREIGN_KEY = "authors_document_fk";
	public static final String AUTHORS_PERSON_FOREIGN_KEY = "authors_person_fk";
	public static final String AUTHORS_EMAIL_ADDRESS = "email_address";

	// Editors Relationship Information

	public static final String EDITORS_DOCUMENT_FOREIGN_KEY = "editors_document_fk";
	public static final String EDITORS_PERSON_FOREIGN_KEY = "editors_person_fk";

	// Cited Patents Relationship Information

	public static final String CITED_PATENTS_DOCUMENT_FOREIGN_KEY = "cited_patents_document_fk";
	public static final String CITED_PATENTS_PATENT_FOREIGN_KEY = "cited_patents_patent_fk";

	// Document Occurrences Relationship Information

	public static final String DOCUMENT_OCCURRENCES_DOCUMENT_FOREIGN_KEY =
		"document_occurrences_document_fk";
	public static final String DOCUMENT_OCCURRENCES_ISI_FILE_FOREIGN_KEY =
		"document_occurrences_isi_file_fk";

	// Cited References Relationship Information

	public static final String CITED_REFERENCES_DOCUMENT_FOREIGN_KEY =
		"cited_references_document_fk";
	public static final String CITED_REFERENCES_REFERENCE_FOREIGN_KEY =
		"cited_references_reference_fk";


	// Some Meaningful Values.
	public static final int NULL_YEAR = -1;
	public static final int NULL_VOLUME = -1;
	public static final int NULL_PAGE_NUMBER = -1;
}