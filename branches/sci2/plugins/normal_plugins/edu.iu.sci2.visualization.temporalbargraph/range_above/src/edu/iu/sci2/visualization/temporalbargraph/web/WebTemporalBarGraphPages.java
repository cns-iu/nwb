package edu.iu.sci2.visualization.temporalbargraph.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.antlr.stringtemplate.StringTemplate;
import org.cishell.utilities.color.ColorRegistry;

import au.com.bytecode.opencsv.CSVWriter;
import edu.iu.sci2.visualization.temporalbargraph.common.AbstractPages;
import edu.iu.sci2.visualization.temporalbargraph.common.DoubleDimension;
import edu.iu.sci2.visualization.temporalbargraph.common.PageElement;
import edu.iu.sci2.visualization.temporalbargraph.common.PostScriptCreationException;
import edu.iu.sci2.visualization.temporalbargraph.common.Record;
import edu.iu.sci2.visualization.temporalbargraph.common.Visualization;

public class WebTemporalBarGraphPages extends AbstractPages{
	
	
	private Visualization visualizations;
	private DoubleDimension size;
	private String legendText;
	
	
	public WebTemporalBarGraphPages(CSVWriter csvWriter, List<Record> records,
			boolean scaleToOnePage, ColorRegistry<String> colorRegistry,
			DoubleDimension size, String legendText)
			throws PostScriptCreationException {
		this.size = size;
		this.legendText = legendText;
		
		DoubleDimension visualizationSize = new DoubleDimension(900.0, 600.0);
		
		visualizations = new Visualization(csvWriter, records,
				visualizationSize, scaleToOnePage, colorRegistry);
	}

	private Map<Integer, List<PageElement>> getPageElementsForSomePages() {
		Map<Integer, List<PageElement>> pageElementsSomePages = new TreeMap<Integer, List<PageElement>>();
		String visualizationDefinitions = visualizations.renderDefinitionsPostscript();
		
		for(int ii = 0; ii < numberOfPages(); ii++){
			String visualization = visualizations.renderVisualizationPostscript(ii);
			PageElement visualizationElement = new PageElement("visualization", 100.0, 300.0, visualization, visualizationDefinitions);
			
			List<PageElement> pageElements = pageElementsSomePages.get(ii);			
			if (!pageElementsSomePages.containsKey(ii)){
				pageElements = new ArrayList<PageElement>();
			}
			
			pageElements.add(visualizationElement);
			
			pageElementsSomePages.put(ii, pageElements);
		}
		
		return pageElementsSomePages;
	}

	private List<PageElement> getPageElementsForAllPages() {
		List<PageElement> pageElements = new ArrayList<PageElement>();
		pageElements.add(getLegendPageElement());
		pageElements.add(getFooterPageElement());
		return pageElements;
	}
	
	private PageElement getLegendPageElement(){
		StringTemplate legendTemplate = pageElementsGroup
				.getInstanceOf("legend");
		legendTemplate.setAttribute("title", this.legendText);
		legendTemplate.setAttribute("startYearLabel", "Start Year");
		legendTemplate.setAttribute("endYearLabel", "End Year");
		
		
		
		StringTemplate legendDefinitionsTemplate = pageElementsGroup
				.getInstanceOf("legendDefinitions");

		legendDefinitionsTemplate.setAttribute("legendWidth",
				size.getWidth() * 0.20);
		
		return new PageElement("legend", size.getWidth() * 0.25, size.getHeight() * 0.25, legendTemplate, legendDefinitionsTemplate);
	}
	
	private PageElement getFooterPageElement(){
		StringTemplate footerTemplate = pageElementsGroup.getInstanceOf("footer");
		footerTemplate.setAttribute("footer", "NIH's Reporter Web site (projectreporter.nih.gov), NETE & CNS (cns.iu.edu)");
		
		StringTemplate footerDefinitionsTemplate = pageElementsGroup
				.getInstanceOf("footerDefinitions");
		footerDefinitionsTemplate.setAttribute("pageWidth", size.getWidth());
		
		return new PageElement("footer", 0, size.getHeight() * 0.05, footerTemplate, footerDefinitionsTemplate);
	}

	@Override
	public int numberOfPages() {
		return visualizations.numberOfVisualizations();
	}

	@Override
	public void addPageElements() {
		addElementsAllPages(getPageElementsForAllPages());		
		addPageElementsSomePages(getPageElementsForSomePages());
	}
}
