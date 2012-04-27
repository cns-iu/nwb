package edu.iu.sci2.reader.googlescholar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.utilities.DataFactory;
import org.osgi.service.log.LogService;

import edu.iu.sci2.reader.googlescholar.citationbibtex.AuthorCitationBibtexDownloader;
import edu.iu.sci2.reader.googlescholar.search.GoogleScholarReaderHelper;

import prefuse.data.Table;

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
public class CitationBibtexAlgorithm implements Algorithm {

	public static class Factory extends AbstractCitationAlgorithmFactory {

		@Override
		protected Algorithm createAlgorithm(Data[] data,
				String userIdColumnName, String delimiter, CIShellContext ciShellContext) {

			return new CitationBibtexAlgorithm(data, userIdColumnName, delimiter, ciShellContext);
		}
	}
	
	/** The data. */
	private Data[] data;

	/** The logger. */
	private LogService logger;

	/** The Constant BIBTEX_MIME_TYPE. */
	private static final String BIBTEX_MIME_TYPE = "file:text/bibtex";

	private String delimiter = null;
	
	/** The author column title. */
	private String userIdColumnName;

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
	public CitationBibtexAlgorithm(Data[] data,
			String userIdColumnName, 
			String delimiter, 
			CIShellContext ciShellContext) {

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
	public Data[] execute() {

		Table inputTable = (Table) (data[0].getData());
		
		Set<String> uniqueUserIds = GoogleScholarReaderHelper.getUniqueAuthors(
				inputTable, userIdColumnName, delimiter, logger);

		// generating new master table for the output of indices
		//The function returns a list of authors found by the google scholar author search
		List<File> bibtexFiles = getAuthorBibtex(uniqueUserIds);
		
		logger.log(LogService.LOG_INFO, "Total user id on file: " + uniqueUserIds.size());
		
		logger.log(LogService.LOG_INFO, "Total user id found: " + bibtexFiles.size());
		
		logger.log(LogService.LOG_INFO, "Total user id not found: " 
										+ (uniqueUserIds.size() - bibtexFiles.size()));
		
		return createOutputData(bibtexFiles);
	}
	
	private List<File> getAuthorBibtex(Set<String> userIds) {
		List<File> outputList = new ArrayList<File>();
		
		for (String userId : userIds) {
			try {
				File bibtexFile = AuthorCitationBibtexDownloader.downloadAuthorBibtexFile(userId);
				outputList.add(bibtexFile);
			} catch (Exception e) {
				logger.log(LogService.LOG_INFO, 
						userId + ": Citation not found for the given user ID!!");
				continue;
			}
		}
		
		return outputList;
	}

	private Data[] createOutputData(List<File> bibtexFiles) {

		List<Data> outputDataList = new ArrayList<Data>();

		for (File bibtex : bibtexFiles) {
			
			Data outputData = DataFactory.forObject(
					bibtex, 
					BIBTEX_MIME_TYPE, 
					DataProperty.TEXT_TYPE, 
					data[0], 
					bibtex.getName());
			
			outputDataList.add(outputData);
		}

		return outputDataList.toArray(new Data[] {});
	}
}