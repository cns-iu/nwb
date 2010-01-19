package edu.iu.scipolicy.database.extract.example;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.Data;

import edu.iu.cns.database.extract.generic.service.ExtractionService;


public class ExtractGraphUsingServiceAlgorithm implements Algorithm {
	
	
	
		private Data[] data;
		private Dictionary parameters;
		private CIShellContext context;
		
		private ExtractionService extractionService;

		/*
		 * (in a real extraction algorithm these constants 
		 * would come from isiutil or something similar.)
		 */
		private String CONSTANT_TABLE_NAME = "SYS.SYSTABLES";
		private String SOME_CONSTANT_COLUMN_NAME = "TABLEID";
		private String SOME_OTHER_COLUMN_NAME = "TABLENAME";
		
		private String OTHER_TABLE_NAME = "sys.sysconstraints";
		
		public ExtractGraphUsingServiceAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
			this.data = data;
			this.parameters = parameters;
			this.context = context;

			this.extractionService = (ExtractionService) context.getService(ExtractionService.class.getName());
		}

		public Data[] execute() throws AlgorithmExecutionException {

			String nodeQuery = "select " + SOME_CONSTANT_COLUMN_NAME + ", " 
			 + SOME_OTHER_COLUMN_NAME +
			 " from " + CONSTANT_TABLE_NAME;
			
			String edgeQuery = "select * from " + OTHER_TABLE_NAME;
			
			String idColumn = SOME_CONSTANT_COLUMN_NAME;
			
			String sourceColumn = SOME_CONSTANT_COLUMN_NAME;
			
			String targetColumn = SOME_CONSTANT_COLUMN_NAME;
			
			Boolean directed = true;
			
			String label = "Wowzers! A Graph!";
			
			Data[] graphData = extractionService.extractGraph(
					data,
					nodeQuery,
					edgeQuery,
					idColumn,
					sourceColumn,
					targetColumn,
					directed,
					label,
					context);
			
			return graphData;
		}
}
