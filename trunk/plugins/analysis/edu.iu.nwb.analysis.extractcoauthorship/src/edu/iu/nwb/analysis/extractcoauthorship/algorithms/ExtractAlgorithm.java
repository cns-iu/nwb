package edu.iu.nwb.analysis.extractcoauthorship.algorithms;

import java.util.Dictionary;

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
		ExtractNetworkfromMultivalues itn = new ExtractNetworkfromMultivalues(dataTable, "AU", "|",null);
		
		Data outputData = new BasicData(itn.constructGraph(), Graph.class.getName());
		Dictionary attributes = outputData.getMetaData();
		attributes.put(DataProperty.MODIFIED, new Boolean(true));
		attributes.put(DataProperty.PARENT, this.data[0]);
		attributes.put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
		return new Data[]{outputData};
		
	}

}
