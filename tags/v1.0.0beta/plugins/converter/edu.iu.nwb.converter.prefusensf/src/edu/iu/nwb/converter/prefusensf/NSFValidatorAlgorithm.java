package edu.iu.nwb.converter.prefusensf;

import java.io.File;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

public class NSFValidatorAlgorithm implements Algorithm {
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    
    public NSFValidatorAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
    }

    public Data[] execute() throws AlgorithmExecutionException {
		String nsfFileName = (String) data[0].getData();
		File nsfFile = new File(nsfFileName);

		try{
			Data[] returnData = new Data[] {new BasicData(nsfFile, "file:text/nsf")};
			returnData[0].getMetadata().put(DataProperty.LABEL, "NSF csv file: " + nsfFileName);
			returnData[0].getMetadata().put(DataProperty.TYPE, DataProperty.TABLE_TYPE);
			return returnData;
		}catch (SecurityException exception){
			throw new AlgorithmExecutionException(exception);
		}


	}
}