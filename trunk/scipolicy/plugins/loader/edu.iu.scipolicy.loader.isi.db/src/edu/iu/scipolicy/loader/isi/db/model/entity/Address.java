package edu.iu.scipolicy.loader.isi.db.model.entity;

import java.util.Dictionary;
import java.util.Hashtable;

import org.cishell.utilities.StringUtilities;

import edu.iu.cns.database.loader.framework.Entity;
import edu.iu.cns.database.loader.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.nwb.shared.isiutil.database.ISIDatabase;

public class Address extends Entity<Address> {
	private String streetAddress;
	private String city;
	private String stateOrProvince;
	private String postalCode;
	private String country;
	private String rawAddress;

	public Address(DatabaseTableKeyGenerator keyGenerator, String rawAddress) {
		this(keyGenerator, "", "", "", "", "", rawAddress);
	}

	public Address(
			DatabaseTableKeyGenerator keyGenerator,
			String streetAddress,
			String city,
			String stateOrProvince,
			String postalCode,
			String country,
			String rawAddress) {
		super(
			keyGenerator,
			createAttributes(
				streetAddress, city, stateOrProvince, postalCode, country, rawAddress));
		this.streetAddress = streetAddress;
		this.city = city;
		this.stateOrProvince = stateOrProvince;
		this.postalCode = postalCode;
		this.country = country;
		this.rawAddress = rawAddress;
	}

	public String getStreetAddress() {
		return this.streetAddress;
	}

	public String getCity() {
		return this.city;
	}

	public String getStateOrProvince() {
		return this.stateOrProvince;
	}

	public String getPostalCode() {
		return this.postalCode;
	}

	public String getCountry() {
		return this.country;
	}

	public String getRawAddress() {
		return this.rawAddress;
	}

	public boolean shouldMerge(Address otherAddress) {
		return StringUtilities.validAndEquivalentIgnoreCase(
			this.rawAddress, otherAddress.getRawAddress());
	}

	public void merge(Address otherAddress) {
		this.streetAddress =
			StringUtilities.simpleMerge(this.streetAddress, otherAddress.getStreetAddress());
		this.city = StringUtilities.simpleMerge(this.city, otherAddress.getCity());
		this.stateOrProvince =
			StringUtilities.simpleMerge(this.stateOrProvince, otherAddress.getStateOrProvince());
		this.postalCode =
			StringUtilities.simpleMerge(this.postalCode, otherAddress.getPostalCode());
		this.country = StringUtilities.simpleMerge(this.country, otherAddress.getCountry());
	}

	public static Dictionary<String, Comparable<?>> createAttributes(
			String streetAddress,
			String city,
			String stateOrProvince,
			String postalCode,
			String country,
			String rawAddress) {
		Dictionary<String, Comparable<?>> attributes = new Hashtable<String, Comparable<?>>();
		attributes.put(ISIDatabase.STREET_ADDRESS, streetAddress);
		attributes.put(ISIDatabase.ADDRESS_CITY, city);
		attributes.put(ISIDatabase.STATE_OR_PROVINCE, stateOrProvince);
		attributes.put(ISIDatabase.POSTAL_CODE, postalCode);
		attributes.put(ISIDatabase.COUNTRY, country);
		attributes.put(ISIDatabase.RAW_ADDRESS, rawAddress);

		return attributes;
	}
}