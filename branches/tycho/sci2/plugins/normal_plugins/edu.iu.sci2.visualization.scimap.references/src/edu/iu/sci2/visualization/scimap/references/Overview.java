package edu.iu.sci2.visualization.scimap.references;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antlr.stringtemplate.AutoIndentWriter;
import org.antlr.stringtemplate.StringTemplate;

import au.com.bytecode.opencsv.CSVWriter;

public class Overview {
	private String name;
	private List<Analysis> analyses = new ArrayList<Analysis>();
	private Map<String, String> rows = new HashMap<String, String>();
	private Combination combo = new Combination();
	private double scalingFactor;

	public Overview(String name, double scalingFactor) {
		this.name = name;
		this.scalingFactor = scalingFactor;
	}

	public void update(Analysis analysis) {
		this.combo.acquire(analysis);
		this.acquire(analysis);
		//this.addRow(analysis.getName(), analysis.getFound());
	}



	/*private void addRow(String name, Map<String, Integer> found) {
		rows.put(name, makeJournals(found));
	}

	private String makeJournals(Map<String, Integer> found) {
		Set<String> journals = new HashSet<String>();
		for(String journal : found.keySet()) {
			String pretty = MapOfScience.pretty.get(journal);
			Set<String> categoryNames = new HashSet<String>();
			for(JournalLocation location : MapOfScience.journals.get(journal)) {
				categoryNames.add(location.getCategory());
			}
			if(categoryNames.size() > 1) {
				continue;
			}
			journals.add(pretty);
		}
		
		
		
		return combine(journals);
	}

	private String combine(Set<String> journals) {
		String names = "";
		for(String journal : journals) {
			names += journal + "|";
		}
		return names.substring(0, names.length() - 1);
	}
	*/
	

	private void acquire(Analysis analysis) {
		analyses.add(analysis);
	}
	
	
	

	public void renderPostscript(Writer writer) throws IOException {
		MapOfScience map = new MapOfScience(combo.found, scalingFactor);
		
		StringTemplate overviewTemplate = MapReferences.group.getInstanceOf("overview"); //MapReferences.templatePaths.get("overview"));
		
		overviewTemplate.setAttribute("overviewMap", map.getPostscript());
		overviewTemplate.setAttribute("pages", this.analyses);
		overviewTemplate.setAttribute("clustering", new Clustering(this.analyses, scalingFactor));
		overviewTemplate.setAttribute("title", Utils.postscriptEscape(this.name));
		overviewTemplate.setAttribute("totals", combo.getTotals());
		overviewTemplate.setAttribute("number", Totals.formatter.format(this.analyses.size()));
		overviewTemplate.setAttribute("analysisTime", Utils.postscriptEscape(DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG).format(new Date())));
		overviewTemplate.setAttribute("scalingFactor", scalingFactor);
		
		
		overviewTemplate.setAttribute("categories", combo.getCollated());
		
		
		AutoIndentWriter templateWriter = new AutoIndentWriter(writer);
		overviewTemplate.write(templateWriter);
		
	}
	/*
	public void writeRows(File file) {
		try {
			PrintWriter writer = new PrintWriter(new FileOutputStream(file));
			writer.println("proposal,journals");
			for(String proposal : rows.keySet()) {
				String journals = rows.get(proposal);
				writer.println(proposal + "," + journals.replace(',', ' '));
			}
			writer.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	} */

	public void renderFound(Writer writer) {
		CSVWriter csv = new CSVWriter(writer);
		csv.writeNext(new String[]{"Proposal", "Journal Name", "Quantity"});
		for(Analysis analysis : analyses) {
			Map<String, Integer> found = analysis.getFound();
			for(String journal : found.keySet()) {
				String quantity = "" + found.get(journal);
				csv.writeNext(new String[]{analysis.getName(), journal, quantity});
			}
		}
	}

	public void renderUnfound(Writer writer) {
		CSVWriter csv = new CSVWriter(writer);
		csv.writeNext(new String[]{"Proposal", "Reference"});
		for(Analysis analysis : analyses) {
			Map<String, Integer> unfound = analysis.getUnfound();
			for(String reference : unfound.keySet()) {
				csv.writeNext(new String[]{analysis.getName(), reference});
			}
		}
	}

}
