package edu.iu.sci2.database.nsf.load.model.entity;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import org.cishell.utilities.StringUtilities;

import edu.iu.cns.database.load.framework.DerbyFieldType;
import edu.iu.cns.database.load.framework.Entity;
import edu.iu.cns.database.load.framework.Schema;
import edu.iu.cns.database.load.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.sci2.utilities.nsf.NsfDatabaseFieldNames;

public class NSFFile extends Entity<NSFFile> {
	public static final Schema<NSFFile> SCHEMA = new Schema<NSFFile>(
			true,
			NsfDatabaseFieldNames.FILE_NAME, DerbyFieldType.TEXT,
			NsfDatabaseFieldNames.FILE_TYPE, DerbyFieldType.TEXT,
			NsfDatabaseFieldNames.FILE_MD5_CHECKSUM, DerbyFieldType.TEXT
			);
	
	private String fileName;
	private String fileType;
	private String fileMD5Checksum;

	public NSFFile(
			DatabaseTableKeyGenerator keyGenerator,
			String fileName,
			String fileType,
			String fileMD5Checksum) {
		super(keyGenerator, createAttributes(fileName, fileType, fileMD5Checksum));
		this.fileName = fileName;
		this.fileType = fileType;
		this.fileMD5Checksum = fileMD5Checksum;
	}
	
	public String getFileName() {
		return this.fileName;
	}

	public String getFileType() {
		return this.fileType;
	}

	public String getFileMD5Checksum() {
		return this.fileMD5Checksum;
	}

	/*@Override
	public boolean shouldMerge(NSFFile otherItem) {
		return (StringUtilities.areValidAndEqualIgnoreCase(
									this.fileName,
									otherItem.getFileName())
				&& StringUtilities.areValidAndEqualIgnoreCase(
							this.fileType,
							otherItem.getFileType())
				&& StringUtilities.areValidAndEqualIgnoreCase(
							this.fileMD5Checksum,
							otherItem.getFileMD5Checksum())
		);
	}*/

	@Override
	public Object createMergeKey() {
		List<Object> mergeKey = new ArrayList<Object>();
		Integer primaryKey = getPrimaryKey();
		addCaseInsensitiveStringOrAlternativeToMergeKey(mergeKey, this.fileName, primaryKey);
		addCaseInsensitiveStringOrAlternativeToMergeKey(mergeKey, this.fileType, primaryKey);
		addCaseInsensitiveStringOrAlternativeToMergeKey(
			mergeKey, this.fileMD5Checksum, primaryKey);

		return mergeKey;
	}

	@Override
	public void merge(NSFFile otherItem) {
		this.fileName = StringUtilities.simpleMerge(this.fileName, otherItem.getFileName());
		this.fileType = StringUtilities.simpleMerge(this.fileType, otherItem.getFileType());
		this.fileMD5Checksum =
			StringUtilities.simpleMerge(this.fileMD5Checksum, otherItem.getFileMD5Checksum());
	}

	private static Dictionary<String, Object> createAttributes(
			String fileName,
			String fileType,
			String fileMD5Checksum) {
		Dictionary<String, Object> attributes = new Hashtable<String, Object>();
		attributes.put(NsfDatabaseFieldNames.FILE_NAME, fileName);
		attributes.put(NsfDatabaseFieldNames.FILE_TYPE, fileType);
		attributes.put(NsfDatabaseFieldNames.FILE_MD5_CHECKSUM, fileMD5Checksum);

		return attributes;
	}
}