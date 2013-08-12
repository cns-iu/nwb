package edu.iu.sci2.preprocessing.scimaps.journal;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;

public class ScimapsJournalMatcher {
	private static final String JOURNAL_FULL_NAME = "journ_names_05.14.13.csv";
	private static final String JOURNAL_ALTERNATIVE_NAME = "journ_alt_names_05.14.13.csv"; 
	private Map<String, Integer> normalizedNameToId;
	private HashMap<Integer, String> idToFullName;
	
	public ScimapsJournalMatcher() throws IOException {
		this.normalizedNameToId = new HashMap<String, Integer>();
		this.idToFullName = new HashMap<Integer, String>();
		initJournalMap();
	}
	
	public String get(String name) {
		String normalizedName = TextNormalizer.normalize(name);
		Integer id = normalizedNameToId.get(normalizedName);
		return (id==null) ? null : idToFullName.get(id);
	}
	
	private void initJournalMap() throws IOException {
		// Read Full Names
		CSVReader reader = null;
		URL url = ScimapsJournalMatcher.class.getResource(JOURNAL_FULL_NAME);
		try {
			reader = new CSVReader(new InputStreamReader(url.openStream()));
			
			// Skip header
			String[] line = reader.readNext();
			while ((line = reader.readNext()) != null) {
				if (line.length > 1) {
					Integer id = Integer.parseInt(line[0].trim());
					String normalizedName = TextNormalizer.normalize(line[1]);
					normalizedNameToId.put(normalizedName, id);
					idToFullName.put(id, line[1]);
				}
			}
			
		} finally {
			if (reader != null) {
				reader.close();
				reader = null;
			}
		}
		
		// Read Alternative Names
		url = ScimapsJournalMatcher.class.getResource(JOURNAL_ALTERNATIVE_NAME);
		try {
			reader = new CSVReader(new InputStreamReader(url.openStream()));
			
			// Skip header
			String[] line = reader.readNext();
			while ((line = reader.readNext()) != null) {
				if (line.length > 1) {
					Integer id = Integer.parseInt(line[0].trim());
					String normalizedName = TextNormalizer.normalize(line[1]);
					normalizedNameToId.put(normalizedName, id);
				}
			}
		} finally {
			if (reader != null) {
				reader.close();
				reader = null;
			}
		}
	}
}
