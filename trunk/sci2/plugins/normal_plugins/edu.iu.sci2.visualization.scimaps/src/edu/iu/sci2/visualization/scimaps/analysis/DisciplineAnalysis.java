package edu.iu.sci2.visualization.scimaps.analysis;

import oim.vivo.scimapcore.journal.Discipline;
import oim.vivo.scimapcore.journal.Disciplines;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Multiset;

import edu.iu.sci2.visualization.scimaps.analysis.table.Column;
import edu.iu.sci2.visualization.scimaps.analysis.table.Row;

/**
 * A summary of the disciplines in a map of science according to how often each was mapped into.
 */
public class DisciplineAnalysis extends AbstractTabularAnalysis<Discipline> {
	public static final Column<String> NAME = Column.create(String.class, "Discipline");
	public static final Column<Integer> TOTAL = Column.create(Integer.class, "Journal Occurrences");
	
	private final ImmutableMultiset<Discipline> disciplineTotals;

	/**
	 * @param disciplines
	 *            A multiset of disciplines where each occurs as many times as it was mapped into
	 */
	public DisciplineAnalysis(Multiset<Discipline> disciplines) {
		super(ImmutableSet.of(NAME, TOTAL));
		Preconditions.checkNotNull(disciplines);
		
		this.disciplineTotals = ImmutableMultiset.copyOf(disciplines);
	}
	
	@Override
	protected Iterable<Discipline> getElements() {
		@SuppressWarnings("unchecked")
		ImmutableSortedSet<Discipline> disciplines =
					ImmutableSortedSet.copyOf(Disciplines.getDisciplines());
		
		return disciplines;
	}

	@Override
	protected Row createRowFor(Discipline discipline) {
		return new Row()
					.put(NAME, discipline.getName())
					.put(TOTAL, disciplineTotals.count(discipline));
	}
}
