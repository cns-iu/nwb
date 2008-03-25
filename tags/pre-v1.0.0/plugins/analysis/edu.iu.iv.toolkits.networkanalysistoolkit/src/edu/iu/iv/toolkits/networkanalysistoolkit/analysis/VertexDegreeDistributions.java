/*
 * Created on Sep 25, 2004
 */
package edu.iu.iv.toolkits.networkanalysistoolkit.analysis;

import java.util.Iterator;
import java.util.Set;

import cern.colt.list.DoubleArrayList;
import cern.jet.stat.Descriptive;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.statistics.DegreeDistributions;
import edu.uci.ics.jung.statistics.Histogram;

/**
 * @author Shashikant
 */
public class VertexDegreeDistributions {

	private static final VertexDegreeDistributions INSTANCE = new VertexDegreeDistributions();

	private VertexDegreeDistributions() {
		// singleton pattern
	}

	public static VertexDegreeDistributions getInstance() {
		return INSTANCE;
	}

	public double getAverageDegree(Network network) {
		return Descriptive.mean(DegreeDistributions.getIndegreeValues(network
				.getGraph().getVertices()));
	}

	public static final DistributionType INDEGREE = new DistributionType();

	public static final DistributionType OUTDEGREE = new DistributionType();

	public static final DistributionType DEGREE = new DistributionType();

	private DoubleArrayList getDegreeValues(Set vertices) {
		DoubleArrayList dal = new DoubleArrayList();
		Iterator iterator = vertices.iterator();
		while (iterator.hasNext()) {
			double val = ((Vertex) iterator.next()).degree();
			System.out.println("gdv:dal: " + val);
			dal.add(val);
		}
		return dal;
	}

	public double getScaleFreeExponent(Network network,
			DistributionType distType) {
		Graph graph = network.getGraph();
		double scaleFreeExponent = 0;
		final int n = graph.getVertices().size();
		DoubleArrayList y = null;
		if (distType == VertexDegreeDistributions.DEGREE)
			y = this.getDegreeValues(graph.getVertices());
		else if (distType == VertexDegreeDistributions.INDEGREE)
			y = DegreeDistributions.getIndegreeValues(graph.getVertices());
		else if (distType == VertexDegreeDistributions.OUTDEGREE)
			y = DegreeDistributions.getOutdegreeValues(graph.getVertices());
		else
			throw new IllegalArgumentException("Invalid Distribution Type!");

		DoubleArrayList x = new DoubleArrayList();

		for (int i = 0; i < n; ++i)
			x.add(i + 1);

		double sigmalnx = 0, sigmalny = 0, sigmalnx2 = 0, sigmalnxlny = 0;
		for (int i = 0; i < n; ++i) {
			double lnxi = Math.log(x.getQuick(i));
			double lnyi = Math.log(y.getQuick(i));
			sigmalnx += lnxi;
			sigmalny += lnyi;
			sigmalnxlny += lnxi * lnyi;
			sigmalnx2 += lnxi * lnxi;
		}
		scaleFreeExponent = (n * sigmalnxlny - sigmalnx * sigmalny)
				/ (n * sigmalnx2 - sigmalnx * sigmalnx);

		B = scaleFreeExponent;
		A = Math.exp((sigmalny - B * sigmalnx) / n);

		return scaleFreeExponent;
	}

	private double A = -1.0, B = -1.0; // y = A.x^B power law fitting

	public double getRSquare(Network network, DistributionType distType) {
		double rSquare = 0;
		DoubleArrayList y = null;
		Graph graph = network.getGraph();
		if (distType == VertexDegreeDistributions.DEGREE) {
			y = this.getDegreeValues(graph.getVertices());
		} else if (distType == VertexDegreeDistributions.INDEGREE) {
			y = DegreeDistributions.getIndegreeValues(graph.getVertices());
		} else if (distType == VertexDegreeDistributions.OUTDEGREE) {
			y = DegreeDistributions.getOutdegreeValues(graph.getVertices());
		} else {
			throw new IllegalArgumentException("Invalid Distribution Type!");
		}
		double y_mean = this.computeMean(y);
		DoubleArrayList x = new DoubleArrayList();
		final int n = graph.numVertices();
		for (int i = 0; i < n; ++i)
			x.add(i);
		if (A == -1.0 || B == -1.0)
			getScaleFreeExponent(network, distType);
		DoubleArrayList y_fit = getPowerLawFitValues(A, B, x);
		double sse = 0.0;
		for (int i = 0; i < n; ++i) {
			double val = y.get(i) - y_fit.get(i);
			// not sure what to do with infinity values of 'val'
			// need to check reference
			if (val == Double.POSITIVE_INFINITY
					|| val == Double.NEGATIVE_INFINITY)
				val = 0;
			System.out.println("val" + i + " : " + val);
			sse += val * val;
		}
		double sst = 0.0;
		for (int i = 0; i < n; ++i) {
			double val = y.get(i) - y_mean;
			sst += val * val;
		}
		rSquare = 1 - sse / sst;
		return rSquare;
	}

	private double computeMean(DoubleArrayList dal) {
		double sum = 0;
		for (int i = 0; i < dal.size(); ++i) {
			sum += dal.get(i);
		}
		return sum / dal.size();
	}

	public DoubleArrayList getPowerLawFitValues(double A, double B,
			DoubleArrayList x) {
		System.out.println("A " + A + " B " + B);
		int n = x.size();
		DoubleArrayList y_fit = new DoubleArrayList();
		for (int i = 0; i < n; ++i) {
			double val = A * Math.pow(x.get(i), B);
			y_fit.add(val);
		}
		return y_fit;
	}

	public int[] getDistribution(Set vertices, DistributionType distType) {
		Iterator iter = vertices.iterator();
		int[] vals = new int[vertices.size()];
		int i = 0;
		while (iter.hasNext()) {
			Vertex v = (Vertex) iter.next();
			if (distType == VertexDegreeDistributions.INDEGREE)
				vals[i] = v.inDegree();
			else if (distType == VertexDegreeDistributions.OUTDEGREE)
				vals[i] = v.outDegree();
			else if (distType == VertexDegreeDistributions.DEGREE)
				vals[i] = v.degree();
			++i;
		}
		return vals;
	}

	public Histogram getHistogram(Set vertices, double min, double max,
			int numBins, DistributionType distType) {
		Histogram histogram;
		if (distType == VertexDegreeDistributions.INDEGREE)
			histogram = DegreeDistributions.getIndegreeHistogram(vertices, min,
					max, numBins);
		else if (distType == VertexDegreeDistributions.OUTDEGREE)
			histogram = DegreeDistributions.getOutdegreeHistogram(vertices,
					min, max, numBins);
		else if (distType == VertexDegreeDistributions.DEGREE)
			histogram = getDegreeHistogram(vertices, min, max, numBins);
		else
			throw new IllegalArgumentException("Invalid distribution type.");
		return histogram;
	}

	private Histogram getDegreeHistogram(Set vertices, double min, double max,
			int numBins) {
		Histogram histogram = new Histogram(min, max, numBins);
		System.out.println(min + " " + max + " " + numBins);
		for (Iterator i = vertices.iterator(); i.hasNext();) {
			double val = ((Vertex) i.next()).degree();
			histogram.fill(val);
		}
		return histogram;
	}
}

class DistributionType {
	// for sake of cleaner code.
	// didn't want to use an integer constant since it makes it non-unique
	// and anyone could by mistake pass the wrong stuff in.
	// this pattern guarantees uniqueness of distribution type
}