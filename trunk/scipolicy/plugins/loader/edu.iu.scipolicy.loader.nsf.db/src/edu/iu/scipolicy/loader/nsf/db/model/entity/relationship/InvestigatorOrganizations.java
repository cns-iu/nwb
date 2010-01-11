package edu.iu.scipolicy.loader.nsf.db.model.entity.relationship;

import java.util.Dictionary;
import java.util.Hashtable;

import edu.iu.cns.database.loader.framework.DerbyFieldType;
import edu.iu.cns.database.loader.framework.RowItem;
import edu.iu.cns.database.loader.framework.Schema;
import edu.iu.scipolicy.loader.nsf.db.NSFDatabase;
import edu.iu.scipolicy.loader.nsf.db.model.entity.Organization;

public class InvestigatorOrganizations extends RowItem<InvestigatorOrganizations> {
	
	public static final Schema<InvestigatorOrganizations> SCHEMA = new Schema<InvestigatorOrganizations>(
			false,
			NSFDatabase.INVESTIGATOR_ORGANIZATIONS_INVESTIGATOR_FOREIGN_KEY, DerbyFieldType.FOREIGN_KEY,
			NSFDatabase.INVESTIGATOR_ORGANIZATIONS_ORGANIZATION_FOREIGN_KEY, DerbyFieldType.FOREIGN_KEY
			).
			FOREIGN_KEYS(
					NSFDatabase.INVESTIGATOR_ORGANIZATIONS_INVESTIGATOR_FOREIGN_KEY,
						NSFDatabase.INVESTIGATOR_TABLE_NAME,
					NSFDatabase.INVESTIGATOR_ORGANIZATIONS_ORGANIZATION_FOREIGN_KEY,
						NSFDatabase.ORGANIZATION_TABLE_NAME);
	
	private Investigator investigator;
	private Organization organization;

	public InvestigatorOrganizations(Investigator investigator, Organization organization) {
		super(createAttributes(investigator, organization));
		this.investigator = investigator;
		this.organization = organization; 
	}
	
	public Investigator getInvestigator() {
		return this.investigator;
	}
	
	public Organization getOrganization() {
		return this.organization;
	}

	private static Dictionary<String, Comparable<?>> createAttributes(Investigator investigator, Organization organization) {
		Dictionary<String, Comparable<?>> attributes = new Hashtable<String, Comparable<?>>();
		attributes.put(NSFDatabase.INVESTIGATOR_ORGANIZATIONS_INVESTIGATOR_FOREIGN_KEY, 
					   investigator.getPrimaryKey());
		
		attributes.put(NSFDatabase.INVESTIGATOR_ORGANIZATIONS_ORGANIZATION_FOREIGN_KEY, 
					   organization.getPrimaryKey());
		return attributes;
	}

	@Override
	public void merge(InvestigatorOrganizations otherItem) { }

	@Override
	public boolean shouldMerge(InvestigatorOrganizations otherItem) {
		return false;
	}
}