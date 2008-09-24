package edu.iu.nwb.converter.prefuseisi.reader;

import java.io.File;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

/**
 * @author Weixia(Bonnie) Huang 
 */
public class PrefuseIsiValidation implements AlgorithmFactory {
	public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
		return new PrefuseCsvValidationAlg(data, parameters, context);
	}

	public class PrefuseCsvValidationAlg implements Algorithm {
		Data[] data;
		Dictionary parameters;
		CIShellContext context;

		public PrefuseCsvValidationAlg(Data[] data, Dictionary parameters, CIShellContext context) {
			this.data = data;
			this.parameters = parameters;
			this.context = context;
		}

		public Data[] execute() throws AlgorithmExecutionException {
			String fileHandler = (String) data[0].getData();
			File inData = new File(fileHandler);

			try{
				Data[] dm = new Data[] {new BasicData(inData, "file:text/isi")};
				dm[0].getMetadata().put(DataProperty.LABEL, "Prefuse ISI file: " + fileHandler);
				dm[0].getMetadata().put(DataProperty.TYPE, DataProperty.TABLE_TYPE);
				return dm;
			}catch (SecurityException exception){
				throw new AlgorithmExecutionException(exception);
			}


		}
	}
}