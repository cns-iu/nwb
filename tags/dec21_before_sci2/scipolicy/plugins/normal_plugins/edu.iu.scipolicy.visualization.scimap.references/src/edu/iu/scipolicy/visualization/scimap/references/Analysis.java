package edu.iu.scipolicy.visualization.scimap.references;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import org.antlr.stringtemplate.StringTemplate;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

//import cc.mallet.fst.SimpleTagger;

public class Analysis {

	private static File modelFile;

	private static Set<String> publishers = loadNameFile("publishers.txt");
	private static Set<String> places = loadNameFile("places.txt");
	private static Set<String> months = loadNameFile("months.txt");
	private static Set<String> lastNames = loadNameFile("lastnames.txt");
	private static Set<String> girls = loadNameFile("girls.txt");
	private static Set<String> boys = loadNameFile("boys.txt");

	private static Pattern puncts = Pattern.compile("[^a-zA-Z0-9 ]+");
	private static Pattern whiteSpace = Pattern.compile("\\s+");
	private static Pattern justTheFacts = Pattern.compile("\\W");
	private static Pattern year = Pattern.compile("^(19|20)\\d{2}$");
	private static Pattern page = Pattern.compile(".*\\d\\-\\d.*");
	private static Pattern vol = Pattern.compile(".*\\d\\(\\d+\\).*");
	private static Pattern ordinal = Pattern.compile("^\\d+(th|st|nd|rd)$");
	private static Pattern hasNum = Pattern.compile(".*\\d.*");
	private static Pattern leadQuote = Pattern.compile("^[\\\"\\`\\'].*");
	private static Pattern endQuote = Pattern.compile(".*[\\\"\\`\\'][^\\s]*$");
	private static Pattern hyphens = Pattern.compile(".*\\-.*\\-.*");
	private static Pattern continues = Pattern.compile(".*[\\-\\,\\:\\;]$");
	private static Pattern stops = Pattern.compile(".*[\\!\\?\\.\\\"\\']$");
	private static Pattern braces = Pattern.compile("^[\\(\\[\\{\\<].+[\\)\\]\\}\\>].?$");
	private static Pattern vols = Pattern.compile("^[0-9]{2,5}\\([0-9]{2,5}\\).?$"); //why only two or more?
	private static Pattern endOfLine = Pattern.compile("(?m)$");
	private static Pattern justNumbers = Pattern.compile("^[\\s\\d]*$");
	private static Pattern anyDigits = Pattern.compile(".*\\d.*");
	private static Pattern authorSep = Pattern.compile("[,;]");
	private static Pattern endsInDot = Pattern.compile(".*\\.\\s*$");
	private static Pattern startsWithCaps = Pattern.compile("^\\p{Upper}.*");
	private static Pattern alpha = Pattern.compile("[a-zA-Z]");

	private static List<Pattern> markers = new ArrayList<Pattern>();

	static {
		markers.add(Pattern.compile("\\n\\s*\\[.+?\\][^\\n]{10,}+"));
		markers.add(Pattern.compile("\\n\\s*\\(.+?\\)[^\n]{10,}+"));
		markers.add(Pattern.compile("\\n\\s*\\d+[^.\\n][^\\n]{9,}+"));
		markers.add(Pattern.compile("\\n\\s*\\d+\\.[^\\n]{10,}+"));
		markers.add(Pattern.compile("\\n\\s*\\[\\d+\\][^\\n]{10,}+"));
	}

	static {
		try {
			modelFile = File.createTempFile("referenceParsing", ".model");
			InputStream in = Analysis.class.getResourceAsStream("crf.model");
			OutputStream out = new FileOutputStream(modelFile);
			byte[] buf = new byte[1024];
			int len;
			while((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			in.close();
			out.close();
		} catch (IOException e) {
			// better succeed, or we have real issues.
			throw new IllegalArgumentException("Major problem beginning processing.");
		}
	}

	private Map<String, Integer> found = new HashMap<String, Integer>();
	private Map<String, Integer> unfound = new HashMap<String, Integer>();
	private boolean failed = false;
	private String reason = "";

	private String name;
	private double scalingFactor;

	public Analysis(File pdf, double scalingFactor) {
		try {
			name = pdf.getName();
			String text = normalize(extractText(pdf));
			Pattern marker = guessMarker(text);
			String[] citations = segment(text, marker);
			Map<String, Integer> rawCitations = totalMap(citations);
			/* String[][][] featureized = new String[citations.length][][];
			for(int index = 0; index < citations.length; index++) {
				String citation = citations[index];
				featureized[index] = featureize(citation);
			}
			File tokenFile = writeTokenFile(featureized);
			Map<String, Integer> rawJournals = tagTokens(tokenFile);
			findJournals(rawJournals, found, unfound); */
			findJournals(rawCitations, found, unfound);




		} catch (PDFReadingException e) {
			failed = true;
			reason = e.getMessage();
		} catch (Exception e) {
			e.printStackTrace();
			failed = true;
			reason = "Unable to tag data.";
		}
		if(failed) {
			System.err.println("FAILED: " + reason);
		}

		this.scalingFactor = scalingFactor;
	}

	private Map<String, Integer> totalMap(String[] citations) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		
		for(String citation : citations) {
			if(citation.length() > 0) {
				updateMap(map, citation, 1);
			}
		}
		
		return map;
	}

	private Iterator<String> subsequences(final String name) {
		return new Iterator<String>() {

			private String[] words = name.split(" ");
			private boolean finished = false;
			private int ii = words.length - 1;
			private int jj = words.length - ii;

			public boolean hasNext() {
				return !finished ;
			}

			public String next() {
				String result = joinReference(Arrays.asList(words).subList(jj, jj + ii));
				jj = jj - 1;
				if(jj < 0) {
					ii = ii - 1;
					jj = words.length - ii;
					
					if(ii <= 0) {
						finished = true;
					}
				}
				return result;
			}

			public void remove() {
				throw new UnsupportedOperationException();	
			}};
	}

	private void findJournals(Map<String, Integer> rawReferences, Map<String, Integer> found, Map<String, Integer> unfound) {
		for(String rawReference : rawReferences.keySet()) {
			String reference = normalizeForLookup(rawReference);
			if(MapOfScience.lookup.containsKey(reference)) {
				updateMap(found, MapOfScience.lookup.get(reference), rawReferences.get(rawReference));
			} else {
				boolean matched = false;
				Iterator<String> possibles = subsequences(reference);
				while(possibles.hasNext()) {
					String possible = possibles.next();
					if(MapOfScience.lookup.containsKey(possible)) {
						updateMap(found, MapOfScience.lookup.get(possible), rawReferences.get(rawReference));
						matched = true;
						break;
					}
				}
				if(!matched) {
					updateMap(unfound, Utils.postscriptEscape(rawReference), rawReferences.get(rawReference));
				}
			}
		}
	}

	

	private static Set<String> loadNameFile(String fileName) {
		Set<String> names = new HashSet<String>();
		BufferedReader in = new BufferedReader(new InputStreamReader(Analysis.class.getResourceAsStream(fileName)));
		String line;
		try {
			while((line = in.readLine()) != null) {
				names.add(line.trim());
			}
			return names;
		} catch (IOException e) {
			throw new IllegalArgumentException("Unable to load critical files.");
		} finally {
			if(in != null) {
				try {
					in.close();
				} catch (IOException e) {}
			}
		}
	}

	private String[] segment(String text, Pattern marker) {
		String[] citations;
		if(marker == null) {
			citations = segmentUnmarked(text);
		} else {
			citations = segmentMarked(text, marker);
		}
		return citations;
	}

/*	private Map<String, Integer> tagTokens(File tokenFile) throws Exception {
		System.err.println("Tagging " + tokenFile.getAbsolutePath());
		//output redirection really bad, could get unintentional data. Seems necessary, though, absent rewriting some really messy code in SimpleTagger.main
		PrintStream oldOut = System.out;
		OutputStream tagData = new ByteArrayOutputStream();
		System.setOut(new PrintStream(tagData, true));
		SimpleTagger.main(new String[]{"--model-file", modelFile.getAbsolutePath(), tokenFile.getAbsolutePath()});
		System.setOut(oldOut);

		BufferedReader tokens = new BufferedReader(new FileReader(tokenFile));
		BufferedReader tags = new BufferedReader(new StringReader(tagData.toString()));
		Map<String, Integer> journals = new HashMap<String, Integer>();
		String previousTag = "";
		List<String> currentJournal = new ArrayList<String>();
		String tag;
		String tokenLine;
		while((tag = tags.readLine()) != null) {
			tokenLine = tokens.readLine();
			String token = tokenLine.split(" ")[0];
			tag = tag.trim();
			if("".equals(tag)) {
				if(currentJournal.size() > 0) {
					updateJournalMap(journals, currentJournal);
					currentJournal = new ArrayList<String>();
				}
			} //else if("journal".equals(tag)) {
				//if("journal".equals(previousTag)) {
					currentJournal.add(token);
				//} else if (currentJournal.size() > 0) {
				//	updateJournalMap(journals, currentJournal);
				//	currentJournal = new ArrayList<String>();
				//	currentJournal.add(token);
				//}
			//}
			previousTag = tag;
		}
		if("journal".equals(previousTag) && currentJournal.size() > 0) {
			updateJournalMap(journals, currentJournal);
		}
		return journals;
	}
*/
	private void updateJournalMap(Map<String, Integer> journals,
			List<String> currentJournal) {
		String journalName = joinReference(currentJournal);
		journalName = normalizeForLookup(journalName);
		if(journalName.trim().length() > 0) {
			updateMap(journals, journalName, 1);
		}
	}

	private String normalizeForLookup(String journalName) {
		journalName = puncts.matcher(journalName).replaceAll("").trim().toLowerCase();
		return journalName;
	}

	private void updateMap(Map<String, Integer> map, String name, int additional) {
		if(map.containsKey(name)) {
			map.put(name, map.get(name) + additional);
		} else {
			map.put(name, additional);
		}
	}

	private File writeTokenFile(String[][][] featureized) throws IOException { //failing in here
		File output = File.createTempFile("references", ".tokens");
		System.err.println("initial token file creation");
		Writer out = new OutputStreamWriter(new FileOutputStream(output));
		for(String[][] citation : featureized) {
			for(String[] features : citation) {
				StringBuilder featureLine = new StringBuilder();
				for(String feature : features) {
					featureLine.append(feature);
					featureLine.append(" ");
				}
				out.write(featureLine.toString().trim() + "\n");
			}
			out.write("\n");
		}
		out.close();

		return output;
	}

	private String[][] featureize(String citation) {
		List<String[]> features = new ArrayList<String[]>();
		String hasPossibleEditor = "noEditors";
		if(citation.contains("ed.") || citation.contains("editor") || citation.contains("editors") || citation.contains("eds.")) {
			hasPossibleEditor = "possibleEditors";
		}
		String[] tokens = whiteSpace.split(citation);
		for(int index = 0; index < tokens.length; index++) {
			String token = tokens[index];
			if(token.length() == 0) {
				continue;
			}
			String[] featureArray = new String[23];
			features.add(featureArray);

			String noPunctuation = justTheFacts.matcher(token).replaceAll("");
			String lowerNoPunct = noPunctuation.toLowerCase();
			if(noPunctuation.trim().length() == 0) {
				noPunctuation = "EMPTY";
				lowerNoPunct = "EMPTY";
			}
			featureArray[0] = token;
			String lastChar = token.substring(token.length() - 1, token.length());
			if(alpha.matcher(lastChar).matches()) {
				if(lastChar.toLowerCase().equals(lastChar)) {
					lastChar = "a";
				} else if(lastChar.toUpperCase().equals(lastChar)){
					lastChar = "A";
				}
			} else if(justNumbers.matcher(lastChar).matches()) {
				lastChar = "0";
			}
			featureArray[1] = lastChar;
			featureArray[2] = token.substring(0, 1);
			featureArray[3] = token.substring(0, Math.min(token.length(), 2));
			featureArray[4] = token.substring(0, Math.min(token.length(), 3));
			featureArray[5] = token.substring(0, Math.min(token.length(), 4));
			featureArray[6] = token.substring(Math.max(0, token.length() - 1), token.length());
			featureArray[7] = token.substring(Math.max(0, token.length() - 2), token.length());
			featureArray[8] = token.substring(Math.max(0, token.length() - 3), token.length());
			featureArray[9] = token.substring(Math.max(0, token.length() - 4), token.length());

			featureArray[10] = lowerNoPunct;
			if(noPunctuation.toUpperCase().equals(noPunctuation)) {
				if(noPunctuation.length() == 1) {
					featureArray[11] = "singleCap";
				} else {
					featureArray[11] = "AllCap";
				}
			} else if(noPunctuation.substring(0, 1).toUpperCase().equals(noPunctuation.substring(0, 1))){
				featureArray[11] = "InitCap";
			} else {
				featureArray[11] = "others";
			}

			if(year.matcher(token).matches()) {
				featureArray[12] = "year";
			} else if(page.matcher(token).matches()) {
				featureArray[12] = "possiblePage";
			} else if(vol.matcher(token).matches()) {
				featureArray[12] = "possibleVol";
			} else if(noPunctuation.length() > 0 && justNumbers.matcher(noPunctuation).matches()) {
				if(noPunctuation.length() < 4) {
					featureArray[12] = "" + noPunctuation.length() + "dig";
				} else {
					featureArray[12] = "4+dig";
				}
			} else if(ordinal.matcher(noPunctuation).matches()) {
				featureArray[12] = "ordinal";
			} else if(hasNum.matcher(noPunctuation).matches()) {
				featureArray[12] = "hasDig";
			} else {
				featureArray[12] = "nonNum";
			}

			int stupidMask = 0;

			if(publishers.contains(lowerNoPunct)) {
				featureArray[19] = "publisherName";
				stupidMask += 32;
			} else {
				featureArray[19] = "no";
			}

			if(places.contains(lowerNoPunct)) {
				featureArray[18] = "placeName";
				stupidMask += 16;
			} else {
				featureArray[18] = "no";
			}

			if(months.contains(lowerNoPunct)) {
				featureArray[17] = "monthName";
				stupidMask += 8;
			} else {
				featureArray[17] = "no";
			}

			if(lastNames.contains(lowerNoPunct)) {
				featureArray[16] = "lastName";
				stupidMask += 4;
			} else {
				featureArray[16] = "no";
			}

			if(girls.contains(lowerNoPunct)) {
				featureArray[15] = "femaleName";
				stupidMask += 2;
			} else {
				featureArray[15] = "no";
			}

			if(boys.contains(lowerNoPunct)) {
				featureArray[14] = "maleName";
				stupidMask += 1;
			} else {
				featureArray[14] = "no";
			}

			featureArray[13] = "" + stupidMask;

			featureArray[20] = hasPossibleEditor;
			featureArray[21] = "" + (int) (index * 12.0 / tokens.length);

			if(leadQuote.matcher(token).matches()) {
				featureArray[22] = "leadQuote";
			} else if(endQuote.matcher(token).matches()) {
				featureArray[22] = "endQuote";
			} else if(hyphens.matcher(token).matches()) {
				featureArray[22] = "multiHyphen";
			} else if(continues.matcher(token).matches()) {
				featureArray[22] = "contPunct";
			} else if(stops.matcher(token).matches()) {
				featureArray[22] = "stopPunct";
			} else if(braces.matcher(token).matches()) {
				featureArray[22] = "braces";
			} else if(vols.matcher(token).matches()) {
				featureArray[22] = "possibleVol";
			} else {
				featureArray[22] = "others";
			}

		}


		return features.toArray(new String[][]{});
	}

	private String[] segmentMarked(String text, Pattern marker) {
		List<List<String>> references = new ArrayList<List<String>>();
		boolean in_citations = false;

		for(String line : endOfLine.split(text)) {
			if(marker.matcher(line).matches()) {
				in_citations = true;
				List<String> reference = new ArrayList<String>();
				reference.add(line.trim());
				references.add(reference);
			} else if(in_citations) {
				if(!justNumbers.matcher(line).matches()) {
					references.get(references.size() - 1).add(line.trim());
				}
			}
		}

		String[] joinedReferences = joinReferences(references);

		return joinedReferences;
	}

	private String[] joinReferences(List<List<String>> references) {
		List<String> joinedReferences = new ArrayList<String>();
		for(List<String> reference : references) {
			if(reference.size() > 0) {
				String referenceString = joinReference(reference);
				if(referenceString.length() > 25) {
					joinedReferences.add(referenceString);
				}
			}
		}
		return joinedReferences.toArray(new String[]{});
	}

	private String joinReference(List<String> reference) {
		StringBuilder builder = new StringBuilder();
		for(String line : reference) {
			if(line.trim().equals("")) {
				continue;
			}
			if(line.endsWith("-")) {
				builder.append(line.substring(0, line.length() - 1));
			} else {
				builder.append(line + " ");
			}
		}
		return builder.toString().trim();
	}

	private String[] segmentUnmarked(String text) {
		//System.err.println(text);
		List<Integer> citationStarts = new ArrayList<Integer>();
		int currentCitationStart = 0;
		String[] lines = endOfLine.split(text);
		for(int index = 0; index < lines.length; index++) {
			String line = (lines[index] = lines[index].trim());
			for(int previousIndex = index; previousIndex > currentCitationStart; previousIndex--) {
				if(lines[previousIndex - 1].length() < 25) {
					//System.err.println("Short line: " + lines[previousIndex - 1]);
					currentCitationStart = previousIndex;
					//System.err.println("start: " + currentCitationStart);
					break;
				}
				int authorLineIndex = -1;
				for(int previousPreviousIndex = previousIndex - 1; previousPreviousIndex > currentCitationStart; previousPreviousIndex--) {
					if(anyDigits.matcher(lines[previousPreviousIndex]).matches()) {
						break;
					}
					if(count(authorSep, lines[previousPreviousIndex]) >= 3) {
						if(previousPreviousIndex == 0 || endsInDot.matcher(lines[previousPreviousIndex]).matches()) {
							authorLineIndex = previousPreviousIndex;
						}
					} else {
						break;
					}
				}
				if(authorLineIndex != -1) {
					currentCitationStart = authorLineIndex;
					break;
				}
				if(endsInDot.matcher(lines[previousIndex - 1]).matches() && startsWithCaps.matcher(lines[previousIndex]).matches() && lines[previousIndex - 1].length() < lines[previousIndex].length()) {
					currentCitationStart = previousIndex;
					break;
				}
			}
			if((citationStarts.size() == 0 || currentCitationStart > citationStarts.get(citationStarts.size() - 1)) && currentCitationStart != 0) {
				citationStarts.add(currentCitationStart);
			}
		}
		List<List<String>> citations = new ArrayList<List<String>>();
		int previousCitationStart = 0;
		List<String> lineList = Arrays.asList(lines);
		for(int citationStart : citationStarts) {
			citations.add(lineList.subList(previousCitationStart, citationStart));
			previousCitationStart = citationStart;
		}

		return joinReferences(citations);
	}

	private Pattern guessMarker(String text) {
		Pattern maxPattern = null;
		int maxMatches = count(endOfLine, text) / 6;
		for(Pattern marker : markers) {
			int matches = count(marker, text);
			if(matches >= maxMatches) {
				maxMatches = matches;
				maxPattern = marker;
			}
		}
		return maxPattern;
	}

	private int count(Pattern pattern, String text) {
		return pattern.split(text).length - 1;
	}

	private String normalize(String text) {
		String[] lines = endOfLine.split(text);

		StringBuilder builder = new StringBuilder();

		for(String line : lines) {
			if(!justNumbers.matcher(line).matches()) {
				builder.append(line);
			}
		}

		return builder.toString();
	}

	private String extractText(File pdf) throws PDFReadingException {
		PDDocument document = null;
		String text = "";
		try {
			document = PDDocument.load(new FileInputStream(pdf), true);
			PDFTextStripper stripper = new PDFTextStripper();
			stripper.setSortByPosition(true);
			text = stripper.getText(document);
			//text = new String(text.getBytes(), "UTF-8");
		} catch (IOException e) {
			throw new PDFReadingException("Unable to read PDF file.");
		} catch (Exception e) {
			throw new PDFReadingException("Problem loading PDF file.");
		} catch(NoClassDefFoundError e) {
			throw new PDFReadingException("Font-related loading problems.");
		} finally {
			if(document != null) {
				try {
					document.close();
				} catch (IOException e) {}
			}
		}
		return text;
	}

	public Map<String, Integer> getFound() {
		return found;
	}

	public Map<String, Integer> getUnfound() {
		return unfound;
	}

	public Totals getTotals() {
		return new Totals(found, unfound);
	}

	public String getPostscript() {
		if(this.failed)  {
			return "1 inch 5 inch moveto (" + this.reason + ") show showpage";
		}
		MapOfScience map = new MapOfScience(found, scalingFactor);
		
		StringTemplate proposalTemplate = MapReferences.group.getInstanceOf("proposal");
		
		proposalTemplate.setAttribute("proposalMap", map.getPostscript());
		proposalTemplate.setAttribute("title", this.name);
		proposalTemplate.setAttribute("totals", this.getTotals());
		
		Collection<Category> categories = collateJournals(found);
		proposalTemplate.setAttribute("listFont", Math.min(8, 260 / (found.size() + unfound.size() + categories.size() * 1.5 + 1)));
		proposalTemplate.setAttribute("categories", categories);
		proposalTemplate.setAttribute("unfound", new ArrayList<String>(unfound.keySet()));
		proposalTemplate.setAttribute("unfoundExist", unfound.size() > 0);
		
		return proposalTemplate.toString();
	}

	private Collection<Category> collateJournals(Map<String, Integer> found) {
		Map<String, Category> categories = new HashMap<String, Category>();
		for(String color : MapOfScience.categories.keySet()) {
			String name = MapOfScience.categories.get(color);
			categories.put(name, new Category(name, color));
		}
		categories.put(Category.MULTIPLE, new Category(Category.MULTIPLE, "255:255:255"));
		for(String journal : found.keySet()) {
			String pretty = MapOfScience.pretty.get(journal);
			Set<String> categoryNames = new HashSet<String>();
			for(JournalLocation location : MapOfScience.journals.get(journal)) {
				categoryNames.add(location.getCategory());
			}
			if(categoryNames.size() > 1) {
				categories.get(Category.MULTIPLE).addJournal(pretty, found.get(journal));
			} else if(categoryNames.size() == 1) {
				categories.get(categoryNames.iterator().next()).addJournal(pretty, found.get(journal));
			} else {
				//should be impossible
				System.err.println("Impossible: a found journal with no category");
			}
		}
		return new TreeSet<Category>(filterCategories(categories));
	}

	private Collection<Category> filterCategories(
			Map<String, Category> categories) {
		Set<Category> filtered = new HashSet<Category>();
		for(String key : categories.keySet()) {
			Category category = categories.get(key);
			if(category.getJournals().size() > 0) {
				filtered.add(category);
			}
		}
		return filtered;
	}

	public String toString() {
		return getPostscript();
	}
	
	public String getName() {
		return name;
	}

}
