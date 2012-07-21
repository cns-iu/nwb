package edu.iu.sci2.visualization.scimaps.parameters;

import org.osgi.service.metatype.AttributeDefinition;

/**
 * Nice names for two special String values that may be returned by
 * {@link AttributeDefinition#validate(java.lang.String)}.
 */
public enum ValidationResult {
	/**
	 * No validation present.
	 * 
	 * @see AttributeDefinition#validate(java.lang.String)
	 */
	NONE(null),
	/**
	 * No problems detected.
	 * 
	 * @see AttributeDefinition#validate(java.lang.String)
	 */
	SUCCESS("");
	
	private final String token;

	private ValidationResult(String token) {
		this.token = token;
	}

	/**
	 * The special String value representing this validation result as defined by
	 * {@link AttributeDefinition#validate(java.lang.String)}.
	 */
	public String token() {
		return token;
	}
	
	/**
	 * 
	 * 
	 * @return Whether this result signals validation failure.
	 */
	// TODO Phrasing?
	public static boolean isFailure(String result) {
		return result != NONE.token() && result != SUCCESS.token();
	}
}