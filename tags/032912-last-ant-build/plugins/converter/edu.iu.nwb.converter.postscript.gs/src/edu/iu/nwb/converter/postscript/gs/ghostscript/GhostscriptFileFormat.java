package edu.iu.nwb.converter.postscript.gs.ghostscript;

import java.io.File;
import java.io.IOException;

import com.google.common.base.Objects;

import edu.iu.nwb.converter.postscript.gs.util.fileformat.BasicFileFormat;
import edu.iu.nwb.converter.postscript.gs.util.fileformat.FileFormat;

/**
 * A {@link FileFormat} together with a {@link GhostscriptDevice} that can write to that format.
 */
public class GhostscriptFileFormat extends FileFormat {
	private final FileFormat fileFormat;
	private final GhostscriptDevice ghostscriptDevice;
	
	public static GhostscriptFileFormat definedBy(
			FileFormat fileFormat, GhostscriptDevice ghostscriptDevice) {
		return new GhostscriptFileFormat(fileFormat, ghostscriptDevice);
	}
	private GhostscriptFileFormat(FileFormat fileFormat, GhostscriptDevice ghostscriptDevice) {
		this.fileFormat = fileFormat;
		this.ghostscriptDevice = ghostscriptDevice;
	}
	
	
	/**
	 * Converts {@code inputFile} to a File in this format.
	 */
	public File convert(File inputFile) throws GhostscriptException {
		File targetFile;
		try {
			targetFile = createTemporaryFileWithPrefix("Ghostscript-");
		} catch (IOException e) {
			throw new GhostscriptException("Could not create output file.", e);
		}
		
		ghostscriptDevice.convert(inputFile, targetFile);
		
		return targetFile;
	}
	
	public FileFormat getFileFormat() {
		return fileFormat;
	}
	
	public GhostscriptDevice getGhostscriptDevice() {
		return ghostscriptDevice;
	}
	
	/**
	 * @see BasicFileFormat#getPrettyName()
	 */
	@Override
	public String getPrettyName() {
		return fileFormat.getPrettyName();
	}
	
	/**
	 * @see BasicFileFormat#getMimeType()
	 */
	@Override
	public String getMimeType() {
		return fileFormat.getMimeType();
	}
	
	/**
	 * @see BasicFileFormat#getFileExtension()
	 */
	@Override
	public String getFileExtension() {
		return fileFormat.getFileExtension();
	}
	
	/**
	 * @see BasicFileFormat#getDataType()
	 */
	@Override
	public String getDataType() {
		return fileFormat.getDataType();
	}
	
	
	@Override
	public boolean equals(Object thatObject) {
		if (this == thatObject) {
			return true;
		}
		
		if (!(thatObject instanceof GhostscriptFileFormat)) {
			return false;
		}
		GhostscriptFileFormat that = (GhostscriptFileFormat) thatObject;
		
		return Objects.equal(this.fileFormat,
							 that.fileFormat)
			&& Objects.equal(this.ghostscriptDevice,
							 that.ghostscriptDevice);
	}
	
	@Override
	public int hashCode() {
		return Objects.hashCode(this.fileFormat,
								this.ghostscriptDevice);
	}
	
	@Override
	public String toString() {
		return Objects.toStringHelper(this)
				.add("fileFormat",
					  fileFormat)
				.add("ghostscriptDevice",
					  ghostscriptDevice)
				.toString();
	}
}
