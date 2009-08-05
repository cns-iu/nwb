package edu.iu.nwb.analysis.extractattractors.components;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.BitSet;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.algorithm.ProgressMonitor;

import prefuse.data.Table;
import edu.iu.nwb.analysis.extractattractors.containers.BigArray;
import edu.iu.nwb.analysis.extractattractors.containers.BigBigIntegerArray;
import edu.iu.nwb.analysis.extractattractors.containers.BigBitSetArray;
import edu.iu.nwb.analysis.extractattractors.containers.BigHashSetVector;

public class ExtractAttractorBasins {

	File[] basins;
	BigInteger[] basinSizes;
	File[] attractorTables;
	double[] observedCoherencies;
	int numberOfBasins = 0;
	File attractorRobustnessFile;
	ProgressMonitor progMonitor;
	int progress = 0;
	boolean tooManyBasins = false;

	public ExtractAttractorBasins(ProgressMonitor pm){
		this.progMonitor = pm;
	}

	public File getRobustnessFile(){
		return this.attractorRobustnessFile;
	}

	public File[] getBasins(){

		return basins;
	}

	public BigInteger[] getBasinSizes(){
		return this.basinSizes;
	}

	public File[] getAttractors(){
		return this.attractorTables;
	}

	public void computeWeakComponents(SimpleStateSpaceGraph inGraph, Table table,String labelColumn, int nodeStates)throws InterruptedException, AlgorithmExecutionException{
		FileWriter robustnessFileWriter = null;
		BasinConstructorThread[] bctArray = new BasinConstructorThread[Runtime.getRuntime().availableProcessors()];

		BigInteger twentyPercent = inGraph.maxSize.divide(new BigInteger(new Integer(5).toString()));
		if(twentyPercent.compareTo(BigInteger.ZERO) == 0)
			twentyPercent = BigInteger.ONE;
		
		for(int i = 0; i < bctArray.length; i++){
			bctArray[i] = new BasinConstructorThread(inGraph,table,labelColumn,this);
			bctArray[i].setTwentyPercent(twentyPercent);
		}
		
		if(inGraph.graphSize.compareTo(BigArray.maxInteger) < 0)
			smallWeakComponentCalculation(this,inGraph,table,labelColumn,bctArray);
		else
			bigWeakComponentCalculation(this, inGraph,table,labelColumn,bctArray);


		this.basins = new File[this.numberOfBasins];
		this.basinSizes = new BigInteger[this.numberOfBasins];
		this.attractorTables = new File[this.numberOfBasins];
		this.observedCoherencies = new double[this.numberOfBasins];

		this.attractorRobustnessFile = createRobustnessFile();
		robustnessFileWriter = writeHeaders(this.attractorRobustnessFile);

		for(int i = 0; i < bctArray.length; i++){
			bctArray[i].join();

		}

		int length = bctArray.length;

		for(int i = 0; i < this.numberOfBasins; i++){
			this.basins[i] = bctArray[i%length].getAttractorBasin(i/length);
			this.attractorTables[i] = bctArray[i%length].getAttractorTable(i/length);
			this.basinSizes[i] = bctArray[i%length].getBasinSize(i/length);
			this.observedCoherencies[i] = bctArray[i%length].getObservedCoherency(i/length);
			writeCoherencyInfo(robustnessFileWriter, this.basinSizes[i],this.observedCoherencies[i],inGraph.labelSize,inGraph.getSize(),nodeStates,i+1);		
		}
	}

	public static void smallWeakComponentCalculation(ExtractAttractorBasins eab, SimpleStateSpaceGraph inGraph, Table table, String labelColumn, BasinConstructorThread[] basinConstructorThreads) throws InterruptedException, AlgorithmExecutionException{
		BitSet seenNodes = new BitSet(inGraph.getSize().intValue());



		int[] preOrder = new int[inGraph.maxSize.intValue()];
		int[] strongComponent = new int[inGraph.maxSize.intValue()];

		java.util.Arrays.fill(preOrder, -1);
		java.util.Arrays.fill(strongComponent, -1);

		for(int i = 0; i < basinConstructorThreads.length; i++){
			basinConstructorThreads[i%basinConstructorThreads.length].setSmallArrays(preOrder, strongComponent);
		}

		int threadNumber = 0;
		int[] targets = inGraph.getSmallOutNeighbors();
		for(int i = 0; i < inGraph.maxSize.intValue(); i++){
			if(inGraph.getOutNeighbor(new BigInteger(new Integer(i).toString())) != null){
				if(!seenNodes.get(i)){
					eab.numberOfBasins++;
					HashSet<Integer> componentNodes = new HashSet<Integer>();

					componentNodes = smallUndirectedDepthFirstSearch(inGraph,seenNodes,componentNodes,i);
					basinConstructorThreads[threadNumber%basinConstructorThreads.length].queueSmallNodes(componentNodes);
					threadNumber = (threadNumber+1)%basinConstructorThreads.length;
				}

			}	
		}

		for(int i = 0; i < basinConstructorThreads.length; i++){
			basinConstructorThreads[i].start();
		}

	}

	public static void bigWeakComponentCalculation(ExtractAttractorBasins eab, SimpleStateSpaceGraph inGraph, Table table, String labelColumn, BasinConstructorThread[] basinConstructorThreads) throws AlgorithmExecutionException{
		BigBitSetArray seenNodes = new BigBitSetArray(inGraph.maxSize);
		BigBigIntegerArray preOrder = new BigBigIntegerArray(inGraph.maxSize);
		BigBigIntegerArray strongComponent = new BigBigIntegerArray(inGraph.maxSize);

		BigBigIntegerArray.fill(preOrder, BigInteger.ZERO);
		BigBigIntegerArray.fill(strongComponent, BigInteger.ZERO);

		for(int i = 0; i < basinConstructorThreads.length; i++){
			basinConstructorThreads[i].setBigArrays(preOrder, strongComponent);
		}

		int threadNumber = 0;
		BigInteger start;
		for(start = BigInteger.ZERO; start.compareTo(inGraph.maxSize) < 0; start = start.add(BigInteger.ONE)){
			if(inGraph.getOutNeighbor(start) != null){
				if(!seenNodes.get(start)){
					eab.numberOfBasins++;
					if(eab.numberOfBasins < 0){
						eab.tooManyBasins = true;
						throw new AlgorithmExecutionException("There are too many basins of attraction for this software to operate on correctly");
					}
					BigHashSetVector componentNodes = new BigHashSetVector();	
					componentNodes = bigUndirectedDepthFirstSearch(inGraph,seenNodes,componentNodes,start);

					basinConstructorThreads[threadNumber%basinConstructorThreads.length].queueBigNodes(componentNodes);
					threadNumber = (threadNumber+1)%basinConstructorThreads.length;
				}
			}
		}

		for(int i = 0; i < basinConstructorThreads.length; i++){
			basinConstructorThreads[i].start();
		}

	}



	public static HashSet<Integer> smallUndirectedDepthFirstSearch(final SimpleStateSpaceGraph inGraph,BitSet encounteredNodes, HashSet<Integer> componentNodes, Integer nodeNumber){
		LinkedList<Integer> queue = new LinkedList<Integer>();
		Integer originalNodeNumber;
		queue.addLast(nodeNumber);
		while(!queue.isEmpty()){
			originalNodeNumber = queue.removeLast();
			if(!encounteredNodes.get(originalNodeNumber.intValue())){
				componentNodes.add(originalNodeNumber);
				encounteredNodes.set(originalNodeNumber.intValue());

				Integer target = new Integer(inGraph.getOutNeighbor(new BigInteger(originalNodeNumber.toString())).intValue());

				if(!encounteredNodes.get(target.intValue())){
					queue.add(target);
				}
				HashSet<Integer> inNodes = inGraph.getSmallInNeighbors(originalNodeNumber.intValue());
				if(inNodes != null){
					for(Iterator<Integer> it = inNodes.iterator(); it.hasNext();){
						Integer source = it.next();

						if(!encounteredNodes.get(source.intValue())){
							queue.add(source);
						}
					}
				}
			}
		}
		return componentNodes;
	}



	private static BigHashSetVector bigUndirectedDepthFirstSearch(final SimpleStateSpaceGraph originalGraph,BigBitSetArray encounteredNodes,BigHashSetVector componentNodes,BigInteger nodeNumber){
		LinkedList<BigInteger> queue = new LinkedList<BigInteger>();
		BigInteger originalNodeNumber;
		queue.addLast(nodeNumber);
		while(!queue.isEmpty()){
			originalNodeNumber = queue.removeLast();

			if(!encounteredNodes.get(originalNodeNumber)){
				componentNodes.add(originalNodeNumber);
				encounteredNodes.set(originalNodeNumber);

				BigInteger target = originalGraph.getOutNeighbor(originalNodeNumber);
				if(!encounteredNodes.get(target)){
					queue.add(target);
				}
				BigHashSetVector inNodes = originalGraph.getBigInNeighbors(originalNodeNumber);
				if(inNodes != null){
					for(Iterator<BigInteger> it = inNodes.iterator(); it.hasNext();){
						BigInteger source = it.next();
						if(!encounteredNodes.get(source)){
							queue.add(source);
						}
					}
				}
			}
		}
		return componentNodes;
	}

	private static FileWriter writeCoherencyInfo(FileWriter fw, BigInteger basinSize, double observedCoherency, int systemSize, BigInteger totalSize,int nodeStates, int basinNumber) throws AlgorithmExecutionException{
		try{
			NumberFormat format = new DecimalFormat("#.#####");
			fw.flush();
			BigDecimal bS = new BigDecimal(basinSize);
			BigDecimal tS = new BigDecimal(totalSize);
			double percentage = (bS.divide(tS,7,BigDecimal.ROUND_DOWN).doubleValue()*100);
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

	private static double calculateMaxCoherency(BigInteger basinSize, int systemSize,int nodeStates){
		double maxCoherency = (Math.log(basinSize.doubleValue())/Math.log(nodeStates));
		maxCoherency = maxCoherency/((double)systemSize);
		return maxCoherency;
	}

	private static double calculateRandomCoherency(BigInteger basinSize, int systemSize, int nodeStates){
		double randomCoherency = basinSize.doubleValue()-1;
		BigInteger totalSize = new BigInteger(new Integer(nodeStates).toString()).pow(systemSize);
		randomCoherency = randomCoherency/totalSize.doubleValue();
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
