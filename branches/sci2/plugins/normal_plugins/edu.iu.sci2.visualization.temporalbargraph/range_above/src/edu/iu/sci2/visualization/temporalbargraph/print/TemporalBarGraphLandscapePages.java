package edu.iu.sci2.visualization.temporalbargraph.print;

import static edu.iu.sci2.visualization.temporalbargraph.utilities.PostScriptFormationUtilities.POINTS_PER_INCH;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.antlr.stringtemplate.StringTemplate;
import org.cishell.utilities.color.ColorRegistry;
import org.joda.time.DateTime;

import au.com.bytecode.opencsv.CSVWriter;
import edu.iu.sci2.visualization.temporalbargraph.common.AbstractPages;
import edu.iu.sci2.visualization.temporalbargraph.common.AbstractTemporalBarGraphAlgorithmFactory;
import edu.iu.sci2.visualization.temporalbargraph.common.CategoryBreakdown;
import edu.iu.sci2.visualization.temporalbargraph.common.DoubleDimension;
import edu.iu.sci2.visualization.temporalbargraph.common.PageElement;
import edu.iu.sci2.visualization.temporalbargraph.common.PostScriptCreationException;
import edu.iu.sci2.visualization.temporalbargraph.common.Record;
import edu.iu.sci2.visualization.temporalbargraph.common.Visualization;

public class TemporalBarGraphLandscapePages extends AbstractPages {
	public static DecimalFormat formatter = new DecimalFormat("###,###");
	private Visualization visualizations;
	private DoubleDimension size;
	private String areaColumn;
	private String query;
	private String categoryColumn;
	private CategoryBreakdown categoryBreakdown;

	public TemporalBarGraphLandscapePages(CSVWriter csvWriter, List<Record> records,
			boolean scaleToOnePage, ColorRegistry<String> colorRegistry,
			DoubleDimension size, String areaColumn, String categoryColumn, String query)
			throws PostScriptCreationException {

		this.size = size;
		DoubleDimension visualizationSize = new DoubleDimension(size.getWidth()
				- (3 * (0.5 * POINTS_PER_INCH)),
				(size.getHeight() - (size.getHeight() * .35)));

		this.visualizations = new Visualization(csvWriter, records,
				visualizationSize, scaleToOnePage, colorRegistry);
		
		this.categoryBreakdown = new CategoryBreakdown(records, colorRegistry, 3, 400, 20);
		System.out.println(this.categoryBreakdown.numberOfPages());
		this.areaColumn = areaColumn;
		this.categoryColumn = categoryColumn;
		this.query = query;

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

	private Map<Integer, List<PageElement>> getPageElementsForSomePages() {
		Map<Integer, List<PageElement>> pageElementsSomePages = new TreeMap<Integer, List<PageElement>>();

		String visualizationDefinitions = this.visualizations
				.renderDefinitionsPostscript();
		int nextPage = 0;
		for (int ii = 0; ii < this.visualizations.numberOfVisualizations(); ii++) {
			String visualization = this.visualizations
					.renderVisualizationPostscript(ii);
			double visualizationLeft = 0.5 * POINTS_PER_INCH;
			double visualizationBottom = 1.75 * POINTS_PER_INCH;
			PageElement visualizationElement = new PageElement("visualization",
					visualizationLeft, visualizationBottom, visualization,
					visualizationDefinitions);

			List<PageElement> pageElements = pageElementsSomePages.get(nextPage);

			if (!pageElementsSomePages.containsKey(nextPage)) {
				pageElements = new ArrayList<PageElement>();
			}

			pageElements.add(visualizationElement);
			pageElements.add(getLegendPageElement());
			pageElements.add(getHowtoPageElement());
			pageElements.add(getAreaLegendElement());
			
			pageElementsSomePages.put(nextPage, pageElements);
			nextPage++;
		}
		
		String categoryBreakdownDefinitions = this.categoryBreakdown.renderPostscriptDefinitions();
		for (int ii = 0; ii < this.categoryBreakdown.numberOfPages(); ii++){
			

			List<PageElement> pageElements = pageElementsSomePages.get(nextPage);

			if (!pageElementsSomePages.containsKey(nextPage)) {
				pageElements = new ArrayList<PageElement>();
			}

			double categoryBreakdownLeft = 0.5 * POINTS_PER_INCH;
			double categoryBreakdownTop = 525;
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
		pageElements.add(getTitlePageElement());
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
		
		return new PageElement("areaLegend", 250, 100, areaTemplate, areaDefinitionsTemplate);
	}
	
	private PageElement getLegendPageElement() {
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
		legendDefinitionsTemplate.setAttribute("colorText1", colorText1);
		legendDefinitionsTemplate.setAttribute("colorText2", colorText2);

		double leftBound = 0.5 * POINTS_PER_INCH;
		double topBound = 100 - 14;
		return new PageElement("legendTitleTop", leftBound, topBound,
				legendTemplate, legendDefinitionsTemplate);
	}

	private PageElement getFooterPageElement() {
		StringTemplate footerTemplate = pageElementsGroup
				.getInstanceOf("footer");
		footerTemplate
				.setAttribute("footer",
						"NIH's Reporter Web site (projectreporter.nih.gov), NETE & CNS (cns.iu.edu)");

		StringTemplate footerDefinitionsTemplate = pageElementsGroup
				.getInstanceOf("footerDefinitions");
		footerDefinitionsTemplate.setAttribute("pageWidth", this.size.getWidth());

		double leftBound = this.size.getWidth() / 2;
		double bottomBound = 0;
		return new PageElement("footer", leftBound, bottomBound,
				footerTemplate, footerDefinitionsTemplate);
	}

	private PageElement getTitlePageElement() {
		StringTemplate titleTemplate = pageElementsGroup
				.getInstanceOf("pageHeading");
		
		
		StringTemplate titleDefinitionsTemplate = pageElementsGroup.getInstanceOf("pageHeadingDefinitions");
		titleDefinitionsTemplate.setAttribute("title", "Temporal Bar Graph");
		titleDefinitionsTemplate.setAttribute("query", this.query);
		titleDefinitionsTemplate.setAttribute("date", new DateTime().toString("MMMM dd, YYYY | h:mm a zzz"));
		
		double leftBound = 0.5 * POINTS_PER_INCH;
		double bottomBound = this.size.getHeight() - 0.25 * POINTS_PER_INCH;
		return new PageElement("pageHeading", leftBound, bottomBound, titleTemplate,
				titleDefinitionsTemplate);
	}

	private static PageElement getHowtoPageElement() {
		StringTemplate howtoTemplate = pageElementsGroup.getInstanceOf("howto");

		StringTemplate howtoDefinitionsTemplate = pageElementsGroup
				.getInstanceOf("howtoDefinitions");
		howtoDefinitionsTemplate.setAttribute("howtoTitleFontSize", 14);
		howtoDefinitionsTemplate.setAttribute("howtoTextFontSize", 10);

		double leftBound = 400.0;
		double topBound = 100.0;
		return new PageElement("howto", leftBound, topBound, howtoTemplate,
				howtoDefinitionsTemplate);
	}

}
