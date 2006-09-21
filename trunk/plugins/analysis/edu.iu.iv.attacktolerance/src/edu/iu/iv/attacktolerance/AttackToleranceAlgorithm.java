	package edu.iu.iv.attacktolerance;

import java.util.Dictionary;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;

import edu.uci.ics.jung.graph.Graph;

public class AttackToleranceAlgorithm implements Algorithm {
    Data[] data;
    Dictionary parameters;
    CIShellContext context;
    
    public AttackToleranceAlgorithm(Data[] data, Dictionary parameters, CIShellContext context) {
        this.data = data;
        this.parameters = parameters;
        this.context = context;
    }

    public Data[] execute() {
    	 Graph graph = (Graph)(data[0].getData());
    	 int numNodes = getInt("numNodes");
         AttackTolerance at = new AttackTolerance(graph, numNodes);
         boolean isDone = at.testAttackTolerance();
         Data[] out_data = null;
         if (isDone) {
             
             Data model = new BasicData(at.getGraph(), Graph.class.getName());
             Dictionary map = model.getMetaData();
             map.put(DataProperty.MODIFIED,
                 new Boolean(true));
             map.put(DataProperty.PARENT, data[0]);
             map.put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
             map.put(DataProperty.LABEL, "Attack Tolerance");
             
             out_data = new Data[]{model};
         }
 
        return out_data;
    }

	private int getInt(String mystring) {
		return ((Integer) parameters.get(mystring)).intValue();
	}
}
