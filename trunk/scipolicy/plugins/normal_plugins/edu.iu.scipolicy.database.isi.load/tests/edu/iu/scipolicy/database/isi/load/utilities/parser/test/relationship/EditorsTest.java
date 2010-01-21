package edu.iu.scipolicy.database.isi.load.utilities.parser.test.relationship;


import java.util.List;

import org.junit.Test;

import edu.iu.cns.database.loader.framework.RowItemContainer;
import edu.iu.cns.database.loader.framework.utilities.DatabaseModel;
import edu.iu.nwb.shared.isiutil.database.ISI;
import edu.iu.scipolicy.database.isi.load.model.entity.Document;
import edu.iu.scipolicy.database.isi.load.model.entity.Person;
import edu.iu.scipolicy.database.isi.load.model.entity.relationship.Editor;
import edu.iu.scipolicy.database.isi.load.utilities.parser.RowItemTest;
import edu.iu.scipolicy.database.isi.load.utilities.parser.test.entity.DocumentTest;
import edu.iu.scipolicy.database.isi.load.utilities.parser.test.entity.PersonTest;

public class EditorsTest extends RowItemTest {
	// Editor test data.
	public static final String ONE_EDITOR_TEST_DATA_PATH = BASE_TEST_DATA_PATH + "OneEditor.isi";
	public static final String MULTIPLE_EDITORS_TEST_DATA_PATH =
		BASE_TEST_DATA_PATH + "MultipleEditors.isi";

	public static final String FIRST_DOCUMENT_TITLE =
		"Planarians Maintain a Constant Ratio of Different Cell Types During " +
		"Changes in Body Size by Using the Stem Cell System";
	public static final String SECOND_DOCUMENT_TITLE =
		"Minimum spanning trees of weighted scale-free networks";

	@Test
	public void testNoEditorsWereParsed() throws Exception {
		DatabaseModel model = parseTestData(EMPTY_TEST_DATA_PATH);
		RowItemContainer<Editor> editors = model.getRowItemListOfTypeByDatabaseTableName(
			ISI.EDITORS_TABLE_NAME);

		checkItemContainerValidity(editors, "editors");
		checkItemCount(editors, 0);
	}

	@Test
	public void testOneEditorWasParsed() throws Exception {
		DatabaseModel model = parseTestData(ONE_EDITOR_TEST_DATA_PATH);
		RowItemContainer<Document> documents = model.getRowItemListOfTypeByDatabaseTableName(
			ISI.DOCUMENT_TABLE_NAME);
		RowItemContainer<Person> people = model.getRowItemListOfTypeByDatabaseTableName(
			ISI.PERSON_TABLE_NAME);
		RowItemContainer<Editor> editors = model.getRowItemListOfTypeByDatabaseTableName(
			ISI.EDITORS_TABLE_NAME);

		checkItemContainerValidity(documents, "documents");
		checkItemCount(documents, 1);

		checkItemContainerValidity(people, "people");
		checkItemCount(people, 1);

		checkItemContainerValidity(editors, "editors");
		checkItemCount(editors, 1);

		Document firstDocument = (Document)documents.getItems().get(0);
		Person firstEditorPerson = (Person)people.getItems().get(0);
		Editor firstEditor = (Editor)editors.getItems().get(0);

		checkEditor(firstEditor, firstDocument, firstEditorPerson);
		
		System.err.println(firstEditorPerson.getUnsplitAbbreviatedName());
	}

	@Test
	public void testMultipleEditorsWereParsed() throws Exception {
		DatabaseModel model = parseTestData(MULTIPLE_EDITORS_TEST_DATA_PATH);
		RowItemContainer<Document> documents = model.getRowItemListOfTypeByDatabaseTableName(
			ISI.DOCUMENT_TABLE_NAME);
		RowItemContainer<Person> people = model.getRowItemListOfTypeByDatabaseTableName(
			ISI.PERSON_TABLE_NAME);
		RowItemContainer<Editor> editors = model.getRowItemListOfTypeByDatabaseTableName(
			ISI.EDITORS_TABLE_NAME);

		checkItemContainerValidity(documents, "documents");
		checkItemCount(documents, 1);
		List<Document> documentItems = (List<Document>)documents.getItems();

		checkItemContainerValidity(people, "people");
		checkItemCount(people, 2);
		List<Person> personItems = (List<Person>)people.getItems();

		checkItemContainerValidity(editors, "authors");
		checkItemCount(editors, 2);
		List<Editor> editorItems = (List<Editor>)editors.getItems();

		Document firstDocument = getFirstDocument(documentItems);
		Person firstEditorPerson = getFirstEditorPerson(personItems);
		Editor firstEditor = getEditor(editorItems, firstDocument, firstEditorPerson);
		Person secondEditorPerson = getSecondEditorPerson(personItems);
		Editor secondEditor = getEditor(editorItems, firstDocument, secondEditorPerson);

		checkEditor(firstEditor, firstDocument, firstEditorPerson, 0);
		checkEditor(secondEditor, firstDocument, secondEditorPerson, 1);
	}

	// TODO: Test Editor.

	public static Editor getEditor(List<Editor> editors, Document document, Person editorPerson) {
		for (Editor editor : editors) {
			if ((editor.getDocument() == document) && (editor.getPerson() == editorPerson)) {
				return editor;
			}
		}

		return null;
	}

	public static void checkEditor(
			Editor editor, Document providedDocument, Person providedEditorPerson) {
		Document editorDocument = editor.getDocument();
		Person editorPerson = editor.getPerson();

		DocumentTest.checkDocuments(editorDocument, providedDocument, "Editor");
		PersonTest.checkPeople(editorPerson, providedEditorPerson, "Editor");
	}

	public static void checkEditor(
			Editor editor,
			Document providedDocument,
			Person providedAuthorPerson,
			int orderListed) {
		checkEditor(editor, providedDocument, providedAuthorPerson);
		checkItemOrderListed(editor, orderListed);
	}

	private Document getFirstDocument(List<Document> documents) {
		return DocumentTest.getDocument(documents, FIRST_DOCUMENT_TITLE);
	}

	private Person getFirstEditorPerson(List<Person> people) throws Exception {
		return PersonTest.getPerson(
			people,
			"",
			"Macdonald",
			"P",
			"",
			"J",
			"",
			"Macdonald, PJ");
	}

	private Person getSecondEditorPerson(List<Person> people) throws Exception {
		return PersonTest.getPerson(
			people,
			"",
			"Almaas",
			"E",
			"",
			"",
			"",
			"Almaas, E");
	}
}
