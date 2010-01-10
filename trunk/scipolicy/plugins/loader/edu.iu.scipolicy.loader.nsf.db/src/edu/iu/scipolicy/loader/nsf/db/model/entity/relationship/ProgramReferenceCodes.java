package edu.iu.scipolicy.loader.nsf.db.model.entity.relationship;

import java.util.Dictionary;
import java.util.Hashtable;

import edu.iu.cns.database.loader.framework.DerbyFieldType;
import edu.iu.cns.database.loader.framework.RowItem;
import edu.iu.cns.database.loader.framework.Schema;
import edu.iu.scipolicy.loader.nsf.db.NSFDatabase;
import edu.iu.scipolicy.loader.nsf.db.model.entity.Award;
import edu.iu.scipolicy.loader.nsf.db.model.entity.Program;

public class ProgramReferenceCodes extends RowItem<ProgramReferenceCodes> {

	public static final Schema<ProgramReferenceCodes> SCHEMA = new Schema<ProgramReferenceCodes>(
			false,
			NSFDatabase.PROGRAM_REFERENCE_CODES_PROGRAM_FOREIGN_KEY, DerbyFieldType.FOREIGN_KEY,
			NSFDatabase.PROGRAM_REFERENCE_CODES_AWARD_FOREIGN_KEY, DerbyFieldType.FOREIGN_KEY
			).
			FOREIGN_KEYS(
					NSFDatabase.PROGRAM_REFERENCE_CODES_PROGRAM_FOREIGN_KEY,
						NSFDatabase.PROGRAM_TABLE_NAME,
					NSFDatabase.PROGRAM_REFERENCE_CODES_AWARD_FOREIGN_KEY,
						NSFDatabase.AWARD_TABLE_NAME);
	
	private Program program;
	private Award award;

	public ProgramReferenceCodes(Program program, Award award) {
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
	public void merge(ProgramReferenceCodes otherItem) { }

	@Override
	public boolean shouldMerge(ProgramReferenceCodes otherItem) {
		return false;
	}
}