package edu.iu.scipolicy.preprocessing.extractzipcode;

public class Zipcode {

	private String primaryZipcode;
	private String extensionZipcode;

	public Zipcode(String primaryZipcode, String extensionZipcode) {
		this.primaryZipcode = primaryZipcode;
		this.extensionZipcode = extensionZipcode;
	}
	
	public String getPrimaryZipcode() {
		return primaryZipcode;
	}

	public String getExtensionZipcode() {
		return extensionZipcode;
	}

	public void setPrimaryZipcode(String primaryZipcode) {
		this.primaryZipcode = primaryZipcode;
	}

	public void setExtensionZipcode(String extensionZipcode) {
		this.extensionZipcode = extensionZipcode;
	}
	
}
