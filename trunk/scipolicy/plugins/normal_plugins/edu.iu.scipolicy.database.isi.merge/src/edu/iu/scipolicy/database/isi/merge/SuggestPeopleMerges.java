package edu.iu.scipolicy.database.isi.merge;

import java.util.Dictionary;
import java.util.Map;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.Data;

import uk.ac.shef.wit.simmetrics.similaritymetrics.InterfaceStringMetric;
import uk.ac.shef.wit.simmetrics.similaritymetrics.Jaro;
import uk.ac.shef.wit.simmetrics.similaritymetrics.JaroWinkler;
import uk.ac.shef.wit.simmetrics.similaritymetrics.Levenshtein;
import uk.ac.shef.wit.simmetrics.similaritymetrics.QGramsDistance;
import uk.ac.shef.wit.simmetrics.similaritymetrics.Soundex;

import com.google.common.collect.ImmutableMap;

import edu.iu.cns.database.merge.generic.maker.KeyMaker;
import edu.iu.cns.database.merge.generic.maker.MergeCheck;
import edu.iu.cns.database.merge.generic.maker.MergeMaker;
import edu.iu.cns.database.merge.generic.maker.PreferrableFormComparator;

public class SuggestPeopleMerges implements Algorithm {
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
    	String personTable = "APP.PERSON";

    	KeyMaker keyMaker = new IsiFirstLettersFamilyName(prefixLength);
    	MergeCheck mergeCheck = new IsiAuthorStringSimilarityMerge(metric);
    	PreferrableFormComparator preferrableFormComparator = new IsiPersonPriorities();
    	
    	
    	return MergeMaker.markTable(
    		personTable,
    		data[0],
    		keyMaker,
    		mergeCheck,
				preferrableFormComparator, context, "suggested people merges ("
						+ metricName + " similarity >= "
						+ similarityCutoff + ", prefix " + prefixLength + ")");
    }
}