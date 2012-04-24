package edu.iu.sci2.medline.converter.medline_to_medlinetable_converter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.cishell.utilities.UnicodeReader;
import org.osgi.service.log.LogService;

import prefuse.data.Table;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import edu.iu.sci2.medline.common.MedlineField;
import edu.iu.sci2.medline.common.MedlineRecord;
import edu.iu.sci2.medline.common.MedlineRecordParser;

/**
 * A utility class for reading in a MEDLINE®/PubMed® file
 */
public class MedlineFileReader {

	private MedlineFileReader() {
		// Utility Class. Don't instantiate.
	}

	/**
	 * Read a MEDLINE®/PubMed® file from the specified {@code pathname} to a
	 * {@link Table}.
	 * 
	 * @throws MedlineFileReaderException
	 *             if the file cannot be read
	 */
	public static Table read(String pathname, LogService logger) throws MedlineFileReaderException {
		Preconditions.checkNotNull(pathname);
		File file = new File(pathname);
		return read(file, logger);
	}

	/**
	 * 
	 * Read a MEDLINE®/PubMed® file from the specified {@code file} to a
	 * {@link Table}.
	 * 
	 * @throws MedlineFileReaderException
	 *             if the file cannot be read
	 */
	public static Table read(File file, LogService logger) throws MedlineFileReaderException {
		Preconditions.checkNotNull(file);
		try {
			BufferedReader reader = new BufferedReader(new UnicodeReader(
					new FileInputStream(file)));
			ImmutableList<MedlineRecord> records = MedlineRecordParser
					.getAllRecords(reader, logger);
			Table table = makeTable(records, logger);
			return table;
		} catch (FileNotFoundException e) {
			throw new MedlineFileReaderException("The file was not found.", e);
		}
	}

	/**
	 * 
	 * Read {@link MedlineRecord}s into a {@link Table}.
	 */
	private static Table makeTable(ImmutableList<MedlineRecord> records, LogService logger) {
		Table table = new Table();

		for (MedlineRecord record : records) {
			int tableRow = table.addRow();
			for (MedlineField field : record.getFields()) {
				if (!(table.canGet(field.getField(), field.getFieldType()))) {
					try {
						table.addColumn(field.getField(), field.getFieldType());
					} catch (IllegalArgumentException e) {
						if (logger == null) {
							e.printStackTrace();
							System.err.println("Couldn't add a new field for "
								+ field.getField());
						} else {
							logger.log(LogService.LOG_ERROR, "Couldn't add a new field for "
								+ field.getField());
						}
					}
				}
				table.set(tableRow, field.getField(), record.getValue(field));
			}
		}

		return table;
	}

	public static class MedlineFileReaderException extends Exception {
		private static final long serialVersionUID = -3655183578898507050L;

		public MedlineFileReaderException(String message, Throwable t) {
			super(message, t);
		}

		public MedlineFileReaderException(String message) {
			super(message);
		}
	}
}
