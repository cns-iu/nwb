package edu.iu.scipolicy.loader.isi.db.utilities.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.cishell.utilities.StringUtilities;

import prefuse.data.Table;
import prefuse.data.Tuple;
import edu.iu.cns.database.loader.framework.RowItemContainer;
import edu.iu.cns.shared.utilities.Pair;
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
		ISIDatabase.ISI_FILE_DISPLAY_NAME, ISIDatabase.ISI_FILE_TABLE_NAME, ISIFile.SCHEMA);
	private RowItemContainer<Publisher> publishers = new RowItemContainer<Publisher>(
		ISIDatabase.PUBLISHER_DISPLAY_NAME, ISIDatabase.PUBLISHER_TABLE_NAME, Publisher.SCHEMA);
	private RowItemContainer<Source> sources = new RowItemContainer<Source>(
		ISIDatabase.SOURCE_DISPLAY_NAME, ISIDatabase.SOURCE_TABLE_NAME, Source.SCHEMA);
	private RowItemContainer<Reference> references = new RowItemContainer<Reference>(
		ISIDatabase.REFERENCE_DISPLAY_NAME, ISIDatabase.REFERENCE_TABLE_NAME, Reference.SCHEMA);
	private RowItemContainer<Address> addresses = new RowItemContainer<Address>(
		ISIDatabase.ADDRESS_DISPLAY_NAME, ISIDatabase.ADDRESS_TABLE_NAME, Address.SCHEMA);
	private RowItemContainer<Keyword> keywords = new RowItemContainer<Keyword>(
		ISIDatabase.KEYWORD_DISPLAY_NAME, ISIDatabase.KEYWORD_TABLE_NAME, Keyword.SCHEMA);
	private RowItemContainer<Person> people = new RowItemContainer<Person>(
		ISIDatabase.PERSON_DISPLAY_NAME, ISIDatabase.PERSON_TABLE_NAME, Person.SCHEMA);
	private RowItemContainer<Patent> patents = new RowItemContainer<Patent>(
		ISIDatabase.PATENT_DISPLAY_NAME, ISIDatabase.PATENT_TABLE_NAME, Patent.SCHEMA);
	private RowItemContainer<Document> documents = new RowItemContainer<Document>(
		ISIDatabase.DOCUMENT_DISPLAY_NAME, ISIDatabase.DOCUMENT_TABLE_NAME, Document.SCHEMA);

	/*
	 * Create all of the entity joining tables (Publisher Addresses, Reprint Addresses,
	 *  Research Addresses, Document Keywords, Authors, Editors, Cited Patents, Document
	 *  Occurrences, and Cited References).
	 */

	RowItemContainer<PublisherAddress> publisherAddresses =
		new RowItemContainer<PublisherAddress>(
			ISIDatabase.PUBLISHER_ADDRESSES_DISPLAY_NAME,
			ISIDatabase.PUBLISHER_ADDRESSES_TABLE_NAME,
			PublisherAddress.SCHEMA);

	RowItemContainer<ReprintAddress> reprintAddresses =
		new RowItemContainer<ReprintAddress>(
			ISIDatabase.REPRINT_ADDRESSES_DISPLAY_NAME,
			ISIDatabase.REPRINT_ADDRESSES_TABLE_NAME,
			ReprintAddress.SCHEMA);

	RowItemContainer<ResearchAddress> researchAddresses =
		new RowItemContainer<ResearchAddress>(
			ISIDatabase.RESEARCH_ADDRESSES_DISPLAY_NAME,
			ISIDatabase.RESEARCH_ADDRESSES_TABLE_NAME,
			ResearchAddress.SCHEMA);

	RowItemContainer<DocumentKeyword> documentKeywords =
		new RowItemContainer<DocumentKeyword>(
			ISIDatabase.DOCUMENT_KEYWORDS_DISPLAY_NAME,
			ISIDatabase.DOCUMENT_KEYWORDS_TABLE_NAME,
			DocumentKeyword.SCHEMA);

	RowItemContainer<Author> authors =
		new RowItemContainer<Author>(
			ISIDatabase.AUTHORS_DISPLAY_NAME,
			ISIDatabase.AUTHORS_TABLE_NAME,
		Author.SCHEMA);

	RowItemContainer<Editor> editors =
		new RowItemContainer<Editor>(
			ISIDatabase.EDITORS_DISPLAY_NAME,
			ISIDatabase.EDITORS_TABLE_NAME,
		Editor.SCHEMA);

	RowItemContainer<CitedPatent> citedPatents =
		new RowItemContainer<CitedPatent>(
			ISIDatabase.CITED_PATENTS_DISPLAY_NAME,
			ISIDatabase.CITED_PATENTS_TABLE_NAME,
		CitedPatent.SCHEMA);

	RowItemContainer<DocumentOccurrence> documentOccurrences =
		new RowItemContainer<DocumentOccurrence>(
			ISIDatabase.DOCUMENT_OCCURRENCES_DISPLAY_NAME,
			ISIDatabase.DOCUMENT_OCCURRENCES_TABLE_NAME,
		DocumentOccurrence.SCHEMA);

	RowItemContainer<CitedReference> citedReferences =
		new RowItemContainer<CitedReference>(
			ISIDatabase.CITED_REFERENCES_DISPLAY_NAME,
			ISIDatabase.CITED_REFERENCES_TABLE_NAME,
		CitedReference.SCHEMA);

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
	public ISIModel parseModel(Table table, Collection<Integer> rows) {
		/*
		 * TODO: Edit template comments to describe process from start to finish, read
		 *  independently of code.
		 */
		// For each record/row in the table:

		for (Integer rowIndex : rows) {
			Tuple row = table.getTuple(rowIndex.intValue());

			// Parse ISI File.

			ISIFile isiFile = parseISIFile(row);

			// Parse Publisher.

			Publisher publisher = parsePublisher(row);

			// Parse Source, and link it to Publisher.

			Source source = parseSource(row);
			publisher.setSource(source);

			// Parse References, and link them to People and Sources.

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

			List<Person> currentAuthorPeople;

			try {
				currentAuthorPeople = parseAuthorPeople(row);
			} catch (PersonParsingException e) {
				currentAuthorPeople = new ArrayList<Person>();
			}

			// Parse Document.

			Document document = null;

			if (currentAuthorPeople.size() != 0) {
				document = parseDocument(row, currentAuthorPeople.get(0));
			} else {
				document = parseDocument(row, null);
			}

			// Link the Authors from the Document to the (Author) People.

			for (int ii = 0; ii < currentAuthorPeople.size(); ii++) {
				Person author = currentAuthorPeople.get(ii);
				this.authors.addOrMerge(new Author(document, author, ii));
			}

			// Parse Author Keywords, and link the Document Keywords from the Document to them.

			List<Keyword> authorKeywords =
				parseKeywords(row, AUTHOR_KEYWORDS, ISITag.ORIGINAL_KEYWORDS);

			for (int ii = 0; ii < authorKeywords.size(); ii++) {
				this.documentKeywords.addOrMerge(
					new DocumentKeyword(document, authorKeywords.get(ii), ii));
			}

			// Parse KeyWords Plus, and link the Document Keywords from the Document to them.

			List<Keyword> keywordsPlus =
				parseKeywords(row, KEYWORDS_PLUS, ISITag.NEW_KEYWORDS_GIVEN_BY_ISI);

			for (int ii = 0; ii < keywordsPlus.size(); ii++) {
				this.documentKeywords.addOrMerge(
					new DocumentKeyword(document, keywordsPlus.get(ii), ii));
			}

			// Parse Patent, and link it to the Document.

			Patent patent = parsePatent(row);
			this.citedPatents.addOrMerge(new CitedPatent(document, patent));

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

			// Link up the Document Occurrence.

			this.documentOccurrences.addOrMerge(new DocumentOccurrence(document, isiFile));

			// Link the Cited References to the Document.

			for (Reference reference : currentReferences) {
				this.citedReferences.addOrMerge(new CitedReference(document, reference));
			}
		}

		// Given all of the master lists of row items, construct an ISIModel and return it.

		return new ISIModel(
			this.isiFiles,
			this.publishers,
			this.sources,
			this.references,
			this.addresses,
			this.keywords,
			this.people,
			this.patents,
			this.documents,
			this.publisherAddresses,
			this.reprintAddresses,
			this.researchAddresses,
			this.documentKeywords,
			this.authors,
			this.editors,
			this.citedPatents,
			this.documentOccurrences,
			this.citedReferences);
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
	private List<Person> parseAuthorPeople(Tuple row) throws PersonParsingException {
		List<Person> authors = new ArrayList<Person>();

		String rawAuthorsString =
			StringUtilities.simpleClean(row.getString(ISITag.AUTHORS.getColumnName()));
		String[] authorStrings = rawAuthorsString.split("\\|");
		String rawFullAuthorNamesString =
			StringUtilities.simpleClean(row.getString(ISITag.AUTHORS_FULL_NAMES.getColumnName()));
		String[] authorFullNameStrings = rawFullAuthorNamesString.split("\\|");

		for (int ii = 0; ii < authorStrings.length; ii++) {
			String authorString = authorStrings[ii];
			String cleanedAuthorString = StringUtilities.simpleClean(authorString);
			Pair<Person, Boolean> personParsingResult;

			if (authorStrings.length == authorFullNameStrings.length) {
				String authorFullNameString = authorFullNameStrings[ii];
				String cleanedAuthorFullNameString =
					StringUtilities.simpleClean(authorFullNameString);
				personParsingResult = PersonParser.parsePerson(
					this.people.getKeyGenerator(),
					cleanedAuthorString,
					cleanedAuthorFullNameString);
			} else {
				personParsingResult = PersonParser.parsePerson(
					this.people.getKeyGenerator(), cleanedAuthorString, "");
			}

			Person authorPerson = personParsingResult.getFirstObject();
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