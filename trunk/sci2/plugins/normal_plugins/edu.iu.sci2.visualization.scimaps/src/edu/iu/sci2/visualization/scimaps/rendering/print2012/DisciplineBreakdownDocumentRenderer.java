package edu.iu.sci2.visualization.scimaps.rendering.print2012;

import static edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState.inch;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.util.SortedMap;
import java.util.SortedSet;

import oim.vivo.scimapcore.journal.Discipline;
import oim.vivo.scimapcore.journal.Journal;
import edu.iu.sci2.visualization.scimaps.MapOfScience;
import edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState;
import edu.iu.sci2.visualization.scimaps.tempvis.RenderableVisualization;

public class DisciplineBreakdownDocumentRenderer implements
		RenderableVisualization {

	private String generatedFrom;
	private Dimension size;
	private SortedMap<Discipline, SortedSet<Journal>> journalsByDiscipline;
	private MapOfScience mapOfScience;

	public DisciplineBreakdownDocumentRenderer(
			SortedMap<Discipline, SortedSet<Journal>> journalsByDiscipline,
			String generatedFrom, Dimension size, MapOfScience mapOfScience) {
		this.journalsByDiscipline = journalsByDiscipline;
		this.generatedFrom = generatedFrom;
		this.size = size;
		this.mapOfScience = mapOfScience;
	}

	public String title() {
		String title = "Topic Analysis - Map of Science";
		return title;
	}

	public GraphicsState preRender(Graphics2D graphics, Dimension size) {

		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		GraphicsState graphicsState = new GraphicsState(graphics);

		graphicsState.setFont("Arial", 12);

		return graphicsState;
	}

	public void render(GraphicsState state, Dimension size) {
		Header header = new Header(title(), this.generatedFrom,
				this.mapOfScience);
		header.render(state, inch(0.5f), inch(0.5f));

		new JournalBreakDownArea(this.journalsByDiscipline).render(state,
				inch(10.0f), inch(6.0f));

		Footer footer = new Footer((float) size.getWidth(), inch(8.0f));
		footer.render(state);
	}

	public Dimension getDimension() {
		return this.size;
	}

	public static class JournalBreakDownArea {
		public static final float defaultColumnHeight = inch(6.0f);
		/*
		 * These values were calculated by printing out the font metrics for
		 * ArialBold at font size 12 and Arial at font size 10.
		 */
		public static final int defaultDisciplineSpace = 15;
		public static final int defaultJournalSpace = 13;
		public static final int disciplineFontSize = 12;
		public static final int journalFontSize = 10;
		public static final String disciplineFont = "Arial Bold";
		public static final String journalFont = "Arial";
		public static final int textMargin = 12;
		public static final int columnsPerPage = 2;
		public static final float columnMargin = inch(0.5f);

		private SortedMap<Discipline, SortedSet<Journal>> journalsByDiscipline;

		public JournalBreakDownArea(
				SortedMap<Discipline, SortedSet<Journal>> journalsByDiscipline) {
			this.journalsByDiscipline = journalsByDiscipline;
		}

		public void render(GraphicsState state, float width, float height) {
			state.save();
			state.current.translate(35, 100);

			int currentColumn = (int) height;
			float columnWidth = (width / columnsPerPage)
					- (columnMargin * (columnsPerPage / 2)) - textMargin;
			for (Discipline discipline : this.journalsByDiscipline.keySet()) {
				// render discipline
				state.save();

				state.setFont(disciplineFont, disciplineFontSize);
				if (discipline != Discipline.MULTIPLE || discipline != Discipline.NONE) {
					state.save();
					state.current
							.setPaint(discipline.getColor());
					int textHeight = state.current.getFontMetrics().getAscent()
							+ state.current.getFontMetrics().getDescent();
					state.current.fillRect(0, 0, 0 - textHeight / 2,
							0 - textHeight / 2);
					state.restore();
				}

				state.save();
				state.current.translate(textMargin, 0);
				String disciplineName = shortenIfNeeded(discipline.getName(),
						state.current, columnWidth);

				state.current.drawString(disciplineName, 0, 0);
				int yDisciplineTranslationAmount = state.current.getFontMetrics()
						.getHeight();
				state.restore();

				state.restore();
				if (currentColumn < yDisciplineTranslationAmount) {
					state.current.translate(columnMargin + columnWidth,
							(int) (-1 * (height - currentColumn)));
					currentColumn = (int) height;
				} else {
					currentColumn -= yDisciplineTranslationAmount;
					state.current.translate(0, yDisciplineTranslationAmount);
				}

				for (Journal journal : this.journalsByDiscipline.get(discipline)) {
					// render journal
					state.save();
					state.current.translate(textMargin, 0);
					state.setFont(journalFont, journalFontSize);
					int yJournalTranslationAmount = state.current
							.getFontMetrics().getHeight();

					String journalName = shortenIfNeeded(
							String.valueOf(journal.getJournalHitCount()) + " "
									+ journal.getJournalName(), state.current,
							columnWidth);
					state.current.drawString(journalName, 0, 0);
					state.restore();
					if (currentColumn < yJournalTranslationAmount) {
						state.current.translate(columnMargin + columnWidth,
								(int) (-1 * (height - currentColumn)));
						currentColumn = (int) height;
					} else {
						currentColumn -= yJournalTranslationAmount;
						state.current.translate(0, yJournalTranslationAmount);
					}
				}
			}
			state.restore();
		}

		private static String shortenIfNeeded(String string, Graphics2D g2d,
				float limit) {
			Rectangle2D stringBounds = g2d.getFontMetrics().getStringBounds(
					string, g2d);
			if (stringBounds.getWidth() <= limit) {
				return string;
			}

			String truncatationIndicator = "...";
			Rectangle2D truncationIndicatorBounds = g2d.getFontMetrics()
					.getStringBounds(truncatationIndicator, g2d);
			int charWidth = g2d.getFontMetrics().getMaxAdvance();

			if (truncationIndicatorBounds.getWidth() + charWidth >= limit) {
				// This will not render well, but there was no way to handle this gracefully.
				return truncatationIndicator;
			}

			int numberToRemove = (int) ((stringBounds.getWidth() - limit) / charWidth);

			if (numberToRemove >= string.length()) {
				// This will not render well, but there was no way to handle this gracefully.
				return truncatationIndicator;
			}

			String truncatedString = string.substring(0, string.length()
					- numberToRemove)
					+ truncatationIndicator;

			while (g2d.getFontMetrics().getStringBounds(truncatedString, g2d)
					.getWidth() > limit
					&& numberToRemove < string.length()) {
				numberToRemove++;
				truncatedString = string.substring(0, string.length()
						- numberToRemove)
						+ truncatationIndicator;
			}

			return truncatedString;
		}

	}
}
