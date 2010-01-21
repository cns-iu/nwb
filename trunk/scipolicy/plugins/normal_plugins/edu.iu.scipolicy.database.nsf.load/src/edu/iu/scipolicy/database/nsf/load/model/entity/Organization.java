package edu.iu.scipolicy.database.nsf.load.model.entity;

import java.util.Dictionary;
import java.util.Hashtable;

import org.cishell.utilities.StringUtilities;

import edu.iu.cns.database.loader.framework.DerbyFieldType;
import edu.iu.cns.database.loader.framework.Entity;
import edu.iu.cns.database.loader.framework.Schema;
import edu.iu.cns.database.loader.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.scipolicy.utilities.nsf.NSF_Database_FieldNames;

public class Organization extends Entity<Organization> implements Comparable<Organization>{

	public static final Schema<Organization> SCHEMA = new Schema<Organization>(
			true,
			NSF_Database_FieldNames.ORGANIZATION_NAME, DerbyFieldType.TEXT,
			NSF_Database_FieldNames.ORGANIZATION_PHONE, DerbyFieldType.TEXT,
			NSF_Database_FieldNames.ORGANIZATION_STREET_ADDRESS, DerbyFieldType.TEXT,
			NSF_Database_FieldNames.ORGANIZATION_CITY, DerbyFieldType.TEXT,
			NSF_Database_FieldNames.ORGANIZATION_STATE, DerbyFieldType.TEXT,
			NSF_Database_FieldNames.ORGANIZATION_ZIP, DerbyFieldType.TEXT
			);

	private String name;
	private String phone;
	private String streetAddress;
	private String city;
	private String state;
	private String zip;
	
	public Organization(DatabaseTableKeyGenerator keyGenerator,
						String name, 
						String phone, 
						String streetAddress,
						String city, 
						String state, 
						String zip) {
		super(keyGenerator, createAttributes(name, 
											 phone, 
											 streetAddress, 
											 city, 
											 state, 
											 zip));
		this.name = name;
		this.phone = phone;
		this.streetAddress = streetAddress;
		this.city = city;
		this.state = state;
		this.zip = zip;
	}

	private static Dictionary<String, Comparable<?>> createAttributes(String name,
															   String phone,
															   String streetAddress,
															   String city,
															   String state,
															   String zip) {
		Dictionary<String, Comparable<?>> attributes = new Hashtable<String, Comparable<?>>();
		attributes.put(NSF_Database_FieldNames.ORGANIZATION_NAME, name);
		attributes.put(NSF_Database_FieldNames.ORGANIZATION_PHONE, phone);
		attributes.put(NSF_Database_FieldNames.ORGANIZATION_STREET_ADDRESS, streetAddress);
		attributes.put(NSF_Database_FieldNames.ORGANIZATION_CITY, city);
		attributes.put(NSF_Database_FieldNames.ORGANIZATION_STATE, state);
		attributes.put(NSF_Database_FieldNames.ORGANIZATION_ZIP, zip);

		return attributes;
	}

	public String getName() {
		return name;
	}

	public String getPhone() {
		return phone;
	}

	public String getStreetAddress() {
		return streetAddress;
	}

	public String getCity() {
		return city;
	}

	public String getState() {
		return state;
	}

	public String getZip() {
		return zip;
	}

	@Override
	public void merge(Organization otherItem) {
		this.phone = StringUtilities.simpleMerge(this.phone, 
				 								 otherItem.getPhone());	
		this.name = StringUtilities.simpleMerge(this.name, 
				 								 otherItem.getName());
		this.city = StringUtilities.simpleMerge(this.city, 
												otherItem.getCity());
		this.state = StringUtilities.simpleMerge(this.state, 
												 otherItem.getState());
		this.streetAddress = StringUtilities.simpleMerge(this.streetAddress, 
														 otherItem.getStreetAddress());
		this.zip = StringUtilities.simpleMerge(this.zip, 
											   otherItem.getZip());
		
	}

	@Override
	public boolean shouldMerge(Organization otherItem) {
		//TODO: check if comparison of only name, phone, city is enoigh?
		return (
				StringUtilities.validAndEquivalentIgnoreCase(this.name, otherItem.getName())
				&& StringUtilities.validAndEquivalentIgnoreCase(this.phone, otherItem.getPhone())
				&& StringUtilities.validAndEquivalentIgnoreCase(this.city, otherItem.getCity())
		);
	}

	public int compareTo(Organization o) {
		// TODO Auto-generated method stub
		return 0;
	}
}