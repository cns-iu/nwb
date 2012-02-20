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
import edu.iu.sci2.visualization.temporalbargraph.common.DoubleDimension;
import edu.iu.sci2.visualization.temporalbargraph.common.PageElement;
import edu.iu.sci2.visualization.temporalbargraph.common.PostScriptCreationException;
import edu.iu.sci2.visualization.temporalbargraph.common.Record;
import edu.iu.sci2.visualization.temporalbargraph.common.Visualization;

public class TemporalBarGraphLandscapePages extends AbstractPages {
	public static DecimalFormat formatter = new DecimalFormat("###,###");
	private Visualization visualizations;
	private DoubleDimension size;
	private String legendText;
	private String query;

	public TemporalBarGraphLandscapePages(CSVWriter csvWriter, List<Record> records,
			boolean scaleToOnePage, ColorRegistry<String> colorRegistry,
			DoubleDimension size, String legendText, String query)
			throws PostScriptCreationException {

		this.size = size;
		DoubleDimension visualizationSize = new DoubleDimension(size.getWidth()
				- (size.getWidth() * .25),
				(size.getHeight() - (size.getHeight() * .35)));

		this.visualizations = new Visualization(csvWriter, records,
				visualizationSize, scaleToOnePage, colorRegistry);

		this.legendText = legendText;
		this.query = query;

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

	private Map<Integer, List<PageElement>> getPageElementsForSomePages() {
		Map<Integer, List<PageElement>> pageElementsSomePages = new TreeMap<Integer, List<PageElement>>();

		String visualizationDefinitions = this.visualizations
				.renderDefinitionsPostscript();

		for (int ii = 0; ii < numberOfPages(); ii++) {
			String visualization = this.visualizations
					.renderVisualizationPostscript(ii);
			double visualizationLeft = 1.0 * POINTS_PER_INCH;
			double visualizationBottom = 1.75 * POINTS_PER_INCH;
			PageElement visualizationElement = new PageElement("visualization",
					visualizationLeft, visualizationBottom, visualization,
					visualizationDefinitions);

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
		pageElements.add(getHowtoPageElement());
		return pageElements;
	}

	private PageElement getLegendPageElement() {
		StringTemplate legendTemplate = pageElementsGroup
				.getInstanceOf("legendTitleTop");
		legendTemplate.setAttribute("title", this.legendText);
		legendTemplate.setAttribute("startYearLabel", "Start Year");
		legendTemplate.setAttribute("endYearLabel", "End Year");
		legendTemplate.setAttribute("min",
				formatter.format(this.visualizations.minRecordValue()));
		legendTemplate.setAttribute("max",
				formatter.format(this.visualizations.maxRecordValue()));

		StringTemplate legendDefinitionsTemplate = pageElementsGroup
				.getInstanceOf("legendTitleTopDefinitions");
		legendDefinitionsTemplate.setAttribute("legendWidth",
				this.size.getWidth() * 0.30);

		double leftBound = 1.0 * POINTS_PER_INCH;
		double bottomBound = 0.5 * POINTS_PER_INCH;
		return new PageElement("legendTitleTop", leftBound, bottomBound,
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
				.getInstanceOf("leftAlignedTitleWithQueryAndInfo");
		titleTemplate.setAttribute("title", "Temporal Bar Graph");
		titleTemplate.setAttribute("query", this.query);
		titleTemplate.setAttribute("date", new DateTime().toString("MMMM dd, YYYY | h:mm a zzz"));

		Map<String, String> attributes = new HashMap<String, String>();
		
		double titleFontSize = 14;
		String titleFontType = "Arial-BoldMT";
		Color titleFontColor = new Color(0x000000);
		attributes.put("titleFontSize", Double.toString(titleFontSize));
		attributes.put("titleFontType", titleFontType);
		float[] titleFontRGB = titleFontColor.getRGBColorComponents(null);
		assert(titleFontRGB.length == 3);
		attributes.put("titleFontColor", String.format("%f %f %f", titleFontRGB[0], titleFontRGB[1], titleFontRGB[2]));
		
		double otherFontSize = 10;
		String otherFontType = "ArialMT";
		Color otherFontColor = new Color(0x999999);
		attributes.put("otherFontSize", Double.toString(otherFontSize));
		attributes.put("otherFontType", otherFontType);
		float[] otherFontRGB = otherFontColor.getRGBColorComponents(null);
		assert(otherFontRGB.length == 3);
		attributes.put("otherFontColor", String.format("%f %f %f", otherFontRGB[0], otherFontRGB[1], otherFontRGB[2]));
		
		
		StringTemplate titleDefinitionsTemplate = pageElementsGroup.getInstanceOf("leftAlignedTitleWithQueryAndInfoDefinitions", attributes);

		double leftBound = 1.0 * POINTS_PER_INCH;
		double bottomBound = this.size.getHeight() - 1.0 * POINTS_PER_INCH;
		return new PageElement("leftAlignedTitleWithQueryAndInfo", leftBound, bottomBound, titleTemplate,
				titleDefinitionsTemplate);
	}

	private PageElement getHowtoPageElement() {
		StringTemplate howtoTemplate = pageElementsGroup.getInstanceOf("howto");

		StringTemplate howtoDefinitionsTemplate = pageElementsGroup
				.getInstanceOf("howtoDefinitions");
		howtoDefinitionsTemplate.setAttribute("howtoTitleFontSize", 10);
		howtoDefinitionsTemplate.setAttribute("howtoTextFontSize", 10);

		double leftBound = 6.75 * POINTS_PER_INCH;
		double bottomBound = 0.4 * POINTS_PER_INCH;
		return new PageElement("howto", leftBound, bottomBound, howtoTemplate,
				howtoDefinitionsTemplate);
	}

}
