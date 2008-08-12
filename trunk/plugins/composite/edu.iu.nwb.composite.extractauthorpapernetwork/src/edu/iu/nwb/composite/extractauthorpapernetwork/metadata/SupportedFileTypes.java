package edu.iu.nwb.composite.extractauthorpapernetwork.metadata;

import java.util.HashMap;

public interface SupportedFileTypes {
	static final AuthorPaperFormat bibtex = new AuthorPaperFormat("bibtex","author","title");
	static final AuthorPaperFormat isi = new AuthorPaperFormat("isi","Authors","Cite Me As");
	static final AuthorPaperFormat scopus = new AuthorPaperFormat("scopus","Authors","Title");
	static final AuthorPaperFormat endnote = new AuthorPaperFormat("endnote","Author","Title");
	
	static final String[] supportedFormats = AuthorPaperFormat.getSupportedFormats();
	
	public class AuthorPaperFormat{
		private static HashMap formatToNameColumn = new HashMap();
		private static HashMap formatToPaperColumn = new HashMap();
		
		public AuthorPaperFormat(String name, String authorColumn, String paperColumn){
			AuthorPaperFormat.formatToNameColumn.put(name, authorColumn);
			AuthorPaperFormat.formatToPaperColumn.put(name, paperColumn);
		}
		
		public static String getAuthorColumnByName(String fileFormat){
			return AuthorPaperFormat.formatToNameColumn.get(fileFormat).toString();
		}
		
		public static String getPaperColumnByName(String fileFormat){
			return AuthorPaperFormat.formatToPaperColumn.get(fileFormat).toString();
		}
		
		
		
		public static String[] getSupportedFormats(){
			String[] supportedFormats = new String[AuthorPaperFormat.formatToNameColumn.size()];
			
			supportedFormats = (String[])AuthorPaperFormat.formatToNameColumn.keySet().toArray(supportedFormats);
			
			return supportedFormats;
		}
		
		
	}
	
}

