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
import edu.iu.sci2.visualization.temporalbargraph.common.AbstractTemporalBarGraphAlgorithmFactory;
import edu.iu.sci2.visualization.temporalbargraph.common.CategoryBreakdown;
import edu.iu.sci2.visualization.temporalbargraph.common.DoubleDimension;
import edu.iu.sci2.visualization.temporalbargraph.common.PageElement;
import edu.iu.sci2.visualization.temporalbargraph.common.PostScriptCreationException;
import edu.iu.sci2.visualization.temporalbargraph.common.Record;
import edu.iu.sci2.visualization.temporalbargraph.common.Visualization;

public class WebTemporalBarGraphPages extends AbstractPages{
	public static DecimalFormat formatter = new DecimalFormat("###,###");	
	private Visualization visualizations;
	private DoubleDimension size;
	private String areaColumn;
	private String categoryColumn;
	private CategoryBreakdown categoryBreakdown;
	private String labelColumn;
	
	
	public WebTemporalBarGraphPages(CSVWriter csvWriter, List<Record> records,
			boolean scaleToOnePage, ColorRegistry<String> colorRegistry,
			DoubleDimension size, String areaColumn, String categoryColumn, String labelColumn)
			throws PostScriptCreationException {
		this.size = size;
		this.areaColumn = areaColumn;
		this.categoryColumn = categoryColumn;
		this.labelColumn = labelColumn;
		
		DoubleDimension visualizationSize = new DoubleDimension(1000.0, 725.0);
		
		this.visualizations = new Visualization(csvWriter, records,
				visualizationSize, scaleToOnePage, colorRegistry);
		this.categoryBreakdown = new CategoryBreakdown(records, colorRegistry, 4, 800, 20);
	}

	private Map<Integer, List<PageElement>> getPageElementsForSomePages() {
		Map<Integer, List<PageElement>> pageElementsSomePages = new TreeMap<Integer, List<PageElement>>();
		String visualizationDefinitions = this.visualizations.renderDefinitionsPostscript();
		
		int nextPage = 0;
		for(int ii = 0; ii < this.visualizations.numberOfVisualizations(); ii++){
			String visualization = this.visualizations.renderVisualizationPostscript(ii);
			PageElement visualizationElement = new PageElement("visualization", 100.0, 200.0, visualization, visualizationDefinitions);
			
			List<PageElement> pageElements = pageElementsSomePages.get(nextPage);			
			if (!pageElementsSomePages.containsKey(nextPage)){
				pageElements = new ArrayList<PageElement>();
			}
			
			pageElements.add(visualizationElement);
			pageElements.add(getLegendPageElement());
			pageElements.add(getAreaLegendElement());
			
			pageElementsSomePages.put(nextPage, pageElements);
			nextPage++;
		}
		
		String categoryBreakdownDefinitions = CategoryBreakdown.renderPostscriptDefinitions();
		for (int ii = 0; ii < this.categoryBreakdown.numberOfPages(); ii++){
			

			List<PageElement> pageElements = pageElementsSomePages.get(nextPage);

			if (!pageElementsSomePages.containsKey(nextPage)) {
				pageElements = new ArrayList<PageElement>();
			}

			double categoryBreakdownLeft = 100.0;
			double categoryBreakdownTop = 900;
			String categoryBreakdownPostscript = this.categoryBreakdown.renderPostscript(ii);
			
			PageElement categoryBreakdownElement = new PageElement("categoryBreakdown",
					categoryBreakdownLeft, categoryBreakdownTop, categoryBreakdownPostscript,
					categoryBreakdownDefinitions);
			pageElements.add(categoryBreakdownElement);
			pageElementsSomePages.put(nextPage, pageElements);
			nextPage++;
		}

		
		return pageElementsSomePages;
	}

	private List<PageElement> getPageElementsForAllPages() {
		List<PageElement> pageElements = new ArrayList<PageElement>();
		pageElements.add(getFooterPageElement());
		return pageElements;
	}
	
	private PageElement getAreaLegendElement() {
		double daysPerPoint = Math.pow(this.visualizations.getPointsPerDay(), -1);
		double yPerPoint = Math.pow(this.visualizations.getPointsPerY(), -1);
		double barWidth = 50;
		double yearValue = barWidth * daysPerPoint / 365.0;
		double bigBarHeight = 18;
		double bigBarValue = (daysPerPoint * barWidth) * (yPerPoint * bigBarHeight);
		double medBarHeight = 6;
		double medBarValue = (daysPerPoint * barWidth) * (yPerPoint * medBarHeight);
		double smallBarHeight = 2;
		double smallBarValue = (daysPerPoint * barWidth) * (yPerPoint * smallBarHeight);
		
		StringTemplate areaDefinitionsTemplate = pageElementsGroup.getInstanceOf("areaLegendDefinitions");
		areaDefinitionsTemplate.setAttribute("barWidth", String.valueOf(barWidth));
		areaDefinitionsTemplate.setAttribute("bigBarHeight", bigBarHeight);
		areaDefinitionsTemplate.setAttribute("medBarHeight", medBarHeight);
		areaDefinitionsTemplate.setAttribute("smallBarHeight", smallBarHeight);
		areaDefinitionsTemplate.setAttribute("bigBarValue", formatter.format(bigBarValue));
		areaDefinitionsTemplate.setAttribute("medBarValue", formatter.format(medBarValue));
		areaDefinitionsTemplate.setAttribute("smallBarValue", formatter.format(smallBarValue));
		areaDefinitionsTemplate.setAttribute("yearValue", formatter.format(yearValue));
		
		StringTemplate areaTemplate = pageElementsGroup.getInstanceOf("areaLegend");
		
		return new PageElement("areaLegend", 350, 150, areaTemplate, areaDefinitionsTemplate);
	}
	
	private PageElement getLegendPageElement(){
			StringTemplate legendTemplate = pageElementsGroup
					.getInstanceOf("legendTitleTop");
			
			String colorText1, colorText2;
			if (AbstractTemporalBarGraphAlgorithmFactory.DO_NOT_PROCESS_CATEGORY_VALUE.equals(this.categoryColumn)) {
				colorText1 = "";
				colorText2 = "";
			} else {
				colorText1 = "Color: " + this.categoryColumn;
				colorText2 = "See end of PDF for color legend.";
				
			}
			
			StringTemplate legendDefinitionsTemplate = pageElementsGroup
					.getInstanceOf("legendTitleTopDefinitions");
			legendDefinitionsTemplate.setAttribute("areaColumn", this.areaColumn);
			legendDefinitionsTemplate.setAttribute("minArea", formatter.format(this.visualizations.minRecordValue()));
			legendDefinitionsTemplate.setAttribute("maxArea", formatter.format(this.visualizations.maxRecordValue()));
			legendDefinitionsTemplate.setAttribute("labelColumn", this.labelColumn);
			legendDefinitionsTemplate.setAttribute("colorText1", colorText1);
			legendDefinitionsTemplate.setAttribute("colorText2", colorText2);

			double leftBound = 1.0 * POINTS_PER_INCH;
			double topBound = 150.0;
			return new PageElement("legendTitleTop", leftBound, topBound,
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
		return this.visualizations.numberOfVisualizations() + this.categoryBreakdown.numberOfPages();
	}

	@Override
	public void addPageElements() {
		addElementsAllPages(getPageElementsForAllPages());		
		addPageElementsSomePages(getPageElementsForSomePages());
	}
}
