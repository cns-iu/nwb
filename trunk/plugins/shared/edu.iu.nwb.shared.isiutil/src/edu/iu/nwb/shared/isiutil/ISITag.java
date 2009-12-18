package edu.iu.nwb.shared.isiutil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class ISITag {
	
	
	
	//IMPORTANT: If you add a tag here, make sure to add it in the isiTagArray below as well.
	
	public static final ISITag FILE_TYPE = new ISITag("FN", "File Type", ContentType.TEXT, true);
	public static final ISITag VERSION_NUMBER =
		new ISITag("VR", "Version Number", ContentType.TEXT, true);
	public static final ISITag END_OF_FILE =
		new ISITag("EF", "End of File", ContentType.NULL, true);
	public static final ISITag ABSTRACT = new ISITag("AB", "Abstract", ContentType.TEXT);
	public static final ISITag ARTICLE_NUMBER_OF_NEW_APS_JOURNALS =
		new ISITag("AR", "New Article Number",  ContentType.TEXT);
	public static final ISITag AUTHORS_FULL_NAMES =
		new ISITag("AF", "Authors (Full Names)", ContentType.MULTI_VALUE_TEXT, "\n");
	public static final ISITag AUTHORS =
		new ISITag("AU", "Authors", ContentType.MULTI_VALUE_TEXT, "\n");
	public static final ISITag BEGINNING_PAGE =
		new ISITag("BP", "Beginning Page", ContentType.TEXT);
	public static final ISITag RESEARCH_ADDRESSES =
		new ISITag("C1", "Research Addresses", ContentType.MULTI_VALUE_TEXT, "\n");
	public static final ISITag CITED_PATENT = new ISITag("CP", "Cited Patent", ContentType.TEXT);
	public static final ISITag CITED_REFERENCES =
		new ISITag("CR", "Cited References", ContentType.MULTI_VALUE_TEXT, "\n");
	public static final ISITag ORIGINAL_KEYWORDS =
		new ISITag("DE", "Original Keywords", ContentType.MULTI_VALUE_TEXT,  ";");
	public static final ISITag DOI = new ISITag("DI", "DOI", ContentType.TEXT);
	public static final ISITag DOCUMENT_TYPE = new ISITag("DT", "Document Type", ContentType.TEXT);
	public static final ISITag EMAIL_PRIMARY_AUTHOR =
		new ISITag("EM", "E-mail Address", ContentType.TEXT);
	public static final ISITag ENDING_PAGE = new ISITag("EP", "Ending Page", ContentType.TEXT);
	public static final ISITag END_OF_RECORD =
		new ISITag("ER", "End of RecordtagName", ContentType.NULL);
	public static final ISITag ISI_DOCUMENT_DELIVERY_NUMBER =
		new ISITag("GA", "ISI Document Delivery Number", ContentType.TEXT);
	public static final ISITag NEW_KEYWORDS_GIVEN_BY_ISI =
		new ISITag("ID", "New ISI Keywords", ContentType.MULTI_VALUE_TEXT,  ";");
	public static final ISITag ISSUE = new ISITag("IS", "Issue", ContentType.TEXT);
	public static final ISITag TWENTY_NINE_CHAR_JOURNAL_ABBREVIATION =
		new ISITag("J9", "Journal Name (Abbreviated)", ContentType.TEXT);
	public static final ISITag ISO_JOURNAL_TITLE_ABBREVIATION =
		new ISITag("JI", "Journal Name (Abbreviated ISO)", ContentType.TEXT);
	public static final ISITag LANGUAGE = new ISITag("LA", "Language", ContentType.TEXT);
	public static final ISITag CITED_REFERENCE_COUNT =
		new ISITag("NR", "Cited Reference Count", ContentType.INTEGER);
	public static final ISITag PUBLICATION_DATE =
		new ISITag("PD", "Publication Date", ContentType.TEXT);
	public static final ISITag NUMBER_OF_PAGES =
		new ISITag("PG", "Number of Pages", ContentType.INTEGER);
	public static final ISITag PUBLISHER_CITY =
		new ISITag("PI", "City of Publisher", ContentType.TEXT);
	public static final ISITag PART_NUMBER = new ISITag("PN", "Part Number", ContentType.TEXT);
	public static final ISITag PUBLICATION_TYPE =
		new ISITag("PT", "Publication Type", ContentType.TEXT);
	public static final ISITag PUBLISHER = new ISITag("PU", "Publisher", ContentType.TEXT);
	public static final ISITag PUBLISHER_ADDRESS =
		new ISITag("PA", "Publisher Address", ContentType.TEXT);
	public static final ISITag PUBLICATION_YEAR =
		new ISITag("PY", "Publication Year", ContentType.INTEGER);
	public static final ISITag REPRINT_ADDRESS =
		new ISITag("RP", "Reprint Address", ContentType.TEXT);
	public static final ISITag BOOK_SERIES_TITLE =
		new ISITag("SE", "Book Series Title", ContentType.TEXT);
	public static final ISITag BOOK_SERIES_SUBTITLE =
		new ISITag("BS", "Book Series Subtitle", ContentType.TEXT);
	public static final ISITag SPECIAL_ISSUE =
		new ISITag("SI", "Special Issue",  ContentType.TEXT);
	public static final ISITag ISBN = new ISITag("BN", "ISBN", ContentType.TEXT);
	public static final ISITag ISSN = new ISITag("SN",  "ISSN", ContentType.TEXT);
	public static final ISITag FULL_JOURNAL_TITLE =
		new ISITag("SO", "Journal Title (Full)",ContentType.TEXT);
	public static final ISITag SUPPLEMENT = new ISITag("SU", "Supplement", ContentType.TEXT);
	public static final ISITag TIMES_CITED = new ISITag("TC", "Times Cited", ContentType.INTEGER);
	public static final ISITag TITLE = new ISITag("TI", "Title", ContentType.TEXT);
	public static final ISITag UNIQUE_ID = new ISITag("UT", "Unique ID", ContentType.TEXT);
	public static final ISITag VOLUME = new ISITag("VL", "Volume", ContentType.TEXT);
	public static final ISITag PUBLISHER_WEB_ADDRESS =
		new ISITag("WP", "Publisher Web Address", ContentType.TEXT);
		
	
	private static final ISITag[] isiTagArray = {
		FILE_TYPE,
		VERSION_NUMBER,
		END_OF_FILE,
		ABSTRACT,
		ARTICLE_NUMBER_OF_NEW_APS_JOURNALS,
		AUTHORS,
		BEGINNING_PAGE,
		RESEARCH_ADDRESSES,
		CITED_PATENT,
		CITED_REFERENCES,
		ORIGINAL_KEYWORDS,
		DOI,
		DOCUMENT_TYPE,
		ENDING_PAGE, 
		END_OF_RECORD,
		ISI_DOCUMENT_DELIVERY_NUMBER, 
		NEW_KEYWORDS_GIVEN_BY_ISI,
		ISSUE,
		TWENTY_NINE_CHAR_JOURNAL_ABBREVIATION,
		ISO_JOURNAL_TITLE_ABBREVIATION,
		LANGUAGE,
		CITED_REFERENCE_COUNT,
		PUBLICATION_DATE,
		NUMBER_OF_PAGES,
		PUBLISHER_CITY,
		PART_NUMBER,
		PUBLICATION_TYPE,
		PUBLISHER,
		PUBLISHER_ADDRESS,
		PUBLICATION_YEAR,
		REPRINT_ADDRESS,
		BOOK_SERIES_TITLE,
		BOOK_SERIES_SUBTITLE,
		SPECIAL_ISSUE,
		ISBN,
		ISSN,
		FULL_JOURNAL_TITLE,
		SUPPLEMENT,
		TIMES_CITED,
		TITLE,
		UNIQUE_ID,
		VOLUME, 
		PUBLISHER_WEB_ADDRESS,
		AUTHORS_FULL_NAMES,
		EMAIL_PRIMARY_AUTHOR
	};
	
	private static List isiTags = new ArrayList();
	private static Dictionary tagNameToTag = new Hashtable();
	private static Dictionary typeToTags = new Hashtable();
	private static Dictionary tagNameToColumnName = new Hashtable();

	static {
		for (int ii = 0; ii < isiTagArray.length; ii++) {
			ISITag tag = isiTagArray[ii];
			
			addTagInternal(tag, false);
		}
	}
	
	private static void addTagInternal(ISITag newTag, boolean checkForDuplicates) {
		if (checkForDuplicates) {
			for (int ii = 0; ii < isiTags.size(); ii++) {	
				ISITag tag = (ISITag) isiTags.get(ii);
				
				if (newTag.tagName.equals(tag.tagName)) {
					System.err.println("Attempted to add a tag tagNamed '" + newTag.tagName + "'" +
							" when one with the same tagName is already known.");
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
		Object typeToTagsResult = typeToTags.get(tagType);
		List tagsWithThisType;
		if (typeToTagsResult == null) {
			tagsWithThisType = new ArrayList();
		} else {
			tagsWithThisType = (List) typeToTagsResult;
		}
		
		tagsWithThisType.add(newTag);
		typeToTags.put(tagType, tagsWithThisType);
	}
	
	
	
	public final String tagName;
	public final String columnName;
	public final ContentType type;
	
	public final String separator;
	public final boolean isFileUnique;

	
	private ISITag(String tagName, String name,ContentType type) {
		this(tagName, name, type, null, false);
	}
	
	private ISITag(String tagName, String name, ContentType type, boolean isFileUnique) {
		this(tagName, name, type, null, isFileUnique);
	}
	
	private ISITag(String tagName, String name, ContentType type, String separator) {
		this(tagName, name, type, separator, false);
	}
	
	private ISITag(String tagName, String name, ContentType type, String separator, boolean isFileUnique) {
		this.tagName = tagName;
		this.columnName = name;
		this.type = type;
		this.separator = separator;
		this.isFileUnique = isFileUnique;
	}
	
	public String getColumnName() {
		return this.columnName;
	}

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
	
	public static void addTag(String tagName, String name, ContentType type, String separator, boolean isFileUnique) {
		ISITag newTag = new ISITag(tagName, name, type, separator, isFileUnique);
		addTagInternal(newTag, true);
	}
	
	public static ISITag getTag(String tagName) {
		if (tagName == null) {
			return null;
		}
		
		Object result = tagNameToTag.get(tagName);
		if (result != null) {
			return (ISITag) result;
		} else {
			return null;
		}
	}
	
	public static ISITag[] getTagsAlphabetically() {
		Collections.sort(isiTags, new ISITagAlphabeticalComparator());
		return (ISITag[]) isiTags.toArray(new ISITag[0]);
	}
	
	public static ISITag[] getTagsWithContentType(ContentType tagType) {
		if (tagType == null) {
			return new ISITag[0];
		}
		
		Object typeToTagsResult = typeToTags.get(tagType);
		if (typeToTagsResult != null) {
			List tags = (List) typeToTagsResult;
			
			return (ISITag[]) tags.toArray(new ISITag[0]);
		} else {
			return new ISITag[0];
		}
	}
	
	public static String getColumnName(String tagName) {
		String columnName = (String) tagNameToColumnName.get(tagName);
		if (columnName != null) {
			return columnName;
		} else {
			System.err.println("Unable to find column name for tag name '" + 
					tagName + "' in class ISITag, in package edu.iu.nwb.shared.isiutil");
			return "";
		}
	}
}
