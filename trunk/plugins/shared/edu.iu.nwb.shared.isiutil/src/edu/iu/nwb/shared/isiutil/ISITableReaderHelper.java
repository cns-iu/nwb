package edu.iu.nwb.shared.isiutil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.osgi.service.log.LogService;

import prefuse.data.Table;
import edu.iu.nwb.shared.isiutil.exception.CitationExtractionPreparationException;
import edu.iu.nwb.shared.isiutil.exception.ReadISIFileException;
import edu.iu.nwb.shared.isiutil.exception.ReadTableException;

public class ISITableReaderHelper {
	public static Table readISIFile(
			File isiFile,
			LogService logger,
			boolean shouldNormalizeAuthorNames,
			boolean shouldCleanAuthorNameCapitalizations,
			boolean shouldFillFileMetadata,
			boolean shouldCleanCitedReferences) throws ReadISIFileException {
		return readISIFile(
			isiFile.getAbsolutePath(),
			isiFile,
			logger,
			shouldNormalizeAuthorNames,
			shouldCleanAuthorNameCapitalizations,
			shouldFillFileMetadata,
			shouldCleanCitedReferences);
	}

	// This is sort of a hack, but meh.
	public static Table readISIFile(
			String originalFileName,
			File isiFile,
			LogService logger,
			boolean shouldNormalizeAuthorNames,
			boolean shouldCleanAuthorNameCapitalizations,
			boolean shouldFillFileMetadata,
			boolean shouldCleanCitedReferences) throws ReadISIFileException {
		try {
    		ISITableReader tableReader = new ISITableReader(logger, shouldNormalizeAuthorNames);
    		Table tableWithDups = tableReader.readTable(
    			originalFileName,
    			isiFile,
    			shouldCleanAuthorNameCapitalizations,
    			shouldFillFileMetadata);
			Table preparedTable =
				new ISICitationExtractionPreparer(logger).prepareForCitationExtraction(
					tableWithDups, shouldCleanCitedReferences);
			
			return preparedTable; 		
    	} catch (SecurityException e) {
    		throw new ReadISIFileException(e.getMessage(), e);    		
    	} catch (FileNotFoundException e) {
    		throw new ReadISIFileException(e.getMessage(), e);    		
    	} catch (UnsupportedEncodingException e) {
    		throw new ReadISIFileException(e.getMessage(), e);			
		} catch (IOException e) {
			throw new ReadISIFileException(e.getMessage(), e);
		} catch (ReadTableException e) {
			throw new ReadISIFileException(e.getMessage(), e);
		} catch (CitationExtractionPreparationException e) {
			throw new ReadISIFileException(e.getMessage(), e);
		}
	}
}