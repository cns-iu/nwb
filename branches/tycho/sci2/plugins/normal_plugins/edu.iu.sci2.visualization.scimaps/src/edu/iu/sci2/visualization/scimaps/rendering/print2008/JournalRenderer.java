package edu.iu.sci2.visualization.scimaps.rendering.print2008;

import oim.vivo.scimapcore.journal.Journal;
import edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState;
import static edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState.inch;

/**
 * This class renders the journal.
 * 
 */
public class JournalRenderer {
	public static void render(GraphicsState state, Journal journal) {
		String valueString = String.valueOf(journal.getJournalHitCount());
		state.current.drawString(valueString,
				inch(.2f) - (state.stringWidth(valueString) + inch(.05f)), 0);
		state.current.drawString(journal.getJournalName(), inch(.2f), 0);
	}
}
