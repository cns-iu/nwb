package edu.iu.sci2.visualization.scimaps.rendering.common.discipline_breakdown;

import static edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState.inch;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import oim.vivo.scimapcore.journal.Discipline;
import oim.vivo.scimapcore.journal.Journal;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSortedSet;

import edu.iu.sci2.visualization.scimaps.MapOfScience;
import edu.iu.sci2.visualization.scimaps.rendering.common.Footer;
import edu.iu.sci2.visualization.scimaps.rendering.print2012.Header;
import edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState;

/**
 * DisciplineBreakDownPages can be thought of as a collection of pages, each of which has a breakdown area, a title, and other elements.
 * 
 * It will render a specific page to a graphic.
 */
public class DisciplineBreakdownPages {
	private static final int disciplineSpace = DisciplineBreakdownAreaRenderer.DISCIPLINE_SPACE;
	private static final int journalSpace = DisciplineBreakdownAreaRenderer.JOURNAL_SPACE;

	private int columnsPerPage;
	private MapOfScience mapOfScience;
	private String generatedFrom;
	private Dimension dimensions;
	private List<DisciplineBreakdownAreaRenderer> disciplineBreakdowns;

	public DisciplineBreakdownPages(int columnsPerPage,
			MapOfScience mapOfScience, String generatedFrom,
			Dimension dimensions) {
		this.columnsPerPage = columnsPerPage;
		this.mapOfScience = mapOfScience;
		this.generatedFrom = generatedFrom;
		this.dimensions = dimensions;

		this.disciplineBreakdowns = getDisciplineBreakdowns();
	}

	public int numberOfPages() {
		return this.disciplineBreakdowns.size();
	}

	public Dimension getDimension() {
		return this.dimensions;
	}

	public void renderPage(int pageNumber, Graphics2D graphics) {
		if (pageNumber > this.disciplineBreakdowns.size()) {
			throw new IllegalArgumentException("Page number '" + pageNumber
					+ "' does not exist.");
		}

		GraphicsState state = new GraphicsState(graphics);
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		state.setFont("Arial", 10);
		
		new Header("Topic Analysis - Map of Science", this.generatedFrom,
				this.mapOfScience).render(state, inch(0.5f), inch(0.5f));

		this.disciplineBreakdowns.get(pageNumber).render(state, 35, 100);

		Footer.renderAbout(state, (float) this.dimensions.getWidth() / 2,
				inch(8.0f));
	}

	private List<DisciplineBreakdownAreaRenderer> getDisciplineBreakdowns() {
		List<DisciplineBreakdownAreaRenderer> breakdownAreaRenderers = new ArrayList<DisciplineBreakdownAreaRenderer>();
		Dimension breakdownAreaSize = new Dimension((int) inch(10.0f),
				(int) inch(6.0f));

		List<Page> pages = getPages(breakdownAreaSize);

		for (Page page : pages) {
			breakdownAreaRenderers.add(new DisciplineBreakdownAreaRenderer(page));
		}

		return breakdownAreaRenderers;
	}

	private List<Page> getPages(Dimension size) {
		SortedMap<Discipline, SortedSet<Journal>> journalsByDiscipline = new TreeMap<Discipline, SortedSet<Journal>>();
		journalsByDiscipline.putAll(this.mapOfScience
				.getMappedJournalsByDiscipline());
		journalsByDiscipline.putAll(this.mapOfScience
				.getUnmappedJournalsByDiscipline());

		List<Page> pages = breakIntoPages(journalsByDiscipline, size,
				this.columnsPerPage);

		return pages;
	}

	private List<Page> breakIntoPages(
			SortedMap<Discipline, SortedSet<Journal>> givenJournalsByDiscipline,
			Dimension size, int numberOfColumns) {

		List<Column> columns = breakIntoColumns(givenJournalsByDiscipline,
				(int) size.getHeight());

		List<Page> pages = new ArrayList<Page>();

		if (numberOfColumns < 1) {
			throw new IllegalArgumentException(
					"The number of columns per page must be at least 1!");
		}

		Page unfilledPage = new Page(numberOfColumns, size);

		for (Column column : columns) {
			if (unfilledPage.isFull()) {
				pages.add(unfilledPage.getImmutableCopy());
				unfilledPage = new Page(numberOfColumns, size);
			}
			try {
				unfilledPage.addColumn(column);
			} catch (PageOutOfSpaceException e) {
				System.err.println("There is a programming error in DisciplineBreakdownPages#breakIntoPages");
				System.err.println("The unfilled page was checked to make sure it was not full, but it is full!");
				e.printStackTrace();
			}

		}

		if (unfilledPage.getColumns().size() > 0) {
			pages.add(unfilledPage);
		}

		return pages;
	}

	private List<Column> breakIntoColumns(
			SortedMap<Discipline, SortedSet<Journal>> givenJournalsByDiscipline,
			int columnSpace) {
		List<Column> columns = new ArrayList<Column>();
		TreeMap<Discipline, SortedSet<Journal>> journalsByDiscipline = new TreeMap<Discipline, SortedSet<Journal>>(
				givenJournalsByDiscipline);

		Column unfilledColumn = new Column(columnSpace);

		for (Discipline discipline : journalsByDiscipline.keySet()) {
			SortedSet<Journal> journals = journalsByDiscipline.get(discipline);

			ColumnEntry entry = new ColumnEntry(discipline);

			if (!unfilledColumn.spaceWillFit(entry.space())) {
				if (unfilledColumn.isEmpty()) {
					throw new IllegalArgumentException(
							"The column height of '"
									+ unfilledColumn.getTotalSpace()
									+ "' is too small to even put a discipline, which requires '"
									+ entry.space() + "'!");
				}
				// Time for a new column.
				columns.add(unfilledColumn.getImmutableCopy());
				unfilledColumn = new Column(columnSpace);
			}

			for (Journal journal : journals) {
				if (!unfilledColumn.spaceWillFit(entry
						.spaceWithOneMoreJournal())) {

					try {
						unfilledColumn.addEntry(entry);
						columns.add(unfilledColumn.getImmutableCopy());
						unfilledColumn = new Column(columnSpace);
						entry = new ColumnEntry(discipline);
					} catch (ColumnOutOfSpaceException e) {
						System.err.println("There is a programming error in DisciplineBreakdownPages#breakIntoColumns");
						System.err.println("The unfilled column was checked to make sure it could fit one more journal, but it can't!");
						e.printStackTrace();
					}
				}

				entry.addJournal(journal);
			}

			try {
				unfilledColumn.addEntry(entry);
			} catch (ColumnOutOfSpaceException e) {
				System.err.println("There is a programming error in DisciplineBreakdownPages#breakIntoColumns");
				System.err.println("The last unfilled column was checked to make sure it could fit one more journal, but it can't!");
				e.printStackTrace();
			}
		}

		columns.add(unfilledColumn.getImmutableCopy());
		return columns;
	}

	public static class DisciplineBreakdownCreationException extends Exception {
		private static final long serialVersionUID = -8717066429217445908L;

		public DisciplineBreakdownCreationException(String message) {
			super(message);
		}
	}

	public class Page {
		private final int numOfColumns;
		private List<Column> columns = new ArrayList<DisciplineBreakdownPages.Column>(
				this.numOfColumns);
		private Dimension size;

		public Page(int numOfColumns, Dimension size) {
			this.numOfColumns = numOfColumns;
			this.size = size;
		}

		public Page(int numOfColumns, Dimension dimension,
				ImmutableList<Column> columns) {
			this.numOfColumns = numOfColumns;
			this.size = dimension;
			this.columns = columns;
		}

		public Dimension getSize() {
			return this.size;
		}

		public int getNumberOfColumns() {
			return this.numOfColumns;
		}

		public boolean isFull() {
			return this.columns.size() >= this.numOfColumns;
		}

		public void addColumn(Column column) throws PageOutOfSpaceException {
			if (this.isFull()) {
				throw new PageOutOfSpaceException("The page is full");
			}

			this.columns.add(column.getImmutableCopy());
		}

		public List<Column> getColumns() {
			return this.columns;
		}

		public Page getImmutableCopy() {
			return new Page(this.numOfColumns, new Dimension(this.size),
					ImmutableList.copyOf(this.columns));
		}
	}

	public static class PageOutOfSpaceException extends Exception {
		private static final long serialVersionUID = -1775800544111832310L;

		public PageOutOfSpaceException(String message) {
			super(message);
		}
	}

	public class Column {
		private List<ColumnEntry> entries;
		private int totalSpace;
		private int spaceUsed;

		public int getTotalSpace() {
			return this.totalSpace;
		}

		public List<ColumnEntry> getColumnEntries() {
			return this.entries;
		}

		public Column(int totalSpace) {
			this.totalSpace = totalSpace;
			this.spaceUsed = 0;
			this.entries = new ArrayList<ColumnEntry>();
		}

		private Column(int totalSpace, int spacedUsed,
				ImmutableList<ColumnEntry> entries) {
			this.totalSpace = totalSpace;
			this.spaceUsed = spacedUsed;
			this.entries = entries;
		}

		public void addEntry(ColumnEntry entry)
				throws ColumnOutOfSpaceException {
			if (this.totalSpace < entry.space() + this.spaceUsed) {
				throw new ColumnOutOfSpaceException(
						"The column cannot fit the entry.  Space required ="
								+ entry.space() + ", space remaining ="
								+ (this.totalSpace - this.spaceUsed));
			}

			this.spaceUsed += entry.space();
			this.entries.add(entry.getImmutableCopy());
		}

		public boolean spaceWillFit(int space) {
			return space + this.spaceUsed <= this.totalSpace;
		}

		public boolean isEmpty() {
			return this.entries.size() == 0;
		}

		public Column getImmutableCopy() {
			return new Column(this.totalSpace, this.spaceUsed,
					ImmutableList.copyOf(this.entries));
		}

		@Override
		public String toString() {
			return "Column [entries=" + this.entries + "]";
		}

	}

	public static class ColumnOutOfSpaceException extends Exception {
		private static final long serialVersionUID = -9194969432252833086L;

		public ColumnOutOfSpaceException(String message) {
			super(message);
		}
	}

	/**
	 * A column entry has Discipline label and, optionally, some journals.
	 *
	 */
	public class ColumnEntry {
		Discipline discipline;
		SortedSet<Journal> journals;

		public ColumnEntry(Discipline discipline) {
			this.discipline = discipline;
			this.journals = new TreeSet<Journal>();
		}

		public SortedSet<Journal> getJournals() {
			return this.journals;
		}

		public Discipline getDiscipline() {
			return this.discipline;
		}

		private ColumnEntry(Discipline discipline,
				ImmutableSortedSet<Journal> journals) {
			this.discipline = discipline;
			this.journals = journals;
		}

		public void addJournal(Journal journal) {
			this.journals.add(journal);
		}

		public int space() {
			return spacedUsedBy(this.journals.size());
		}

		public int spaceWithOneMoreJournal() {
			return spacedUsedBy(this.journals.size() + 1);
		}

		public int spacedUsedBy(int numberOfJournals) {
			return 1 * disciplineSpace + numberOfJournals * journalSpace;
		}

		public ColumnEntry getImmutableCopy() {
			String disciplineId = this.discipline.getId();
			String disciplineName = this.discipline.getName();
			Color disciplineColor = this.discipline.getColor();
			return new ColumnEntry(new Discipline(disciplineId, disciplineName,
					new Color(disciplineColor.getRed(),
							disciplineColor.getGreen(),
							disciplineColor.getBlue(),
							disciplineColor.getAlpha())),
					ImmutableSortedSet.copyOf(this.journals));
		}

		@Override
		public String toString() {
			return "ColumnEntry [discipline=" + this.discipline
					+ ", #journals=" + this.journals.size() + "]";
		}

	}
}
