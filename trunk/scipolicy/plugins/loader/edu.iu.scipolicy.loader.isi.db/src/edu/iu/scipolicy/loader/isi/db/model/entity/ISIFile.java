package edu.iu.scipolicy.loader.isi.db.model.entity;

import java.util.Dictionary;
import java.util.Hashtable;


import edu.iu.cns.database.loader.framework.Entity;
import edu.iu.cns.database.loader.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.scipolicy.loader.isi.db.ISIDatabase;

public class ISIFile extends Entity {
	private String fileName;
	private String fileType;
	private String fileFormatVersionNumber;

	public ISIFile(
			DatabaseTableKeyGenerator keyGenerator,
			String fileName,
			String fileType,
			String fileFormatVersionNumber) {
		super(keyGenerator);
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

	public Dictionary<String, Object> createAttributes() {
		Dictionary<String, Object> attributes = new Hashtable<String, Object>();
		attributes.put(ISIDatabase.FILE_NAME, this.fileName);
		attributes.put(ISIDatabase.FILE_TYPE, this.fileType);
		attributes.put(ISIDatabase.FILE_FORMAT_VERSION_NUMBER, this.fileFormatVersionNumber);

		return attributes;
	}
}