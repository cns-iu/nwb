package edu.iu.scipolicy.loader.nsf.db.model.entity;

import java.util.Dictionary;
import java.util.Hashtable;

import org.cishell.utilities.StringUtilities;

import edu.iu.cns.database.loader.framework.DerbyFieldType;
import edu.iu.cns.database.loader.framework.Entity;
import edu.iu.cns.database.loader.framework.Schema;
import edu.iu.cns.database.loader.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.scipolicy.loader.nsf.db.NSFDatabase;

public class NSFFile extends Entity<NSFFile> implements Comparable<NSFFile> {
	public static final Schema<NSFFile> SCHEMA = new Schema<NSFFile>(
			true,
			NSFDatabase.FILE_NAME, DerbyFieldType.TEXT,
			NSFDatabase.FILE_TYPE, DerbyFieldType.TEXT,
			NSFDatabase.FILE_MD5_CHECKSUM, DerbyFieldType.TEXT);
	
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
		attributes.put(NSFDatabase.FILE_NAME, fileName);
		attributes.put(NSFDatabase.FILE_TYPE, fileType);
		attributes.put(NSFDatabase.FILE_MD5_CHECKSUM, fileMD5Checksum);

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
		return (StringUtilities.validAndEquivalentIgnoreCase(
									this.fileName,
									otherItem.getFileName())
				&& StringUtilities.validAndEquivalentIgnoreCase(
							this.fileType,
							otherItem.getFileType())
				&& StringUtilities.validAndEquivalentIgnoreCase(
							this.fileMD5Checksum,
							otherItem.getFileMD5Checksum())
		);
	}

	public int compareTo(NSFFile o) {
		// TODO Auto-generated method stub
		return 0;
	}

}