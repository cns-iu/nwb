package edu.iu.sci2.visualization.temporalbargraph.common;

import java.io.InputStreamReader;
import java.util.List;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;
import org.joda.time.LocalDate;
public abstract class AbstractPostscriptDocument {

	public static final String DOCUMENT_STRING_TEMPLATE_FILE_PATH = "/edu/iu/sci2/visualization/temporalbargraph/common/stringtemplates/document.st";
	public static final StringTemplateGroup documentGroup;
	
	protected abstract AbstractPages getPages();
	protected abstract DoubleDimension getPageSize();
	
	static {
		documentGroup = new StringTemplateGroup(new InputStreamReader(
				AbstractPostscriptDocument.class
						.getResourceAsStream(DOCUMENT_STRING_TEMPLATE_FILE_PATH)));
	}
	
	public String renderPostscript() {
		
		/**
		 * A postscript document looks like the following:
		 * Header
		 * Procedure Definitions
		 * Document Setup
		 * Pages
		 * Document Trailer
		 */
	
		StringBuilder documentPostScript = new StringBuilder();
		documentPostScript.append(renderHeader());
		documentPostScript.append(renderProcedureDefinitions());
		documentPostScript.append(renderDocumentSetup());
		documentPostScript.append(renderPages(getPages()));
		documentPostScript.append(renderDocumentTrailer());
		
		return documentPostScript.toString();
	}

	protected String renderHeader() {
		StringTemplate headerTemplate =
				getGroup().getInstanceOf("header");
		headerTemplate.setAttribute("numberOfPages", getPages().numberOfPages());
		headerTemplate.setAttribute("date", new LocalDate().toString());
		return headerTemplate.toString();
	}

	protected static String renderProcedureDefinitions() {
		StringTemplate procedureDefinitionsTemplate =
				getGroup().getInstanceOf("procedureDefinitions");
		return procedureDefinitionsTemplate.toString();
	}

	protected String renderDocumentSetup() {
		StringTemplate documentSetupTemplate =
				getGroup().getInstanceOf("documentSetup");
		documentSetupTemplate.setAttribute("pageWidth", getPageSize().getWidth());
		documentSetupTemplate.setAttribute("pageHeight", getPageSize().getHeight());
		return documentSetupTemplate.toString();
	}

	protected static String renderPages(AbstractPages pages) {
		StringBuilder pagePostcript = new StringBuilder();
		
		pagePostcript.append(pages.renderDefinitionsPostscript());
		List<String> individualPages = pages.renderPagesIndividuallyPostScript();
		for (int ii = 0; ii < individualPages.size(); ii++) {		
			StringTemplate pageSetupTemplate = AbstractPostscriptDocument.documentGroup
					.getInstanceOf("pageSetup");
			pageSetupTemplate.setAttribute("pageNumber", ii + 1);
			pagePostcript.append(pageSetupTemplate.toString());
			
			pagePostcript.append(individualPages.get(ii));
			
			StringTemplate pageTearDownTemplate = AbstractPostscriptDocument.documentGroup
					.getInstanceOf("pageTearDown");
			pagePostcript.append(pageTearDownTemplate.toString());
		}
		
		return pagePostcript.toString();
	}

	protected static String renderDocumentTrailer() {
		StringTemplate documentTrailerTemplate =
				getGroup().getInstanceOf("documentTrailer");
		return documentTrailerTemplate.toString();
	}
	
	protected static StringTemplateGroup getGroup(){
		return documentGroup;
	}
}
