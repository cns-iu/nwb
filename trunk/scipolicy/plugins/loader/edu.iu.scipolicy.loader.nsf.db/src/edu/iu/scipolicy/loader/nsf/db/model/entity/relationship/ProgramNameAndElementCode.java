package edu.iu.scipolicy.loader.nsf.db.model.entity.relationship;

import java.util.Dictionary;
import java.util.Hashtable;

import edu.iu.cns.database.loader.framework.DerbyFieldType;
import edu.iu.cns.database.loader.framework.RowItem;
import edu.iu.cns.database.loader.framework.Schema;
import edu.iu.scipolicy.loader.nsf.db.model.entity.Award;
import edu.iu.scipolicy.loader.nsf.db.model.entity.Program;
import edu.iu.scipolicy.utilities.nsf.NSF_Database_FieldNames;

public class ProgramNameAndElementCode extends RowItem<ProgramNameAndElementCode> {

	public static final Schema<ProgramNameAndElementCode> SCHEMA = new Schema<ProgramNameAndElementCode>(
			false,
			NSF_Database_FieldNames.PROGRAM_NAME_AND_ELEMENT_CODES_PROGRAM_FOREIGN_KEY, DerbyFieldType.FOREIGN_KEY,
			NSF_Database_FieldNames.PROGRAM_NAME_AND_ELEMENT_CODES_AWARD_FOREIGN_KEY, DerbyFieldType.FOREIGN_KEY
			).
			FOREIGN_KEYS(
					NSF_Database_FieldNames.PROGRAM_NAME_AND_ELEMENT_CODES_PROGRAM_FOREIGN_KEY,
						NSF_Database_FieldNames.PROGRAM_TABLE_NAME,
					NSF_Database_FieldNames.PROGRAM_NAME_AND_ELEMENT_CODES_AWARD_FOREIGN_KEY,
						NSF_Database_FieldNames.AWARD_TABLE_NAME);
	
	private Program program;
	private Award award;

	public ProgramNameAndElementCode(Program program, Award award) {
		super(createAttributes(program, award));
		this.program = program;
		this.award = award;
	}

	public Program getProgram() {
		return this.program;
	}
	
	public Award getAward() {
		return this.award;
	}

	private static Dictionary<String, Comparable<?>> createAttributes(Program program, Award award) {
		Dictionary<String, Comparable<?>> attributes = new Hashtable<String, Comparable<?>>();
		
		attributes.put(NSF_Database_FieldNames.PROGRAM_NAME_AND_ELEMENT_CODES_PROGRAM_FOREIGN_KEY, 
					   program.getPrimaryKey());
		
		attributes.put(NSF_Database_FieldNames.PROGRAM_NAME_AND_ELEMENT_CODES_AWARD_FOREIGN_KEY, 
					   award.getPrimaryKey());
		
		return attributes;
	}

	@Override
	public void merge(ProgramNameAndElementCode otherItem) { }

	@Override
	public boolean shouldMerge(ProgramNameAndElementCode otherItem) {
		return false;
	}
}