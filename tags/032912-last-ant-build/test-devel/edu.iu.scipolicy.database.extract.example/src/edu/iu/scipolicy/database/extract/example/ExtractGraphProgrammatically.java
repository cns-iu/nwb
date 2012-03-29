package edu.iu.scipolicy.database.extract.example;

import edu.iu.cns.database.extract.generic.ExtractGraphWithQueriesFactory;

public class ExtractGraphProgrammatically extends ExtractGraphWithQueriesFactory {

	/*
	 * Programmers can define extraction properties ...
	 * 1) Entirely programmatically
	 * 2) In the .properties file
	 * 3) specifying a template in the .properties file, and modifying that programmatically
	 */
	
	/*
	 * (in a real extraction algorithm these constants 
	 * would come from isiutil or something similar.)
	 */
	private String CONSTANT_TABLE_NAME = "SYS.SYSTABLES";
	private String SOME_CONSTANT_COLUMN_NAME = "TABLEID";
	private String SOME_OTHER_COLUMN_NAME = "TABLENAME";
	
	private String OTHER_TABLE_NAME = "SYS.SYSCONSTRAINTS";
	
	
	
	//example of 1) writing the entire query programmatically
	public String nodeQuery() {
		String nodeQuery = "select " + SOME_CONSTANT_COLUMN_NAME + ", " 
						 + SOME_OTHER_COLUMN_NAME +
		       " from " + CONSTANT_TABLE_NAME;
		return nodeQuery;
		
	}
	
	//example of 3) modifying the query from .properties
	public String edgeQuery(String edgeQueryFromPropertiesFile) {
		String modifiedEdgeQuery = 
			edgeQueryFromPropertiesFile.replace("$FOO", OTHER_TABLE_NAME);
		return modifiedEdgeQuery;
	}
	
	public String idColumn() {
		return SOME_CONSTANT_COLUMN_NAME;
	}
	
	public String sourceColumn() {
		return SOME_CONSTANT_COLUMN_NAME;
	}
	
	public String targetColumn() {
		return SOME_CONSTANT_COLUMN_NAME;
	}
	
	//no directed method or label method (see .properties file)
	//example of 2) defining the extraction properties in the .properties file only
}
