package edu.iu.sci2.visualization.temporalbargraph.web;

import static edu.iu.sci2.visualization.temporalbargraph.utilities.PostScriptFormationUtilities.POINTS_PER_INCH;

import java.text.DecimalFormat;
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
	public static DecimalFormat formatter = new DecimalFormat("###,###");	
	private Visualization visualizations;
	private DoubleDimension size;
	private String legendText;
	private String categoryText;
	
	
	public WebTemporalBarGraphPages(CSVWriter csvWriter, List<Record> records,
			boolean scaleToOnePage, ColorRegistry<String> colorRegistry,
			DoubleDimension size, String legendText, String categoryText)
			throws PostScriptCreationException {
		this.size = size;
		this.legendText = legendText;
		this.categoryText = categoryText;
		
		DoubleDimension visualizationSize = new DoubleDimension(1000.0, 775.0);
		
		this.visualizations = new Visualization(csvWriter, records,
				visualizationSize, scaleToOnePage, colorRegistry);
	}

	private Map<Integer, List<PageElement>> getPageElementsForSomePages() {
		Map<Integer, List<PageElement>> pageElementsSomePages = new TreeMap<Integer, List<PageElement>>();
		String visualizationDefinitions = this.visualizations.renderDefinitionsPostscript();
		
		for(int ii = 0; ii < numberOfPages(); ii++){
			String visualization = this.visualizations.renderVisualizationPostscript(ii);
			PageElement visualizationElement = new PageElement("visualization", 100.0, 150.0, visualization, visualizationDefinitions);
			
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
					.getInstanceOf("legendTitleTop");
			legendTemplate.setAttribute("title", this.legendText);
			legendTemplate.setAttribute("category", this.categoryText);
			legendTemplate.setAttribute("min",
					formatter.format(this.visualizations.minRecordValue()));
			legendTemplate.setAttribute("max",
					formatter.format(this.visualizations.maxRecordValue()));

			StringTemplate legendDefinitionsTemplate = pageElementsGroup
					.getInstanceOf("legendTitleTopDefinitions");

			double leftBound = 1.0 * POINTS_PER_INCH;
			double bottomBound = 50.0;
			return new PageElement("legendTitleTop", leftBound, bottomBound,
					legendTemplate, legendDefinitionsTemplate);
	}
	
	private PageElement getFooterPageElement(){
		StringTemplate footerTemplate = pageElementsGroup.getInstanceOf("footer");
		footerTemplate.setAttribute("footer", "NIH's Reporter Web site (projectreporter.nih.gov), NETE & CNS (cns.iu.edu)");
		
		StringTemplate footerDefinitionsTemplate = pageElementsGroup
				.getInstanceOf("footerDefinitions");
		footerDefinitionsTemplate.setAttribute("pageWidth", this.size.getWidth());
		
		return new PageElement("footer", 0, 25.0, footerTemplate, footerDefinitionsTemplate);
	}

	@Override
	public int numberOfPages() {
		return this.visualizations.numberOfVisualizations();
	}

	@Override
	public void addPageElements() {
		addElementsAllPages(getPageElementsForAllPages());		
		addPageElementsSomePages(getPageElementsForSomePages());
	}
}
