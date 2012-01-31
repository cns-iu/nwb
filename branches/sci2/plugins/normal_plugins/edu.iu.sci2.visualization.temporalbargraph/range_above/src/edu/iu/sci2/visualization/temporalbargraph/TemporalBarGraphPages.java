package edu.iu.sci2.visualization.temporalbargraph;

import static edu.iu.sci2.visualization.temporalbargraph.utilities.PostScriptFormationUtilities.POINTS_PER_INCH;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.antlr.stringtemplate.StringTemplate;
import org.cishell.utilities.color.ColorRegistry;
import org.joda.time.DateTime;

import au.com.bytecode.opencsv.CSVWriter;
import edu.iu.sci2.visualization.temporalbargraph.common.AbstractPages;
import edu.iu.sci2.visualization.temporalbargraph.common.DoubleDimension;
import edu.iu.sci2.visualization.temporalbargraph.common.PageElement;
import edu.iu.sci2.visualization.temporalbargraph.common.PostScriptCreationException;
import edu.iu.sci2.visualization.temporalbargraph.common.Record;
import edu.iu.sci2.visualization.temporalbargraph.common.Visualization;

public class TemporalBarGraphPages extends AbstractPages {
	public static DecimalFormat formatter = new DecimalFormat("###,###");
	private Visualization visualizations;
	private DoubleDimension size;
	private String legendText;
	private String query;

	public TemporalBarGraphPages(CSVWriter csvWriter, List<Record> records,
			boolean scaleToOnePage, ColorRegistry<String> colorRegistry,
			DoubleDimension size, String legendText, String query)
			throws PostScriptCreationException {

		this.size = size;
		DoubleDimension visualizationSize = new DoubleDimension(size.getWidth()
				- (size.getWidth() * .25),
				(size.getHeight() - (size.getHeight() * .35)));

		visualizations = new Visualization(csvWriter, records,
				visualizationSize, scaleToOnePage, colorRegistry);

		this.legendText = legendText;
		this.query = query;

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

	private Map<Integer, List<PageElement>> getPageElementsForSomePages() {
		Map<Integer, List<PageElement>> pageElementsSomePages = new TreeMap<Integer, List<PageElement>>();

		String visualizationDefinitions = visualizations
				.renderDefinitionsPostscript();

		for (int ii = 0; ii < numberOfPages(); ii++) {
			String visualization = visualizations
					.renderVisualizationPostscript(ii);
			PageElement visualizationElement = new PageElement("visualization",
					size.getWidth() * 0.10, size.getHeight() * 0.25,
					visualization, visualizationDefinitions);

			List<PageElement> pageElements = pageElementsSomePages.get(ii);

			if (!pageElementsSomePages.containsKey(ii)) {
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
		pageElements.add(getTitlePageElement());
		pageElements.add(getQueryInfoPageElement());
		pageElements.add(getHowtoPageElement());
		return pageElements;
	}

	private PageElement getLegendPageElement() {
		StringTemplate legendTemplate = pageElementsGroup
				.getInstanceOf("legendTitleTop");
		legendTemplate.setAttribute("title", this.legendText);
		legendTemplate.setAttribute("startYearLabel", "Start Year");
		legendTemplate.setAttribute("endYearLabel", "End Year");
		legendTemplate.setAttribute("min", formatter.format(this.visualizations.minRecordValue()));
		legendTemplate.setAttribute("max",  formatter.format(this.visualizations.maxRecordValue()));

		StringTemplate legendDefinitionsTemplate = pageElementsGroup
				.getInstanceOf("legendTitleTopDefinitions");
		legendDefinitionsTemplate.setAttribute("legendWidth",
				size.getWidth() * 0.30);

		return new PageElement("legendTitleTop", size.getWidth() * 0.10,
				size.getHeight() * 0.10, legendTemplate,
				legendDefinitionsTemplate);
	}

	private PageElement getFooterPageElement() {
		StringTemplate footerTemplate = pageElementsGroup
				.getInstanceOf("footer");
		footerTemplate
				.setAttribute("footer",
						"NIH's Reporter Web site (projectreporter.nih.gov), NETE & CNS (cns.iu.edu)");

		StringTemplate footerDefinitionsTemplate = pageElementsGroup
				.getInstanceOf("footerDefinitions");
		footerDefinitionsTemplate.setAttribute("pageWidth", size.getWidth());

		return new PageElement("footer", 0, 0, footerTemplate,
				footerDefinitionsTemplate);
	}

	private PageElement getTitlePageElement() {
		StringTemplate titleTemplate = pageElementsGroup
				.getInstanceOf("pageTitle");
		titleTemplate.setAttribute("title", "Horizontal Bar Graph");

		double titleFontSize = 14;
		StringTemplate titleDefinitionsTemplate = pageElementsGroup
				.getInstanceOf("pageTitleDefinitions");
		titleDefinitionsTemplate.setAttribute("titleFontSize", titleFontSize);

		return new PageElement("title", this.size.getWidth() / 2,
				this.size.getHeight() - (this.size.getHeight() * 0.05),
				titleTemplate, titleDefinitionsTemplate);
	}

	private PageElement getQueryInfoPageElement() {
		StringTemplate pageInfoTemplate = pageElementsGroup
				.getInstanceOf("queryinfo");
		pageInfoTemplate.setAttribute("query", this.query);
		pageInfoTemplate.setAttribute("date", new DateTime().toLocalDate()
				.toString("yyyy.MM.dd"));

		StringTemplate pageInfoDefintionsTemplate = pageElementsGroup
				.getInstanceOf("queryinfoDefinitions");
		pageInfoDefintionsTemplate.setAttribute("queryInfoFontSize", 10);

		return new PageElement("queryinfo", this.size.getWidth() / 2,
				this.size.getHeight() - (this.size.getHeight() * 0.08),
				pageInfoTemplate, pageInfoDefintionsTemplate);
	}

	private PageElement getHowtoPageElement() {
		StringTemplate howtoTemplate = pageElementsGroup.getInstanceOf("howto");

		StringTemplate howtoDefinitionsTemplate = pageElementsGroup
				.getInstanceOf("howtoDefinitions");
		howtoDefinitionsTemplate.setAttribute("howtoTitleFontSize", 14);
		howtoDefinitionsTemplate.setAttribute("howtoTextFontSize", 10);

		return new PageElement("howto", (size.getWidth() / 2)
				+ (0.10 * POINTS_PER_INCH), size.getHeight() * 0.10,
				howtoTemplate, howtoDefinitionsTemplate);
	}

}
