package edu.iu.nwb.converter.prefuseisi.reader;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.osgi.service.log.LogService;

import prefuse.data.DataTypeException;
import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.util.collections.IntIterator;
import edu.iu.nwb.converter.prefuseisi.util.SetMap;
import edu.iu.nwb.converter.prefuseisi.util.StringUtil;
import edu.iu.nwb.shared.isiutil.ISITag;

public class ISICitationExtractionPreparer {
	
	private static final String SELF_REFERENCE_COLUMN_NAME = "Cite Me As";
	private static final String ISI_AUTHOR_SEPARATOR = "|";
	private static final String ISI_CITATION_SEPARATOR = "|";
	private static final String ISI_FIELD_SEPARATOR = ", ";
	
	private LogService log;
	
	public ISICitationExtractionPreparer(LogService log ) {
		this.log = log;
	}
	public Table prepareForCitationExtraction(Table isiTable) {
		//make journal names in papers conform to journal names used to reference those papers
		cleanReferences(isiTable);
		//create self-references (that is, a field that looks like how other papers will reference it) for each publication
		addSelfReferences(isiTable);
		
		return isiTable;
	}
	
	//side-effects isiTable
	private void addSelfReferences(Table isiTable) {
		//create the self-reference column
		isiTable.addColumn(SELF_REFERENCE_COLUMN_NAME, String.class);
		//for each record in the table...
		for (IntIterator tableIt = isiTable.rows(); tableIt.hasNext();) {
			int rowIndex = tableIt.nextInt();
			Tuple row = isiTable.getTuple(rowIndex);
			//calculate the self-reference based on the contents of other fields
			String selfReference = createSelfReference(row);
			//add the self-reference to the current record
			isiTable.setString(rowIndex, SELF_REFERENCE_COLUMN_NAME, selfReference);
		}
	}
	
	private String createSelfReference(Tuple isiRow) {
		List selfReferenceTokenList = new ArrayList();
		try {
			//standard elements
			String author = extractFirstAndLastNameOfFirstAuthor(isiRow);
			if (author != null) {
				selfReferenceTokenList.add(author);
			} else {
				handleNoAuthor(isiRow);
			}
			String year = extractPublicationYear(isiRow);
			if (year != null) {
				selfReferenceTokenList.add(year);
			} else {
				handleNoYear(isiRow);
			}
			String journal = extractAbbreviatedJournalName(isiRow);
			if (journal != null) {
				selfReferenceTokenList.add(journal);
			} else {
				handleNoJournal(isiRow);
			}
			//optional elements
			String volume = extractVolume(isiRow);
			if (volume != null) {
				selfReferenceTokenList.add(volume);
			} else {
				handleNoVolume(isiRow);
			}
			String page = extractFirstPage(isiRow);
			if (page != null) {
				selfReferenceTokenList.add(page);
			} else {
				handleNoPage(isiRow);
			}
		} catch (ArrayIndexOutOfBoundsException e1) {
			//column requested does not exist (for entire table or just this field?)
			//Fail silently. This will happen normally. The remainder of the self reference will be returned.
		} catch (DataTypeException e2) {
			//column type cannot be interpreted as a string (?)
			//this should only happen if the column is of some bizarre unexpected type
			printWrongColumnTypeError(e2, isiRow);
		}
		//construct self reference from tokens we just collected
		String[] selfReferenceTokens = 
			(String[]) selfReferenceTokenList.toArray(new String[selfReferenceTokenList.size()]);
		String selfReference = StringUtil.join(selfReferenceTokens, ISI_FIELD_SEPARATOR);
		return selfReference;
	}
	
	/*
	 * This method solves the following problem:
	 *   The name ISI uses for a journal when a paper cites another paper is similar but different from the journal name each paper
	 *      claims that it was cited in.
	 *   This makes it impossible to construct a self-reference for a paper, given only the information present in that paper itself.
	 * Solution:
	 *   Change the names of journals as they appear in papers to the name that journals are cited with.
	 */
	private Table cleanReferences(Table isiTable) {
		//record the cited journal names for each citation reference of each paper, categorized by first letter
		SetMap citedJournalNamesByFirstLetter = extractCitedJournalNames(isiTable);
		//record the normal journal names for each paper, categorized by first letter
		SetMap journalNamesByFirstLetter = extractJournalNames(isiTable);
		//create a map where journal names correspond to their cited journal names
		Map journalNameToCitedJournalName = 
			linkJournalNamesToCitedJournalNames(citedJournalNamesByFirstLetter, journalNamesByFirstLetter);
		//go through each paper, and replace the journal name with the cited journal name
		isiTable = replaceJournalNamesWithCitedJournalNames(isiTable, journalNameToCitedJournalName);
		return isiTable;
	}
	
	private Table replaceJournalNamesWithCitedJournalNames(
			Table isiTable,
			Map journalNameToCitedJournalName) {
		//for each paper in the isi table...
		for (IntIterator tableIt = isiTable.rows(); tableIt.hasNext();) {
			int rowIndex = tableIt.nextInt();
			Tuple row = isiTable.getTuple(rowIndex);
			//for each journal name...
			String journalName = row.getString(ISITag.TWENTY_NINE_CHAR_JOURNAL_ABBREV.columnName);
			if (journalName == null) {continue;};
			//replace it with the cited journal name, it there is a known replacement
			String citedJournalName = (String) journalNameToCitedJournalName.get(journalName);
			if (citedJournalName != null) {
				row.setString(ISITag.TWENTY_NINE_CHAR_JOURNAL_ABBREV.columnName, citedJournalName);
			}
		}
		return isiTable;
	}
	
	private SetMap extractCitedJournalNames(Table isiTable) {
		SetMap citedJournalNames = new SetMap();
		//for each paper in the isi table...
		for (IntIterator tableIt = isiTable.rows(); tableIt.hasNext();) {
			int rowIndex = tableIt.nextInt();
			Tuple row = isiTable.getTuple(rowIndex);
			//for each cited reference...
			String citedReferences = row.getString(ISITag.CITED_REFERENCES.columnName);
			if (citedReferences == null) {continue;};
			String[] eachCitedReference = citedReferences.split("\\" + ISI_CITATION_SEPARATOR);
			for (int i = 0; i < eachCitedReference.length; i++) {
				String citedReference = eachCitedReference[i];
				//extract the journal name from the reference
				String citedJournalName = extractCitedJournalName(citedReference);
				//add that journal name to our set map, with the first letter as a key
				if (citedJournalName != null) {
					String firstLetter = extractFirstLetter(citedJournalName);
					if (firstLetter != null) {
					citedJournalNames.put(firstLetter, citedJournalName);
					}
				}
			}
		}
		return citedJournalNames;
	}
	
	private SetMap extractJournalNames(Table isiTable) {
		SetMap journalNames = new SetMap();
		//for each paper in the isi table...
		for (IntIterator tableIt = isiTable.rows(); tableIt.hasNext();) {
			int rowIndex = tableIt.nextInt();
			Tuple row = isiTable.getTuple(rowIndex);
			//add the journal name to our set map
			String journalName = row.getString(ISITag.TWENTY_NINE_CHAR_JOURNAL_ABBREV.columnName);
			if (journalName == null) {continue;};
			String firstLetter = extractFirstLetter(journalName);
			if (firstLetter != null) {
				journalNames.put(firstLetter, journalName);
			}
		}
		return journalNames;
	}
	
	private Map linkJournalNamesToCitedJournalNames(
			SetMap journalNamesByFirstLetter,
			SetMap citedJournalNamesByFirstLetter) {
		Map journalNameToCitedJournalName = new Hashtable();
		//for each first letter of a journal name...
		Set journalNameKeys = journalNamesByFirstLetter.keySet();
		for (Iterator firstLetterKeyIt = journalNameKeys.iterator(); firstLetterKeyIt.hasNext();) {
			String firstLetterKey = (String) firstLetterKeyIt.next();
			//get the journal names that start with that letter
			Set journalNamesStartingWithLetter = journalNamesByFirstLetter.get(firstLetterKey);
			//and get the cited journals that start with that letter
			Set citedJournalNamesStartingWithLetter = citedJournalNamesByFirstLetter.get(firstLetterKey);
			//link the journal names to the best matching cited journal names, if they exist.
			linkJournalNamesOfSameFirstLetter(journalNameToCitedJournalName,
					journalNamesStartingWithLetter,
					citedJournalNamesStartingWithLetter);
		}
		return journalNameToCitedJournalName;
	}
	
	private Map linkJournalNamesOfSameFirstLetter(Map links, Set journalNames, Set citedJournalNames) {
		if (journalNames == null || citedJournalNames == null) {return links;}
	
		//for each journal name...
		for (Iterator journalNameIt = journalNames.iterator(); journalNameIt.hasNext();) {
			String journalName = (String) journalNameIt.next();
			String bestCitedJournalName = null;
			float NO_CHANGE_THRESHOLD = .1f;
			float bestCitedJournalSimilarity = NO_CHANGE_THRESHOLD;
			for (Iterator citedJournalNameIt = citedJournalNames.iterator(); citedJournalNameIt.hasNext();) {
				String citedJournalName = (String) citedJournalNameIt.next();
				//compare it to each cited journal name, and 
				float similarity = calculateNameSimilarity(journalName, citedJournalName);
				if (similarity > bestCitedJournalSimilarity) {
					bestCitedJournalName = citedJournalName;
					bestCitedJournalSimilarity = similarity;
				}
			}
			if (bestCitedJournalName != null) {
				links.put(journalName, bestCitedJournalName);
			}
		}
		return links;
	}
	
	private float calculateNameSimilarity(String jn, String cjn) {
		StringBuilder log = new StringBuilder();
		log.append("Calcuating '" + jn + "' and '" + cjn + "'.\r\n");
		String whitespace = "\\s";
		String[] jnWords = trimAfterEmpties(jn.split(whitespace));
		String[] cjnWords = trimAfterEmpties(cjn.split(whitespace));
		String[] oneWithMoreWords = getLongest(jnWords, cjnWords);
		float scoreCounter = 0f;
		//look through indices where both have letters
		int i = 0;
		for (; i < jnWords.length && i < cjnWords.length; i++) {
			scoreCounter += calculateWordSimilarity(jnWords[i], cjnWords[i], log);
		}
		
		//look through indices where only one has letters
		for (int j = i; j < jnWords.length || j < cjnWords.length; j++) {
			scoreCounter += calculateWordSimilarity(oneWithMoreWords[j], null, log);
		}
		
		float finalScore = scoreCounter / (float) oneWithMoreWords.length;
		if (finalScore > -.5f && finalScore < .9) {
			System.out.println("" + jn + " == " + cjn + ": " + finalScore);
			System.out.println(log.toString());
		}
		return finalScore;
	}
	
	private static final float SAME_WORD_SCORE = 1f;
	private static final float DIFFERENT_WORD_SCORE = -5f;
	private static final float MISSING_LETTER_ABBREVIATION_SCORE = -3f;
	private static final float MAYBE_USED_DIFFERENT_VOWEL_SCORE = .5f;
	private static final float EXTRA_LETTER_PENALTY = -.3f;
	
	private static final String VOWEL = "[aeiouyAEIOUY]";
	private static final String ALL_NUMBERS =  "^[0-9]+$";
	
	private float calculateWordSimilarity(String word1, String word2, StringBuilder log) {
		//TODO: This needs to be refactored.
		log.append("  comparing '" + word1 + "' with '" + word2 + "'\r\n");
		if (word1 == null && word2 == null) {
			log.append("    both null. returning 0\r\n");
			return 0f;
		} else if (word1 == null) {
			if (word2.matches(ALL_NUMBERS)) {
				return -.5f;
			}
			log.append("    word1 is null. returning " + word1.length() * EXTRA_LETTER_PENALTY + "\r\n");
			return word2.length() * EXTRA_LETTER_PENALTY + -5.0f;
		} else if (word2 == null) {
			if (word1.matches(ALL_NUMBERS)) {
				return -.5f;
			}
			log.append("    word2 is null. returning " + word1.length() * EXTRA_LETTER_PENALTY + "\r\n");
			return word1.length() * EXTRA_LETTER_PENALTY + -5.0f;
		} else {
			int minLength = Math.min(word1.length(), word2.length());
			int maxLength = Math.max(word1.length(), word2.length());
			String shortWord = getShortWord(word1, word2);
			String longWord = getLongWord(word1, word2);
			float scoreModifier = 0f;
			boolean sameWord = true;
			boolean skippedVowel = false;
			int vowelsSkipped = 0;
			boolean missingLetterAbbreviation = false;
			boolean maybeUsedWrongVowel = false;
			for (int i = 0; i < minLength; i++) {
				if (word1.charAt(i) == word2.charAt(i)) {
					scoreModifier += 1f;
				} else if (String.valueOf(word1.charAt(i)).matches(VOWEL) && String.valueOf(word2.charAt(i)).matches(VOWEL) && minLength == maxLength && i != 1) {
					log.append("Maybe used wrong vowel");
					maybeUsedWrongVowel = true;
				} else {
					maybeUsedWrongVowel = false;
					log.append("      non-matching letters " + word1.charAt(i) + "," + word2.charAt(i));
					if (i == 0) {
						sameWord = false;
					}
					
					int longWordPlace = i;
					for (int j = i; j < minLength; j++) {
			
						int index = longWord.substring(longWordPlace).indexOf(shortWord.charAt(j));
						
				
						log.append("      does '" + longWord.substring(longWordPlace) + "' contain " + shortWord.charAt(j) +  "'?");
						if (index != -1) {
							
							//if we skipped over something other than a vowel, it's bad
							if (index > 0) {
								missingLetterAbbreviation = true;
								for (int k = longWordPlace; k < longWordPlace + index; k++) {
									if (! (String.valueOf(longWord.charAt(k))).matches(VOWEL)) {
										skippedVowel = true;
										vowelsSkipped++;
										if (vowelsSkipped > 1) {
											sameWord = false;
										}
										log.append("Skipped a vowel!");
									}
								}
							}
							log.append("      Yes");
							scoreModifier += 1f / (float) (index + 1);
							longWordPlace += index + 1;
						} else {
							log.append("      No");
							sameWord = false;
						}
					}
					break;
			}
			}
			float finalScoreModifier = scoreModifier / maxLength;
			float finalScore = 0;
			if (sameWord && ! missingLetterAbbreviation) {
				if (! maybeUsedWrongVowel) {
				finalScore = SAME_WORD_SCORE * finalScoreModifier;
				} else {
					finalScore = MAYBE_USED_DIFFERENT_VOWEL_SCORE * finalScoreModifier;
				}
			} else if (missingLetterAbbreviation && sameWord) {
				log.append("Missing letter abbreviation score!: (final score modifier) " + finalScoreModifier + "\r\n");
				finalScore = MISSING_LETTER_ABBREVIATION_SCORE * (.6f - finalScoreModifier);
		    } else {
				finalScore = DIFFERENT_WORD_SCORE * (1 - finalScoreModifier) -1.5f;
			}
			log.append("   returning: " + finalScore + "\r\n");
			return finalScore;
		}
	}
	
	private String[] getLongest(String[] sa1, String[] sa2) {
		if (sa1.length >= sa2.length) {
			return sa1;
		} else {
			return sa2;
		}
	}
	
	private String getLongWord(String w1, String w2) {
		if (w1.length() >= w2.length()) {
			return w1;
		} else {
			return w2;
		}
	}
	
	private String getShortWord(String w1, String w2) {
		if (w1.length() >= w2.length()) {
			return w2;
		} else {
			return w1;
		}
	}
	
	private String extractCitedJournalName(String citedReference) {
		if (citedReference == null) {printNullCitedReferenceWarning(); return null;}
		//the journal name SHOULD be in the third comma-and-space-delinated section.
		String[] sections = citedReference.split(ISI_FIELD_SEPARATOR);
		if (sections.length < 3) { printNullCitedReferenceWarning(); return null;}
		String citedJournalName = sections[2];
		return citedJournalName;
	}

	private String extractFirstLetter(String citedJournalName) {
		if (citedJournalName.trim().length() == 0) { printZeroLengthCitedJournalNameWarning(); return null;}
		String firstLetter = citedJournalName.trim().substring(0, 1);
		return firstLetter;
	}
	
	private String extractFirstAndLastNameOfFirstAuthor(Tuple isiRow) {
		String authors = isiRow.getString(ISITag.AUTHORS.columnName); 
		if (authors == null) return null;
		String[] eachAuthor = authors.split("\\" + ISI_AUTHOR_SEPARATOR); //since it's a regex, we have to escape the pipe 
		if (eachAuthor.length == 0) return handleNoAuthors();
		String firstAuthor = eachAuthor[0];
		String oneOrMoreCommasOrWhitespaces = "[,\\s]+";
		String[] nameTokens = firstAuthor.trim().split(oneOrMoreCommasOrWhitespaces);
		String firstAndLastNameOfFirstAuthor = StringUtil.join(nameTokens, " ");
		return firstAndLastNameOfFirstAuthor;
	}
	
	private String extractPublicationYear(Tuple isiRow) {
		String publicationYear = isiRow.getString(ISITag.PUBLICATION_YEAR.columnName);
		return publicationYear;
	}
	
	private String extractAbbreviatedJournalName(Tuple isiRow) {
		String abbreviatedJournalName = isiRow.getString(ISITag.TWENTY_NINE_CHAR_JOURNAL_ABBREV.columnName);
		return abbreviatedJournalName;
		
	}
	
	private String extractVolume(Tuple isiRow) {
		String volume = isiRow.getString(ISITag.VOLUME.columnName);
		if (volume == null) {return null;}
		return "V" + volume;
	}
	
	private String extractFirstPage(Tuple isiRow) {
		String firstPage = isiRow.getString(ISITag.BEGINNING_PAGE.columnName);
		if (firstPage == null) {return null;}
		String startsWithALetter = "^[a-zA-Z].*$";
		if (firstPage.matches(startsWithALetter)) {
			return firstPage;
		} else {
			return "P" + firstPage;
		}
	}
	
	private String handleNoAuthors() {
		return null;
	}
	
	private String[] trimAfterEmpties(String[] s) {
		List sList = new ArrayList();
		for (int i = 0; i < s.length; i++) {
			if (! s[i].equals("")) {
				sList.add(s[i]);
			} else {
				break; // trims too 
			}
		}
		
		return (String[]) sList.toArray(new String[sList.size()]);
	}
	
	private void printWrongColumnTypeError(DataTypeException e2, Tuple isiRow) {
		this.log.log(LogService.LOG_ERROR, "Some elements in the tuple '" + isiRow + "' cannot be converted to a String (apparently)", e2);
	}
	
	private void handleNoAuthor(Tuple isiRow) {
		this.log.log(LogService.LOG_WARNING, "The row " + isiRow +
				" has no author column. The '" + 
				SELF_REFERENCE_COLUMN_NAME + "' field  will be invalid");
	}

	private void handleNoYear(Tuple isiRow) {
		this.log.log(LogService.LOG_WARNING, "The row " + isiRow +
				" has no year column. The '" + 
				SELF_REFERENCE_COLUMN_NAME + "' field may be invalid");
	}

	private void handleNoJournal(Tuple isiRow) {
		this.log.log(LogService.LOG_WARNING, "The row " + isiRow +
				" has no jounal column (J9). The '" + 
				SELF_REFERENCE_COLUMN_NAME + "' field may be invalid");
	}

	private void handleNoVolume(Tuple isiRow) {
		//This field is optional (though very common). No error will be printed.
	}
	private void handleNoPage(Tuple isiRow) {
		//This field is optional (though very common). No error will be printed.
	}
	
	private void printNullCitedReferenceWarning() {
		//do nothing. This error is difficult to report in an informative way.
	}
	
	private void printZeroLengthCitedJournalNameWarning() {
		//do nothing. This error is difficult to report in an informative way.
	}
	
}
