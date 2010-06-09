package edu.iu.nwb.analysis.betweenness;

import java.io.File;
import java.io.IOException;
import java.util.Dictionary;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmExecutionException;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.cishell.utilities.BasicDataPlus;
import org.cishell.utilities.FileUtilities;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

import edu.iu.nwb.util.nwbfile.NWBFileParser;
import edu.iu.nwb.util.nwbfile.NWBFileParserAdapter;
import edu.iu.nwb.util.nwbfile.NWBFileProperty;
import edu.iu.nwb.util.nwbfile.NWBFileWriter;
import edu.iu.nwb.util.nwbfile.ParsingException;

public class Betweenness implements Algorithm {
    private Data[] data;
    private Dictionary parameters;
    private CIShellContext ciShellContext;
    
    public Betweenness(Data[] data,
    				  Dictionary parameters,
    				  CIShellContext ciShellContext) {
        this.data = data;
        this.parameters = parameters;
        this.ciShellContext = ciShellContext;
    }

    public Data[] execute() throws AlgorithmExecutionException {
    	
    	
    	File input = (File) data[0].getData();
    	
    	final Multimap<Integer, Integer> adjacency = ArrayListMultimap.create();
    	
    	final int[] nodeCounter = {0};
    	try {
			new NWBFileParser(input).parse(new NWBFileParserAdapter(){
				
				public void addNode(int id, String label, Map attributes) {
					nodeCounter[0] = nodeCounter[0] + 1;
				}
				
				public void addDirectedEdge(int sourceNode, int targetNode, Map attributes) {
					adjacency.put(sourceNode, targetNode);
				}

				public void addUndirectedEdge(int node1, int node2, Map attributes) {
					adjacency.put(node1, node2);
					adjacency.put(node2, node1);
				}
				
			});
		} catch (ParsingException e) {
			throw new AlgorithmExecutionException("There was a problem reading the input: " + e.getMessage(), e);
		} catch (IOException e) {
			throw new AlgorithmExecutionException("Unable to read the input.", e);
		}
		
		final Map<Integer, Double> rawBetweenness = new BasicBetweenness(adjacency.asMap()).calculate();
		/*double totalBetweenness = 0;
		for(Double betweenness : rawBetweenness.values()) {
			totalBetweenness += betweenness;
		}
		final double total = totalBetweenness;
		*/
		final double maxBetweenness = (nodeCounter[0] - 1.0) * (nodeCounter[0] - 2.0);
		File output;
		try {
			output = FileUtilities.createTemporaryFileInDefaultTemporaryDirectory("betweenness", "nwb");
			new NWBFileParser(input).parse(new NWBFileWriter(output) {
				
				private String rawBetweennessAttribute;
				private Object normalizedBetweennessAttribute;

				public void setNodeSchema(LinkedHashMap schema) {
					this.rawBetweennessAttribute = calculateNewAttributeName(schema.keySet(), "raw_betweenness");
					this.normalizedBetweennessAttribute = calculateNewAttributeName(schema.keySet(), "normalized_betweenness");
					schema.put(rawBetweennessAttribute, NWBFileProperty.TYPE_FLOAT);
					schema.put(normalizedBetweennessAttribute, NWBFileProperty.TYPE_FLOAT);
					super.setNodeSchema(schema);
				}
				
				private String calculateNewAttributeName(Set existing,
						String base) {
					if(!existing.contains(base)) {
						return base;
					} else {
						int counter = 1;
						String candidate = base + "_" + counter;
						while(existing.contains(candidate)) {
							counter++;
							candidate = base + "_" + counter;
						}
						return candidate;
					}
				}

				public void addNode(int id, String label, Map attributes) {
					attributes = Maps.newHashMap(attributes);
					Integer node = new Integer(id);
					if(rawBetweenness.containsKey(node)) {
						Double betweenness = rawBetweenness.get(node);
						attributes.put(rawBetweennessAttribute, betweenness);
						attributes.put(normalizedBetweennessAttribute, betweenness / maxBetweenness);
					} else {
						attributes.put(rawBetweennessAttribute, 0);
						attributes.put(normalizedBetweennessAttribute, 0);
					}
					super.addNode(id, label, attributes);
				}
			});
		} catch (ParsingException e) {
			throw new AlgorithmExecutionException("There was a problem writing the output: " + e.getMessage(), e);
		} catch (IOException e) {
			throw new AlgorithmExecutionException("Unable to write the output.", e);
		}
    	
		BasicDataPlus outputData = new BasicDataPlus(output, "file:text/nwb", data[0]);
		outputData.setLabel("with Betweenness Centrality");
		outputData.setType(DataProperty.NETWORK_TYPE);
        return new Data[]{outputData};
    }
}