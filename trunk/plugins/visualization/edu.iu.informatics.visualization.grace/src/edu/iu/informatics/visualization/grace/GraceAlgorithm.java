/* 
 * InfoVis CyberInfrastructure: A Data-Code-Compute Resource for Research
 * and Education in Information Visualization (http://iv.slis.indiana.edu)
 */
package edu.iu.informatics.visualization.grace;

import java.io.File;
import java.io.IOException;

import edu.iu.iv.core.IVC;
import edu.iu.iv.core.algorithm.AbstractAlgorithm;
import edu.iu.iv.core.datamodels.DataModel;
import edu.iu.iv.core.persistence.BasicFileResourceDescriptor;
import edu.iu.iv.core.persistence.FileResourceDescriptor;
import edu.iu.iv.core.persistence.PersistenceException;
import edu.iu.iv.core.persistence.ResourceDescriptor;
import edu.iu.iv.core.util.staticexecutable.StaticExecutableRunner;

/**
 * Class to implement the Algorithm for this IVC Plug-in.
 *
 * @author
 */
public class GraceAlgorithm extends AbstractAlgorithm {    
    //Oprivate static final String ALGORITHM_NAME = "Grace";
    DataModel dm;
    
    /**
     * Creates a new GraceAlgorithm.
     */
	public GraceAlgorithm(DataModel dm) {
		this.dm = dm;
	}
	
	/**
	 * Executes this GraceAlgorithm.
	 * 
	 * @return true if the Algorithm was successful, false if not
	 */
	public boolean execute(){
		StaticExecutableRunner runner = new StaticExecutableRunner();
		
		//Save the .dat file for Grace 
		FileResourceDescriptor frd = (FileResourceDescriptor)dm.getData();
		String file = runner.getTempDirectory().getPath() 
		              + File.separator
		              + frd.getFileName();
		ResourceDescriptor saveResourceDescriptor = new BasicFileResourceDescriptor(new File(file));
		try {
			IVC.getInstance().getPersistenceRegistry().save(frd, saveResourceDescriptor);
			
			//set arguments and run Grace
			runner.setArguments(frd.getFileName());		
			runner.execute(GracePlugin.ID_PLUGIN);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (PersistenceException e) {
			e.printStackTrace();
			return false;
		}
		
	    return true;
	}
}
