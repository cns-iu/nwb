package edu.iu.scipolicy.loader.isi.db.utilities.extractor;

import java.util.ArrayList;
import java.util.List;

import org.cishell.utilities.StringUtilities;

import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.util.collections.IntIterator;
import edu.iu.cns.database.loader.framework.DatabaseTableType;
import edu.iu.nwb.shared.isiutil.ISITableReader;
import edu.iu.nwb.shared.isiutil.ISITag;
import edu.iu.scipolicy.loader.isi.db.ISIDatabase;
import edu.iu.scipolicy.loader.isi.db.model.ISIModel;
import edu.iu.scipolicy.loader.isi.db.model.entity.Address;
import edu.iu.scipolicy.loader.isi.db.model.entity.Document;
import edu.iu.scipolicy.loader.isi.db.model.entity.ISIFile;
import edu.iu.scipolicy.loader.isi.db.model.entity.Keyword;
import edu.iu.scipolicy.loader.isi.db.model.entity.Patent;
import edu.iu.scipolicy.loader.isi.db.model.entity.Person;
import edu.iu.scipolicy.loader.isi.db.model.entity.Publisher;
import edu.iu.scipolicy.loader.isi.db.model.entity.Reference;
import edu.iu.scipolicy.loader.isi.db.model.entity.Source;
import edu.iu.scipolicy.loader.isi.db.model.entity.relationship.Author;
import edu.iu.scipolicy.loader.isi.db.model.entity.relationship.CitedPatent;
import edu.iu.scipolicy.loader.isi.db.model.entity.relationship.CitedReference;
import edu.iu.scipolicy.loader.isi.db.model.entity.relationship.DocumentKeyword;
import edu.iu.scipolicy.loader.isi.db.model.entity.relationship.DocumentOccurrence;
import edu.iu.scipolicy.loader.isi.db.model.entity.relationship.Editor;
import edu.iu.scipolicy.loader.isi.db.model.entity.relationship.PublisherAddress;
import edu.iu.scipolicy.loader.isi.db.model.entity.relationship.ReprintAddress;
import edu.iu.scipolicy.loader.isi.db.model.entity.relationship.ResearchAddress;

public class ISITableModelExtractor {
	public static final int AUTHOR_FIELD_TOKEN_INDEX = 0;

	/*
	 * For each type of entity (ISI File, Publisher, Source, Reference, Address, Keyword,
	 *  Person, Patent, and Document), create a master list of entities.
	 */

	private DatabaseTableType<ISIFile> isiFiles = new DatabaseTableType<ISIFile>(
		ISIDatabase.ISI_FILE_TYPE_NAME, ISIDatabase.ISI_FILE_TABLE_NAME);
	private DatabaseTableType<Publisher> publishers = new DatabaseTableType<Publisher>(
		ISIDatabase.PUBLISHER_TYPE_NAME, ISIDatabase.PUBLISHER_TABLE_NAME);
	private DatabaseTableType<Source> sources = new DatabaseTableType<Source>(
		ISIDatabase.SOURCE_TYPE_NAME, ISIDatabase.SOURCE_TABLE_NAME);
	private DatabaseTableType<Reference> references = new DatabaseTableType<Reference>(
		ISIDatabase.REFERENCE_TYPE_NAME, ISIDatabase.REFERENCE_TABLE_NAME);
	private DatabaseTableType<Address> addresses = new DatabaseTableType<Address>(
		ISIDatabase.ADDRESS_TYPE_NAME, ISIDatabase.ADDRESS_TABLE_NAME);
	private DatabaseTableType<Keyword> keywords = new DatabaseTableType<Keyword>(
		ISIDatabase.KEYWORD_TYPE_NAME, ISIDatabase.KEYWORD_TABLE_NAME);
	private DatabaseTableType<Person> people = new DatabaseTableType<Person>(
		ISIDatabase.PERSON_TYPE_NAME, ISIDatabase.PERSON_TABLE_NAME);
	private DatabaseTableType<Patent> patents = new DatabaseTableType<Patent>(
		ISIDatabase.PATENT_TYPE_NAME, ISIDatabase.PATENT_TABLE_NAME);
	private DatabaseTableType<Document> documents = new DatabaseTableType<Document>(
		ISIDatabase.DOCUMENT_TYPE_NAME, ISIDatabase.DOCUMENT_TABLE_NAME);

	/*
	 * Create all of the entity joining tables (Publisher Addresses, Reprint Addresses,
	 *  Research Addresses, Document Keywords, Authors, Editors, Cited Patents, Document
	 *  Occurrences, and Cited References).
	 */

	DatabaseTableType<PublisherAddress> publisherAddresses =
		new DatabaseTableType<PublisherAddress>(
			ISIDatabase.PUBLISHER_ADDRESSES_TYPE_NAME,
			ISIDatabase.PUBLISHER_ADDRESSES_TABLE_NAME);

	DatabaseTableType<ReprintAddress> reprintAddresses =
		new DatabaseTableType<ReprintAddress>(
			ISIDatabase.REPRINT_ADDRESSES_TYPE_NAME,
			ISIDatabase.REPRINT_ADDRESSES_TABLE_NAME);

	DatabaseTableType<ResearchAddress> researchAddresses =
		new DatabaseTableType<ResearchAddress>(
			ISIDatabase.RESEARCH_ADDRESSES_TYPE_NAME,
			ISIDatabase.RESEARCH_ADDRESSES_TABLE_NAME);

	DatabaseTableType<DocumentKeyword> documentKeywords =
		new DatabaseTableType<DocumentKeyword>(
			ISIDatabase.DOCUMENT_KEYWORDS_TYPE_NAME,
			ISIDatabase.DOCUMENT_KEYWORDS_TABLE_NAME);

	DatabaseTableType<Author> authors =
		new DatabaseTableType<Author>(
			ISIDatabase.AUTHORS_TYPE_NAME,
			ISIDatabase.AUTHORS_TABLE_NAME);

	DatabaseTableType<Editor> editors =
		new DatabaseTableType<Editor>(
			ISIDatabase.EDITORS_TYPE_NAME,
			ISIDatabase.EDITORS_TABLE_NAME);

	DatabaseTableType<CitedPatent> citedPatents =
		new DatabaseTableType<CitedPatent>(
			ISIDatabase.CITED_PATENTS_TYPE_NAME,
			ISIDatabase.CITED_PATENTS_TABLE_NAME);

	DatabaseTableType<DocumentOccurrence> documentOccurrences =
		new DatabaseTableType<DocumentOccurrence>(
			ISIDatabase.DOCUMENT_OCCURRENCES_TYPE_NAME,
			ISIDatabase.DOCUMENT_OCCURRENCES_TABLE_NAME);

	DatabaseTableType<CitedReference> citedReferences =
		new DatabaseTableType<CitedReference>(
			ISIDatabase.CITED_REFERENCES_TYPE_NAME,
			ISIDatabase.CITED_REFERENCES_TABLE_NAME);

	/**
	 * This is an instance method instead of a static method so all of the tables can be instance
	 *  variables and thus don't clutter up this method.
	 */
	public ISIModel extractModel(Table table) {
		// For each record/row in the table:

		for (IntIterator rows = table.rows(); rows.hasNext(); ) {
			Tuple row = table.getTuple(rows.nextInt());

			/*
			 * Extract all entities and relationships out of the row.
			 * Extract ISI File.
			 * Extract Publisher.
			 * Extract Source.
			 * Update reference from Publisher to Source.
			 * Extract Reference.
			 * Update references between Source and Reference.
			 * Extract Document.
			 * Extract Document First Author (Person).
			 * Update references between Source and Document; Document and Reference; Document and
			 *  First Author (Person); Reference and First Author (Person).
			 * Extract Publisher Addresses, and add relationships to publisherAddresses.
			 * Extract Reprint Addresses, and add relationships to reprintAddresses.
			 * Extract Research Addresses, and add relationships to researchAddresses.
			 * Extract Authors, and add relationships to authors.
			 * Extract Editors, and add relationships to editors.
			 * Extract Document Occurrences, and add relationships to documentOccurrences.
			 * Extract Cited Patents, and add relationships to citedPatents.
			 * Extract Cited References, and add relationships to citedReferences.
			 */

			ISIFile isiFile = extractISIFile(row);
			Publisher publisher = extractPublisher(row);
			Source source = extractSource(row);
			publisher.setSource(source);
			List<Reference> currentReferences = extractReferences(row);
			//System.err.println("source: " + source);

			// For each of the entities just extracted:
				// Check if it is a duplicate.
					/*
					 * If it is, it will be a duplicate with at most one other entity.
					 * Update any references to it to refer to the entity it is duplicate with.
					 * Remove it.
					 */
		}

		// Given all of the master lists of entities, construct an ISIModel and return it.

		// Create new ISIModel, passing in all entity tables and relationship tables.

		return null;
	}

	private ISIFile extractISIFile(Tuple row) {
		String fileName =
			StringUtilities.simpleClean(row.getString(ISITableReader.FILE_PATH_COLUMN_NAME));
		String fileType =
			StringUtilities.simpleClean(row.getString(ISITag.FILE_TYPE.getColumnName()));
		String versionNumber =
			StringUtilities.simpleClean(row.getString(ISITag.VERSION_NUMBER.getColumnName()));
		ISIFile isiFile =
			new ISIFile(this.isiFiles.getKeyGenerator(), fileName, fileType, versionNumber);

		return this.isiFiles.addOrMerge(isiFile);
	}

	private Publisher extractPublisher(Tuple row) {
		String name =
			StringUtilities.simpleClean(row.getString(ISITag.PUBLISHER.getColumnName()));
		String city =
			StringUtilities.simpleClean(row.getString(ISITag.PUBLISHER_CITY.getColumnName()));
		String webAddress = StringUtilities.simpleClean(
			row.getString(ISITag.PUBLISHER_WEB_ADDRESS.getColumnName()));
		Publisher publisher =
			new Publisher(this.publishers.getKeyGenerator(), name, city, webAddress);

		return this.publishers.addOrMerge(publisher);
	}

	private Source extractSource(Tuple row) {
		String fullTitle =
			StringUtilities.simpleClean(row.getString(ISITag.FULL_JOURNAL_TITLE.getColumnName()));
		String publicationType =
			StringUtilities.simpleClean(row.getString(ISITag.PUBLICATION_TYPE.getColumnName()));
		String isoTitleAbbreviation = StringUtilities.simpleClean(
			row.getString(ISITag.ISO_JOURNAL_TITLE_ABBREVIATION.getColumnName()));
		String bookSeriesTitle =
			StringUtilities.simpleClean(row.getString(ISITag.BOOK_SERIES_TITLE.getColumnName()));
		String bookSeriesSubtitle =
			StringUtilities.simpleClean(row.getString(ISITag.BOOK_SERIES_SUBTITLE.getColumnName()));
		String issn =
			StringUtilities.simpleClean(row.getString(ISITag.ISSN.getColumnName()));
		String twentyNineCharacterSourceTitleAbbreviation = StringUtilities.simpleClean(
			row.getString(ISITag.TWENTY_NINE_CHAR_JOURNAL_ABBREVIATION.getColumnName()));
		// TODO: ?
		String conferenceTitle = "";
		String conferenceDate = "";
		String conferenceDonation = "";
		Source source = new Source(
			this.sources.getKeyGenerator(),
			fullTitle,
			publicationType,
			isoTitleAbbreviation,
			bookSeriesTitle,
			bookSeriesSubtitle,
			issn,
			twentyNineCharacterSourceTitleAbbreviation,
			conferenceTitle,
			conferenceDate,
			conferenceDonation);

		return this.sources.addOrMerge(source);
	}

	private List<Reference> extractReferences(Tuple row) {
		List<Reference> references = new ArrayList<Reference>();
		String rawReferencesString =
			StringUtilities.simpleClean(row.getString(ISITag.CITED_REFERENCES.getColumnName()));
		String[] referenceStrings = rawReferencesString.split("\\|");

		for (String referenceString : referenceStrings) {
			String[] referenceTokens = referenceString.split(",");

			if (StringUtilities.isEmptyOrWhiteSpace(referenceString)) {
				continue;
			}

			ReferenceDataExtractor referenceData = new ReferenceDataExtractor(
				this.people.getKeyGenerator(), this.sources.getKeyGenerator(), referenceString);

			/*Person author = referenceData.getAuthor();

			if (author != null) {
				System.err.println("Personal name: " + author.getPersonalName());
				System.err.println("\tAdditional name: " + author.getAdditionalName());
				System.err.println("\tFamily name: " + author.getFamilyName());
				System.err.println("\tFirst initial: " + author.getFirstInitial());
				System.err.println("\tMiddle initial: " + author.getMiddleInitial());
				System.err.println("\tUnsplit name: " + author.getUnsplitName());
				System.err.println("\tFull name: " + author.getFullName());
			}*/

			//String authorField = referenceTokens[AUTHOR_FIELD_TOKEN_INDEX];
			/*Person author = extractAuthorFromReference(referenceTokens);
			Source source = extractSourceFromReference(referenceTokens);
			int year = extractYearFromReference(referenceTokens);
			String volume = extractVolumeFromReference(referenceTokens);*/
		}

		return references;
	}
}