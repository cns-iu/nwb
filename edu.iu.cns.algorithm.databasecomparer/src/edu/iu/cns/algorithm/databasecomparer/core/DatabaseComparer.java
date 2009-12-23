package edu.iu.cns.algorithm.databasecomparer.core;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.cishell.service.database.Database;
import org.cishell.utilities.StringUtilities;

public class DatabaseComparer {
	
		
		/*
		 * Determine whether two databases are 'equal', and report their differences if not equal.
		 * Two databases are 'equal' if they have the same table names, 
		 * same schemas for those tables, and identical rows in those tables.
		 * 
		 * if full error reporting is false, return on the first difference found between databases.
		 * (faster)
		 * 
		 * if full error reporting is true, continue comparing databases when differences are found,
		 * and return the full list of differences at the end.
		 * (better reporting)
		 */
		public static ComparisonResult compare(Database db1, Database db2) 
			throws DatabaseComparisonException {
			try {
			//(will be side-effected to log any differences found)
			DifferenceLog differenceLog = new DifferenceLog();
			
			//given two databases...
			Connection db1Connection = db1.getConnection();
			Connection db2Connection = db2.getConnection();
			
			//COMPARE TABLE NAMES

			//get the table names from both databases
			List<String> db1TableNames = getTableNames(db1Connection);
			List<String> db2TableNames = getTableNames(db2Connection);
			
			//check to see if the database's table names are identical
			List<String> sharedTableNames = compareTableNames(db1TableNames, db2TableNames, differenceLog);
			if (differenceLog.differencesHaveBeenFound()) {
				return new ComparisonResult(false, differenceLog);
			}
			
	//COMPARE TABLE SCHEMAS
			
			
				
			List<String> tablesWithSharedSchemas = new ArrayList<String>();
			//for each pair of tables with identical names...
			for (String sharedTableName : sharedTableNames) {
				ResultSet db1Columns = db1Connection.getMetaData().getColumns(null, "APP", sharedTableName, null);
				ResultSet db2Columns = db2Connection.getMetaData().getColumns(null, "APP", sharedTableName, null);
				
				boolean allColumnsAreIdentical = compareColumns(sharedTableName, db1Columns, db2Columns, differenceLog);
				if (differenceLog.differencesHaveBeenFound()) {
					return new ComparisonResult(false, differenceLog);
				}
				if (allColumnsAreIdentical) {
					tablesWithSharedSchemas.add(sharedTableName);
				}
			}
			

			//COMPARE TABLE ROWS
			
			//for tables whose schemas are identical...
				//compare the rows in those tables to each other (in order)
					//if any two rows differ...
						//report the difference and move on to the next row
		
			
			//RETURN RESULTS
			
			if (differenceLog.differencesHaveBeenFound()) {
				return new ComparisonResult(false, differenceLog);
			} else {
				return new ComparisonResult(true, differenceLog);
			}
			//if no differences were reported...
				//return that both databases are equal
			//else...
				//return that the databases are not equal, along with how they differ.
			} catch (Exception e) {
				throw new DatabaseComparisonException(e);
			}
		}
		
		private static final int SCHEMA_NAME_INDEX = 2;
		private static final int TABLE_NAME_INDEX = 3;

		
		//TODO: NON-SYSTEM
		private static List<String> getTableNames(Connection dbConnection) throws DatabaseComparisonException {
			try {
			   DatabaseMetaData dbMetadata = dbConnection.getMetaData();
			   ResultSet allTableNames = dbMetadata.getTables(null, "APP", null, null);
			   
			   List<String> nonSystemTableNames = new ArrayList<String>();
			   while (allTableNames.next()) {
				   String schemaName = allTableNames.getString(SCHEMA_NAME_INDEX);
				   if (isNonSystemSchemaName(schemaName)) {
					   String tableName = allTableNames.getString(TABLE_NAME_INDEX);
					   nonSystemTableNames.add(tableName);
				   }
			   }
			   
			   return nonSystemTableNames;
			} catch (Exception e) {
				throw new DatabaseComparisonException(e);
			}
		}
		
		private static List<String> compareTableNames(List names1, List names2, DifferenceLog differenceLog) {
			if (equalContents(names1, names2)) {
				return names1;
			} else {
				List<String> inNames1ButNotNames2 = asymmetricDifference(names1, names2);
				for (Iterator it = inNames1ButNotNames2.iterator(); it.hasNext();) {
					String unsharedName = (String) it.next();
					String differenceReport = "Database 1 has a table named '"
						+ unsharedName + ", " 
						+ "while Database 2 does not.";
					differenceLog.addDifference(differenceReport);
				}
				
				List<String> inNames2ButNotNames1 = asymmetricDifference(names1, names2);
				for (Iterator it = inNames2ButNotNames1.iterator(); it.hasNext();) {
					String unsharedName = (String) it.next();
					String differenceReport = "Database 2 has a table named '"
						+ unsharedName + ", " 
						+ "while Database 1 does not.";
					differenceLog.addDifference(differenceReport);
				}
				
				return intersection(names1, names2);
			}
		}
		
		private static boolean compareColumns(String tableName, ResultSet table1Columns, ResultSet table2Columns, DifferenceLog differenceLog)
			throws SQLException {
			boolean allColumnsAreIdentical = true;
			//compare the schemas of those tables
			//if the schemas differ...
				//report how they differ
				//move on to the next pair of tables with identical names
			//keep track of which tables have identical schemas
			boolean moreInTable1 = table1Columns.next();
			boolean moreInTable2 = table2Columns.next();
			while (true)  {
				
				if (! moreInTable1 && ! moreInTable2) {
					break;
				}
					//compare columns by name
				
					String column1Name = table1Columns.getString("COLUMN_NAME");
					String column2Name = table1Columns.getString("COLUMN_NAME");
					
					if (! column1Name.equals(column2Name)) {
						String differenceReport = 
							"in '" + tableName + ", the first table's column is named '" + column1Name +
							"while the second table's column is named " + column2Name;
						differenceLog.addDifference(differenceReport);
						
						allColumnsAreIdentical = false;
					}
					
					//compare columns by type
					
					//TODO: Maybe use actual data type?
					String column1Type = table1Columns.getString("TYPE_NAME");
					String column2Type = table1Columns.getString("TYPE_NAME");
					
					if (! column1Type.equals(column2Type)) {
						String differenceReport = 
							"in '" + tableName + ", the first table's column has type '" + column1Type +
							"while the second table's column has type " + column2Type;
						differenceLog.addDifference(differenceReport);
						allColumnsAreIdentical = false;
					}
					
					moreInTable1 = table1Columns.next();
					moreInTable2 = table2Columns.next();
			}
			
			if (moreInTable1) {
				String longerTable;
				List<String> extraColumnNames = new ArrayList<String>();
				if (! table1Columns.isAfterLast()) {
					longerTable = "Table 1";
					//table 1 has more columns
					while (table2Columns.next()) {
						extraColumnNames.add(table1Columns.getString("COLUMN_NAME"));
					}
				} else {
					//table 2 has more columns
					longerTable = "Table 2";
					while (table2Columns.next()) {
						extraColumnNames.add(table2Columns.getString("COLUMN_NAME"));
					}
				}
				
				String listOfExtraColumns = StringUtilities.implodeStringArray((String[]) extraColumnNames.toArray(), ",");
				String differenceReport = longerTable + " has the following extra columns: " 
					+ listOfExtraColumns;
				differenceLog.addDifference(differenceReport);
				allColumnsAreIdentical = false;
			}
			
			return allColumnsAreIdentical;
		}
		
		//****---UTILITIES----****
	
		private static final String NONSYSTEM_SCHEMA_NAME = "APP";
		
		private static boolean isNonSystemSchemaName(String tableName) {
			return tableName.indexOf(NONSYSTEM_SCHEMA_NAME) != -1;
		}
		
		private static List copy(List l) {
			List lCopy = new ArrayList();
			Collections.copy(l, lCopy);
			return lCopy;
		}
		
		private static List intersection(List l1, List l2) {
			Set intersection = new HashSet(l1);
			intersection.retainAll(l2);
			return new ArrayList(intersection);
		}
		
		private static List asymmetricDifference(List l1, List l2) {
			List l1Copy = copy(l1);
			l1Copy.removeAll(l2);
			List l1MinusL2 = l1Copy;
			return l1MinusL2;
		}
		
		
		private static boolean equalContents(List l1, List l2) {
			return l1.containsAll(l2) && l1.containsAll(l2);
		}
		
}
