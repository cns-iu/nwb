package edu.iu.nwb.analysis.extractattractors.components;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;

import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.ProgressMonitor;

import prefuse.data.Table;

public class ExtractAttractorBasins {

	ArrayList attractorBasins = new ArrayList();
	ArrayList attractorSizes = new ArrayList();
	ArrayList attractorTables = new ArrayList();
	File attractorRobustnessFile;
	ProgressMonitor progMonitor;
	int progress = 0;

	public ExtractAttractorBasins(ProgressMonitor pm){
		this.progMonitor = pm;
	}

	public File getRobustnessFile(){
		return this.attractorRobustnessFile;
	}

	public File[] getBasins(){
		File[] basins = new File[this.attractorBasins.size()];

		return (File[])this.attractorBasins.toArray(basins);
	}

	public int[] getBasinSizes(){
		int[] sizes = new int[this.attractorSizes.size()];
		for(int i = 0; i < this.attractorSizes.size(); i++){
			sizes[i] = ((Integer)this.attractorSizes.get(i)).intValue();
		}
		return sizes;
	}

	public File[] getAttractors(){
		File[] attractors = new File[this.attractorTables.size()];
		return (File[])this.attractorTables.toArray(attractors);
	}

	public void weakComponentCalculation(SimpleStateSpaceGraph inGraph, Table table, String labelColumn,int nodeStates) throws InterruptedException, AlgorithmExecutionException{
		int systemSize = inGraph.labelSize;
		HashSet seenNodes = new HashSet();
		int numberOfNodes = inGraph.getSize();
		int[] preOrder = new int[numberOfNodes];
		int[] strongComponent = new int[numberOfNodes];
		java.util.Arrays.fill(preOrder, -1);
		java.util.Arrays.fill(strongComponent, -1);
		for(int i = 0; i < numberOfNodes; i++){
			Integer nodeNumber = new Integer(i);
			if(seenNodes.add(nodeNumber)){
				LinkedHashSet componentNodes = new LinkedHashSet();
				componentNodes = undirectedDepthFirstSearch(inGraph,componentNodes,i);
				seenNodes.addAll(componentNodes);
				this.attractorSizes.add(new Integer(componentNodes.size()));
				BasinConstructorThread bct = new BasinConstructorThread(inGraph, table,labelColumn,componentNodes,systemSize,nodeStates,preOrder,strongComponent,this);
				this.attractorBasins.add(bct);
				bct.start();
			}
		}

		seenNodes = null;
		this.attractorRobustnessFile = createRobustnessFile();
		FileWriter robustnessFileWriter = writeHeaders(this.attractorRobustnessFile);
		for(int i = 0; i < attractorBasins.size(); i++){
			BasinConstructorThread bct = (BasinConstructorThread)attractorBasins.get(i);
			bct.join();
			this.attractorBasins.set(i, bct.getAttractorBasin());
			this.attractorTables.add(bct.getAttractorTable());
			writeCoherencyInfo(robustnessFileWriter, bct.basinSize,bct.getObservedCoherency(),systemSize,numberOfNodes,nodeStates,i+1);
			System.gc();
		}

	}

	public static LinkedHashSet undirectedDepthFirstSearch(final SimpleStateSpaceGraph inGraph, LinkedHashSet componentNodes, int nodeNumber){
		runUDFS(inGraph,componentNodes,nodeNumber);
		return componentNodes;
	}

	private static void runUDFS(final SimpleStateSpaceGraph originalGraph,LinkedHashSet componentNodes,int nodeNumber){
		LinkedList queue = new LinkedList();
		Integer originalNodeNumber;
		queue.addLast(new Integer(nodeNumber));
		while(!queue.isEmpty()){
			originalNodeNumber = (Integer)queue.removeLast();

			if(!componentNodes.contains(originalNodeNumber)){
				componentNodes.add(originalNodeNumber);

				Integer target = originalGraph.getOutNeighbor(originalNodeNumber.intValue());
				if(!componentNodes.contains(target)){
					queue.add(target);
				}
				LinkedHashSet inNodes = originalGraph.getInNeighbors(originalNodeNumber.intValue());
				if(inNodes != null){
					for(Iterator it = inNodes.iterator(); it.hasNext();){
						Integer source = (Integer)it.next();
						if(!componentNodes.contains(source)){
							queue.add(source);
						}
					}
				}
			}
		}
	}
	
	
	
	private static FileWriter writeCoherencyInfo(FileWriter fw, int basinSize, double observedCoherency, int systemSize, int totalSize,int nodeStates, int basinNumber) throws AlgorithmExecutionException{
		try{
		NumberFormat format = new DecimalFormat("#.#####");
		fw.flush();
		double percentage = (((double)basinSize/(double)totalSize)*100);
		double maxCoherency = calculateMaxCoherency(basinSize,systemSize,nodeStates);
		double randomCoherency = calculateRandomCoherency(basinSize,systemSize,nodeStates);
		
		double structuralCoherency = calculateStructuralCoherency(maxCoherency,randomCoherency,observedCoherency);
		fw.write(basinNumber+","+format.format(percentage)+","+format.format(maxCoherency)+
				","+format.format(randomCoherency)+","+format.format(observedCoherency)+","+
				format.format(structuralCoherency)+System.getProperty("line.separator"));
		fw.flush();
		}catch(IOException ioe){
			throw new AlgorithmExecutionException("Errors writing Data to the structural coherency file.", ioe);
		}
		return fw;
	}
	
	private static File createRobustnessFile() throws AlgorithmExecutionException{
		try{
		File robustnessFile = File.createTempFile(new Long(new Date().getTime()).toString(), ".csv");
			return robustnessFile;
		}catch(IOException ioe){
			throw new AlgorithmExecutionException("Unable to create the structural coherency file.",ioe);
		}
	}
	
	private static FileWriter writeHeaders(File f) throws AlgorithmExecutionException{
		try{
			FileWriter fw = new FileWriter(f);
			fw.flush();
			fw.write("Attractor Basin,Percentage of Total Size,Maximum Coherency,Random Coherency,Observed Coherency,Structural Coherency"+System.getProperty("line.separator"));
			fw.flush();
		return fw;
		}catch(IOException ioe){
			throw new AlgorithmExecutionException("Errors writing Headers to the structural coherency file.",ioe);
		}
	}
	
	private static double calculateMaxCoherency(int basinSize, int systemSize,int nodeStates){
		double maxCoherency = (Math.log(basinSize)/Math.log(nodeStates));
		maxCoherency = maxCoherency/systemSize;
		return maxCoherency;
	}
	
	private static double calculateRandomCoherency(int basinSize, int systemSize, int nodeStates){
		double randomCoherency = basinSize-1;
		int totalSize = (int)Math.pow(nodeStates, systemSize);
		randomCoherency = randomCoherency/totalSize;
		return randomCoherency;
	}
	
	protected static double calculateStructuralCoherency(double maxCoherency, double randomCoherency, double observedCoherency){
		double results;
		if(maxCoherency == 0 || observedCoherency == 0)
			return 0;
		
		double top = observedCoherency-randomCoherency;
		double bottom = maxCoherency-randomCoherency;
		results = top/bottom;
		return results;
		
	}

	public static void updateProgress(ExtractAttractorBasins eab, int progress){
		eab.progress+=progress;
		eab.progMonitor.worked(eab.progress);
	}

}
