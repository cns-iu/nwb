package edu.iu.scipolicy.loader.nsf.db.model.entity.relationship;

import java.util.Dictionary;
import java.util.Hashtable;

import edu.iu.cns.database.loader.framework.DerbyFieldType;
import edu.iu.cns.database.loader.framework.RowItem;
import edu.iu.cns.database.loader.framework.Schema;
import edu.iu.scipolicy.loader.nsf.db.NSFDatabase;
import edu.iu.scipolicy.loader.nsf.db.model.entity.Award;
import edu.iu.scipolicy.loader.nsf.db.model.entity.Program;

public class ProgramNameAndElementCodes extends RowItem<ProgramNameAndElementCodes> {

	public static final Schema<ProgramNameAndElementCodes> SCHEMA = new Schema<ProgramNameAndElementCodes>(
			false,
			NSFDatabase.PROGRAM_NAME_AND_ELEMENT_CODES_PROGRAM_FOREIGN_KEY, DerbyFieldType.FOREIGN_KEY,
			NSFDatabase.PROGRAM_NAME_AND_ELEMENT_CODES_AWARD_FOREIGN_KEY, DerbyFieldType.FOREIGN_KEY
			).
			FOREIGN_KEYS(
					NSFDatabase.PROGRAM_NAME_AND_ELEMENT_CODES_PROGRAM_FOREIGN_KEY,
						NSFDatabase.PROGRAM_TABLE_NAME,
					NSFDatabase.PROGRAM_NAME_AND_ELEMENT_CODES_AWARD_FOREIGN_KEY,
						NSFDatabase.AWARD_TABLE_NAME);
	
	private Program program;
	private Award award;

	public ProgramNameAndElementCodes(Program program, Award award) {
		super(createAttributes());
		this.program = program;
		this.award = award;
	}

	public Program getProgram() {
		return this.program;
	}
	
	public Award getAward() {
		return this.award;
	}

	private static Dictionary<String, Comparable<?>> createAttributes() {
		return new Hashtable<String, Comparable<?>>();
	}

	@Override
	public void merge(ProgramNameAndElementCodes otherItem) { }

	@Override
	public boolean shouldMerge(ProgramNameAndElementCodes otherItem) {
		return false;
	}
}