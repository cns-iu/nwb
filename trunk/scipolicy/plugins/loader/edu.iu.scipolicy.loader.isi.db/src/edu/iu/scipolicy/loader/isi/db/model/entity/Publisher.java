package edu.iu.scipolicy.loader.isi.db.model.entity;

import java.util.Dictionary;
import java.util.Hashtable;


import edu.iu.cns.database.loader.framework.Entity;
import edu.iu.cns.database.loader.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.scipolicy.loader.isi.db.ISIDatabase;

public class Publisher extends Entity {
	private String name;
	private String city;
	private String webAddress;

	public Publisher(
			DatabaseTableKeyGenerator keyGenerator, String name, String city, String webAddress) {
		super(keyGenerator);
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

	public Dictionary<String, Object> createAttributes() {
		Dictionary<String, Object> attributes = new Hashtable<String, Object>();
		attributes.put(ISIDatabase.NAME, this.name);
		attributes.put(ISIDatabase.PUBLISHER_CITY, this.city);
		attributes.put(ISIDatabase.WEB_ADDRESS, this.webAddress);

		return attributes;
	}
}