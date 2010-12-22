package edu.iu.sci2.database.nsf.load.model.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import org.cishell.utilities.StringUtilities;

import edu.iu.cns.database.load.framework.DerbyFieldType;
import edu.iu.cns.database.load.framework.Entity;
import edu.iu.cns.database.load.framework.Schema;
import edu.iu.cns.database.load.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.sci2.utilities.nsf.NSF_Database_FieldNames;

public class Award extends Entity<Award> {
	public static final Schema<Award> SCHEMA = new Schema<Award>(
			true,
			NSF_Database_FieldNames.AWARD_NUMBER, DerbyFieldType.TEXT,
			NSF_Database_FieldNames.TITLE, DerbyFieldType.TEXT,
			NSF_Database_FieldNames.START_DATE, DerbyFieldType.DATE,
			NSF_Database_FieldNames.RAW_START_DATE, DerbyFieldType.TEXT,
			NSF_Database_FieldNames.EXPIRATION_DATE, DerbyFieldType.DATE,
			NSF_Database_FieldNames.RAW_EXPIRATION_DATE, DerbyFieldType.TEXT,
			NSF_Database_FieldNames.LAST_AMENDMENT_DATE, DerbyFieldType.DATE,
			NSF_Database_FieldNames.RAW_LAST_AMENDMENT_DATE, DerbyFieldType.TEXT,
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
	private Date lastAmendmentDate;
	private double awardedAmountToDate;
	private String awardInstrument;
	private String nSFDirectorate;
	private String nSFOrganization;
	private String abstractText;
	private String rawStartDate;
	private String rawAwardedAmountToDate;
	private String rawLastAmendmentDate;
	private String rawExpirationDate;

	public Award(DatabaseTableKeyGenerator keyGenerator,
				 String awardNumber,
				 String title, 
				 Date startDate,
				 String rawStartDate, 
				 Date expirationDate, 
				 String rawExpirationDate, 
				 Date lastAmendmentDate,
				 String rawLastAmendmentDate, 
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
											 lastAmendmentDate,
											 rawLastAmendmentDate, 
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
		this.lastAmendmentDate = lastAmendmentDate;
		this.rawLastAmendmentDate = rawLastAmendmentDate;
		this.awardedAmountToDate = awardedAmountToDate;
		this.rawAwardedAmountToDate = rawAwardedAmountToDate;
		this.awardInstrument = awardInstrument;
		this.nSFDirectorate = nSFDirectorate;
		this.nSFOrganization = nSFOrganization;
		this.abstractText = abstractText;
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


	public Date getLastAmendmentDate() {
		return lastAmendmentDate;
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

	public String getRawStartDate() {
		return rawStartDate;
	}


	public String getRawAwardedAmountToDate() {
		return rawAwardedAmountToDate;
	}


	public String getRawLastAmendmentDate() {
		return rawLastAmendmentDate;
	}


	public String getRawExpirationDate() {
		return rawExpirationDate;
	}

	public void addArbitraryColumn(String name, String value) {
		if (SCHEMA.findField(name) == null) {
			SCHEMA.addField(name, DerbyFieldType.TEXT);
		}
		getAttributes().put(name, value);
	}

	/*@Override
	public boolean shouldMerge(Award otherItem) {
		return (
			StringUtilities.areValidAndEqualIgnoreCase(
				this.awardNumber, 
				otherItem.getAwardNumber())
			&& StringUtilities.areValidAndEqualIgnoreCase(
					this.title, 
					otherItem.getTitle()) 
		);
	}*/

	@Override
	public Object createMergeKey() {
		List<Object> mergeKey = new ArrayList<Object>();
		Integer primaryKey = getPrimaryKey();
		addCaseInsensitiveStringOrAlternativeToMergeKey(mergeKey, this.awardNumber, primaryKey);
		addCaseInsensitiveStringOrAlternativeToMergeKey(mergeKey, this.title, primaryKey);

		return mergeKey;
	}

	@Override
	public void merge(Award otherItem) {
		this.rawStartDate =
			StringUtilities.simpleMerge(this.rawStartDate, otherItem.getRawStartDate());
		this.rawExpirationDate =
			StringUtilities.simpleMerge(this.rawExpirationDate, otherItem.getRawExpirationDate());
		this.rawLastAmendmentDate = StringUtilities.simpleMerge(
			this.rawLastAmendmentDate, otherItem.getRawLastAmendmentDate());
		this.rawAwardedAmountToDate = StringUtilities.simpleMerge(
			this.rawAwardedAmountToDate, otherItem.getRawAwardedAmountToDate());
		this.awardInstrument =
			StringUtilities.simpleMerge(this.awardInstrument, otherItem.getAwardInstrument());
		this.nSFDirectorate =
			StringUtilities.simpleMerge(this.nSFDirectorate, otherItem.getNSFDirectorate());	 
		this.nSFOrganization =
			StringUtilities.simpleMerge(this.nSFOrganization, otherItem.getNSFOrganization());	 
		this.abstractText =
			StringUtilities.simpleMerge(this.abstractText, otherItem.getAbstractText());
	}

	private static Dictionary<String, Object> createAttributes(
			String awardNumber,
			String title,
			Date startDate,
			String rawStartDate, 
			Date expirationDate,
			String rawExpirationDate, 
			Date lastAmendmentDate,
			String rawLastAmendmentDate, 
			double awardedAmountToDate,
			String rawAwardedAmountToDate,
			String awardInstrument,
			String nSFDirectorate,
			String nSFOrganization,
			String abstractText) {
		Dictionary<String, Object> attributes = new Hashtable<String, Object>();
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

		if (lastAmendmentDate != null) {
			attributes.put(NSF_Database_FieldNames.LAST_AMENDMENT_DATE, lastAmendmentDate);
		}
		
		attributes.put(NSF_Database_FieldNames.RAW_LAST_AMENDMENT_DATE, rawLastAmendmentDate);
		attributes.put(NSF_Database_FieldNames.AWARDED_AMOUNT_TO_DATE, awardedAmountToDate);
		attributes.put(NSF_Database_FieldNames.RAW_AWARDED_AMOUNT_TO_DATE, rawAwardedAmountToDate);
		attributes.put(NSF_Database_FieldNames.AWARD_INSTRUMENT, awardInstrument);
		attributes.put(NSF_Database_FieldNames.NSF_DIRECTORATE, nSFDirectorate);
		attributes.put(NSF_Database_FieldNames.NSF_ORGANIZATION, nSFOrganization);
		attributes.put(NSF_Database_FieldNames.ABSTRACT_TEXT, abstractText);
		
		return attributes;
	}
}