package edu.iu.nwb.converter.postscript.gs.util.fileformat;

import org.cishell.framework.data.DataProperty;

import com.google.common.base.Objects;

/**
 * Immutable value class encapsulating a {@link FileFormat}'s human-readable name, MIME type,
 * filename extension, and corresponding {@link DataProperty#TYPE}.
 */
// TODO Move to CIShell?
public class BasicFileFormat extends FileFormat {
	private final String prettyName;
	private final String mimeType;
	private final String fileExtension;
	private final String dataType;
	
	/**
	 * @param prettyName		See {@link DataProperty#LABEL}.
	 * @param mimeType			E.g. "file:image/png".
	 * @param fileExtension		E.g. "png".
	 * @param dataType			See {@link DataProperty#TYPE}.
	 * @return	A new instance of BasicFileFormat.
	 */
	public static BasicFileFormat definedBy(
			String prettyName, String mimeType, String fileExtension, String dataType) {
		return new BasicFileFormat(prettyName, mimeType, fileExtension, dataType);
	}
	private BasicFileFormat(
			String prettyName, String mimeType, String fileExtension, String dataType) {
		this.prettyName = prettyName;
		this.mimeType = mimeType;
		this.fileExtension = fileExtension;
		this.dataType = dataType;
	}
	

	@Override
	public String getPrettyName() {
		return prettyName;
	}
	
	@Override
	public String getMimeType() {
		return mimeType;
	}

	@Override
	public String getFileExtension() {
		return fileExtension;
	}
	
	@Override
	public String getDataType() {
		return dataType;
	}

	
	
	@Override
	public boolean equals(Object thatObject) {
		if (this == thatObject) {
			return true;
		}
		
		if (!(thatObject instanceof BasicFileFormat)) {
			return false;
		}		
		BasicFileFormat that = (BasicFileFormat) thatObject;
		
		return Objects.equal(this.prettyName,
				 			 that.prettyName)
			&& Objects.equal(this.mimeType,
							 that.mimeType)
			&& Objects.equal(this.fileExtension,
							 that.fileExtension)
			&& Objects.equal(this.dataType,
							 that.dataType);
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(prettyName,
								mimeType,
								fileExtension,
								dataType);
	}
	
	@Override
	public String toString() {
		return Objects.toStringHelper(this)
				.add("prettyName",
					  prettyName)
				.add("mimeType",
					  mimeType)
				.add("fileExtension",
					  fileExtension)
				.add("dataType",
					  dataType)
				.toString();
	}
}
