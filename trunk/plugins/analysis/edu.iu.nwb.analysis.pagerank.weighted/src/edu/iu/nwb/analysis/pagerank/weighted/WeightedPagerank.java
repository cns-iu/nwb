package edu.iu.nwb.analysis.pagerank.weighted;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;

import cern.colt.function.DoubleDoubleFunction;
import cern.colt.function.DoubleFunction;
import cern.colt.matrix.DoubleFactory1D;
import cern.colt.matrix.DoubleMatrix1D;
import cern.jet.math.Functions;
import edu.iu.nwb.util.nwbfile.GetMetadataAndCounts;
import edu.iu.nwb.util.nwbfile.NWBFileParser;
import edu.iu.nwb.util.nwbfile.NWBFileParserHandler;
import edu.iu.nwb.util.nwbfile.ParsingException;

public class WeightedPagerank implements Algorithm {
	private Data[] data;
    private Dictionary parameters;
	//private LogService logger;
    
    public WeightedPagerank(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
		//this.logger = (LogService) context.getService(LogService.class.getName());

    }

    public Data[] execute() throws AlgorithmExecutionException {
    	
    	File nwbFile = (File) data[0].getData();
    	
    	WeightAccessor weightAccessor = getWeightAccessor();
    	
    	double dampingFactor = ((Double) parameters.get("d"));
    	
    	Data nwbOutput = annotateWithPagerank(nwbFile, weightAccessor,
				dampingFactor);
		
		
        return new Data[]{nwbOutput};
    }

	private Data annotateWithPagerank(File nwbFile,
			WeightAccessor weightAccessor, double dampingFactor)
			throws AlgorithmExecutionException {
		Graph graph = prepareEdges(nwbFile, weightAccessor);
    	int numberOfNodes = graph.getNumberOfNodes();
    	//TODO: maybe move nodeLookup stuff inside of network, rewrite edges to avoid multiple translation?
    	final Map<Integer, Integer> nodeLookup = createSequentialLookup(graph);
    	
    	DoubleMatrix1D pagerank = calculatePagerank(graph, nodeLookup,
				numberOfNodes, dampingFactor);
    	
    	
    	Data nwbOutput = prepareAnnotatedData(nwbFile, nodeLookup, pagerank);
		return nwbOutput;
	}

	private Data prepareAnnotatedData(File nwbFile,
			final Map<Integer, Integer> nodeLookup, DoubleMatrix1D pagerank)
			throws AlgorithmExecutionException {
		NWBIntegrator integrator = annotateInputFile(nwbFile, nodeLookup,
				pagerank);

		Data nwbOutput = prepareData(integrator.getOutputFile());
		return nwbOutput;
	}

	private NWBIntegrator annotateInputFile(File nwbFile,
			final Map<Integer, Integer> nodeLookup, DoubleMatrix1D pagerank)
			throws AlgorithmExecutionException {
		NWBIntegrator integrator = new NWBIntegrator(nodeLookup, pagerank);
		parseNWBFile(nwbFile, integrator);
		return integrator;
	}

	private WeightAccessor getWeightAccessor() {
		WeightAccessor weightAccessor;
    	String weightAttribute = (String) parameters.get("weight");
    	if(WeightedPagerankFactory.DEFAULT_WEIGHT.equals(weightAttribute)) {
    		weightAccessor = new ConstantWeightAccessor(1.0);
    	} else {
    		weightAccessor = new ColumnWeightAccessor(weightAttribute);
    	}
		return weightAccessor;
	}

	private Data prepareData(File file) {
		Data nwbOutput = new BasicData(file, "file:text/nwb");
		Dictionary<String, Object> metadata = nwbOutput.getMetadata();
		metadata.put(DataProperty.PARENT, data[0]);
		metadata.put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
		metadata.put(DataProperty.LABEL, "with Pagerank");
		return nwbOutput;
	}

	private DoubleMatrix1D calculatePagerank(Graph graph,
			final Map<Integer, Integer> nodeLookup, final int numberOfNodes,
			final double dampingFactor) throws AlgorithmExecutionException {
		final DoubleMatrix1D normStrengths = calculateNormStrengths(graph,
				nodeLookup, numberOfNodes, dampingFactor);
    	
    	int[] stopnodes = determineStopnodeIndices(normStrengths);
    	
    	
    	
    	DoubleMatrix1D currentValues = DoubleFactory1D.dense.make(numberOfNodes, 1.0 / numberOfNodes);
    	boolean finished = false;
    	
    	while(!finished) {
    		final DoubleMatrix1D previousValues = currentValues;
    		DoubleMatrix1D stopnodeValues = previousValues.viewSelection(stopnodes);
    		double stopnodeContributions = stopnodeValues.aggregate(Functions.plus, new DoubleFunction() {
				public double apply(double arg) {
					return arg * dampingFactor / numberOfNodes;
				}
    		});
    		currentValues = DoubleFactory1D.dense.make(numberOfNodes, stopnodeContributions + (1 - dampingFactor) / numberOfNodes);
    		
    		
    		final DoubleMatrix1D currentValues2 = currentValues;
    		graph.performEdgePass(new EdgeHandler() {

				public void handleEdge(Edge edge) {
					int source = nodeLookup.get(edge.getSource());
					int target = nodeLookup.get(edge.getTarget());
					currentValues2.setQuick(target, currentValues2.getQuick(target) + normStrengths.get(source) * previousValues.get(source) * edge.getWeight());
					
				}
    			
    		});
    		

    		finished = isWithinTolerance(currentValues, previousValues);
    		
    		if(Math.abs(currentValues.zSum() - 1) > .00001) {
    			throw new IllegalArgumentException("Alert: The total pagerank is not 1. This should not be possible. The total pagerank is " + currentValues.zSum());
    		}
    		
    	}
		return currentValues;
	}

	private boolean isWithinTolerance(DoubleMatrix1D currentValues,
			final DoubleMatrix1D previousValues) {
		boolean finished;
		finished = previousValues.assign(currentValues, new DoubleDoubleFunction() {

			public double apply(double previousValue, double currentValue) {
				return Math.abs(currentValue - previousValue) / previousValue;
			}
			
		}).viewSelection(Functions.isLess(.00001)).size() == previousValues.size();
		return finished;
	}

	private int[] determineStopnodeIndices(DoubleMatrix1D normStrengths) {
		List<Integer> stopnodeIndices = new ArrayList<Integer>();
    	for(int index = 0; index < normStrengths.size(); index++) {
    		if(Double.isInfinite(normStrengths.getQuick(index))) {
    			stopnodeIndices.add(index);
    		}
    	}
    	
    	int numberOfStopnodes = stopnodeIndices.size();
		int[] stopnodes = new int[numberOfStopnodes];
    	for(int index = 0; index < numberOfStopnodes; index++) {
    		stopnodes[index] = stopnodeIndices.get(index);
    	}
		return stopnodes;
	}
    
    //TODO: check weights are > 0
	private DoubleMatrix1D calculateNormStrengths(Graph graph,
			final Map<Integer, Integer> nodeLookup, int numberOfNodes, final double dampingFactor) throws AlgorithmExecutionException {
		final DoubleMatrix1D nodeStrengths = DoubleFactory1D.dense.make(numberOfNodes);
    	
    	graph.performEdgePass(new EdgeHandler() {

			public void handleEdge(Edge edge) {
				int source = nodeLookup.get(edge.getSource());
				double currentStrength = nodeStrengths.getQuick(source);
				nodeStrengths.setQuick(source, currentStrength + edge.getWeight());
			}
    		
    	});
    	
    	
    	//note: overwrites existing matrix
    	DoubleMatrix1D normStrengths = nodeStrengths.assign(new DoubleFunction() {

			public double apply(double arg) {
				return dampingFactor / arg;
			}
    		
    	});
		return normStrengths;
	}

	private Map<Integer, Integer> createSequentialLookup(Graph graph) throws AlgorithmExecutionException {
		final Map<Integer, Integer> nodeLookup = new HashMap<Integer, Integer>();
		graph.performEdgePass(new EdgeHandler() {
			private int nodeIndex = 0;
			public void handleEdge(Edge edge) {
				indexNode(edge.getSource());
				indexNode(edge.getTarget());
				
			}
			private void indexNode(int source) {
				if(!nodeLookup.containsKey(source)) {
					nodeLookup.put(source, nodeIndex);
					nodeIndex++;
				}
			}
			
		});
		return nodeLookup;
	}

	private Graph prepareEdges(File nwbFile, WeightAccessor weightAccessor)
			throws AlgorithmExecutionException {
		Graph edges;
    	
    	GetMetadataAndCounts networkInfo = new GetMetadataAndCounts();
    	parseNWBFile(nwbFile, networkInfo);
		
		int numberOfNodes = networkInfo.getNodeCount();
		int numberOfDirectedEdges = networkInfo.getDirectedEdgeCount();
		
		Runtime runtime = Runtime.getRuntime();
		long availableMemory = runtime.freeMemory() + (runtime.maxMemory() - runtime.totalMemory());
		// approximation of maximum memory per edge -- 64 bit architecture (8 bytes per int), three ints per edge, two ints for bookkeeping, allowing half for other stuff
		long possibleEdges = availableMemory / (8 * 5 * 2 * 2); 
		if(numberOfDirectedEdges > possibleEdges) {
			edges = new OnDiskGraph(nwbFile, weightAccessor, numberOfNodes);
		} else {
			edges = new InMemoryGraph(nwbFile, weightAccessor, numberOfNodes, numberOfDirectedEdges);
		}
		return edges;
	}

	protected static void parseNWBFile(File nwbFile, NWBFileParserHandler handler)
			throws AlgorithmExecutionException {
		try {
			new NWBFileParser(nwbFile).parse(handler);
		} catch (ParsingException e) {
			throw new AlgorithmExecutionException("Problem parsing input file: " + e.toString(), e);
		} catch (IOException e) {
			throw new AlgorithmExecutionException("Problem reading input file: " + e.toString(), e);
		}
	}
}