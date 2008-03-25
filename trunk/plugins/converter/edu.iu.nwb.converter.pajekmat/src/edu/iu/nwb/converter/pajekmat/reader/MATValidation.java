/**
 * 
 */
package edu.iu.nwb.converter.pajekmat.reader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.log.LogService;
import org.osgi.service.metatype.MetaTypeProvider;

import edu.iu.nwb.converter.pajekmat.common.MATFileProperty;
import edu.iu.nwb.converter.pajekmat.common.ValidateMATFile;

/**
 * @author Timothy Kelley
 *
 */
public class MATValidation implements AlgorithmFactory {

	 protected void activate(ComponentContext ctxt) {}
	 protected void deactivate(ComponentContext ctxt) { }
	/* (non-Javadoc)
	 * @see org.cishell.framework.algorithm.AlgorithmFactory#createAlgorithm(org.cishell.framework.data.Data[], java.util.Dictionary, org.cishell.framework.CIShellContext)
	 */
	public Algorithm createAlgorithm(Data[] data, Dictionary parameters,
			CIShellContext context) {
		// TODO Auto-generated method stub
		return new MATValidationAlg(data, parameters, context);
	}

	/* (non-Javadoc)
	 * @see org.cishell.framework.algorithm.AlgorithmFactory#createParameters(org.cishell.framework.data.Data[])
	 */
	public MetaTypeProvider createParameters(Data[] data) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public class MATValidationAlg implements Algorithm {
        
    	Data[] data;
        Dictionary parameters;
        CIShellContext ciContext;
        LogService logger;
        
        public MATValidationAlg(Data[] dm, Dictionary parameters, CIShellContext context) {
        	this.data = dm;
            this.parameters = parameters;
            this.ciContext = context;
            logger = (LogService)ciContext.getService(LogService.class.getName());
        }

        public Data[] execute() {
	    

			String fileHandler = (String) data[0].getData();
			File inData = new File(fileHandler);
			ValidateMATFile validator = new ValidateMATFile();
			try{ 
				validator.validateMATFormat(inData);
				if(validator.getValidationResult()){						
					Data[] dm = new Data[] {new BasicData(inData, MATFileProperty.MAT_MIME_TYPE)};
					dm[0].getMetadata().put(DataProperty.LABEL, "Pajek .mat file: " + fileHandler);
					dm[0].getMetadata().put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
                	return dm;

				}else {
					//System.out.println(">>>wrong format: "+validator.getErrorMessages());
					logger.log(LogService.LOG_ERROR,"Sorry, your file does not comply with the .mat File Format Specification.\n"+
							"Please review the latest NET File Format Specification at "+
							"http://vlado.fmf.uni-lj.si/pub/networks/pajek/doc/pajekman.pdf, and update your file. \n"+
							validator.getErrorMessages());
					return null;
				}

			}catch (FileNotFoundException e){
				logger.log(LogService.LOG_ERROR, "Could not find the Pajek .mat file to validate.",e);	
				return null;
			}catch (IOException ioe){
				logger.log(LogService.LOG_ERROR,
						"IO Errors while reading the specified Pajek .mat file.",ioe);
				return null;
			}
        }
      

    }

}
