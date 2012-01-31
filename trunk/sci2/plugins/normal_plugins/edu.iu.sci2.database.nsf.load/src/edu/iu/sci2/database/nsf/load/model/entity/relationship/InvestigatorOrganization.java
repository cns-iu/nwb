package edu.iu.sci2.database.nsf.load.model.entity.relationship;

import java.util.Dictionary;
import java.util.Hashtable;

import edu.iu.cns.database.load.framework.DerbyFieldType;
import edu.iu.cns.database.load.framework.RowItem;
import edu.iu.cns.database.load.framework.Schema;
import edu.iu.sci2.database.nsf.load.model.entity.Organization;
import edu.iu.sci2.utilities.nsf.NsfDatabaseFieldNames;

public class InvestigatorOrganization extends RowItem<InvestigatorOrganization> {
	
	public static final Schema<InvestigatorOrganization> SCHEMA = new Schema<InvestigatorOrganization>(
			false,
			NsfDatabaseFieldNames.INVESTIGATOR_ORGANIZATIONS_INVESTIGATOR_FOREIGN_KEY,
				DerbyFieldType.FOREIGN_KEY,
			NsfDatabaseFieldNames.INVESTIGATOR_ORGANIZATIONS_ORGANIZATION_FOREIGN_KEY,
				DerbyFieldType.FOREIGN_KEY
			).
			FOREIGN_KEYS(
				NsfDatabaseFieldNames.INVESTIGATOR_ORGANIZATIONS_INVESTIGATOR_FOREIGN_KEY,
					NsfDatabaseFieldNames.INVESTIGATOR_TABLE_NAME,
				NsfDatabaseFieldNames.INVESTIGATOR_ORGANIZATIONS_ORGANIZATION_FOREIGN_KEY,
					NsfDatabaseFieldNames.ORGANIZATION_TABLE_NAME
			);
	
	private Investigator investigator;
	private Organization organization;

	public InvestigatorOrganization(Investigator investigator, Organization organization) {
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

	/*@Override
	public Object createMergeKey() {
		List<Object> mergeKey = new ArrayList<Object>();
		mergeKey.add(this.investigator.getPrimaryKey());
		mergeKey.add(this.organization.getPrimaryKey());

		return mergeKey;
	}

	@Override
	public void merge(InvestigatorOrganization otherItem) {
	}*/

	private static Dictionary<String, Object> createAttributes(
			Investigator investigator, Organization organization) {
		Dictionary<String, Object> attributes = new Hashtable<String, Object>();
		attributes.put(
			NsfDatabaseFieldNames.INVESTIGATOR_ORGANIZATIONS_INVESTIGATOR_FOREIGN_KEY,
			investigator.getPrimaryKey());
		attributes.put(
			NsfDatabaseFieldNames.INVESTIGATOR_ORGANIZATIONS_ORGANIZATION_FOREIGN_KEY,
			organization.getPrimaryKey());

		return attributes;
	}
}