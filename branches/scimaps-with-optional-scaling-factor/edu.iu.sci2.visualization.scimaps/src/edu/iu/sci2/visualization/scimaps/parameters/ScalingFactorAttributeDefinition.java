package edu.iu.sci2.visualization.scimaps.parameters;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import org.cishell.reference.service.metatype.BasicAttributeDefinition;
import org.cishell.reference.service.metatype.BasicObjectClassDefinition;
import org.cishell.utilities.MutateParameterUtilities;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

import com.google.common.base.Optional;
import com.google.common.collect.Range;
import com.google.common.collect.Ranges;

import edu.iu.sci2.visualization.scimaps.journals.JournalsMapAlgorithmFactory;

public final class ScalingFactorAttributeDefinition extends BasicAttributeDefinition {
	public static final String AUTO_TOKEN = "auto";
	public static final Range<Float> VALID_RANGE = Ranges.greaterThan(0.0f);
	
	public ScalingFactorAttributeDefinition(String id, String name, String description,
			String defaultValue) {
		/* Nominally STRING so that AUTO_TOKEN is an acceptable value, but really FLOAT. */
		super(id, name, description, AttributeDefinition.STRING, defaultValue);
	}

	@Override
	public String validate(String value) {
		// Fail if parent not satisfied
		String parentValidation = super.validate(value);
		if (ValidationResult.isFailure(parentValidation)) {
			return parentValidation;
		}
		
		assert (value != null) : "TODO Is this even possible?";
		
		// Succeed if value can be interpreted as a request for automatic scaling
		if (isRequestForAuto(value)) {
			return ValidationResult.SUCCESS.token();
		}			
		
		try {
			interpretAsExplicitScalingFactor(value);
			
			return ValidationResult.SUCCESS.token();
		} catch (ScalingFactorInterpretationException e) {
			return e.getMessage(); // A string explaining the problem signals validation failure
		}
	}
	
	public static BasicObjectClassDefinition mutateParameters(ObjectClassDefinition oldOCD) {
		BasicObjectClassDefinition newOCD = MutateParameterUtilities.createNewParameters(oldOCD);
		
		for (AttributeDefinition oldAD : oldOCD.getAttributeDefinitions(
				ObjectClassDefinition.ALL)) {
			String id = oldAD.getID();
	
			if (JournalsMapAlgorithmFactory.SCALING_FACTOR_ID.equals(id)) {
				AttributeDefinition newAD = new ScalingFactorAttributeDefinition(id,
						oldAD.getName(), oldAD.getDescription(),
						AUTO_TOKEN);
	
				newOCD.addAttributeDefinition(
					ObjectClassDefinition.REQUIRED, newAD);
			} else {
				// TODO Not good that this makes everything else ObjectClassDefinition.REQUIRED..
				newOCD.addAttributeDefinition(
						ObjectClassDefinition.REQUIRED, oldAD);
			}
		}
		
		return newOCD;
	}

	/**
	 * TODO javadoc
	 * 
	 * @param s
	 * @return
	 * @throws ScalingFactorInterpretationException
	 */
	public static Optional<Float> asOptional(String s) throws ScalingFactorInterpretationException {
		if (isRequestForAuto(s)) {
			return Optional.absent();
		} else {
			return Optional.of(interpretAsExplicitScalingFactor(s));
		}
	}
	
	private static boolean isRequestForAuto(String s) {
		return ScalingFactorAttributeDefinition.AUTO_TOKEN.equals(s.trim());
	}
	
	private static float interpretAsExplicitScalingFactor(String s)
			throws ScalingFactorInterpretationException {
		final Locale locale = Locale.getDefault();
		
		try {
			float asFloat = NumberFormat.getInstance(locale).parse(s).floatValue();
			
			if (ScalingFactorAttributeDefinition.VALID_RANGE.contains(asFloat)) {
				return asFloat;
			} else {
				throw new ScalingFactorInterpretationException(
						String.format(
								"Scaling factor must be in the range %s.",
								ScalingFactorAttributeDefinition.VALID_RANGE));
			}
		} catch (ParseException e) {
			throw new ScalingFactorInterpretationException(
					String.format(
							"Cannot interpret the value %s as a number using the default locale (%s).",
							s, locale),
					e);
		}
	}
	
	public static class ScalingFactorInterpretationException extends Exception {
		private static final long serialVersionUID = 6935912990246353042L;

		private ScalingFactorInterpretationException(String message, Throwable cause) {
			super(message, cause);
		}

		private ScalingFactorInterpretationException(String message) {
			super(message);
		}
	}
}