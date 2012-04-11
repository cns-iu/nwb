package edu.iu.nwb.shared.isiutil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.cishell.utilities.StringUtilities;
import org.osgi.service.log.LogService;

import prefuse.data.DataTypeException;
import prefuse.data.Table;
import prefuse.data.Tuple;
import prefuse.util.collections.IntIterator;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;

import edu.iu.nwb.shared.isiutil.exception.CitationExtractionPreparationException;

public class ISICitationExtractionPreparer {
	public static final String SELF_REFERENCE_COLUMN_NAME = "Cite Me As";
	private static final String ISI_AUTHOR_SEPARATOR = "|";
	private static final String ISI_CITATION_SEPARATOR = "|";
	private static final String ISI_FIELD_SEPARATOR = ", ";

	private LogService log;

	public ISICitationExtractionPreparer(LogService log) {
		this.log = log;
	}

	/**
	 * Prepare the table for citation extraction.  This will side effect isiTable.
	 * @param isiTable This table will be side-effected! 
	 */
	public Table prepareForCitationExtraction(Table isiTable, boolean shouldCleanReferences)
			throws CitationExtractionPreparationException {
		if (shouldCleanReferences) {
			/*
			 * Make journal names in papers conform to journal names used to reference
			 *  those papers.
			 */
			isiTable = cleanReferences(isiTable);
		}

		/*
		 * Create self-references (that is, a field that looks like how other papers will reference
		 *  it) for each paper.
		 */
		isiTable = addSelfReferences(isiTable);

		return isiTable;
	}

	// side-effects isiTable
	private Table addSelfReferences(Table isiTable) throws CitationExtractionPreparationException {
		// create the self-reference column
		isiTable.addColumn(SELF_REFERENCE_COLUMN_NAME, String.class);
		// for each record in the table...
		for (IntIterator tableIt = isiTable.rows(); tableIt.hasNext();) {
			int rowIndex = tableIt.nextInt();
			Tuple row = isiTable.getTuple(rowIndex);
			// calculate the self-reference based on the contents of other fields
			String selfReference = createSelfReference(row);
			// add the self-reference to the current record
			isiTable.setString(rowIndex, SELF_REFERENCE_COLUMN_NAME, selfReference);
		}
		return isiTable;
	}

	private String createSelfReference(Tuple isiRow) throws CitationExtractionPreparationException {
		List<String> selfReferenceTokenList = new ArrayList<String>();
		try {
			// standard elements
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
			// optional elements
			String volume = extractVolume(isiRow);
			if (volume != null) {
				selfReferenceTokenList.add(volume);
			} else {
				handleNoVolume();
			}
			String page = extractFirstPage(isiRow);
			if (page != null) {
				selfReferenceTokenList.add(page);
			} else {
				handleNoPage();
			}
			
			String doi = extractDOI(isiRow);
			if (doi != null) {
				selfReferenceTokenList.add(doi);
			} else {
				handleNoDOI();
			}
			
		} catch (ArrayIndexOutOfBoundsException e) {
			// column requested does not exist (for entire table or just this field?)
			// Fail silently. This will happen normally. The remainder of the self reference will be returned.
		} catch (DataTypeException e) {
			// column type cannot be interpreted as a string (?)
			// this should only happen if the column is of some bizarre unexpected type
			throw new CitationExtractionPreparationException(
					"Some elements in the tuple '" + isiRow
					+ "' cannot be converted to a String (apparently)", e);
		}
		// construct self reference from tokens we just collected
		String[] selfReferenceTokens =
			selfReferenceTokenList.toArray(new String[selfReferenceTokenList.size()]);
		String selfReference =
			StringUtilities.implodeStringArray(selfReferenceTokens, ISI_FIELD_SEPARATOR);

		return selfReference;
	}

	private static Table replaceJournalNamesWithCitedJournalNames(Table isiTable, Map<String, String> journalNameToCitedJournalName) {
		// for each paper in the isi table...
		for (IntIterator tableIt = isiTable.rows(); tableIt.hasNext();) {
			int rowIndex = tableIt.nextInt();
			Tuple row = isiTable.getTuple(rowIndex);
			// for each journal name...
			String journalName = row.getString(ISITag.TWENTY_NINE_CHAR_JOURNAL_ABBREVIATION.columnName);
			if (journalName == null) {
				continue;
			}
			
			// replace it with the cited journal name, it there is a known replacement
			String citedJournalName = journalNameToCitedJournalName.get(journalName);
			if (citedJournalName != null) {
				row.setString(ISITag.TWENTY_NINE_CHAR_JOURNAL_ABBREVIATION.columnName, citedJournalName);
			}
		}
		return isiTable;
	}

	private SetMultimap<String, String> extractCitedJournalNames(Table isiTable) {
		SetMultimap<String, String> citedJournalNames = HashMultimap.create();
		
		// for each paper in the isi table...
		for (IntIterator tableIt = isiTable.rows(); tableIt.hasNext();) {
			int rowIndex = tableIt.nextInt();
			Tuple row = isiTable.getTuple(rowIndex);
			// for each cited reference...
			String citedReferences = row.getString(ISITag.CITED_REFERENCES.columnName);
			if (citedReferences == null) {
				continue;
			}
			
			String[] eachCitedReference = citedReferences.split("\\" + ISI_CITATION_SEPARATOR);
			for (int i = 0; i < eachCitedReference.length; i++) {
				String citedReference = eachCitedReference[i];
				// extract the journal name from the reference
				String citedJournalName = extractCitedJournalName(citedReference);
				// add that journal name to our set map, with the first letter as a key
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

	private SetMultimap<String, String> extractJournalNames(Table isiTable) {
		SetMultimap<String, String> journalNames = HashMultimap.create();
		// for each paper in the isi table...
		for (IntIterator tableIt = isiTable.rows(); tableIt.hasNext();) {
			int rowIndex = tableIt.nextInt();
			Tuple row = isiTable.getTuple(rowIndex);
			// add the journal name to our set map
			String journalName = row.getString(ISITag.TWENTY_NINE_CHAR_JOURNAL_ABBREVIATION.columnName);
			if (journalName == null) {
				continue;
			}
			
			String firstLetter = extractFirstLetter(journalName);
			if (firstLetter != null) {
				journalNames.put(firstLetter, journalName);
			}
		}
		return journalNames;
	}

	private static Map<String, String> linkJournalNamesToCitedJournalNames(SetMultimap<String, String> journalNamesByFirstLetter,
			SetMultimap<String, String> citedJournalNamesByFirstLetter) {
		Map<String, String> journalNameToCitedJournalName = new HashMap<String, String>();
		// for each first letter of a journal name...
		Set<String> journalNameKeys = journalNamesByFirstLetter.keySet();
		for (Iterator<String> firstLetterKeyIt = journalNameKeys.iterator(); firstLetterKeyIt.hasNext();) {
			String firstLetterKey = firstLetterKeyIt.next();
			// get the journal names that start with that letter
			Set<String> journalNamesStartingWithLetter = journalNamesByFirstLetter.get(firstLetterKey);
			// and get the cited journals that start with that letter
			Set<String> citedJournalNamesStartingWithLetter = citedJournalNamesByFirstLetter.get(firstLetterKey);
			// link the journal names to the best matching cited journal names, if they exist.
			linkJournalNamesOfSameFirstLetter(journalNameToCitedJournalName, journalNamesStartingWithLetter,
					citedJournalNamesStartingWithLetter);
		}
		return journalNameToCitedJournalName;
	}

	private static Map<String, String> linkJournalNamesOfSameFirstLetter(
			Map<String, String> links, Set<String> journalNames,
			Set<String> citedJournalNames) {
		if (journalNames == null || citedJournalNames == null) {
			return links;
		}

		// for each journal name...
		for (Iterator<String> journalNameIt = journalNames.iterator(); journalNameIt.hasNext();) {
			String journalName = journalNameIt.next();
			String bestCitedJournalName = null;
			float NO_CHANGE_THRESHOLD = .1f;
			float bestCitedJournalSimilarity = NO_CHANGE_THRESHOLD;
			for (Iterator<String> citedJournalNameIt = citedJournalNames.iterator(); citedJournalNameIt.hasNext();) {
				String citedJournalName = citedJournalNameIt.next();
				// compare it to each cited journal name, and
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

	/**
	 * This method solves the following problem: The name ISI used for a journal when a paper cites another paper is
	 * similar but different from the journal name each paper claims that it was cited in. This makes it impossible to
	 * construct a self-reference for a paper, given only the information present in that paper itself. Solution: Change
	 * the names of journals as they appear in papers to the name that journals are cited with.
	 * 
	 * @param isiTable This table will be side-effected!
	 */
	private Table cleanReferences(Table isiTable) {
		// record the cited journal names for each citation reference of each paper, categorized by first letter
		SetMultimap<String, String> citedJournalNamesByFirstLetter = extractCitedJournalNames(isiTable);
		// record the normal journal names for each paper, categorized by first letter
		SetMultimap<String, String> journalNamesByFirstLetter = extractJournalNames(isiTable);
		// create a map where journal names correspond to their cited journal names
		Map<String, String> journalNameToCitedJournalName = linkJournalNamesToCitedJournalNames(citedJournalNamesByFirstLetter,
				journalNamesByFirstLetter);
		// go through each paper, and replace the journal name with the cited journal name
		isiTable = replaceJournalNamesWithCitedJournalNames(isiTable, journalNameToCitedJournalName);
		return isiTable;
	}

	private static float calculateNameSimilarity(String jn, String cjn) {
		StringBuffer bufferLog = new StringBuffer();
		bufferLog.append("Calculating '" + jn + "' and '" + cjn + "'.\r\n");
		String whitespace = "\\s";
		String[] jnWords = trimAfterEmpties(jn.split(whitespace));
		String[] cjnWords = trimAfterEmpties(cjn.split(whitespace));
		String[] oneWithMoreWords = getLongest(jnWords, cjnWords);
		float scoreCounter = 0f;
		// look through indices where both have letters
		int i = 0;
		for (; i < jnWords.length && i < cjnWords.length; i++) {
			scoreCounter += calculateWordSimilarity(jnWords[i], cjnWords[i], bufferLog);
		}

		// look through indices where only one has letters
		for (int j = i; j < jnWords.length || j < cjnWords.length; j++) {
			scoreCounter += calculateWordSimilarity(oneWithMoreWords[j], null, bufferLog);
		}

		float finalScore = scoreCounter / oneWithMoreWords.length;
		if (finalScore > -.5f && finalScore < .5) {
			System.out.println("" + jn + " == " + cjn + ": " + finalScore);
			System.out.println(bufferLog.toString());
		}
		return finalScore;
	}

	private static final float SAME_WORD_SCORE = 1f;
	private static final float DIFFERENT_WORD_SCORE = -5f;
	private static final float MISSING_LETTER_ABBREVIATION_SCORE = -3f;
	private static final float MAYBE_USED_DIFFERENT_VOWEL_SCORE = .5f;
	private static final float EXTRA_LETTER_PENALTY = -.3f;

	private static final String VOWEL = "[aeiouyAEIOUY]";
	private static final String ALL_NUMBERS = "^[0-9]+$";

	//TODO: Maybe expose some of these through preferences.
	private static float calculateWordSimilarity(String word1, String word2, StringBuffer wordSimilarityCalculationLog) {
		// TODO: This needs to be refactored, a.k.a this code is horrible and I know it.
		wordSimilarityCalculationLog.append("  comparing '" + word1 + "' with '" + word2 + "'\r\n");
		if (word1 == null && word2 == null) {
			wordSimilarityCalculationLog.append("    both null. returning 0\r\n");
			return 0f;
		} else if (word1 == null) {
			if (word2.matches(ALL_NUMBERS)) {
				return -.5f;
			}
			wordSimilarityCalculationLog.append("    word1 is null. returning " + word2.length() * EXTRA_LETTER_PENALTY + "\r\n");
			return word2.length() * EXTRA_LETTER_PENALTY + -5.0f;
		} else if (word2 == null) {
			if (word1.matches(ALL_NUMBERS)) {
				return -.5f;
			}
			wordSimilarityCalculationLog.append("    word2 is null. returning " + word1.length() * EXTRA_LETTER_PENALTY + "\r\n");
			return word1.length() * EXTRA_LETTER_PENALTY + -5.0f;
		} else {
			int minLength = Math.min(word1.length(), word2.length());
			int maxLength = Math.max(word1.length(), word2.length());
			String shortWord = getShortWord(word1, word2);
			String longWord = getLongWord(word1, word2);
			float scoreModifier = 0f;
			boolean sameWord = true;
			int vowelsSkipped = 0;
			boolean missingLetterAbbreviation = false;
			boolean maybeUsedWrongVowel = false;
			for (int i = 0; i < minLength; i++) {
				if (word1.charAt(i) == word2.charAt(i)) {
					scoreModifier += 1f;
				} else if (String.valueOf(word1.charAt(i)).matches(VOWEL)
						&& String.valueOf(word2.charAt(i)).matches(VOWEL) && minLength == maxLength && i != 1) {
					wordSimilarityCalculationLog.append("Maybe used wrong vowel");
					maybeUsedWrongVowel = true;
				} else {
					maybeUsedWrongVowel = false;
					wordSimilarityCalculationLog.append("      non-matching letters " + word1.charAt(i) + "," + word2.charAt(i));
					if (i == 0) {
						sameWord = false;
					}

					int longWordPlace = i;
					for (int j = i; j < minLength; j++) {

						int index = longWord.substring(longWordPlace).indexOf(shortWord.charAt(j));

						wordSimilarityCalculationLog.append("      does '" + longWord.substring(longWordPlace) + "' contain "
								+ shortWord.charAt(j) + "'?");
						if (index != -1) {

							// if we skipped over something other than a vowel, it's bad
							if (index > 0) {
								missingLetterAbbreviation = true;
								for (int k = longWordPlace; k < longWordPlace + index; k++) {
									if (!(String.valueOf(longWord.charAt(k))).matches(VOWEL)) {
										vowelsSkipped++;
										if (vowelsSkipped > 1) {
											sameWord = false;
										}
										wordSimilarityCalculationLog.append("Skipped a vowel!");
									}
								}
							}
							wordSimilarityCalculationLog.append("      Yes");
							scoreModifier += 1f / (index + 1);
							longWordPlace += index + 1;
						} else {
							wordSimilarityCalculationLog.append("      No");
							sameWord = false;
						}
					}
					break;
				}
			}
			float finalScoreModifier = scoreModifier / maxLength;
			float finalScore = 0;
			if (sameWord && !missingLetterAbbreviation) {
				if (!maybeUsedWrongVowel) {
					finalScore = SAME_WORD_SCORE * finalScoreModifier;
				} else {
					finalScore = MAYBE_USED_DIFFERENT_VOWEL_SCORE * finalScoreModifier;
				}
			} else if (missingLetterAbbreviation && sameWord) {
				wordSimilarityCalculationLog.append("Missing letter abbreviation score!: (final score modifier) " + finalScoreModifier + "\r\n");
				finalScore = MISSING_LETTER_ABBREVIATION_SCORE * (.6f - finalScoreModifier);
			} else {
				finalScore = DIFFERENT_WORD_SCORE * (1 - finalScoreModifier) - 1.5f;
			}
			wordSimilarityCalculationLog.append("   returning: " + finalScore + "\r\n");
			return finalScore;
		}
	}

	private static String[] getLongest(String[] sa1, String[] sa2) {
		if (sa1.length >= sa2.length) {
			return sa1;
		}
		return sa2;
	}

	private static String getLongWord(String w1, String w2) {
		if (w1.length() >= w2.length()) {
			return w1;
		}
		return w2;
	}

	private static String getShortWord(String w1, String w2) {
		if (w1.length() >= w2.length()) {
			return w2;
		}
		return w1;
	}

	private String extractCitedJournalName(String citedReference) {
		if (citedReference == null) {
			printNullCitedReferenceWarning();
			return null;
		}
		// the journal name SHOULD be in the third comma-and-space-delinated section.
		String[] sections = citedReference.split(ISI_FIELD_SEPARATOR);
		if (sections.length < 3) {
			printNullCitedReferenceWarning();
			return null;
		}
		String citedJournalName = sections[2];
		return citedJournalName;
	}

	private String extractFirstLetter(String citedJournalName) {
		if (citedJournalName.trim().length() == 0) {
			printZeroLengthCitedJournalNameWarning();
			return null;
		}
		String firstLetter = citedJournalName.trim().substring(0, 1);
		return firstLetter;
	}

	private static String extractFirstAndLastNameOfFirstAuthor(Tuple isiRow) {
		String authors = isiRow.getString(ISITag.AUTHORS.columnName);
		if (authors == null)
			return null;
		String[] eachAuthor = authors.split("\\" + ISI_AUTHOR_SEPARATOR); // since it's a regex, we have to escape the
																			// pipe
		if (eachAuthor.length == 0)
			return handleNoAuthors();
		String firstAuthor = eachAuthor[0];
		String oneOrMoreCommasOrWhitespaces = "[,\\s]+";
		String[] nameTokens = firstAuthor.trim().split(oneOrMoreCommasOrWhitespaces);
		String firstAndLastNameOfFirstAuthor = StringUtilities.implodeStringArray(nameTokens, " ");
		return firstAndLastNameOfFirstAuthor;
	}

	private static String extractPublicationYear(Tuple isiRow) {
		String publicationYear = isiRow.getString(ISITag.PUBLICATION_YEAR.columnName);
		return publicationYear;
	}

	private static String extractAbbreviatedJournalName(Tuple isiRow) {
		String abbreviatedJournalName = isiRow.getString(ISITag.TWENTY_NINE_CHAR_JOURNAL_ABBREVIATION.columnName);
		return abbreviatedJournalName;

	}

	private static String extractVolume(Tuple isiRow) {
		String volume = isiRow.getString(ISITag.VOLUME.columnName);
		if (volume == null) {
			return null;
		}
		return "V" + volume;
	}

	private static String extractFirstPage(Tuple isiRow) {
		String firstPage = isiRow.getString(ISITag.BEGINNING_PAGE.columnName);
		if (firstPage == null) {
			return null;
		}
		
		String startsWithALetter = "^[a-zA-Z].*$";
		if (firstPage.matches(startsWithALetter)) {
			return firstPage;
		}
		
		return "P" + firstPage;
	}
	
	private static String extractDOI(Tuple isiRow) {
		String doi = isiRow.getString(ISITag.DOI.columnName);
		if (doi == null) {
			return null;
		}
		return "DOI " + doi;
	}

	private static String handleNoAuthors() {
		return null;
	}

	private static String[] trimAfterEmpties(String[] s) {
		List<String> sList = new ArrayList<String>();
		for (int i = 0; i < s.length; i++) {
			if (!s[i].equals("")) {
				sList.add(s[i]);
			} else {
				break; // trims too
			}
		}

		return sList.toArray(new String[sList.size()]);
	}

	private void handleNoAuthor(Tuple isiRow) {
		this.log.log(LogService.LOG_WARNING, "The row " + isiRow + " has no author column. The '"
				+ SELF_REFERENCE_COLUMN_NAME + "' field  will be invalid");
	}

	private void handleNoYear(Tuple isiRow) {
		this.log.log(LogService.LOG_WARNING, "The row " + isiRow + " has no year column. The '"
				+ SELF_REFERENCE_COLUMN_NAME + "' field may be invalid");
	}

	private void handleNoJournal(Tuple isiRow) {
		this.log.log(LogService.LOG_WARNING, "The row " + isiRow + " has no jounal column (J9). The '"
				+ SELF_REFERENCE_COLUMN_NAME + "' field may be invalid");
	}

	private void handleNoVolume() {
		// This field is optional (though very common). No error will be printed.
	}

	private void handleNoPage() {
		// This field is optional (though very common). No error will be printed.
	}
	
	private void handleNoDOI() {
		// This field is optional (though very common). No error will be printed.
	}

	private void printNullCitedReferenceWarning() {
		// do nothing. This error is difficult to report in an informative way.
	}

	private void printZeroLengthCitedJournalNameWarning() {
		// do nothing. This error is difficult to report in an informative way.
	}

}
