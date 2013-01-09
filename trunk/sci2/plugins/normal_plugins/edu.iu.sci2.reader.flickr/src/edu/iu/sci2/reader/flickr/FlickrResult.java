package edu.iu.sci2.reader.flickr;

public class FlickrResult {
	private String username;
	private String title;
	private String date;
	private String url;
	
	public FlickrResult (String username, String title, String date, String url) {
		this.username = username;
		this.title = title;
		this.date = date;
		this.url = url;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getDate() {
		return date;
	}
	
	public String getUrl() {
		return url;
	}
}
