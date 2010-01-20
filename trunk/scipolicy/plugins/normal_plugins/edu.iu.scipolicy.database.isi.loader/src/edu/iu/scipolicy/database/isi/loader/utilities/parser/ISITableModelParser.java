package edu.iu.scipolicy.database.isi.loader.utilities.parser;

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
import edu.iu.nwb.shared.isiutil.database.ISI;
import edu.iu.scipolicy.database.isi.loader.model.entity.Address;
import edu.iu.scipolicy.database.isi.loader.model.entity.Document;
import edu.iu.scipolicy.database.isi.loader.model.entity.ISIFile;
import edu.iu.scipolicy.database.isi.loader.model.entity.Keyword;
import edu.iu.scipolicy.database.isi.loader.model.entity.Patent;
import edu.iu.scipolicy.database.isi.loader.model.entity.Person;
import edu.iu.scipolicy.database.isi.loader.model.entity.Publisher;
import edu.iu.scipolicy.database.isi.loader.model.entity.Reference;
import edu.iu.scipolicy.database.isi.loader.model.entity.Source;
import edu.iu.scipolicy.database.isi.loader.model.entity.relationship.Author;
import edu.iu.scipolicy.database.isi.loader.model.entity.relationship.CitedPatent;
import edu.iu.scipolicy.database.isi.loader.model.entity.relationship.CitedReference;
import edu.iu.scipolicy.database.isi.loader.model.entity.relationship.DocumentKeyword;
import edu.iu.scipolicy.database.isi.loader.model.entity.relationship.DocumentOccurrence;
import edu.iu.scipolicy.database.isi.loader.model.entity.relationship.Editor;
import edu.iu.scipolicy.database.isi.loader.model.entity.relationship.PublisherAddress;
import edu.iu.scipolicy.database.isi.loader.model.entity.relationship.ReprintAddress;
import edu.iu.scipolicy.database.isi.loader.model.entity.relationship.ResearchAddress;
import edu.iu.scipolicy.database.isi.loader.utilities.parser.exception.PersonParsingException;
import edu.iu.scipolicy.database.isi.loader.utilities.parser.exception.ReferenceParsingException;

public class ISITableModelParser {
	public static final String AUTHOR_KEYWORDS = "authorKeywords";
	public static final String KEYWORDS_PLUS = "keywordsPlus";

	/*
	 * For each type of entity (ISI File, Publisher, Source, Reference, Address, Keyword,
	 *  Person, Patent, and Document), create a master list of entities.
	 */

	private RowItemContainer<ISIFile> isiFiles = new RowItemContainer<ISIFile>(
		ISI.ISI_FILE_DISPLAY_NAME, ISI.ISI_FILE_TABLE_NAME, ISIFile.SCHEMA);
	private RowItemContainer<Publisher> publishers = new RowItemContainer<Publisher>(
		ISI.PUBLISHER_DISPLAY_NAME, ISI.PUBLISHER_TABLE_NAME, Publisher.SCHEMA);
	private RowItemContainer<Source> sources = new RowItemContainer<Source>(
		ISI.SOURCE_DISPLAY_NAME, ISI.SOURCE_TABLE_NAME, Source.SCHEMA);
	private RowItemContainer<Reference> references = new RowItemContainer<Reference>(
		ISI.REFERENCE_DISPLAY_NAME, ISI.REFERENCE_TABLE_NAME, Reference.SCHEMA);
	private RowItemContainer<Address> addresses = new RowItemContainer<Address>(
		ISI.ADDRESS_DISPLAY_NAME, ISI.ADDRESS_TABLE_NAME, Address.SCHEMA);
	private RowItemContainer<Keyword> keywords = new RowItemContainer<Keyword>(
		ISI.KEYWORD_DISPLAY_NAME, ISI.KEYWORD_TABLE_NAME, Keyword.SCHEMA);
	private RowItemContainer<Person> people = new RowItemContainer<Person>(
		ISI.PERSON_DISPLAY_NAME, ISI.PERSON_TABLE_NAME, Person.SCHEMA);
	private RowItemContainer<Patent> patents = new RowItemContainer<Patent>(
		ISI.PATENT_DISPLAY_NAME, ISI.PATENT_TABLE_NAME, Patent.SCHEMA);
	private RowItemContainer<Document> documents = new RowItemContainer<Document>(
		ISI.DOCUMENT_DISPLAY_NAME, ISI.DOCUMENT_TABLE_NAME, Document.SCHEMA);

	/*
	 * Create all of the entity joining tables (Publisher Addresses, Reprint Addresses,
	 *  Research Addresses, Document Keywords, Authors, Editors, Cited Patents, Document
	 *  Occurrences, and Cited References).
	 */

	RowItemContainer<PublisherAddress> publisherAddresses =
		new RowItemContainer<PublisherAddress>(
			ISI.PUBLISHER_ADDRESSES_DISPLAY_NAME,
			ISI.PUBLISHER_ADDRESSES_TABLE_NAME,
			PublisherAddress.SCHEMA);

	RowItemContainer<ReprintAddress> reprintAddresses =
		new RowItemContainer<ReprintAddress>(
			ISI.REPRINT_ADDRESSES_DISPLAY_NAME,
			ISI.REPRINT_ADDRESSES_TABLE_NAME,
			ReprintAddress.SCHEMA);

	RowItemContainer<ResearchAddress> researchAddresses =
		new RowItemContainer<ResearchAddress>(
			ISI.RESEARCH_ADDRESSES_DISPLAY_NAME,
			ISI.RESEARCH_ADDRESSES_TABLE_NAME,
			ResearchAddress.SCHEMA);

	RowItemContainer<DocumentKeyword> documentKeywords =
		new RowItemContainer<DocumentKeyword>(
			ISI.DOCUMENT_KEYWORDS_DISPLAY_NAME,
			ISI.DOCUMENT_KEYWORDS_TABLE_NAME,
			DocumentKeyword.SCHEMA);

	RowItemContainer<Author> authors =
		new RowItemContainer<Author>(
			ISI.AUTHORS_DISPLAY_NAME,
			ISI.AUTHORS_TABLE_NAME,
		Author.SCHEMA);

	RowItemContainer<Editor> editors =
		new RowItemContainer<Editor>(
			ISI.EDITORS_DISPLAY_NAME,
			ISI.EDITORS_TABLE_NAME,
		Editor.SCHEMA);

	RowItemContainer<CitedPatent> citedPatents =
		new RowItemContainer<CitedPatent>(
			ISI.CITED_PATENTS_DISPLAY_NAME,
			ISI.CITED_PATENTS_TABLE_NAME,
		CitedPatent.SCHEMA);

	RowItemContainer<DocumentOccurrence> documentOccurrences =
		new RowItemContainer<DocumentOccurrence>(
			ISI.DOCUMENT_OCCURRENCES_DISPLAY_NAME,
			ISI.DOCUMENT_OCCURRENCES_TABLE_NAME,
		DocumentOccurrence.SCHEMA);

	RowItemContainer<CitedReference> citedReferences =
		new RowItemContainer<CitedReference>(
			ISI.CITED_REFERENCES_DISPLAY_NAME,
			ISI.CITED_REFERENCES_TABLE_NAME,
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

			// Parse Source.

			Source source = parseSource(row);

			// Parse Publisher Address (a link between Publisher and Address).

			Address addressOfPublisher = parseAddressOfPublisher(row);

			// Parse Publisher.

			Publisher publisher = parsePublisher(row);

			// Link the Publisher to the other things parsed.

			if ((publisher != null) && (source != null)) {
				publisher.setSource(source);
			}

			if ((publisher != null) && (addressOfPublisher != null)) {
				this.publisherAddresses.addOrMerge(
					new PublisherAddress(publisher, addressOfPublisher));
			}

			// Parse Document.

			Document document = parseDocument(row, currentAuthorPeople, source);

			// Link the Document to the other things parsed.

			linkDocumentToPeople_AsAuthors(document, currentAuthorPeople, row);
			linkDocumentToPeople_AsEditors(document, currentEditorPeople, row);
			this.documentOccurrences.addOrMerge(new DocumentOccurrence(document, isiFile));
			linkDocumentToKeywords(document, authorKeywords);
			linkDocumentToKeywords(document, keywordsPlus);

			if (patent != null) {
				this.citedPatents.addOrMerge(new CitedPatent(document, patent));
			}

			if (addressForReprinting != null) {
				this.reprintAddresses.addOrMerge(
					new ReprintAddress(document, addressForReprinting));
			}

			linkDocumentToAddressesOfResearch(document, currentAddressesOfResearch);
			linkDocumentToCitedReferences(document, currentReferences);
		}

		linkReferencesToDocuments();

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
			String cleanedKeywordString = StringUtilities.simpleClean(keywordString);

			if (!StringUtilities.isEmptyOrWhiteSpace(keywordString)) {
				Keyword keyword = this.keywords.addOrMerge(
					new Keyword(this.keywords.getKeyGenerator(), cleanedKeywordString, keywordType));
				keywords.add(keyword);
			}
		}

		return keywords;
	}

	private Patent parsePatent(Tuple row) {
		String patentNumber =
			StringUtilities.simpleClean(row.getString(ISITag.CITED_PATENT.getColumnName()));

		if (!StringUtilities.isEmptyOrWhiteSpace(patentNumber)) {
			return this.patents.addOrMerge(
				new Patent(this.patents.getKeyGenerator(), patentNumber));
		} else {
			return null;
		}
	}

	private Address parseAddressForReprinting(Tuple row) {
		String addressForReprintingString =
			StringUtilities.simpleClean(row.getString(ISITag.REPRINT_ADDRESS.getColumnName()));

		if (!StringUtilities.isEmptyOrWhiteSpace(addressForReprintingString)) {
			// TODO: AddressParser?

			return this.addresses.addOrMerge(
				new Address(this.addresses.getKeyGenerator(), addressForReprintingString));
		} else {
			return null;
		}
	}

	private List<Address> parseAddressesOfResearch(Tuple row) {
		List<Address> addressesOfResearch = new ArrayList<Address>();

		String rawAddressesString =
			StringUtilities.simpleClean(row.getString(ISITag.RESEARCH_ADDRESSES.getColumnName()));
		String addressStrings[] = rawAddressesString.split("\\|");

		for (String addressString : addressStrings) {
			String cleanedAddressString = StringUtilities.simpleClean(addressString);

			if (!StringUtilities.isEmptyOrWhiteSpace(cleanedAddressString)) {
				// TODO: AddressParser?
				Address mergedAddressOfResearch = this.addresses.addOrMerge(
					new Address(this.addresses.getKeyGenerator(), cleanedAddressString));
				addressesOfResearch.add(mergedAddressOfResearch);
			}
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
					referenceData.getDigitalObjectIdentifier(),
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

	private Document parseDocument(Tuple row, List<Person> authorPeople, Source source) {
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
			source,
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

		/*if (this.sources.getKeyGenerator().getNextKey() == 73) {
			System.err.println("bookSeriesTitle: \"" + bookSeriesTitle + "\"");
			System.err.println("bookSeriesSubtitle: \"" + bookSeriesSubtitle + "\"");
			System.err.println("conferenceHost: \"" + conferenceHost + "\"");
			System.err.println("conferenceLocation: \"" + conferenceLocation + "\"");
			System.err.println("conferenceSponsors: \"" + conferenceSponsors + "\"");
			System.err.println("conferenceTitle: \"" + conferenceTitle + "\"");
			System.err.println("fullTitle: \"" + fullTitle + "\"");
			System.err.println("isoTitleAbbreviation: \"" + isoTitleAbbreviation + "\"");
			System.err.println("issn: \"" + issn + "\"");
			System.err.println("publicationType: \"" + publicationType + "\"");
			System.err.println("twentyNineCharacterSourceTitleAbbreviation: \"" + twentyNineCharacterSourceTitleAbbreviation + "\"");
		}*/

		if (!StringUtilities.allAreEmptyOrWhiteSpace(
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
				twentyNineCharacterSourceTitleAbbreviation)) {
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
		} else {
			return null;
		}
	}

	private Address parseAddressOfPublisher(Tuple row) {
		String addressOfPublisherString =
			StringUtilities.simpleClean(row.getString(ISITag.PUBLISHER_ADDRESS.getColumnName()));

		if (!StringUtilities.isEmptyOrWhiteSpace(addressOfPublisherString)) {
			return this.addresses.addOrMerge(
				new Address(this.addresses.getKeyGenerator(), addressOfPublisherString));
		} else {
			return null;
		}
	}

	private Publisher parsePublisher(Tuple row) {
		String city =
			StringUtilities.simpleClean(row.getString(ISITag.PUBLISHER_CITY.getColumnName()));
		String name =
			StringUtilities.simpleClean(row.getString(ISITag.PUBLISHER.getColumnName()));
		String webAddress = StringUtilities.simpleClean(
			row.getString(ISITag.PUBLISHER_WEB_ADDRESS.getColumnName()));

		if (!StringUtilities.allAreEmptyOrWhiteSpace(city, name, webAddress)) {
			Publisher publisher =
				new Publisher(this.publishers.getKeyGenerator(), city, name, webAddress);

			return this.publishers.addOrMerge(publisher);
		} else {
			return null;
		}
	}

	private void linkReferencesToDocuments() {
		for (Reference reference : (List<Reference>)this.references.getItems()) {
			String referenceDigitalObjectIdentifier = reference.getDigitalObjectIdentifier();

			for (Document document : (List<Document>)this.documents.getItems()) {
				String documentDigitalObjectIdentifier = document.getDigitalObjectIdentifier();

				if (!StringUtilities.isEmptyOrWhiteSpace(documentDigitalObjectIdentifier) &&
						documentDigitalObjectIdentifier.equals(referenceDigitalObjectIdentifier)) {
					reference.setPaper(document);
				}
			}
		}
	}
}