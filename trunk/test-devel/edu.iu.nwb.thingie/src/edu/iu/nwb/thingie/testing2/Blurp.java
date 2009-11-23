package edu.iu.nwb.thingie.testing2;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.random.generators.ErdosRenyiGenerator;

public class Blurp {
	
	public static void main(String[] args) {
		ErdosRenyiGenerator g = new ErdosRenyiGenerator(10, 1);
		Graph gr = (Graph) g.generateGraph();
		System.out.println(gr.getEdges().size());
	}
}
