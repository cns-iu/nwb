/* 
 * InfoVis CyberInfrastructure: A Data-Code-Compute Resource for Research
 * and Education in Information Visualization (http://iv.slis.indiana.edu/).
 * 
 * Created on Oct 12, 2005 at Indiana University.
 */
package edu.iu.informatics.shared;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.iu.iv.core.IVC;
import edu.iu.iv.core.algorithm.AbstractAlgorithm;
import edu.iu.iv.core.datamodels.DataModel;
import edu.iu.iv.core.persistence.BasicFileResourceDescriptor;
import edu.iu.iv.core.persistence.FileResourceDescriptor;
import edu.iu.iv.core.persistence.PersistenceException;
import edu.iu.iv.core.persistence.Persister;
import edu.iu.iv.core.util.staticexecutable.StaticExecutableRunner;

/**
 * 
 * @author Bruce Herr
 */
public abstract class FortranAlgorithm extends AbstractAlgorithm {
    protected DataModel dm;
    
    public FortranAlgorithm(DataModel dm) {
        this.dm = dm;
    }

    /**
     * @see edu.iu.iv.core.algorithm.AbstractAlgorithm#execute()
     */
    public abstract boolean execute();

    protected void saveDataModel(StaticExecutableRunner runner,
			                     String fileName, 
			                     Persister p) {
		String file = runner.getTempDirectory().getPath() + File.separator
				+ fileName;
		
		FileResourceDescriptor frd = new BasicFileResourceDescriptor(new File(
				file));
		try {
			if (p == null) {
				IVC.getInstance().getPersistenceRegistry().save(dm.getData(),
						frd);
			} else {
				p.persist(dm.getData(), frd);
			}
		} catch (IOException e) {
			throw new Error(e);
		} catch (PersistenceException e) {
			throw new Error(e);
		}

	}

    protected void saveDataModel(StaticExecutableRunner runner, String fileName) {
    	saveDataModel(runner, fileName, null);
    }
    
    protected void makeInputFile(StaticExecutableRunner runner, String fileName, 
    		                     List parmList, boolean parmListAtBegin) {
        try {
            String file = runner.getTempDirectory().getPath() + File.separator + fileName;
            PrintStream out = new PrintStream(new FileOutputStream(file));
            
            if (parmListAtBegin) {
            	writeParameterList(out, parmList);
            	writeParameterMap(out);
            }
            else {
            	writeParameterMap(out);
            	writeParameterList(out, parmList);            	
            }
            
            out.close();
        } catch (FileNotFoundException e) {
            throw new Error(e);
        }
    }
    
    private void writeParameterList(PrintStream out, List parmList) {
    	Iterator iter = parmList.iterator();
    	
    	while(iter.hasNext()) {
    		out.println(iter.next());
    	}
    }
    
    private void writeParameterMap(PrintStream out) {
        Iterator iter = parameterMap.getAllKeys();
        
        while (iter.hasNext()) {
            String key = iter.next().toString();
            
            out.println(parameterMap.getTextValue(key));
        }
    }
    
    protected void makeInputFile(StaticExecutableRunner runner, String fileName) {
/*    	
        try {
            String file = runner.getTempDirectory().getPath() + File.separator + fileName;
            PrintStream out = new PrintStream(new FileOutputStream(file));
            
            Iterator iter = parameterMap.getAllKeys();
            
            while (iter.hasNext()) {
                String key = iter.next().toString();
                
                out.println(parameterMap.getTextValue(key));
            }
            out.close();
        } catch (FileNotFoundException e) {
            throw new Error(e);
        }
*/
    	makeInputFile(runner, fileName, new ArrayList(), false);
    }
}
