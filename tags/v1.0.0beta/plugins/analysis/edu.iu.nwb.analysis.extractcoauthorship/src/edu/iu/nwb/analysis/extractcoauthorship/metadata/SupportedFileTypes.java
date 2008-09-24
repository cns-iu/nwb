package edu.iu.nwb.analysis.extractcoauthorship.metadata;

import java.util.HashMap;

public interface SupportedFileTypes {
	static final CitationFormat bibtex = new CitationFormat("bibtex","author");
	static final CitationFormat isi = new CitationFormat("isi","Authors");
	static final CitationFormat scopus = new CitationFormat("scopus","Authors");
	static final CitationFormat endnote = new CitationFormat("endnote","Authors");
	
	static final String[] supportedFormats = CitationFormat.getSupportedFormats();
	
	public class CitationFormat{
		private static HashMap nameToColumn = new HashMap();
		
		public CitationFormat(String name, String authorColumn){
			CitationFormat.nameToColumn.put(name, authorColumn);
		}

		public static String getAuthorColumnByName(String name){
			return CitationFormat.nameToColumn.get(name).toString();
		}
		
		public static String[] getSupportedFormats(){
			String[] supportedFormats = new String[CitationFormat.nameToColumn.size()];
			
			supportedFormats = (String[])CitationFormat.nameToColumn.keySet().toArray(supportedFormats);
			
			return supportedFormats;
		}
		
		
	}
	
}

