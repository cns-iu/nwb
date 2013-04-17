package edu.iu.nwb.shared.isiutil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import com.google.common.collect.Ordering;

public final class ISITag {
	/*
	 * IMPORTANT: If you add a tag here, make sure to add it in the isiTagArray below as well.
	 * Sorting these alphabetically is a little more sane.
	 * 
	 * Tag references:
	 * http://images.webofknowledge.com/WOK45/help/RCI/WOK/hft_wos.html
	 * http://isi-rb.rubyforge.org/
	 */
	
	public static final ISITag ABSTRACT = new ISITag("AB", "Abstract", ContentType.TEXT);
	public static final ISITag ARTICLE_NUMBER_OF_NEW_APS_JOURNALS =
		new ISITag("AR", "New Article Number",  ContentType.TEXT);
	public static final ISITag AUTHORS =
		new ISITag("AU", "Authors", ContentType.MULTI_VALUE_TEXT, "\n");
	public static final ISITag AUTHORS_FULL_NAMES =
		new ISITag("AF", "Authors (Full Names)", ContentType.MULTI_VALUE_TEXT, "\n");

	public static final ISITag BEGINNING_PAGE =
		new ISITag("BP", "Beginning Page", ContentType.TEXT);
	public static final ISITag BOOK_SERIES_TITLE =
		new ISITag("SE", "Book Series Title", ContentType.TEXT);
	public static final ISITag BOOK_SERIES_SUBTITLE =
		new ISITag("BS", "Book Series Subtitle", ContentType.TEXT);

	public static final ISITag CITED_PATENT = new ISITag("CP", "Cited Patent", ContentType.TEXT);
	public static final ISITag CITED_REFERENCE_COUNT =
		new ISITag("NR", "Cited Reference Count", ContentType.INTEGER);
	public static final ISITag CITED_REFERENCES =
		new ISITag("CR", "Cited References", ContentType.MULTI_VALUE_TEXT, "\n");
	public static final ISITag CONFERENCE_DATES =
		new ISITag("CY", "Conference Dates", ContentType.TEXT);
	public static final ISITag CONFERENCE_HOST =
		new ISITag("HO", "Conference Host", ContentType.TEXT);
	public static final ISITag CONFERENCE_LOCATION =
		new ISITag("CL", "Conference Location", ContentType.TEXT);
	public static final ISITag CONFERENCE_SPONSORS =
		new ISITag("SP", "Conference Sponsors", ContentType.TEXT);
	public static final ISITag CONFERENCE_TITLE =
		new ISITag("CT", "Conference Title", ContentType.TEXT);

	public static final ISITag DOCUMENT_TYPE = new ISITag("DT", "Document Type", ContentType.TEXT);
	public static final ISITag DOI = new ISITag("DI", "DOI", ContentType.TEXT);

	public static final ISITag EDITORS =
		new ISITag("ED", "Editors", ContentType.MULTI_VALUE_TEXT, "\n");
	public static final ISITag EMAIL_ADDRESSES =
		new ISITag("EM", "E-mail Addresses", ContentType.MULTI_VALUE_TEXT, ";\n"); //Separators are both semicolon and newline
	public static final ISITag END_OF_FILE =
		new ISITag("EF", "End of File", ContentType.NULL, true);
	public static final ISITag ENDING_PAGE = new ISITag("EP", "Ending Page", ContentType.TEXT);
	public static final ISITag END_OF_RECORD =
		new ISITag("ER", "End of RecordtagName", ContentType.NULL);

	public static final ISITag FILE_TYPE = new ISITag("FN", "File Type", ContentType.TEXT, true);
	public static final ISITag FULL_JOURNAL_TITLE =
		new ISITag("SO", "Journal Title (Full)", ContentType.TEXT);
	public static final ISITag FUNDING_AGENCY_AND_GRANT_NUMBER =
		new ISITag("FU", "Funding Agency and Grant Number", ContentType.TEXT);
	public static final ISITag FUNDING_TEXT = new ISITag("FX", "Funding Text", ContentType.TEXT);

	public static final ISITag ISBN = new ISITag("BN", "ISBN", ContentType.TEXT);
	public static final ISITag ISSN = new ISITag("SN",  "ISSN", ContentType.TEXT);
	public static final ISITag ISI_DOCUMENT_DELIVERY_NUMBER =
		new ISITag("GA", "ISI Document Delivery Number", ContentType.TEXT);
	public static final ISITag ISO_JOURNAL_TITLE_ABBREVIATION =
		new ISITag("JI", "Journal Name (Abbreviated ISO)", ContentType.TEXT);
	public static final ISITag ISSUE = new ISITag("IS", "Issue", ContentType.TEXT);

	public static final ISITag LANGUAGE = new ISITag("LA", "Language", ContentType.TEXT);

	public static final ISITag NEW_KEYWORDS_GIVEN_BY_ISI =
		new ISITag("ID", "New ISI Keywords", ContentType.MULTI_VALUE_TEXT, ";");
	public static final ISITag NUMBER_OF_PAGES =
		new ISITag("PG", "Number of Pages", ContentType.INTEGER);

	public static final ISITag ORIGINAL_KEYWORDS =
		new ISITag("DE", "Original Keywords", ContentType.MULTI_VALUE_TEXT, ";");

	public static final ISITag PART_NUMBER = new ISITag("PN", "Part Number", ContentType.TEXT);
	public static final ISITag PUBLICATION_DATE =
		new ISITag("PD", "Publication Date", ContentType.TEXT);
	public static final ISITag PUBLICATION_TYPE =
		new ISITag("PT", "Publication Type", ContentType.TEXT);
	public static final ISITag PUBLICATION_YEAR =
		new ISITag("PY", "Publication Year", ContentType.INTEGER);
	public static final ISITag PUBLISHER_ADDRESS =
		new ISITag("PA", "Publisher Address", ContentType.TEXT);
	public static final ISITag PUBLISHER_CITY =
		new ISITag("PI", "City of Publisher", ContentType.TEXT);
	public static final ISITag PUBLISHER = new ISITag("PU", "Publisher", ContentType.TEXT);
	public static final ISITag PUBLISHER_WEB_ADDRESS =
		new ISITag("WP", "Publisher Web Address", ContentType.TEXT);

	public static final ISITag REPRINT_ADDRESS =
		new ISITag("RP", "Reprint Address", ContentType.TEXT);
	public static final ISITag RESEARCH_ADDRESSES =
		new ISITag("C1", "Research Addresses", ContentType.MULTI_VALUE_TEXT, "\n");
	public static final ISITag RESEARCH_FIELD = 
		new ISITag("WC", "Research Field", ContentType.MULTI_VALUE_TEXT, ";");
	public static final ISITag RESEARCHER_ID =
		new ISITag("RID", "Researcher ID", ContentType.TEXT); 
	
	public static final ISITag SPECIAL_ISSUE =
		new ISITag("SI", "Special Issue",  ContentType.TEXT);
	public static final ISITag SUBJECT_CATEGORY =
		new ISITag("SC", "Subject Category", ContentType.TEXT);
	public static final ISITag SUPPLEMENT = new ISITag("SU", "Supplement", ContentType.TEXT);

	public static final ISITag TIMES_CITED = new ISITag("TC", "Times Cited", ContentType.INTEGER);
	public static final ISITag TITLE = new ISITag("TI", "Title", ContentType.TEXT);
	public static final ISITag TOTAL_TIMES_CITED = 
			new ISITag("Z9", "Total Times Cited", ContentType.INTEGER);
	public static final ISITag TWENTY_NINE_CHAR_JOURNAL_ABBREVIATION =
		new ISITag("J9", "Journal Name (Abbreviated)", ContentType.TEXT);

	public static final ISITag UNIQUE_ID = new ISITag("UT", "Unique ID", ContentType.TEXT);

	public static final ISITag VERSION_NUMBER =
		new ISITag("VR", "Version Number", ContentType.TEXT, true);
	public static final ISITag VOLUME = new ISITag("VL", "Volume", ContentType.TEXT);
	
	private static final ISITag[] isiTagArray = {
		ABSTRACT,
		ARTICLE_NUMBER_OF_NEW_APS_JOURNALS,
		AUTHORS,
		AUTHORS_FULL_NAMES,

		BEGINNING_PAGE,
		BOOK_SERIES_TITLE,
		BOOK_SERIES_SUBTITLE,

		CITED_PATENT,
		CITED_REFERENCE_COUNT,
		CITED_REFERENCES,
		CONFERENCE_DATES,
		CONFERENCE_HOST,
		CONFERENCE_LOCATION,
		CONFERENCE_SPONSORS,
		CONFERENCE_TITLE,

		DOCUMENT_TYPE,
		DOI,

		EDITORS,
		EMAIL_ADDRESSES,
		END_OF_FILE,
		ENDING_PAGE, 
		END_OF_RECORD,

		FILE_TYPE,
		FULL_JOURNAL_TITLE,
		FUNDING_AGENCY_AND_GRANT_NUMBER,
		FUNDING_TEXT,

		ISBN,
		ISSN,
		ISI_DOCUMENT_DELIVERY_NUMBER, 
		ISO_JOURNAL_TITLE_ABBREVIATION,
		ISSUE,

		LANGUAGE,

		NEW_KEYWORDS_GIVEN_BY_ISI,
		NUMBER_OF_PAGES,

		ORIGINAL_KEYWORDS,

		PART_NUMBER,
		PUBLICATION_DATE,
		PUBLICATION_TYPE,
		PUBLICATION_YEAR,
		PUBLISHER,
		PUBLISHER_ADDRESS,
		PUBLISHER_CITY,
		PUBLISHER_WEB_ADDRESS,

		REPRINT_ADDRESS,
		RESEARCH_ADDRESSES,
		RESEARCH_FIELD,
		RESEARCHER_ID,

		SPECIAL_ISSUE,
		SUBJECT_CATEGORY,
		SUPPLEMENT,

		TIMES_CITED,
		TITLE,
		TOTAL_TIMES_CITED,
		TWENTY_NINE_CHAR_JOURNAL_ABBREVIATION,

		UNIQUE_ID,

		VERSION_NUMBER,
		VOLUME, 
	};
	
	private static List<ISITag> isiTags = new ArrayList<ISITag>();
	private static Dictionary<String, ISITag> tagNameToTag = new Hashtable<String, ISITag>();
	private static Dictionary<ContentType, List<ISITag>> typeToTags = new Hashtable<ContentType, List<ISITag>>();
	private static Dictionary<String, String> tagNameToColumnName = new Hashtable<String, String>();

	private static List<ISITag> arbitraryISITags = new ArrayList<ISITag>();
	private static Dictionary<String, ISITag> arbitraryTagNameToTag = new Hashtable<String, ISITag>();
	private static Dictionary<ContentType, List<ISITag>> arbitraryTypeToTags = new Hashtable<ContentType, List<ISITag>>();
	private static Dictionary<String, String> arbitraryTagNameToColumnName = new Hashtable<String, String>();

	static {
		for (int ii = 0; ii < isiTagArray.length; ii++) {
			ISITag tag = isiTagArray[ii];
			
			addTagInternal(tag, false);
		}
	}
	
	private static void addTagInternal(ISITag newTag, boolean checkForDuplicates) {
		if (checkForDuplicates) {
			for (int ii = 0; ii < isiTags.size(); ii++) {	
				ISITag tag = isiTags.get(ii);
				
				if (newTag.tagName.equals(tag.tagName)) {
					System.err.println(
							"Attempted to add a tag tagNamed '"
							+ newTag.tagName
							+ "'"
							+ " when one with the same tagName is already known.");
					System.err.println("Ignoring attempt to add new tag");
					return;
				}
			}
		}
		
		//add to isiTags
		
		isiTags.add(newTag);
		//add to tagNameToTag
		
		tagNameToTag.put(newTag.tagName, newTag);
		
		//add to tagNameToColumnName
		
		tagNameToColumnName.put(newTag.tagName, newTag.columnName);
		
		//add to typeToTags

		ContentType tagType = newTag.type;
		List<ISITag> tags = typeToTags.get(tagType);
		if (tags == null) {
			tags = new ArrayList<ISITag>();
		}
		tags.add(newTag);
		typeToTags.put(tagType, tags);
	}

	private static void addArbitraryTagInternal(ISITag newTag, boolean checkForDuplicates) {
		if (checkForDuplicates) {
			for (int ii = 0; ii < arbitraryISITags.size(); ii++) {	
				ISITag tag = arbitraryISITags.get(ii);
				
				if (newTag.tagName.equals(tag.tagName)) {
					System.err
							.println("Attempted to add an arbitrary tag tagNamed '"
									+ newTag.tagName
									+ "'"
									+ " when one with the same tagName is already known.");
					System.err.println("Ignoring attempt to add new tag");

					return;
				}
			}
		}
		
		//add to isiTags
		
		arbitraryISITags.add(newTag);
		//add to tagNameToTag
		
		arbitraryTagNameToTag.put(newTag.tagName, newTag);
		
		//add to tagNameToColumnName
		
		arbitraryTagNameToColumnName.put(newTag.tagName, newTag.columnName);
		
		//add to typeToTags
		ContentType tagType = newTag.type;
		List<ISITag> tags = arbitraryTypeToTags.get(tagType);

		if (tags == null) {
			tags = new ArrayList<ISITag>();
		}
		
		tags.add(newTag);
		arbitraryTypeToTags.put(tagType, tags);
	}

	public final String tagName;
	public final String columnName;
	public final ContentType type;
	
	public final String separator;
	public final boolean isFileUnique;

	
	private ISITag(String tagName, String name, ContentType type) {
		this(tagName, name, type, null, false);
	}
	
	private ISITag(String tagName, String name, ContentType type, boolean isFileUnique) {
		this(tagName, name, type, null, isFileUnique);
	}
	
	private ISITag(String tagName, String name, ContentType type, String separator) {
		this(tagName, name, type, separator, false);
	}

	private ISITag(String tagName, String name, ContentType type,
			String separator, boolean isFileUnique) {
		this.tagName = tagName;
		this.columnName = name;
		this.type = type;
		this.separator = separator;
		this.isFileUnique = isFileUnique;
	}
	
	public String getColumnName() {
		return this.columnName;
	}

	@Override
	public String toString() {
		return this.tagName;
	}
	
	public static void addTag(String tagName, String name, ContentType type) {
		ISITag newTag = new ISITag(tagName, name, type);
		addTagInternal(newTag, true);
	}
	
	public static void addTag(String tagName, String name, ContentType type, boolean isFileUnique) {
		ISITag newTag = new ISITag(tagName, name, type, isFileUnique);
		addTagInternal(newTag, true);
	}
	
	public static void addTag(String tagName, String name, ContentType type, String separator) {
		ISITag newTag = new ISITag(tagName, name, type, separator);
		addTagInternal(newTag, true);
	}

	public static void addTag(String tagName, String name, ContentType type,
			String separator, boolean isFileUnique) {
		ISITag newTag = new ISITag(tagName, name, type, separator, isFileUnique);
		addTagInternal(newTag, true);
	}

	public static void addArbitraryTag(String tagName, String name, ContentType type) {
		ISITag newTag = new ISITag(tagName, name, type);
		addTagInternal(newTag, true);
		addArbitraryTagInternal(newTag, true);
	}
	
	public static ISITag getTag(String tagName) {
		if (tagName == null) {
			return null;
		}
		
		return tagNameToTag.get(tagName);
	}

	public static ISITag getArbitraryTag(String tagName) {
		if (tagName == null) {
			return null;
		}
		
		return arbitraryTagNameToTag.get(tagName);
	}
	
	public static ISITag[] getTagsAlphabetically() {
		Collections.sort(isiTags, ISITagAlphabeticalOrdering);
		return isiTags.toArray(new ISITag[0]);
	}
	
	public static ISITag[] getTagsWithContentType(ContentType tagType) {
		if (tagType == null) {
			return new ISITag[0];
		}
		
		List<ISITag> tags = typeToTags.get(tagType);
		if (tags == null) {
			return new ISITag[0];
		}
		return tags.toArray(new ISITag[0]);
	}
	
	public static String getColumnName(String tagName) {
		String columnName = tagNameToColumnName.get(tagName);
		if (columnName == null) {
			throw new AssertionError("The column name returned 'null' which should be impossible for tags already added.");
		}
		return columnName;
	}
	
	/**
	 * This is an Alphabetical Ordering based on the column name (ignoring the
	 * case) where nulls will come last.
	 */
	public static Ordering<ISITag> ISITagAlphabeticalOrdering = new Ordering<ISITag>() {

		@Override
		public int compare(ISITag left, ISITag right) {
			return left.getColumnName().compareToIgnoreCase(
					right.getColumnName());
		}
	}.nullsLast();
}
