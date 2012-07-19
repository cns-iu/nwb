package edu.iu.sci2.visualization.scimaps;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import oim.vivo.scimapcore.journal.Discipline;
import oim.vivo.scimapcore.journal.Edge;
import oim.vivo.scimapcore.journal.Journal;
import oim.vivo.scimapcore.journal.Node;
import oim.vivo.scimapcore.journal.Nodes;
import oim.vivo.scimapcore.mapping.DetailedScienceMappingResult;
import oim.vivo.scimapcore.mapping.ScienceMapping;

import com.google.common.collect.ImmutableMultiset;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Iterables;

import edu.iu.sci2.visualization.scimaps.analysis.AbstractTabularAnalysis;
import edu.iu.sci2.visualization.scimaps.analysis.DisciplineAnalysis;
import edu.iu.sci2.visualization.scimaps.analysis.SubdisciplineAnalysis;
import edu.iu.sci2.visualization.scimaps.analysis.SubdisciplineAnalysis.Subdiscipline;
import edu.iu.sci2.visualization.scimaps.journals.JournalDataset;

/*
 * This class represents the Map of Science.
 */
public class MapOfScience {
	public static final DecimalFormat FORMATTER = new DecimalFormat("###,###");

	private final String dataColumnName;
	private DetailedScienceMappingResult mappingResult;

	/**
	 * Creates a map of science. You must provide a mapping from the journal
	 * name to the hits for that journal.
	 */
	public MapOfScience(String dataColumnName, Map<String, Integer> journalOccurrences) {
		this(dataColumnName, ScienceMapping.generateDetailedScienceMappingResult(
				journalOccurrences));
	}
	
	public MapOfScience(String dataColumnName, DetailedScienceMappingResult mappingResult){
		this.dataColumnName = dataColumnName;
		this.mappingResult = mappingResult;
	}
	

	/**
	 * Return a float representing the number of found publications.
	 */
	public float countOfMappedPublications() {
		return this.mappingResult.getMappedJournals().size();
	}

	/**
	 * Return a string representing a pretty version of the found publications.
	 */
	public String prettyCountOfMappedPublications() {
		float count = countOfMappedPublications();
		return FORMATTER.format(count);
	}

	/**
	 * Return a float representing the number of unfound publications.
	 */
	public float countOfUnmappedPublications() {
		return this.mappingResult.getUnmappedJournals().size();
	}

	/**
	 * Return a string representing a pretty version of the unfound
	 * publications.
	 */
	public String prettyCountOfUnmappedPublications() {
		float count = countOfUnmappedPublications();
		return FORMATTER.format(count);
	}

	/**
	 * Return a float representing the number of total publications.
	 */
	public float countOfPublications() {
		return this.mappingResult.getJournals().size();
	}

	/**
	 * Return a string representing a pretty version of the total publications.
	 */
	public String prettyCountOfPublications() {
		float count = countOfPublications();
		return FORMATTER.format(count);
	}

	/**
	 * Return a float representing the number of found subdisciplines.
	 */
	public float countOfMappedSubdisciplines() {
		return this.mappingResult.getMappedResult().size();
	}

	/**
	 * Return a string representing a pretty version of the found subdisciplines.
	 */
	public String prettyCountOfMappedSubdisciplines() {
		float count = countOfMappedSubdisciplines();
		return FORMATTER.format(count);
	}

	/**
	 * Return a float representing the number of disciplines that contain all the
	 * publications.
	 */
	public float countOfDisciplinesUsed() {
		Set<Discipline> disciplinesUsed = new HashSet<Discipline>();

		Set<Integer> usedNodeIds = getMappedResults().keySet();
		for (int usedNodeId : usedNodeIds) {
			Discipline nodeDiscipline = Nodes.getNodeByID(usedNodeId).getDiscipline();
			disciplinesUsed.add(nodeDiscipline);
		}

		return disciplinesUsed.size();
	}

	/**
	 * Return a string representing a pretty version of the number of disciplines
	 * that contain all the publications.
	 */
	public String prettyCountOfDisciplinesUsed() {
		float count = countOfDisciplinesUsed();
		return FORMATTER.format(count);
	}

	/**
	 * Return a mapping of found publication node ids to their weight.
	 */
	public Map<Integer, Float> getMappedResults() {
		return this.mappingResult.getMappedResult();
	}

	/**
	 * Return a mapping of unfound publication names to the hitcount of those
	 * publications
	 */
	public Map<String, Float> getUnmappedResults() {
		return this.mappingResult.getUnmappedResult();
	}

	/**
	 * Return the total weights for all found node ids
	 */
	public Collection<Float> getMappedWeights() {
		return getMappedResults().values();
	}

	/**
	 * Return a set of all the node ids that matched publications found.
	 */
	public Set<Integer> getMappedIds() {
		return getMappedResults().keySet();
	}

	/**
	 * This will return a list of publication node's ids that were found ordered
	 * by the weight of that node.
	 */
	public List<Integer> getMappedIdsByWeight() {
		Integer[] mappedIds = getMappedIds().toArray(new Integer[] {});
		Arrays.sort(mappedIds, new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return getIdWeightMapping().get(o1).compareTo(
						getIdWeightMapping().get(o2));
			}
		});

		return Arrays.asList(mappedIds);
	}

	/**
	 * Returns a map linking the node id to the weight for that node.
	 */
	public Map<Integer, Float> getIdWeightMapping() {
		return this.mappingResult.getMappedResult();
	}

	/**
	 * Get a set of all the journals.
	 */
	public Set<Journal> getJournals() {
		return this.mappingResult.getJournals();
	}

	/**
	 * Get a set of all the edges used in the map of science.
	 */
	public static Set<Edge> getEdges() {
		return ScienceMapping.getEdges();
	}

	/**
	 * Get a set of all the disciplines used in the map of science.
	 */
	public static Set<Discipline> getDisciplines() {
		return ScienceMapping.getDisciplines();
	}

	/**
	 * Get a set of all the nodes used in the map of science.
	 */
	public static Set<Node> getNodes() {
		return ScienceMapping.getNodes();
	}

	/**
	 * Get a set of all the journals that were found for the map of science.
	 */
	/* XXX The oim.vivo.scimapcore.journal.Journal comparator is currently not consistent with
	 * equals. Check client code before changing the type of this set! */
	public Set<Journal> getMappedJournals() {
		return this.mappingResult.getMappedJournals();
	}

	/**
	 * Get a set of all the journals that were not found for the map of science.
	 */
	/* XXX The oim.vivo.scimapcore.journal.Journal comparator is currently not consistent with
	 * equals. Check client code before changing the type of this set! */
	public Set<Journal> getUnmappedJournals() {
		return this.mappingResult.getUnmappedJournals();
	}

	/**
	 * This gives a mapping for all the disciplines used by the mapped (used)
	 * journals.
	 */
	public SortedMap<Discipline, SortedSet<Journal>> getMappedJournalsByDiscipline() {
		return getJournalsByDiscipline(getMappedJournals());
	}

	/**
	 * This gives a mapping for all the disciplines used by unused journals. This
	 * should only return the NONE discipline.
	 */
	public SortedMap<Discipline, SortedSet<Journal>> getUnmappedJournalsByDiscipline() {
		return getJournalsByDiscipline(getUnmappedJournals());
	}

	/**
	 * Given a set of journals, it will return a mapping from the discipline used
	 * by those journals to a list of the journals in that discipline.
	 */
	public static SortedMap<Discipline, SortedSet<Journal>> getJournalsByDiscipline(
			Set<Journal> journals) {
		/* XXX The oim.vivo.scimapcore.journal.Journal comparator is currently not consistent with
		 * equals. Check client code before changing the type of this set! */
		SortedMap<Discipline, SortedSet<Journal>> disciplinesByJournal = new TreeMap<Discipline, SortedSet<Journal>>();

		for (Journal journal : journals) {
			Discipline discipline = journal.getJournalDiscipline();
			SortedSet<Journal> journalsForDiscipline = disciplinesByJournal.get(discipline);

			if (journalsForDiscipline == null) {
				journalsForDiscipline = new TreeSet<Journal>();
				journalsForDiscipline.add(journal);
			} else {
				journalsForDiscipline.add(journal);
			}

			disciplinesByJournal.put(discipline, journalsForDiscipline);
		}
		return disciplinesByJournal;
	}

	public String getDataColumnName() {
		return dataColumnName;
	}
	
	/**
	 * A tabular analysis of the number of journal occurrences in each discipline (including the
	 * special values {@link Discipline#MULTIPLE} and {@link Discipline#NONE}) of this map of
	 * science. Journal occurrences are figured by weighting the unique journals in each discipline
	 * by their number of occurrences in the dataset using {@code journalOccurrences}.
	 */
	public AbstractTabularAnalysis<Discipline> createDisciplineAnalysis(
			JournalDataset journalOccurrences) {
		ImmutableMultiset.Builder<Discipline> journalOccurrencesPerDiscipline =
				ImmutableMultiset.builder();
		
		/* XXX getMappedJournalsByDiscipline() and getUnmappedJournalsByDiscipline() return Maps to
		 * value type SortedSet<Journal>. These Maps are assembled by getJournalsByDiscipline(),
		 * whose input is a (Hash)Set<Journal>. But Journal, though it implements Comparable in
		 * terms of its String name, currently has only Object equality. But the Map returned here
		 * is built by code that is currently put()ting only once per (journal, count) anyway.
		 * 
		 * So ultimately this stuff kinda works out, but this is sort of incidental and flimsy. */
		Iterable<Entry<Discipline, SortedSet<Journal>>> allUniqueJournalsByDiscipline =
				Iterables.concat(
						getMappedJournalsByDiscipline().entrySet(),
						getUnmappedJournalsByDiscipline().entrySet());
		
		// For each discipline, whether mapped or unmapped..
		for (Entry<Discipline, SortedSet<Journal>> entry : allUniqueJournalsByDiscipline) {
			Discipline discipline = entry.getKey();
			
			/* Turn the set of oim.vivo.scimapcore.journal.Journal into the type JournalDataset
			 * understands */
			ImmutableSet<JournalDataset.Journal> disciplineJournals =
					JournalDataset.Journal.forVivoCoreJournals(entry.getValue());
			
			/* Total the count of occurrences of each journal mapped to this discipline, then set
			 * the discipline to that total. */
			journalOccurrencesPerDiscipline.setCount(
					discipline,
					journalOccurrences.totalCount(disciplineJournals));
		}
		
		return new DisciplineAnalysis(journalOccurrencesPerDiscipline.build());
	}
	
	/**
	 * A tabular analysis of the number of journal occurrences in each subdiscipline of this map of
	 * science. The Map returned by getIdWeightMapping() already accounts for multiplicity, but
	 * since getUnmappedJournals() does not, journal occurrences for it are figured by weighting
	 * the unique unmapped journals by their number of occurrences in the dataset using
	 * {@code journalOccurrences}.
	 */
	public AbstractTabularAnalysis<Subdiscipline> createSubdisciplineAnalysis(
			JournalDataset journalOccurrences) {
		ImmutableSortedMap.Builder<Subdiscipline, Float> journalDistributionPerSubdiscipline =
				ImmutableSortedMap.naturalOrder();
		
		/* getIdWeightMapping() is already counted with multiplicity, so journalOccurrences is not
		 * needed for this part. */
		for (Map.Entry<Integer, Float> entry : getIdWeightMapping().entrySet()) {
			journalDistributionPerSubdiscipline.put(
					Subdiscipline.forID(entry.getKey()), entry.getValue());
		}
		
		/* XXX getUnmappedJournals() returns a (Hash)Set<Journal>, but Journal, though it
		 * implements Comparable in terms of its String name, currently has only Object equality.
		 * But the underlying code is building the set from a map that is currently put()ting only
		 * once per (journal, count) anyway.
		 * 
		 * So ultimately the set of unmapped journals has one entry per journal, but this is sort
		 * of incidental and flimsy. */
		
		/* Turn the set of oim.vivo.scimapcore.journal.Journal into the type JournalDataset
		 * understands */
		ImmutableSet<JournalDataset.Journal> unmappedJournals =
				JournalDataset.Journal.forVivoCoreJournals(getUnmappedJournals());
		
		/* Total the count of occurrences of each unmapped journal, then set the discipline to that
		 * total. */
		int unmappedJournalOccurrencesCount = journalOccurrences.totalCount(unmappedJournals);
		
		return new SubdisciplineAnalysis(journalDistributionPerSubdiscipline.build(),
				unmappedJournalOccurrencesCount);
	}
}
