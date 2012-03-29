package edu.iu.iv.modeling.tarl.main.impl;

import java.io.File;

import edu.iu.iv.modeling.tarl.TarlException;
import edu.iu.iv.modeling.tarl.input.HelperParameters;
import edu.iu.iv.modeling.tarl.main.TarlExecuter;
import edu.iu.iv.modeling.tarl.main.TarlHelper;
import edu.iu.iv.modeling.tarl.util.YearInformation;


/**
 * This class defines a default implementation of the <code>TarlHelperInterface</code>.  It utilizes the default implementations of all the standard interfaces to achieve its cause.
 *
 * @author Jeegar T Maru
 * @see TarlExecuter
 * @see TarlHelper
 */
public class DefaultTarlHelper implements TarlHelper {
	/**
	 * Stores the <code>TarlExecuter</code> to perform its duties
	 */
	protected TarlExecuter tarlExecuter;

	/**
	 * Stores the YearInformation for the model
	 */
	protected YearInformation yearInformation;

	/**
	 * Creates a new instance of the <code>TarlHelper</code>
	 */
	public DefaultTarlHelper() {
		tarlExecuter = null;
		yearInformation = null;
	}

	/**
	 * Initializes the Tarl model
	 *
	 * @param helperParameters Specifies the model parameters related to the module <code>TarlHelper</code>
	 */
	public void initializeModel(HelperParameters helperParameters, File agingFunctionFile)
		throws TarlException {
		yearInformation = helperParameters.getYearInformation();
		tarlExecuter = new DefaultTarlExecuter();
		tarlExecuter.initializeModel(helperParameters.getExecuterParameters(), agingFunctionFile);
	}

	/**
	 * Runs the Tarl model
	 */
	public void runModel() throws TarlException {
		int currentYear;
		int endYear;

		endYear = yearInformation.getEndYear();

		for (currentYear = yearInformation.getStartYear() + 1;
			    currentYear <= endYear; currentYear++) {
			tarlExecuter.producePublications();
			tarlExecuter.terminateCurrentYear();
		}
	}

	/**
	 * Stores the output of the model on the file system.  It mainly stores the 3 Pajek files for the Cocitation network, Coauthorship network and the Publication-Author network.  It also stores some data files which includes complete information about the running of the model.
	 *
	 * @param outputFolder Specifies the name of the output folder
	 */
	public void writeOutputFiles(String outputFolder) {
		tarlExecuter.writeOutputFiles(outputFolder);
	}

    public TarlExecuter getTarlExecuter() {
        return tarlExecuter;
    }
	/**
	 * Cleans up the system.
	 */
	public void cleanUpSystem() {
		tarlExecuter.cleanUpSystem();
	}
}
