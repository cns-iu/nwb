package edu.iu.scipolicy.loader.nsf.db.model.entity.relationship;

import java.util.Dictionary;
import java.util.Hashtable;

import edu.iu.cns.database.loader.framework.RowItem;
import edu.iu.scipolicy.loader.nsf.db.model.entity.Organization;

public class InvestigatorOrganizations extends RowItem<InvestigatorOrganizations> {
	private Investigator investigator;
	private Organization organization;

	public InvestigatorOrganizations(Investigator investigator, Organization organization) {
		super(createAttributes());
		this.investigator = investigator;
		this.organization = organization; 
	}
	
	public Investigator getInvestigator() {
		return this.investigator;
	}
	
	public Organization getOrganization() {
		return this.organization;
	}

	private static Dictionary<String, Comparable<?>> createAttributes() {
		return new Hashtable<String, Comparable<?>>();
	}

	@Override
	public void merge(InvestigatorOrganizations otherItem) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean shouldMerge(InvestigatorOrganizations otherItem) {
		// TODO Auto-generated method stub
		return false;
	}
}