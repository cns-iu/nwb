package edu.iu.cns.database.merge.generic.prepare.marked.grouping.stringbased;

import java.util.Map;

import uk.ac.shef.wit.simmetrics.similaritymetrics.InterfaceStringMetric;
import uk.ac.shef.wit.simmetrics.similaritymetrics.Jaro;
import uk.ac.shef.wit.simmetrics.similaritymetrics.JaroWinkler;
import uk.ac.shef.wit.simmetrics.similaritymetrics.Levenshtein;
import uk.ac.shef.wit.simmetrics.similaritymetrics.QGramsDistance;
import uk.ac.shef.wit.simmetrics.similaritymetrics.Soundex;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

public enum StringMetric {
	/* Pretty names must be unique.
	 * They are used to populate the corresponding algorithm parameter options. */
	JARO(new Jaro(), "Jaro"),
	LEVENSHTEIN(new Levenshtein(), "Levenshtein"),
	JARO_WINKLER(new JaroWinkler(), "Jaro Winkler"),
	SOUNDEX(new Soundex(), "Soundex"),
	QGRAM(new QGramsDistance(), "q-gram");
	
	private InterfaceStringMetric metric;
	private String prettyName;

	StringMetric(InterfaceStringMetric metric, String prettyName) {
		this.metric = metric;
		this.prettyName = prettyName;
	}
	
	public InterfaceStringMetric metric() {
		return this.metric;
	}
	
	public String prettyName() {
		return this.prettyName;
	}
	
	public static ImmutableMap<String, InterfaceStringMetric> prettyNameToMetric() {
		Map<String, InterfaceStringMetric> prettyNameToMetric = Maps.newHashMap();
		
		for (StringMetric stringMetric : StringMetric.values()) {
			prettyNameToMetric.put(stringMetric.prettyName(), stringMetric.metric());
		}
		
		return ImmutableMap.copyOf(prettyNameToMetric);
	}
	
	@Override
	public String toString() {
		return prettyName();
	}
}
