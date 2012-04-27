package edu.iu.sci2.reader.googlescholar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.utilities.DataFactory;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import edu.iu.sci2.reader.googlescholar.citationtable.CitationTableExtractor;
import edu.iu.sci2.reader.googlescholar.search.GoogleScholarReaderHelper;

/**
 * The Class CitationIndicesTableAlgorithm.
 * 
 * 
 * Author: P632
 * 
 * Code Review: P632
 */

public class CitationTableAlgorithm implements Algorithm {
	
	public static class Factory extends AbstractCitationAlgorithmFactory {

		@Override
		protected Algorithm createAlgorithm(Data[] data, String authorColumnName,
				String delimiter, CIShellContext ciShellContext) {
			
			return new CitationTableAlgorithm(data, authorColumnName, delimiter, ciShellContext);
		}
	}

	/** The data. */
	private Data[] data;

	/** The logger. */
	private LogService logger;

	/** The delimiter. */
	private String delimiter = null;

	/** The author column title. */
	private String userIdColumnName;

	/**
	 * Instantiates a new citation indices table algorithm.
	 * 
	 * @param data
	 *            the data
	 * @param parameters
	 *            the parameters
	 * @param ciShellContext
	 *            the ci shell context
	 */
	public CitationTableAlgorithm(Data[] data,
			String userIdColumnName, 
			String delimiter, 
			CIShellContext ciShellContext) {

		this.data = data;
		this.userIdColumnName = userIdColumnName;
		this.delimiter = delimiter;
		this.logger = (LogService) ciShellContext.getService(LogService.class.getName());
	}

	/**
	 * Execute.
	 * 
	 * @return the data[]
	 * @throws AlgorithmExecutionException
	 *             the algorithm execution exception
	 */
	public Data[] execute() throws AlgorithmExecutionException {

		Table inputTable = (Table) (data[0].getData());
		Set<String> uniqueUserIds = GoogleScholarReaderHelper.getUniqueAuthors(
						inputTable, userIdColumnName, delimiter, logger);

		// Generating new master table for the output of indices
		// The function returns a list of authors found by the google scholar author search
		Map<String, Table> outputMap = this.getAuthorCitations(uniqueUserIds);
		
		logger.log(LogService.LOG_INFO, "Total authors on file: " + uniqueUserIds.size());
		
		logger.log(LogService.LOG_INFO, "Total authors found: " + outputMap.size());
		
		logger.log(LogService.LOG_INFO, "Total authors not found: " 
										+ (uniqueUserIds.size() - outputMap.size()));
		
		return createOutputData(outputMap);

	}
	
	private Map<String, Table> getAuthorCitations(Set<String> userIds) 
			throws AlgorithmExecutionException {
		Map<String, Table> outputMap = new HashMap<String, Table>();
		
		for (String userId : userIds) {
			try {
				Table citationTable = CitationTableExtractor.getAuthorCitation(userId);
				outputMap.put(userId, citationTable);
			} catch (Exception e) {
				logger.log(LogService.LOG_INFO, 
						userId + ": Citation not found for the given user ID!!");
			}
		}
		
		return outputMap;
	}

	/**
	 * Creates the output data.
	 * 
	 * @param table
	 *            the output table
	 * @param tables
	 *            the tables
	 * @return the data[]
	 */
	private Data[] createOutputData(Map<String, Table> outputMap) {

		List<Data> outputDataList = new ArrayList<Data>();

		for (String userId : outputMap.keySet()) {
			String label = "Citation Table: " + userId;
			Data outputData = DataFactory.forObject(
					outputMap.get(userId), 
					Table.class.getName(), 
					DataProperty.TABLE_TYPE, 
					data[0], 
					label);
			
			outputDataList.add(outputData);
		}

		return (Data[]) outputDataList.toArray(new Data[] {});
	}

}