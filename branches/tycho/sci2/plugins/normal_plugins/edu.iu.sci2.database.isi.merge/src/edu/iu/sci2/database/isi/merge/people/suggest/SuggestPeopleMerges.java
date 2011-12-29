package edu.iu.sci2.database.isi.merge.people.suggest;

import java.util.Dictionary;
import java.util.Map;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.service.database.Database;
import org.cishell.utilities.DataFactory;
import org.cishell.utilities.ToCaseFunction;

import prefuse.data.Table;
import uk.ac.shef.wit.simmetrics.similaritymetrics.InterfaceStringMetric;
import uk.ac.shef.wit.simmetrics.similaritymetrics.Jaro;
import uk.ac.shef.wit.simmetrics.similaritymetrics.JaroWinkler;
import uk.ac.shef.wit.simmetrics.similaritymetrics.Levenshtein;
import uk.ac.shef.wit.simmetrics.similaritymetrics.QGramsDistance;
import uk.ac.shef.wit.simmetrics.similaritymetrics.Soundex;

import com.google.common.collect.ImmutableMap;

import edu.iu.cns.database.merge.generic.prepare.marked.MergeMarker;
import edu.iu.cns.database.merge.generic.prepare.marked.grouping.CheckBasedGroupingStrategy;
import edu.iu.cns.database.merge.generic.prepare.marked.grouping.CompoundGroupingStrategy;
import edu.iu.cns.database.merge.generic.prepare.marked.grouping.KeyBasedGroupingStrategy;
import edu.iu.cns.database.merge.generic.prepare.marked.grouping.stringbased.StringSimilarityMergeCheck;
import edu.iu.nwb.shared.isiutil.database.ISI;
import edu.iu.sci2.database.isi.merge.people.IsiPersonPriorities;

public class SuggestPeopleMerges implements Algorithm {
	public static final String PERSON_TABLE_NAME = "APP.PERSON";
	
    private static final String METRIC = "metric";
	private static final String SIMILARITY = "similarity";
	private static final String NUM_PREFIX_LETTERS = "numPrefixLetters";
	private static final Map<String, InterfaceStringMetric> metrics = loadMetrics();
	private Data[] data;
    private CIShellContext context;
	private int prefixLength;
	private float similarityCutoff;
	private InterfaceStringMetric metric;
	private String metricName;
    
    public SuggestPeopleMerges(
    		Data[] data,
    		Dictionary<String, Object> parameters,
    		CIShellContext context) {
        this.data = data;
        this.context = context;
        this.prefixLength = (Integer) parameters.get(NUM_PREFIX_LETTERS);
        this.similarityCutoff = (Float) parameters.get(SIMILARITY);
        this.metricName = (String) parameters.get(METRIC);
		this.metric = metrics.get(metricName);
    }

    private static Map<String, InterfaceStringMetric> loadMetrics() {
    	//NOTE: if you change this, don't forget to change the metatype.
		return new ImmutableMap.Builder<String, InterfaceStringMetric>()
			.put("Jaro", new Jaro())
			.put("Levenshtein", new Levenshtein())
			.put("Jaro Winkler", new JaroWinkler())
			.put("Soundex", new Soundex())
			.put("q-gram", new QGramsDistance())
			.build();
	}

	public Data[] execute() throws AlgorithmExecutionException {
		MergeMarker mergeMarker =
    			new MergeMarker(
    					CompoundGroupingStrategy.compound(
    							new KeyBasedGroupingStrategy<String>(
    									new IsiFirstLettersFamilyName(prefixLength)),
    							new CheckBasedGroupingStrategy(
    	    							new StringSimilarityMergeCheck(
    	    									ISI.UNSPLIT_ABBREVIATED_NAME,
    	    									ToCaseFunction.LOWER,
    	    									metric,
    	    									similarityCutoff))),
    					new IsiPersonPriorities());
    	
    	Table mergingTable = mergeMarker.createMarkedMergingTable(
    			PERSON_TABLE_NAME, (Database) data[0].getData(), context);
    	
    	return new Data[]{ DataFactory.withClassNameAsFormat(
    			mergingTable,
				DataProperty.TABLE_TYPE,
				data[0],
				String.format(
						"suggested people merges (%s similarity >= %f, prefix %d)",
						metricName, similarityCutoff, prefixLength)) };
    }
	
	public static void main(String[] args) {
		for (InterfaceStringMetric metric : loadMetrics().values()) {
			System.out.println(metric.getShortDescriptionString());
			System.out.println(metric.getLongDescriptionString());
			System.out.println(metric.toString());
			System.out.println();
		}
	}
}