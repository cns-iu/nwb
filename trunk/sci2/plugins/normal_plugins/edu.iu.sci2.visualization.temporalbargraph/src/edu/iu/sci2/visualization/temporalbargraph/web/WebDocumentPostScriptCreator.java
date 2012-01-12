package edu.iu.sci2.visualization.temporalbargraph.web;

import java.util.Collections;
import java.util.List;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.joda.time.LocalDate;

import au.com.bytecode.opencsv.CSVWriter;
import edu.iu.sci2.visualization.temporalbargraph.common.PostScriptCreationException;
import edu.iu.sci2.visualization.temporalbargraph.common.Record;

public class WebDocumentPostScriptCreator {
	private CSVWriter csvWriter;
	private List<Record> records;
	private boolean scaleToOnePage;
	
	private static double PIXELS_PER_INCH = 72;
	private static double POINTS_PER_INCH = 72;
	
	private static double WEB_HEIGHT = 960; // in pixels
	private static double WEB_WIDTH = 1280; // in pixels
	
	public static StringTemplateGroup group = WebTemporalBarGraphAlgorithmFactory.loadTemplates();
	
	public WebDocumentPostScriptCreator(CSVWriter csvWriter,
			List<Record> records, boolean scaleToOnePage) {
		this.csvWriter = csvWriter;
		this.records = records;
		this.scaleToOnePage = scaleToOnePage;
	}

	
	public String createPostScript() throws PostScriptCreationException{
		

		/**
		 * A postscript document looks like the following:
		 * Header
		 * Procedure Definitions
		 * Document Setup
		 * Pages
		 * Document Trailer
		 */
		
		StringBuilder documentPostScript = new StringBuilder();
		
		double pageHeight = WEB_HEIGHT / PIXELS_PER_INCH * POINTS_PER_INCH;
		double pageWidth = WEB_WIDTH / PIXELS_PER_INCH * POINTS_PER_INCH;

		// I don't like this, what would be a better way?
		WebVizArea vizArea = new WebVizArea(csvWriter, records, pageWidth, pageHeight, scaleToOnePage);
		List<String> pages = vizArea.getPostScriptPages();
		
		documentPostScript.append(getHeader(pages.size()));
		documentPostScript.append(getProcedureDefinitions());
		documentPostScript.append(getDocumentSetup(pageHeight, pageWidth));
		documentPostScript.append(getPages(pageHeight, pageWidth, vizArea));
		documentPostScript.append(getDocumentTrailer());

		return documentPostScript.toString();
	}

	private static String getDocumentTrailer() {
		StringTemplate documentTrailerTemplate =
				group.getInstanceOf("documentTrailer");
		return documentTrailerTemplate.toString();
	}

	private static String getPage(String vizAreaOfPage, int pageNumber){
		StringBuilder pagePostScript = new StringBuilder();
		
		StringTemplate pageSetupTemplate =
				group.getInstanceOf("pageSetup");
		pageSetupTemplate.setAttribute("pageNumber", pageNumber);
		pagePostScript.append(pageSetupTemplate.toString());
		
		StringTemplate scalebarScaleBarTemplate =
				group.getInstanceOf("scalebarScaleBar");
		scalebarScaleBarTemplate.setAttribute("title", "Area size equals numberical value");
		scalebarScaleBarTemplate.setAttribute("startYearLabel", "Start Year");
		scalebarScaleBarTemplate.setAttribute("endYearLabel", "End Year");
		pagePostScript.append(scalebarScaleBarTemplate.toString());
		
		StringTemplate footer = group.getInstanceOf("footer");
		footer.setAttribute("footer", "NIH's Reporter Web site (projectreporter.nih.gov), NETE & CNS (cns.iu.edu)");
		pagePostScript.append(footer.toString());
		
		pagePostScript.append(vizAreaOfPage);
		
		StringTemplate pageTearDownTemplate =
				group.getInstanceOf("pageTearDown");
		pagePostScript.append(pageTearDownTemplate.toString());
		return pagePostScript.toString();
	}
	
	private String getPages(double pageHeight, double pageWidth, WebVizArea vizArea) {
		
		StringBuilder pagesPostScript = new StringBuilder();
		
		Collections.sort(records, Record.START_DATE_ORDERING);
		
		StringTemplate pageUtilitiesTemplate =
				group.getInstanceOf("pageUtilities");
		pagesPostScript.append(pageUtilitiesTemplate.toString());
		
		
		pagesPostScript.append(vizArea.getPostScriptVisualizationDefinitions());
		
		StringTemplate scalebarAreaDefinitionsTemplate =
				group.getInstanceOf("scalebarAreaDefinitions");
		scalebarAreaDefinitionsTemplate.setAttribute("scaleBarLeftPosition", pageWidth * 0.25);
		scalebarAreaDefinitionsTemplate.setAttribute("scaleBarBottomPosition", pageHeight * 0.25);
		scalebarAreaDefinitionsTemplate.setAttribute("scaleBarWidth", pageWidth * 0.20);
		pagesPostScript.append(scalebarAreaDefinitionsTemplate.toString());
		
		StringTemplate footerDefinitionsTemplate = group.getInstanceOf("footerDefinitions");
		footerDefinitionsTemplate.setAttribute("footerBottomPosition", pageHeight * .05);
		footerDefinitionsTemplate.setAttribute("pageWidth", pageWidth);
		pagesPostScript.append(footerDefinitionsTemplate.toString());
		List<String> pages = vizArea.getPostScriptPages();
		for (String page : pages) {
			pagesPostScript.append(getPage(page, pages.indexOf(page) + 1));
		}
		
		return pagesPostScript.toString();
	}

	private static String getDocumentSetup(double pageHeight, double pageWidth) {
		StringTemplate documentSetupTemplate =
				group.getInstanceOf("documentSetup");
		documentSetupTemplate.setAttribute("pageWidth", pageWidth);
		documentSetupTemplate.setAttribute("pageHeight", pageHeight);
		return documentSetupTemplate.toString();
	}

	private static String getProcedureDefinitions() {
		StringTemplate procedureDefinitionsTemplate =
				group.getInstanceOf("procedureDefinitions");
		return procedureDefinitionsTemplate.toString();
	}

	private static String getHeader(int pages) {
		StringTemplate headerTemplate =
				group.getInstanceOf("header");
		headerTemplate.setAttribute("numberOfPages", pages);
		headerTemplate.setAttribute("date", new LocalDate().toString());
		return headerTemplate.toString();
	}
}
