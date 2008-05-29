package edu.iu.nwb.analysis.extractattractors.components;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import org.cishell.framework.algorithm.AlgorithmExecutionException;

import prefuse.data.Edge;
import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Schema;
import prefuse.data.Table;
import edu.iu.nwb.util.nwbfile.NWBFileWriter;

public class BasinConstructorThread extends Thread{
	File basinGraph;
	int basinSize;
	LinkedHashSet nodes;
	Graph stateGraph;
	
	public BasinConstructorThread(Graph stateGraph,LinkedHashSet nodes){
		this.stateGraph = stateGraph;
		this.nodes = nodes;
	}
	
	public void run() {
		LinkedHashMap sourceToTargetMap = new LinkedHashMap();
		Graph basinGraph = initializeBasinGraph();
		for(Iterator it = nodes.iterator(); it.hasNext();){
			int sourceNodeRow = ((Integer)it.next()).intValue();
			int targetNodeRow = basinGraph.addNodeRow();
			sourceToTargetMap.put(new Integer(sourceNodeRow), new Integer(targetNodeRow));
			copyNode(basinGraph,sourceNodeRow,targetNodeRow);
		}
		
		for(Iterator it = nodes.iterator(); it.hasNext();){
			int sourceNodeRow = ((Integer)it.next()).intValue();
			
			for(Iterator edges = this.stateGraph.outEdges(this.stateGraph.getNode(sourceNodeRow)); edges.hasNext();){
				Edge e = (Edge)edges.next();
				int source = e.getSourceNode().getRow();
				int target = e.getTargetNode().getRow();
				
				basinGraph.addEdge(((Integer)sourceToTargetMap.get(new Integer(source))).intValue(), ((Integer)sourceToTargetMap.get(new Integer(target))).intValue());
			}	
		}
		
		NodeCleaningThread nct = new NodeCleaningThread(basinGraph);
		nct.run();
		try{
			nct.join();
			this.basinGraph = generateStateSpaceFile(basinGraph);
			this.basinSize = basinGraph.getNodeCount();
		}catch(InterruptedException ie){
			
		}catch(AlgorithmExecutionException aee){
			
		}
	
	}
	
	public File getAttractorBasin(){
		return this.basinGraph;
	}
	
	public int getBasinSize(){
		return this.basinSize;
	}
	
	private Graph initializeBasinGraph(){
		Graph basinGraph;
		Schema nodeSchema = getSchema("node");
		Schema edgeSchema = getSchema("edge");
		
		nodeSchema.addColumn("attractor", int.class, new Integer(0));
		basinGraph = new Graph(nodeSchema.instantiate(),edgeSchema.instantiate(),true);
		return basinGraph;
	}
	
	private Schema getSchema(String schemaType){
		Schema newSchema = new Schema();
		Schema oldSchema = null;
		if(schemaType.equals("node")){
			oldSchema = this.stateGraph.getNodeTable().getSchema();
			
		}
		else if(schemaType.equals("edge")){
			oldSchema = this.stateGraph.getEdgeTable().getSchema();
		}
		
		for(int i = 0; i < oldSchema.getColumnCount(); i++){
			newSchema.addColumn(oldSchema.getColumnName(i), oldSchema.getColumnType(i));
		}
		
		return newSchema;
	}
	
	private void copyNode(Graph basinGraph, int sourceNodeRow, int targetNodeRow){
		for(int i = 0; i < this.stateGraph.getNodeTable().getColumnCount(); i++){
			basinGraph.getNodeTable().set(targetNodeRow, i, this.stateGraph.getNodeTable().get(sourceNodeRow, i));
		}
	}
	
	
	private static File generateStateSpaceFile(final Graph g) throws AlgorithmExecutionException{
		try{
			File stateSpaceFile = File.createTempFile("NWB-Session-StateSpace-", ".nwb");
			NWBFileWriter stateSpaceWriter = new NWBFileWriter(stateSpaceFile);
			LinkedHashMap nodeSchema = NWBFileWriter.getDefaultNodeSchema();
			LinkedHashMap edgeSchema = NWBFileWriter.getDefaultEdgeSchema();
			
			nodeSchema = generateSchema(g.getNodeTable(),nodeSchema);
			edgeSchema = generateSchema(g.getEdgeTable(),edgeSchema);
			
			stateSpaceWriter.setNodeSchema(nodeSchema);
			writeNodes(g,stateSpaceWriter);
			stateSpaceWriter.setDirectedEdgeSchema(edgeSchema);
			writeEdges(g,stateSpaceWriter);
			
			stateSpaceWriter.haltParsingNow();
			return stateSpaceFile;
		}catch(IOException ioe){
			throw new AlgorithmExecutionException("Error creating and writing the state space graph file.\n",ioe);
		}
	}
	
	private static LinkedHashMap generateSchema(final Table t, LinkedHashMap schema){
		for(int i = 0; i < t.getColumnCount(); i++){
			if(schema.get(t.getColumnName(i)) == null){
				schema.put(t.getColumnName(i), t.getColumnType(i).toString().toLowerCase());
			}
		}
		
		return schema;
	}
	
	private static void writeNodes(final Graph g, NWBFileWriter nfw){
		int i = 0;
		for(Iterator it = g.nodes(); it.hasNext();){
			i++;
			HashMap columnValues = new HashMap();
			Node n = (Node)it.next();
			
			columnValues.put("attractor", n.get("attractor"));
			nfw.addNode(n.getRow()+1, n.getString("label"), columnValues);
			
		}
	}
	
	private static void writeEdges(final Graph g, NWBFileWriter nfw){
		int i = 0;
		for(Iterator it = g.edges(); it.hasNext();){
			i++;
			Edge e = (Edge)it.next();
			
		
			nfw.addDirectedEdge(e.getSourceNode().getRow()+1, e.getTargetNode().getRow()+1,null);
			
		}
	}
	
}
