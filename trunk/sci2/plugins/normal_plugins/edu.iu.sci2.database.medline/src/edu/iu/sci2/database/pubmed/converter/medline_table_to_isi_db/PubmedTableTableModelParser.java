package edu.iu.sci2.database.pubmed.converter.medline_table_to_isi_db;

import static edu.iu.sci2.database.scopus.load.EntityUtils.cleanString;
import static edu.iu.sci2.database.scopus.load.EntityUtils.removeArrayWrapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.osgi.service.log.LogService;

import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.data.util.TableIterator;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;

import edu.iu.cns.database.load.framework.utilities.DatabaseModel;
import edu.iu.cns.database.load.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.sci2.database.pubmed.common.AbstractPubmedTableModelParser;
import edu.iu.sci2.database.scholarly.model.entity.Address;
import edu.iu.sci2.database.scholarly.model.entity.Author;
import edu.iu.sci2.database.scholarly.model.entity.Document;
import edu.iu.sci2.database.scholarly.model.entity.DocumentKeyword;
import edu.iu.sci2.database.scholarly.model.entity.Editor;
import edu.iu.sci2.database.scholarly.model.entity.Keyword;
import edu.iu.sci2.database.scholarly.model.entity.Person;
import edu.iu.sci2.database.scholarly.model.entity.ResearchAddress;
import edu.iu.sci2.database.scholarly.model.entity.Source;
import edu.iu.sci2.medline.common.MedlineField;
import edu.iu.sci2.medline.common.MedlineTable;

/**
 * A parser that can create a {@linkplain DatabaseModel} from a {@linkplain MedlineTable}.
 */
public class PubmedTableTableModelParser extends
		AbstractPubmedTableModelParser {

	private Table table;
	private LogService logger;
	private DatabaseModel model;
	
	public PubmedTableTableModelParser(MedlineTable medlineTable,
			LogService logger) {
		this.logger = logger;
		this.table = medlineTable.getTable();
		this.model = null;
	}

	@Override
	public DatabaseModel getModel() {
		if (this.model == null) {
			this.model = parseModel();
		}
		return this.model;
	}
	
	private DatabaseModel parseModel() {
		TableIterator rowNumbers = this.table.iterator();
		while (rowNumbers.hasNext()) {
			int rowNumber = rowNumbers.nextInt();
			Tuple row = this.table.getTuple(rowNumber);

			/*
			 * Sources Table
			 */
			Source source = getSource(this.sourcesTable.getKeyGenerator(), row,
					this.logger);
			this.sourcesTable.add(source);

			/*
			 * Documents Table
			 */

			Document document = getDocument(this.documentsTable.getKeyGenerator(),
					row, this.logger);
			this.documentsTable.add(document);

			/*
			 * People Table - add investigators
			 */
			ImmutableList<String> investigatorNames = getCleanSplitValues(row, MedlineField.INVESTIGATOR_NAME, this.logger);
			List<Person> investigators = getPeople(
					this.peopleTable.getKeyGenerator(), investigatorNames, this.logger,
					MedlineField.INVESTIGATOR_NAME);
			ImmutableList<String> fullInvestigatorNames = getCleanSplitValues(row, MedlineField.FULL_INVESTIGATOR_NAME, this.logger);
			investigators.addAll(getPeople(this.peopleTable.getKeyGenerator(), fullInvestigatorNames,
					this.logger, MedlineField.FULL_INVESTIGATOR_NAME));

			/*
			 * People Table - add authors
			 */
			ImmutableList<String> authorNames = getCleanSplitValues(row, MedlineField.AUTHOR, this.logger);
			ImmutableList<String> fullAuthorNames = getCleanSplitValues(row, MedlineField.FULL_AUTHOR, this.logger);
			List<Person> authors = getPeople(this.peopleTable.getKeyGenerator(),
					authorNames, this.logger, MedlineField.AUTHOR);
			authors.addAll(getPeople(this.peopleTable.getKeyGenerator(), fullAuthorNames,
					this.logger, MedlineField.FULL_AUTHOR));
			for (Person person : authors) {
				this.peopleTable.add(person);
			}

			/*
			 * Authors Table
			 */

			for (Author author : Author.makeAuthors(document, authors)) {
				this.authorsTable.add(author);
			}

			/*
			 * People Table - add editors
			 */
			ImmutableList<String> editorNames = getCleanSplitValues(row, MedlineField.EDITOR_NAME, this.logger);
			ImmutableList<String> fullEditorNames = getCleanSplitValues(row, MedlineField.FULL_EDITOR_NAME, this.logger);
			List<Person> editors = getPeople(this.peopleTable.getKeyGenerator(),
					editorNames, this.logger, MedlineField.EDITOR_NAME);
			editors.addAll(getPeople(this.peopleTable.getKeyGenerator(), fullEditorNames,
					this.logger, MedlineField.FULL_EDITOR_NAME));
			for (Person editor : editors) {
				this.peopleTable.add(editor);
			}

			/*
			 * Editors Table
			 */

			for (Editor editor : Editor.makeEditors(document, editors)) {
				this.editorsTable.add(editor);
			}

			/*
			 * Keywords Table
			 */
			List<Keyword> keywords = getKeywords(
					this.keywordsTable.getKeyGenerator(), row, this.logger);
			for (Keyword keyword : keywords) {
				this.keywordsTable.add(keyword);
			}

			/*
			 * Document Keywords
			 */
			for (DocumentKeyword documentKeyword : DocumentKeyword
					.makeDocumentKeywords(document, keywords)) {
				this.documentKeywordsTable.add(documentKeyword);
			}

			/*
			 * Addresses
			 */
			List<Address> addresses = getAddresses(
					this.addressesTable.getKeyGenerator(), row, this.logger);
			for (Address address : addresses) {
				this.addressesTable.add(address);
			}

			/*
			 * Research Addresses
			 */
			for (ResearchAddress researchAddress : ResearchAddress
					.makeResearchAddresses(document, addresses)) {
				this.researchAddressesTable.add(researchAddress);
			}
		}

		return new DatabaseModel(this.dbTables);
	}

	protected static Document getDocument(
			DatabaseTableKeyGenerator keyGenerator, Tuple row, LogService logger) {
		Map<MedlineField, Object> medlineValues = new HashMap<MedlineField, Object>();
		
		for (MedlineField field : DOCUMENT_TO_MEDLINE.values()) {
			medlineValues.put(field, getCleanValue(row, field, logger));
		}
		
		return makeDocument(keyGenerator, medlineValues, logger);
	}

	protected static List<Address> getAddresses(
			DatabaseTableKeyGenerator keyGenerator, Tuple row, LogService logger) {

		return makeAddresses(keyGenerator, getCleanSplitValues(row, MedlineField.AFFILIATION, logger));
	}

	protected static ImmutableList<Keyword> getKeywords(
			DatabaseTableKeyGenerator keyGenerator, Tuple row, LogService logger) {
		final String PUBMED_KEYWORD_PREFIX = "pubmed ";
		
		ImmutableList<Keyword> originalKeywords = makeKeywords(getCleanSplitValues(row, MedlineField.MESH_TERMS, logger), PUBMED_KEYWORD_PREFIX
				+ MedlineField.MESH_TERMS.getName(), keyGenerator);
		
		ImmutableList<Keyword> newKeywords = makeKeywords(getCleanSplitValues(row, MedlineField.OTHER_TERM, logger), PUBMED_KEYWORD_PREFIX
				+ MedlineField.OTHER_TERM.getName(), keyGenerator);

		return new ImmutableList.Builder<Keyword>().addAll(originalKeywords).addAll(newKeywords).build();
	}

	protected static Source getSource(DatabaseTableKeyGenerator keyGenerator,
			Tuple row, LogService logger) {
		Map<MedlineField, Object> medlineValues = new HashMap<MedlineField, Object>();
		
		for (MedlineField field : SOURCE_TO_MEDLINE.values()) {
			medlineValues.put(field, getCleanValue(row, field, logger));
		}
		
		return makeSource(keyGenerator, medlineValues, logger);
	}

	/**
	 * Try to get the {@link MedlineField}'s value from the {@code row}.
	 * 
	 * @return The {@link String} value of the row or {@code ""} if nothing is
	 *         found.
	 */
	protected static String getCleanValue(Tuple row, MedlineField medlineField,
			LogService logger) {
		if (!row.canGet(medlineField.getField(), medlineField.getFieldType())) {
			logger.log(
					LogService.LOG_ERROR,
					"The field '"
							+ medlineField.getField()
							+ "' ("
							+ medlineField.getName()
							+ ") was missing from all the medline records.  This might cause problems with the output database.");
			return "";
		}

		Object value = row.get(medlineField.getField());
		if (value == null) {
			logger.log(
					LogService.LOG_WARNING,
					"The medline file being processed has an entry that has no value for '"
							+ medlineField.getField()
							+ "'.  This might cause problems with the output table.");

		}
		return cleanString(removeArrayWrapper(value, ""));
	}

	/**
	 * Get multi-value {@link MedlineField} {@code String} values from the
	 * {@code row}.
	 * 
	 * @param medlineField
	 *            A multi-value {@link MedlineField} (e.g.
	 *            {@link MedlineField.Author}).
	 * @return An {@link ImmutableList} of {@code String}s representing the
	 *         values from the row. If no value was found, an empty list will be
	 *         returned.
	 */
	protected static ImmutableList<String> getCleanSplitValues(Tuple row,
			MedlineField medlineField, LogService logger) {
		String cleanValue = getCleanValue(row, medlineField, logger);
		if (cleanValue.isEmpty()) {
			return ImmutableList.of();
		}
		return ImmutableList.copyOf(Splitter.on(
				MedlineField.MEDLINE_MULTI_VALUE_SEPERATOR).split(cleanValue));
	}

}
