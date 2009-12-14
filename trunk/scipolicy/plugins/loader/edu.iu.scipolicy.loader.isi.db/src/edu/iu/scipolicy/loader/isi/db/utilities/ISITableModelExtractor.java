package edu.iu.scipolicy.loader.isi.db.utilities;

import java.util.ArrayList;
import java.util.List;

import prefuse.data.Table;
import edu.iu.cns.database.loader.framework.Entity;
import edu.iu.cns.database.loader.framework.EntityRelationshipTableType;
import edu.iu.cns.database.loader.framework.EntityTableType;
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
	public static ISIModel extractModel(Table table) {
		/*
		 * For each type of entity (ISI File, Publisher, Source, Reference, Address, Keyword,
		 *  Person, Patent, and Document), create a master list of entities.
		 */

		EntityTableType<ISIFile> isiFiles = new EntityTableType<ISIFile>(
			ISIDatabase.ISI_FILE_TYPE_NAME, ISIDatabase.ISI_FILE_TABLE_NAME);
		EntityTableType<Publisher> publishers = new EntityTableType<Publisher>(
			ISIDatabase.PUBLISHER_TYPE_NAME, ISIDatabase.PUBLISHER_TABLE_NAME);
		EntityTableType<Source> sources = new EntityTableType<Source>(
			ISIDatabase.SOURCE_TYPE_NAME, ISIDatabase.SOURCE_TABLE_NAME);
		EntityTableType<Reference> references = new EntityTableType<Reference>(
			ISIDatabase.REFERENCE_TYPE_NAME, ISIDatabase.REFERENCE_TABLE_NAME);
		EntityTableType<Address> addresses = new EntityTableType<Address>(
			ISIDatabase.ADDRESS_TYPE_NAME, ISIDatabase.ADDRESS_TABLE_NAME);
		EntityTableType<Keyword> keywords = new EntityTableType<Keyword>(
			ISIDatabase.KEYWORD_TYPE_NAME, ISIDatabase.KEYWORD_TABLE_NAME);
		EntityTableType<Person> people = new EntityTableType<Person>(
			ISIDatabase.PERSON_TYPE_NAME, ISIDatabase.PERSON_TABLE_NAME);
		EntityTableType<Patent> patents = new EntityTableType<Patent>(
			ISIDatabase.PATENT_TYPE_NAME, ISIDatabase.PATENT_TABLE_NAME);
		EntityTableType<Document> documents = new EntityTableType<Document>(
			ISIDatabase.DOCUMENT_TYPE_NAME, ISIDatabase.DOCUMENT_TABLE_NAME);

		/*
		 * Create all of the entity joining tables (Publisher Addresses, Reprint Addresses,
		 *  Research Addresses, Document Keywords, Authors, Editors, Cited Patents, Document
		 *  Occurrences, and Cited References).
		 */

		EntityRelationshipTableType<PublisherAddress> publisherAddresses =
			new EntityRelationshipTableType<PublisherAddress>(
				ISIDatabase.PUBLISHER_ADDRESSES_TYPE_NAME,
				ISIDatabase.PUBLISHER_ADDRESSES_TABLE_NAME);

		EntityRelationshipTableType<ReprintAddress> reprintAddresses =
			new EntityRelationshipTableType<ReprintAddress>(
				ISIDatabase.REPRINT_ADDRESSES_TYPE_NAME,
				ISIDatabase.REPRINT_ADDRESSES_TABLE_NAME);

		EntityRelationshipTableType<ResearchAddress> researchAddresses =
			new EntityRelationshipTableType<ResearchAddress>(
				ISIDatabase.RESEARCH_ADDRESSES_TYPE_NAME,
				ISIDatabase.RESEARCH_ADDRESSES_TABLE_NAME);

		EntityRelationshipTableType<DocumentKeyword> documentKeywords =
			new EntityRelationshipTableType<DocumentKeyword>(
				ISIDatabase.DOCUMENT_KEYWORDS_TYPE_NAME,
				ISIDatabase.DOCUMENT_KEYWORDS_TABLE_NAME);

		EntityRelationshipTableType<Author> authors =
			new EntityRelationshipTableType<Author>(
				ISIDatabase.AUTHORS_TYPE_NAME,
				ISIDatabase.AUTHORS_TABLE_NAME);

		EntityRelationshipTableType<Editor> editors =
			new EntityRelationshipTableType<Editor>(
				ISIDatabase.EDITORS_TYPE_NAME,
				ISIDatabase.EDITORS_TABLE_NAME);

		EntityRelationshipTableType<CitedPatent> citedPatents =
			new EntityRelationshipTableType<CitedPatent>(
				ISIDatabase.CITED_PATENTS_TYPE_NAME,
				ISIDatabase.CITED_PATENTS_TABLE_NAME);

		EntityRelationshipTableType<DocumentOccurrence> documentOccurrences =
			new EntityRelationshipTableType<DocumentOccurrence>(
				ISIDatabase.DOCUMENT_OCCURRENCES_TYPE_NAME,
				ISIDatabase.DOCUMENT_OCCURRENCES_TABLE_NAME);

		EntityRelationshipTableType<CitedReference> citedReferences =
			new EntityRelationshipTableType<CitedReference>(
				ISIDatabase.CITED_REFERENCES_TYPE_NAME,
				ISIDatabase.CITED_REFERENCES_TABLE_NAME);

		// For each record/row in the table:
			// Extract all entities and relationships out of the row.

			/*
			 * Extract ISI File.
			 * Extract Publisher.
			 * Extract Source.
			 * Update references between Publisher and Source.
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

			// For each of the entities just extracted:
				// Check if it is a duplicate.
					/*
					 * If it is, it will be a duplicate with at most one other entity.
					 * Update any references to it to refer to the entity it is duplicate with.
					 * Remove it.
					 */

		// Given all of the master lists of entities, construct an ISIModel and return it.

		// Create new ISIModel, passing in all entity tables and relationship tables.
		
		List<EntityTableType<? extends Entity> > tables =
			new ArrayList<EntityTableType<? extends Entity> >();
		tables.add(isiFiles);
		tables.add(publishers);

		for (EntityTableType<? extends Entity> t : tables) {
			List<? extends Entity> entities = t.getEntities();
			/*String query = formQuery(entities);
			runQuery(query);*/
		}

		return null;
	}
}