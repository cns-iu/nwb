package edu.iu.scipolicy.loader.nsf.db.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.swing.JFileChooser;

import edu.iu.cns.database.loader.framework.utilities.DatabaseTableKeyGenerator;
import edu.iu.scipolicy.loader.nsf.db.model.entity.NSFFile;

public class NSFFileParser {
	public static NSFFile parseNSFFile(
			DatabaseTableKeyGenerator nSFFileKeyGenerator, File nsfCsvFile) {

		String fileName = nsfCsvFile.getName();
	    String fileType = getFileTypeInformation(nsfCsvFile); 
		String md5Checksum = computeMD5Checksum(nsfCsvFile);

		return new NSFFile(nSFFileKeyGenerator,
						   fileName,
						   fileType,
						   md5Checksum);
	}

	//TODO: this method is slow. need to find a better alternative.
	private static String getFileTypeInformation(File nsfCsvFile) {
		JFileChooser jFileChooser = new JFileChooser();
		return jFileChooser.getTypeDescription(nsfCsvFile);
	}

	private static String computeMD5Checksum(File file){
		
		InputStream is = null;
		byte[] buffer = new byte[8192];
		int read = 0;
		String output = "";
		try {
			MessageDigest digest = MessageDigest.getInstance("MD5");
			is = new FileInputStream(file);
			
			while ((read = is.read(buffer)) > 0) {
				digest.update(buffer, 0, read);
			}
			byte[] md5sum = digest.digest();
			BigInteger bigInt = new BigInteger(1, md5sum);
			output = bigInt.toString(16);

		} catch (IOException e) {
			throw new RuntimeException("Unable to process file for MD5", e);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				throw new RuntimeException(
						"Unable to close input stream for MD5 calculation", e);
			}
		}
		return output;
	}
}