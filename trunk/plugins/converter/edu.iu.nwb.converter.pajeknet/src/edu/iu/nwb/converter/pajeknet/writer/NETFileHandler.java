/**
 *
 */
package edu.iu.nwb.converter.pajeknet.writer;

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
import org.osgi.service.component.ComponentContext;
import org.osgi.service.log.LogService;
import org.osgi.service.metatype.MetaTypeProvider;
import org.osgi.service.metatype.MetaTypeService;

import edu.iu.nwb.converter.pajeknet.common.NETFileProperty;
import edu.iu.nwb.converter.pajeknet.common.NETFileValidator;

/**
 * @author Timothy Kelley
 * 
 */
public class NETFileHandler implements AlgorithmFactory {
	public Algorithm createAlgorithm(Data[] data, Dictionary parameters,
			CIShellContext context) {
		return new NETFileHandlerAlg(data, parameters, context);
	}

	public class NETFileHandlerAlg implements Algorithm {
		private Object inData;
		private String inFormat;

		public NETFileHandlerAlg(Data[] data, Dictionary parameters,
				CIShellContext context) {
			inData = data[0].getData();
			inFormat = data[0].getFormat();
		}

		public Data[] execute() throws AlgorithmExecutionException {
			if (inData instanceof File) {
				if (inFormat.equals(NETFileProperty.NET_MIME_TYPE)) {
					NETFileValidator validator = new NETFileValidator();
					
					try {
						validator.validateNETFormat((File) inData);
						
						if (validator.getValidationResult()) {
							return new Data[] { new BasicData(inData,
									NETFileProperty.NET_FILE_TYPE) };
						} else {
							throw new AlgorithmExecutionException(
								"Sorry, your file does not seem to comply with "
									+ "the Pajek .net File Format Specification."
									+ "We are writing it out anyway.\n"
									+ "Please review the latest NET File Format "
									+ "Specification at "
									+ "http://vlado.fmf.uni-lj.si/pub/networks/pajek/doc/pajekman.pdf"
									+ ", and update your file."
									+ validator.getErrorMessages());
						}
					} catch (FileNotFoundException e) {
						String message =
							"Couldn't find Pajek .net file: " + e.getMessage();
						throw new AlgorithmExecutionException(message, e);
					} catch (IOException e) {
						String message = "File access error: " + e.getMessage();
						throw new AlgorithmExecutionException(message, e);
					}
				} else {
					String message =
						"Expected "
						+ NETFileProperty.NET_MIME_TYPE
						+ ", but the input format is " + inFormat;
					throw new AlgorithmExecutionException(message);
				}
			} else {
				String message =
					"Expected a File, but the input data is "
					+ inData.getClass().getName();
				throw new AlgorithmExecutionException(message);
			}
		}
	}

}
