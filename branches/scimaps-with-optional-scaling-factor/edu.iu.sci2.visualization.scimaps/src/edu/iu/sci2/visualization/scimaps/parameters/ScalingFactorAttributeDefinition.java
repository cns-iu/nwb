package edu.iu.sci2.visualization.scimaps.parameters;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Locale;

import org.cishell.reference.service.metatype.BasicAttributeDefinition;
import org.cishell.reference.service.metatype.BasicObjectClassDefinition;
import org.cishell.utilities.MutateParameterUtilities;
import org.osgi.service.metatype.AttributeDefinition;
import org.osgi.service.metatype.ObjectClassDefinition;

import com.google.common.collect.Range;
import com.google.common.collect.Ranges;

/**
 * An attribute definition with additional validation for its value as either the special String
 * {@value #AUTO_TOKEN} or a String representing a valid float, as judged by the NumberFormat for
 * the default locale and limited to {@link #VALID_RANGE}.
 */
public final class ScalingFactorAttributeDefinition extends BasicAttributeDefinition {
	public static final String AUTO_TOKEN = "auto";
	public static final Range<Float> VALID_RANGE = Ranges.greaterThan(0.0f);
	
	ScalingFactorAttributeDefinition(String id, String name, String description) {
		/* Nominally STRING so that AUTO_TOKEN is an acceptable value, but every other value will
		 * need to pass validation as an interpretable float. */
		super(id, name, description, AttributeDefinition.STRING, AUTO_TOKEN);
	}

	@Override
	public String validate(String value) {
		assert (value != null);
		
		// Fail if parent not satisfied
		String parentValidation = super.validate(value);
		if (ValidationResult.isFailure(parentValidation)) {
			return parentValidation;
		}		
		
		try {
			interpret(value);
			
			return ValidationResult.SUCCESS.token();
		} catch (ScalingFactorInterpretationException e) {
			return e.getMessage();
		}
	}
	
	/**
	 * Produces a copy of {@code oldOCD} where any existing attribute definition with ID
	 * {@code targetID} is replaced with an instance of ScalingFactorAttributeDefinition.
	 */
	public static BasicObjectClassDefinition mutateParameters(ObjectClassDefinition oldOCD,
			String targetID) {
		BasicObjectClassDefinition newOCD = MutateParameterUtilities.createNewParameters(oldOCD);
		
		/* XXX The original scaling factor attribute definition must be
		 * ObjectClassDefinition.REQUIRED. */
		for (AttributeDefinition oldAD : oldOCD.getAttributeDefinitions(
				ObjectClassDefinition.REQUIRED)) {
			String id = oldAD.getID();
	
			if (targetID.equals(id)) {
				newOCD.addAttributeDefinition(ObjectClassDefinition.REQUIRED,
						new ScalingFactorAttributeDefinition(
								oldAD.getID(), oldAD.getName(), oldAD.getDescription()));
			} else {
				newOCD.addAttributeDefinition(ObjectClassDefinition.REQUIRED, oldAD);
			}
		}
		
		return newOCD;
	}

	/**
	 * @param s
	 *            Text indicating a scaling strategy (e.g. {@value #AUTO_TOKEN} or "1.5")
	 * @throws ScalingFactorInterpretationException
	 *             if the value cannot be interpreted as any of the recognized strategies
	 */
	public static ScalingStrategy interpret(String s) throws ScalingFactorInterpretationException {
		if (isRequestForAutomaticScaling(s)) {
			return ScalingStrategies.automatic();
		} else {
			return ScalingStrategies.explicit(interpretAsExplicitScalingFactor(s));
		}
	}
	
	private static boolean isRequestForAutomaticScaling(String s) {
		return ScalingFactorAttributeDefinition.AUTO_TOKEN.equals(s.trim());
	}
	
	private static float interpretAsExplicitScalingFactor(String s)
			throws ScalingFactorInterpretationException {
		final Locale locale = Locale.getDefault();
		
		try {
			// Try a strict parse
			NumberFormat numberFormat = NumberFormat.getInstance(locale);
			ParsePosition position = new ParsePosition(0);
			Number number = numberFormat.parse(s, position);
			if (position.getIndex() != s.length()) {
				throw new ParseException("Failed to parse entire string: " + s,	position.getIndex());
			}
			if (number == null) {
				throw new ScalingFactorInterpretationException("No number found.");
			}
			
			float asFloat = number.floatValue();
			
			// Check range
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
	
	/**
	 * An exception signaling failure in the interpretation of a String as a valid scaling factor.
	 */
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