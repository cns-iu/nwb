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

public class Publisher extends Entity<Publisher> {
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

	public boolean shouldMerge(Publisher otherPublisher) {
		boolean namesAndCitiesAreEquivalent = (
			StringUtilities.areValidAndEqualIgnoreCase(this.city, otherPublisher.getCity()) &&
			StringUtilities.areValidAndEqualIgnoreCase(this.name, otherPublisher.getName()));
		boolean webAddressesAreEquivalent = StringUtilities.areValidAndEqualIgnoreCase(
			this.webAddress, otherPublisher.getWebAddress());

		return (namesAndCitiesAreEquivalent || webAddressesAreEquivalent);
	}

	public void merge(Publisher otherPublisher) {
		this.city = StringUtilities.simpleMerge(this.city, otherPublisher.getCity());
		this.name = StringUtilities.simpleMerge(this.name, otherPublisher.getName());
		this.webAddress =
			StringUtilities.simpleMerge(this.webAddress, otherPublisher.getWebAddress());
	}

	public static Dictionary<String, Object> createAttributes(
			String city, String name, String webAddress) {
		Dictionary<String, Object> attributes = new Hashtable<String, Object>();
		DictionaryUtilities.addIfNotNull(
			attributes,
			new DictionaryEntry<String, Object>(ISI.PUBLISHER_CITY, city),
			new DictionaryEntry<String, Object>(ISI.PUBLISHER_NAME, name),
			new DictionaryEntry<String, Object>(ISI.PUBLISHER_WEB_ADDRESS, webAddress));
		/*attributes.put(ISI.PUBLISHER_CITY, city);
		attributes.put(ISI.PUBLISHER_NAME, name);
		attributes.put(ISI.PUBLISHER_WEB_ADDRESS, webAddress);*/

		return attributes;
	}
}