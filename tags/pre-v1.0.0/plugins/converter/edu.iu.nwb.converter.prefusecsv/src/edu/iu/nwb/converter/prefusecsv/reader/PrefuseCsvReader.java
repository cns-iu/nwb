package edu.iu.nwb.converter.prefusecsv.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
public class PrefuseCsvReader implements Algorithm {
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    
    public PrefuseCsvReader(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
    }

    public Data[] execute() {
     	LogService logger = (LogService)context.getService(LogService.class.getName());
    	File file = (File) data[0].getData();

    	try{
    		CSVTableReader tableReader = new CSVTableReader();
    		tableReader.setHasHeader(true);
			Table table= tableReader.readTable(new FileInputStream(file));
    		Data[] dm = new Data[] {new BasicData(table, Table.class.getName())};
    		dm[0].getMetaData().put(DataProperty.LABEL, "Prefuse Table: " + file);
            dm[0].getMetaData().put(DataProperty.TYPE, DataProperty.TEXT_TYPE);
    		return dm;
    	}catch (DataIOException dioe){
    		logger.log(LogService.LOG_ERROR, "DataIOException", dioe);
    		return null;
    	}catch (SecurityException exception){
    		logger.log(LogService.LOG_ERROR, "SecurityException", exception);
    		return null;
    	}catch (FileNotFoundException e){
    		logger.log(LogService.LOG_ERROR, "FileNotFoundException", e);
    		return null;
    	}
    	
    }
}