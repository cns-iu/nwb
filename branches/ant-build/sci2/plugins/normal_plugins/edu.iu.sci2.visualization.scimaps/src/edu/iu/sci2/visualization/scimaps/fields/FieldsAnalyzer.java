package edu.iu.sci2.visualization.scimaps.fields;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import oim.vivo.scimapcore.journal.Discipline;
import oim.vivo.scimapcore.journal.Journal;
import oim.vivo.scimapcore.journal.Nodes;

public class FieldsAnalyzer {
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
		for (int nodeId : this.found.keySet()) {
			String journalName = this.ucsdAreaLabels.get(nodeId);
			int journalHitCount = Math.round(this.found.get(nodeId));
			Discipline journalDiscipline = Nodes.getNodeByID(nodeId).getDiscipline();
			Journal journal = new Journal(journalName, journalHitCount,
					journalDiscipline);
			mappedJournals.add(journal);
		}
		return mappedJournals;
	}

	public Set<Journal> getUnmappedFields() {
		Set<Journal> unmappedJournals = new HashSet<Journal>();
		for (String journalName : this.unclassifiedLabelCounts.keySet()) {
			int journalHitCount = Math.round(this.unclassifiedLabelCounts.get(journalName));
			Discipline journalDiscipline = Discipline.NONE;
			Journal journal = new Journal(journalName, journalHitCount,
					journalDiscipline);
			unmappedJournals.add(journal);
		}
		return unmappedJournals;
	}

	public Map<Integer, Float> getFound() {
		return this.found;
	}

	public Map<String, Float> getUnfound() {
		return this.unclassifiedLabelCounts;
	}
}
