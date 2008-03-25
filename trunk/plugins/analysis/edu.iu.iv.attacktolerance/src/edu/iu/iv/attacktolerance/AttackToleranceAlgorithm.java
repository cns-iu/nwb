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
         
    	 int numNodes = ((Integer) parameters.get("numNodes")).intValue();
         Data[] out_data;   
             Data model = new BasicData(AttackTolerance.testAttackTolerance(graph,numNodes), Graph.class.getName());
             Dictionary map = model.getMetadata();
             map.put(DataProperty.MODIFIED,new Boolean(true));
             map.put(DataProperty.PARENT, data[0]);
             map.put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
             map.put(DataProperty.LABEL, "High Degree Node Deletion (Attack Tolerance)");
             
             out_data = new Data[]{model};
             
             return out_data;
         }
}
