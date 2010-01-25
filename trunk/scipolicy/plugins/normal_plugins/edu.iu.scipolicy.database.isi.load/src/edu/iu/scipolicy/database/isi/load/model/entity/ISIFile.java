package edu.iu.scipolicy.database.isi.load.model.entity;

import java.util.Dictionary;
import java.util.Hashtable;

import org.cishell.utilities.StringUtilities;
import org.cishell.utilities.dictionary.DictionaryEntry;
import org.cishell.utilities.dictionary.DictionaryUtilities;

import edu.iu.cns.database.load.framework.DerbyFieldType;
import edu.iu.cns.database.load.framework.Entity;
import edu.iu.cns.database.load.framework.Schema;
import edu.iu.cns.database.load.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.nwb.shared.isiutil.database.ISI;

public class ISIFile extends Entity<ISIFile> implements Comparable<ISIFile> {
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

	public int compareTo(ISIFile otherISIFile) {
		if (shouldMerge(otherISIFile)) {
			return 0;
		}

		return this.fileName.compareTo(otherISIFile.getFileName());
	}

	public boolean shouldMerge(ISIFile otherISIFile) {
		return StringUtilities.areValidAndEqualIgnoreCase(
			this.fileName, otherISIFile.getFileName());
	}

	public void merge(ISIFile otherISIFile) {
		this.fileFormatVersionNumber = StringUtilities.simpleMerge(
			this.fileFormatVersionNumber, otherISIFile.getFileFormatVersionNumber());
		this.fileType = StringUtilities.simpleMerge(this.fileType, otherISIFile.getFileType());
	}

	private static Dictionary<String, Comparable<?>> createAttributes(
			String fileFormatVersionNumber, String fileName, String fileType) {
		Dictionary<String, Comparable<?>> attributes = new Hashtable<String, Comparable<?>>();
		DictionaryUtilities.addIfNotNull(
			attributes,
			new DictionaryEntry<String, Comparable<?>>(
				ISI.FILE_FORMAT_VERSION_NUMBER, fileFormatVersionNumber),
			new DictionaryEntry<String, Comparable<?>>(ISI.FILE_NAME, fileName),
			new DictionaryEntry<String, Comparable<?>>(ISI.FILE_TYPE, fileType));
		/*attributes.put(ISI.FILE_FORMAT_VERSION_NUMBER, fileFormatVersionNumber);
		attributes.put(ISI.FILE_NAME, fileName);
		attributes.put(ISI.FILE_TYPE, fileType);*/

		return attributes;
	}
}