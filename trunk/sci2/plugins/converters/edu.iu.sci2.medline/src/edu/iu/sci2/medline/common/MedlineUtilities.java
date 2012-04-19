package edu.iu.sci2.medline.common;

import java.util.Arrays;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Preconditions;

/**
 * A class to hold useful methods for operating on MEDLINE®/PubMed® objects.
 */
public class MedlineUtilities {

	private MedlineUtilities() {
		// Utility Class
	}

	public static final List<MedlineField> SUPPORTED_FULL_NAMES = Arrays
			.asList(MedlineField.FULL_AUTHOR, MedlineField.FULL_EDITOR_NAME,
					MedlineField.FULL_INVESTIGATOR_NAME);
	public static final List<MedlineField> SUPPORTED_NAMES = Arrays.asList(
			MedlineField.AUTHOR, MedlineField.EDITOR_NAME,
			MedlineField.INVESTIGATOR_NAME);

	/**
	 * Get a {@link ParsedName} from a {@code String} representation of that
	 * name given the {@link MedlineField} from which that string came.
	 * 
	 * @throws NameParsingException
	 *             if the {@code name} cannot be parsed
	 */
	public static ParsedName parseName(MedlineField field, String name)
			throws NameParsingException {
		if (SUPPORTED_FULL_NAMES.contains(field)) {
			/*
			 * See http://www.nlm.nih.gov/bsd/mms/medlineelements.html#fau for
			 * details.
			 */
			String lastNamePattern = "(.+),";
			// Anything without digits
			String foreNamePattern = "(?: ([\\D]*))?";
			// Anything with digits
			String suffixPattern = "(?: (\\S*[^\\p{javaUpperCase}]\\S*))?";
			String pattern = lastNamePattern + foreNamePattern + suffixPattern;
			Matcher matcher = Pattern.compile(pattern).matcher(name);
			if (matcher.matches()) {
				MatchResult result = matcher.toMatchResult();
				String lastName = result.group(1);
				String foreName = result.group(2) == null ? "" : result
						.group(2);
				String suffix = result.group(3) == null ? "" : result
						.group(3);
				String initials = "";
				return new ParsedName(lastName, foreName, suffix, initials);
			}
			throw new NameParsingException("The name '" + name
					+ "' could not be parsed");
		} else if (SUPPORTED_NAMES.contains(field)) {
			/*
			 * See http://www.nlm.nih.gov/bsd/mms/medlineelements.html#au for
			 * details.
			 */
			String lastNamePattern = "(.+)";
			// At most two uppercase characters
			String initialsPattern = "(?: (\\p{javaUpperCase}\\p{javaUpperCase}?))";
			// A suffix has atleast one not upper case
			String suffixPattern = "(?: (\\S*[^\\p{javaUpperCase}]\\S*))?";
			String pattern = lastNamePattern + initialsPattern + suffixPattern;
			Matcher matcher = Pattern.compile(pattern).matcher(name);
			if (matcher.matches()) {
				MatchResult result = matcher.toMatchResult();
				String foreName = "";
				String lastName = result.group(1);
				String initials = result.group(2) == null ? "" : result
						.group(2);
				String suffix = result.group(3) == null ? "" : result
						.group(3);
				return new ParsedName(lastName, foreName, suffix, initials);
			}
			throw new NameParsingException("The name '" + name
					+ "' could not be parsed");
		} else {
			throw new NameParsingException(field + " is an unsupported field.");
		}
	}

	/**
	 * A class which hold the different values that can be parsed from a MEDLINE®/PubMed® style name.
	 *
	 */
	public static class ParsedName {
		private String lastName;
		private String foreName;
		private String suffix;
		private String initials;

		public ParsedName(String lastName, String foreName, String suffix,
				String initials) {
			Preconditions.checkNotNull(lastName);
			Preconditions.checkNotNull(foreName);
			Preconditions.checkNotNull(suffix);
			Preconditions.checkNotNull(initials);
			this.lastName = lastName;
			this.foreName = foreName;
			this.suffix = suffix;
			this.initials = initials;
		}

		public String getLastName() {
			return this.lastName;
		}

		public String getForeName() {
			return this.foreName;
		}

		public String getSuffix() {
			return this.suffix;
		}

		public String getInitials() {
			return this.initials;
		}

		@Override
		public String toString() {
			return "ParsedName [lastName=" + this.lastName + ", foreName="
					+ this.foreName + ", suffix=" + this.suffix + ", initials="
					+ this.initials + "]";
		}
	}

	public static class NameParsingException extends Exception {
		private static final long serialVersionUID = 7248885784936796628L;

		public NameParsingException(String message) {
			super(message);
		}
	}
}
