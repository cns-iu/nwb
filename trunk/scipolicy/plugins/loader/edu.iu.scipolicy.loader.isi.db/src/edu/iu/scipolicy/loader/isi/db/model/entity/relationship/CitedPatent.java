package edu.iu.scipolicy.loader.isi.db.model.entity.relationship;

import java.util.Dictionary;
import java.util.Hashtable;

import edu.iu.cns.database.loader.framework.RowItem;
import edu.iu.scipolicy.loader.isi.db.model.entity.Document;
import edu.iu.scipolicy.loader.isi.db.model.entity.Patent;

public class CitedPatent extends RowItem<CitedPatent> {
	private Document document;
	private Patent patent;

	public CitedPatent(Document document, Patent patent) {
		super(createAttributes());
		this.document = document;
		this.patent = patent;
	}

	public Document getDocument() {
		return this.document;
	}

	public Patent getPatent() {
		return this.patent;
	}

	public boolean shouldMerge(CitedPatent otherCitedPatent) {
		return false;
	}

	public void merge(CitedPatent otherCitedPatent) {
	}

	public static Dictionary<String, Comparable<?>> createAttributes() {
		return new Hashtable<String, Comparable<?>>();
	}
}