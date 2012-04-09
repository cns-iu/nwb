package edu.iu.sci2.database.scopus.load;

import edu.iu.sci2.database.scholarly.FileField;

public enum ScopusField implements FileField {
	AUTHORS("Authors"),
	TITLE("Title"),
	YEAR("Year"),
	SOURCE_TITLE("Source title"),
	VOLUME("Volume"),
	ISSUE("Issue"),
	ART_NO("Art. No."),
	PAGE_START("Page start"),
	PAGE_END("Page end"),
	PAGE_COUNT("Page count"),
	CITED_BY("Cited by"),
	LINK("Link"),
	AFFILIATIONS("Affiliations"),
	ABSTRACT("Abstract"),
	AUTHOR_KEYWORDS("Author Keywords"),
	INDEX_KEYWORDS("Index Keywords"),
	MOLECULAR_SEQUENCE_NUMBERS("Molecular Sequence Numbers"),
	CHEMICALS("Chemicals/CAS"),
	TRADENAMES("Tradenames"),
	MANUFACTURERS("Manufacturers"),
	REFERENCES("References"),
	CORRESPONDENCE_ADDRESS("Correspondence Address"),
	EDITORS("Editors"),
	SPONSORS("Sponsors"),
	PUBLISHER("Publisher"),
	CONFERENCE_NAME("Conference name"),
	CONFERENCE_DATE("Conference date"),
	CONFERENCE_LOCATION("Conference location"),
	CONFERENCE_CODE("Conference code"),
	ISSN("ISSN"),
	ISBN("ISBN"),
	CODEN("CODEN"),
	DOI("DOI"),
	LANGUAGE_OF_ORIGINAL_DOCUMENT("Language of Original Document"),
	ABBREVIATED_SOURCE_TITLE("Abbreviated Source Title"),
	DOCUMENT_TYPE("Document Type"),
	SOURCE("Source"),
	SELF_REFERENCE("Self Reference");
	
	private String fieldName;

	private ScopusField(String name) {
		this.fieldName = name;
	}
	
	@Override
	public String toString() {
		return this.fieldName;
	}
	
	/* (non-Javadoc)
	 * @see edu.iu.sci2.database.scopus.load.FileField#getName()
	 */
	@Override
	public String getName() {
		return this.fieldName;
	}
}