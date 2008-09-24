package edu.iu.iv.modeling.tarl.main;

import java.io.File;

import edu.iu.iv.modeling.tarl.TarlException;
import edu.iu.iv.modeling.tarl.author.AuthorManager;
import edu.iu.iv.modeling.tarl.input.ExecuterParameters;
import edu.iu.iv.modeling.tarl.publication.PublicationManager;


/**
 * This interface defines the function of the <code>TarlExecuter</code>.  The <code>TarlExecuter</code> is responsible for the providing the core functions for the Tarl model such as adding an author/publication, publishing for the current year, deactivating an author, terminating the current year, generating output, etc.
 *
 * @author Jeegar T Maru
 * @see TarlHelper
 */
public interface TarlExecuter {
	/**
	 * Initializes itself with the corresponding input parameters.  These parameters are used while carrying out the functions so that they need not be provided again and again.  This function should also initialize any of the modules that the <code>TarlExecuter</code> is relying on.  This function should be called before calling any other function in the interface.
	 *
	 * @param executerParameters Specifies the executer parameters of the model
	 *
	 * @exception TarlException if the constraints of the model are violated
	 */
	public void initializeModel(ExecuterParameters executerParameters, File agingFunctionFile)
		throws TarlException;

	/**
	 * Produces <code>Publications</code> for the current year.  The <code>TarlExecuter</code> should choose the parameters for producing the publications based on the initialization values that it was supplied with.
	 *
	 * @exception TarlException if the constraints of the model are violated
	 */
	public void producePublications() throws TarlException;

	/**
	 * Terminates the current year for the system.  The <code>TarlExecuter</code> should update the system to indicate that the current year has terminated.
	 *
	 * @exception TarlException if the new authors cannot be instantiated
	 */
	public void terminateCurrentYear() throws TarlException;

	/**
	 * Writes the Output Files to the file system
	 *
	 * @param outputFolder Specifies the name of the output folder
	 */
	public void writeOutputFiles(String outputFolder);

	/**
	 * Cleans up the system.  This function frees all the resources that the system has consumed while running.
	 */
	public void cleanUpSystem();
	
	/**
	 * @return The author manager used by this class to manage authors.
	 */
	public AuthorManager getAuthorManager() ;
	
	/**
	 * @return The publication manager used this class to manage publications.
	 */
	public PublicationManager getPublicationManager() ;
}
