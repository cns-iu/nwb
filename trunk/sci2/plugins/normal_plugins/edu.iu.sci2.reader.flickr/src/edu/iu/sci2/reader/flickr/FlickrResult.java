package edu.iu.sci2.reader.flickr;

public class FlickrResult {
	private String username;
	private String title;
	private String url;
	
	public FlickrResult (String username, String title, String url) {
		this.username = username;
		this.title = title;
		this.url = url;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getUrl() {
		return url;
	}
}
