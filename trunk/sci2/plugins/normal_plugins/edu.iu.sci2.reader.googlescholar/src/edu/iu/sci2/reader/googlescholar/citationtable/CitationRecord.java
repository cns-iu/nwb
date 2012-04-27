package edu.iu.sci2.reader.googlescholar.citationtable;

public class CitationRecord {
	private String title;
	private String authors;
	private Integer citedBy;
	private Integer year;
	
	public CitationRecord(String title, String authors, Integer citedBy,
			Integer year) {
		this.title = title;
		this.authors = authors;
		this.citedBy = citedBy;
		this.year = year;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getAuthors() {
		return authors;
	}
	
	public Integer getCitedBy() {
		return citedBy;
	}
	
	public Integer getYear() {
		return year;
	}
}
