package edu.iu.sci2.database.nsf.load.model.entity.relationship;

import java.util.Dictionary;
import java.util.Hashtable;

import edu.iu.cns.database.load.framework.DerbyFieldType;
import edu.iu.cns.database.load.framework.RowItem;
import edu.iu.cns.database.load.framework.Schema;
import edu.iu.sci2.database.nsf.load.model.entity.Organization;
import edu.iu.scipolicy.utilities.nsf.NSF_Database_FieldNames;

public class InvestigatorOrganization extends RowItem<InvestigatorOrganization> {
	
	public static final Schema<InvestigatorOrganization> SCHEMA = new Schema<InvestigatorOrganization>(
			false,
			NSF_Database_FieldNames.INVESTIGATOR_ORGANIZATIONS_INVESTIGATOR_FOREIGN_KEY,
				DerbyFieldType.FOREIGN_KEY,
			NSF_Database_FieldNames.INVESTIGATOR_ORGANIZATIONS_ORGANIZATION_FOREIGN_KEY,
				DerbyFieldType.FOREIGN_KEY
			).
			FOREIGN_KEYS(
				NSF_Database_FieldNames.INVESTIGATOR_ORGANIZATIONS_INVESTIGATOR_FOREIGN_KEY,
					NSF_Database_FieldNames.INVESTIGATOR_TABLE_NAME,
				NSF_Database_FieldNames.INVESTIGATOR_ORGANIZATIONS_ORGANIZATION_FOREIGN_KEY,
					NSF_Database_FieldNames.ORGANIZATION_TABLE_NAME
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
			NSF_Database_FieldNames.INVESTIGATOR_ORGANIZATIONS_INVESTIGATOR_FOREIGN_KEY,
			investigator.getPrimaryKey());
		attributes.put(
			NSF_Database_FieldNames.INVESTIGATOR_ORGANIZATIONS_ORGANIZATION_FOREIGN_KEY,
			organization.getPrimaryKey());

		return attributes;
	}
}