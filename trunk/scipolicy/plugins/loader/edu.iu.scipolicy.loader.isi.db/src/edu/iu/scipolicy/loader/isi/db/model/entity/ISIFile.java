package edu.iu.scipolicy.loader.isi.db.model.entity;

import java.util.Dictionary;
import java.util.Hashtable;

import org.cishell.utilities.StringUtilities;

import edu.iu.cns.database.loader.framework.Entity;
import edu.iu.cns.database.loader.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.nwb.shared.isiutil.database.ISIDatabase;

public class ISIFile extends Entity<ISIFile> {
	private String fileName;
	private String fileType;
	private String fileFormatVersionNumber;

	public ISIFile(
			DatabaseTableKeyGenerator keyGenerator,
			String fileName,
			String fileType,
			String fileFormatVersionNumber) {
		super(keyGenerator, createAttributes(fileName, fileType, fileFormatVersionNumber));
		this.fileName = fileName;
		this.fileType = fileType;
		this.fileFormatVersionNumber = fileFormatVersionNumber;
	}
	
	public String getFileName() {
		return this.fileName;
	}

	public String getFileType() {
		return this.fileType;
	}

	public String getFileFormatVersionNumber() {
		return this.fileFormatVersionNumber;
	}

	public boolean shouldMerge(ISIFile otherISIFile) {
		return StringUtilities.validAndEquivalentIgnoreCase(
			this.fileName, otherISIFile.getFileName());
	}

	public void merge(ISIFile otherISIFile) {
		this.fileType = StringUtilities.simpleMerge(this.fileType, otherISIFile.getFileType());
		this.fileFormatVersionNumber = StringUtilities.simpleMerge(
			this.fileFormatVersionNumber, otherISIFile.getFileFormatVersionNumber());
	}

	private static Dictionary<String, Comparable<?>> createAttributes(
			String fileName, String fileType, String fileFormatVersionNumber) {
		Dictionary<String, Comparable<?>> attributes = new Hashtable<String, Comparable<?>>();
		attributes.put(ISIDatabase.FILE_NAME, fileName);
		attributes.put(ISIDatabase.FILE_TYPE, fileType);
		attributes.put(ISIDatabase.FILE_FORMAT_VERSION_NUMBER, fileFormatVersionNumber);

		return attributes;
	}
}