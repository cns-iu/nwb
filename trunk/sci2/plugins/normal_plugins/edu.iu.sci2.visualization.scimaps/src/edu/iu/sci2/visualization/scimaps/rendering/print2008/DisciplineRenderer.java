package edu.iu.sci2.visualization.scimaps.rendering.print2008;

import static edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState.inch;

import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;

import oim.vivo.scimapcore.journal.Discipline;
import oim.vivo.scimapcore.journal.Journal;
import edu.iu.sci2.visualization.scimaps.MapOfScience;
import edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState;

@Deprecated
public class DisciplineRenderer {

	@Deprecated
	public static void renderListHeader(GraphicsState state, double lineHeight,
			Discipline discipline) {
		state.current.translate(0, 1.3 * lineHeight);
		state.save();
		state.current.setColor(discipline.getColor());
		int radius = (int) Math.max(1, lineHeight / 3);
		state.drawArc((int) (-7.0 / 3.0 * lineHeight),
				(int) (-lineHeight / 2.0), radius, 0, 360);
		state.restore();

		state.save();
		state.scaleFont(1.5);
		state.current.drawString(discipline.getName(), 0, 0);
		state.restore();
	}

	@Deprecated
	public static void renderMappedDisciplines(GraphicsState state,
			MapOfScience mapOfScience, double lineHeight) {
		SortedMap<Discipline, SortedSet<Journal>> disciplinesToJournalsMap = mapOfScience
				.getMappedJournalsByDiscipline();
		renderDisciplines(state, disciplinesToJournalsMap, lineHeight);
	}

	@Deprecated
	public static void renderUnmappedDisciplines(GraphicsState state,
			MapOfScience mapOfScience, double lineHeight) {
		SortedMap<Discipline, SortedSet<Journal>> disciplineToJournalsMap = mapOfScience
				.getUnmappedJournalsByDiscipline();
		renderDisciplines(state, disciplineToJournalsMap, lineHeight);
	}

	@Deprecated
	public static void renderDisciplines(GraphicsState state,
			SortedMap<Discipline, SortedSet<Journal>> disciplinesToJournalsMap,
			double lineHeight) {
		Set<Discipline> usedDisciplines = disciplinesToJournalsMap.keySet();

		for (Discipline discipline : usedDisciplines) {

			DisciplineRenderer.renderListHeader(state, lineHeight, discipline);

			for (Journal journal : disciplinesToJournalsMap.get(discipline)) {
				state.current.translate(0, lineHeight);
				JournalRenderer.render(state, journal);
			}
		}

		state.current.translate(inch(-.5f), 2.2 * lineHeight);
	}
}
