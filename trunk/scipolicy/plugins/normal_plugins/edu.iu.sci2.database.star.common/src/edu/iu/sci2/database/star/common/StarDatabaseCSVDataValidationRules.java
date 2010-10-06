package edu.iu.sci2.database.star.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.cishell.utilities.StringUtilities;
import org.osgi.service.log.LogService;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

import edu.iu.sci2.database.star.common.utility.csv.validator.CSVDataValidationRules;
import edu.iu.sci2.database.star.common.utility.csv.validator.exception.CSVBodyValidationException;
import edu.iu.sci2.database.star.common.utility.csv.validator.exception.CSVHeaderValidationException;

public class StarDatabaseCSVDataValidationRules implements CSVDataValidationRules {
	private LogService logger;

	public StarDatabaseCSVDataValidationRules(LogService logger) {
		this.logger = logger;
	}

	public void validateHeader(String[] header) throws CSVHeaderValidationException {
		Multimap<String, String> normalizedNamesByNames = getNormalizedNamesByNames(header);

		Collection<Integer> emptyColumnNameIndices =
			determineColumnHeaderNames(normalizedNamesByNames);
		logEmptyColumnNames(emptyColumnNameIndices, this.logger);

		Collection<Collection<String>> duplicateNamesSets =
			getDuplicateNamesSets(normalizedNamesByNames);
		logDuplicateNamesSets(duplicateNamesSets, this.logger);
	}

	public void validateRow(String[] row) throws CSVBodyValidationException {
	}

	private static Multimap<String, String> getNormalizedNamesByNames(String[] names) {
		Multimap<String, String> normalizedNamesByNames = LinkedHashMultimap.create();

		for (String name : names) {
			normalizedNamesByNames.put(normalizeName(name), name);
		}

		return normalizedNamesByNames;
	}

	private static Collection<Integer> determineColumnHeaderNames(
			Multimap<String, String> normalizedNamesByNames) throws CSVHeaderValidationException {
		// TODO: Refactor this to use Google Collections?

		Collection<Integer> emptyColumnNameIndices = new HashSet<Integer>();

		int columnIndex = 0;
		for (String columnName : normalizedNamesByNames.values()) {
			if (StringUtilities.isNull_Empty_OrWhitespace(columnName)) {
				emptyColumnNameIndices.add(columnIndex);
			}

			columnIndex++;
		}

		return emptyColumnNameIndices;
	}

	private static Collection<Collection<String>> getDuplicateNamesSets(
			Multimap<String, String> normalizedNamesByNames) {
		Collection<Collection<String>> duplicateNamesSets = new ArrayList<Collection<String>>();

		for (Entry<String, Collection<String>> entry : normalizedNamesByNames.asMap().entrySet()) {
			Collection<String> values = entry.getValue();

			if (values.size() > 1) {
				duplicateNamesSets.add(values);
			}
		}

		return duplicateNamesSets;
	}

	// TODO: Think about throwing multiple error messages at a time.
	public static void logEmptyColumnNames(
			Collection<Integer> emptyColumnNameIndices, LogService logger)
			throws CSVHeaderValidationException {
		if (emptyColumnNameIndices.size() > 0) {
			String logMessage = buildEmptyColumnNamesLogMessage(emptyColumnNameIndices);
			logger.log(LogService.LOG_ERROR, logMessage);

			throw new CSVHeaderValidationException(logMessage);
		}
	}

	public static void logDuplicateNamesSets(
			Collection<Collection<String>> duplicateNamesSets, LogService logger)
			throws CSVHeaderValidationException {
		if (duplicateNamesSets.size() > 0) {
			String logMessage = buildDuplicateNamesLogMessage(duplicateNamesSets);
			logger.log(LogService.LOG_ERROR, logMessage);

			throw new CSVHeaderValidationException(logMessage);
		}
	}

	public static String normalizeName(String name) {
		Pattern punctuationMatcher = Pattern.compile("[^a-zA-Z \\w\\d _]");
		String withoutPunctuation = punctuationMatcher.matcher(name.toUpperCase()).replaceAll("");
		String withWhitespaceReplacedByUnderscores = StringUtilities.implodeStringArray(
			StringUtilities.tokenizeByWhitespace(withoutPunctuation), "_");

		return withWhitespaceReplacedByUnderscores;
	}

	private static String buildEmptyColumnNamesLogMessage(
			Collection<Integer> emptyColumnNameIndices) {
		String format =
			"The names of the columns in your CSV file must all be specified to be usable in" +
			" a database.  When normalizing them, one or more name(s) were found that cannot be" +
			"normalized for use in a database.  Column indices: [ %s ]";
		String logMessage = String.format(
			format, StringUtilities.implodeItems(emptyColumnNameIndices, ", "));

		return logMessage;
	}

	private static String buildDuplicateNamesLogMessage(
			Collection<Collection<String>> duplicateNamesSets) {
		String logMessageHeader =
			"The names of the columns in your CSV file must be normalized to be usable in " +
			"a database.  When normalizing, one or more set(s) of columns conflicted: ";
		Collection<String> duplicateNamesLogMessages = new ArrayList<String>();

		for (Collection<String> duplicateNames : duplicateNamesSets) {
			duplicateNamesLogMessages.add(
				"(" + StringUtilities.implodeItems(duplicateNames, ", ") + ")");
		}

		return logMessageHeader + StringUtilities.implodeItems(duplicateNamesLogMessages, ", ");
	}
}