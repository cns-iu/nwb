package edu.iu.scipolicy.visualization.scimap.references;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import cern.colt.GenericSorting;
import cern.colt.function.DoubleProcedure;
import cern.colt.matrix.DoubleFactory2D;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.ObjectFactory1D;
import cern.colt.matrix.ObjectMatrix1D;

public class Clustering {	
	private static final int NUMBER_SIMILAR = 10;
	private DoubleMatrix2D similarities;
	private List<Analysis> analyses;
	private double scalingFactor;
	
	public Clustering(List<Analysis> analyses, double scalingFactor) {
		this.analyses = analyses;
		this.similarities = createSimilarityMatrix(analyses);
		this.scalingFactor = scalingFactor;
	}

	private DoubleMatrix2D createSimilarityMatrix(List<Analysis> analyses) {
		DoubleMatrix2D similarities = DoubleFactory2D.dense.make(analyses.size(), analyses.size());
		for(int ii = 0; ii < analyses.size(); ii++) {
			for(int jj = 0; jj < analyses.size(); jj++) {
				if(jj == ii) {
					similarities.setQuick(ii, ii, 1);
				} else if(similarities.getQuick(jj, ii) > 0) {
					similarities.setQuick(ii, jj, similarities.getQuick(jj, ii));
				} else {
					Analysis a1 = analyses.get(ii);
					Analysis a2 = analyses.get(jj);
					similarities.setQuick(ii, jj, calculateSimilarity(a1, a2));
				}
			}
		}
		return similarities;
	}
	
	private double calculateSimilarity(Analysis a1, Analysis a2) {
		
		Map<String, Integer> found1 = a1.getFound();
		Map<String, Integer> found2 = a2.getFound();
		if(found1.size() == 0 || found2.size() == 0) {
			return 0;
		}
		
		Set<String> unionJournals = new HashSet<String>(found1.keySet());
		unionJournals.addAll(found2.keySet());
		
		int overlap = 0;
		int total = 0;
		
		for(String journal : unionJournals) {
			int number1 = howMany(found1, journal);
			int number2 = howMany(found2, journal);
			overlap += Math.min(number1, number2);
			total += Math.max(number1, number2);
		}
		
		if (total == 0) {
			return 0;
		} else {
			return overlap * 1.0 / total;
		}
	}

	private int howMany(Map<String, Integer> found, String journal) {
		if(found.containsKey(journal)) {
			return found.get(journal);
		} else {
			return 0;
		}
	}

	public List<Cluster> getClusters() {
		List<Cluster> clusters = new ArrayList<Cluster>();
		
		int values = analyses.size();
		for(int ii = 0; ii < values; ii++) {
			DoubleMatrix1D analysisSim = similarities.viewRow(ii);
			clusters.add(new Cluster(topTen(analyses, analysisSim), scalingFactor));
		}
		return clusters;
	}

	private Similar[] topTen(List<Analysis> analyses, final DoubleMatrix1D analysisSim) {
		Set<Similar> similarAnalyses = new TreeSet<Similar>();
		int size = analyses.size();
		for(int ii = 0; ii < size; ii++) {
			similarAnalyses.add(new Similar(analyses.get(ii), analysisSim.get(ii)));
		}
		return (Similar[]) new ArrayList(similarAnalyses).subList(0, Math.min(NUMBER_SIMILAR + 1, size / 2)).toArray(new Similar[]{});
	}



}
