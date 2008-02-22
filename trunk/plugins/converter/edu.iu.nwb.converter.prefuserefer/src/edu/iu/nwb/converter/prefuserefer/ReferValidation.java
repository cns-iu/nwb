package edu.iu.nwb.converter.prefuserefer;

import java.io.File;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

public class ReferValidation implements Algorithm {
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    
    public ReferValidation(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
    }

    public Data[] execute() {
    	LogService logger = (LogService) context.getService(LogService.class.getName());

		String fileHandler = (String) data[0].getData();
		File inData = new File(fileHandler);

		try{
			Data[] dm = new Data[] {new BasicData(inData, "file:text/referbib")};
			dm[0].getMetaData().put(DataProperty.LABEL, "Endnote reference file: " + fileHandler);
			dm[0].getMetaData().put(DataProperty.TYPE, DataProperty.TEXT_TYPE);
			return dm;
		}catch (SecurityException exception){
			logger.log(LogService.LOG_ERROR, "SecurityException", exception);
			exception.printStackTrace();
			return null;
		}
    }
}