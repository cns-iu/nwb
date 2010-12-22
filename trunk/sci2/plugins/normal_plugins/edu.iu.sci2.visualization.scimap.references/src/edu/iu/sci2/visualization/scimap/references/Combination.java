package edu.iu.sci2.visualization.scimap.references;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Combination {
	public Map<String, Integer> found = new HashMap<String, Integer>();
	public Map<String, Integer> unfound = new HashMap<String, Integer>();
	public Totals totals;

	private void updateFound(Map<String, Integer> found) {
		updateMap(found, this.found);
	}

	private void updateMap(Map<String, Integer> from, Map<String, Integer> to) {
		for(String key : from.keySet()) {
			if(to.containsKey(key)) {
				to.put(key, to.get(key) + from.get(key));
			} else {
				to.put(key, from.get(key));
			}
		}
	}

	private void updateUnfound(Map<String, Integer> unfound) {
		updateMap(unfound, this.unfound);
	}
	
	public Totals getTotals() {
		return new Totals(found, unfound);
	}
	
	public Collection<Category> getCollated() {
		Map<String, Category> categories = new HashMap<String, Category>();
		for(String color : MapOfScience.categories.keySet()) {
			String name = MapOfScience.categories.get(color);
			categories.put(name, new Category(name, color));
		}
		for(String journal : found.keySet()) {
			String pretty = MapOfScience.pretty.get(journal);
			Set<String> categoryNames = new HashSet<String>();
			for(JournalLocation location : MapOfScience.journals.get(journal)) {
				categoryNames.add(location.getCategory());
			}
			for(String categoryName : categoryNames) {
				categories.get(categoryName).addJournal(pretty, found.get(journal));
			}
		}
		return new TreeSet<Category>(categories.values());
	}

	public void acquire(Analysis analysis) {
		this.updateFound(analysis.getFound());
		this.updateUnfound(analysis.getUnfound());
	}
}
