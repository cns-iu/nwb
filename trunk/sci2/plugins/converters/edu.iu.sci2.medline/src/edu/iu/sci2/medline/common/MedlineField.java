package edu.iu.sci2.medline.common;

import com.google.common.base.Preconditions;

/**
 * <p>
 * This class is the representation of all the
 * {@code MEDLINE®/PubMed® Data Element (Field) Description}s.
 * </p>
 * 
 * @see <a href="http://www.nlm.nih.gov/bsd/mms/medlineelements.html">The
 *      Medline Elements Spec.</a>
 * 
 */
public enum MedlineField {
	ABSTRACT("Abstract", "AB", String.class, ""),
	COPYRIGHT_INFORMATION("Copyright Information", "CI", String.class, ""),
	AFFILIATION("Affiliation", "AD", String.class, ""),
	Investigator_Affiliation("Investigator Affiliation", "IRAD", String.class,
			""),
	ARTICLE_IDENTIFIER("Article Identifier", "AID", String.class, ""),
	AUTHOR("Author", "AU", String.class, ""),
	FULL_AUTHOR("Full Author", "FAU", String.class, ""),
	BOOK_TITLE("Book Title", "BTI", String.class, ""),
	COLLECTION_TITLE("Collection Title", "CTI", String.class, ""),
	COMMENT_ON(
			"Comment on",
			"CON",
			String.class,
			"cites the reference upon which the article comments; began use with journal issues published in 1989."),
	COMMENT_IN(
			"Comment in",
			"CIN",
			String.class,
			"cites the reference containing a commentary about the article (appears on citation for original article); began use with journal issues published in 1989."),
	ERRATUM_IN(
			"Erratum in",
			"EIN",
			String.class,
			"cites a published erratum to the article (appears on citation for original article); began use in 1987."),
	ERRATUM_FOR("Erratum for", "EFR", String.class,
			"cites the original article for which there is a published erratum."),
	CORRECTED_AND_REPUBLISHED_IN(
			"Corrected and Republished in",
			"CRI",
			String.class,
			"cites the final, correct version of a corrected and republished article (appears on citation for original article). Began use in 1987 as Republished in (RPI); renamed in 2006."),
	CORRECTED_AND_REPUBLISHED_FROM(
			"Corrected and Republished from",
			"CRF",
			String.class,
			"cites the original article subsequently corrected and republished. Began use in 1987 as Republished from (RPF); renamed in 2006."),
	PARTIAL_RETRACTION_IN(
			"Partial retraction in",
			"PRIN",
			String.class,
			"cites the reference containing a partial retraction of the article (appears on citation for original article); began use in 2007."),
	PARTIAL_RETRACTION_OF("Partial retraction of", "PROF", String.class,
			"	cites the article being partially retracted; began use in 2007."),
	REPUBLISHED_IN(
			"Republished in",
			"RPI",
			String.class,
			"cites the subsequent (and possibly abridged) version of a republished article (appears on citation for original article); began use in 2006."),
	REPUBLISHED_FROM("Republished from", "RPF", String.class,
			"cites the first, originally published article; began use in 2006."),
	RETRACTION_IN(
			"Retraction in",
			"RIN",
			String.class,
			"cites the retraction of the article (appears on citation for original article); began use in August 1984."),
	RETRACTION_OF("Retraction of", "ROF", String.class,
			"cites the article(s) being retracted; began use in August 1984."),
	UPDATE_IN(
			"Update in",
			"UIN",
			String.class,
			"cites an updated version of the article (appears on citation for original article); began limited use in 2001."),
	UPDATE_OF("Update of", "UOF", String.class,
			"cites the article being updated; limited use; began limited use in 2001."),
	SUMMARY_FOR_PATIENTS_IN(
			"Summary for patients in",
			"SPIN",
			String.class,
			"cites a patient summary article; began use in November 2001 (these records contain Publication Type, Patient Education Handout). See the article 'Patient Education Handouts in MEDLINE®/PubMed®' in the NLM Technical Bulletin at http://www.nlm.nih.gov/pubs/techbull/ma02/ma02_new_pt.html for more information."),
	ORIGINAL_REPORT_IN("Original report in", "ORI", String.class,
			"cites a scientific article associated with the patient summary."),
	CORPORATE_AUTHOR("Corporate Author", "CN", String.class, ""),
	CREATE_DATE("Create Date", "CRDT", String.class, ""),
	DATE_COMPLETED("Date Completed", "DCOM", String.class, ""),
	DATE_CREATED("Date Created", "DA", String.class, ""),
	DATE_LAST_REVISED("Date Last Revised", "LR", String.class, ""),
	DATE_OF_ELECTRONIC_PUBLICATION("Date of Electronic Publication", "DEP",
			String.class, ""),
	DATE_OF_PUBLICATION("Date of Publication", "DP", String.class, ""),
	EDITION("Edition", "EN", String.class, ""),
	EDITOR_NAME("Editor Name", "ED", String.class, ""),
	FULL_EDITOR_NAME("Full Editor Name", "FED", String.class, ""),
	ENTREZ_DATE("Entrez Date", "EDAT", String.class, ""),
	GENE_SYMBOL("Gene Symbol", "GS", String.class, ""),
	GENERAL_NOTE("General Note", "GN", String.class, ""),
	GRANT_NUMBER("Grant Number", "GR", String.class, ""),
	INVESTIGATOR_NAME("Investigator Name", "IR", String.class, ""),
	FULL_INVESTIGATOR_NAME("Full Investigator Name", "FIR", String.class, ""),
	ISBN("ISBN", "ISBN", String.class, ""),
	ISSN("ISSN", "IS", String.class, ""),
	ISSUE("Issue", "IP", String.class, ""),
	JOURNAL_TITLE_ABBREVIATION("Journal Title Abbreviation", "TA",
			String.class, ""),
	JOURNAL_TITLE("Journal Title", "JT", String.class, ""),
	LANGUAGE("Language", "LA", String.class, ""),
	LOCATION_IDENTIFIER("Location Identifier", "LID", String.class, ""),
	MANUSCRIPT_IDENTIFIER("Manuscript Identifier", "MID", String.class, ""),
	MESH_DATE("MeSH Date", "MHDA", String.class, ""),
	MESH_TERMS("MeSH Terms", "MH", String.class, ""),
	NLM_UNIQUE_ID("NLM Unique ID", "JID", String.class, ""),
	NUMBER_OF_REFERENCES("Number of References", "RF", String.class, ""),
	OTHER_ABSTRACT("Other Abstract", "OAB", String.class, ""),
	OTHER_COPYRIGHT_INFORMATION("Other Copyright Information", "OCI",
			String.class, ""),
	OTHER_ID("Other ID", "OID", String.class, ""),
	OTHER_TERM("Other Term", "OT", String.class, ""),
	OTHER_TERM_OWNER("Other Term Owner", "OTO", String.class, ""),
	OWNER("Owner", "OWN", String.class, ""),
	PAGINATION("Pagination", "PG", String.class, ""),
	PERSONAL_NAME_AS_SUBJECT("Personal Name as Subject", "PS", String.class, ""),
	FULL_PERSONAL_NAME_AS_SUBJECT("Full Personal Name as Subject", "FPS",
			String.class, ""),
	PLACE_OF_PUBLICATION("Place of Publication", "PL", String.class, ""),
	PUBLICATION_HISTORY_STATUS("Publication History Status", "PHST",
			String.class, ""),
	PUBLICATION_STATUS("Publication Status", "PST", String.class, ""),
	PUBLICATION_TYPE("Publication Type", "PT", String.class, ""),
	PUBLISHING_MODEL("Publishing Model", "PUBM", String.class, ""),
	PUBMED_CENTRAL_IDENTIFIER("PubMed Central Identifier", "PMC", String.class,
			""),
	PUBMED_CENTRAL_RELEASE("PubMed Central Release", "PMCR", String.class, ""),
	PUBMED_UNIQUE_IDENTIFIER("PubMed Unique Identifier", "PMID", String.class,
			""),
	REGISTRY_NUMBER_EC_NUMBER("Registry Number/EC Number", "RN", String.class,
			""),
	SUBSTANCE_NAME("Substance Name", "NM", String.class, ""),
	SECONDARY_SOURCE_ID("Secondary Source ID", "SI", String.class, ""),
	SOURCE("Source", "SO", String.class, ""),
	SPACE_FLIGHT_MISSION("Space Flight Mission", "SFM", String.class, ""),
	STATUS("Status", "STAT", String.class, ""),
	SUBSET("Subset", "SB", String.class, ""),
	TITLE("Title", "TI", String.class, ""),
	TRANSLITERATED_TITLE("Transliterated Title", "TT", String.class, ""),
	VOLUME("Volume", "VI", Integer.class, ""),
	VOLUME_TITLE("Volume Title", "VTI", String.class, "");

	/**
	 * The value that separates a tag from its value in a medline file.
	 */
	public static String MEDLINE_TAG_VALUE_SEPERATOR = "-";

	/**
	 * Medline uses multiple entries to denote multiple values. This will not
	 * work for a table, so this separator is used.
	 */
	public static String MEDLINE_MULTI_VALUE_SEPERATOR = "|";

	private String name;
	private String field;
	private Class<?> fieldType;
	private String description;

	private MedlineField(String name, String field, Class<?> fieldType,
			String description) {
		Preconditions.checkNotNull(name);
		Preconditions.checkNotNull(field);
		Preconditions.checkNotNull(fieldType);
		Preconditions.checkNotNull(description);

		this.name = name;
		this.field = field;
		this.fieldType = fieldType;
		this.description = description;
	}

	/**
	 * Get the human readable name from the {@link MedlineField}. Known as
	 * 'Field' by MEDLINE®/PubMed®.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Get the 'Field' or 'Tag' from the {@link MedlineField}. Known as
	 * 'Abbreviation' by MEDLINE®/PubMed®.
	 */
	public String getField() {
		return this.field;
	}

	/**
	 * Get the Java {@link Class} for the {@link MedlineField}.
	 */
	public Class<?> getFieldType() {
		return this.fieldType;
	}

	/**
	 * Return a human readable description for the {@link MedlineField}. It is
	 * often empty.
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * Determine if there is a {@link MedlineField} that is represented by the
	 * value of {@code field}.
	 * 
	 * @param field
	 *            The field value ('Abbreviation' in MEDLINE®/PubMed® parlance).
	 */
	public static boolean validMedlineField(String field) {
		for (MedlineField medlineField : MedlineField.values()) {
			if (medlineField.getField().equalsIgnoreCase(field)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Given a field it will return the corresponding {@link MedlineField} or
	 * {@code null} if one is not found.
	 */
	public static MedlineField getMedlineFieldFromField(String field) {
		// A map could be constructed at value creation time and implemented
		// here if this will be called often.
		for (MedlineField f : MedlineField.values()) {
			if (f.getField().equalsIgnoreCase(field)) {
				return f;
			}
		}

		return null;
	}

	/**
	 * Given a {@code name}, return the matching {@link MedlineField} or
	 * {@code null} if there is no matching field.
	 */
	public static MedlineField getMedlineFieldFromName(String name) {
		for (MedlineField f : MedlineField.values()) {
			if (f.getName().equalsIgnoreCase(name)) {
				return f;
			}
		}

		return null;
	}
}
