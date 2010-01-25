package edu.iu.scipolicy.database.nsf.load.model.entity;

import java.util.Dictionary;
import java.util.Hashtable;

import org.cishell.utilities.StringUtilities;

import edu.iu.cns.database.loader.framework.DerbyFieldType;
import edu.iu.cns.database.loader.framework.Entity;
import edu.iu.cns.database.loader.framework.Schema;
import edu.iu.cns.database.loader.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.scipolicy.utilities.nsf.NSF_Database_FieldNames;

public class NSFFile extends Entity<NSFFile> implements Comparable<NSFFile> {
	public static final Schema<NSFFile> SCHEMA = new Schema<NSFFile>(
			true,
			NSF_Database_FieldNames.FILE_NAME, DerbyFieldType.TEXT,
			NSF_Database_FieldNames.FILE_TYPE, DerbyFieldType.TEXT,
			NSF_Database_FieldNames.FILE_MD5_CHECKSUM, DerbyFieldType.TEXT);
	
	private String fileName;
	private String fileType;
	private String fileMD5Checksum;

	public NSFFile(
			DatabaseTableKeyGenerator keyGenerator,
			String fileName,
			String fileType,
			String fileMD5Checksum) {
		super(keyGenerator, createAttributes(fileName, 
											 fileType, 
											 fileMD5Checksum));
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

	private static Dictionary<String, Comparable<?>> createAttributes(String fileName,
															   String fileType,
															   String fileMD5Checksum) {
		Dictionary<String, Comparable<?>> attributes = new Hashtable<String, Comparable<?>>();
		attributes.put(NSF_Database_FieldNames.FILE_NAME, fileName);
		attributes.put(NSF_Database_FieldNames.FILE_TYPE, fileType);
		attributes.put(NSF_Database_FieldNames.FILE_MD5_CHECKSUM, fileMD5Checksum);

		return attributes;
	}

	@Override
	public void merge(NSFFile otherItem) {
		this.fileName = StringUtilities.simpleMerge(this.fileName, 
													otherItem.getFileName());
		
		this.fileType = StringUtilities.simpleMerge(this.fileType, 
													otherItem.getFileType());
		
		this.fileMD5Checksum = StringUtilities.simpleMerge(this.fileMD5Checksum, 
														   otherItem.getFileMD5Checksum());
	}

	@Override
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
	}

	public int compareTo(NSFFile o) {
		// TODO Auto-generated method stub
		return 0;
	}

}