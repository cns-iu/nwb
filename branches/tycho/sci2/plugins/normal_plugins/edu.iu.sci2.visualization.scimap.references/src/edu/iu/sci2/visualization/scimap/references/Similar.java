package edu.iu.sci2.visualization.scimap.references;

import java.text.DecimalFormat;

public class Similar implements Comparable<Similar> {

	private Analysis analysis;
	private double similarity;
	private static DecimalFormat formatter = new DecimalFormat("#,##0.00");

	public Similar(Analysis analysis, double similarity) {
		this.analysis = analysis;
		this.similarity = similarity;
	}

	public int compareTo(Similar o) {
		if(o.similarity != this.similarity) {
			return Double.compare(o.similarity, this.similarity); //intentionally reverse order! we always want descending.
		} else {
			return this.analysis.getName().compareTo(o.analysis.getName()); //now use normal ordering
		}
	}
	
	public String getSimilarity() {
		return formatter.format(similarity);
	}
	
	public Analysis getAnalysis() {
		return analysis;
	}

}
