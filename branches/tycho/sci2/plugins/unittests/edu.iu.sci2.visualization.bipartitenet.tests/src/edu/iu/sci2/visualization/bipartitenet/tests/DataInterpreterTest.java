package edu.iu.sci2.visualization.bipartitenet.tests;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import edu.iu.nwb.util.nwbfile.ParsingException;
import edu.iu.sci2.visualization.bipartitenet.model.BipartiteGraphDataModel;
import edu.iu.sci2.visualization.bipartitenet.model.NWBDataImporter;

@RunWith(JUnit4.class)
public class DataInterpreterTest {
	@Rule
	public ExpectedException exception = ExpectedException.none();
	
	@Test
	public void testNormal() throws IOException, ParsingException {
		NWBDataImporter importer = new NWBDataImporter("bipartitetype", "Who", "Desirability", null);
		BipartiteGraphDataModel model;
		model = importer.constructModelFromFile(getTestNetwork());
		assertNotNull(model);
	}

	private InputStream getTestNetwork() {
		return this.getClass().getResourceAsStream("test-network.nwb");
	}
	
	@Test
	public void testBadTypeColumn() throws IOException, ParsingException {
		NWBDataImporter importer = new NWBDataImporter("wrongname", "Who", "Desirability", null);
		exception.expect(AssertionError.class);
		importer.constructModelFromFile(getTestNetwork());
	}
	
	@Test
	public void testBadSizeColumn() throws IOException, ParsingException {
		NWBDataImporter importer = new NWBDataImporter("bipartitetype", "Who", "wrongname", null);
		exception.expect(AssertionError.class);
		importer.constructModelFromFile(getTestNetwork());
	}
}
