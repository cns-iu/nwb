package edu.iu.nwb.analysis.betweenness;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

import com.google.common.collect.Maps;

public class BasicBetweenness {
	
	private static final Integer INFINITY = new Integer(-1);
	
	private Map<Integer, Double> betweenness = new HashMap<Integer, Double>();
	private final Map<Integer, Collection<Integer>> adjacency;
	private Queue<Integer> Q = new LinkedList<Integer>();
	private Stack<Integer> S = new Stack<Integer>();
	private Map<Integer, Integer> dist = Maps.newHashMap();
	private Map<Integer, List<Integer>> pred = Maps.newHashMap();
	private Map<Integer, Integer> sigma = Maps.newHashMap();
	private Map<Integer, Double> delta = Maps.newHashMap();
	
	public BasicBetweenness(final Map<Integer, Collection<Integer>> adjacency) {
		this.adjacency = adjacency;
		for(Integer s : adjacency.keySet()) {
			betweenness.put(s, 0.0);
		}
	}
	
	public Map<Integer, Double> calculate() {
		for(Integer s : adjacency.keySet()) {
			singleSource(s);
			accumulation(s);
		}
		
		return betweenness;
	}

	private void singleSource(final Integer s) {
		initialization(s);
		Integer v;
		while((v = Q.poll()) != null) {
			S.push(v);
			for(Integer w : adjacency.get(v)) {
				pathDiscovery(v, w);
				pathCounting(v, w);
			}
		}
	}

	private void accumulation(final Integer s) {
		for(Integer node : adjacency.keySet()) {
			delta.put(node, 0.0);
		}
		while(!S.empty()) {
			Integer w = S.pop();
			for(Integer v : pred.get(w)) {
				delta.put(v, delta.get(v) + sigma.get(v) * (1.0 + delta.get(w))/ sigma.get(w));
				if(!w.equals(s)) {
					betweenness.put(w, betweenness.get(w) + delta.get(w));
				}
			}
		}
		
	}

	private void pathCounting(final Integer v, final Integer w) {
		if(dist.get(w) == dist.get(v) + 1) {
			sigma.put(w, sigma.get(w) + sigma.get(v));
			pred.get(w).add(v);
		}
	}

	private void pathDiscovery(final Integer v, final Integer w) {
		if(dist.get(w) == INFINITY) {
			dist.put(w, dist.get(v) + 1);
			Q.add(w);
		}
	}

	private void initialization(final Integer s) {
		for(Integer node : adjacency.keySet()) {
			pred.put(node, new ArrayList<Integer>());
			dist.put(node, INFINITY);
			sigma.put(node, 0);
		}
		dist.put(s, 0);
		sigma.put(s, 1);
		Q.add(s);
	}
}
