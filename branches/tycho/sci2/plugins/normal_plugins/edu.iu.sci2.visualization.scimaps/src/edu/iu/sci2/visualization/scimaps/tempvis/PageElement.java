package edu.iu.sci2.visualization.scimaps.tempvis;

public interface PageElement {
	public void render(GraphicsState state) throws PageElementRenderingException;
	
	public static class PageElementRenderingException extends Exception {
		public PageElementRenderingException() {
			super();
		}

		public PageElementRenderingException(String message) {
			super(message);
		}
		
		public PageElementRenderingException(Throwable cause) {
			super(cause);
		}
		
		public PageElementRenderingException(String message, Throwable cause) {
			super(message, cause);
		}

		private static final long serialVersionUID = -3602350657917186922L;
	}
}
