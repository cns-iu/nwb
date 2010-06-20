package edu.iu.sci2.database.star.load;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.cishell.utilities.StringUtilities;
import org.osgi.service.log.LogService;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import edu.iu.sci2.database.star.load.utility.validator.CSVDataValidationRules;
import edu.iu.sci2.database.star.load.utility.validator.exception.CSVBodyValidationException;
import edu.iu.sci2.database.star.load.utility.validator.exception.CSVHeaderValidationException;

public class StarDatabaseCSVDataValidationRules implements CSVDataValidationRules {
	private LogService logger;

	public StarDatabaseCSVDataValidationRules(LogService logger) {
		this.logger = logger;
	}

	public void validateHeader(String[] header) throws CSVHeaderValidationException {
		Multimap<String, String> normalizedNamesByNames = getNormalizedNamesByNames(header);
		Collection<Collection<String>> duplicateNamesSets =
			getDuplicateNamesSets(normalizedNamesByNames);
		logDuplicateNamesSets(duplicateNamesSets, this.logger);
	}

	public void validateRow(String[] row) throws CSVBodyValidationException {
	}

	private static Multimap<String, String> getNormalizedNamesByNames(String[] names) {
		Multimap<String, String> normalizedNamesByNames = ArrayListMultimap.create();

		for (String name : names) {
			normalizedNamesByNames.put(normalizeName(name), name);
		}

		return normalizedNamesByNames;
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

	public static void logDuplicateNamesSets(
			Collection<Collection<String>> duplicateNamesSets, LogService logger)
			throws CSVHeaderValidationException {
		if (duplicateNamesSets.size() > 0) {
			String logMessage = buildLogMessage(duplicateNamesSets);
			logger.log(LogService.LOG_ERROR, logMessage);

			throw new CSVHeaderValidationException(logMessage);
		}
	}

	public static String normalizeName(String name) {
		Pattern punctuationMatcher = Pattern.compile("[^a-zA-Z \\w\\d _]");

		return punctuationMatcher.matcher(name.toUpperCase()).replaceAll("");
	}

	private static String buildLogMessage(Collection<Collection<String>> duplicateNamesSets) {
		final String logMessageHeader =
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