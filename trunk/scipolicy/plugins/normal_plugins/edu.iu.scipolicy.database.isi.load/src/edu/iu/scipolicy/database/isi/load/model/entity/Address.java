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

public class Address extends Entity<Address> implements Comparable<Address> {
	public static final Schema<Address> SCHEMA = new Schema<Address>(
		true,
		ISI.ADDRESS_CITY, DerbyFieldType.TEXT,
		ISI.COUNTRY, DerbyFieldType.TEXT,
		ISI.POSTAL_CODE, DerbyFieldType.TEXT,
		ISI.RAW_ADDRESS, DerbyFieldType.TEXT,
		ISI.STATE_OR_PROVINCE, DerbyFieldType.TEXT,
		ISI.STREET_ADDRESS, DerbyFieldType.TEXT);

	private String city;
	private String country;
	private String postalCode;
	private String rawAddress;
	private String stateOrProvince;
	private String streetAddress;

	// TODO Change to nulls
	public Address(DatabaseTableKeyGenerator keyGenerator, String rawAddress) {
		this(keyGenerator, null, null, null, rawAddress, null, null);
	}

	public Address(
			DatabaseTableKeyGenerator keyGenerator,
			String city,
			String country,
			String postalCode,
			String rawAddress,
			String stateOrProvince,
			String streetAddress) {
		super(
			keyGenerator,
			createAttributes(
				city, country, postalCode, rawAddress, stateOrProvince, streetAddress));
		this.city = city;
		this.country = country;
		this.postalCode = postalCode;
		this.rawAddress = rawAddress;
		this.stateOrProvince = stateOrProvince;
		this.streetAddress = streetAddress;
	}

	public String getCity() {
		return this.city;
	}

	public String getCountry() {
		return this.country;
	}

	public String getPostalCode() {
		return this.postalCode;
	}

	public String getRawAddress() {
		return this.rawAddress;
	}

	public String getStateOrProvince() {
		return this.stateOrProvince;
	}

	public String getStreetAddress() {
		return this.streetAddress;
	}

	public int compareTo(Address otherAddress) {
		// TODO:
		return -1;
	}

	public boolean shouldMerge(Address otherAddress) {
		return StringUtilities.areValidAndEqualIgnoreCase(
			this.rawAddress, otherAddress.getRawAddress());
	}

	public void merge(Address otherAddress) {
		this.city = StringUtilities.simpleMerge(this.city, otherAddress.getCity());
		this.country = StringUtilities.simpleMerge(this.country, otherAddress.getCountry());
		this.postalCode =
			StringUtilities.simpleMerge(this.postalCode, otherAddress.getPostalCode());
		this.stateOrProvince =
			StringUtilities.simpleMerge(this.stateOrProvince, otherAddress.getStateOrProvince());
		this.streetAddress =
			StringUtilities.simpleMerge(this.streetAddress, otherAddress.getStreetAddress());
	}

	public static Dictionary<String, Comparable<?>> createAttributes(
			String city,
			String country,
			String postalCode,
			String rawAddress,
			String streetAddress,
			String stateOrProvince) {
		Dictionary<String, Comparable<?>> attributes = new Hashtable<String, Comparable<?>>();
		DictionaryUtilities.addIfNotNull(
			attributes,
			new DictionaryEntry<String, Comparable<?>>(ISI.ADDRESS_CITY, city),
			new DictionaryEntry<String, Comparable<?>>(ISI.COUNTRY, country),
			new DictionaryEntry<String, Comparable<?>>(ISI.POSTAL_CODE, postalCode),
			new DictionaryEntry<String, Comparable<?>>(ISI.RAW_ADDRESS, rawAddress),
			new DictionaryEntry<String, Comparable<?>>(ISI.STATE_OR_PROVINCE, stateOrProvince),
			new DictionaryEntry<String, Comparable<?>>(ISI.STREET_ADDRESS, streetAddress));
		
		/*attributes.put(ISI.ADDRESS_CITY, city);
		attributes.put(ISI.COUNTRY, country);
		attributes.put(ISI.POSTAL_CODE, postalCode);
		attributes.put(ISI.RAW_ADDRESS, rawAddress);
		attributes.put(ISI.STATE_OR_PROVINCE, stateOrProvince);
		attributes.put(ISI.STREET_ADDRESS, streetAddress);*/

		return attributes;
	}
}