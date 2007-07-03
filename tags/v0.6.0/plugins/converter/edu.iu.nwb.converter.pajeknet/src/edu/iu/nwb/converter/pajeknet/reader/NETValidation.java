/**
 * 
 */
package edu.iu.nwb.converter.pajeknet.reader;

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
import org.cishell.service.guibuilder.GUIBuilderService;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.metatype.MetaTypeProvider;

import edu.iu.nwb.converter.pajeknet.common.NETFileProperty;
import edu.iu.nwb.converter.pajeknet.common.ValidateNETFile;

/**
 * @author Timothy Kelley
 *
 */
public class NETValidation implements AlgorithmFactory {

	 protected void activate(ComponentContext ctxt) {}
	 protected void deactivate(ComponentContext ctxt) { }
	/* (non-Javadoc)
	 * @see org.cishell.framework.algorithm.AlgorithmFactory#createAlgorithm(org.cishell.framework.data.Data[], java.util.Dictionary, org.cishell.framework.CIShellContext)
	 */
	public Algorithm createAlgorithm(Data[] data, Dictionary parameters,
			CIShellContext context) {
		// TODO Auto-generated method stub
		return new NETValidationAlg(data, parameters, context);
	}

	/* (non-Javadoc)
	 * @see org.cishell.framework.algorithm.AlgorithmFactory#createParameters(org.cishell.framework.data.Data[])
	 */
	public MetaTypeProvider createParameters(Data[] data) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public class NETValidationAlg implements Algorithm {
        
    	Data[] data;
        Dictionary parameters;
        CIShellContext ciContext;
        
        public NETValidationAlg(Data[] dm, Dictionary parameters, CIShellContext context) {
        	this.data = dm;
            this.parameters = parameters;
            this.ciContext = context;
        }

        public Data[] execute() {
	    	GUIBuilderService guiBuilder = 
	    			(GUIBuilderService)ciContext.getService(GUIBuilderService.class.getName());

			String fileHandler = (String) data[0].getData();
			File inData = new File(fileHandler);
			ValidateNETFile validator = new ValidateNETFile();
			try{ 
				validator.validateNETFormat(inData);
				if(validator.getValidationResult()){						
					Data[] dm = new Data[] {new BasicData(inData, NETFileProperty.NET_MIME_TYPE)};
					dm[0].getMetaData().put(DataProperty.LABEL, "Pajek .net file: " + fileHandler);
					dm[0].getMetaData().put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
                	return dm;

				}else {
					System.out.println(">>>wrong format: "+validator.getErrorMessages());
					guiBuilder.showError("Bad NET Format", 
							"Sorry, your file does not comply with the NET File Format Specification.",
							"Sorry, your file does not comply with the NET File Format Specification.\n"+
							"Please review the latest NET File Format Specification at "+
							"http://vlado.fmf.uni-lj.si/pub/networks/pajek/doc/pajekman.pdf, and update your file. \n"+
							validator.getErrorMessages());
					return null;
				}

			}catch (FileNotFoundException e){
				guiBuilder.showError("File Not Found Exception", 
						"Got an File Not Found Exception",e);	
				return null;
			}catch (IOException ioe){
				guiBuilder.showError("IOException", 
						"Got an IOException",ioe);
				return null;
			}
        }
      

    }

}
