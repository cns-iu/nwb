package edu.iu.scipolicy.database.isi.loader.model.entity;

import java.util.Dictionary;
import java.util.Hashtable;

import org.cishell.utilities.StringUtilities;

import edu.iu.cns.database.loader.framework.Entity;
import edu.iu.cns.database.loader.framework.Schema;
import edu.iu.cns.database.loader.framework.DerbyFieldType;
import edu.iu.cns.database.loader.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.nwb.shared.isiutil.database.ISIDatabase;

public class Keyword extends Entity<Keyword> implements Comparable<Keyword> {
	public static final Schema<Keyword> SCHEMA = new Schema<Keyword>(
		true,
		ISIDatabase.KEYWORD, DerbyFieldType.TEXT,
		ISIDatabase.TYPE, DerbyFieldType.TEXT);

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

	public int compareTo(Keyword otherKeyword) {
		return new Integer(getPrimaryKey()).compareTo(new Integer(otherKeyword.getPrimaryKey()));
	}

	public boolean shouldMerge(Keyword otherKeyword) {
		return (
			StringUtilities.validAndEquivalent(this.keyword, otherKeyword.getKeyword()) &&
			StringUtilities.validAndEquivalent(this.type, otherKeyword.getType()));
	}

	public void merge(Keyword otherKeyword) {
		this.keyword = StringUtilities.simpleMerge(this.keyword, otherKeyword.getKeyword());
		this.type = StringUtilities.simpleMerge(this.type, otherKeyword.getType());
	}

	public static Dictionary<String, Comparable<?>> createAttributes(String keyword, String type) {
		Dictionary<String, Comparable<?>> attributes = new Hashtable<String, Comparable<?>>();
		attributes.put(ISIDatabase.KEYWORD, keyword);
		attributes.put(ISIDatabase.TYPE, type);

		return attributes;
	}
}