package edu.iu.scipolicy.loader.isi.db.model.entity;

import java.util.Dictionary;
import java.util.Hashtable;

import edu.iu.cns.database.loader.framework.Entity;
import edu.iu.cns.database.loader.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.nwb.shared.isiutil.database.ISIDatabase;

public class Publisher extends Entity<Publisher> {
	private String name;
	private String city;
	private String webAddress;
	private Source source;

	public Publisher(
			DatabaseTableKeyGenerator keyGenerator, String name, String city, String webAddress) {
		super(
			keyGenerator,
			createAttributes(name, city, webAddress));
		this.name = name;
		this.city = city;
		this.webAddress = webAddress;
	}

	public String getName() {
		return this.name;
	}

	public String getCity() {
		return this.city;
	}

	public String getWebAddress() {
		return this.webAddress;
	}

	public Source getSource() {
		return this.source;
	}

	public void setSource(Source source) {
		this.source = source;
		// TODO: Figure out a better way to do this?
		getAttributes().put(ISIDatabase.PUBLISHER_SOURCE, this.source);
	}

	public boolean equals(Entity otherEntity) {
		if (this == otherEntity) {
			return true;
		} else if (!(otherEntity instanceof Publisher)) {
			return false;
		}

		// TODO: Compare by ISSN?
		return super.equals(otherEntity);
	}

	public Publisher merge(Publisher otherPublisher) {
		// TODO: Implement this.
		return this;
	}

	public static Dictionary<String, Comparable<?>> createAttributes(
			String name, String city, String webAddress) {
		Dictionary<String, Comparable<?>> attributes = new Hashtable<String, Comparable<?>>();
		attributes.put(ISIDatabase.NAME, name);
		attributes.put(ISIDatabase.PUBLISHER_CITY, city);
		attributes.put(ISIDatabase.WEB_ADDRESS, webAddress);

		return attributes;
	}
}