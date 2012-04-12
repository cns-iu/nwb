package edu.iu.sci2.database.isi.extract.tests;

import static org.junit.Assert.fail;

import java.util.Hashtable;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.LocalCIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.utilities.AlgorithmUtilities;
import org.cishell.utilities.FileUtilities;
import org.junit.BeforeClass;
import org.junit.Test;
import org.osgi.framework.BundleContext;



public class DatabaseExtractTest {
	private static Data databaseData;
	private static BundleContext bundleContext;
	private static CIShellContext ciContext;
	
	@BeforeClass
	public static void makeDB() throws Exception {
		bundleContext = Activator.getContext();
		ciContext = new LocalCIShellContext(bundleContext);
		String file =
				FileUtilities.safeLoadFileFromClasspath(DatabaseExtractTest.class, 
						"/edu/iu/sci2/database/scopus/load/testdata/BrainCancer.scopus").toString();
		
		AlgorithmFactory isiLoaderFactory = AlgorithmUtilities.getAlgorithmFactoryByPID(
				"edu.iu.sci2.database.isi.load.ISIDatabaseLoaderAlgorithm", bundleContext);

		Data inFile = new BasicData(file, "file:text/csv");
		
		Algorithm algo = isiLoaderFactory.createAlgorithm(new Data[] {inFile}, new Hashtable<String, Object>(), ciContext);
		Data[] results = algo.execute();
		databaseData = results[0];
//		Database db = (Database) results[0].getData();
//		connection = db.getConnection();
	}
	
	@Test
	public void test() throws Exception {
		
		AlgorithmFactory fact = AlgorithmUtilities.getAlgorithmFactoryByPID(
				"edu.iu.sci2.database.isi.extract.network.cocitation.document.core", bundleContext);
		
		Algorithm algo = fact.createAlgorithm(new Data[] {databaseData}, new Hashtable<String, Object>(), ciContext);
		Data[] results = algo.execute();
		
		fail("Not yet implemented");
	}

}
