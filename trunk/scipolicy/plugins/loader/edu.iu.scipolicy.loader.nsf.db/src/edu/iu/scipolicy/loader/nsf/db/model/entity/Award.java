package edu.iu.scipolicy.loader.nsf.db.model.entity;

import java.util.Date;
import java.util.Dictionary;
import java.util.Hashtable;

import org.cishell.utilities.StringUtilities;

import edu.iu.cns.database.loader.framework.DerbyFieldType;
import edu.iu.cns.database.loader.framework.Entity;
import edu.iu.cns.database.loader.framework.Schema;
import edu.iu.cns.database.loader.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.scipolicy.loader.nsf.db.NSFDatabase;

public class Award extends Entity<Award> implements Comparable<Award>{
	public static final Schema<Award> SCHEMA = new Schema<Award>(
			true,
			NSFDatabase.AWARD_NUMBER, DerbyFieldType.TEXT,
			NSFDatabase.TITLE, DerbyFieldType.TEXT,
			NSFDatabase.START_DATE, DerbyFieldType.DATE,
			NSFDatabase.EXPIRATION_DATE, DerbyFieldType.DATE,
			NSFDatabase.LAST_AMMENDMENT_DATE, DerbyFieldType.DATE,
			NSFDatabase.AWARDED_AMOUNT_TO_DATE, DerbyFieldType.DOUBLE,
			NSFDatabase.AWARD_INSTRUMENT, DerbyFieldType.TEXT,
			NSFDatabase.NSF_DIRECTORATE, DerbyFieldType.TEXT,
			NSFDatabase.NSF_ORGANIZATION, DerbyFieldType.TEXT,
			NSFDatabase.ABSTRACT_TEXT, DerbyFieldType.TEXT
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

	public Award(DatabaseTableKeyGenerator keyGenerator,
				 String awardNumber,
				 String title, 
				 Date startDate,
				 Date expirationDate, 
				 Date lastAmmendmentDate,
				 double awardedAmountToDate, 
				 String awardInstrument,
				 String nSFDirectorate, 
				 String nSFOrganization, 
				 String abstractText) {
		super(keyGenerator, createAttributes(awardNumber, 
											 title, 
											 startDate, 
											 expirationDate, 
											 lastAmmendmentDate, 
											 awardedAmountToDate, 
											 awardInstrument, 
											 nSFDirectorate, 
											 nSFOrganization, 
											 abstractText));
		this.awardNumber = awardNumber;
		this.title = title;
		this.startDate = startDate;
		this.expirationDate = expirationDate;
		this.lastAmmendmentDate = lastAmmendmentDate;
		this.awardedAmountToDate = awardedAmountToDate;
		this.awardInstrument = awardInstrument;
		this.nSFDirectorate = nSFDirectorate;
		this.nSFOrganization = nSFOrganization;
		this.abstractText = abstractText;
	}
	
	
	private static Dictionary<String, Comparable<?>> createAttributes(String awardNumber,
													   String title,
													   Date startDate,
													   Date expirationDate,
													   Date lastAmmendmentDate,
													   double awardedAmountToDate,
													   String awardInstrument,
													   String nSFDirectorate,
													   String nSFOrganization,
													   String abstractText) {
		Dictionary<String, Comparable<?>> attributes = new Hashtable<String, Comparable<?>>();
		attributes.put(NSFDatabase.TITLE, title);
		attributes.put(NSFDatabase.AWARD_NUMBER, awardNumber);
		attributes.put(NSFDatabase.START_DATE, startDate);
		attributes.put(NSFDatabase.EXPIRATION_DATE, expirationDate);
		attributes.put(NSFDatabase.LAST_AMMENDMENT_DATE, lastAmmendmentDate);
		attributes.put(NSFDatabase.AWARDED_AMOUNT_TO_DATE, awardedAmountToDate);
		attributes.put(NSFDatabase.AWARD_INSTRUMENT, awardInstrument);
		attributes.put(NSFDatabase.NSF_DIRECTORATE, nSFDirectorate);
		attributes.put(NSFDatabase.NSF_ORGANIZATION, nSFOrganization);
		attributes.put(NSFDatabase.ABSTRACT_TEXT, abstractText);
		
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


	@Override
	public void merge(Award otherItem) {
		//TODO: do something about date comparisiosn & amount awarded.
		//i.e. in general anything non-string
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
}