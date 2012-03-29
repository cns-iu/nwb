package edu.iu.sci2.testutilities;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.utilities.FileUtilities;
import org.osgi.framework.BundleException;

import prefuse.data.Table;
import edu.iu.nwb.converter.prefusecsv.reader.PrefuseCsvReader;

public class TestUtilities {
	public static int HEADER_COLUMN_NAME_INDEX = 0;
	public static int HEADER_COLUMN_TYPE_INDEX = 1;
	public static final String CSV_MIME_TYPE = "file:text/csv";

	/**
	 * Open the {@link File} with the default given by the {@link Desktop}.
	 * 
	 */
	public static void openFile(File givenFile) throws IOException {

		if (Desktop.isDesktopSupported()) {
			String filename = givenFile.getAbsolutePath();
			File file = File.createTempFile(
					"copy of " + FilenameUtils.getBaseName(filename), "."
							+ FilenameUtils.getExtension(filename));
			FileUtils.copyFile(givenFile, file);
			Desktop desktop = Desktop.getDesktop();
			
			desktop.open(file);
		}

	}

	public static Data[] createTestTableData(Class<?> clazz, String testDataPath)
			throws Exception {
		File file = FileUtilities
				.safeLoadFileFromClasspath(clazz, testDataPath);

		return createTestTableData(file);
	}

	public static Data[] createTestTableData(File file) throws Exception {
		Data fileData = new BasicData(file, CSV_MIME_TYPE);

		return convertFileDataToTableData(fileData);
	}

	public static Data[] convertFileDataToTableData(Data fileData)
			throws Exception {
		PrefuseCsvReader csvReader = new PrefuseCsvReader(
				new Data[] { fileData });

		return csvReader.execute();
	}

	/**
	 * Returns a CIShellContext suitable for a testing environment. It includes
	 * a logging and database service.
	 */
	public static TestContext createFakeCIShellContext() throws BundleException {
		return new TestContext();
	}

	/**
	 * Create a database and fill it with the provided parameters, and wrap and
	 * return it in a nicely-packaged Data array. header is an array of
	 * array-of-Strings-that-should-be-2-in-length, where the first value is an
	 * database table column name, and the second value is the type. contents is
	 * an array of array-of-Strings representing a table of values to be placed
	 * in the database table. The length of header is the number of columns in
	 * the table, so it should match the length of each element (or each row) in
	 * contents.
	 **/
	// TODO: Maybe throw exception if header and contents lengths aren't
	// correct.
	/*
	 * public static Data[] createAndFillTestDatabase(String tableName,
	 * String[][] header, String primaryKey, String[][] contents,
	 * DatabaseService databaseService) throws Exception { Connection
	 * databaseConnection = null;
	 * 
	 * try { DataSourceWithID database = databaseService.createDatabase();
	 * databaseConnection = database.getConnection();
	 * 
	 * createEmptyDatabaseTable(tableName, database.getID(), header, primaryKey,
	 * databaseConnection);
	 * 
	 * fillDatabaseTable (tableName, database.getID(), header, contents,
	 * databaseConnection);
	 * 
	 * Data databaseData = new BasicData(database,
	 * database.getClass().getName());
	 * 
	 * return new Data[] { databaseData }; } catch (Exception e) { throw e; } }
	 */

	public static Table createEmptyPrefuseTable(String[] columnNames,
			Class[] columnTypes) {
		Table table = new Table();

		for (int ii = 0; ii < columnNames.length; ii++)
			table.addColumn(columnNames[ii], columnTypes[ii]);

		return table;
	}

	public static Table createPrefuseTableAndFillItWithTestRecordData(
			String labelKey, String startDateKey, String endDateKey,
			String amountKey) {
		String[] columnNames = new String[] { labelKey, startDateKey,
				endDateKey, amountKey };

		Class[] columnTypes = new Class[] { String.class, Date.class,
				Date.class, Integer.class };

		Table table = createEmptyPrefuseTable(columnNames, columnTypes);

		table.addRow();
		table.set(0, labelKey, "Micah");
		table.set(0, startDateKey, new Date(1985, 6, 3));
		table.set(0, endDateKey, new Date(2009, 6, 4));
		table.set(0, amountKey, new Integer(1000));
		// ----
		table.addRow();
		table.set(1, labelKey, "Patrick");
		table.set(1, startDateKey, new Date(1984, 2, 15));
		table.set(1, endDateKey, new Date(1985, 2, 15));
		table.set(1, amountKey, new Integer(100));
		// ----
		table.addRow();
		table.set(2, labelKey, "Elisha");
		table.set(2, startDateKey, new Date(1985, 10, 19));
		table.set(2, endDateKey, new Date(1994, 9, 20));
		table.set(2, amountKey, new Integer(500));

		return table;
	}

	private static void createEmptyDatabaseTable(String tableName,
			int databaseID, String[][] header, String primaryKey,
			Connection databaseConnection) throws Exception {
		try {
			StringBuffer sql = new StringBuffer();

			sql.append("CREATE TABLE " + tableName + databaseID + " (");

			// Fill in the table schema.
			for (String[] headerItem : header) {
				sql.append(headerItem[HEADER_COLUMN_NAME_INDEX] + " "
						+ headerItem[HEADER_COLUMN_TYPE_INDEX]);
				sql.append(",");
			}

			sql.append("PRIMARY KEY (" + primaryKey + "))");

			Statement createAwardTableStatement = databaseConnection
					.createStatement();
			createAwardTableStatement.execute(sql.toString());
		} catch (SQLException e) {
			throw new Exception(
					"Error occurred while interacting with database", e);
		}
	}

	private static void fillDatabaseTable(String tableName, int databaseID,
			String[][] header, String[][] contents,
			Connection databaseConnection) throws Exception {
		StringBuffer columnNamesSQL = new StringBuffer();
		StringBuffer valuesSQL = new StringBuffer();

		// For the column names SQL.
		for (int ii = 0; ii < header.length; ii++) {
			String[] headerItem = header[ii];

			columnNamesSQL.append(headerItem[HEADER_COLUMN_NAME_INDEX]);

			// Separate the column names by comma.
			if (ii < (header.length - 1))
				columnNamesSQL.append(",");
		}

		// Form the values SQL.
		for (int ii = 0; ii < header.length; ii++) {
			valuesSQL.append("?");

			// Separate the values by comma.
			if (ii < (header.length - 1))
				valuesSQL.append(",");
		}

		// The final SQL will look like:
		// "INSERT INTO table# (column1 ...) VALUES (value1 ...)"

		String sqlTemplate = "INSERT INTO " + tableName + databaseID + " ("
				+ columnNamesSQL.toString() + ") VALUES ("
				+ valuesSQL.toString() + ")";

		PreparedStatement insertIntoAward = databaseConnection
				.prepareStatement(sqlTemplate);

		// Go through all rows in the table, placing the values into the
		// prepared
		// statement, and adding the prepared statement to the batch of
		// statements
		// to be executed afterwards.
		// Prepared statements use a 1-based index scheme for its placeholders,
		// instead of a 0-based scheme, hence the ((ii + 1).
		for (String[] row : contents) {
			for (int ii = 0; ii < row.length; ii++)
				insertIntoAward.setObject((ii + 1), row[ii]);

			insertIntoAward.addBatch();
		}

		insertIntoAward.executeBatch();
	}

	// TODO: This and parseDate should go in DateUtilities.
	private static final DateFormat[] ACCEPTED_DATE_FORMATS = {
			DateFormat.getDateInstance(DateFormat.FULL),
			new SimpleDateFormat("dd/MM/yy"),
			new SimpleDateFormat("dd/MM/yyyy"),
			DateFormat.getDateInstance(DateFormat.SHORT),
			DateFormat.getDateInstance(DateFormat.MEDIUM),
			DateFormat.getDateInstance(DateFormat.LONG), };

	private static java.sql.Date parseDate(String dateString) throws Exception {
		for (DateFormat format : ACCEPTED_DATE_FORMATS) {
			try {
				format.setLenient(false);
				java.util.Date date = format.parse(dateString);

				if (date.getYear() < 1900)
					date.setYear(date.getYear() + 1900);

				java.sql.Date dateForSQL = new java.sql.Date(date.getTime());

				return dateForSQL;
			} catch (ParseException e) {
				continue;
			}
		}

		String exceptionMessage = "Could not parse the field " + "'"
				+ dateString + "'" + " as a date. Aborting the algorithm.";

		throw new Exception(exceptionMessage);
	}
}