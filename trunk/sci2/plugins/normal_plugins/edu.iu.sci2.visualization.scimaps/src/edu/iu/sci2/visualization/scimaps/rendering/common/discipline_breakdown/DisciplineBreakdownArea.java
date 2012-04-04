package edu.iu.sci2.visualization.scimaps.rendering.common.discipline_breakdown;

import static edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState.inch;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.util.List;
import java.util.SortedSet;

import oim.vivo.scimapcore.journal.Discipline;
import oim.vivo.scimapcore.journal.Journal;
import edu.iu.sci2.visualization.scimaps.rendering.common.discipline_breakdown.DisciplineBreakdownAreas.Column;
import edu.iu.sci2.visualization.scimaps.rendering.common.discipline_breakdown.DisciplineBreakdownAreas.ColumnEntry;
import edu.iu.sci2.visualization.scimaps.rendering.common.discipline_breakdown.DisciplineBreakdownAreas.Page;
import edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState;
import edu.iu.sci2.visualization.scimaps.tempvis.PageElement;

/**
 * This class holds all the information required to draw the category to color
 * legend and render it.
 */
public class DisciplineBreakdownArea implements PageElement{
	/**
	 * The amount of space that each journal will take.
	 */
	public static final int JOURNAL_SPACE = 13;
	/**
	 * The amount of space that each discipline will take.
	 */
	public static final int DISCIPLINE_SPACE = 17 + JOURNAL_SPACE;
	
	private static final int disciplineFontSize = 14;
	private static final int journalFontSize = 10;
	private static final String disciplineFont = "Arial";
	private static final String journalFont = "Arial";
	private static final int textMargin = 12;
	private static final float columnMargin = inch(0.5f);

	private double width;
	private int columnsPerPage;
	private Page page;
	private double leftBoundary;
	private double topBoundary;

	public DisciplineBreakdownArea(Page page, double leftBoundary, double topBoundary) {
		this.page = page;
		this.width = (int) page.getSize().getWidth();
		this.leftBoundary = leftBoundary;
		this.topBoundary = topBoundary;
	}

	public void render(GraphicsState state) {
		final DecimalFormat formatter = new DecimalFormat("###,###");
		boolean journalSizeSanityChecked = false;
		boolean disciplineSizeSanityChecked = false;

		state.save();
		state.current.translate(this.leftBoundary, this.topBoundary);

		double columnWidth = (this.width / this.page.getNumberOfColumns())
				- (columnMargin * (this.columnsPerPage - 1)) - textMargin;

		List<Column> columns = this.page.getColumns();
		for (int i = 0; i < columns.size(); i++) {
			Column column = columns.get(i);

			state.current.translate((columnWidth + columnMargin) * i, 0);

			state.save();
			for (ColumnEntry columnEntry : column.getColumnEntries()) {
				// Render discipline
				Discipline discipline = columnEntry.getDiscipline();
				SortedSet<Journal> journals = columnEntry.getJournals();

				state.setBoldFont(disciplineFont, disciplineFontSize);
				// Draw Square
				if (discipline != Discipline.MULTIPLE
						|| discipline != Discipline.NONE) {
					state.save();
					state.current.setPaint(discipline.getColor());
					int boxHeight = state.current.getFontMetrics().getAscent()
							+ state.current.getFontMetrics().getDescent();
					state.current.fillRect(0, -boxHeight / 2, boxHeight / 2,
							boxHeight / 2);
					state.restore();
				}

				// Draw String
				state.current.translate(textMargin, 0);
				String disciplineName = shortenIfNeeded(discipline.getName(),
						state.current, columnWidth);

				state.drawStringAndTranslate(disciplineName);
				if (!disciplineSizeSanityChecked) {
					disciplineSizeSanityChecked = true;
					int disciplineSpace = state.current.getFontMetrics()
							.getHeight();
					if (DISCIPLINE_SPACE != disciplineSpace + JOURNAL_SPACE) {
						System.err
								.println("The size calculated for the Discipline is wrong.  Current ="
										+ DISCIPLINE_SPACE
										+ ", Actual="
										+ disciplineSpace);
					}
				}

				// draw journals

				state.setFont(journalFont, journalFontSize);

				for (Journal journal : journals) {
					String journalName = shortenIfNeeded(
							formatter.format(journal.getJournalHitCount())
									+ " " + journal.getJournalName(),
							state.current, columnWidth);
					state.drawStringAndTranslate(journalName);
					if (!journalSizeSanityChecked) {
						journalSizeSanityChecked = true;
						int journalSpace = state.current.getFontMetrics()
								.getHeight();
						if (JOURNAL_SPACE != journalSpace) {
							System.err
									.println("The size calculated for the Journal is wrong.  Current ="
											+ JOURNAL_SPACE
											+ ", Actual="
											+ journalSpace);
						}
					}
				}

				state.current.translate(-textMargin, state.current.getFontMetrics().getHeight());

			}
			state.restore();
		}
		state.restore();
	}

	private static String shortenIfNeeded(String string, Graphics2D g2d,
			double limit) {
		Rectangle2D stringBounds = g2d.getFontMetrics().getStringBounds(string,
				g2d);
		if (stringBounds.getWidth() <= limit) {
			return string;
		}

		String truncatationIndicator = "...";
		Rectangle2D truncationIndicatorBounds = g2d.getFontMetrics()
				.getStringBounds(truncatationIndicator, g2d);
		int charWidth = g2d.getFontMetrics().getMaxAdvance();

		if (truncationIndicatorBounds.getWidth() + charWidth >= limit) {
			// This will not render well, but there was no way to handle
			// this gracefully.
			return truncatationIndicator;
		}

		int numberToRemove = (int) ((stringBounds.getWidth() - limit) / charWidth);

		if (numberToRemove >= string.length()) {
			// This will not render well, but there was no way to handle
			// this gracefully.
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
