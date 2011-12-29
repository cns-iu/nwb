package edu.iu.scipolicy.visualization.scimap.journals;

import java.util.Set;
import java.util.TreeSet;

public class Category implements Comparable<Category> {
	public static class JournalTotal implements Comparable<JournalTotal> {
		private String innerName;
		private Integer total;

		public JournalTotal(String innerName, int total) {
			this.innerName = innerName;
			this.total = total;
		}

		public String getName() {
			return innerName;
		}

		public Integer getTotal() {
			return total;
		}

		public int compareTo(JournalTotal o) {
			return getName().compareTo(o.getName());
		}


	}

	public static final String MULTIPLE = "Multiple Categories";
	private String name;
	private Set<JournalTotal> journals = new TreeSet<JournalTotal>();
	private String color;
	private int categoryTotal = 0;

	public Category(String name, String color) {
		this.name = name;
		String[] components = color.split(":");
		double red = Double.parseDouble(components[0]) / 255.0;
		double green = Double.parseDouble(components[1]) / 255.0;
		double blue = Double.parseDouble(components[2]) / 255.0;
		this.color = "" + red + " " + green + " " + blue;
	}

	public int compareTo(Category o) {
		if(this.name.equals(MULTIPLE) && !o.name.equals(MULTIPLE)) {
			return 1;
		} else if(o.name.equals(MULTIPLE) && !this.name.equals(MULTIPLE)) {
			return -1;
		} else {
			return this.name.compareTo(o.name);
		}
	}

	public void addJournal(String journal, Integer total) {
		categoryTotal += total;
		journals.add(new JournalTotal(journal, total));
	}

	public String getName() {
		return name;
	}

	public String getColor() {
		return color;
	}

	public Set<JournalTotal> getJournals() {
		return journals;
	}

	public int getTotal() {
		return categoryTotal;
	}

}
