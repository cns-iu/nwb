package edu.iu.sci2.visualization.scimaps.tempvis;

import java.awt.Dimension;

public interface PageManager {
	public void render(int pageNumber, GraphicsState state)
			throws PageManagerRenderingException;

	public int numberOfPages();
	
	public Dimension pageDimensions();

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
