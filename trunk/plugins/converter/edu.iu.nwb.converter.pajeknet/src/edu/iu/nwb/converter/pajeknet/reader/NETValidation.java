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
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.log.LogService;
import org.osgi.service.metatype.MetaTypeProvider;

import edu.iu.nwb.converter.pajeknet.common.NETFileProperty;
import edu.iu.nwb.converter.pajeknet.common.NETFileValidator;

/**
 * @author Timothy Kelley
 *
 */
public class NETValidation implements AlgorithmFactory {
	public Algorithm createAlgorithm(Data[] data, Dictionary parameters,
			CIShellContext context) {
		return new NETValidationAlgorithm(data, parameters, context);
	}

	public class NETValidationAlgorithm implements Algorithm {
		private String inNetFileName;

		public NETValidationAlgorithm(
				Data[] data, Dictionary parameters, CIShellContext context) {
			this.inNetFileName = (String) data[0].getData();
		}

		public Data[] execute() throws AlgorithmExecutionException {
			File inputFileData = new File(inNetFileName);
			NETFileValidator validator = new NETFileValidator();
			
			try{
				validator.validateNETFormat(inputFileData);
				
				if (validator.getValidationResult()) {
					return createOutData(inNetFileName, inputFileData);

				} else {
					String message =
						"Sorry, your file does not comply with the NET File Format Specification.\n"
						+ "Please review the latest NET File Format Specification at "
						+ "http://vlado.fmf.uni-lj.si/pub/networks/pajek/doc/pajekman.pdf, and update your file. \n"
						+ validator.getErrorMessages();
					throw new AlgorithmExecutionException(message);
				}

			} catch (FileNotFoundException e) {
				String message =
					"Couldn't find Pajek .net file: " + e.getMessage();
				throw new AlgorithmExecutionException(message, e);
			} catch (IOException e) {
				String message = "File access error: " + e.getMessage();
				throw new AlgorithmExecutionException(message, e);
			}
		}

		private Data[] createOutData(String fileHandler, File inputFileData) {
			Data[] validationData = new Data[] {new BasicData(inputFileData, NETFileProperty.NET_MIME_TYPE)};
			validationData[0].getMetadata().put(DataProperty.LABEL, "Pajek .net file: " + fileHandler);
			validationData[0].getMetadata().put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
			return validationData;
		}
	}
}
