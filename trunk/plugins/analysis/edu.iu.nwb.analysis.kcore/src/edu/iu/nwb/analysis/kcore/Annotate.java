package edu.iu.nwb.analysis.kcore;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.log.LogService;

import cern.colt.function.DoubleDoubleFunction;
import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.jet.math.Functions;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.utils.GraphUtils;
import edu.uci.ics.jung.utils.UserData;
import edu.uci.ics.jung.utils.UserDataContainer.CopyAction;


public class Annotate implements Algorithm {
	Data[] data;
	Dictionary parameters;
	CIShellContext context;
	
	private String attributeName = "nwb_coreness";

	public Annotate(Data[] data, Dictionary parameters, CIShellContext context) {
		this.data = data;
		this.parameters = parameters;
		this.context = context;

	}

	public Data[] execute() {

		LogService logger = (LogService)context.getService(LogService.class.getName());

		
		Graph inputGraph = (Graph) this.data[0].getData();
		
		
		
		Graph outputGraph = (Graph) inputGraph.copy();
		
		Graph calcGraph = (Graph) outputGraph.copy(); //chaining these calls should mean getEqualEdge works
		
		
		
		int currentCore = 0;
		
		while(calcGraph.numVertices() > 0) {
			
			
			Set toCheck = calcGraph.getVertices();
			
			while(toCheck.size() > 0) {
				Iterator checking = toCheck.iterator();
				toCheck = new HashSet();
				
				Set toRemove = new HashSet();
				
				while(checking.hasNext()) {
					Vertex vertex = (Vertex) checking.next();
					
					if(vertex.degree() <= currentCore) {
						vertex.getEqualVertex(outputGraph).setUserDatum(attributeName, new Integer(currentCore), UserData.SHARED);
						
						toCheck.addAll(vertex.getNeighbors());
						
						toRemove.add(vertex);
						
					}
				}
				
				toCheck.removeAll(toRemove); //being taken out, so don't check
				
				
				GraphUtils.removeVertices(calcGraph, toRemove);
				
				
			}
			
			currentCore++;
			
			
		}
		
		
		
		
		
		Data output = new BasicData(outputGraph, Graph.class.getName());
		
		Dictionary metadata = output.getMetadata();
		metadata.put(DataProperty.LABEL, "K-Coreness annotated graph");
		metadata.put(DataProperty.PARENT, this.data[0]);
		metadata.put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);

		return new Data[]{output};
		
		
		
		
	}
}