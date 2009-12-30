package edu.iu.scipolicy.loader.isi.db.model.entity;

import java.util.Dictionary;
import java.util.Hashtable;

import org.cishell.utilities.StringUtilities;

import edu.iu.cns.database.loader.framework.Entity;
import edu.iu.cns.database.loader.framework.Schema;
import edu.iu.cns.database.loader.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.nwb.shared.isiutil.database.ISIDatabase;

public class Keyword extends Entity<Keyword> {
	public static final Schema<Keyword> SCHEMA = new Schema<Keyword>(
		ISIDatabase.TYPE, Schema.TEXT_CLASS,
		ISIDatabase.KEYWORD, Schema.TEXT_CLASS);

	private String type;
	private String keyword;

	public Keyword(DatabaseTableKeyGenerator keyGenerator, String type, String keyword) {
		super(keyGenerator, createAttributes(type, keyword));
		this.type = type;
		this.keyword = keyword;
	}

	public String getType() {
		return this.type;
	}

	public String getKeyword() {
		return this.keyword;
	}

	public boolean shouldMerge(Keyword otherKeyword) {
		return (
			StringUtilities.validAndEquivalent(this.type, otherKeyword.getType()) &&
			StringUtilities.validAndEquivalent(this.keyword, otherKeyword.getKeyword()));
	}

	public void merge(Keyword otherKeyword) {
		this.type = StringUtilities.simpleMerge(this.type, otherKeyword.getType());
		this.keyword = StringUtilities.simpleMerge(this.keyword, otherKeyword.getKeyword());
	}

	public static Dictionary<String, Comparable<?>> createAttributes(String type, String keyword) {
		Dictionary<String, Comparable<?>> attributes = new Hashtable<String, Comparable<?>>();
		attributes.put(ISIDatabase.TYPE, type);
		attributes.put(ISIDatabase.KEYWORD, keyword);

		return attributes;
	}
}