package edu.iu.nwb.analysis.extractcoauthorship.metadata;

import java.util.HashMap;

public interface SupportedFileTypes {
	static final CitationFormat bibtex = new CitationFormat("bibtex","author");
	static final CitationFormat isi = new CitationFormat("isi","Authors");
	static final CitationFormat scopus = new CitationFormat("scopus","Authors");
	static final CitationFormat endnote = new CitationFormat("endnote","Author");
	
	static final String[] supportedFormats = CitationFormat.getSupportedFormats();
	
	public class CitationFormat{
		String name;
		String authorColumn;
		private static HashMap nameToColumn = new HashMap();
		
		public CitationFormat(String name, String authorColumn){
			this.name = name;
			this.authorColumn = authorColumn;
			CitationFormat.nameToColumn.put(this.name, this.authorColumn);
		}

		public String getName(){
			return this.name;
		}
		
		public String getAuthorColumn(){
			return this.authorColumn;
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

