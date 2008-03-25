package edu.iu.nwb.converter.prefuserefer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.Dictionary;

import javax.xml.stream.events.Characters;

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
    
    private LogService log;
    private ReferUtil util;
    
    public ReferValidation(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
        this.log = (LogService) context.getService(LogService.class.getName());
        
        this.util = new ReferUtil(log);
    }

    public Data[] execute() {
    	

		String fileHandler = (String) data[0].getData();
		File inData = new File(fileHandler);

		if (isValid(inData)) {
			try{
				Data[] dm = new Data[] {new BasicData(inData, "file:text/referbib")};
				dm[0].getMetaData().put(DataProperty.LABEL, "EndNote reference file: " + fileHandler);
				dm[0].getMetaData().put(DataProperty.TYPE, DataProperty.MATRIX_TYPE);
				return dm;
			}catch (SecurityException exception){
				log.log(LogService.LOG_ERROR, "SecurityException", exception);
				exception.printStackTrace();
				return null;
			}
		} else {
			return null;
		}
    }
    
    //not fool-proof. No smoking gun for validating the file without basically reading the whole thing.
    //if the file has 0 or more empty lines followed by a line starting with %, it's good.
    //else, it's not good.
    private boolean isValid(File referFile) {
    	BufferedReader fileReader = util.makeReader(referFile);
    	try {
    		while (true) {
    			String line = fileReader.readLine();
    			if (util.isEndOfFile(line)) {
    				return false;
    			} else if (util.isBlank(line)) {	
    				continue;
    			} else if (util.startsWithFieldMarker(line)) {
    				return true;
    			} else {
    				return false;
    			}
    		}
    	} catch (IOException e1) {
    		this.log.log(LogService.LOG_ERROR, "IO Error while attempting to validate potential refer file", e1);
    		return false;
    	}
    }
}