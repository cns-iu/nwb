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
import org.osgi.service.component.ComponentContext;
import org.osgi.service.metatype.MetaTypeProvider;
import org.osgi.service.log.*;

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
		return new NETValidationAlgorithm(data, parameters, context);
	}

	/* (non-Javadoc)
	 * @see org.cishell.framework.algorithm.AlgorithmFactory#createParameters(org.cishell.framework.data.Data[])
	 */
	public MetaTypeProvider createParameters(Data[] data) {
		// TODO Auto-generated method stub
		return null;
	}

	public class NETValidationAlgorithm implements Algorithm {

		Data[] data;
		Dictionary parameters;
		CIShellContext ciContext;
		LogService logger;

		public NETValidationAlgorithm(Data[] inputData, Dictionary parameters, CIShellContext ciShellContext) {
			this.data = inputData;
			this.parameters = parameters;
			this.ciContext = ciShellContext;
			logger = (LogService)ciContext.getService(LogService.class.getName());
		}

		public Data[] execute() {

			String fileHandler = (String) data[0].getData();
			File inputFileData = new File(fileHandler);
			ValidateNETFile validator = new ValidateNETFile();
			try{ 
				/*
				 * validator function is used to validate the data & only if the input file has valid pajek data 
				 * then its loaded into the workbench for further manipulations by the user. 
				 * */
				validator.validateNETFormat(inputFileData);
				if(validator.getValidationResult()){						
					Data[] validationData = new Data[] {new BasicData(inputFileData, NETFileProperty.NET_MIME_TYPE)};
					validationData[0].getMetadata().put(DataProperty.LABEL, "Pajek .net file: " + fileHandler);
					validationData[0].getMetadata().put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
					return validationData;

				}else {

					logger.log(LogService.LOG_ERROR,"Sorry, your file does not comply with the NET File Format Specification.\n"+
							"Please review the latest NET File Format Specification at "+
							"http://vlado.fmf.uni-lj.si/pub/networks/pajek/doc/pajekman.pdf, and update your file. \n"+
							validator.getErrorMessages());
					return null;
				}

			}catch (FileNotFoundException e){
				logger.log(LogService.LOG_ERROR, "Could not find the Pajek .net file to validate.",e);	
				return null;
			}catch (IOException ioe){
				logger.log(LogService.LOG_ERROR,
						"IO Errors while reading the specified Pajek .net file.",ioe);
				return null;
			}
		}


	}

}
