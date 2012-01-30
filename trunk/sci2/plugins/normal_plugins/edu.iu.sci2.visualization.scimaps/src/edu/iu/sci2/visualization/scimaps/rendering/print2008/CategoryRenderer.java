package edu.iu.sci2.visualization.scimaps.rendering.print2008;

import static edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState.inch;

import java.awt.Color;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;

import oim.vivo.scimapcore.journal.Category;
import oim.vivo.scimapcore.journal.Journal;
import edu.iu.sci2.visualization.scimaps.MapOfScience;
import edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState;

public class CategoryRenderer {

	public static void renderListHeader(GraphicsState state, double lineHeight, Category category) {
		state.current.translate(0, 1.3 * lineHeight);
		state.save();
		String[] rgb = category.getRGBColor().split(" ");
		state.current.setColor(new Color(Float.parseFloat(rgb[0]),
								   Float.parseFloat(rgb[1]),
								   Float.parseFloat(rgb[2])));
		int radius = (int)Math.max(1, lineHeight/3);
		state.drawArc((int)(-7.0/3.0 * lineHeight), (int)(-lineHeight/2.0), radius, 0, 360);
		state.restore();
		
		state.save();
		state.scaleFont(1.5);
		state.current.drawString(category.getName(), 0, 0);
		state.restore();		
	}
	
	public static void renderMappedCategories(GraphicsState state, MapOfScience mapOfScience, double lineHeight) {
		SortedMap<Category, SortedSet<Journal>> categoriesToJournalsMap = mapOfScience.getMappedJournalsByCategory();
		renderCategories(state, categoriesToJournalsMap, lineHeight);
	}

	public static void renderUnmappedCategories(GraphicsState state,
			MapOfScience mapOfScience, double lineHeight) {
		SortedMap<Category, SortedSet<Journal>> categoriesToJournalsMap = mapOfScience.getUnmappedJournalsByCategory();
		renderCategories(state, categoriesToJournalsMap, lineHeight);
	}
	
	public static void renderCategories(GraphicsState state,
			SortedMap<Category, SortedSet<Journal>> categoriesToJournalsMap,
			double lineHeight) {
		Set<Category> usedCategories = categoriesToJournalsMap.keySet();

		for (Category category : usedCategories) {

			CategoryRenderer.renderListHeader(state, lineHeight, category);

			for (Journal journal : categoriesToJournalsMap.get(category)) {
				state.current.translate(0, lineHeight);
				JournalRenderer.render(state, journal);
			}
		}

		state.current.translate(inch(-.5f), 2.2 * lineHeight);
	}
}
