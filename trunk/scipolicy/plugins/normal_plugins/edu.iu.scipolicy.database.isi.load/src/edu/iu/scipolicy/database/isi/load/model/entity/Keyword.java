package edu.iu.scipolicy.database.isi.load.model.entity;

import java.util.Dictionary;
import java.util.Hashtable;

import org.cishell.utilities.StringUtilities;
import org.cishell.utilities.dictionary.DictionaryEntry;
import org.cishell.utilities.dictionary.DictionaryUtilities;

import edu.iu.cns.database.load.framework.DerbyFieldType;
import edu.iu.cns.database.load.framework.Entity;
import edu.iu.cns.database.load.framework.Schema;
import edu.iu.cns.database.load.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.nwb.shared.isiutil.database.ISI;

public class Keyword extends Entity<Keyword> {
	public static final Schema<Keyword> SCHEMA = new Schema<Keyword>(
		true,
		ISI.KEYWORD, DerbyFieldType.TEXT,
		ISI.TYPE, DerbyFieldType.TEXT);

	private String keyword;
	private String type;

	public Keyword(DatabaseTableKeyGenerator keyGenerator, String keyword, String type) {
		super(keyGenerator, createAttributes(keyword, type));
		this.keyword = keyword;
		this.type = type;
	}

	public String getKeyword() {
		return this.keyword;
	}

	public String getType() {
		return this.type;
	}

	public boolean shouldMerge(Keyword otherKeyword) {
		return (
			StringUtilities.areValidAndEqual(this.keyword, otherKeyword.getKeyword()) &&
			StringUtilities.areValidAndEqual(this.type, otherKeyword.getType()));
	}

	public void merge(Keyword otherKeyword) {
		this.keyword = StringUtilities.simpleMerge(this.keyword, otherKeyword.getKeyword());
		this.type = StringUtilities.simpleMerge(this.type, otherKeyword.getType());

		DictionaryUtilities.addIfNotNull(
			getAttributes(),
			new DictionaryEntry<String, Object>(ISI.KEYWORD, this.keyword),
			new DictionaryEntry<String, Object>(ISI.TYPE, this.type));
	}

	public static Dictionary<String, Object> createAttributes(String keyword, String type) {
		Dictionary<String, Object> attributes = new Hashtable<String, Object>();
		DictionaryUtilities.addIfNotNull(
			attributes,
			new DictionaryEntry<String, Object>(ISI.KEYWORD, keyword),
			new DictionaryEntry<String, Object>(ISI.TYPE, type));

		return attributes;
	}
}