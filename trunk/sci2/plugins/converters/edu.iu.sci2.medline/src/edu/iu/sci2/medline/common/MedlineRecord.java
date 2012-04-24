package edu.iu.sci2.medline.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.cishell.utilities.NumberUtilities;
import org.osgi.service.log.LogService;

import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;

/**
 * This class is for processing the collection of MedlineFields that make up a
 * MEDLINE®/PubMed® Record or publication.
 */
public class MedlineRecord {
	private Map<MedlineField, Object> record;
	private LogService logger;
	
	/**
	 * Generates a record from the lines in the MEDLINE®/PubMed® file associated
	 * with a single record.
	 * 
	 * @param recordString
	 *            The format is
	 *            {@code FIELD - VALUE\nFIELD - VALUE\n\tVALUE\n...}
	 */
	public MedlineRecord(String recordString, LogService logger) {
		Preconditions.checkArgument(recordString != null, "The record string must not be null");
		Preconditions.checkArgument(logger != null, "The logger must not be null.");
		this.logger = logger;
		this.record = new HashMap<MedlineField, Object>();
		BufferedReader reader = new BufferedReader(new StringReader(
				recordString));
		String line;
		Pattern tagValuePattern = Pattern.compile(
				"^(?:(\\S+)\\s*-)?\\s*(.+)\\s*$", Pattern.MULTILINE);
		try {
			String currentTag = null;
			String currentValue = null;
			while ((line = reader.readLine()) != null) {
				if (line.isEmpty()) {
					continue;
				}
				Matcher matcher = tagValuePattern.matcher(line);
				if (matcher.matches()) {
					String foundTag = matcher.group(1);
					String foundValue = matcher.group(2);
					if (foundTag == null) {
						Preconditions
								.checkState(
										currentValue != null,
										"The current value should not be null since it is impossible to have a tag without a value");
						currentValue += " " + foundValue;
					} else {
						if (currentTag != null && currentValue != null) {
							saveTag(currentTag, currentValue);
						}
						currentTag = foundTag;
						currentValue = foundValue;
					}
				} else {
					logError("A parsing error occurred with line '" + line + "'.");
				}
			}
			if (currentTag != null && currentValue != null) {
				saveTag(currentTag, currentValue);
			}
		} catch (IOException e) {
			e.printStackTrace();
			logError("An IOException was caught while attempting to parse a MedlineRecord.\n" + e.getLocalizedMessage());
		}
	}

	private void saveTag(String tag, String value) {
		Preconditions.checkArgument(tag != null, "A Tag without a tag cannot be saved.");
		Preconditions.checkArgument(value != null, "A Tag without a value cannot be saved.");
		add(tag, value);
	}
	
	/**
	 * {@code fields} that can't be matched to a {@linkplain MedlineField} are skipped.
	 */
	private void add(String field, String value) {
		Preconditions.checkArgument(field != null, "The field must not be null.");
		Preconditions.checkArgument(value != null, "The value must not be null.");
		
		MedlineField medlineField = MedlineField
				.getMedlineFieldFromField(field);
		if (medlineField == null) {
			logError("A medline field value could not be found for '" + field + "'.");
			return;
		}

		if (medlineField.getFieldType() != value.getClass()) {
			if (medlineField.getFieldType().isAssignableFrom(value.getClass())) {
				Object checkedValue = medlineField.getFieldType().cast(value);
				this.appendFieldValue(medlineField, checkedValue);
			} else if (medlineField.getFieldType() == Integer.class) {
				Integer parsedValue = NumberUtilities.interpretObjectAsInteger(value);
				if (parsedValue != null) {
					this.appendFieldValue(medlineField, parsedValue);
				} else {
					this.appendFieldValue(medlineField, value);
				}
			} else {
				logError(medlineField + "'s type '" + medlineField.getFieldType() + "' is an unsupported type.\n");
			}
		} else {
			this.appendFieldValue(medlineField, value);
		}
	}

	private void appendFieldValue(MedlineField field, Object value) {
		Preconditions.checkArgument(field != null, "The field must not be null.");
		Preconditions.checkArgument(value != null, "The value must not be null.");
		if (! field.getFieldType().isAssignableFrom(value.getClass())) {
			String message = "'" + value + "''s class '" + value.getClass().getName()
			+ "' did not match the provided MedlineField '" + field + "''s class '"
			+ field.getFieldType().getName() + "' and was skipped.";
			logError(message);
			return;
		}
		

		// String fields can have multivalues.  Otherwise, the value is overwritten.
		if (field.getFieldType().isAssignableFrom(String.class)
				&& this.record.containsKey(field)) {
			String previousValue = (String) this.record.get(field);
			this.record.put(field, previousValue
					+ MedlineField.MEDLINE_MULTI_VALUE_SEPERATOR + value);
		} else {
			this.record.put(field, value);
		}
	}

	/**
	 * Get all the {@link MedlineField} fields for this record.
	 */
	public Set<MedlineField> getFields() {
		return this.record.keySet();
	}

	/**
	 * Get the value for a {@link MedlineField} from this record. <br />
	 * If this is a multivalue field, see {@link MedlineRecord#getValues}.
	 */
	public Object getValue(MedlineField field) {
		return this.record.get(field);
	}

	/**
	 * <p>
	 * Get the individual values for a {@link MedlineField} field.
	 * </p>
	 * <p>
	 * This works for fields like {@link MedlineField#AUTHOR} which could have
	 * several authors separated by a {@code |}. This implies that
	 * {@link MedlineField#getFieldType()} is a {@link String}.
	 * </p>
	 */
	public ImmutableList<String> getValues(MedlineField field) {
		Preconditions.checkArgument(
				String.class.isAssignableFrom(field.getFieldType()),
				"This method only can work for String type MedlineFields.");
		String value = (String) this.getValue(field);
		if (value == null || value.isEmpty()) {
			return ImmutableList.of();
		}
		return ImmutableList.copyOf(Splitter.on(
				MedlineField.MEDLINE_MULTI_VALUE_SEPERATOR).split(value));
	}

	private void logError(String message) {
		this.logger.log(LogService.LOG_ERROR, message);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MedlineRecord [record=");
		for (MedlineField field : this.record.keySet()) {
			builder.append("[");
			builder.append(field);
			builder.append("=");
			builder.append(this.record.get(field));
			builder.append("]");
		}
		builder.append("]");
		return builder.toString();
	}
}
