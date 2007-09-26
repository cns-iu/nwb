package edu.iu.nwb.converter.prefuseisi.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

import prefuse.data.Table;
import prefuse.data.io.CSVTableReader;
import prefuse.data.io.DataIOException;

/**
 * @author Russell Duhon
 */
public class PrefuseIsiReader implements Algorithm {
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    
    public PrefuseIsiReader(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
    }

    public Data[] execute() {
     	LogService logger = (LogService)context.getService(LogService.class.getName());
    	File file = (File) data[0].getData();

    	try{
    		ISITableReader tableReader = new ISITableReader();
			Table table= tableReader.readTable(new FileInputStream(file));
    		Data[] dm = new Data[] {new BasicData(table, Table.class.getName())};
    		dm[0].getMetaData().put(DataProperty.LABEL, "ISI Data: " + file);
            dm[0].getMetaData().put(DataProperty.TYPE, DataProperty.TEXT_TYPE);
            //logger.log(LogService.LOG_INFO, "" + table.getRowCount());
    		return dm;
    	}catch (SecurityException exception){
    		logger.log(LogService.LOG_ERROR, "SecurityException", exception);
    		
    	}catch (FileNotFoundException e){
    		logger.log(LogService.LOG_ERROR, "FileNotFoundException", e);
    		
    	} catch (UnsupportedEncodingException e) {
			// UTF-8 wasn't good
    		logger.log(LogService.LOG_ERROR, "UTF-8 is not supported.", e);
			
		} catch (IOException e) {
			logger.log(LogService.LOG_ERROR, "Problem reading data from file.", e);
		}
		
		return null;
    	
    }
}