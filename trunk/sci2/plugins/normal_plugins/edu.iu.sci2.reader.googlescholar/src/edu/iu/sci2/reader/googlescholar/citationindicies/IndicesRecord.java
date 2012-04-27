package edu.iu.sci2.reader.googlescholar.citationindicies;

public class IndicesRecord {
	private String userId;
	private String citations;
	private String citationsSince2007;
	private String hIndex;
	private String hIndexSince2007;
	private String i10Index;
	private String i10IndexSince2007;
	
	protected IndicesRecord(String userId, String citations2, String citationsSince20072, 
			String hIndex2,	String hIndexSince20072, String i10Index2, String i10IndexSince20072) {
		
		this.userId = userId;
		this.citations = citations2;
		this.citationsSince2007 = citationsSince20072;
		this.hIndex = hIndex2;
		this.hIndexSince2007 = hIndexSince20072;
		this.i10Index = i10Index2;
		this.i10IndexSince2007 = i10IndexSince20072;
	}
	
	public String getUserId() {
		return userId;
	}
	
	public String getCitations() {
		return citations;
	}

	public String getCitationsSince2007() {
		return citationsSince2007;
	}
	
	public String getHIndex() {
		return hIndex;
	}
	
	public String getHIndexSince2007() {
		return hIndexSince2007;
	}
	
	public String getI10Index() {
		return i10Index;
	}
	
	public String getI10IndexSince2007() {
		return i10IndexSince2007;
	}
}
