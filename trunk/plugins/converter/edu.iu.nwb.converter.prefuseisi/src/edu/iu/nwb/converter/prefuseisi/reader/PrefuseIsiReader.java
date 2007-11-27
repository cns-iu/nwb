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
import edu.iu.nwb.analysis.isidupremover.IsiDupRemover;
import edu.iu.nwb.analysis.isidupremover.TablePair;

/**
 * @author Russell Duhon
 */
public class PrefuseIsiReader implements Algorithm {
	
	private static final boolean REMOVE_DUPLICATE_PUBLICATIONS = false;
	private static final boolean NORMALIZE_AUTHOR_NAMES = true;
	
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    
    private LogService log;
    private IsiDupRemover dupRemover;
    
    public PrefuseIsiReader(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
        
     	this.log = (LogService)context.getService(LogService.class.getName());
    }

    public Data[] execute() {

    	File file = (File) data[0].getData();
    	
    	try{
    		ISITableReader tableReader = new ISITableReader(this.log, NORMALIZE_AUTHOR_NAMES);
			
    		if (NORMALIZE_AUTHOR_NAMES) {
    			log.log(LogService.LOG_INFO, "");
    			log.log(LogService.LOG_INFO, "Normalizing author names...");
    		}
    		
    		Table tableWithDups = tableReader.readTable(new FileInputStream(file));
		  	
            Table tableToReturn;     
			if (REMOVE_DUPLICATE_PUBLICATIONS) {
			
				log.log(LogService.LOG_INFO, "");
				log.log(LogService.LOG_INFO, "Removing duplicate publications...");
				log.log(LogService.LOG_INFO, "----------------------------------------");
				log.log(LogService.LOG_INFO, "");
				
				if (dupRemover == null) {
				dupRemover = new IsiDupRemover();
				} 
				
				TablePair noDupandDupTables = 
					dupRemover.removeDuplicatePublications(tableWithDups, log, false);
				Table tableWithoutDups = noDupandDupTables.getNoDupTable();
				
				tableToReturn = tableWithoutDups;
			} else {
				tableToReturn = tableWithDups;
			}
			
			Data[] tableToReturnData = 
				new Data[] {new BasicData(tableToReturn, Table.class.getName())};
    		tableToReturnData[0].getMetaData().put(DataProperty.LABEL, "ISI Data: " + file);
            tableToReturnData[0].getMetaData().put(DataProperty.TYPE, DataProperty.TEXT_TYPE);
			
    		return tableToReturnData;
    		
    	}catch (SecurityException exception){
    		log.log(LogService.LOG_ERROR, "SecurityException", exception);
    		
    	}catch (FileNotFoundException e){
    		log.log(LogService.LOG_ERROR, "FileNotFoundException", e);
    		
    	} catch (UnsupportedEncodingException e) {
			// UTF-8 wasn't good
    		log.log(LogService.LOG_ERROR, "UTF-8 is not supported.", e);
			
		} catch (IOException e) {
			log.log(LogService.LOG_ERROR, "Problem reading data from file.", e);
		}
		
		return null;
    	
    }
}