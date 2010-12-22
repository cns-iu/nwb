package edu.iu.sci2.visualization.scimap.references;

import java.text.DateFormat;
import java.util.Date;

import org.antlr.stringtemplate.AutoIndentWriter;
import org.antlr.stringtemplate.StringTemplate;

public class Cluster {
	private Similar[] similars;
	private Combination combo = new Combination();
	private double scalingFactor;

	public Cluster(Similar[] similars, double scalingFactor) {
		this.similars = similars;
		for(Similar similar : similars) {
			Analysis analysis = similar.getAnalysis();
			combo.acquire(analysis);
		}
		
		this.scalingFactor = scalingFactor;
	}

	public String getPostscript() {
		MapOfScience map = new MapOfScience(combo.found, scalingFactor);
		
		StringTemplate overviewTemplate = MapReferences.group.getInstanceOf("cluster");
		
		overviewTemplate.setAttribute("overviewMap", map.getPostscript());
		overviewTemplate.setAttribute("title", Utils.postscriptEscape(this.similars[0].getAnalysis().getName()));
		overviewTemplate.setAttribute("totals", combo.getTotals());
		overviewTemplate.setAttribute("number", Totals.formatter.format(this.similars.length - 1));
		overviewTemplate.setAttribute("similars", similars);
		
		overviewTemplate.setAttribute("categories", combo.getCollated());
		
		
		return overviewTemplate.toString();
	}
	
	public String toString() {
		return getPostscript();
	}
	
	public Combination getCombination() {
		return combo;
	}
	
	public Similar[] getSimilars() {
		return similars;
	}
}
