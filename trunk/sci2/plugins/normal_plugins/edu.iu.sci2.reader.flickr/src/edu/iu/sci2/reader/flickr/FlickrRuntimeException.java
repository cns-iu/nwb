package edu.iu.sci2.reader.flickr;
/**
 * Extends runtime exception
 * @author Jeff
 *
 */
public class FlickrRuntimeException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FlickrRuntimeException(String msg) {
		super(msg);
	}

	public FlickrRuntimeException(String msg, Throwable cause) {
		super(msg, cause);
	}
}
