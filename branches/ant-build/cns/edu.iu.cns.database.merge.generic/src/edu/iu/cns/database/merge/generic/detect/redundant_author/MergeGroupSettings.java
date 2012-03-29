package edu.iu.cns.database.merge.generic.detect.redundant_author;

public class MergeGroupSettings {
	public final String MERGE_TABLE_PERSON_PK_COLUMN;
	public final String MERGE_TABLE_GROUP_COLUMN;
	
	public final String AUTHORS_TABLE;
	public final String AUTHORS_DOCUMENT_FK;
	public final String AUTHORS_PERSON_FK;
	
	
	public final String PERSON_TABLE;
	public final String PERSON_ID;
	public final String PERSON_PK;
	
	public final String DOCUMENT_TABLE;
	public final String DOCUMENT_ID;
	public final String DOCUMENT_PK;

	public MergeGroupSettings(final String MERGE_TABLE_PERSON_PK_COLUMN,
			final String MERGE_TABLE_GROUP_COLUMN, final String AUTHORS_TABLE,
			final String AUTHORS_DOCUMENT_FK, final String AUTHORS_PERSON_FK,
			final String PERSON_TABLE, final String PERSON_ID,
			final String PERSON_PK, final String DOCUMENT_TABLE,
			final String DOCUMENT_ID, final String DOCUMENT_PK) {
		this.MERGE_TABLE_PERSON_PK_COLUMN = MERGE_TABLE_PERSON_PK_COLUMN;
		this.MERGE_TABLE_GROUP_COLUMN = MERGE_TABLE_GROUP_COLUMN;
		this.AUTHORS_TABLE = AUTHORS_TABLE;
		this.AUTHORS_DOCUMENT_FK = AUTHORS_DOCUMENT_FK;
		this.AUTHORS_PERSON_FK = AUTHORS_PERSON_FK;
		this.PERSON_TABLE = PERSON_TABLE;
		this.PERSON_ID = PERSON_ID;
		this.PERSON_PK = PERSON_PK;
		this.DOCUMENT_TABLE = DOCUMENT_TABLE;
		this.DOCUMENT_ID = DOCUMENT_ID;
		this.DOCUMENT_PK = DOCUMENT_PK;
	}

	
}
