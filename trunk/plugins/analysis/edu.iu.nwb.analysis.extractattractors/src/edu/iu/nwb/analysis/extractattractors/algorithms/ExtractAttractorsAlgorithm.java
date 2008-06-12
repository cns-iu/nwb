package edu.iu.nwb.analysis.extractattractors.algorithms;

import java.io.File;
import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.ProgressMonitor;
import org.cishell.framework.algorithm.ProgressTrackable;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

import prefuse.data.Graph;
import prefuse.data.Table;
import edu.iu.nwb.analysis.extractattractors.components.ExtractAttractorBasins;


public class ExtractAttractorsAlgorithm implements Algorithm, ProgressTrackable {
	Data[] data;
	Dictionary parameters;
	CIShellContext context;
	LogService logger;
	ProgressMonitor monitor;

	public ProgressMonitor getProgressMonitor() {
		// TODO Auto-generated method stub
		return this.monitor;
	}

	public void setProgressMonitor(ProgressMonitor monitor) {
		this.monitor = monitor;
	}

	public ExtractAttractorsAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
		this.data = data;
		this.parameters = parameters;
		this.context = context;
		this.logger = (LogService)context.getService(LogService.class.getName());
	}

	public Data[] execute() throws AlgorithmExecutionException{
		Table nameTable;
		Graph stateSpace;
		Object[] data = new Object[2];
		int graphIndex = getInputData(data);
		
		//dependencyGraph = (Graph)data[0].getData();
		
/*
		String nodeStates = this.getParameter(ParameterDefinitions.NODESTATES);

		int numberOfStates = new Integer(nodeStates).intValue();
		if(numberOfStates < 2){
			throw new AlgorithmExecutionException("There must be at least two states to correctly analyze these expressions.\n");
		}	
		
		
*/
		
		String labelColumn = this.parameters.get("labelColumn").toString();
		
		if(graphIndex > -1){
			stateSpace = (Graph)data[0];
			nameTable = (Table)data[1];
		try{
			
			monitor.start(ProgressMonitor.WORK_TRACKABLE, stateSpace.getNodeCount());
			ExtractAttractorBasins eab = new ExtractAttractorBasins();
			eab.extractAttractorBasins(stateSpace, 0, true,nameTable,labelColumn);
			File[] attractorBasins = eab.getBasins();
			Table[] attractorTables = eab.getAttractors();
			int[] attractorSizes = eab.getBasinSizes();
			Data[] returnData = new Data[2*attractorBasins.length];
			for(int i = 0; i < attractorBasins.length; i++){
				returnData[2*i] = constructData(this.data[0],attractorBasins[i],"file:text/nwb",DataProperty.NETWORK_TYPE,"Attractor Basin " + (i+1) + " of " + attractorSizes[i] + " nodes");
				returnData[(2*i)+1] = constructData(this.data[0],attractorTables[i],prefuse.data.Table.class.toString(),DataProperty.MATRIX_TYPE,"Table view of Attractor " + (i+1));
				}
			
			return returnData;
			
			
		}catch(InterruptedException ie){
			throw new AlgorithmExecutionException("There was an error in calculating the basins of attraction.",ie);
		}			

	}
	else{
		throw new AlgorithmExecutionException("The correct input data was not provided.");
	}
	}

	private Data constructData(Data parent, Object obj, String className, String type, String label){
		Data outputData = new BasicData(obj,className);
		Dictionary dataAttributes = outputData.getMetadata();
		dataAttributes.put(DataProperty.MODIFIED, new Boolean(true));
		dataAttributes.put(DataProperty.PARENT, parent);
		dataAttributes.put(DataProperty.TYPE, type);
		dataAttributes.put(DataProperty.LABEL,label);

		return outputData;
	}

	private String getParameter(String s){
		try{
			return this.parameters.get(s).toString();
		}catch(NullPointerException npe){
			return null;
		}
	}

	private static boolean intStringToBoolean(String s){
		int value = new Integer(s).intValue();
		if(value >= 1){
			return true;
		}
		return false;
	}

	private static String[] handleOptionalData(boolean b, String s, String errorMessage) throws AlgorithmExecutionException{
		String[] returnValue = null;
		if(!b){
			if(s.trim().equals("") || s == null){
				throw new AlgorithmExecutionException(errorMessage);
			}
			returnValue = s.split("\\s+");
		}
		return returnValue;
	}
	
	 private int getInputData(Object[] dataList){
		 int graphIndex = -1;
	    	if (data.length != 2) {
	      	  logger.log (LogService.LOG_ERROR, 
					"Error: This algorithm requires two datasets as inputs: a graph/network "+
					"and a node list with the instruction of merging nodes. \n");
	      	  return graphIndex;
	        }    	
	    	for (int index =0; index<data.length; index++){
	    		String dataFormat = data[index].getData().getClass().getName();    		
	    		if (dataFormat.equalsIgnoreCase("prefuse.data.Graph")){
	    			dataList[0] = data[index].getData();
	    			graphIndex = index;
	    		}
	    		else if (dataFormat.equalsIgnoreCase("prefuse.data.Table"))
	    			dataList[1] = data[index].getData();
	    		else {
	    			logger.log (LogService.LOG_ERROR, 
	  				"Error: the data format of the input dataset is "+dataFormat+",\n"+
	  				"This algorithm requires the following data formats as inputs: \n"+
	  				"	prefuse.data.Graph for a graph/network and \n"+
	  				"	prefuse.data.Table for a node list. \n");
	    			return -1;
	    		}
	    	}
	    	if (dataList[1]==null){
	    		logger.log (LogService.LOG_ERROR, 
	    		"Error: This algorithm did not get prefuse.data.Table for a node list as one of the inputs. \n");
	    		return -1;
	    	}    	
	    	if(dataList[0] == null){
	    		logger.log (LogService.LOG_ERROR, 
	    		"Error: This algorithm did not get prefuse.data.Graph for a graph/network as one of the inputs. \n");
	    		return -1;
	    	}
	    	return graphIndex;
	    }

}