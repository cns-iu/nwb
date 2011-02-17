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
//	public static final String METADATA_KEY_FILE_NAME = "fileName";
	public static final String DATUM_INDEX_METADATA_KEY = "datumIndex";
	public static final String FILE_NAME_METADATA_KEY = "fileName";
	public static final String TARGET_MIME_TYPE_METADATA_KEY = "targetMimeType";

	public static final Collection<String> VALID_METADATA_KEYS =
		Collections.unmodifiableCollection(Arrays.asList(
			DATUM_INDEX_METADATA_KEY,
			FILE_NAME_METADATA_KEY,
			TARGET_MIME_TYPE_METADATA_KEY,
			DataProperty.LABEL,
			DataProperty.MODIFIED,
			DataProperty.PARENT,
			DataProperty.SHORT_LABEL,
			DataProperty.TYPE));
}