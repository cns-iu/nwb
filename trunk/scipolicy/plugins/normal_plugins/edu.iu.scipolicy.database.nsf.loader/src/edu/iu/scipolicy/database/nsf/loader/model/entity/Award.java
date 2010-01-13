package edu.iu.scipolicy.database.nsf.loader.model.entity;

import java.util.Date;
import java.util.Dictionary;
import java.util.Hashtable;

import org.cishell.utilities.StringUtilities;

import edu.iu.cns.database.loader.framework.DerbyFieldType;
import edu.iu.cns.database.loader.framework.Entity;
import edu.iu.cns.database.loader.framework.Schema;
import edu.iu.cns.database.loader.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.scipolicy.utilities.nsf.NSF_Database_FieldNames;

public class Award extends Entity<Award> implements Comparable<Award> {
	public static final Schema<Award> SCHEMA = new Schema<Award>(
			true,
			NSF_Database_FieldNames.AWARD_NUMBER, DerbyFieldType.TEXT,
			NSF_Database_FieldNames.TITLE, DerbyFieldType.TEXT,
			NSF_Database_FieldNames.START_DATE, DerbyFieldType.DATE,
			NSF_Database_FieldNames.RAW_START_DATE, DerbyFieldType.TEXT,
			NSF_Database_FieldNames.EXPIRATION_DATE, DerbyFieldType.DATE,
			NSF_Database_FieldNames.RAW_EXPIRATION_DATE, DerbyFieldType.TEXT,
			NSF_Database_FieldNames.LAST_AMMENDMENT_DATE, DerbyFieldType.DATE,
			NSF_Database_FieldNames.RAW_LAST_AMMENDMENT_DATE, DerbyFieldType.TEXT,
			NSF_Database_FieldNames.AWARDED_AMOUNT_TO_DATE, DerbyFieldType.DOUBLE,
			NSF_Database_FieldNames.RAW_AWARDED_AMOUNT_TO_DATE, DerbyFieldType.TEXT,
			NSF_Database_FieldNames.AWARD_INSTRUMENT, DerbyFieldType.TEXT,
			NSF_Database_FieldNames.NSF_DIRECTORATE, DerbyFieldType.TEXT,
			NSF_Database_FieldNames.NSF_ORGANIZATION, DerbyFieldType.TEXT,
			NSF_Database_FieldNames.ABSTRACT_TEXT, DerbyFieldType.TEXT
			);
	
	private String awardNumber;
	private String title;
	private Date startDate;
	private Date expirationDate;
	private Date lastAmmendmentDate;
	private double awardedAmountToDate;
	private String awardInstrument;
	private String nSFDirectorate;
	private String nSFOrganization;
	private String abstractText;
	private String rawStartDate;
	private String rawAwardedAmountToDate;
	private String rawLastAmmendmentDate;
	private String rawExpirationDate;

	public Award(DatabaseTableKeyGenerator keyGenerator,
				 String awardNumber,
				 String title, 
				 Date startDate,
				 String rawStartDate, 
				 Date expirationDate, 
				 String rawExpirationDate, 
				 Date lastAmmendmentDate,
				 String rawLastAmmendmentDate, 
				 double awardedAmountToDate,
				 String rawAwardedAmountToDate,
				 String awardInstrument,
				 String nSFDirectorate, 
				 String nSFOrganization, 
				 String abstractText) {
		super(keyGenerator, createAttributes(awardNumber, 
											 title, 
											 startDate, 
											 rawStartDate, 
											 expirationDate, 
											 rawExpirationDate, 
											 lastAmmendmentDate,
											 rawLastAmmendmentDate, 
											 awardedAmountToDate,
											 rawAwardedAmountToDate, 
											 awardInstrument, 
											 nSFDirectorate, 
											 nSFOrganization, 
											 abstractText));
		this.awardNumber = awardNumber;
		this.title = title;
		this.startDate = startDate;
		this.rawStartDate = rawStartDate;
		this.expirationDate = expirationDate;
		this.rawExpirationDate = rawExpirationDate;
		this.lastAmmendmentDate = lastAmmendmentDate;
		this.rawLastAmmendmentDate = rawLastAmmendmentDate;
		this.awardedAmountToDate = awardedAmountToDate;
		this.rawAwardedAmountToDate = rawAwardedAmountToDate;
		this.awardInstrument = awardInstrument;
		this.nSFDirectorate = nSFDirectorate;
		this.nSFOrganization = nSFOrganization;
		this.abstractText = abstractText;
	}
	
	
	private static Dictionary<String, Comparable<?>> createAttributes(String awardNumber,
													   String title,
													   Date startDate,
													   String rawStartDate, 
													   Date expirationDate,
													   String rawExpirationDate, 
													   Date lastAmmendmentDate,
													   String rawLastAmmendmentDate, 
													   double awardedAmountToDate,
													   String rawAwardedAmountToDate,
													   String awardInstrument,
													   String nSFDirectorate,
													   String nSFOrganization,
													   String abstractText) {
		Dictionary<String, Comparable<?>> attributes = new Hashtable<String, Comparable<?>>();
		attributes.put(NSF_Database_FieldNames.TITLE, title);
		attributes.put(NSF_Database_FieldNames.AWARD_NUMBER, awardNumber);
		
		if (startDate != null) {
			attributes.put(NSF_Database_FieldNames.START_DATE, startDate);
		}
		
		attributes.put(NSF_Database_FieldNames.RAW_START_DATE, rawStartDate);
		
		if (expirationDate != null) {
			attributes.put(NSF_Database_FieldNames.EXPIRATION_DATE, expirationDate);
		}
		
		attributes.put(NSF_Database_FieldNames.RAW_EXPIRATION_DATE, rawExpirationDate);

		if (lastAmmendmentDate != null) {
			attributes.put(NSF_Database_FieldNames.LAST_AMMENDMENT_DATE, lastAmmendmentDate);
		}
		
		attributes.put(NSF_Database_FieldNames.RAW_LAST_AMMENDMENT_DATE, rawLastAmmendmentDate);
		attributes.put(NSF_Database_FieldNames.AWARDED_AMOUNT_TO_DATE, awardedAmountToDate);
		attributes.put(NSF_Database_FieldNames.RAW_AWARDED_AMOUNT_TO_DATE, rawAwardedAmountToDate);
		attributes.put(NSF_Database_FieldNames.AWARD_INSTRUMENT, awardInstrument);
		attributes.put(NSF_Database_FieldNames.NSF_DIRECTORATE, nSFDirectorate);
		attributes.put(NSF_Database_FieldNames.NSF_ORGANIZATION, nSFOrganization);
		attributes.put(NSF_Database_FieldNames.ABSTRACT_TEXT, abstractText);
		
		return attributes;
	}


	public String getAwardNumber() {
		return awardNumber;
	}


	public String getTitle() {
		return title;
	}


	public Date getStartDate() {
		return startDate;
	}


	public Date getExpirationDate() {
		return expirationDate;
	}


	public Date getLastAmmendmentDate() {
		return lastAmmendmentDate;
	}


	public double getAwardedAmountToDate() {
		return awardedAmountToDate;
	}


	public String getAwardInstrument() {
		return awardInstrument;
	}


	public String getNSFDirectorate() {
		return nSFDirectorate;
	}


	public String getNSFOrganization() {
		return nSFOrganization;
	}


	public String getAbstractText() {
		return abstractText;
	}

	public void addArbitraryColumn(String name, String value) {
		if (SCHEMA.findField(name) == null) {
			SCHEMA.addField(name, DerbyFieldType.TEXT);
		}
		getAttributes().put(name, value);
	}

	@Override
	public void merge(Award otherItem) {
		this.rawStartDate = StringUtilities.simpleMerge(this.rawStartDate,
														otherItem.getRawStartDate());
		this.rawExpirationDate = StringUtilities.simpleMerge(this.rawExpirationDate,
															 otherItem.getRawExpirationDate());
		this.rawLastAmmendmentDate = StringUtilities.simpleMerge(
											this.rawLastAmmendmentDate,
											otherItem.getRawLastAmmendmentDate());
		this.rawAwardedAmountToDate = StringUtilities.simpleMerge(
											this.rawAwardedAmountToDate,
											otherItem.getRawAwardedAmountToDate());
		this.awardInstrument = StringUtilities.simpleMerge(this.awardInstrument,
														   otherItem.getAwardInstrument());
		this.nSFDirectorate = StringUtilities.simpleMerge(this.nSFDirectorate,
				   										  otherItem.getNSFDirectorate());	 
		this.nSFOrganization = StringUtilities.simpleMerge(this.nSFOrganization,
					  									   otherItem.getNSFOrganization());	 
		this.abstractText = StringUtilities.simpleMerge(this.abstractText,
					  									otherItem.getAbstractText());
	}


	@Override
	public boolean shouldMerge(Award otherItem) {
		return (
			StringUtilities.validAndEquivalentIgnoreCase(
				this.awardNumber, 
				otherItem.getAwardNumber())
			&& StringUtilities.validAndEquivalentIgnoreCase(
					this.title, 
					otherItem.getTitle()) 
		);
	}


	public int compareTo(Award o) {
		// TODO Auto-generated method stub
		return 0;
	}


	public String getRawStartDate() {
		return rawStartDate;
	}


	public String getRawAwardedAmountToDate() {
		return rawAwardedAmountToDate;
	}


	public String getRawLastAmmendmentDate() {
		return rawLastAmmendmentDate;
	}


	public String getRawExpirationDate() {
		return rawExpirationDate;
	}
}