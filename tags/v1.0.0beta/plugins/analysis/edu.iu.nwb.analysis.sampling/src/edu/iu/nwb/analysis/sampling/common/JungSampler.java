package edu.iu.nwb.analysis.sampling.common;

import java.util.Dictionary;

import edu.uci.ics.jung.graph.Graph;

public interface JungSampler {

	Graph sample(Graph graph, Dictionary parameters);

	String getSampleName();
}
