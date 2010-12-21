package edu.iu.nwb.analysis.extractattractors.algorithms;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
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
import edu.iu.nwb.analysis.extractattractors.components.BigSimpleStateSpaceGraph;
import edu.iu.nwb.analysis.extractattractors.components.ExtractAttractorBasins;
import edu.iu.nwb.analysis.extractattractors.components.SimpleStateSpaceGraph;
import edu.iu.nwb.analysis.extractattractors.components.SmallSimpleStateSpaceGraph;
import edu.iu.nwb.analysis.extractattractors.containers.BigArray;


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
				int systemSize = nameTable.getRowCount();
				sssg = getStateSpaceGraphFromEdge(stateSpace,systemSize,numberOfStates);
				System.gc();
				monitor.start(ProgressMonitor.WORK_TRACKABLE, 100);
				ExtractAttractorBasins eab = new ExtractAttractorBasins(monitor);
			
				eab.computeWeakComponents(sssg, nameTable, labelColumn, numberOfStates);
				File[] attractorBasins = eab.getBasins();
				File[] attractorTables = eab.getAttractors();
				File attractorRobustness = eab.getRobustnessFile();
				BigInteger[] attractorSizes = eab.getBasinSizes();
				
				ArrayList<Data> dataValues = new ArrayList<Data>();
				dataValues.add(constructData(this.data[0],attractorRobustness,"file:text/csv",DataProperty.TABLE_TYPE,"Table of the attractors coherency statistics"));
				for(int i = 0; i < attractorBasins.length; i++){
					if(attractorBasins[i] != null){
						dataValues.add(constructData(this.data[0],attractorBasins[i],"file:text/nwb",DataProperty.NETWORK_TYPE,"Attractor Basin " + (i+1) + " of " + attractorSizes[i] + " nodes"));
					}else
						this.logger.log(LogService.LOG_INFO, "Attractor Basin " + (i+1) + " was too large to include in the NWB File Format.");
					dataValues.add(constructData(this.data[0],attractorTables[i],"file:text/csv",DataProperty.TABLE_TYPE,"Table view of Attractor " + (i+1)));
				}
				dataValues.trimToSize();
				
				monitor.done();
				return dataValues.toArray(new Data[dataValues.size()]);


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
	
	private static SimpleStateSpaceGraph getStateSpaceGraphFromEdge(File edgeFile,int systemSize, int numberOfStates) throws AlgorithmExecutionException{
		SimpleStateSpaceGraph sssg = null;
		try{
			FileReader edgeFileReader = new FileReader(edgeFile);
			BufferedReader bufferedReader = new BufferedReader(edgeFileReader);
			String line;
			
			
			BigInteger size = BigInteger.ZERO;
			
			while((line = bufferedReader.readLine()) != null){
				if(line.length() > 0 && !line.equalsIgnoreCase("") && !line.matches("[a-zA-Z]")){
					
					size = size.add(BigInteger.ONE);				
				}
			}
			
			bufferedReader.close();
			if(size.compareTo(BigArray.maxInteger) <= 0)
				sssg = new SmallSimpleStateSpaceGraph(numberOfStates,systemSize);
			else
				sssg = new BigSimpleStateSpaceGraph(numberOfStates,systemSize);
			
			edgeFileReader = new FileReader(edgeFile);
			bufferedReader = new BufferedReader(edgeFileReader);
			
			sssg.createEdgeLists(size);
			
			
			
			while((line = bufferedReader.readLine()) != null){
				if(line.length() > 0 && !line.equalsIgnoreCase("") && !line.matches("[a-zA-Z]")){
					String[] sourceTarget = line.split("\\s+");
					String source = sourceTarget[0].replace("\"", "");
					String target = sourceTarget[1].replace("\"", "");
					
					
					sssg.addEdge(new BigInteger(source), new BigInteger(target));
									
				}
			}
			
			bufferedReader.close();
			
		}catch (IOException ioe){
			throw new AlgorithmExecutionException("Error reading " + edgeFile.getName().toString(), ioe);
		}
		return sssg;
	}
 
}