package edu.iu.sci2.database.scopus.load;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;

import edu.iu.sci2.database.scholarly.FileField;

public enum ScopusField implements FileField {
	/** A multi-valued list of authors of the document */
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
	/** A multi-valued list of institutions with addresses */
	AFFILIATIONS("Affiliations"),
	ABSTRACT("Abstract"),
	/** A multi-valued list of keywords supplied by the author */
	AUTHOR_KEYWORDS("Author Keywords"),
	/** A multi-valued list of keywords used in the index */
	INDEX_KEYWORDS("Index Keywords"),
	MOLECULAR_SEQUENCE_NUMBERS("Molecular Sequence Numbers"),
	CHEMICALS("Chemicals/CAS"),
	TRADENAMES("Tradenames"),
	MANUFACTURERS("Manufacturers"),
	REFERENCES("References"),
	CORRESPONDENCE_ADDRESS("Correspondence Address"),
	/** A multi-valued list of editors of the document (seems to be usually empty) */
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
	
	private static final ImmutableMap<ScopusField,Character> SEPARATORS = ImmutableMap.of(
			AFFILIATIONS, ';',
			AUTHOR_KEYWORDS, ';',
			INDEX_KEYWORDS, ';',
			AUTHORS, '|',
			EDITORS, '|');
	
	private static final ImmutableMap<ScopusField,Splitter> SPLITTERS = ImmutableMap.copyOf(
			Maps.transformValues(SEPARATORS, new Function<Character, Splitter>() {
				@Override
				public Splitter apply(Character delimiter) {
					return Splitter.on(delimiter);
				}
			}));

	/**
	 * Given a ScopusField, returns the separator that divides multiple values within that field.
	 * 
	 * @param field the field in question
	 * @return a Character, which can be used to divide or join multiple values in that field
	 * @throws IllegalArgumentException if the field is not a multi-value field
	 */
	public static Character getSeparatorFor(ScopusField field) {
		Character separator = SEPARATORS.get(field);
		Preconditions.checkArgument(separator != null, "No separator available for ScopusField %s", field);
		return separator;
	}
	
	/**
	 * Given a ScopusField, returns a {@link Splitter} that can split multiple values 
	 * from that field.
	 * 
	 * @param field the field in question
	 * @return a Splitter, which can be used to divide multiple values in that field
	 * @throws IllegalArgumentException if the field is not a multi-value field
	 */
	public static Splitter getSplitterFor(ScopusField field) {
		Splitter splitter = SPLITTERS.get(field);
		Preconditions.checkArgument(splitter != null, "No splitter available for ScopusField %s", field);
		return splitter;
	}
	
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