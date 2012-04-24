package edu.iu.sci2.visualization.scimaps.fields;

import java.util.Map;
import java.util.Set;

import oim.vivo.scimapcore.journal.Journal;
import oim.vivo.scimapcore.mapping.DetailedScienceMappingResult;

public class FieldsDetailedScienceMapping extends DetailedScienceMappingResult {

	public FieldsDetailedScienceMapping(Map<Integer, Float> mappedResult, Map<String, Float> unmappedResult, Set<Journal> mappedJournals, Set<Journal> unmappedJournals){
		setMappedResult(mappedResult);
		setUnmappedJournals(unmappedJournals);
		setMappedJournals(mappedJournals);
		setUnmappedResult(unmappedResult);
	}
}
