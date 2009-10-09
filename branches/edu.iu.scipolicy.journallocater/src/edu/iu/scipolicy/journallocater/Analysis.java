package edu.iu.scipolicy.journallocater;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.antlr.stringtemplate.StringTemplate;

import edu.iu.scipolicy.journallocater.Category.JournalTotal;

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

	private Iterator<String> subsequences(final String name) {
		return new Iterator<String>() {

			private String[] words = name.split(" ");
			private boolean finished = false;
			private int ii = words.length - 1;
			private int jj = words.length - ii;

			public boolean hasNext() {
				return !finished ;
			}

			public String next() {
				String result = joinReference(Arrays.asList(words).subList(jj, jj + ii));
				jj = jj - 1;
				if(jj < 0) {
					ii = ii - 1;
					jj = words.length - ii;

					if(ii <= 0) {
						finished = true;
					}
				}
				return result;
			}

			public void remove() {
				throw new UnsupportedOperationException();
			}};
	}

	private void findJournals(Map<String, Integer> rawReferences, Map<String, Integer> found, Map<String, Integer> unfound) {
		for(String rawReference : rawReferences.keySet()) {
			String reference = normalizeForLookup(rawReference);
			if(MapOfScience.lookup.containsKey(reference)) {
				updateMap(found, MapOfScience.lookup.get(reference), rawReferences.get(rawReference));
			} else {
				boolean matched = false;
				Iterator<String> possibles = subsequences(reference);
				while(possibles.hasNext()) {
					String possible = possibles.next();
					if(MapOfScience.lookup.containsKey(possible)) {
						updateMap(found, MapOfScience.lookup.get(possible), rawReferences.get(rawReference));
						matched = true;
						break;
					}
				}
				if(!matched) {
					updateMap(unfound, Utils.postScriptEscape(rawReference), rawReferences.get(rawReference));
				}
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
		
		StringTemplate proposalTemplate = JournalLocaterAlgorithm.group.getInstanceOf("proposal");

		proposalTemplate.setAttribute("proposalMap", map.getPostScript());
		proposalTemplate.setAttribute("title", this.name);
		proposalTemplate.setAttribute("totals", this.getTotals());
		proposalTemplate.setAttribute("numberOfHitCategories", determineHitCategories(found).size());
		proposalTemplate.setAttribute("numberOfIDsHit", map.calculateNumberOfIDsHit());

		Collection<Category> categories = collateJournals(found);		
//		Set<String> hitCategories = determineHitCategories(found);
//		System.out.println(hitCategories.size() + " categories hit:");
//		List<String> asList = new ArrayList<String>(hitCategories);
//		Collections.sort(asList);
//		for (String hitCategory : asList) {
//			System.out.println("  " + hitCategory);
//		}
		
		proposalTemplate.setAttribute("listFont", Math.min(8, 260 / (found.size() + unfound.size() + categories.size() * 1.5 + 1)));
		proposalTemplate.setAttribute("categories", categories);
		proposalTemplate.setAttribute("unfound", fromMap(unfound));
		proposalTemplate.setAttribute("unfoundExist", unfound.size() > 0);
		proposalTemplate.setAttribute("analysisTime", Utils.postScriptEscape(DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG).format(new Date())));
		proposalTemplate.setAttribute("analysisDataLabel", Utils.postScriptEscape(inDataLabel));

		return proposalTemplate.toString();
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
	
	private Set<String> determineHitCategories(Map<String, Integer> found) {
		Set<String> hitCategories = new HashSet<String>();
		
		for(String journal : found.keySet()) {
			for(JournalLocation location : MapOfScience.journals.get(journal)) {
				hitCategories.add(location.getCategory());
			}
		}
		
		return hitCategories;
		
//		Map<String, Set<String>> hitCategories = new HashMap<String, Set<String>>();
//		
//		for(String journal : found.keySet()) {
//			for(JournalLocation location : MapOfScience.journals.get(journal)) {
////				if (!hitCategories.contains(location.getCategory())) {
////					System.out.println("Adding category\n  " + location.getCategory() + "\n for journal\n    " + location.getName());
////				}
//				
//				if (!hitCategories.containsKey(location.getName())) {
//					hitCategories.put(location.getName(), new HashSet<String>());
//				}
//				hitCategories.get(location.getName()).add(location.getCategory());
//				
////				hitCategories.put(location.getName(), location.getCategory());
//			}
//		}
//		
//		for (Entry<String, Set<String>> entry : hitCategories.entrySet()) {
//			System.out.println("The journal " + entry.getKey() + " has these categories:");
//			for (String category : entry.getValue()) {
//				System.out.println("  " + category);
//			}
//		}
//		
//		Set<String> unionOfCategories = new HashSet<String>();
//		for (Entry<String, Set<String>> entry : hitCategories.entrySet()) {
//			unionOfCategories.addAll(entry.getValue());
//		}
//		System.out.println("Union of categories:");
//		for (String category : unionOfCategories) {
//			System.out.println("  " + category);
//		}
//		
//		return unionOfCategories;
	}

	private Collection<Category> filterCategories(
			Map<String, Category> categories) {
		Set<Category> filtered = new HashSet<Category>();
		for(String key : categories.keySet()) {
			Category category = categories.get(key);
//			System.out.println("category " + category.getName() + " has #journals = " + category.getJournals().size());
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

	// TODO What's the right (stringtemplate-y) way to do this?
	public static List<JournalTotal> fromMap(
			Map<String, Integer> journalCountMap) {
		List<JournalTotal> journalCountList = new ArrayList<JournalTotal>();

		for (Entry<String, Integer> journalCount : journalCountMap.entrySet()) {
			journalCountList.add(
					new JournalTotal(
							journalCount.getKey(),
							journalCount.getValue()));
		}

		return journalCountList;
	}

	//	private static class JournalCount {
	//		private String name;
	//		private int count;
	//
	//		public JournalCount(String name, int count) {
	//			this.name = name;
	//			this.count = count;
	//		}
	//
	//		@SuppressWarnings("unused") // Used by stringtemplate
	//		public String getName() {
	//			return name;
	//		}
	//
	//		@SuppressWarnings("unused") // Used by stringtemplate
	//		public int getCount() {
	//			return count;
	//		}
	//	}
}
