package edu.iu.scipolicy.loader.isi.db.model.entity;

import java.util.Dictionary;
import java.util.Hashtable;

import edu.iu.cns.database.loader.framework.Entity;
import edu.iu.cns.database.loader.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.scipolicy.loader.isi.db.ISIDatabase;

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

	public ISIFile merge(ISIFile otherISIFile) {
		// TODO: Implement this.
		return otherISIFile;
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