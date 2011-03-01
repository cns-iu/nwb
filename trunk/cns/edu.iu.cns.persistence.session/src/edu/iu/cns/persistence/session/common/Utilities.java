package edu.iu.cns.persistence.session.common;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.cishell.framework.data.DataProperty;

public class Utilities {
	public static final String METADATA_FILE_NAME = "session";
	public static final String METADATA_FILE_EXTENSION = "metadata";
	public static final String FULL_METADATA_FILE_NAME = String.format(
		"%s.%s", METADATA_FILE_NAME, METADATA_FILE_EXTENSION);

	public static final String DATUM_INDEX_METADATA_KEY = "datumIndex";
	public static final String ZIPPED_FILE_NAME_KEY = "zippedFileName";
	public static final String FILE_NAME_METADATA_KEY = "fileName";
	public static final String TOOL_MIME_TYPE_METADATA_KEY = "toolMimeType";

	public static final Collection<String> DEFAULT_METADATA_KEYS =
		Collections.unmodifiableCollection(Arrays.asList(
			DATUM_INDEX_METADATA_KEY,
			ZIPPED_FILE_NAME_KEY,
			FILE_NAME_METADATA_KEY,
			TOOL_MIME_TYPE_METADATA_KEY));

	public static final Collection<String> METADATA_KEYS_TO_IGNORE_WHEN_READING =
		Collections.unmodifiableCollection(Arrays.asList(
			DATUM_INDEX_METADATA_KEY,
			ZIPPED_FILE_NAME_KEY,
			FILE_NAME_METADATA_KEY,
			TOOL_MIME_TYPE_METADATA_KEY,
			DataProperty.PARENT));

	public static final String FILE_EXTENSION = "file-ext:";
	public static final String FILE = "file:";
}