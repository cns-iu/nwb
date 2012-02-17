package edu.iu.sci2.database.isi.load.integration.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Hashtable;
import java.util.Properties;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.LocalCIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.service.database.Database;
import org.cishell.utilities.AlgorithmUtilities;
import org.cishell.utilities.FileUtilities;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.osgi.framework.BundleContext;

import edu.iu.nwb.shared.isiutil.database.ISI;
import edu.iu.sci2.database.scholarly.model.entity.Person;

public class TryIntegrationTest {
	private static Connection connection;
	
	/**
	 * Sets up logging, so that you can see all the SQL statements.  They're logged to "derby.log"
	 * in the root directory of this plugin.
	 * @throws Exception 
	 */
	@BeforeClass
	public static void ensureConnection() throws Exception {
		if (connection == null) {
			Properties sprops = System.getProperties();
			sprops.setProperty("derby.language.logStatementText", "true");
			BundleContext context = Activator.getContext();
			CIShellContext ciContext = new LocalCIShellContext(context);
			AlgorithmFactory factory = AlgorithmUtilities.getAlgorithmFactoryByPID(
					"edu.iu.sci2.database.scopus.load.ScopusDatabaseLoaderAlgorithm", Activator.getContext());
			
			String file =
					FileUtilities.safeLoadFileFromClasspath(TryIntegrationTest.class, 
							"/edu/iu/sci2/database/scopus/load/testdata/BrainCancer.scopus").toString();
	
			Data inFile = new BasicData(file, "file:text/csv");
			
			Algorithm algo = factory.createAlgorithm(new Data[] {inFile}, new Hashtable(), ciContext);
			Data[] results = algo.execute();
			Database db = (Database) results[0].getData();
			connection = db.getConnection();
		}
	}
	
	
	@Test
	public void test() throws Exception {
		Statement s = connection.createStatement();
		s.execute("select * from document");
		ResultSet rs = s.getResultSet();
		ResultSetMetaData md = rs.getMetaData();
		rs.next();
		for (int i = 1; i <= md.getColumnCount(); i++) {
			System.out.println(rs.getString(i));
		}
		assertTrue(numRowsInTable(ISI.PERSON_TABLE_NAME) > 70);
		rs.close();
		s.close();
	}
	
	@Test
	public void testFirstPaperAuthors() throws Exception {
		Statement s = connection.createStatement();
		
		s.execute("select pk from document where title like 'Synthesis of carbon%'");
		ResultSet rs = s.getResultSet();
		rs.next();
		int document_pk = rs.getInt(1);
		System.out.println("primary key of a document: " + document_pk);
		
		s.execute("select * from authors join person on (authors.AUTHORS_PERSON_FK = person.PK) WHERE authors.AUTHORS_DOCUMENT_FK = " + document_pk + " order by ORDER_LISTED");
		rs = s.getResultSet();
		rs.next();
		assertEquals(rs.getString(Person.Field.UNSPLIT_NAME.name()), "Gao M.");
		rs.next();
		assertEquals(rs.getString(Person.Field.UNSPLIT_NAME.name()), "Wang M.");
		rs.next();
		assertEquals(rs.getString(Person.Field.UNSPLIT_NAME.name()), "Zheng Q.-H.");
		assertFalse(rs.next());
		rs.close();
		s.close();
	}


	private int numRowsInTable(String tableName) throws Exception {
		Statement s = connection.createStatement();
		s.execute("select count(*) from " + tableName);
		ResultSet rs = s.getResultSet();
		rs.next();
		int count = rs.getInt(1);
		rs.close();
		s.close();
		return count;
	}
	
	// TODO: test whether the page count matches end - start?

	@Ignore
	@Test 
	public void testIsi_fileExists() {
		try {
			numRowsInTable(ISI.ISI_FILE_TABLE_NAME);
		} catch (Exception e) {
			throw new AssertionError("Table " + ISI.ISI_FILE_TABLE_NAME + " seems not to exist: " + e.getMessage());
		}
	}

	@Ignore
	@Test
	public void testPublisherExists() {
		try {
			numRowsInTable(ISI.PUBLISHER_TABLE_NAME);
		} catch (Exception e) {
			throw new AssertionError("Table " + ISI.PUBLISHER_TABLE_NAME + " seems not to exist: " + e.getMessage());
		}
	}

	@Ignore
	@Test
	public void testSourceExists() {
		try {
			numRowsInTable(ISI.SOURCE_TABLE_NAME);
		} catch (Exception e) {
			throw new AssertionError("Table " + ISI.SOURCE_TABLE_NAME + " seems not to exist: " + e.getMessage());
		}
	}

	@Ignore
	@Test
	public void testReferenceExists() {
		try {
			numRowsInTable(ISI.REFERENCE_TABLE_NAME);
		} catch (Exception e) {
			throw new AssertionError("Table " + ISI.REFERENCE_TABLE_NAME + " seems not to exist: " + e.getMessage());
		}
	}

	@Test
	public void testAddressExists() {
		try {
			numRowsInTable(ISI.ADDRESS_TABLE_NAME);
		} catch (Exception e) {
			throw new AssertionError("Table " + ISI.ADDRESS_TABLE_NAME + " seems not to exist: " + e.getMessage());
		}
	}

	@Ignore
	@Test
	public void testKeywordExists() {
		try {
			numRowsInTable(ISI.KEYWORD_TABLE_NAME);
		} catch (Exception e) {
			throw new AssertionError("Table " + ISI.KEYWORD_TABLE_NAME + " seems not to exist: " + e.getMessage());
		}
	}

	@Test
	public void testPersonExists() {
		try {
			numRowsInTable(ISI.PERSON_TABLE_NAME);
		} catch (Exception e) {
			throw new AssertionError("Table " + ISI.PERSON_TABLE_NAME + " seems not to exist: " + e.getMessage());
		}
	}

	@Ignore
	@Test
	public void testPatentExists() {
		try {
			numRowsInTable(ISI.PATENT_TABLE_NAME);
		} catch (Exception e) {
			throw new AssertionError("Table " + ISI.PATENT_TABLE_NAME + " seems not to exist: " + e.getMessage());
		}
	}

	@Test
	public void testDocumentExists() {
		try {
			numRowsInTable(ISI.DOCUMENT_TABLE_NAME);
		} catch (Exception e) {
			throw new AssertionError("Table " + ISI.DOCUMENT_TABLE_NAME + " seems not to exist: " + e.getMessage());
		}
	}

	@Ignore
	@Test
	public void testPublisher_addressesExists() {
		try {
			numRowsInTable(ISI.PUBLISHER_ADDRESSES_TABLE_NAME);
		} catch (Exception e) {
			throw new AssertionError("Table " + ISI.PUBLISHER_ADDRESSES_TABLE_NAME + " seems not to exist: " + e.getMessage());
		}
	}

	@Test
	public void testReprint_addressesExists() {
		try {
			numRowsInTable(ISI.REPRINT_ADDRESSES_TABLE_NAME);
		} catch (Exception e) {
			throw new AssertionError("Table " + ISI.REPRINT_ADDRESSES_TABLE_NAME + " seems not to exist: " + e.getMessage());
		}
	}

	@Ignore
	@Test
	public void testResearch_addressesExists() {
		try {
			numRowsInTable(ISI.RESEARCH_ADDRESSES_TABLE_NAME);
		} catch (Exception e) {
			throw new AssertionError("Table " + ISI.RESEARCH_ADDRESSES_TABLE_NAME + " seems not to exist: " + e.getMessage());
		}
	}

	@Ignore
	@Test
	public void testDocument_keywordsExists() {
		try {
			numRowsInTable(ISI.DOCUMENT_KEYWORDS_TABLE_NAME);
		} catch (Exception e) {
			throw new AssertionError("Table " + ISI.DOCUMENT_KEYWORDS_TABLE_NAME + " seems not to exist: " + e.getMessage());
		}
	}

	@Test
	public void testAuthorsExists() {
		try {
			numRowsInTable(ISI.AUTHORS_TABLE_NAME);
		} catch (Exception e) {
			throw new AssertionError("Table " + ISI.AUTHORS_TABLE_NAME + " seems not to exist: " + e.getMessage());
		}
	}

	@Ignore
	@Test
	public void testEditorsExists() {
		try {
			numRowsInTable(ISI.EDITORS_TABLE_NAME);
		} catch (Exception e) {
			throw new AssertionError("Table " + ISI.EDITORS_TABLE_NAME + " seems not to exist: " + e.getMessage());
		}
	}

	@Ignore
	@Test
	public void testCited_patentsExists() {
		try {
			numRowsInTable(ISI.CITED_PATENTS_TABLE_NAME);
		} catch (Exception e) {
			throw new AssertionError("Table " + ISI.CITED_PATENTS_TABLE_NAME + " seems not to exist: " + e.getMessage());
		}
	}

	@Ignore
	@Test
	public void testDocument_occurrencesExists() {
		try {
			numRowsInTable(ISI.DOCUMENT_OCCURRENCES_TABLE_NAME);
		} catch (Exception e) {
			throw new AssertionError("Table " + ISI.DOCUMENT_OCCURRENCES_TABLE_NAME + " seems not to exist: " + e.getMessage());
		}
	}

	@Ignore
	@Test
	public void testCited_referencesExists() {
		try {
			numRowsInTable(ISI.CITED_REFERENCES_TABLE_NAME);
		} catch (Exception e) {
			throw new AssertionError("Table " + ISI.CITED_REFERENCES_TABLE_NAME + " seems not to exist: " + e.getMessage());
		}
	}
}
