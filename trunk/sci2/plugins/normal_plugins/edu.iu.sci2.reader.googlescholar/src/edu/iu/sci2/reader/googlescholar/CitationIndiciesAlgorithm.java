package edu.iu.sci2.reader.googlescholar;

import java.util.Set;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.utilities.DataFactory;
import org.osgi.service.log.LogService;

import prefuse.data.Table;

import edu.iu.sci2.reader.googlescholar.search.GoogleScholarReaderHelper;
import edu.iu.sci2.reader.googlescholar.citationindicies.AuthorIndicesExtractor;
import edu.iu.sci2.reader.googlescholar.citationindicies.IndicesRecord;
import edu.iu.sci2.reader.googlescholar.citationindicies.IndicesRecordTable;
import edu.iu.sci2.reader.googlescholar.citationindicies.AuthorIndicesExtractor.
IndexExtractionException;

/**
 * CitationIndiciesAlgorithm.
 * 
 * Web reader algorithm that accepts a table containing a column of author names
 * and returns a table with added columns that give the citation indices tied to
 * that author on their Google Scholar author page
 * 
 * Author: P632
 * 
 * Code Review: P632
 */
public class CitationIndiciesAlgorithm implements Algorithm {
	
	public static class Factory extends AbstractCitationAlgorithmFactory {
		@Override
		protected Algorithm createAlgorithm(Data[] data,
				String userIdColumnName, String delimiter, CIShellContext ciShellContext) {

			return new CitationIndiciesAlgorithm(data, userIdColumnName, delimiter, ciShellContext);
		}
	}

	/** The data. */
	private Data[] data;

	/** The logger. */
	private LogService logger;

	private String userIdColumnName;

	private String delimiter;

	/**
	 * Instantiates a new citation indicies algorithm.
	 * 
	 * @param data
	 *            the data
	 * @param parameters
	 *            the parameters
	 * @param ciShellContext
	 *            the ci shell context
	 */
	public CitationIndiciesAlgorithm(Data[] data, String userIdColumnName,
			String delimiter, CIShellContext ciShellContext) {

		this.data = data;
		this.userIdColumnName = userIdColumnName;
		this.delimiter = delimiter;
		this.logger = (LogService) ciShellContext.getService(LogService.class.getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cishell.framework.algorithm.Algorithm#execute()
	 */
	public Data[] execute() throws AlgorithmExecutionException {
		Table inputTable = (Table) (data[0].getData());
		Set<String> uniqueUserIds = GoogleScholarReaderHelper.getUniqueAuthors(
				inputTable, userIdColumnName, delimiter, logger);

		Table table = this.getAuthorIndicesTable(uniqueUserIds, logger);

		logger.log(LogService.LOG_INFO, "Total user id on file: " + uniqueUserIds.size());
		
		logger.log(LogService.LOG_INFO, "Total user id found: " + table.getRowCount());
		
		logger.log(LogService.LOG_INFO, "Total user id not found: " 
										+ (uniqueUserIds.size() - table.getRowCount()));

		return prepareOutputData(table);
	}
	
	private Table getAuthorIndicesTable(Set<String> userIds, LogService logger) 
			throws AlgorithmExecutionException {
		
		IndicesRecordTable indicesRecordTable = new IndicesRecordTable();
		
		for (String userId : userIds) {
			try {
				IndicesRecord indicesRecord = AuthorIndicesExtractor.getAuthorIndices(userId);
				indicesRecordTable.addIndicesRecord(indicesRecord);
			} catch (IndexExtractionException e) {
				throw new AlgorithmExecutionException(e);
			} catch (Exception e) {
				logger.log(LogService.LOG_INFO, 
						userId + ": Profile not found for the given user ID!!");
			}
		}
		
		return indicesRecordTable.getTable();
	}

	/**
	 * Prepare output table data.
	 * 
	 * @param outputTable
	 *            the output table data
	 * @return the data
	 */
	private Data[] prepareOutputData(Table table) {

		Data[] outputData = { DataFactory.forObject(
				table, 
				Table.class.getName(), 
				DataProperty.TABLE_TYPE, 
				data[0], 
				"Author Indices")};

		return outputData;
	}
}