package edu.iu.sci2.database.nsf.load.model.entity;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import org.cishell.utilities.StringUtilities;

import edu.iu.cns.database.load.framework.DerbyFieldType;
import edu.iu.cns.database.load.framework.Entity;
import edu.iu.cns.database.load.framework.Schema;
import edu.iu.cns.database.load.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.sci2.utilities.nsf.NSF_Database_FieldNames;

public class Program extends Entity<Program> {

	public static final Schema<Program> SCHEMA = new Schema<Program>(
			true,
			NSF_Database_FieldNames.PROGRAM_NAME, DerbyFieldType.TEXT,
			NSF_Database_FieldNames.FUNDING_CODE, DerbyFieldType.TEXT
			);
	
	private String name;
	private String fundingCode;

	public Program(DatabaseTableKeyGenerator keyGenerator, String name, String fundingCode) {
		super(keyGenerator, createAttributes(name, fundingCode));
		this.fundingCode = fundingCode;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getFundingCode() {
		return fundingCode;
	}

	/*@Override
	public boolean shouldMerge(Program otherItem) {
		Program otherProgram = otherItem;
		boolean isCurrentProgramNameEmpty = name.length() == 0 ? true : false;
		boolean isOtherProgramNameEmpty = otherProgram.getName().length() == 0 ? true : false;

		//TODO: explain 
		if (isCurrentProgramNameEmpty && isOtherProgramNameEmpty) {
			if (fundingCode.equalsIgnoreCase(otherProgram.getFundingCode())) {
				return true;
			} else {
				return false;
			}
		} else if (!isCurrentProgramNameEmpty && !isOtherProgramNameEmpty) {
			if (name.equalsIgnoreCase(otherProgram.getName())) {
				return true;
			} else {
				return false;
			}
		} else {
			if (fundingCode.equalsIgnoreCase(otherProgram.getFundingCode())) {
				return true;
			} else {
				return false;
			}
		}
	}*/

	public Object createMergeKey() {
		List<Object> mergeKey = new ArrayList<Object>();
		Integer primaryKey = getPrimaryKey();
		addCaseInsensitiveStringOrAlternativeToMergeKey(mergeKey, this.fundingCode, primaryKey);
		addCaseInsensitiveStringOrAlternativeToMergeKey(mergeKey, this.name, primaryKey);

		return mergeKey;
	}
	
	@Override
	public void merge(Program otherItem) {
		this.name = StringUtilities.simpleMerge(this.name, otherItem.getName());
		this.fundingCode = StringUtilities.simpleMerge(this.fundingCode, otherItem.getFundingCode());
	}

	private static Dictionary<String, Object> createAttributes(String name,  String fundingCode) {
		Dictionary<String, Object> attributes = new Hashtable<String, Object>();
		attributes.put(NSF_Database_FieldNames.PROGRAM_NAME, name);
		attributes.put(NSF_Database_FieldNames.FUNDING_CODE, fundingCode);

		return attributes;
	}
}