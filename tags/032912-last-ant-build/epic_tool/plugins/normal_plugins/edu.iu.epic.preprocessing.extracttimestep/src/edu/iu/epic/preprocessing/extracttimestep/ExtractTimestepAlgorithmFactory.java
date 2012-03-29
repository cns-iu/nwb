package edu.iu.epic.preprocessing.extracttimestep;

import java.io.File;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Set;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.cishell.utilities.MutateParameterUtilities;
import org.osgi.service.metatype.ObjectClassDefinition;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multiset.Entry;
import com.google.common.collect.Ordering;
import com.google.common.primitives.Ints;

import edu.iu.nwb.util.nwbfile.NWBFileUtilities;
import edu.iu.nwb.util.nwbfile.NWBMetadataParsingException;

public class ExtractTimestepAlgorithmFactory implements AlgorithmFactory, ParameterMutator {
	public static final String TIMESTEP_COLUMN_NAME_PREFIX_ID = "timestepColumnNamePrefix";

	public Algorithm createAlgorithm(Data[] data,
    								 Dictionary<String, Object> parameters,
    								 CIShellContext ciShellContext) {
    	File nwbFile = (File) data[0].getData();
    	int timestep = (Integer) parameters.get("timestep");
    	String timestepColumnNamePrefix = (String) parameters.get(TIMESTEP_COLUMN_NAME_PREFIX_ID);
    	
        return new ExtractTimestepAlgorithm(nwbFile, timestep, timestepColumnNamePrefix, data[0]);
    }

	public ObjectClassDefinition mutateParameters(Data[] data, ObjectClassDefinition oldOCD) {
		File nwbFile = (File) data[0].getData();
		
		ObjectClassDefinition withTimestepAttributeNameChoices = oldOCD;
		try {
			withTimestepAttributeNameChoices =
				populateTimestepAttributeNameChoices(oldOCD, nwbFile);
		} catch (NWBMetadataParsingException e) {
			// TODO Log warning?
		}
		
		// TODO Also mutate timestep parameter to bound max value?
		
		return withTimestepAttributeNameChoices;
	}

	private ObjectClassDefinition populateTimestepAttributeNameChoices(
			ObjectClassDefinition oldOCD, File nwbFile) throws NWBMetadataParsingException {
		Set<String> nodeAttributeNames = readNwbNodeAttributeNames(nwbFile);		
		Multiset<String> nodeAttributeNamePrefixes = extractPrefixes(nodeAttributeNames);		
		Collection<String> nodeAttributeNamePrefixesByCountDescending =
			sortByCountDescending(nodeAttributeNamePrefixes);
	
		return MutateParameterUtilities.mutateToDropdown(
				oldOCD,
				TIMESTEP_COLUMN_NAME_PREFIX_ID,
				nodeAttributeNamePrefixesByCountDescending,
				nodeAttributeNamePrefixesByCountDescending);
	}

	private Set<String> readNwbNodeAttributeNames(File nwbFile) throws NWBMetadataParsingException {
		return ImmutableSet.copyOf(NWBFileUtilities.getNodeSchema(nwbFile).keySet());
	}

	private Collection<String> sortByCountDescending(Multiset<String> nodeAttributeNamePrefixes) {
		Ordering<Entry<?>> byCountDescending = new ByCountAscending().reverse();

		return Collections2.transform(
				byCountDescending.sortedCopy(nodeAttributeNamePrefixes.entrySet()),
				new Function<Entry<String>, String>() {
					public String apply(Entry<String> entry) {
						return entry.getElement();
					}						
				});
	}

	private Multiset<String> extractPrefixes(Set<String> nodeAttributeNames) {
		// Replace any digit strings ending a node attribute name with the empty string
		return HashMultiset.create(
				Collections2.transform(nodeAttributeNames, new Function<String, String>() {
					public String apply(String integerColumnName) {
						return integerColumnName.replaceFirst("\\d+$", "");
					}
				}));
	}
	
	protected static class ByCountAscending extends Ordering<Multiset.Entry<?>> {
		public int compare(Entry<?> left, Entry<?> right) {
			return Ints.compare(left.getCount(), right.getCount());
		}		
	}
}