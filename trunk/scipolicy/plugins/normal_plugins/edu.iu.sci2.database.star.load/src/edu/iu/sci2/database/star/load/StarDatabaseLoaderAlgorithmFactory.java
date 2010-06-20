package edu.iu.sci2.database.star.load;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Map;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.cishell.service.database.DatabaseService;
import org.cishell.utilities.ListUtilities;
import org.cishell.utilities.StringUtilities;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.log.LogService;
import org.osgi.service.metatype.ObjectClassDefinition;

import edu.iu.cns.database.load.framework.exception.InvalidDerbyFieldTypeException;
import edu.iu.sci2.database.star.load.parameter.ColumnDescriptor;
import edu.iu.sci2.database.star.load.parameter.ColumnDescriptorFactory;
import edu.iu.sci2.database.star.load.parameter.ParameterDescriptors;
import edu.iu.sci2.database.star.load.parameter.ParameterFactory;
import edu.iu.sci2.database.star.load.utility.validator.CSVDataValidator;
import edu.iu.sci2.database.star.load.utility.validator.exception.CSVHeaderValidationException;

public class StarDatabaseLoaderAlgorithmFactory implements AlgorithmFactory, ParameterMutator {
	private LogService logger;

	protected void activate(ComponentContext componentContext) {
		this.logger = (LogService)componentContext.locateService("LOG");
	}

	public ObjectClassDefinition mutateParameters(Data[] data, ObjectClassDefinition parameters) {
		/*
	 	 * In order to translate the data from CSV to database, we must normalize column names for
	 	 *  the database.  One potential issue with this is normalized-name conflicts.  We can't
	 	 *  reasonably handle such issues automatically, so we simply validate and fail if
	 	 *  there are any.
	 	 */
		validateData(data[0]);

		/*
		 * We require a lot of instruction from the user on the exact format and intention of their
		 *  data, so we must construct user-facing parameters as such.
		 */
		return ParameterFactory.createParameters(data[0], parameters);
	}

    public Algorithm createAlgorithm(
    		Data[] data, Dictionary<String, Object> parameters, CIShellContext ciShellContext) {
    	try {
    		String coreEntityDisplayName = determineCoreEntityDisplayName(parameters);
    		String coreEntityTableName = constructCoreEntityTableName(coreEntityDisplayName);
    		Map<String, ColumnDescriptor> columnDescriptors =
    			ColumnDescriptorFactory.createColumnDescriptors(data[0], parameters);
    		validateCoreEntityTableName(coreEntityTableName, columnDescriptors);
    		DatabaseService databaseService =
    			(DatabaseService)ciShellContext.getService(DatabaseService.class.getName());

        	return new StarDatabaseLoaderAlgorithm(
        		data[0],
        		coreEntityDisplayName,
        		coreEntityTableName,
        		columnDescriptors,
        		this.logger,
        		databaseService);
    	} catch (CSVHeaderValidationException e) {
    		throw new RuntimeException(e.getMessage(), e);
    	} catch (InvalidDerbyFieldTypeException e) {
    		throw new RuntimeException(e.getMessage(), e);
    	} catch (IOException e) {
    		throw new RuntimeException(e.getMessage(), e);
    	}
    }

    private void validateData(Data data) {
    	try {
			CSVDataValidator validator =
				new CSVDataValidator(data, new StarDatabaseCSVDataValidationRules(this.logger));
			validator.validateHeader();
		}
		catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (CSVHeaderValidationException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
    }

    /* determineCoreEntityDisplayName attempts to get the human-readable core entity from
     *  the user-inputted parameters.  If the user entered what is effectively an empty name,
     *  the name is defaulted to a sensible default (i.e. "CORE"), and the user is alerted as such
     *  via a log message.
     */
	private String determineCoreEntityDisplayName(Dictionary<String, Object> parameters) {
		String coreEntityName = (String)parameters.get(
			ParameterDescriptors.CoreEntityName.CORE_ENTITY_NAME_ID);

		if (!StringUtilities.isNull_Empty_OrWhitespace(coreEntityName)) {
			return coreEntityName;
		} else {
			String logMessage =
				"The core entity name you provided is not valid.  " +
				"Defaulting to " +
				ParameterDescriptors.CoreEntityName.DEFAULT_CORE_ENTITY_NAME_VALUE +
				".";
			this.logger.log(LogService.LOG_WARNING, logMessage);

			return ParameterDescriptors.CoreEntityName.DEFAULT_CORE_ENTITY_NAME_VALUE;
		}
	}

	private String constructCoreEntityTableName(String coreEntityDisplayName) {
		return StarDatabaseCSVDataValidationRules.normalizeName(coreEntityDisplayName);
	}

	/*
	 * We were able to validate the input CSV's column names at mutateParameters time, but we
	 *  cannot validate the core entity table name until the user entered it (naturally).
	 * We have to make sure the user-entered core entity table name won't conflict with the
	 *  (normalized) input-CSV's column names.
	 */
	@SuppressWarnings("unchecked")	// Raw Collection (logDuplicateNamesSets)
	private void validateCoreEntityTableName(
			String coreEntityTableName, Map<String, ColumnDescriptor> columnDescriptors)
			throws CSVHeaderValidationException {
		Collection<String> conflictingNames = new ArrayList<String>();

		for (ColumnDescriptor column : columnDescriptors.values()) {
			if (column.getNameForDatabase().equals(coreEntityTableName)) {
				conflictingNames.add(column.getName());
			}
		}

		if (conflictingNames.size() > 0) {
			StarDatabaseCSVDataValidationRules.logDuplicateNamesSets(
				ListUtilities.createAndFillList(conflictingNames), this.logger);
		}
	}
}