package edu.iu.scipolicy.database.isi.loader.model.entity;

import java.util.Dictionary;
import java.util.Hashtable;

import org.cishell.utilities.StringUtilities;

import edu.iu.cns.database.loader.framework.Entity;
import edu.iu.cns.database.loader.framework.Schema;
import edu.iu.cns.database.loader.framework.DerbyFieldType;
import edu.iu.cns.database.loader.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.nwb.shared.isiutil.database.ISI;

public class Publisher extends Entity<Publisher> implements Comparable<Publisher> {
	public final static Schema<Publisher> SCHEMA = new Schema<Publisher>(
		true,
		ISI.PUBLISHER_CITY, DerbyFieldType.TEXT,
		ISI.PUBLISHER_NAME, DerbyFieldType.TEXT,
		ISI.PUBLISHER_SOURCE, DerbyFieldType.FOREIGN_KEY,
		ISI.PUBLISHER_WEB_ADDRESS, DerbyFieldType.TEXT).
		FOREIGN_KEYS(ISI.PUBLISHER_SOURCE, ISI.SOURCE_TABLE_NAME);

	private String city;
	private String name;
	private Source source;
	private String webAddress;

	public Publisher(
			DatabaseTableKeyGenerator keyGenerator, String city, String name, String webAddress) {
		super(
			keyGenerator,
			createAttributes(city, name, webAddress));
		this.city = city;
		this.name = name;
		this.webAddress = webAddress;
	}

	public String getCity() {
		return this.city;
	}

	public String getName() {
		return this.name;
	}

	public Source getSource() {
		return this.source;
	}

	public String getWebAddress() {
		return this.webAddress;
	}

	public void setSource(Source source) {
		this.source = source;
		// TODO: Figure out a better way to do this?
		getAttributes().put(ISI.PUBLISHER_SOURCE, this.source.getPrimaryKey());
	}

	public int compareTo(Publisher otherPublisher) {
		// TODO:
		return -1;
	}


	public boolean shouldMerge(Publisher otherPublisher) {
		boolean namesAndCitiesAreEquivalent = (
			StringUtilities.validAndEquivalentIgnoreCase(this.city, otherPublisher.getCity()) &&
			StringUtilities.validAndEquivalentIgnoreCase(this.name, otherPublisher.getName()));
		boolean webAddressesAreEquivalent = StringUtilities.validAndEquivalentIgnoreCase(
			this.webAddress, otherPublisher.getWebAddress());

		return (namesAndCitiesAreEquivalent || webAddressesAreEquivalent);
	}

	public void merge(Publisher otherPublisher) {
		this.city = StringUtilities.simpleMerge(this.city, otherPublisher.getCity());
		this.name = StringUtilities.simpleMerge(this.name, otherPublisher.getName());
		this.webAddress =
			StringUtilities.simpleMerge(this.webAddress, otherPublisher.getWebAddress());
	}

	//TODO: Put in publisher source
	public static Dictionary<String, Comparable<?>> createAttributes(
			String city, String name, String webAddress) {
		Dictionary<String, Comparable<?>> attributes = new Hashtable<String, Comparable<?>>();
		attributes.put(ISI.PUBLISHER_CITY, city);
		attributes.put(ISI.PUBLISHER_NAME, name);
		attributes.put(ISI.PUBLISHER_WEB_ADDRESS, webAddress);

		return attributes;
	}
}