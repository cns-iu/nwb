package edu.iu.nwb.analysis.extractattractors.algorithms;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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

import prefuse.data.Table;
import edu.iu.nwb.analysis.extractattractors.components.ExtractAttractorBasins;
import edu.iu.nwb.analysis.extractattractors.components.SimpleStateSpaceGraph;


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
		//Graph stateSpace;
		File stateSpace;
		SimpleStateSpaceGraph sssg;
		Object[] data = new Object[2];
		int graphIndex = getInputData(data);

		String nodeStates = this.getParameter("noStates");

		int numberOfStates = new Integer(nodeStates).intValue();
		if(numberOfStates < 2){
			throw new AlgorithmExecutionException("There must be at least two states to correctly analyze these expressions.\n");
		}	



		String labelColumn = this.parameters.get("labelColumn").toString();

		if(graphIndex > -1){
			
			stateSpace = (File)data[0];
			nameTable = (Table)data[1];
			try{
				sssg=getStateSpaceGraph(stateSpace);
			
				monitor.start(ProgressMonitor.WORK_TRACKABLE, sssg.getSize());
				ExtractAttractorBasins eab = new ExtractAttractorBasins(monitor);
			
				eab.weakComponentCalculation(sssg, nameTable, labelColumn, numberOfStates);
				File[] attractorBasins = eab.getBasins();
				File[] attractorTables = eab.getAttractors();
				File attractorRobustness = eab.getRobustnessFile();
				int[] attractorSizes = eab.getBasinSizes();
				Data[] returnData = new Data[(2*attractorBasins.length)+1];
				returnData[0] = constructData(this.data[0],attractorRobustness,"file:text/csv",DataProperty.TABLE_TYPE,"Table of the attractors coherency statistics");
				for(int i = 1; i < attractorBasins.length+1; i++){
					returnData[(2*i)-1] = constructData(this.data[0],attractorBasins[i-1],"file:text/nwb",DataProperty.NETWORK_TYPE,"Attractor Basin " + (i) + " of " + attractorSizes[i-1] + " nodes");
					returnData[(2*i)] = constructData(this.data[0],attractorTables[i-1],"file:text/csv",DataProperty.TABLE_TYPE,"Table view of Attractor " + (i));
				}

				monitor.done();
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
			if (dataFormat.equalsIgnoreCase("java.io.File")){
				dataList[0] = data[index].getData();
				graphIndex = index;
			}
			else if (dataFormat.equalsIgnoreCase("prefuse.data.Table"))
				dataList[1] = data[index].getData();
			else {
				logger.log (LogService.LOG_ERROR, 
						"Error: the data format of the input dataset is "+dataFormat+",\n"+
						"This algorithm requires the following data formats as inputs: \n"+
						"	file:text/nwb for a graph/network and \n"+
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

	private static SimpleStateSpaceGraph getStateSpaceGraph(File nwbFile) throws AlgorithmExecutionException{
		SimpleStateSpaceGraph sssg = new SimpleStateSpaceGraph();
		try{
			FileReader nwbFileReader = new FileReader(nwbFile);
			BufferedReader bufferedReader = new BufferedReader(nwbFileReader);
			String line;
			line = bufferedReader.readLine();
			line = bufferedReader.readLine();
			int nodeCount = 1;
			line = bufferedReader.readLine();
			String label ="";
			String[] attributes = line.split("\\s+");
			int i = 1;
			while(!attributes[i].endsWith("\"")){
				label+=attributes[i]+ " ";
				i++;
			}
			label+=attributes[i];
			int labelLength = ((label.length())-1)/2;
			sssg.setLabelSize(labelLength);
			while(!(line = bufferedReader.readLine()).startsWith("*DirectedEdges")){
				nodeCount++;
			}
			sssg.createEdgeLists(nodeCount);
			line = bufferedReader.readLine();
			String[] sourceTarget;
			while((line=bufferedReader.readLine()) != null){
				line.trim();
				sourceTarget = line.split("\\s+");
				int source = new Integer(sourceTarget[0]).intValue()-1;
				int target = new Integer(sourceTarget[1]).intValue()-1;

				sssg.addEdge(source, target);
			}
			bufferedReader.close();
		}catch (IOException ioe){
			throw new AlgorithmExecutionException("Error reading " + nwbFile.getName().toString(), ioe);
		}
	
		return sssg;
	}


}