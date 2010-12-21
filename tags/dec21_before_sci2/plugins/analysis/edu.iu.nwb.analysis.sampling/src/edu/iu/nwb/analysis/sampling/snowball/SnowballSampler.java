package edu.iu.nwb.analysis.sampling.snowball;

import java.util.Dictionary;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import edu.iu.nwb.analysis.sampling.common.JungSampler;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.utils.GraphUtils;

public class SnowballSampler implements JungSampler {

	private static final String NUMBER_PARAMETER_KEY = "nodes";
	
	
	//this is not efficient, but I have not tried to make it so, yet. However, its efficiency does not seem
	//to be the limiting factor -- the memory efficiency of the NWB -> Jung conversion is. I suspect this is
	//due to the horrendously large intermediate step to GraphML; perhaps a direct NWB -> Jung or Prefuse (prefuse
	//can handle a similar format out of the box, so it might be simpler) conversion is in order?
	public Graph sample(Graph graph, Dictionary parameters) {
		int totalNeeded = ((Integer) parameters.get(NUMBER_PARAMETER_KEY)).intValue();
		//this might well be the really inefficient line. Perhaps this should be reimplemented using NWB or Prefuse format?
		Vertex[] vertices = (Vertex[]) graph.getVertices().toArray(new Vertex[]{});
		
		Random randomizer = new Random();
		int startingIndex = randomizer.nextInt(vertices.length);
		Vertex start = vertices[startingIndex];
		
		Set sampledVertices = new HashSet();
		
		Set currentShell = new HashSet();
		currentShell.add(start);
		
		
		Set nextShell;
		
		int numVertices = sampledVertices.size();
		while(numVertices < totalNeeded) {
			int currentTotal = numVertices;
			nextShell = new HashSet();
			Iterator currentIter = currentShell.iterator();
			while(currentIter.hasNext()) {
				Vertex current = (Vertex) currentIter.next();
				nextShell.addAll(current.getNeighbors());
			}
			if(currentShell.size() + numVertices <= totalNeeded) {
				sampledVertices.addAll(currentShell);
			} else {
				while(currentShell.size() > 0 && sampledVertices.size() < totalNeeded) {
					Object vertex = currentShell.toArray()[randomizer.nextInt(currentShell.size())];
					sampledVertices.add(vertex);
					currentShell.remove(vertex);
				}
			}
			currentShell = nextShell;
			numVertices = sampledVertices.size();
			if(currentTotal == numVertices) {
				break;
			}
		}
		
		return GraphUtils.vertexSetToGraph(sampledVertices);
	}

	public String getSampleName() {
		return "Snowball Sample";
	}

}
