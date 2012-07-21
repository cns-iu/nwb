package edu.iu.sci2.visualization.scimaps.tests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

import edu.iu.nwb.converter.prefusecsv.reader.PrefuseCsvReader;
import edu.iu.sci2.visualization.scimaps.MapOfScience;
import edu.iu.sci2.visualization.scimaps.journals.JournalsMapAlgorithm;
import edu.iu.sci2.visualization.scimaps.journals.JournalsMapAlgorithmFactory;
import edu.iu.sci2.visualization.scimaps.parameters.ScalingFactorAttributeDefinition;
import edu.iu.sci2.visualization.scimaps.testing.LogOnlyCIShellContext;

public class MapOfScienceTest {
	Map<String, Integer> realJournals = new HashMap<String, Integer>();
	Map<String, Integer> fakeJournals = new HashMap<String, Integer>();
	Map<String, Integer> allJournals = new HashMap<String, Integer>();
	MapOfScience mapOfScience;

	@Before
	public void setUp() {
		this.realJournals.put("BIOl_signal_recept", 100);
		this.realJournals.put("embo journal", 13);
		this.realJournals.put("environmental HEALTH perspectives", 10);
		this.realJournals.put("Oncology", 25);
		this.fakeJournals.put("haha", 99);

		this.allJournals.putAll(this.realJournals);
		this.allJournals.putAll(this.fakeJournals);

		this.mapOfScience = new MapOfScience("Fractional Journal Count", this.allJournals);
	}

	@Test
	public void testCountOfMappedPublications() {

		assertTrue(this.mapOfScience.countOfMappedPublications() == this.realJournals
				.size());
	}

	@Test
	public void testPrettyCountOfMappedPublications() {
		DecimalFormat formatter = MapOfScience.FORMATTER;
		assertTrue(this.mapOfScience.prettyCountOfMappedPublications().equals(
				formatter.format(this.mapOfScience.countOfMappedPublications())));
	}

	@Test
	public void testCountOfUnmappedPublications() {

		assertTrue(this.mapOfScience.countOfUnmappedPublications() == this.fakeJournals
				.size());
	}

	@Test
	public void testCountOfPublications() {

		assertTrue(this.mapOfScience.countOfPublications() == this.allJournals.size());
	}

	/**
	 * Test the number of subdisciplines expected to be used by the test data.
	 */
	@Test
	public void testCountOfMappedSubdisciplines() {
		assertTrue("There should 3 mapped subdisciplines but "
				+ this.mapOfScience.countOfMappedSubdisciplines()
				+ " were found.",
				this.mapOfScience.countOfMappedSubdisciplines() == 3);
	}
	
	/**
	 * Test the number of disciplines expected to be used by the test data.
	 */
	@Test
	public void testCountOfCategoriesUsed() {
		assertTrue(
				"Expected 1 category to be used but "
						+ this.mapOfScience.countOfDisciplinesUsed()
						+ " were found.",
				this.mapOfScience.countOfDisciplinesUsed() == 1);
	}

	@Test
	public void testGetUnmappedResults(){
		Map<String, Float> unmappedResults = this.mapOfScience.getUnmappedResults();
		for(String journalName : unmappedResults.keySet()){
			assertTrue(this.fakeJournals.containsKey(journalName));
			assertTrue((float) this.fakeJournals.get(journalName) == unmappedResults.get(journalName));
		}
	}
	

	// This could be used to manually run the algorithm test to visually verify the results.
	@Test
	public void testVisually() {

		try {
			File inFile = new File("sampledata", "LaszloBarabasi.isi.csv");
			Data data = new BasicData(inFile, JournalsMapAlgorithm.CSV_MIME_TYPE);

			PrefuseCsvReader prefuseCSVReader = new PrefuseCsvReader(new Data[] { data });
			Data[] convertedData = prefuseCSVReader.execute();

			Dictionary<String, Object> parameters = new Hashtable<String, Object>();
			parameters.put(JournalsMapAlgorithmFactory.JOURNAL_COLUMN_ID, "Journal Title (Full)");
			parameters.put(JournalsMapAlgorithmFactory.SCALING_FACTOR_ID,
//					String.valueOf(1.0f));
					ScalingFactorAttributeDefinition.AUTO_TOKEN);
			parameters.put(JournalsMapAlgorithmFactory.SUBTITLE_ID, inFile.getName());
			parameters.put(JournalsMapAlgorithmFactory.SHOW_EXPORT_WINDOW, true);
			parameters.put(JournalsMapAlgorithmFactory.WEB_VERSION_ID, false);

			AlgorithmFactory algorithmFactory = new JournalsMapAlgorithmFactory();
			CIShellContext ciContext = new LogOnlyCIShellContext();
			Algorithm algorithm = algorithmFactory.createAlgorithm(convertedData, parameters,
					ciContext);

			System.out.println("Executing.. ");
			Data[] outData = algorithm.execute();
			System.out.println(".. Done.");
			
//			Desktop.getDesktop().open((File) outData[2].getData()); // TODO remove
		} catch (NoClassDefFoundError e) {
			if (e.getMessage() != null
					&& e.getMessage()
							.contains("sun.awt.X11GraphicsEnvironment")) {
				Assume.assumeNoException(e);
			}
			throw e;
		} catch (Exception e) {
			System.err.println("error!");
			e.printStackTrace();
			fail("There was a problem" + e.getMessage());
		}
	}

}
