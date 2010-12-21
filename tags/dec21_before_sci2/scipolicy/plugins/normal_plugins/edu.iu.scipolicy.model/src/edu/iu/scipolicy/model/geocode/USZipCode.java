package edu.iu.scipolicy.model.geocode;

/**
 * 
 * Data Model that represent US Zip Code.
 * @author kongch
 *
 */
public class USZipCode {
	public static final int UZIP_LENGTH = 5;
	public static final int POST_BOX_LENGTH = 4;
	public static final String UZIP_SEPARATOR = "-";
	public static final String DEFAULT_VALUE = "";
	private String uzip =  DEFAULT_VALUE;
	private String postBox = DEFAULT_VALUE;
	
	public USZipCode(String uzip, String postBox) {
		if (uzip != null) {
			this.uzip = uzip.trim();
			
			/* no meaning to set postBox if uzip is null */
			if (postBox != null) {
				this.postBox = postBox.trim();
			}
		}
	}
	
	public String getUzip() {
		return this.uzip;
	}
	
	public String getPostBox() {
		return this.postBox;
	}
	
	@Override
	public String toString() {
		if (this.postBox == DEFAULT_VALUE) {
			return this.uzip;
		} else {
			return this.uzip + UZIP_SEPARATOR + this.postBox;
		}
	}
	
	/**
	 * Parses the given String to ZipCode object.
	 * @param zipString - zip code in String format
	 * @return parsed ZipCode if success. Else return an
	 * empty ZipCode object
	 */
	public static USZipCode parse(String zipString) {
		if (zipString == null) {
			zipString = DEFAULT_VALUE;
		}
		String[] zipStrings = zipString.split(UZIP_SEPARATOR);
		String uzip = parseUzip(zipStrings);
		String postBox = parsePostBox(zipStrings);
		return new USZipCode(uzip, postBox);
	}
	
	private static String parseUzip(String[] strings) {
		String uzip = DEFAULT_VALUE;
		String uzipPrefix = DEFAULT_VALUE; 
		
		if (strings.length > 0) {
			uzip = strings[0].trim();
			int length = uzip.length();
			
			if (length == 0) {
				return uzip;
			}
			
			/* Handling 9 digits zip code without "-" */
			if (length > UZIP_LENGTH) {
				uzip = uzip.substring(0, length - POST_BOX_LENGTH);
				length = uzip.length();
			}
			
			int remain = UZIP_LENGTH - length;
			while (remain > 0) {
				remain--;
				uzipPrefix += "0";
			}
		}

		return uzipPrefix + uzip;
	}
	
	private static String parsePostBox(String[] strings) {
		
		String postBox = DEFAULT_VALUE;
		
		if (strings.length > 1) {
			int length = strings[1].trim().length();
			
			if (length == POST_BOX_LENGTH) {
				postBox = strings[1].trim();
			}
		} else if (strings.length > 0) {
			int length = strings[0].trim().length();
			
			if (length > UZIP_LENGTH) {
				postBox = strings[0].trim().substring(length - POST_BOX_LENGTH);
			}
		}
		
		return postBox;
	}
}
