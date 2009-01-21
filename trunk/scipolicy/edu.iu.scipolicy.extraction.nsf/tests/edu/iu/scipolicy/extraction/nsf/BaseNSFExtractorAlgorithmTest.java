package edu.iu.scipolicy.extraction.nsf;

import java.io.File;

import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.service.database.DataSourceWithID;

public class BaseNSFExtractorAlgorithmTest {
	public File createTemporaryNSFFile(String extractorName) {
		// TODO: Actually create a temporary file.
		return null;
	}
	
	public DataSourceWithID createTestNSFDatabase(File sourceFile) {
		// TODO: Write a temporary NSF file out to disk.
		// TODO: Load the temporary NSF back in using the NSF loader algorithm,
		// which will produce our data source.
		return null;
	}
	
	public NSFGrantExtractorAlgorithm createTestNSFGrantExtractor(File sourceFile) {
		// Create the test database.
		DataSourceWithID testNSFDatabase = createTestNSFDatabase(sourceFile);
		
		// Wrap it so we can create the test NSF grant extractor.
		Data testNSFDatabaseData =
			new BasicData(testNSFDatabase, testNSFDatabase.getClass().getName());
		
		return new NSFGrantExtractorAlgorithm(new Data[] { testNSFDatabaseData },
									 null,
									 null);
	}
}