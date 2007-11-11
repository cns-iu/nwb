package edu.iu.nwb.analysis.extractcoauthorship.algorithms;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Dictionary;
import java.util.Iterator;
import java.util.Properties;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

import prefuse.data.Graph;
import edu.iu.nwb.analysis.extractmultivaluednetwork.components.ExtractNetworkfromMultivalues;

public class ExtractAlgorithm implements Algorithm{
	Data[] data;
    Dictionary parameters;
    CIShellContext ciContext;
    LogService logger;
   
	
	
    public ExtractAlgorithm(Data[] dm, Dictionary parameters, CIShellContext cContext){
    	this.data = dm;
		this.parameters = parameters;
		this.ciContext = cContext;
		logger = (LogService)ciContext.getService(LogService.class.getName());
		
    }
    
    
	public Data[] execute() {
		// TODO Auto-generated method stub
		
		prefuse.data.Table dataTable = (prefuse.data.Table)this.data[0].getData();
	
		
		ClassLoader loader = getClass().getClassLoader();
	
		InputStream in = loader.getResourceAsStream("/edu/iu/nwb/analysis/extractcoauthorship/metadata/Operations.properties");
		
		Properties metaData = new Properties();
		try{
			metaData.load(in);
		}catch(FileNotFoundException fnfe){
			logger.log(LogService.LOG_ERROR, fnfe.getMessage());
		}catch(IOException ie){
			logger.log(LogService.LOG_ERROR, ie.getMessage());
		}
		
		Data outputData = new BasicData(ExtractNetworkfromMultivalues.constructGraph(dataTable,"AU","" +
				"|",metaData,false), Graph.class.getName());
		Dictionary attributes = outputData.getMetaData();
		attributes.put(DataProperty.MODIFIED, new Boolean(true));
		attributes.put(DataProperty.PARENT, this.data[0]);
		attributes.put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
		attributes.put(DataProperty.LABEL, "Extracted Co-authorship Network");
		return new Data[]{outputData};
		
	}

}
