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
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

import edu.iu.nwb.converter.pajekmat.common.MATFileProperty;
import edu.iu.nwb.converter.pajekmat.common.MATFileValidator;

/**
 * @author Timothy Kelley
 *
 */
public class MATValidation implements AlgorithmFactory {
	public Algorithm createAlgorithm(Data[] data, Dictionary parameters,
			CIShellContext context) {
		return new MATValidationAlg(data, parameters, context);
	}

	
	public class MATValidationAlg implements Algorithm {
		private String inMATFileName;		

		public MATValidationAlg(
				Data[] data, Dictionary parameters, CIShellContext context) {
			this.inMATFileName = (String) data[0].getData();
        }

        public Data[] execute() throws AlgorithmExecutionException {
        	File inMATFile = new File(inMATFileName);
			MATFileValidator validator = new MATFileValidator();
			try { 
				validator.validateMATFormat(inMATFile);
				if (validator.getValidationResult()){						
					return createOutData(inMATFile);
				} else {
					String message =
						"Sorry, your file does not comply with the .mat File Format Specification.\n"+
						"Please review the latest NET File Format Specification at "+
						"http://vlado.fmf.uni-lj.si/pub/networks/pajek/doc/pajekman.pdf, and update your file. \n\n"
						+ validator.getErrorMessages();
					throw new AlgorithmExecutionException(message);
				}
			} catch (FileNotFoundException e) {
				String message =
					"Couldn't find Pajek .mat file: " + e.getMessage();
				throw new AlgorithmExecutionException(message, e);
			} catch (IOException e) {
				String message = "File access error: " + e.getMessage();
				throw new AlgorithmExecutionException(message, e);
			}
        }

		private Data[] createOutData(File inMATFile) {
			Data[] dm = new Data[]{ new BasicData(
					inMATFile, MATFileProperty.MAT_MIME_TYPE) };
			dm[0].getMetadata().put(DataProperty.LABEL, "Pajek .mat file: " + inMATFileName);
			dm[0].getMetadata().put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
			return dm;
		}
    }
}
