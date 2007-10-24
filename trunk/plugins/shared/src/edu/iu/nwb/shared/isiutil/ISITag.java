package edu.iu.nwb.shared.isiutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

public class ISITag {
	
	
	
	//IMPORTANT: If you add a tag here, make sure to add it in the isiTagArray below as well.
	
	public static final ISITag FILE_TYPE                          = new ISITag("FN", ContentType.TEXT, true);
	public static final ISITag VERSION_NUMBER                     = new ISITag("VR", ContentType.TEXT, true);
	public static final ISITag END_OF_FILE                        = new ISITag("EF", ContentType.NULL  , true);
	public static final ISITag ABSTRACT                           = new ISITag("AB", ContentType.TEXT);
	public static final ISITag ARTICLE_NUMBER_OF_NEW_APS_JOURNALS = new ISITag("AR", ContentType.TEXT);
	public static final ISITag AUTHORS                            = new ISITag("AU", ContentType.MULTI_VALUE_TEXT, "\n");
	public static final ISITag BEGINNING_PAGE                     = new ISITag("BP", ContentType.TEXT);
	public static final ISITag RESEARCH_ADDRESSES                 = new ISITag("C1", ContentType.MULTI_VALUE_TEXT, "\n");
	public static final ISITag CITED_REFERENCES                   = new ISITag("CR", ContentType.MULTI_VALUE_TEXT, "\n");
	public static final ISITag ORIGINAL_KEYWORDS                  = new ISITag("DE", ContentType.MULTI_VALUE_TEXT,  ";");
	public static final ISITag DOI                                = new ISITag("DI", ContentType.TEXT);
	public static final ISITag DOCUMENT_TYPE                      = new ISITag("DT", ContentType.TEXT);
	public static final ISITag ENDING_PAGE                        = new ISITag("EP", ContentType.TEXT);
	public static final ISITag END_OF_RECORD                      = new ISITag("ER", ContentType.NULL);
	public static final ISITag ISI_DOCUMENT_DELIVERY_NUMBER       = new ISITag("GA", ContentType.TEXT);
	public static final ISITag NEW_KEYWORDS_GIVEN_BY_ISI          = new ISITag("ID", ContentType.MULTI_VALUE_TEXT,  ";");
	public static final ISITag ISSUE                              = new ISITag("IS", ContentType.TEXT);
	public static final ISITag TWENTY_NINE_CHAR_JOURNAL_ABBREV    = new ISITag("J9", ContentType.TEXT);
	public static final ISITag ISO_JOURNAL_TITLE_ABBREV           = new ISITag("JI", ContentType.TEXT);
	public static final ISITag LANGUAGE                           = new ISITag("LA", ContentType.TEXT);
	public static final ISITag CITED_REFERENCE_COUNT              = new ISITag("NR", ContentType.INTEGER);
	public static final ISITag PUBLICATION_DATE                   = new ISITag("PD", ContentType.TEXT);
	public static final ISITag NUMBER_OF_PAGES                    = new ISITag("PG", ContentType.INTEGER);
	public static final ISITag PUBLISHER_CITY                     = new ISITag("PI", ContentType.TEXT);
	public static final ISITag PART_NUMBER                        = new ISITag("PN", ContentType.TEXT);
	public static final ISITag PUBLICATION_TYPE                   = new ISITag("PT", ContentType.TEXT);
	public static final ISITag PUBLISHER                          = new ISITag("PU", ContentType.TEXT);
	public static final ISITag PUBLICATION_YEAR                   = new ISITag("PY", ContentType.INTEGER);
	public static final ISITag REPRINT_ADDRESS                    = new ISITag("RP", ContentType.TEXT);
	public static final ISITag BOOK_SERIES_TITLE                  = new ISITag("SE", ContentType.TEXT);
	public static final ISITag SPECIAL_ISSUE                      = new ISITag("SI", ContentType.TEXT);
	public static final ISITag ISSN                               = new ISITag("SN", ContentType.TEXT);
	public static final ISITag FULL_JOURNAL_TITLE                 = new ISITag("SO", ContentType.TEXT);
	public static final ISITag SUPPLEMENT                         = new ISITag("SU", ContentType.TEXT);
	public static final ISITag TIMES_CITED                        = new ISITag("TC", ContentType.INTEGER);
	public static final ISITag TITLE                              = new ISITag("TI", ContentType.TEXT);
	public static final ISITag UNIQUE_ID                          = new ISITag("UT", ContentType.TEXT);
	public static final ISITag VOLUME                             = new ISITag("VL", ContentType.TEXT);
	public static final ISITag PUBLISHER_WEB_ADDRESS              = new ISITag("WP", ContentType.TEXT);
		
	
	private static final ISITag[] isiTagArray = { FILE_TYPE, VERSION_NUMBER,
		END_OF_FILE, ABSTRACT, ARTICLE_NUMBER_OF_NEW_APS_JOURNALS, AUTHORS,
		BEGINNING_PAGE, RESEARCH_ADDRESSES, CITED_REFERENCES,
		ORIGINAL_KEYWORDS, DOI, DOCUMENT_TYPE, ENDING_PAGE, 
		END_OF_RECORD, ISI_DOCUMENT_DELIVERY_NUMBER, 
		NEW_KEYWORDS_GIVEN_BY_ISI, ISSUE, TWENTY_NINE_CHAR_JOURNAL_ABBREV,
		ISO_JOURNAL_TITLE_ABBREV, LANGUAGE, CITED_REFERENCE_COUNT,
		PUBLICATION_DATE, NUMBER_OF_PAGES, PUBLISHER_CITY, PART_NUMBER,
		PUBLICATION_TYPE, PUBLISHER, PUBLICATION_YEAR, REPRINT_ADDRESS,
		BOOK_SERIES_TITLE, SPECIAL_ISSUE, ISSN, FULL_JOURNAL_TITLE,
		SUPPLEMENT, TIMES_CITED, TITLE, UNIQUE_ID, VOLUME, 
		PUBLISHER_WEB_ADDRESS
	};
	
	private static List isiTags = new ArrayList();
	private static Dictionary nameToTag = new Hashtable();
	private static Dictionary typeToTags = new Hashtable();

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
				
				if (newTag.name.equals(tag.name)) {
					System.err.println("Attempted to add a tag named '" + newTag.name + "'" +
							" when one with the same name is already known.");
					System.err.println("Ignoring attempt to add new tag");
					return;
				}
			}
		}
		
		//add to isiTags
		
		isiTags.add(newTag);
		
		//add to nameToTag
		
		nameToTag.put(newTag.name, newTag);
		
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
	
	
	
	public final String name;
	public final ContentType type;
	
	public final String separator;
	public final boolean isFileUnique;

	
	private ISITag(String name, ContentType type) {
		this(name, type, null, false);
	}
	
	private ISITag(String name, ContentType type, boolean isFileUnique) {
		this(name, type, null, isFileUnique);
	}
	
	private ISITag(String name, ContentType type, String separator) {
		this(name, type, separator, false);
	}
	
	private ISITag(String name, ContentType type, String separator, boolean isFileUnique) {
		this.name = name;
		this.type = type;
		this.separator = separator;
		this.isFileUnique = isFileUnique;
	}

	public String toString() {
		return this.name;
	}
	
	public static void addTag(String name, ContentType type) {
		ISITag newTag = new ISITag(name, type);
		addTagInternal(newTag, true);
	}
	
	public static void addTag(String name, ContentType type, boolean isFileUnique) {
		ISITag newTag = new ISITag(name, type, isFileUnique);
		addTagInternal(newTag, true);
	}
	
	public static void addTag(String name, ContentType type, String separator) {
		ISITag newTag = new ISITag(name, type, separator);
		addTagInternal(newTag, true);
	}
	
	public static void addTag(String name, ContentType type, String separator, boolean isFileUnique) {
		ISITag newTag = new ISITag(name, type, separator, isFileUnique);
		addTagInternal(newTag, true);
	}
	
	public static ISITag getTag(String name) {
		if (name == null) {
			return null;
		}
		
		Object result = nameToTag.get(name);
		if (result != null) {
			return (ISITag) result;
		} else {
			return null;
		}
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
}
