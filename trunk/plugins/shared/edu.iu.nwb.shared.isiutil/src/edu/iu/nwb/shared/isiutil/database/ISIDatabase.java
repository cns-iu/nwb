package edu.iu.nwb.shared.isiutil.database;

public class ISIDatabase {
	// Entity Type Information

	public static final String ISI_FILE_TYPE_NAME = "ISI Files";
	public static final String ISI_FILE_TABLE_NAME = "ISI_FILES";

	public static final String PUBLISHER_TYPE_NAME = "Publishers";
	public static final String PUBLISHER_TABLE_NAME = "PUBLISHER";

	public static final String SOURCE_TYPE_NAME = "Sources";
	public static final String SOURCE_TABLE_NAME = "SOURCE";

	public static final String REFERENCE_TYPE_NAME = "References";
	public static final String REFERENCE_TABLE_NAME = "REFERENCE";

	public static final String ADDRESS_TYPE_NAME = "Addresses";
	public static final String ADDRESS_TABLE_NAME = "ADDRESS";

	public static final String KEYWORD_TYPE_NAME = "Keywords";
	public static final String KEYWORD_TABLE_NAME = "KEYWORD";

	public static final String PERSON_TYPE_NAME = "People";
	public static final String PERSON_TABLE_NAME = "REFERENCE_AUTHOR";

	public static final String PATENT_TYPE_NAME = "Patents";
	public static final String PATENT_TABLE_NAME = "PATENT";

	public static final String DOCUMENT_TYPE_NAME = "Documents";
	public static final String DOCUMENT_TABLE_NAME = "DOCUMENT";

	// ISI File Entity Information
	public static final String FILE_NAME = "file_name";
	public static final String FILE_TYPE = "file_type";
	public static final String FILE_FORMAT_VERSION_NUMBER = "file_format_version_number";

	// Publisher Entity Information
	public static final String NAME = "name";
	public static final String PUBLISHER_CITY = "publisher_city";
	public static final String WEB_ADDRESS = "web_address";
	public static final String PUBLISHER_SOURCE = "publisher_source";

	// Source Entity Information
	public static final String FULL_TITLE = "full_title";
	public static final String PUBLICATION_TYPE = "publication_type";
	public static final String ISO_TITLE_ABBREVIATION = "iso_title_abbreviation";
	public static final String BOOK_SERIES_TITLE = "book_series_title";
	public static final String BOOK_SERIES_SUBTITLE = "book_series_subtitle";
	public static final String ISSN = "issn";
	public static final String TWENTY_NINE_CHARACTER_SOURCE_TITLE_ABBREVIATION =
		"twenty_nine_character_source_title_abbreviation";
	public static final String CONFERENCE_TITLE = "conference_title";
	public static final String CONFERENCE_DATE = "conference_date";
	public static final String CONFERENCE_DONATION = "conference_donation";

	// Reference Entity Information
	public static final String REFERENCE_STRING = "reference_string";
	public static final String PAPER = "paper";
	public static final String REFERENCE_AUTHOR = "reference_author";
	public static final String SOURCE = "source";
	public static final String YEAR = "year";
	public static final String REFERENCE_VOLUME = "reference_volume";
	public static final String PAGE_NUMBER = "page_number";
	public static final String ANNOTATION = "annotation";
	public static final String AUTHOR_WAS_STARRED = "author_was_starred";

	// Address Entity Information
	public static final String STREET_ADDRESS = "street_address";
	public static final String ADDRESS_CITY = "address_city";
	public static final String STATE_OR_PROVINCE = "state_or_province";
	public static final String POSTAL_CODE = "postal_code";
	public static final String COUNTRY = "country";
	public static final String RAW_ADDRESS = "raw_address";

	// Keyword Entity Information
	public static final String TYPE = "type";
	public static final String KEYWORD = "keyword";

	// Person Entity Information
	public static final String PERSONAL_NAME = "personal_name";
	public static final String ADDITIONAL_NAME = "additional_name";
	public static final String FAMILY_NAME = "family_name";
	public static final String FIRST_INITIAL = "first_initial";
	public static final String MIDDLE_INITIAL = "middle_initial";
	public static final String UNSPLIT_NAME = "unsplit_name";
	public static final String FULL_NAME = "full_name";

	// Patent Entity Information
	public static final String PATENT_NUMBER = "patent_number";

	// Document Entity Information
	public static final String DIGITAL_OBJECT_IDENTIFIER = "digital_object_identifier";
	public static final String TITLE = "title";
	public static final String ARTICLE_NUMBER = "article_number";
	public static final String FIRST_AUTHOR = "first_author";
	public static final String LANGUAGE = "language";
	public static final String DOCUMENT_TYPE = "document_type";
	public static final String CITED_REFERENCE_COUNT = "cited_reference_count";
	public static final String ABSTRACT_TEXT = "abstract_text";
	public static final String TIMES_CITED = "times_cited";
	public static final String BEGINNING_PAGE = "beginning_page";
	public static final String ENDING_PAGE = "ending_page";
	public static final String PAGE_COUNT = "page_count";
	public static final String ISI_UNIQUE_ARTICLE_IDENTIFIER = "isi_unique_article_identifier";
	public static final String PUBLICATION_YEAR = "publication_year";
	public static final String PUBLICATION_DATE = "publication_date";
	public static final String DOCUMENT_VOLUME = "document_volume";
	public static final String ISSUE = "issue";
	public static final String SUPPLEMENT = "supplement";
	public static final String SPECIAL_ISSUE = "special_issue";
	public static final String ISI_DOCUMENT_DELIVERY_NUMBER = "isi_document_delivery_number";
	public static final String ISBN = "isbn";
	public static final String EMAIL_ADDRESS = "email_address";

	// Publisher Addresses Relationship Information
	public static final String PUBLISHER_ADDRESSES_TYPE_NAME = "Publisher Addresses";
	public static final String PUBLISHER_ADDRESSES_TABLE_NAME = "PUBLISHER_ADDRESSES";

	// Reprint Addresses Relationship Information
	public static final String REPRINT_ADDRESSES_TYPE_NAME = "Reprint Addresses";
	public static final String REPRINT_ADDRESSES_TABLE_NAME = "REPRINT_ADDRESSES";

	// Research Addresses Relationship Information
	public static final String RESEARCH_ADDRESSES_TYPE_NAME = "Research Addresses";
	public static final String RESEARCH_ADDRESSES_TABLE_NAME = "RESEARCH_ADDRESSES";

	// Document Keywords Relationship Information
	public static final String DOCUMENT_KEYWORDS_TYPE_NAME = "Document Keywords";
	public static final String DOCUMENT_KEYWORDS_TABLE_NAME = "DOCUMENT_KEYWORDS";

	// Authors Relationship Information
	public static final String AUTHORS_TYPE_NAME = "Authors";
	public static final String AUTHORS_TABLE_NAME = "AUTHORS";

	// Editors Relationship Information
	public static final String EDITORS_TYPE_NAME = "Editors";
	public static final String EDITORS_TABLE_NAME = "EDITORS";

	// Cited Patents Relationship Information
	public static final String CITED_PATENTS_TYPE_NAME = "Cited Patents";
	public static final String CITED_PATENTS_TABLE_NAME = "CITED_PATENTS";

	// Document Occurrences Relationship Information
	public static final String DOCUMENT_OCCURRENCES_TYPE_NAME = "Document Occurrences";
	public static final String DOCUMENT_OCCURRENCES_TABLE_NAME = "DOCUMENT_OCCURRENCES";

	// Cited References Relationship Information
	public static final String CITED_REFERENCES_TYPE_NAME = "Cited References";
	public static final String CITED_REFERENCES_TABLE_NAME = "CITED_REFERENCES";

	// Commonly-Found Entity Relationship Table Information
	public static final String ORDER_LISTED = "order_listed";

	// Some Meaningful Values.
	public static final int NULL_YEAR = -1;
	public static final int NULL_VOLUME = -1;
	public static final int NULL_PAGE_NUMBER = -1;
}