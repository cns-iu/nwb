package edu.iu.scipolicy.visualization.scimap.journals;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import org.antlr.stringtemplate.StringTemplate;

public class Analysis {
	public static final Pattern puncts = Pattern.compile("[^a-zA-Z0-9 ]+");

	private static List<Pattern> markers = new ArrayList<Pattern>();
	static {
		markers.add(Pattern.compile("\\n\\s*\\[.+?\\][^\\n]{10,}+"));
		markers.add(Pattern.compile("\\n\\s*\\(.+?\\)[^\n]{10,}+"));
		markers.add(Pattern.compile("\\n\\s*\\d+[^.\\n][^\\n]{9,}+"));
		markers.add(Pattern.compile("\\n\\s*\\d+\\.[^\\n]{10,}+"));
		markers.add(Pattern.compile("\\n\\s*\\[\\d+\\][^\\n]{10,}+"));
	}
	
	private String name;
	private String inDataLabel;

	private boolean failed = false;
	private String reason = "";
	
	private Map<String, Integer> found = new HashMap<String, Integer>();
	private Map<String, Integer> unfound = new HashMap<String, Integer>();

	public Analysis(Map<String, Integer> journalCounts, String inFileName, String inDataLabel) {
		this.name = inFileName;
		this.inDataLabel = inDataLabel;
		findJournals(journalCounts, found, unfound);
	}
	
	private void findJournals(
			Map<String, Integer> rawJournalNamesToCounts,
			Map<String, Integer> found,
			Map<String, Integer> unfound) {
		for(String rawJournalName : rawJournalNamesToCounts.keySet()) {
			String normalJournalName = normalizeForLookup(rawJournalName);
			if(MapOfScience.lookup.containsKey(normalJournalName)) {
				updateMap(
						found,
						MapOfScience.lookup.get(normalJournalName),
						rawJournalNamesToCounts.get(rawJournalName));
			} else {
				updateMap(
						unfound,
						Utils.postScriptEscape(rawJournalName),
						rawJournalNamesToCounts.get(rawJournalName));
			}
		}
	}

	private String normalizeForLookup(String journalName) {
		String normalizedJournalName =
			puncts.matcher(journalName).replaceAll("").trim().toLowerCase();
		return normalizedJournalName;
	}

	private void updateMap(Map<String, Integer> map, String name, int additional) {
		if(map.containsKey(name)) {
			map.put(name, map.get(name) + additional);
		} else {
			map.put(name, additional);
		}
	}

	protected String joinReference(List<String> reference) {
		StringBuilder builder = new StringBuilder();
		for(String line : reference) {
			if(line.trim().equals("")) {
				continue;
			}
			if(line.endsWith("-")) {
				builder.append(line.substring(0, line.length() - 1));
			} else {
				builder.append(line + " ");
			}
		}
		return builder.toString().trim();
	}

	public Map<String, Integer> getFound() {
		return found;
	}

	public Map<String, Integer> getUnfound() {
		return unfound;
	}

	public Totals getTotals() {
		return new Totals(found, unfound);
	}

	public String getPostScript() {
		if(this.failed)  {
			return "1 inch 5 inch moveto (" + this.reason + ") show showpage";
		}
		MapOfScience map = new MapOfScience(found);
		
		StringTemplate headerTemplate =
			ScienceMapAlgorithm.group.getInstanceOf("header");
		headerTemplate.setAttribute("creator", "Science Map via Journals (Circle Annotations)");
		headerTemplate.setAttribute("title", name);
		headerTemplate.setAttribute("boundingBoxString", "0 0 612 792");
		
		StringTemplate proposalTemplate =
			ScienceMapAlgorithm.group.getInstanceOf("proposal");

		proposalTemplate.setAttribute("proposalMap", map.getPostScript());
		proposalTemplate.setAttribute("title", this.name);
		proposalTemplate.setAttribute("totals", this.getTotals());
		proposalTemplate.setAttribute(
				"numberOfHitCategories",
				countCategoriesHit(found));
		proposalTemplate.setAttribute("numberOfIDsHit", map.getIDsHit());

		Collection<Category> categories = collateJournals(found);
		proposalTemplate.setAttribute(
				"listFont",
				calculateListFontSize(categories));
		proposalTemplate.setAttribute("categories", categories);
		
		proposalTemplate.setAttribute("unfound", unfound.entrySet());
		proposalTemplate.setAttribute("unfoundExist", (unfound.size() > 0));
		
		DateFormat longDateFormat =
			DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG);
		proposalTemplate.setAttribute(
				"analysisTime",
				Utils.postScriptEscape(longDateFormat.format(new Date())));
		
		proposalTemplate.setAttribute(
				"analysisDataLabel",
				Utils.postScriptEscape(inDataLabel));

		return headerTemplate.toString() + proposalTemplate.toString();
	}

	private double calculateListFontSize(Collection<Category> categories) {
		return Math.min(8, 260 / (found.size() + unfound.size() + categories.size() * 1.5 + 1));
	}

	private Collection<Category> collateJournals(Map<String, Integer> found) {
		Map<String, Category> categories = new HashMap<String, Category>();
		for(String color : MapOfScience.categories.keySet()) {
			String name = MapOfScience.categories.get(color);
			categories.put(name, new Category(name, color));
		}
		categories.put(Category.MULTIPLE, new Category(Category.MULTIPLE, "255:255:255"));
		
		for(String journal : found.keySet()) {			
			Set<String> categoryNames = new HashSet<String>();
			for(JournalLocation location : MapOfScience.journals.get(journal)) {
				categoryNames.add(location.getCategory());
			}
			
			String pretty = MapOfScience.pretty.get(journal);
			if(categoryNames.size() > 1) {
				categories.get(Category.MULTIPLE).addJournal(pretty, found.get(journal));
			} else if(categoryNames.size() == 1) {
				categories.get(categoryNames.iterator().next()).addJournal(pretty, found.get(journal));
			} else {
				//should be impossible
				System.err.println("Impossible: a found journal with no category");
			}
		}
		
		return new TreeSet<Category>(filterCategories(categories));
	}
	
	private int countCategoriesHit(Map<String, Integer> found) {
		Set<String> hitCategories = new HashSet<String>();
		
		Map<String, Set<String>> categories = new HashMap<String, Set<String>>();

		for(String journal : found.keySet()) {
			for(JournalLocation location : MapOfScience.journals.get(journal)) {
				if (!categories.containsKey(journal)) {
					categories.put(journal, new HashSet<String>());
				}
				
				categories.get(journal).add(location.getCategory());
				
				hitCategories.add(location.getCategory());
			}
		}
		
		return hitCategories.size();
	}

	private Collection<Category> filterCategories(
			Map<String, Category> categories) {
		Set<Category> filtered = new HashSet<Category>();
		for(String key : categories.keySet()) {
			Category category = categories.get(key);
			
			if(category.getJournals().size() > 0) {
				filtered.add(category);
			}
		}
		return filtered;
	}

	@Override
	public String toString() {
		return getPostScript();
	}

	public String getName() {
		return name;
	}
}
