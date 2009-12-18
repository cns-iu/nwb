package edu.iu.scipolicy.loader.isi.db.utilities.parser;

import java.util.ArrayList;
import java.util.List;

import org.cishell.utilities.StringUtilities;

import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.util.collections.IntIterator;
import edu.iu.cns.database.loader.framework.RowItemContainer;
import edu.iu.nwb.shared.isiutil.ISITableReader;
import edu.iu.nwb.shared.isiutil.ISITag;
import edu.iu.nwb.shared.isiutil.database.ISIDatabase;
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

public class ISITableModelParser {
	public static final String AUTHOR_KEYWORDS = "authorKeywords";
	public static final String KEYWORDS_PLUS = "keywordsPlus";

	/*
	 * For each type of entity (ISI File, Publisher, Source, Reference, Address, Keyword,
	 *  Person, Patent, and Document), create a master list of entities.
	 */

	private RowItemContainer<ISIFile> isiFiles = new RowItemContainer<ISIFile>(
		ISIDatabase.ISI_FILE_TYPE_NAME, ISIDatabase.ISI_FILE_TABLE_NAME);
	private RowItemContainer<Publisher> publishers = new RowItemContainer<Publisher>(
		ISIDatabase.PUBLISHER_TYPE_NAME, ISIDatabase.PUBLISHER_TABLE_NAME);
	private RowItemContainer<Source> sources = new RowItemContainer<Source>(
		ISIDatabase.SOURCE_TYPE_NAME, ISIDatabase.SOURCE_TABLE_NAME);
	private RowItemContainer<Reference> references = new RowItemContainer<Reference>(
		ISIDatabase.REFERENCE_TYPE_NAME, ISIDatabase.REFERENCE_TABLE_NAME);
	private RowItemContainer<Address> addresses = new RowItemContainer<Address>(
		ISIDatabase.ADDRESS_TYPE_NAME, ISIDatabase.ADDRESS_TABLE_NAME);
	private RowItemContainer<Keyword> keywords = new RowItemContainer<Keyword>(
		ISIDatabase.KEYWORD_TYPE_NAME, ISIDatabase.KEYWORD_TABLE_NAME);
	private RowItemContainer<Person> people = new RowItemContainer<Person>(
		ISIDatabase.PERSON_TYPE_NAME, ISIDatabase.PERSON_TABLE_NAME);
	private RowItemContainer<Patent> patents = new RowItemContainer<Patent>(
		ISIDatabase.PATENT_TYPE_NAME, ISIDatabase.PATENT_TABLE_NAME);
	private RowItemContainer<Document> documents = new RowItemContainer<Document>(
		ISIDatabase.DOCUMENT_TYPE_NAME, ISIDatabase.DOCUMENT_TABLE_NAME);

	/*
	 * Create all of the entity joining tables (Publisher Addresses, Reprint Addresses,
	 *  Research Addresses, Document Keywords, Authors, Editors, Cited Patents, Document
	 *  Occurrences, and Cited References).
	 */

	RowItemContainer<PublisherAddress> publisherAddresses =
		new RowItemContainer<PublisherAddress>(
			ISIDatabase.PUBLISHER_ADDRESSES_TYPE_NAME,
			ISIDatabase.PUBLISHER_ADDRESSES_TABLE_NAME);

	RowItemContainer<ReprintAddress> reprintAddresses =
		new RowItemContainer<ReprintAddress>(
			ISIDatabase.REPRINT_ADDRESSES_TYPE_NAME,
			ISIDatabase.REPRINT_ADDRESSES_TABLE_NAME);

	RowItemContainer<ResearchAddress> researchAddresses =
		new RowItemContainer<ResearchAddress>(
			ISIDatabase.RESEARCH_ADDRESSES_TYPE_NAME,
			ISIDatabase.RESEARCH_ADDRESSES_TABLE_NAME);

	RowItemContainer<DocumentKeyword> documentKeywords =
		new RowItemContainer<DocumentKeyword>(
			ISIDatabase.DOCUMENT_KEYWORDS_TYPE_NAME,
			ISIDatabase.DOCUMENT_KEYWORDS_TABLE_NAME);

	RowItemContainer<Author> authors =
		new RowItemContainer<Author>(
			ISIDatabase.AUTHORS_TYPE_NAME,
			ISIDatabase.AUTHORS_TABLE_NAME);

	RowItemContainer<Editor> editors =
		new RowItemContainer<Editor>(
			ISIDatabase.EDITORS_TYPE_NAME,
			ISIDatabase.EDITORS_TABLE_NAME);

	RowItemContainer<CitedPatent> citedPatents =
		new RowItemContainer<CitedPatent>(
			ISIDatabase.CITED_PATENTS_TYPE_NAME,
			ISIDatabase.CITED_PATENTS_TABLE_NAME);

	RowItemContainer<DocumentOccurrence> documentOccurrences =
		new RowItemContainer<DocumentOccurrence>(
			ISIDatabase.DOCUMENT_OCCURRENCES_TYPE_NAME,
			ISIDatabase.DOCUMENT_OCCURRENCES_TABLE_NAME);

	RowItemContainer<CitedReference> citedReferences =
		new RowItemContainer<CitedReference>(
			ISIDatabase.CITED_REFERENCES_TYPE_NAME,
			ISIDatabase.CITED_REFERENCES_TABLE_NAME);

	/*
	 * TODO: Write a short paragraph explaining the general technique we're using here 
	 * (do entities first, then relationship tables that take entities in their constructors...etc..?)
	 */
	
	/*
	 * TODO: Pattern: parse a set of columns, add that set of columns entities to their entity
	 *  lists, and return those entities for use in creating other row items (like relationship
	 *  tables which tie one entity to another).
	 */
	
	
	/**
	 * This is an instance method instead of a static method so all of the tables can be instance
	 *  variables and thus don't clutter up this method.
	 */
	// TODO: Rename this method. Also get rid of ISIModel.
	public ISIModel parseModel(Table table) {
		/*
		 * TODO: Edit template comments to describe process from start to finish, read
		 *  independently of code.
		 */
		// For each record/row in the table:

		for (IntIterator rows = table.rows(); rows.hasNext(); ) {
			Tuple row = table.getTuple(rows.nextInt());

			// Parse ISI File.

			ISIFile isiFile = parseISIFile(row);

			// Parse Publisher.

			Publisher publisher = parsePublisher(row);

			// Parse Source.

			Source source = parseSource(row);
			publisher.setSource(source);

			// Parse References.

			List<Reference> currentReferences = parseReferences(row);

			for (Reference reference : currentReferences) {
				Person referenceAuthor = reference.getAuthor();
				Source referenceSource = reference.getSource();

				if (referenceAuthor != null) {
					Person mergedAuthor = this.people.addOrMerge(referenceAuthor);
					reference.setAuthor(mergedAuthor);
				}

				if (referenceSource != null) {
					Source mergedSource = this.sources.addOrMerge(referenceSource);
					reference.setSource(mergedSource);
				}
			}

			// Parse (Author) People.

			List<Person> currentAuthorPeople = parseAuthorPeople(row);

			// Parse Document.

			Document document = null;

			if (currentAuthorPeople.size() != 0) {
				document = parseDocument(row, currentAuthorPeople.get(0));
			} else {
				document = parseDocument(row, null);
			}

			for (int ii = 0; ii < currentAuthorPeople.size(); ii++) {
				Person author = currentAuthorPeople.get(ii);
				this.authors.addOrMerge(new Author(document, author, ii));
			}

			// Parse Author Keywords.

			List<Keyword> authorKeywords =
				parseKeywords(row, AUTHOR_KEYWORDS, ISITag.ORIGINAL_KEYWORDS);

			for (int ii = 0; ii < authorKeywords.size(); ii++) {
				this.documentKeywords.addOrMerge(
					new DocumentKeyword(document, authorKeywords.get(ii), ii));
			}

			// Parse KeyWords Plus.

			List<Keyword> keywordsPlus =
				parseKeywords(row, KEYWORDS_PLUS, ISITag.NEW_KEYWORDS_GIVEN_BY_ISI);

			for (int ii = 0; ii < keywordsPlus.size(); ii++) {
				this.documentKeywords.addOrMerge(
					new DocumentKeyword(document, keywordsPlus.get(ii), ii));
			}

			// Parse Patent.

			Patent patent = parsePatent(row);

			// Parse Publisher Address.

			Address addressOfPublisher = parseAddressOfPublisher(row);
			this.publisherAddresses.addOrMerge(
				new PublisherAddress(publisher, addressOfPublisher));

			// Parse Reprint Address.

			Address addressForReprinting = parseAddressForReprinting(row);
			this.reprintAddresses.addOrMerge(new ReprintAddress(document, addressForReprinting));

			// Parse Research Address.

			List<Address> currentAddressesOfResearch = parseAddressesOfResearch(row);

			for (int ii = 0; ii < currentAddressesOfResearch.size(); ii++) {
				Address addressOfResearch = currentAddressesOfResearch.get(ii);
				this.researchAddresses.addOrMerge(
					new ResearchAddress(document, addressOfResearch, ii));
			}

			// Parse Editors. (?)
			//TODO: Look into what's up with editors.

			// Link up Document Occurrence.

			this.documentOccurrences.addOrMerge(new DocumentOccurrence(document, isiFile));

			// P
		}

		// Given all of the master lists of entities, construct an ISIModel and return it.
		//TODO: ?
		// Create new ISIModel, passing in all entity tables and relationship tables.
		//TODO: ?
		return null;
	}

	private ISIFile parseISIFile(Tuple row) {
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

	private Publisher parsePublisher(Tuple row) {
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

	private Source parseSource(Tuple row) {
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

	private List<Reference> parseReferences(Tuple row) {
		List<Reference> references = new ArrayList<Reference>();
		String rawReferencesString =
			StringUtilities.simpleClean(row.getString(ISITag.CITED_REFERENCES.getColumnName()));
		String[] referenceStrings = rawReferencesString.split("\\|");

		for (String referenceString : referenceStrings) {
			if (StringUtilities.isEmptyOrWhiteSpace(referenceString)) {
				continue;
			}

			ReferenceDataParser referenceData = new ReferenceDataParser(
				this.people.getKeyGenerator(), this.sources.getKeyGenerator(), referenceString);

			Reference reference = new Reference(
				this.references.getKeyGenerator(),
				referenceData.getRawString(),
				null,
				referenceData.getAuthor(),
				referenceData.getSource(),
				referenceData.getYear(),
				referenceData.getVolume(),
				referenceData.getPageNumber(),
				referenceData.getAnnotation(),
				referenceData.authorWasStarred());
			Reference mergedReference = this.references.addOrMerge(reference);
			references.add(mergedReference);
		}

		return references;
	}

	/*
	 * TODO: Lol authorPeople (no change required). Remind me to give you my copy of
	 *  'Wizard People, Dear Reader' the Harry Potter movie.
	 */
	private List<Person> parseAuthorPeople(Tuple row) {
		List<Person> authors = new ArrayList<Person>();

		String rawAuthorsString =
			StringUtilities.simpleClean(row.getString(ISITag.AUTHORS.getColumnName()));
		String[] authorStrings = rawAuthorsString.split("\\|");

		for (String authorString : authorStrings) {
			String cleanedAuthorString = StringUtilities.simpleClean(authorString);
			Person authorPerson = PersonParser.parsePerson(
				this.authors.getKeyGenerator(), cleanedAuthorString).getFirstObject();
			Person mergedAuthorPerson = this.people.addOrMerge(authorPerson);
			authors.add(mergedAuthorPerson);
		}

		return authors;
	}

	private Document parseDocument(Tuple row, Person firstAuthor) {
		String digitalObjectidentifier =
			StringUtilities.simpleClean(row.getString(ISITag.DOI.getColumnName()));
		String title = StringUtilities.simpleClean(row.getString(ISITag.TITLE.getColumnName()));
		String articleNumber =
			StringUtilities.simpleClean(
				row.getString(ISITag.ARTICLE_NUMBER_OF_NEW_APS_JOURNALS.getColumnName()));
		String language = StringUtilities.simpleClean(row.getString(ISITag.TITLE.getColumnName()));
		String documentType =
			StringUtilities.simpleClean(row.getString(ISITag.DOCUMENT_TYPE.getColumnName()));
		int citedReferenceCount = IntegerParserWithDefault.parse(StringUtilities.simpleClean(
			row.getString(ISITag.CITED_REFERENCE_COUNT.getColumnName())));
		String documentAbstract =
			StringUtilities.simpleClean(row.getString(ISITag.ABSTRACT.getColumnName()));
		int timesCited = IntegerParserWithDefault.parse(
			StringUtilities.simpleClean(row.getString(ISITag.TIMES_CITED.getColumnName())));
		int beginningPage = IntegerParserWithDefault.parse(
			StringUtilities.simpleClean(row.getString(ISITag.BEGINNING_PAGE.getColumnName())));
		int endingPage = IntegerParserWithDefault.parse(
			StringUtilities.simpleClean(row.getString(ISITag.ENDING_PAGE.getColumnName())));
		int pageCount = IntegerParserWithDefault.parse(
			StringUtilities.simpleClean(row.getString(ISITag.NUMBER_OF_PAGES.getColumnName())));
		String isiUniqueArticleIdentifier =
			StringUtilities.simpleClean(row.getString(ISITag.UNIQUE_ID.getColumnName()));
		int publicationYear = IntegerParserWithDefault.parse(
			StringUtilities.simpleClean(row.getString(ISITag.PUBLICATION_YEAR.getColumnName())));
		String publicationDate =
			StringUtilities.simpleClean(row.getString(ISITag.PUBLICATION_DATE.getColumnName()));
		String volume = StringUtilities.simpleClean(row.getString(ISITag.VOLUME.getColumnName()));
		String issue = StringUtilities.simpleClean(row.getString(ISITag.ISSUE.getColumnName()));
		String partNumber =
			StringUtilities.simpleClean(row.getString(ISITag.PART_NUMBER.getColumnName()));
		String supplement =
			StringUtilities.simpleClean(row.getString(ISITag.SUPPLEMENT.getColumnName()));
		String specialIssue =
			StringUtilities.simpleClean(row.getString(ISITag.SPECIAL_ISSUE.getColumnName()));
		String isiDocumentDeliveryNumber = StringUtilities.simpleClean(
			row.getString(ISITag.ISI_DOCUMENT_DELIVERY_NUMBER.getColumnName()));
		String isbn = StringUtilities.simpleClean(row.getString(ISITag.ISBN.getColumnName()));
		String emailAddress = StringUtilities.simpleClean(
			row.getString(ISITag.EMAIL_PRIMARY_AUTHOR.getColumnName()));

		return this.documents.addOrMerge(new Document(
			this.documents.getKeyGenerator(),
			digitalObjectidentifier,
			title,
			articleNumber,
			firstAuthor,
			language,
			documentType,
			citedReferenceCount,
			documentAbstract,
			timesCited,
			beginningPage,
			endingPage,
			pageCount,
			isiUniqueArticleIdentifier,
			publicationYear,
			publicationDate,
			volume,
			issue,
			partNumber,
			supplement,
			specialIssue,
			isiDocumentDeliveryNumber,
			isbn,
			emailAddress));
	}

	private List<Keyword> parseKeywords(
			Tuple row, String keywordType, ISITag sourceTag) {
		List<Keyword> keywords = new ArrayList<Keyword>();

		String rawKeywordsString =
			StringUtilities.simpleClean(row.getString(sourceTag.getColumnName()));
		String[] keywordStrings = rawKeywordsString.split("\\|");

		for (String keywordString : keywordStrings) {
			Keyword keyword = this.keywords.addOrMerge(
				new Keyword(this.keywords.getKeyGenerator(), keywordType, keywordString));
			keywords.add(keyword);
		}

		return keywords;
	}

	private Patent parsePatent(Tuple row) {
		String patentNumber =
			StringUtilities.simpleClean(row.getString(ISITag.CITED_PATENT.getColumnName()));

		return this.patents.addOrMerge(new Patent(this.patents.getKeyGenerator(), patentNumber));
	}

	private Address parseAddressOfPublisher(Tuple row) {
		String addressOfPublisherString =
			StringUtilities.simpleClean(row.getString(ISITag.PUBLISHER_ADDRESS.getColumnName()));

		return this.addresses.addOrMerge(
			new Address(this.addresses.getKeyGenerator(), addressOfPublisherString));
	}

	private Address parseAddressForReprinting(Tuple row) {
		String addressForReprintingString =
			StringUtilities.simpleClean(row.getString(ISITag.REPRINT_ADDRESS.getColumnName()));

		return this.addresses.addOrMerge(
			new Address(this.addresses.getKeyGenerator(), addressForReprintingString));
	}

	private List<Address> parseAddressesOfResearch(Tuple row) {
		List<Address> addressesOfResearch = new ArrayList<Address>();

		String rawAddressesString =
			StringUtilities.simpleClean(row.getString(ISITag.RESEARCH_ADDRESSES.getColumnName()));
		String addressStrings[] = rawAddressesString.split("\\|");

		for (String addressString : addressStrings) {
			String cleanedAddressString = StringUtilities.simpleClean(addressString);
			// TODO: AddressParser?
			Address mergedAddressOfResearch = this.addresses.addOrMerge(
				new Address(this.addresses.getKeyGenerator(), cleanedAddressString));
			addressesOfResearch.add(mergedAddressOfResearch);
		}

		return addressesOfResearch;
	}
}