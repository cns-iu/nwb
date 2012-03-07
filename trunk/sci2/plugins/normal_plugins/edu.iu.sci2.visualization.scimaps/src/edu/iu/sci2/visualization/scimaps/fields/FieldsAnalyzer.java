package edu.iu.sci2.visualization.scimaps.fields;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import oim.vivo.scimapcore.journal.Discipline;
import oim.vivo.scimapcore.journal.Journal;
import oim.vivo.scimapcore.journal.Nodes;

public class FieldsAnalyzer {
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

	private Map<Integer, Float> found = new HashMap<Integer, Float>();
	private Map<Integer, String> ucsdAreaLabels;
	private Map<String, Float> unclassifiedLabelCounts;

	public FieldsAnalyzer(Map<Integer, Float> ucsdAreaTotals,
			Map<Integer, String> ucsdAreaLabels,
			Map<String, Float> unclassifiedLabelCounts) {
		this.found = ucsdAreaTotals;
		this.ucsdAreaLabels = ucsdAreaLabels;
		this.unclassifiedLabelCounts = unclassifiedLabelCounts;
	}

	public Set<Journal> getMappedFields() {
		Set<Journal> mappedJournals = new HashSet<Journal>();
		for (int nodeId : found.keySet()) {
			String journalName = ucsdAreaLabels.get(nodeId);
			int journalHitCount = Math.round(found.get(nodeId));
			Discipline journalDiscipline = Nodes.getNodeByID(nodeId).getDiscipline();
			Journal journal = new Journal(journalName, journalHitCount,
					journalDiscipline);
			mappedJournals.add(journal);
		}
		return mappedJournals;
	}

	public Set<Journal> getUnmappedFields() {
		Set<Journal> unmappedJournals = new HashSet<Journal>();
		for (String journalName : unclassifiedLabelCounts.keySet()) {
			int journalHitCount = Math.round(unclassifiedLabelCounts.get(journalName));
			Discipline journalDiscipline = Discipline.NONE;
			Journal journal = new Journal(journalName, journalHitCount,
					journalDiscipline);
			unmappedJournals.add(journal);
		}
		return unmappedJournals;
	}

	public Map<Integer, Float> getFound() {
		return found;
	}

	public Map<String, Float> getUnfound() {
		return unclassifiedLabelCounts;
	}

	protected String joinReference(List<String> reference) {
		StringBuilder builder = new StringBuilder();
		for (String line : reference) {
			if (line.trim().equals("")) {
				continue;
			}
			if (line.endsWith("-")) {
				builder.append(line.substring(0, line.length() - 1));
			} else {
				builder.append(line + " ");
			}
		}
		return builder.toString().trim();
	}

	double calculateListFontSize(Collection<Discipline> disciplines) {
		return Math.min(8, 260 / (found.size() + 0/* unfound.size() */
				+ disciplines.size() * 1.5 + 1));
	}

}
