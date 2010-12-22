package edu.iu.sci2.database.star.common.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.cishell.framework.data.Data;
import org.cishell.utilities.StringUtilities;
import org.cishell.utilities.UnicodeReader;

import au.com.bytecode.opencsv.CSVReader;

public class CSVReaderUtilities {
	public static String[] getHeader(Data data) throws IOException {
		CSVReader reader = createCSVReader((File)data.getData(), true);
		String[] header = StringUtilities.simpleCleanStrings(reader.readNext());
		reader.close();

		if (header != null) {
			return StringUtilities.simpleCleanStrings(header);
		} else {
			return null;
		}
	}

	public static CSVReader createCSVReader(File file, boolean includeHeader) throws IOException {
		CSVReader reader = new CSVReader(
			new UnicodeReader(new FileInputStream(file)));

		if (allRowsHaveTheSameColumnCount(reader)) {
			reader.close();

			if (includeHeader) {
				return new CSVReader(
					new UnicodeReader(new FileInputStream(file)));
			} else {
				return new CSVReader(
					new UnicodeReader(new FileInputStream(file)));
			}
		} else {
			reader.close();

			if (includeHeader) {
				return new CSVReader(
					new UnicodeReader(new FileInputStream(file)), '\t');
			} else {
				reader = new CSVReader(
						new UnicodeReader(new FileInputStream(file)), '\t');
				reader.readNext();

				return reader;
			}
		}
	}

	public static boolean allRowsHaveTheSameColumnCount(CSVReader reader) throws IOException {
		final String[] firstRow = reader.readNext();

		if (firstRow == null) {
			return true;
		}

		int columnCount = firstRow.length;

		String[] currentRow;

		while ((currentRow = reader.readNext()) != null) {
			if (currentRow.length != columnCount) {
				return false;
			}
		}

		return true;
	}

	public static int rowCount(File file, boolean includeHeader) throws IOException {
		return createCSVReader(file, includeHeader).readAll().size();
//		CSVReader reader = createCSVReader(file, includeHeader);
//		int rowCount = 0;
//
//		while (reader.readNext() != null) {
//			rowCount++;
//		}
//
//		reader.close();
//
//		return rowCount;
	}
}