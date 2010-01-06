package edu.iu.scipolicy.loader.isi.db.model.entity;

import java.util.Dictionary;
import java.util.Hashtable;

import org.cishell.utilities.StringUtilities;

import edu.iu.cns.database.loader.framework.Entity;
import edu.iu.cns.database.loader.framework.Schema;
import edu.iu.cns.database.loader.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.nwb.shared.isiutil.database.ISIDatabase;

public class Address extends Entity<Address> implements Comparable<Address> {
	public static final Schema<Address> SCHEMA = new Schema<Address>(
		true,
		ISIDatabase.ADDRESS_CITY, Schema.TEXT_CLASS,
		ISIDatabase.COUNTRY, Schema.TEXT_CLASS,
		ISIDatabase.POSTAL_CODE, Schema.TEXT_CLASS,
		ISIDatabase.RAW_ADDRESS, Schema.TEXT_CLASS,
		ISIDatabase.STATE_OR_PROVINCE, Schema.TEXT_CLASS,
		ISIDatabase.STREET_ADDRESS, Schema.TEXT_CLASS);

	private String city;
	private String country;
	private String postalCode;
	private String rawAddress;
	private String stateOrProvince;
	private String streetAddress;

	public Address(DatabaseTableKeyGenerator keyGenerator, String rawAddress) {
		this(keyGenerator, "", "", "", "", "", rawAddress);
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
		return StringUtilities.validAndEquivalentIgnoreCase(
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
		attributes.put(ISIDatabase.ADDRESS_CITY, city);
		attributes.put(ISIDatabase.COUNTRY, country);
		attributes.put(ISIDatabase.POSTAL_CODE, postalCode);
		attributes.put(ISIDatabase.RAW_ADDRESS, rawAddress);
		attributes.put(ISIDatabase.STATE_OR_PROVINCE, stateOrProvince);
		attributes.put(ISIDatabase.STREET_ADDRESS, streetAddress);

		return attributes;
	}
}