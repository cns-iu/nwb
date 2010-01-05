package edu.iu.scipolicy.loader.nsf.db.model.entity.relationship;

import java.util.Dictionary;
import java.util.Hashtable;

import edu.iu.cns.database.loader.framework.RowItem;
import edu.iu.scipolicy.loader.nsf.db.model.entity.Award;
import edu.iu.scipolicy.loader.nsf.db.model.entity.Program;

public class ProgramNameAndElementCodes extends RowItem<ProgramNameAndElementCodes> {

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
	public void merge(ProgramNameAndElementCodes otherItem) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean shouldMerge(ProgramNameAndElementCodes otherItem) {
		// TODO Auto-generated method stub
		return false;
	}
}