package edu.iu.sci2.visualization.temporalbargraph.common;

import java.awt.Color;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.cishell.utilities.color.ColorRegistry;

import com.google.common.collect.Lists;

import edu.iu.sci2.visualization.temporalbargraph.utilities.PostScriptFormationUtilities;

public class CategoryBreakdown {
	List<Category> categories;
	private int columnSize;
	private int categorySize;
	private int numberOfColumns;
	private List<Column> columnBreakdown;

	public static final String STRING_TEMPLATE_FILE_PATH = "/edu/iu/sci2/visualization/temporalbargraph/common/stringtemplates/category_breakdown.st";
	public static final StringTemplateGroup group;

	static {
		group = new StringTemplateGroup(new InputStreamReader(
				AbstractTemporalBarGraphAlgorithmFactory.class
						.getResourceAsStream(STRING_TEMPLATE_FILE_PATH)));
	}

	public CategoryBreakdown(List<Record> records,
			ColorRegistry<String> colorRegistry, int numberOfColumns,
			int columnSize, int categorySize) {
		if (columnSize < categorySize) {
			throw new IllegalArgumentException(
					"The column must be large enough for atleast one category!");
		}
		this.numberOfColumns = numberOfColumns;
		this.columnSize = columnSize;
		this.categorySize = categorySize;

		SortedMap<String, Category> categoryNameToCategory = new TreeMap<String, CategoryBreakdown.Category>();

		for (Record record : records) {
			if (record.getCategory() == edu.iu.sci2.visualization.temporalbargraph.common.Record.Category.DEFAULT
					.toString()) {
				continue;
			}
			Category category = categoryNameToCategory
					.get(record.getCategory());
			if (category == null) {
				category = new Category(record.getCategory(),
						colorRegistry.getColorOf(record.getCategory()),
						colorRegistry.getDefaultColor());
				categoryNameToCategory.put(category.getName(), category);
			}
			category.addMember(record);
		}

		this.categories = new ArrayList<CategoryBreakdown.Category>(
				categoryNameToCategory.values());
		this.columnBreakdown = breakIntoPostscriptColumns();
	}

	public int numberOfPages() {
		return Double.valueOf(
				Math.ceil((double) this.columnBreakdown.size()
						/ (double) this.numberOfColumns)).intValue();
	}

	// TODO document pagenumber is 0indexed
	public String renderPostscript(int pageNumber) {
		final String COLUMN_PS_KEYWORD = "categoryColumn";
		if (pageNumber > this.numberOfPages()) {
			throw new IllegalArgumentException("The page number '" + pageNumber
					+ "' is out of bounds.");
		}

		int firstColumnIndex = this.numberOfColumns * pageNumber;
		int finalColumnIndex = Math.min(
				firstColumnIndex + this.numberOfColumns,
				this.columnBreakdown.size());

		List<Column> columns = new ArrayList<Column>(
				this.columnBreakdown
						.subList(firstColumnIndex, finalColumnIndex));

		StringBuilder columnPostscript = new StringBuilder();
		for (Column column : columns) {
					
			// It needs to be reversed since postscript is stack oriented.
			for (Category category : Lists.reverse(column.getCategories())) {
				columnPostscript.append(String.format("(%s) %f %f %f ",
						category.getName(),
						category.getColor().getRed() / 255.0, category
								.getColor().getGreen() / 255.0, category
								.getColor().getBlue() / 255.0));
			}
			columnPostscript.append(column.getCategories().size() + " ")
					.append(columns.indexOf(column) + " ")
					.append(COLUMN_PS_KEYWORD)
					.append(System.getProperty("line.separator"));
		}

		return columnPostscript.toString();
	}

	public static String renderPostscriptDefinitions() {
		StringTemplate definitionsTemplate = group
				.getInstanceOf("categoryColumnDefinitions");
		return definitionsTemplate.toString();
	}

	public List<Column> breakIntoPostscriptColumns() {

		List<Column> columns = new ArrayList<CategoryBreakdown.Column>();
		Column currentColumn = new Column(this.columnSize, this.categorySize);
		
		List<Category> sortedCategories = this.categories;
		Collections.sort(sortedCategories);
		
		for (Category category : sortedCategories) {
			try {
				currentColumn.addCategory(category);
			} catch (ColumnFullException e) {
				columns.add(currentColumn);
				currentColumn = new Column(this.columnSize, this.categorySize);
				try {
					currentColumn.addCategory(category);
				} catch (ColumnFullException e1) {
					// This should not be reachable due to the argument check
					// in the constructor but just in case...
					throw new IllegalArgumentException(
							"The column was not found to be large enough to hold even one category");
				}
			}
		}

		if (currentColumn.getCategories().size() > 0) {
			columns.add(currentColumn);
		}

		return columns;
	}

	private class Column {
		private List<Category> categories;
		private int columnSize;
		private final int categorySize;

		public Column(int columnSize, int categorySize) {
			this.columnSize = columnSize;
			this.categorySize = categorySize;
			this.categories = new ArrayList<Category>();
		}

		public void addCategory(Category category) throws ColumnFullException {
			if (this.columnSize >= this.categorySize) {
				this.categories.add(category);
				this.columnSize -= this.categorySize;
			} else {
				throw new ColumnFullException();
			}
		}

		public List<Category> getCategories() {
			return this.categories;
		}

		@Override
		public String toString() {
			return "Column [categories=" + this.categories + ", columnSize="
					+ this.columnSize + ", categorySize=" + this.categorySize
					+ "]";
		}
	}

	private class ColumnFullException extends Exception {
		private static final long serialVersionUID = -6704418458099991290L;

		public ColumnFullException() {
			super();
		}
	}

	private class Category implements Comparable<Category> {
		private String name;
		private Color color;
		private List<Record> members;
		private final Color defaultColor;

		public Category(String name, Color color, Color defaultColor) {
			this(name, color, new ArrayList<Record>(), defaultColor);
		}

		public Category(String name, Color color, List<Record> members,
				Color defaultColor) {
			this.name = PostScriptFormationUtilities.matchParentheses(name);
			this.color = color;
			this.members = members;
			this.defaultColor = defaultColor;
		}

		public String getName() {
			return this.name;
		}

		public Color getColor() {
			return this.color;
		}

		public List<Record> getMembers() {
			return this.members;
		}

		public void addMember(Record record) {
			this.members.add(record);
		}

		@Override
		public int compareTo(Category other) {
			final int BEFORE = -1;
			final int EQUAL = 0;
			final int AFTER = 1;

			if (this == null || other == null) {
				throw new IllegalArgumentException(
						"You cannot compare null Categories!");
			}
			if (this == other)
				return EQUAL;
			if (this.color == other.color)
				return this.getName().compareTo(other.getName());
			if (this.color == this.defaultColor)
				return AFTER;
			if (other.color == this.defaultColor)
				return BEFORE;
			return this.getName().compareTo(other.getName());
		}
		
		

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((this.color == null) ? 0 : this.color.hashCode());
			result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Category other = (Category) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (this.color == null) {
				if (other.color != null)
					return false;
			} else if (!this.color.equals(other.color))
				return false;
			if (this.name == null) {
				if (other.name != null)
					return false;
			} else if (!this.name.equals(other.name))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "Category [name=" + this.name + ", color=" + this.color
					+ ", #members=" + this.members.size() + "]";
		}

		private CategoryBreakdown getOuterType() {
			return CategoryBreakdown.this;
		}

	}
}
