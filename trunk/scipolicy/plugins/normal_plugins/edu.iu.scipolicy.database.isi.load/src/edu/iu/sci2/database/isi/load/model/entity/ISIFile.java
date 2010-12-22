package edu.iu.sci2.database.isi.load.model.entity;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import org.cishell.utilities.StringUtilities;
import org.cishell.utilities.dictionary.DictionaryEntry;
import org.cishell.utilities.dictionary.DictionaryUtilities;

import edu.iu.cns.database.load.framework.DerbyFieldType;
import edu.iu.cns.database.load.framework.Entity;
import edu.iu.cns.database.load.framework.Schema;
import edu.iu.cns.database.load.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.nwb.shared.isiutil.database.ISI;

public class ISIFile extends Entity<ISIFile> {
	public static final Schema<ISIFile> SCHEMA = new Schema<ISIFile>(
		true,
		ISI.FILE_FORMAT_VERSION_NUMBER, DerbyFieldType.TEXT,
		ISI.FILE_NAME, DerbyFieldType.TEXT,
		ISI.FILE_TYPE, DerbyFieldType.TEXT);
		
	private String fileFormatVersionNumber;
	private String fileName;
	private String fileType;

	public ISIFile(
			DatabaseTableKeyGenerator keyGenerator,
			String fileFormatVersionNumber,
			String fileName,
			String fileType) {
		super(keyGenerator, createAttributes(fileFormatVersionNumber, fileName, fileType));
		this.fileFormatVersionNumber = fileFormatVersionNumber;
		this.fileName = fileName;
		this.fileType = fileType;
	}

	public String getFileFormatVersionNumber() {
		return this.fileFormatVersionNumber;
	}

	public String getFileName() {
		return this.fileName;
	}

	public String getFileType() {
		return this.fileType;
	}
	
	@Override
	public List<Object> createMergeKey() {
		List<Object> mergeKey = new ArrayList<Object>();
		Integer primaryKey = getPrimaryKey();
		addStringOrAlternativeToMergeKey(mergeKey, this.fileFormatVersionNumber, primaryKey);
		addStringOrAlternativeToMergeKey(mergeKey, this.fileName, primaryKey);
		addStringOrAlternativeToMergeKey(mergeKey, this.fileType, primaryKey);

		return mergeKey;
	}

	@Override
	public void merge(ISIFile otherISIFile) {
		this.fileFormatVersionNumber = StringUtilities.simpleMerge(
			this.fileFormatVersionNumber, otherISIFile.getFileFormatVersionNumber());
		this.fileType = StringUtilities.simpleMerge(this.fileType, otherISIFile.getFileType());

		DictionaryUtilities.addIfNotNull(
			getAttributes(),
			new DictionaryEntry<String, Object>(
				ISI.FILE_FORMAT_VERSION_NUMBER, fileFormatVersionNumber),
			new DictionaryEntry<String, Object>(ISI.FILE_NAME, this.fileName),
			new DictionaryEntry<String, Object>(ISI.FILE_TYPE, this.fileType));
	}

	private static Dictionary<String, Object> createAttributes(
			String fileFormatVersionNumber, String fileName, String fileType) {
		Dictionary<String, Object> attributes = new Hashtable<String, Object>();
		DictionaryUtilities.addIfNotNull(
			attributes,
			new DictionaryEntry<String, Object>(
				ISI.FILE_FORMAT_VERSION_NUMBER, fileFormatVersionNumber),
			new DictionaryEntry<String, Object>(ISI.FILE_NAME, fileName),
			new DictionaryEntry<String, Object>(ISI.FILE_TYPE, fileType));

		return attributes;
	}
}