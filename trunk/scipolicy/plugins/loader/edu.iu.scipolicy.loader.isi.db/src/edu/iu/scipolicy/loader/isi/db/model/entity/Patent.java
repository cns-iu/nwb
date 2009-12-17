package edu.iu.scipolicy.loader.isi.db.model.entity;

import java.util.Dictionary;
import java.util.Hashtable;

import edu.iu.cns.database.loader.framework.Entity;
import edu.iu.cns.database.loader.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.scipolicy.loader.isi.db.ISIDatabase;

public class Patent extends Entity<Patent> {
	private String patentNumber;

	public Patent(DatabaseTableKeyGenerator keyGenerator, String patentNumber) {
		super(keyGenerator, createAttributes(patentNumber));
		this.patentNumber = patentNumber;
	}

	public String getPatentNumber() {
		return this.patentNumber;
	}

	public Patent merge(Patent otherPatent) {
		// TODO: Implement this.
		return otherPatent;
	}

	public static Dictionary<String, Comparable<?>> createAttributes(String patentNumber) {
		Dictionary<String, Comparable<?>> attributes = new Hashtable<String, Comparable<?>>();
		attributes.put(ISIDatabase.PATENT_NUMBER, patentNumber);

		return attributes;
	}
}