package edu.iu.sci2.visualization.temporalbargraph.common;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.antlr.stringtemplate.StringTemplate;
import org.antlr.stringtemplate.StringTemplateGroup;

import edu.iu.sci2.visualization.temporalbargraph.web.WebTemporalBarGraphPages;

public abstract class AbstractPages {
	protected static final StringTemplateGroup utilitiesGroup,
			pageElementsGroup;

	public static final String UTILITIES_STRING_TEMPLATE_FILE_PATH = "/edu/iu/sci2/visualization/temporalbargraph/common/stringtemplates/utilities.st";
	public static final String PAGE_ELEMENTS_STRING_TEMPLATE_FILE_PATH = "/edu/iu/sci2/visualization/temporalbargraph/common/stringtemplates/page_elements.st";
	private List<PageElement> pageElementsAllPages;
	private Map<Integer, List<PageElement>> pageElementsSomePages;

	private boolean pageElementsAdded;

	static {
		utilitiesGroup = new StringTemplateGroup(
				new InputStreamReader(
						WebTemporalBarGraphPages.class
								.getResourceAsStream(UTILITIES_STRING_TEMPLATE_FILE_PATH)));
		pageElementsGroup = new StringTemplateGroup(
				new InputStreamReader(
						WebTemporalBarGraphPages.class
								.getResourceAsStream(PAGE_ELEMENTS_STRING_TEMPLATE_FILE_PATH)));
	}

	public AbstractPages(){
		this.pageElementsAllPages = new ArrayList<PageElement>();
		this.pageElementsSomePages = new TreeMap<Integer, List<PageElement>>();
		this.pageElementsAdded = false;
	}
	
	public String renderDefinitionsPostscript() {
		if(!pageElementsAdded){
			addPageElements();
			pageElementsAdded = true;
		}
		
		Set<String> definitionsSet = new HashSet<String>();
		definitionsSet.add(utilitiesGroup.getInstanceOf(
				"pageUtilityDefinitions").toString());
		
		Set<PageElement> allPageElements = new HashSet<PageElement>();
		allPageElements.addAll(getPageElementsAllPages());
		
		for (List<PageElement> elements : getPageElementsSomePages().values()) {
			allPageElements.addAll(elements);
		}
		
		for (PageElement element : allPageElements) {
			definitionsSet.add(element.renderDefinitionsPostscript());
		}
		
		StringBuilder definitions = new StringBuilder();
		for(String definition : definitionsSet){
			definitions.append(definition);
		}
		return definitions.toString();
	}

	/**
	 * If you use this method, you are responsible for making sure that the 
	 * definitions needed for the pages are put in to the postscript page.
	 * @return
	 */
	public List<String> renderPagesIndividuallyPostScript(){
		List<String> pages = new ArrayList<String>();
		
		for(int ii = 0; ii < numberOfPages(); ii++){
			pages.add(renderPagePostscript(ii));
		}
		
		return pages;
	}
	
	public String renderPagesPostscript() {
		if(!pageElementsAdded){
			addPageElements();
			pageElementsAdded = true;
		}
		StringBuilder pagesPostScript = new StringBuilder();

		pagesPostScript.append(renderDefinitionsPostscript());


		for (int ii = 0; ii < numberOfPages(); ii++) {
			StringTemplate pageSetupTemplate = AbstractPostscriptDocument.documentGroup
					.getInstanceOf("pageSetup");
			pageSetupTemplate.setAttribute("pageNumber", ii + 1);
			pagesPostScript.append(pageSetupTemplate.toString());
			
			pagesPostScript.append(renderPagePostscript(ii));
			
			StringTemplate pageTearDownTemplate = AbstractPostscriptDocument.documentGroup
					.getInstanceOf("pageTearDown");
			pagesPostScript.append(pageTearDownTemplate.toString());
		}

		return pagesPostScript.toString();
	}

	protected String renderPagePostscript(int pageNumber) {
		StringBuilder pagePostScript = new StringBuilder();

		for(PageElement element : getPageElementsAllPages()){
			pagePostScript.append(element.renderPostscript());
		}
		
		for (PageElement element : getPageElementsSomePages(pageNumber)) {
			pagePostScript.append(element.renderPostscript());
		}
		
		return pagePostScript.toString();
	}

	protected List<PageElement> getPageElementsSomePages(int pageNumber) {
		return pageElementsSomePages.get(pageNumber);
	}

	protected Map<Integer, List<PageElement>> getPageElementsSomePages() {
		return pageElementsSomePages;
	}

	protected List<PageElement> getPageElementsAllPages() {
		return pageElementsAllPages;
	}

	protected List<PageElement> setElementsForPage(int pageNumber, List<PageElement> elements){
		return pageElementsSomePages.put(pageNumber, elements);
	}
	
	protected void addElementsAllPages(List<PageElement> elements){
		for(PageElement element : elements){
			addPageElementAllPages(element);
		}
	}
	
	protected void addPageElementAllPages(PageElement element) {
		pageElementsAllPages.add(element);
	}
	
	protected void addPageElementSomePages(PageElement element, List<Integer> pages){
		for(int pageNumber : pages){
			addElementForPage(pageNumber, element);
		}
	}
	
	protected void addPageElementsSomePages(Map<Integer, List<PageElement>> pageToElements){
		for(int pageNumber : pageToElements.keySet()){
			for(PageElement element : pageToElements.get(pageNumber)){
				addElementForPage(pageNumber, element);
			}
		}
	}

	protected void addElementForPage(int pageNumber, PageElement element){
		List<PageElement> elements = getPageElementsSomePages(pageNumber);
		if (elements == null){
			elements = new ArrayList<PageElement>();
		}
		elements.add(element);
		
		setElementsForPage(pageNumber, elements);
	}
	

	protected abstract int numberOfPages();
	
	public abstract void addPageElements();
}
