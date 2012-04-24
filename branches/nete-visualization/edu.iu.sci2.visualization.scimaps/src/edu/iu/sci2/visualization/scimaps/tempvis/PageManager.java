package edu.iu.sci2.visualization.scimaps.tempvis;

import static edu.iu.sci2.visualization.scimaps.tempvis.GraphicsState.inch;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import edu.iu.sci2.visualization.scimaps.tempvis.PageElement.PageElementRenderingException;

/**
 * A page manager knows about "page specific" {@link PageElement}s and
 * "page independent" {@link PageElement}s. Page specific elements are those
 * that only occur on a specific page. Page independent elements are those that
 * occur on every page.
 * <p>
 * Extending classes should use {@code addToPage} and {@code addToAllPages} in
 * their constructor to add the required {@link PageElement}(s).
 * 
 */
public abstract class PageManager {

	private Multimap<Integer, PageElement> pageSpecificElements = HashMultimap
			.create();
	private HashSet<PageElement> pageIndependentElements = new HashSet<PageElement>();
	/*
	 * The page size in points
	 */
	protected Dimension dimensions = new Dimension((int) inch(11.0f),
			(int) inch(8.5f));
	
	/**
	 * Add a "page dependent" element to a specific page.
	 */
	protected void addToPage(int pageNumber, PageElement pageElement) {
		this.pageSpecificElements.put(pageNumber, pageElement);
	}

	/**
	 * Add a "page independent" element to all pages.
	 */
	protected void addToAllPages(PageElement pageElement) {
		this.pageIndependentElements.add(pageElement);
	}
	
	/**
	 * This will render the page specified by {@code pageNumber} to the
	 * {@code state}.
	 * 
	 * @throws PageManagerRenderingException
	 *             if there is a problem with the rendering.
	 */
	public void render(int pageNumber, GraphicsState state)
			throws PageManagerRenderingException {
		if (!this.pageSpecificElements.isEmpty()
				&& this.pageSpecificElements.get(pageNumber).isEmpty()) {
			return;
		}

		if (pageNumber != 1 && this.pageSpecificElements.isEmpty()) {
			throw new PageManagerRenderingException("Page number '"
					+ pageNumber + "' does not exist");
		}

		List<PageElementRenderingException> exceptions = new ArrayList<PageElementRenderingException>();

		for (PageElement element : this.pageIndependentElements) {
			try {
				element.render(state);
			} catch (PageElementRenderingException e) {
				exceptions.add(e);
			}
		}

		for (PageElement element : this.pageSpecificElements.get(pageNumber)) {
			try {
				element.render(state);
			} catch (PageElementRenderingException e) {
				exceptions.add(e);
			}
		}

		if (!exceptions.isEmpty()) {
			String newline = System.getProperty("line.separator");
			String message = "The following exceptions occured when rendering.  The cause of the first is also passed up."
					+ newline;

			for (PageElementRenderingException e : exceptions) {
				message += e.getMessage() + newline;
			}

			throw new PageManagerRenderingException(message, exceptions.get(0));
		}

		return;

	}

	/**
	 * Return the number of pages that the {@link PageManager} currently has.
	 */
	public int numberOfPages() {
		if (this.pageIndependentElements.isEmpty()
				&& this.pageSpecificElements.isEmpty()) {
			return 0;
		}

		if (this.pageSpecificElements.isEmpty()
				&& !this.pageIndependentElements.isEmpty()) {
			return 1;
		}

		return this.pageSpecificElements.keySet().size();
	}
	
	/**
	 * Return the size of the pages that the {@link PageManager} manages.
	 */
	public Dimension pageDimensions() {
		return this.dimensions;
	}

	/**
	 * A class representing an error in rendering.
	 * 
	 */
	public static class PageManagerRenderingException extends Exception {
		public PageManagerRenderingException() {
			super();
		}

		public PageManagerRenderingException(String message) {
			super(message);
		}
		
		public PageManagerRenderingException(Throwable cause) {
			super(cause);
		}
		
		public PageManagerRenderingException(String message, Throwable cause) {
			super(message, cause);
		}

		private static final long serialVersionUID = -3602350657917186922L;
	}
}
