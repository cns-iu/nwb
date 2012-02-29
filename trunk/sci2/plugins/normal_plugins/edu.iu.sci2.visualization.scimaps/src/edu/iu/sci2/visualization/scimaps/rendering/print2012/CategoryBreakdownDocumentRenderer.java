package edu.iu.sci2.visualization.scimaps.rendering.print2012;

import static edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState.inch;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.util.SortedMap;
import java.util.SortedSet;

import oim.vivo.scimapcore.journal.Category;
import oim.vivo.scimapcore.journal.Journal;
import edu.iu.sci2.visualization.scimaps.MapOfScience;
import edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState;
import edu.iu.sci2.visualization.scimaps.tempvis.RenderableVisualization;

public class CategoryBreakdownDocumentRenderer implements
		RenderableVisualization {

	private String generatedFrom;
	private Dimension size;
	private SortedMap<Category, SortedSet<Journal>> journalsByCategory;
	private MapOfScience mapOfScience;

	public CategoryBreakdownDocumentRenderer(
			SortedMap<Category, SortedSet<Journal>> journalsByCategory,
			String generatedFrom, Dimension size, MapOfScience mapOfScience) {
		this.journalsByCategory = journalsByCategory;
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

		new JournalBreakDownArea(this.journalsByCategory).render(state,
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
		 * These valuse were calculated by printing out the font metrics for
		 * arial bold at font size 12 and arial at font size 10.
		 */
		public static final int defaultCategorySpace = 15;
		public static final int defaultJournalSpace = 13;
		public static final int categoryFontSize = 12;
		public static final int journalFontSize = 10;
		public static final String categoryFont = "Arial Bold";
		public static final String journalFont = "Arial";
		public static final int textMargin = 12;
		public static final int columnsPerPage = 2;
		public static final float columnMargin = inch(0.5f);

		private SortedMap<Category, SortedSet<Journal>> journalsByCategory;

		public JournalBreakDownArea(
				SortedMap<Category, SortedSet<Journal>> journalsByCategory) {
			this.journalsByCategory = journalsByCategory;
		}

		public void render(GraphicsState state, float width, float height) {
			state.save();
			state.current.translate(35, 100);

			int currentColumn = (int) height;
			float columnWidth = (width / columnsPerPage)
					- (columnMargin * (columnsPerPage / 2)) - textMargin;
			for (Category category : this.journalsByCategory.keySet()) {
				// render category
				state.save();

				state.setFont(categoryFont, categoryFontSize);
				if (category != Category.MULTIPLE || category != Category.NONE) {
					state.save();
					String[] rgb = category.getRGBColor().split(" ");
					state.current
							.setPaint(new Color(Float.parseFloat(rgb[0]), Float
									.parseFloat(rgb[1]), Float
									.parseFloat(rgb[2])));
					int textHeight = state.current.getFontMetrics().getAscent()
							+ state.current.getFontMetrics().getDescent();
					state.current.fillRect(0, 0, 0 - textHeight / 2,
							0 - textHeight / 2);
					state.restore();
				}

				state.save();
				state.current.translate(textMargin, 0);
				String categoryName = shortenIfNeeded(category.getName(),
						state.current, columnWidth);

				state.current.drawString(categoryName, 0, 0);
				int yCategoryTranslationAmount = state.current.getFontMetrics()
						.getHeight();
				state.restore();

				state.restore();
				if (currentColumn < yCategoryTranslationAmount) {
					state.current.translate(columnMargin + columnWidth,
							(int) (-1 * (height - currentColumn)));
					currentColumn = (int) height;
				} else {
					currentColumn -= yCategoryTranslationAmount;
					state.current.translate(0, yCategoryTranslationAmount);
				}

				for (Journal journal : this.journalsByCategory.get(category)) {
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
