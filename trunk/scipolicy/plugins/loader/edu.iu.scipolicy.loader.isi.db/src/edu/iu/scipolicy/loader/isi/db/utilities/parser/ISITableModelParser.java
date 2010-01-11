package edu.iu.scipolicy.loader.isi.db.utilities.parser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.cishell.utilities.IntegerParserWithDefault;
import org.cishell.utilities.StringUtilities;

import prefuse.data.Table;
import prefuse.data.Tuple;
import edu.iu.cns.database.loader.framework.RowItemContainer;
import edu.iu.cns.database.loader.framework.utilities.DatabaseModel;
import edu.iu.cns.shared.utilities.Pair;
import edu.iu.nwb.shared.isiutil.ISITableReader;
import edu.iu.nwb.shared.isiutil.ISITag;
import edu.iu.nwb.shared.isiutil.database.ISIDatabase;
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
import edu.iu.scipolicy.loader.isi.db.utilities.parser.exception.PersonParsingException;
import edu.iu.scipolicy.loader.isi.db.utilities.parser.exception.ReferenceParsingException;

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
	// TODO: Rename this method. Also get rid of DatabaseModel.
	public DatabaseModel parseModel(Table table, Collection<Integer> rows) {
		/*
		 * TODO: Edit template comments to describe process from start to finish, read
		 *  independently of code.
		 */
		// For each record/row in the table:

		for (Integer rowIndex : rows) {
			Tuple row = table.getTuple(rowIndex.intValue());

			// Parse (Author) People.

			List<Person> currentAuthorPeople = parseAuthorPeople(row);

			// Parse (Editor) People.

			List<Person> currentEditorPeople = parseEditorPeople(row);

			// Parse ISI File.

			ISIFile isiFile = parseISIFile(row);

			/*
			 * Parse Author Keywords, and link the Document Keywords from the Document to them
			 *  via DocumentKeywords.
			 */

			List<Keyword> authorKeywords =
				parseKeywords(row, AUTHOR_KEYWORDS, ISITag.ORIGINAL_KEYWORDS);

			/*
			 * Parse KeyWords Plus, and link the Document Keywords from the Document to them
			 *  via DocumentKeywords.
			 */

			List<Keyword> keywordsPlus =
				parseKeywords(row, KEYWORDS_PLUS, ISITag.NEW_KEYWORDS_GIVEN_BY_ISI);

			// Parse Patent, and link it to the Document via CitedPatents.

			Patent patent = parsePatent(row);

			// Parse Reprint Address (a link between Document and Address).

			Address addressForReprinting = parseAddressForReprinting(row);

			// Parse Research Addresses (links between Document and Address).

			List<Address> currentAddressesOfResearch = parseAddressesOfResearch(row);

			// Parse References, and link them to People and Sources.

			List<Reference> currentReferences = parseReferences(row);
			handlePeopleAndSourcesFromReferences(currentReferences);

			// Parse Document.

			Document document = parseDocument(row, currentAuthorPeople);

			// Link the Document to the other things parsed.

			linkDocumentToPeople_AsAuthors(document, currentAuthorPeople, row);
			linkDocumentToPeople_AsEditors(document, currentEditorPeople, row);
			this.documentOccurrences.addOrMerge(new DocumentOccurrence(document, isiFile));
			linkDocumentToKeywords(document, authorKeywords);
			linkDocumentToKeywords(document, keywordsPlus);
			this.citedPatents.addOrMerge(new CitedPatent(document, patent));
			this.reprintAddresses.addOrMerge(new ReprintAddress(document, addressForReprinting));
			linkDocumentToAddressesOfResearch(document, currentAddressesOfResearch);
			linkDocumentToCitedReferences(document, currentReferences);

			// Parse Source, and link it to Publisher.

			Source source = parseSource(row);

			// Parse Publisher Address (a link between Publisher and Address).

			Address addressOfPublisher = parseAddressOfPublisher(row);

			// Parse Publisher.

			Publisher publisher = parsePublisher(row);

			// Link the Publisher to the other things parsed.

			publisher.setSource(source);
			this.publisherAddresses.addOrMerge(
				new PublisherAddress(publisher, addressOfPublisher));

			// Parse Editors. (?)
			//TODO: Look into what's up with editors.
		}

		// Given all of the master lists of row items, construct an DatabaseModel and return it.

		return new DatabaseModel(
			// Entities
			this.addresses,
			this.documents,
			this.isiFiles,
			this.keywords,
			this.patents,
			this.people,
			this.publishers,
			this.references,
			this.sources,
			// Relationships
			this.authors,
			this.editors,
			this.citedPatents,
			this.citedReferences,
			this.documentKeywords,
			this.documentOccurrences,
			this.publisherAddresses,
			this.reprintAddresses,
			this.researchAddresses);
	}

	private List<Person> parseAuthorPeople(Tuple row) {
		List<Person> authorPeople = new ArrayList<Person>();

		String rawAuthorsString =
			StringUtilities.simpleClean(row.getString(ISITag.AUTHORS.getColumnName()));
		String[] authorStrings = rawAuthorsString.split("\\|");
		String rawFullAuthorNamesString = StringUtilities.simpleClean(
			row.getString(ISITag.AUTHORS_FULL_NAMES.getColumnName()));
		String[] authorFullNameStrings = rawFullAuthorNamesString.split("\\|");

		for (int ii = 0; ii < authorStrings.length; ii++) {
			String authorString = authorStrings[ii];
			String cleanedAuthorString = StringUtilities.simpleClean(authorString);
			Pair<Person, Boolean> personParsingResult;

			try {
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
				authorPeople.add(mergedAuthorPerson);
			} catch (PersonParsingException e) {}
		}

		return authorPeople;
	}

	private List<Person> parseEditorPeople(Tuple row) {
		List<Person> editorPeople = new ArrayList<Person>();

		String rawEditorsString =
			StringUtilities.simpleClean(row.getString(ISITag.EDITORS.getColumnName()));
		String[] editorStrings = rawEditorsString.split("\\|");

		for (int ii = 0; ii < editorStrings.length; ii++) {
			String editorString = editorStrings[ii];
			String cleanedEditorString = StringUtilities.simpleClean(editorString);
			Pair<Person, Boolean> personParsingResult;

			try {
				personParsingResult = PersonParser.parsePerson(
					this.people.getKeyGenerator(), cleanedEditorString, "");

				Person editorPerson = personParsingResult.getFirstObject();
				Person mergedEditorPerson = this.people.addOrMerge(editorPerson);
				editorPeople.add(mergedEditorPerson);
			} catch (PersonParsingException e) {}
		}

		return editorPeople;
	}

	private ISIFile parseISIFile(Tuple row) {
		String formatVersionNumber =
			StringUtilities.simpleClean(row.getString(ISITag.VERSION_NUMBER.getColumnName()));
		String fileName =
			StringUtilities.simpleClean(row.getString(ISITableReader.FILE_PATH_COLUMN_NAME));
		String fileType =
			StringUtilities.simpleClean(row.getString(ISITag.FILE_TYPE.getColumnName()));
		ISIFile isiFile =
			new ISIFile(this.isiFiles.getKeyGenerator(), formatVersionNumber, fileName, fileType);

		return this.isiFiles.addOrMerge(isiFile);
	}

	private List<Keyword> parseKeywords(
			Tuple row, String keywordType, ISITag sourceTag) {
		List<Keyword> keywords = new ArrayList<Keyword>();

		String rawKeywordsString =
			StringUtilities.simpleClean(row.getString(sourceTag.getColumnName()));
		String[] keywordStrings = rawKeywordsString.split("\\|");

		for (String keywordString : keywordStrings) {
			Keyword keyword = this.keywords.addOrMerge(
				new Keyword(this.keywords.getKeyGenerator(), keywordString, keywordType));
			keywords.add(keyword);
		}

		return keywords;
	}

	private Patent parsePatent(Tuple row) {
		String patentNumber =
			StringUtilities.simpleClean(row.getString(ISITag.CITED_PATENT.getColumnName()));

		return this.patents.addOrMerge(new Patent(this.patents.getKeyGenerator(), patentNumber));
	}

	private Address parseAddressForReprinting(Tuple row) {
		String addressForReprintingString =
			StringUtilities.simpleClean(row.getString(ISITag.REPRINT_ADDRESS.getColumnName()));
		// TODO: AddressParser?

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

	private List<Reference> parseReferences(Tuple row) {
		List<Reference> currentReferences = new ArrayList<Reference>();
		String rawReferencesString =
			StringUtilities.simpleClean(row.getString(ISITag.CITED_REFERENCES.getColumnName()));
		String[] referenceStrings = rawReferencesString.split("\\|");

		for (String referenceString : referenceStrings) {
			if (StringUtilities.isEmptyOrWhiteSpace(referenceString)) {
				continue;
			}

			try {
				ReferenceDataParser referenceData = new ReferenceDataParser(
					this.people.getKeyGenerator(),
					this.sources.getKeyGenerator(),
					referenceString);

				Reference reference = new Reference(
					this.references.getKeyGenerator(),
					referenceData.getAnnotation(),
					referenceData.getAuthorPerson(),
					referenceData.authorWasStarred(),
					referenceData.getPageNumber(),
					null,
					referenceData.getRawString(),
					referenceData.getVolume(),
					referenceData.getSource(),
					referenceData.getYear());
				Reference mergedReference = this.references.addOrMerge(reference);
				currentReferences.add(mergedReference);
			} catch (ReferenceParsingException e) {
				// TODO: Print a warning?  For now, it's just skipped.
			}
		}

		return currentReferences;
	}

	private void handlePeopleAndSourcesFromReferences(List<Reference> currentReferences) {
		for (Reference reference : currentReferences) {
			Person referenceAuthorPerson = reference.getAuthorPerson();
			Source referenceSource = reference.getSource();

			if (referenceAuthorPerson != null) {
				Person mergedAuthor = this.people.addOrMerge(referenceAuthorPerson);
				reference.setAuthor(mergedAuthor);
			}

			if (referenceSource != null) {
				Source mergedSource = this.sources.addOrMerge(referenceSource);
				reference.setSource(mergedSource);
			}
		}
	}

	private Document parseDocument(Tuple row, List<Person> authorPeople) {
		Person firstAuthor = null;

		if (authorPeople.size() != 0) {
			firstAuthor = authorPeople.iterator().next();
		}

		String documentAbstract =
			StringUtilities.simpleClean(row.getString(ISITag.ABSTRACT.getColumnName()));
		String articleNumber =
			StringUtilities.simpleClean(
				row.getString(ISITag.ARTICLE_NUMBER_OF_NEW_APS_JOURNALS.getColumnName()));

		int beginningPage = IntegerParserWithDefault.parse(
			StringUtilities.simpleClean(row.getString(ISITag.BEGINNING_PAGE.getColumnName())));

		int citedReferenceCount = IntegerParserWithDefault.parse(StringUtilities.simpleClean(
			row.getString(ISITag.CITED_REFERENCE_COUNT.getColumnName())));
		int citedYear = IntegerParserWithDefault.parse(StringUtilities.simpleClean(
			row.getString(ISITag.CITED_YEAR.getColumnName())));

		String digitalObjectidentifier =
			StringUtilities.simpleClean(row.getString(ISITag.DOI.getColumnName()));
		String documentType =
			StringUtilities.simpleClean(row.getString(ISITag.DOCUMENT_TYPE.getColumnName()));
		String documentVolume =
			StringUtilities.simpleClean(row.getString(ISITag.VOLUME.getColumnName()));

		int endingPage = IntegerParserWithDefault.parse(
			StringUtilities.simpleClean(row.getString(ISITag.ENDING_PAGE.getColumnName())));

		String fundingAgencyAndGrantNumber = StringUtilities.simpleClean(
			row.getString(ISITag.FUNDING_AGENCY_AND_GRANT_NUMBER.getColumnName()));
		String fundingText =
			StringUtilities.simpleClean(row.getString(ISITag.FUNDING_TEXT.getColumnName()));

		String isbn = StringUtilities.simpleClean(row.getString(ISITag.ISBN.getColumnName()));
		String isiDocumentDeliveryNumber = StringUtilities.simpleClean(
			row.getString(ISITag.ISI_DOCUMENT_DELIVERY_NUMBER.getColumnName()));
		String isiUniqueArticleIdentifier =
			StringUtilities.simpleClean(row.getString(ISITag.UNIQUE_ID.getColumnName()));
		String issue = StringUtilities.simpleClean(row.getString(ISITag.ISSUE.getColumnName()));

		String language =
			StringUtilities.simpleClean(row.getString(ISITag.LANGUAGE.getColumnName()));

		int pageCount = IntegerParserWithDefault.parse(
			StringUtilities.simpleClean(row.getString(ISITag.NUMBER_OF_PAGES.getColumnName())));
		String partNumber =
			StringUtilities.simpleClean(row.getString(ISITag.PART_NUMBER.getColumnName()));
		String publicationDate =
			StringUtilities.simpleClean(row.getString(ISITag.PUBLICATION_DATE.getColumnName()));
		int publicationYear = IntegerParserWithDefault.parse(
			StringUtilities.simpleClean(row.getString(ISITag.PUBLICATION_YEAR.getColumnName())));

		String specialIssue =
			StringUtilities.simpleClean(row.getString(ISITag.SPECIAL_ISSUE.getColumnName()));
		String subjectCategory =
			StringUtilities.simpleClean(row.getString(ISITag.SUBJECT_CATEGORY.getColumnName()));
		String supplement =
			StringUtilities.simpleClean(row.getString(ISITag.SUPPLEMENT.getColumnName()));

		int timesCited = IntegerParserWithDefault.parse(
			StringUtilities.simpleClean(row.getString(ISITag.TIMES_CITED.getColumnName())));
		String title = StringUtilities.simpleClean(row.getString(ISITag.TITLE.getColumnName()));

		// TODO: Use ISITag.isiTagArray to determine which tags we don't handle, and add those in
		// to document.

		return this.documents.addOrMerge(new Document(
			this.documents.getKeyGenerator(),
			documentAbstract,
			articleNumber,
			beginningPage,
			citedReferenceCount,
			citedYear,
			digitalObjectidentifier,
			documentType,
			documentVolume,
			endingPage,
			firstAuthor,
			fundingAgencyAndGrantNumber,
			fundingText,
			isbn,
			isiDocumentDeliveryNumber,
			isiUniqueArticleIdentifier,
			issue,
			language,
			pageCount,
			partNumber,
			publicationDate,
			publicationYear,
			specialIssue,
			subjectCategory,
			supplement,
			timesCited,
			title));
	}

	private void linkDocumentToPeople_AsAuthors(
			Document document, List<Person> authorPeople, Tuple row) {
		String rawEmailAddressesString =
			StringUtilities.simpleClean(row.getString(ISITag.EMAIL_ADDRESSES.getColumnName()));
		String[] emailAddressStrings = rawEmailAddressesString.split("\\|");

		if (emailAddressStrings.length != authorPeople.size()) {
			// TODO: Warning or fail?
			// Assume just the first e-mail address for all authors.
			String emailAddress = "";

			if (emailAddressStrings.length != 0) {
				emailAddress = emailAddressStrings[0];
			}

			Iterator<Person> personIterator = authorPeople.iterator();

			for (int ii = 0; ii < authorPeople.size(); ii++) {
				Person authorPerson = personIterator.next();
				this.authors.addOrMerge(new Author(document, authorPerson, emailAddress, ii));
			}
		} else {
			Iterator<Person> personIterator = authorPeople.iterator();

			for (int ii = 0; ii < authorPeople.size(); ii++) {
				Person authorPerson = personIterator.next();
				String emailAddress = emailAddressStrings[ii];
				this.authors.addOrMerge(new Author(document, authorPerson, emailAddress, ii));
			}
		}
	}

	private void linkDocumentToPeople_AsEditors(
			Document document, List<Person> editorPeople, Tuple row) {
		Iterator<Person> personIterator = editorPeople.iterator();

		for (int ii = 0; ii < editorPeople.size(); ii++) {
			Person editorPerson = personIterator.next();
			this.editors.addOrMerge(new Editor(document, editorPerson, ii));
		}
	}

	private void linkDocumentToKeywords(Document document, List<Keyword> keywords) {
		for (int ii = 0; ii < keywords.size(); ii++) {
			this.documentKeywords.addOrMerge(
				new DocumentKeyword(document, keywords.get(ii), ii));
		}
	}

	private void linkDocumentToAddressesOfResearch(
			Document document, List<Address> addressesOfResearch) {
		for (int ii = 0; ii < addressesOfResearch.size(); ii++) {
			Address addressOfResearch = addressesOfResearch.get(ii);
			this.researchAddresses.addOrMerge(
				new ResearchAddress(document, addressOfResearch, ii));
		}
	}

	private void linkDocumentToCitedReferences(
			Document document, List<Reference> references) {
		for (Reference reference : references) {
			this.citedReferences.addOrMerge(new CitedReference(document, reference));
		}
	}

	private Source parseSource(Tuple row) {
		String bookSeriesTitle =
			StringUtilities.simpleClean(row.getString(ISITag.BOOK_SERIES_TITLE.getColumnName()));
		String bookSeriesSubtitle = StringUtilities.simpleClean(
			row.getString(ISITag.BOOK_SERIES_SUBTITLE.getColumnName()));

		String conferenceHost =
			StringUtilities.simpleClean(row.getString(ISITag.CONFERENCE_HOST.getColumnName()));
		String conferenceLocation =
			StringUtilities.simpleClean(row.getString(ISITag.CONFERENCE_LOCATION.getColumnName()));
		String conferenceSponsors =
			StringUtilities.simpleClean(row.getString(ISITag.CONFERENCE_SPONSORS.getColumnName()));
		String conferenceTitle =
			StringUtilities.simpleClean(row.getString(ISITag.CONFERENCE_TITLE.getColumnName()));

		String fullTitle =
			StringUtilities.simpleClean(row.getString(ISITag.FULL_JOURNAL_TITLE.getColumnName()));

		String isoTitleAbbreviation = StringUtilities.simpleClean(
			row.getString(ISITag.ISO_JOURNAL_TITLE_ABBREVIATION.getColumnName()));
		String issn =
			StringUtilities.simpleClean(row.getString(ISITag.ISSN.getColumnName()));

		String publicationType =
			StringUtilities.simpleClean(row.getString(ISITag.PUBLICATION_TYPE.getColumnName()));

		String twentyNineCharacterSourceTitleAbbreviation = StringUtilities.simpleClean(
			row.getString(ISITag.TWENTY_NINE_CHAR_JOURNAL_ABBREVIATION.getColumnName()));
		// TODO: ?
		Source source = new Source(
			this.sources.getKeyGenerator(),
			bookSeriesTitle,
			bookSeriesSubtitle,
			conferenceHost,
			conferenceLocation,
			conferenceSponsors,
			conferenceTitle,
			fullTitle,
			isoTitleAbbreviation,
			issn,
			publicationType,
			twentyNineCharacterSourceTitleAbbreviation);

		return this.sources.addOrMerge(source);
	}

	private Address parseAddressOfPublisher(Tuple row) {
		String addressOfPublisherString =
			StringUtilities.simpleClean(row.getString(ISITag.PUBLISHER_ADDRESS.getColumnName()));

		return this.addresses.addOrMerge(
			new Address(this.addresses.getKeyGenerator(), addressOfPublisherString));
	}

	private Publisher parsePublisher(Tuple row) {
		String city =
			StringUtilities.simpleClean(row.getString(ISITag.PUBLISHER_CITY.getColumnName()));
		String name =
			StringUtilities.simpleClean(row.getString(ISITag.PUBLISHER.getColumnName()));
		String webAddress = StringUtilities.simpleClean(
			row.getString(ISITag.PUBLISHER_WEB_ADDRESS.getColumnName()));
		Publisher publisher =
			new Publisher(this.publishers.getKeyGenerator(), city, name, webAddress);

		return this.publishers.addOrMerge(publisher);
	}
}