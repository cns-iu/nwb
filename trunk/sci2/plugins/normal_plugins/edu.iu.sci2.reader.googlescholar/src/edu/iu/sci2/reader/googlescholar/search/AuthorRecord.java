package edu.iu.sci2.reader.googlescholar.search;

public class AuthorRecord {
	private String name;
	private String userId;
	private String university;
	private String email;
	private String citedBy;
	private String queriedAuthor;
	
	public AuthorRecord(String name, String userId, String university,
			String email, String citedBy, String queriedAuthor) {
		this.name = name;
		this.userId = userId;
		this.university = university;
		this.email = email;
		this.citedBy = citedBy;
		this.queriedAuthor = queriedAuthor;
	}

	public String getName() {
		return name;
	}
	
	public String getUserId() {
		return userId;
	}
	
	public String getUniversity() {
		return university;
	}
	
	public String getEmail() {
		return email;
	}
	
	public String getCitedBy() {
		return citedBy;
	}
	
	public String getQueriedAuthor() {
		return queriedAuthor;
	}
}
