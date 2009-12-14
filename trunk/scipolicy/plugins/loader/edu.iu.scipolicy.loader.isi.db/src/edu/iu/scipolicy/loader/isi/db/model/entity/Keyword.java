package edu.iu.scipolicy.loader.isi.db.model.entity;

import java.util.Dictionary;
import java.util.Hashtable;


import edu.iu.cns.database.loader.framework.Entity;
import edu.iu.cns.database.loader.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.scipolicy.loader.isi.db.ISIDatabase;

public class Keyword extends Entity {
	private String type;
	private String keyword;

	public Keyword(DatabaseTableKeyGenerator keyGenerator, String type, String keyword) {
		super(keyGenerator);
		this.type = type;
		this.keyword = keyword;
	}

	public String getType() {
		return this.type;
	}

	public String getKeyword() {
		return this.keyword;
	}

	public Dictionary<String, Object> createAttributes() {
		Dictionary<String, Object> attributes = new Hashtable<String, Object>();
		attributes.put(ISIDatabase.TYPE, this.type);
		attributes.put(ISIDatabase.KEYWORD, this.keyword);

		return attributes;
	}
}