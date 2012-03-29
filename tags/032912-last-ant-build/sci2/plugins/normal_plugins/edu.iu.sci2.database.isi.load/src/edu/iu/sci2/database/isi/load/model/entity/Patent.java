package edu.iu.sci2.database.isi.load.model.entity;

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

public class Patent extends Entity<Patent> {
	public static final Schema<Patent> SCHEMA = new Schema<Patent>(
		true,
		ISI.PATENT_NUMBER, DerbyFieldType.TEXT);

	private String patentNumber;

	public Patent(DatabaseTableKeyGenerator keyGenerator, String patentNumber) {
		super(keyGenerator, createAttributes(patentNumber));
		this.patentNumber = patentNumber;
	}

	public String getPatentNumber() {
		return this.patentNumber;
	}

	@Override
	public Object createMergeKey() {
		return StringUtilities.alternativeIfNotNull_Empty_OrWhitespace(
			this.patentNumber, getPrimaryKey());
	}

	@Override
	public void merge(Patent otherPatent) {
	}

	public static Dictionary<String, Object> createAttributes(String patentNumber) {
		Dictionary<String, Object> attributes = new Hashtable<String, Object>();
		DictionaryUtilities.addIfNotNull(
			attributes,
			new DictionaryEntry<String, Object>(ISI.PATENT_NUMBER, patentNumber));

		return attributes;
	}
}