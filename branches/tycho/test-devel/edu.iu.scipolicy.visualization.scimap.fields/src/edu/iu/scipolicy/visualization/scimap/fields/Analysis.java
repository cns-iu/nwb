package edu.iu.scipolicy.visualization.scimap.fields;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.antlr.stringtemplate.StringTemplate;

public class Analysis {
	
	
	
	public static final char DESCRIPTION_SEPARATOR = '\t';
	
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
	
	private Map<Integer, Integer> found = new HashMap<Integer, Integer>();
//	private Map<String, Integer> unfound = new HashMap<String, Integer>();
	private int goodRecordCount;
	private Map<Integer, String> ucsdAreaLabels;
	private Map<String, Integer> unclassifiedLabelCounts;
	private int unclassifiedRecordCount;

	private String nodeValueColumnName;

	public Analysis(
			Map<Integer, Integer> journalCounts,
			Map<Integer, String> ucsdAreaLabels,
			Map<String, Integer> unclassifiedLabelCounts,
			String inFileName,
			String inDataLabel,
			int recordCount,
			int unclassifiedRecordCount,
			String nodeValueColumnName) {
		this.name = inFileName;
		this.inDataLabel = inDataLabel;
//		findJournals(journalCounts, found, unfound);
		this.goodRecordCount = recordCount;
		this.found = journalCounts;
		this.ucsdAreaLabels = ucsdAreaLabels;
		this.unclassifiedLabelCounts = unclassifiedLabelCounts;
		this.unclassifiedRecordCount = unclassifiedRecordCount;
		this.nodeValueColumnName = nodeValueColumnName;
	}
	
//	private void findJournals(
//			Map<String, Integer> rawJournalNamesToCounts,
//			Map<String, Integer> found,
//			Map<String, Integer> unfound) {
//		for(String rawJournalName : rawJournalNamesToCounts.keySet()) {
//			String normalJournalName = normalizeForLookup(rawJournalName);
//			if(MapOfScience.lookup.containsKey(normalJournalName)) {
//				incrementAtKeyBy(
//						found,
//						MapOfScience.lookup.get(normalJournalName),
//						rawJournalNamesToCounts.get(rawJournalName));
//			} else {
//				incrementAtKeyBy(
//						unfound,
//						Utils.postScriptEscape(rawJournalName),
//						rawJournalNamesToCounts.get(rawJournalName));
//			}
//		}
//	}

//	private String normalizeForLookup(String journalName) {
//		String normalizedJournalName =
//			puncts.matcher(journalName).replaceAll("").trim().toLowerCase();
//		return normalizedJournalName;
//	}
//
//	private void incrementAtKeyBy(Map<String, Integer> map, String name, int additional) {
//		if(map.containsKey(name)) {
//			map.put(name, map.get(name) + additional);
//		} else {
//			map.put(name, additional);
//		}
//	}

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

//	public Map<Integer, Integer> getFound() {
//		return found;
//	}

//	public Map<String, Integer> getUnfound() {
//		return unfound;
//	}

	public Totals getTotals() {
		return new Totals(found);//, unfound);
	}

	public String getPostScript() {
		if(this.failed)  {
			return "1 inch 5 inch moveto (" + this.reason + ") show showpage";
		}
		MapOfScience map = new MapOfScience(found, nodeValueColumnName);
		
		StringTemplate headerTemplate =
			ScienceMapAlgorithm.group.getInstanceOf("header");
		headerTemplate.setAttribute("creator", "Science Map via 554 Fields (Circle Annotations)");
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
		
		proposalTemplate.setAttribute("goodRecordCount", goodRecordCount);
		proposalTemplate.setAttribute("totalRecordCount", goodRecordCount + unclassifiedRecordCount);
		
//		proposalTemplate.setAttribute("unfoundCount", unclassifiedCount);
//		proposalTemplate.setAttribute("unfoundTotal", unclassifiedTotal);
		proposalTemplate.setAttribute("unclassifiedExist", (unclassifiedLabelCounts.size() > 0));
		List<Entry<String, Integer>> unclassifiedEntries = new ArrayList<Entry<String, Integer>>(unclassifiedLabelCounts.entrySet());
		Collections.sort(unclassifiedEntries, new Comparator<Entry<String, Integer>>() {
			public int compare(Entry<String, Integer> entry1, Entry<String, Integer> entry2) {
				String key1 = entry1.getKey();
				String key2 = entry2.getKey();
				
				String[] tokens1 = key1.split(" ");
				String[] tokens2 = key2.split(" ");
				
				if (tokens1.length >= 1 && tokens2.length >= 1) {
					String codeString1 = tokens1[0];
					String codeString2 = tokens2[0];
					
					try {
						Integer code1 = Integer.valueOf(codeString1);
						Integer code2 = Integer.valueOf(codeString2);
						
						return code1.compareTo(code2);
					} catch (NumberFormatException e) {
						return key1.compareTo(key2);
					}					
				} else {
					return key1.compareTo(key2);
				}
			}			
		});
		proposalTemplate.setAttribute("unclassifiedLabelCounts", unclassifiedEntries);
		
		DateFormat longDateFormat =
			DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG);
		proposalTemplate.setAttribute(
				"analysisTime",
				Utils.postScriptEscape(longDateFormat.format(new Date())));
		
		proposalTemplate.setAttribute(
				"analysisDataLabel",
				Utils.postScriptEscape(inDataLabel));

		
		
		
		
		return proposalTemplate.toString();
	}

	private double calculateListFontSize(Collection<Category> categories) {
		return Math.min(8, 260 / (found.size() + 0/*unfound.size()*/ + categories.size() * 1.5 + 1));
	}

	private Collection<Category> collateJournals(Map<Integer, Integer> found) {
		Map<String, Category> categories = new HashMap<String, Category>();
		for(String color : MapOfScience.categories.keySet()) {
			String name = MapOfScience.categories.get(color);
			categories.put(name, new Category(name, color));
		}
		categories.put(Category.MULTIPLE, new Category(Category.MULTIPLE, "255:255:255"));
		
		for(Integer ucsdArea : found.keySet()) {
			Set<String> categoryNames = new HashSet<String>();
			String category = MapOfScience.nodes.get(ucsdArea).getCategory();
			
			categoryNames.add(category);
			
//			for(JournalLocation location : MapOfScience.journals.get(journal)) {
//				categoryNames.add(location.getCategory());
//			}
//			String pretty = "in Area " + String.valueOf(ucsdArea);
//			if (ucsdToKADescription.containsKey(ucsdArea)) {
//				pretty = ucsdToKADescription.get(ucsdArea);//MapOfScience.pretty.get(ucsdArea);
//			} else if (ucsdToUCSDDescription.containsKey(ucsdArea)) {
//				pretty = ucsdToUCSDDescription.get(ucsdArea);
//			}
			String pretty = "Area " + String.valueOf(ucsdArea);
			if (ucsdAreaLabels.containsKey(ucsdArea)) {
				pretty = ucsdAreaLabels.get(ucsdArea);
			}
			
			if(categoryNames.size() > 1) {
				categories.get(Category.MULTIPLE).addJournal(pretty, found.get(ucsdArea));
			} else if(categoryNames.size() == 1) {
				categories.get(categoryNames.iterator().next()).addJournal(pretty, found.get(ucsdArea));
			} else {
				//should be impossible
				System.err.println("Impossible: a found journal with no category");
			}
		}
		
		return new TreeSet<Category>(filterCategories(categories));
	}
	
	private int countCategoriesHit(Map<Integer, Integer> found) {
		Set<String> hitCategories = new HashSet<String>();
		
		Map<Integer, Set<String>> categories = new HashMap<Integer, Set<String>>();

		for(Integer ucsdArea : found.keySet()) {
			String category = MapOfScience.nodes.get(ucsdArea).getCategory();
			
			if (!categories.containsKey(ucsdArea)) {
				categories.put(ucsdArea, new HashSet<String>());
			}
			
			categories.get(ucsdArea).add(category);
			
			hitCategories.add(category);
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
