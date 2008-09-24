package edu.iu.nwb.analysis.directedknn.algorithm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeSet;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.ProgressMonitor;
import org.cishell.framework.algorithm.ProgressTrackable;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;

import edu.iu.nwb.analysis.java.directedknn.components.DirectedKNNArrayAndMapContainer;

public class DirectedKNNAlgorithm implements Algorithm,ProgressTrackable {
	private Data[] data;
	private Dictionary parameters;
	private CIShellContext context;
	private static String LINE_SEP = "line.separator";
	private ProgressMonitor progMonitor;
	
	public DirectedKNNAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
		this.data = data;
		this.parameters = parameters;
		this.context = context;
	}

	public Data[] execute() throws AlgorithmExecutionException {
		try{
			File inData = (File)this.data[0].getData();
			FileReader fr = new FileReader(inData);
			BufferedReader nwbFileReader = new BufferedReader(fr);
			this.progMonitor.start(ProgressMonitor.WORK_TRACKABLE, 350);
			int totalNodes = preProcessNWBFileNodes(nwbFileReader);
			this.progMonitor.worked(100);
			Map nodeToInNeighbors = new HashMap(totalNodes);
			Map nodeToOutNeighbors = new HashMap(totalNodes);
			DirectedKNNArrayAndMapContainer knnMapContainer = new DirectedKNNArrayAndMapContainer();
			knnMapContainer = preProcessNWBFileEdges(nwbFileReader,knnMapContainer, nodeToInNeighbors,nodeToOutNeighbors,totalNodes);
			this.progMonitor.worked(200);
			nwbFileReader.close();
			fr = new FileReader(inData);
			nwbFileReader = new BufferedReader(fr);
			Map inDegreeTotals = knnMapContainer.getInDegreeTotals();
			Map outDegreeTotals = knnMapContainer.getOutDegreeTotals();
			File returnNetworkFile = processNWBFile(nwbFileReader,nodeToInNeighbors,nodeToOutNeighbors,knnMapContainer,totalNodes,this.progMonitor,200);
			File returnKInInFile = createPlotFile(knnMapContainer.getKInInMap(),inDegreeTotals,totalNodes,"Indegree\t|\tK_In_In");
			File returnKInOutFile = createPlotFile(knnMapContainer.getKInOutMap(),inDegreeTotals,totalNodes,"Indegree\t|\tK_In_Out");
			File returnKOutInFile = createPlotFile(knnMapContainer.getKOutInMap(),outDegreeTotals,totalNodes,"Outdegree\t|\tK_Out_In");
			File returnKOutOutFile = createPlotFile(knnMapContainer.getKOutOutMap(),outDegreeTotals,totalNodes,"Outdegree\t|\tK_Out_Out");

			Data networkData = constructData(this.data[0],returnNetworkFile,"file:text/nwb",DataProperty.NETWORK_TYPE,"Network annotated with KNN analysis");
			Data kInInData = constructData(this.data[0],returnKInInFile,"file:text/grace",DataProperty.PLOT_TYPE,"Indegree of nodes correlated with indegree of in-neighbors normalized by expected indegree");
			Data kInOutData = constructData(this.data[0],returnKInOutFile,"file:text/grace",DataProperty.PLOT_TYPE,"Indegree of nodes correlated with outdegree of in-neighbors normalized by expected outdegree");
			Data kOutInData = constructData(this.data[0],returnKOutInFile,"file:text/grace",DataProperty.PLOT_TYPE,"Outdegree of nodes correlated with indegree of out-neighbors normalized by expected indegree");
			Data kOutOutData = constructData(this.data[0],returnKOutOutFile,"file:text/grace",DataProperty.PLOT_TYPE,"Outdegree of nodes correlated with outdegree of out-neighbors normalized by expected outdegree");
			this.progMonitor.done();
			return new Data[] {networkData,kInInData,kInOutData,kOutInData,kOutOutData};
		}catch(FileNotFoundException fnfe){
			throw new AlgorithmExecutionException("Unable to find the file.",fnfe);
		}catch(IOException ioe){
			throw new AlgorithmExecutionException("Error closing the file input stream.",ioe);
		}
	}

	private static Data constructData(Data parent, Object obj, String className, String type, String label){
		Data outputData = new BasicData(obj,className);
		Dictionary dataAttributes = outputData.getMetadata();
		dataAttributes.put(DataProperty.MODIFIED, new Boolean(true));
		dataAttributes.put(DataProperty.PARENT, parent);
		dataAttributes.put(DataProperty.TYPE, type);
		dataAttributes.put(DataProperty.LABEL,label);

		return outputData;
	}

	private static File processNWBFile(BufferedReader sourceFileReader, Map nodeToInNeighbors, Map nodeToOutNeighbors,DirectedKNNArrayAndMapContainer dkaamc, int totalNodes, ProgressMonitor monitor,int worked) throws AlgorithmExecutionException{
		File targetFile = null;
		int workDone = 200;
		int workToDo = 2*dkaamc.getUndirectedDegree();
		int step = Math.max(1, workToDo/100);
		try{
			targetFile = File.createTempFile(new Long(new Date().getTime()).toString(), ".nwb");
		}catch(IOException ioe){
			throw new AlgorithmExecutionException("Unable to create the annotated nwb file.",ioe);
		}
		try{
			FileWriter fw = new FileWriter(targetFile);
			BufferedWriter targetNWBFileWriter = new BufferedWriter(fw);
			String sourceLine;


			while(!(sourceLine = sourceFileReader.readLine().trim()).startsWith("id*int")){
				targetNWBFileWriter.flush();
				targetNWBFileWriter.write(sourceLine+System.getProperty(LINE_SEP));
			}
			sourceLine = sourceLine+"\tkInIn*float\tkInOut*float\tkOutIn*float\tkOutOut*float";
			targetNWBFileWriter.write(sourceLine+System.getProperty(LINE_SEP));

			while((sourceLine = sourceFileReader.readLine()) != null){
				targetNWBFileWriter.flush();
				sourceLine = sourceLine.trim();
				if(sourceLine.toLowerCase().startsWith("*undirected") || sourceLine.toLowerCase().startsWith("*directed")){
					targetNWBFileWriter.write(sourceLine+System.getProperty(LINE_SEP));
					break;
				}
					
				if(!sourceLine.startsWith("#")){
					String[] attributeValues = sourceLine.split("\\s+");
					Integer id = new Integer(attributeValues[0]);
					double[] kNNs = calculateKNNs(id,dkaamc,nodeToInNeighbors, nodeToOutNeighbors,totalNodes);
					targetNWBFileWriter.write(sourceLine+"\t"+kNNs[0]+"\t"+kNNs[1]+"\t"+kNNs[2]+"\t"+kNNs[3]+System.getProperty(LINE_SEP));
					if((worked-workDone) % step == 0)
						monitor.worked(workDone++);
				}	
			}
			
			while((sourceLine = sourceFileReader.readLine())!=null){
				targetNWBFileWriter.write(sourceLine+System.getProperty(LINE_SEP));
			}
			targetNWBFileWriter.flush();
			targetNWBFileWriter.close();

			return targetFile;
		}catch(IOException ioe2){
			throw new AlgorithmExecutionException("Error writing to target file.",ioe2);
		}
	}
	
	private static File createPlotFile(Map KNNMap, Map degreeCountsMap, int totalNodes, String label) throws AlgorithmExecutionException{
		File targetFile = null;
		try{
			targetFile = File.createTempFile(new Long(new Date().getTime()).toString(),	 ".txt");
			FileWriter plotFileWriter = new FileWriter(targetFile);
			BufferedWriter bufferedFileWriter = new BufferedWriter(plotFileWriter);
			bufferedFileWriter.write("#\t"+label+System.getProperty(LINE_SEP));
			double expectedDegree = (double)((double)totalNodes*((double)totalNodes-1))/2;
			expectedDegree = totalNodes/expectedDegree;
			expectedDegree = totalNodes*expectedDegree;
			TreeSet degrees = new TreeSet(KNNMap.keySet());
			for(Iterator it = degrees.iterator(); it.hasNext();){
				Integer degree = (Integer)it.next();
				Double knn = (Double)KNNMap.get(degree);
				Integer totals = (Integer)degreeCountsMap.get(degree);
				double correlation = knn.doubleValue()/totals.doubleValue();
				correlation = correlation/expectedDegree;
				bufferedFileWriter.write(degree + "\t"+correlation+System.getProperty(LINE_SEP));
			}
			bufferedFileWriter.flush();
			bufferedFileWriter.close();
			return targetFile;
		}catch(IOException ioe){
			throw new AlgorithmExecutionException("Unable to create the file for indegree correlation with indegree of in-neighbors.",ioe);
		}
	}
	
	private static double[] calculateKNNs(LinkedList neighbors, Map inNeighborsMap, Map outNeighborsMap){
		double[] results = new double[2];
		
		Map inDegreeTotals;
		Map outDegreeTotals;

		inDegreeTotals = new HashMap();
		outDegreeTotals = new HashMap();
		for(Iterator it = neighbors.iterator(); it.hasNext();){

			Integer neighborId = (Integer)it.next();

			int inDegree = calculateDegree(neighborId,inNeighborsMap);
			int outDegree = calculateDegree(neighborId,outNeighborsMap);
			DirectedKNNArrayAndMapContainer.updateDegreeTotals(inDegree,inDegreeTotals);
			DirectedKNNArrayAndMapContainer.updateDegreeTotals(outDegree, outDegreeTotals);
			
		}
		
		results[0] = calculateKNN(inDegreeTotals,neighbors.size());
		results[1] = calculateKNN(outDegreeTotals,neighbors.size());
		
		return results;
		
	}
	
	private static double calculateKNN(Map degreeTotals, int numberOfNeighbors){
		double knn = 0;
		for(Iterator it = degreeTotals.keySet().iterator(); it.hasNext();){
			Integer degree = ((Integer)it.next());
			Integer count = (Integer)degreeTotals.get(degree);
			knn += (degree.doubleValue()*count.doubleValue())/numberOfNeighbors;
		}
		return knn;
	}

	private static double[] calculateKNNs(Integer nodeId, DirectedKNNArrayAndMapContainer dkaamc, Map inNeighborsMap, Map outNeighborsMap, int totalNodes){
		double kInIn = 0;
		double kInOut = 0;
		double kOutIn = 0;
		double kOutOut = 0;

		double[] results = new double[4];

		LinkedList inNeighbors = (LinkedList)inNeighborsMap.get(nodeId);
		LinkedList outNeighbors = (LinkedList)outNeighborsMap.get(nodeId);
		
	
		
		if(inNeighbors != null){
			double[] kIns = calculateKNNs(inNeighbors,inNeighborsMap,outNeighborsMap);
			kInIn = kIns[0];
			kInOut = kIns[1];
			dkaamc.updateInDegreeValues(inNeighbors.size(), kInIn, kInOut);
		}
		if(outNeighbors != null){
			double[] kOuts = calculateKNNs(outNeighbors,inNeighborsMap,outNeighborsMap);
			kOutIn = kOuts[0];
			kOutOut = kOuts[1];
		
			dkaamc.updateOutDegreeValues(outNeighbors.size(), kOutIn, kOutOut);
		}
		results[0] = kInIn;
		results[1] = kInOut;
		results[2] = kOutIn;
		results[3] = kOutOut;

		return results;
	}

	private static int calculateDegree(Integer nodeId, Map neighborMap){
		LinkedList neighbors = (LinkedList)neighborMap.get(nodeId);
		if(neighbors == null){
			return 0;
		}else{
			return neighbors.size();
		}

	}

	private static int preProcessNWBFileNodes(BufferedReader nwbFileReader)throws AlgorithmExecutionException{
		try{
			nwbFileReader.readLine();
			String line = nwbFileReader.readLine();
			String[] attributes = line.split("\\s+");
			int totalNodes = 0;

			checkAttributes(attributes);

			while(!(line = nwbFileReader.readLine()).startsWith("*DirectedEdges")){
				if(line == null || line.equalsIgnoreCase("*undirectededges"))
					throw new AlgorithmExecutionException("This algorithm should be used on Directed Networks.");
				totalNodes++;
			}
			return totalNodes;
		}catch(IOException ioe){
			throw new AlgorithmExecutionException("Errors reading the file.",ioe);
		}
	}

	private static DirectedKNNArrayAndMapContainer preProcessNWBFileEdges(BufferedReader nwbFileReader, DirectedKNNArrayAndMapContainer mapContainer, Map nodeToInNeighbors, Map nodeToOutNeighbors, int numberOfNodes) throws AlgorithmExecutionException{
		try{
			nwbFileReader.readLine();
			String line;
			int size;
			while((line = nwbFileReader.readLine()) != null){
				if(line.toLowerCase().startsWith("*undirectededges"))
					return mapContainer;
				if(!line.startsWith("#")){
					String[] sourceTargetArray = line.split("\\s+");
					Integer source = new Integer(sourceTargetArray[0]);
					Integer target = new Integer(sourceTargetArray[1]);
					addEdge(source,target,nodeToInNeighbors,nodeToOutNeighbors);
					mapContainer.updateDegree();
				}
			}

			for(Iterator it = nodeToOutNeighbors.keySet().iterator(); it.hasNext();){
				LinkedList outNeighbors = (LinkedList)nodeToOutNeighbors.get(it.next());
				if(outNeighbors == null){
					size = 0;
				}else{
					size = outNeighbors.size();
				}
				mapContainer.updateOutDegreeTotals(size);
			}

			for(Iterator it = nodeToInNeighbors.keySet().iterator(); it.hasNext();){
				LinkedList inNeighbors = (LinkedList)nodeToInNeighbors.get(it.next());
				if(inNeighbors == null){
					size = 0;
				}else{
					size = inNeighbors.size();
				}
				mapContainer.updateInDegreeTotals(inNeighbors.size());
			}

			return mapContainer;
		}catch(IOException ioe){
			throw new AlgorithmExecutionException("Errors reading the file.", ioe);
		}
	}


	private static void addEdge(Integer source, Integer target, Map nodeToInNeighbors, Map nodeToOutNeighbors){
		LinkedList outNeighbors = (LinkedList)nodeToOutNeighbors.get(source);
		if(outNeighbors == null){
			outNeighbors = new LinkedList();
		}
		LinkedList inNeighbors = (LinkedList)nodeToInNeighbors.get(target);
		if(inNeighbors == null){
			inNeighbors = new LinkedList();
		}

		outNeighbors.add(target);
		inNeighbors.add(source);

		nodeToOutNeighbors.put(source, outNeighbors);
		nodeToInNeighbors.put(target, inNeighbors);
	}

	private static void throwExistingAttributeException(String attribute) throws AlgorithmExecutionException{
		throw new AlgorithmExecutionException("This Graph already has an attribute for " + attribute + ". Please change the name of that attribute if you wish to use this algorithm.");
	}

	private static void checkAttributes(String[] attributes) throws AlgorithmExecutionException{
		for(int i = 0; i < attributes.length; i++){
			String attribute = attributes[i];
			attribute = attribute.split("\\*")[0];
			if(attribute.equals("kInIn") || attribute.equals("kInOut") || attribute.equals("kOutIn") || attribute.equals("kOutOut")){
				throwExistingAttributeException(attribute);
			}

		}
	}

	public ProgressMonitor getProgressMonitor() {
		// TODO Auto-generated method stub
		return this.progMonitor;
	}

	public void setProgressMonitor(ProgressMonitor monitor) {
		this.progMonitor = monitor;
		
	}
}