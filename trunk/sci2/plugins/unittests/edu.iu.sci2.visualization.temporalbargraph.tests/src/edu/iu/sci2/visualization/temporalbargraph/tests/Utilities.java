package edu.iu.sci2.visualization.temporalbargraph.tests;

import static edu.iu.sci2.testutilities.TestUtilities.openFile;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.Data;
import org.osgi.framework.BundleException;

import edu.iu.sci2.testutilities.TestUtilities;

public class Utilities {
	/**
	 * This run the TBG algorithm indicated by the {@code AlgorithmFactory}.
	 * Additionally, it will open the files if the {@code openFiles} parameter
	 * is {@code true}.
	 * 
	 * @throws AlgorithmExecutionException If the files could not be opened
	 */
	public static void testTBGAlgorithm(AlgorithmFactory algorithmFactory,
			Dictionary<String, Object> parameters, Data[] convertedData,
			boolean openFiles) throws AlgorithmExecutionException {
		CIShellContext cishellContext;

		try {
			cishellContext = TestUtilities.createFakeCIShellContext();
		} catch (BundleException e) {
			throw new AlgorithmExecutionException(
					"There was a problem creating the test CIShell Context: "
							+ e.getLocalizedMessage());
		}

		Algorithm algorithm = algorithmFactory.createAlgorithm(convertedData,
				parameters, cishellContext);

		Data[] returnData = algorithm.execute();
		assertTrue(returnData.length == 2);
		assertNotNull(returnData[0]);
		assertNotNull(returnData[1]);
		assertTrue(returnData[0].getData() instanceof File);
		assertTrue(returnData[1].getData() instanceof File);

		if (openFiles) {
			try {
				openFile((File) returnData[0].getData());
				openFile((File) returnData[1].getData());
			} catch (IOException e) {
				e.printStackTrace();
				throw new AlgorithmExecutionException(
						"The files produced could not be opened!"
								+ e.getLocalizedMessage(), e);
			}
		}
	}
}
