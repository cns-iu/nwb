package edu.iu.sci2.database.star.common.utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.cishell.framework.data.Data;
import org.cishell.utilities.ListUtilities;
import org.osgi.service.log.LogService;

import edu.iu.sci2.database.star.common.StarDatabaseCSVDataValidationRules;
import edu.iu.sci2.database.star.common.parameter.ColumnDescriptor;
import edu.iu.sci2.database.star.common.utility.csv.validator.CSVDataValidator;
import edu.iu.sci2.database.star.common.utility.csv.validator.exception.CSVHeaderValidationException;

public class StarDatabaseDataValidator {
	/*
 	 * In order to translate the data from CSV to database, we must normalize column names for
 	 *  the database.  One potential issue with this is normalized-name conflicts.  We can't
 	 *  reasonably handle such issues automatically, so we simply validate and fail if
 	 *  there are any.
 	 */
	public static void validateData(Data data, LogService logger) {
    	try {
			CSVDataValidator validator =
				new CSVDataValidator(data, new StarDatabaseCSVDataValidationRules(logger));
			validator.validateHeader();
		}
		catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (CSVHeaderValidationException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
    }

	/*
	 * We were able to validate the input CSV's column names at mutateParameters time, but we
	 *  cannot validate the core entity table name until the user entered it (naturally).
	 * We have to make sure the user-entered core entity table name won't conflict with the
	 *  (normalized) input-CSV's column names.
	 */
	@SuppressWarnings("unchecked")	// Raw Collection (logDuplicateNamesSets)
	public static void validateCoreEntityTableName(
			String coreEntityTableName,
			Map<String, ColumnDescriptor> columnDescriptors,
			LogService logger)
			throws CSVHeaderValidationException {
		Collection<String> conflictingNames = new ArrayList<String>();

		for (ColumnDescriptor column : columnDescriptors.values()) {
			if (column.getNameForDatabase().equals(coreEntityTableName)) {
				conflictingNames.add(column.getName());
			}
		}

		if (conflictingNames.size() > 0) {
			StarDatabaseCSVDataValidationRules.logDuplicateNamesSets(
				ListUtilities.createAndFillList(conflictingNames), logger);
		}
	}
}