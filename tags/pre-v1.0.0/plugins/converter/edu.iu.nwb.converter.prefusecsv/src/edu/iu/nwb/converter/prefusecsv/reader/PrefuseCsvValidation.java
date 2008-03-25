package edu.iu.nwb.converter.prefusecsv.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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

/**
 * @author Weixia(Bonnie) Huang 
 */
public class PrefuseCsvValidation implements AlgorithmFactory {
	protected void activate(ComponentContext ctxt) {
	}
	protected void deactivate(ComponentContext ctxt) {
	}

	public Algorithm createAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
		return new PrefuseCsvValidationAlg(data, parameters, context);
	}
	public MetaTypeProvider createParameters(Data[] data) {
		return null;
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

		public Data[] execute() {
			LogService logger = (LogService)context.getService(LogService.class.getName());

			String fileHandler = (String) data[0].getData();
			File inData = new File(fileHandler);

			try{
				Data[] dm = new Data[] {new BasicData(inData, "file:text/csv")};
				dm[0].getMetaData().put(DataProperty.LABEL, "Prefuse CSV file: " + fileHandler);
				dm[0].getMetaData().put(DataProperty.TYPE, DataProperty.TEXT_TYPE);
				return dm;
			}catch (SecurityException exception){
				logger.log(LogService.LOG_ERROR, "Might not be a CSV file. Got the following security exception");
				exception.printStackTrace();
				return null;
			}


		}
	}
}