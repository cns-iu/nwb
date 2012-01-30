package edu.iu.sci2.visualization.scimaps.tests;

import static org.junit.Assert.assertTrue;

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
import org.junit.Before;
import org.junit.Test;

import edu.iu.nwb.converter.prefusecsv.reader.PrefuseCsvReader;
import edu.iu.sci2.visualization.scimaps.MapOfScience;
import edu.iu.sci2.visualization.scimaps.journals.JournalsMapAlgorithm;
import edu.iu.sci2.visualization.scimaps.journals.JournalsMapAlgorithmFactory;
import edu.iu.sci2.visualization.scimaps.testing.LogOnlyCIShellContext;

public class MapOfScienceTests {
	Map<String, Integer> realJournals = new HashMap<String, Integer>();
	Map<String, Integer> fakeJournals = new HashMap<String, Integer>();
	Map<String, Integer> allJournals = new HashMap<String, Integer>();
	MapOfScience mapOfScience;

	@Before
	public void setUp() {
		realJournals.put("BIOl_signal_recept", 100);
		realJournals.put("embo journal", 13);
		realJournals.put("environmental HEALTH perspectives", 10);
		realJournals.put("Oncology", 25);
		fakeJournals.put("haha", 99);
		fakeJournals.put(null, 5);

		allJournals.putAll(realJournals);
		allJournals.putAll(fakeJournals);

		mapOfScience = new MapOfScience(allJournals);
	}

	@Test
	public void testCountOfMappedPublications() {

		assertTrue(mapOfScience.countOfMappedPublications() == realJournals
				.size());
	}

	@Test
	public void testPrettyCountOfMappedPublications() {
		DecimalFormat formatter = MapOfScience.formatter;
		assertTrue(mapOfScience.prettyCountOfMappedPublications().equals(
				formatter.format(mapOfScience.countOfMappedPublications())));
	}

	@Test
	public void testCountOfUnmappedPublications() {

		assertTrue(mapOfScience.countOfUnmappedPublications() == fakeJournals
				.size());
	}

	@Test
	public void testCountOfPublications() {

		assertTrue(mapOfScience.countOfPublications() == allJournals.size());
	}

	
	@Test
	public void testCountOfMappedSubdiciplines() {
		assertTrue(mapOfScience.countOfMappedSubdiciplines() == 83);
	}
	
	@Test
	public void testCountOfCategoriesUsed() {
		assertTrue(mapOfScience.countOfCategoriesUsed() == 9);
	}
	
	@Test
	public void testGetUnmappedResults(){
		Map<String, Float> unmappedResults = mapOfScience.getUnmappedResults();
		for(String journalName : unmappedResults.keySet()){
			assertTrue(fakeJournals.containsKey(journalName));
			assertTrue((float) fakeJournals.get(journalName) == unmappedResults.get(journalName));
		}
	}
	

	// This could be used to manually run the algorithm test to visually verify the results.
	@Test
	public void testVisually() {

		try {
			String filelocation = "sampledata\\LaszloBarabasi.isi.csv";
			File inFile = new File(filelocation);
			Data data = new BasicData(inFile, JournalsMapAlgorithm.CSV_MIME_TYPE);

			PrefuseCsvReader prefuseCSVReader = new PrefuseCsvReader(
					new Data[] { data });
			Data[] convertedData = prefuseCSVReader.execute();

			Dictionary<String, Object> parameters = new Hashtable<String, Object>();
			parameters.put(JournalsMapAlgorithmFactory.JOURNAL_COLUMN_ID,
					"Journal Title (Full)");
			parameters.put(JournalsMapAlgorithmFactory.SCALING_FACTOR_ID, 1.0f);
			parameters.put(JournalsMapAlgorithmFactory.DATA_DISPLAY_NAME_ID,
					inFile.getName());
			parameters.put(JournalsMapAlgorithmFactory.WEB_VERSION_ID, false);

			AlgorithmFactory algorithmFactory = new JournalsMapAlgorithmFactory();
			CIShellContext ciContext = new LogOnlyCIShellContext();
			Algorithm algorithm = algorithmFactory.createAlgorithm(
					convertedData, parameters, ciContext);

			System.out.println("Executing.. ");
			algorithm.execute();
			System.out.println(".. Done.");
		} catch (Exception e) {
			System.err.println("error!");
			e.printStackTrace();
			System.exit(-1);
		}
	}

}
