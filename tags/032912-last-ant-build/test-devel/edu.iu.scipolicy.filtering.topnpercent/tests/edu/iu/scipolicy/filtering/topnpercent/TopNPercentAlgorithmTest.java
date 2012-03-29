package edu.iu.scipolicy.filtering.topnpercent;

import static org.junit.Assert.fail;

import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.service.database.DataSourceWithID;
import org.cishell.templates.database.SQLFormationException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TopNPercentAlgorithmTest {
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testFormSQL() {
		// Just "use" NSF "data" for now.
		TopNPercentAlgorithm topNPercentAlgorithm =
			createTopNPercentAlgorithm(createNSFDataSource());
		
		try {
			topNPercentAlgorithm.formSQL();
		}
		catch (SQLFormationException e) {
			fail();
		}
	}

	@Test
	public void testCreateOutDataFromNSFDataSource() {
		// Just "use" NSF "data" for now.
		TopNPercentAlgorithm topNPercentAlgorithm =
			createTopNPercentAlgorithm(createNSFDataSource());
		
		try {
			topNPercentAlgorithm.execute();
		}
		catch (Exception e) {
			fail();
		}
	}
	
	@Test
	public void testCreateOutDataFromSomeOtherDataSource() {
		// Just "use" some other "data" for now.
		TopNPercentAlgorithm topNPercentAlgorithm =
			createTopNPercentAlgorithm(createSomeOtherDataSource());
		
		try {
			topNPercentAlgorithm.execute();
		}
		catch (Exception e) {
			fail();
		}
	}
	
	private DataSourceWithID createNSFDataSource() {
		// TODO: Write temporary NSF file to disk.
		// TODO: Run NSF Loader algorithm on temporary NSF file.
		return null;
	}
	
	private DataSourceWithID createSomeOtherDataSource() {
		// TODO: Write temporary some-other-file to disk.
		// TODO: Run SomeOther Loader algorithm on temporary some-other-file.
		return null;
	}

	private TopNPercentAlgorithm createTopNPercentAlgorithm(DataSourceWithID dataSource) {
		// Wrap the data source to pass in to the new top n% algorithm.
		Data dataSourceData = new BasicData(dataSource, dataSource.getClass().getName());
		
		return new TopNPercentAlgorithm(new Data[] { dataSourceData }, null, null);
	}
}
