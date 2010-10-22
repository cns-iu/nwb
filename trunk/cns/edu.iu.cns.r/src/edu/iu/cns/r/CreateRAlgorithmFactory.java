package edu.iu.cns.r;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.AllParametersMutatedOutException;
import org.cishell.framework.algorithm.ParameterMutator;
import org.cishell.framework.data.Data;
import org.cishell.reference.service.metatype.BasicAttributeDefinition;
import org.cishell.reference.service.metatype.BasicObjectClassDefinition;
import org.cishell.utilities.MutateParameterUtilities;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.log.LogService;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

import edu.iu.cns.r.utility.RHomeFinder;

public class CreateRAlgorithmFactory implements AlgorithmFactory, ParameterMutator {
	public static final String R_HOME_DIRECTORY_ID = "rExecutableDirectory";

	private LogService logger;

	protected void activate(ComponentContext componentContext) {
		this.logger = (LogService) componentContext.locateService("LOG");
	}

    public Algorithm createAlgorithm(
    		Data[] data, Dictionary<String, Object> parameters, CIShellContext ciShellContext) {
    	String rHome = getRHome(parameters);

        return new CreateRAlgorithm(rHome, this.logger);
    }

	public ObjectClassDefinition mutateParameters(
			Data[] data, ObjectClassDefinition oldParameters) {
		String rHomeFromPath = RHomeFinder.findRHome();

		BasicObjectClassDefinition newParameters =
			MutateParameterUtilities.createNewParameters(oldParameters);
		processAttributeDefinitions(
			oldParameters, newParameters, ObjectClassDefinition.REQUIRED, rHomeFromPath);
		processAttributeDefinitions(
			oldParameters, newParameters, ObjectClassDefinition.OPTIONAL, rHomeFromPath);

		if (newParameters.getAttributeDefinitions(ObjectClassDefinition.ALL).length == 0) {
			throw new AllParametersMutatedOutException();
		} else {
			return newParameters;
		}
	}

	private void processAttributeDefinitions(
			ObjectClassDefinition oldParameters,
			BasicObjectClassDefinition newParameters,
			int attributeDefinitionType,
			String rHomeFromPath) {
		if (rHomeFromPath != null) {
			rHomeFromPath = "directory:" + rHomeFromPath;
		} else {
			rHomeFromPath = "directory:";
		}

		for (AttributeDefinition oldAttributeDefinition :
				oldParameters.getAttributeDefinitions(attributeDefinitionType)) {
			AttributeDefinition newAttributeDefinition = oldAttributeDefinition;

			if (R_HOME_DIRECTORY_ID.equals(oldAttributeDefinition.getID())) {
				/* Commented out until we figure out how to handle if the user has multiple
				 * R in stallations.
				 */
//				if (rHomeFromPath != null) {
//					continue;
//				}

				newAttributeDefinition = new BasicAttributeDefinition(
    					oldAttributeDefinition.getID(),
    					oldAttributeDefinition.getName(),
    					oldAttributeDefinition.getDescription(),
    					oldAttributeDefinition.getType(),
    					/*"directory:"*/rHomeFromPath) {
    				@Override
    				public String validate(String value) {
    					String preValidation = super.validate(value);

    					if (preValidation != null) {
    						return preValidation;
    					} else {
    						if (!RHomeFinder.isValidRHome(value)) {
    							String format =
    								"%s is not a valid R home.  " +
    								"(The R executable could not be found.)" +
    								"Please choose a different directory as your R home.";
    							String errorMessage = String.format(format, value);
    							CreateRAlgorithmFactory.this.logger.log(
    								LogService.LOG_ERROR, errorMessage);

    							return errorMessage;
    						} else {
    							return null;
    						}
    					}
    				}
    			};
			}

			newParameters.addAttributeDefinition(attributeDefinitionType, newAttributeDefinition);
		}
	}

	private static String getRHome(Dictionary<String, Object> parameters) {
		String rHome = (String) parameters.get(R_HOME_DIRECTORY_ID);

		if (rHome != null) {
			return rHome;
		} else {
			return RHomeFinder.findRHome();
		}
	}
}