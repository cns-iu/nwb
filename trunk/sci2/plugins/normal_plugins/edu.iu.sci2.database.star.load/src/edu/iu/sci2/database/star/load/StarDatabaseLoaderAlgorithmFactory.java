package edu.iu.sci2.database.star.load;

import java.io.IOException;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.cishell.service.database.DatabaseService;
import org.cishell.utilities.StringUtilities;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.log.LogService;
import org.osgi.service.metatype.ObjectClassDefinition;

import edu.iu.cns.database.load.framework.exception.InvalidDerbyFieldTypeException;
import edu.iu.sci2.database.star.common.parameter.ParameterDescriptors;
import edu.iu.sci2.database.star.common.utility.StarDatabaseDataValidator;
import edu.iu.sci2.database.star.common.utility.csv.validator.exception.CSVHeaderValidationException;
import edu.iu.sci2.database.star.load.parameter.ParameterFactory;

public class StarDatabaseLoaderAlgorithmFactory implements AlgorithmFactory, ParameterMutator {
	private LogService logger;

	protected void activate(ComponentContext componentContext) {
		this.logger = (LogService)componentContext.locateService("LOG");
	}

	public ObjectClassDefinition mutateParameters(Data[] data, ObjectClassDefinition parameters) {
		StarDatabaseDataValidator.validateData(data[0], this.logger);

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
    		DatabaseService databaseService =
    			(DatabaseService)ciShellContext.getService(DatabaseService.class.getName());

        	return new StarDatabaseLoaderAlgorithm(
        		data[0],
        		coreEntityDisplayName,
        		this.logger,
        		databaseService,
        		parameters);
    	} catch (CSVHeaderValidationException e) {
    		throw new RuntimeException(e.getMessage(), e);
    	} catch (InvalidDerbyFieldTypeException e) {
    		throw new RuntimeException(e.getMessage(), e);
    	} catch (IOException e) {
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
}