package edu.iu.sci2.database.isi.extract.tests;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.service.database.Database;
import org.cishell.utilities.AlgorithmUtilities;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.google.common.collect.Lists;

@RunWith(Parameterized.class)
public class DatabaseExtractTest {
	
	private static Data databaseData;
	private static BundleContext bundleContext = Activator.getContext();
	private static CIShellContext ciContext = Activator.getCiContext();
	
	/**
	 * Finds all of the isi.extract.* algorithms in the current OSGi context.  To get more of them,
	 * edit MANIFEST.MF and add them to Require-Bundle:.  Weighted document-document network is
	 * intentionally left out of there, because it requires a parameter (the rest don't).
	 * <p>
	 * The list of Strings is used to run the test() method.
	 * @throws Exception
	 */
	// Annoying that the test name doesn't include the PID of the algorithm under test.
	// The first release of JUnit after April 9, 2012 will support this:
	// @Parameters(name="DatabaseExtractTest({0})")
	// See https://github.com/KentBeck/junit/commit/3a5c9f2731462e36dd1c173ea8840d7b9b34b0ab
	// and http://stackoverflow.com/a/10143872/88198
	@Parameters
	public static Collection<Object[]> data() throws Exception {
		Database db = FivePapersHelper.createDatabase();
		databaseData = new BasicData(db, FivePapersHelper.DATA_FORMAT);
		
		ServiceReference<?>[] refs = bundleContext.getServiceReferences(AlgorithmFactory.class.getName(),
				"(service.pid=*isi.extract.*)");
		ArrayList<Object[]> listOfObjArrays = Lists.newArrayList();
		for (ServiceReference<?> r : refs) {
			String thisPid = (String) r.getProperty("service.pid");
			// this is the only one that takes parameters, so skip it for now
			listOfObjArrays.add(new Object[] { thisPid });
		}
		return listOfObjArrays;
	}
	
	private String pid;
	public DatabaseExtractTest(String pid) {
		this.pid = pid;
	}
	
	/**
	 * Try to run a database algorithm (specified by this.pid)
	 * @throws Exception
	 */
	@Test
	public void test() throws Exception {
		System.err.println("RUNNING THIS: " + pid);
		AlgorithmFactory fact = AlgorithmUtilities.getAlgorithmFactoryByPID(
				pid, bundleContext);
		
		Algorithm algo = fact.createAlgorithm(new Data[] {databaseData}, new Hashtable<String, Object>(), ciContext);
		try {
			Data[] results = algo.execute();
			assertTrue(pid + " didn't generate any Data results", results.length > 0);
		} catch (AlgorithmExecutionException e) {
			throw new AssertionError("Error when running " + pid, e);
		}
	}
	
}
