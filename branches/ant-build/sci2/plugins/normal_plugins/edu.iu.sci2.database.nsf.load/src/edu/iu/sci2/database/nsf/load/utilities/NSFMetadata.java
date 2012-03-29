package edu.iu.sci2.database.nsf.load.utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import au.com.bytecode.opencsv.CSVReader;
import edu.iu.sci2.utilities.nsf.NsfCsvFieldNames;

public class NSFMetadata {
	private String fileName;
	private String md5Checksum;
	private String fileType;
	private Map<String, Integer> columnNameToColumnIndex;
	private Map<String, Integer> unknownColumnNameToColumnIndex;
	private int rowCount = 0;

	public NSFMetadata(String[] nsfFileColumnNames, File nsfFile, CSVReader csvReader) {
		this.fileName = nsfFile.getName();
	    this.fileType = getFileTypeInformation(nsfFile); 
		this.md5Checksum = computeMD5Checksum(nsfFile);
		this.columnNameToColumnIndex = mapNSFColumnNameToColumnIndex(nsfFileColumnNames);
		this.unknownColumnNameToColumnIndex =
			mapUnknowmColumnNameToColumnIndex(columnNameToColumnIndex);

		try {
			//(Didn't have a significant effect on load-time in testing)
			this.rowCount = csvReader.readAll().size();
		} catch (IOException e) {
			//will show no progress for this phase.
		}
	}

	// TODO: this method is slow. need to find a better alternative.
	private static String getFileTypeInformation(File nsfCsvFile) {
		return "NSF File";
	}

	/*
	 * TODO: I assume there's a practical reason why we reimplemented this ourselves, but go ahead
	 * and make it a utility in org.cishell.utilities.FileUtilities?
	 */
	private static String computeMD5Checksum(File file) {
		InputStream inputStream = null;
		String output = "";

		try {
			byte[] buffer = new byte[8192];
			int readByte = 0;
			MessageDigest digest = MessageDigest.getInstance("MD5");
			inputStream = new FileInputStream(file);
			
			while ((readByte = inputStream.read(buffer)) > 0) {
				digest.update(buffer, 0, readByte);
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
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				throw new RuntimeException("Unable to close input stream for MD5 calculation", e);
			}
		}

		return output;
	}

	private static Map<String, Integer> mapNSFColumnNameToColumnIndex(String[] columnNames) {
		Map<String, Integer> columnNameToColumnIndex = new HashMap<String, Integer>();
		for (int columnIndex = 0; columnIndex < columnNames.length; columnIndex++) {
			String trimmedCurrentColumnName = columnNames[columnIndex].trim();

			/*
			 * We are testing for the membership first to avoid duplicate columns like
			 * "Award Number" re-assigned with a different column index. On analyzing
			 * a corpus of nsf data it was found that "Award Number" in the first 
			 * occurrence is more stable than the later occurrence.
			 */
			if (!columnNameToColumnIndex.containsKey(trimmedCurrentColumnName)) {
				columnNameToColumnIndex.put(trimmedCurrentColumnName, columnIndex);
			}
		}

		return columnNameToColumnIndex;
	}

	private static Map<String, Integer> mapUnknowmColumnNameToColumnIndex(
			Map<String, Integer> nsfFieldsColumnNameToColumnIndex) {
		Map<String, Integer> unknownColumnNameToColumnIndex = new HashMap<String, Integer>();
		List<String> nsfCSVFields = NsfCsvFieldNames.CSV.getNsfCsvFields();

		for (Entry<String, Integer> columnEntry : nsfFieldsColumnNameToColumnIndex.entrySet()) {
			
			if (!nsfCSVFields.contains(columnEntry.getKey())) {
				unknownColumnNameToColumnIndex.put(columnEntry.getKey(), columnEntry.getValue());
			}
		}
		
		return unknownColumnNameToColumnIndex;
	}
	
	public String getFileName() {
		return this.fileName;
	}


	public String getMD5Checksum() {
		return this.md5Checksum;
	}


	public String getFileType() {
		return this.fileType;
	}


	public Map<String, Integer> getColumnNameToColumnIndex() {
		return this.columnNameToColumnIndex;
	}


	public Map<String, Integer> getUnknownColumnNameToColumnIndex() {
		return this.unknownColumnNameToColumnIndex;
	}

	public int getRowCount() {
		return this.rowCount;
	}
}
