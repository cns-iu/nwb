package edu.iu.nwb.converter.postscript.gs.util.fileformat;

import java.io.File;
import java.io.IOException;
import java.util.Dictionary;

import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;

// TODO Move to CIShell?
public abstract class FileFormat {
	/**
	 * A human-readable String describing this FileFormat.
	 */
	public abstract String getPrettyName();
	
	/**
	 * The MIME type of this FileFormat, e.g. "file:image/png".
	 */
	public abstract String getMimeType();
	
	/**
	 * The filename extension of this FileFormat, e.g. "png".
	 */
	public abstract String getFileExtension();
	
	/**
	 * The {@link DataProperty#TYPE} associated with this FileFormat,
	 * e.g. {@link DataProperty#RASTER_IMAGE_TYPE}.
	 */
	public abstract String getDataType();
	
		
	/**
	 * The file extension with a dot prepended.
	 */
	public String filenameSuffix() {
		return "." + getFileExtension();
	}
	
	/**
	 * The file extension with "file-ext:" prepended.  Suitable for use as a Data "format" in the
	 * sense of {@link Data#getFormat()} or {@link BasicData#BasicData(Object, String)}.
	 */
	public String extensionDataFormat() {
		return "file-ext:" + getFileExtension();
	}
	
	/**
	 * A simple String suitable for use as a {@link DataProperty#LABEL} for a piece of Data in this
	 * FileFormat.
	 */
	public String suggestedDataLabel() {
		return String.format("as %s", getPrettyName());
	}
	
	/**
	 * Wraps {@code file} as a new {@link BasicData} using the provided {@code parent} and this
	 * FileFormat's "file-ext:" data format, data type, and data label.
	 */
	public Data wrapAsValidatedData(File file, Data parent) {
		Data outputFileData = new BasicData(file, extensionDataFormat());
		
		Dictionary<String, Object> metadata = outputFileData.getMetadata();
		metadata.put(DataProperty.PARENT, parent);
		metadata.put(DataProperty.TYPE, getDataType());
		metadata.put(DataProperty.LABEL, suggestedDataLabel());		
	
		return outputFileData;
	}
	/**
	 * Alias for {@link #wrapAsValidatedData(File, Data)}.
	 */
	public Data wrapAsHandledData(File file, Data parent) {
		return wrapAsValidatedData(file, parent);
	}	
	
	/**
	 * Wraps {@code file} as a new {@link BasicData} using the provided {@code parent} and
	 * this FileFormat's "file:" MIME type data format, data type, and data label.
	 */
	public Data wrapAsLoadedData(File file, Data parent) {
		Data outputFileData = new BasicData(file, getMimeType());
		
		Dictionary<String, Object> metadata = outputFileData.getMetadata();
		metadata.put(DataProperty.PARENT, parent);
		metadata.put(DataProperty.TYPE, getDataType());
		metadata.put(DataProperty.LABEL, suggestedDataLabel());		
	
		return outputFileData;
	}
	/**
	 * Alias for {@link #wrapAsLoadedData(File, Data)}.
	 */
	public Data wrapAsSavedData(File file, Data parent) {
		return wrapAsLoadedData(file, parent);
	}

	/**
	 * {@link File#createTempFile(String, String)} + {@link File#deleteOnExit()}.
	 */
	public File createTemporaryFileWithPrefix(String prefix) throws IOException {
		File file = File.createTempFile(prefix, filenameSuffix());
		file.deleteOnExit();		
		return file;
	}
}