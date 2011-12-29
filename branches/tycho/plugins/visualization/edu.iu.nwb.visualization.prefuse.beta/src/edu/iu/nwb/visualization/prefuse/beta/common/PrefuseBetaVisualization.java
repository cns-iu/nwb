package edu.iu.nwb.visualization.prefuse.beta.common;

import java.util.Dictionary;

import prefuse.data.Graph;

public interface PrefuseBetaVisualization {
	public Graph create(Graph graph, Dictionary parameters);
}
