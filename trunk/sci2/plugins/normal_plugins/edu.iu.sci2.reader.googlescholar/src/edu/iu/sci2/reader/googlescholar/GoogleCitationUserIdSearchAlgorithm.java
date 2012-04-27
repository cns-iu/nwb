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
import edu.iu.sci2.reader.googlescholar.search.AuthorSearch;
import edu.iu.sci2.reader.googlescholar.search.GoogleScholarReaderHelper;

/**
 * <p>
 * Web reader algorithm that accepts a table containing a column of author names
 * and returns a table with added columns that give the Google citation user id 
 * tied to that author on their Google Scholar author page.
 * </p>
 * 
 * @author P632
 */
public class GoogleCitationUserIdSearchAlgorithm implements Algorithm {

	public static class Factory extends AbstractCitationAlgorithmFactory {
		@Override
		protected Algorithm createAlgorithm(Data[] data, String authorColumnName,
				String delimiter, CIShellContext ciShellContext) {
			return new GoogleCitationUserIdSearchAlgorithm(
					data, authorColumnName, delimiter, ciShellContext);
		}
	}
	
	private Data[] data;
	private LogService logger;
	private String authorColumnName;
	private String delimiter;

	public GoogleCitationUserIdSearchAlgorithm(Data[] data, String authorColumnName,
			String delimiter, CIShellContext ciShellContext) {
		this.data = data;
		this.authorColumnName = authorColumnName;
		this.delimiter = delimiter;
		this.logger = (LogService) ciShellContext.getService(LogService.class.getName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.cishell.framework.algorithm.Algorithm#execute()
	 */
	public Data[] execute() throws AlgorithmExecutionException {
		Table table = (Table) (data[0].getData());
		Set<String> uniqueAuthors = GoogleScholarReaderHelper.getUniqueAuthors(
						table, authorColumnName, delimiter, logger);

		AuthorSearch authorSearch = new AuthorSearch(uniqueAuthors, logger);
		Table outputTable = authorSearch.getAuthorInformationTable();

		logger.log(LogService.LOG_INFO, "Total authors on file: "
				+ (authorSearch.getCountSingleAuthorFound()
				+ authorSearch.getCountMultipleAuthorFound() 
				+ authorSearch.getCountAuthorNotFound()));
		logger.log(LogService.LOG_INFO, "Total authors found(Exact match): "
				+ authorSearch.getCountSingleAuthorFound());
		logger.log(LogService.LOG_INFO, "Total authors found(Not exact match): "
				+ authorSearch.getCountMultipleAuthorFound());
		logger.log(LogService.LOG_INFO, "Total authors not found(No match): "
				+ authorSearch.getCountAuthorNotFound());

		return prepareOutputData(outputTable);
	}

	/**
	 * Create the appropriate {@link Data} container for the {@link Table}.
	 */
	private Data[] prepareOutputData(Table table) {

		Data[] outputData = {DataFactory.forObject(
				table, 
				Table.class.getName(), 
				DataProperty.TABLE_TYPE, 
				data[0], 
				"Google Citation User Id")};

		return outputData;
	}
}