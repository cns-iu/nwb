package edu.iu.nwb.modeling.discretenetworkdynamics.components;

import java.util.HashSet;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import prefuse.data.Graph;
import prefuse.data.Node;
import prefuse.data.Schema;
import prefuse.data.Table;
import edu.iu.nwb.modeling.discretenetworkdynamics.parser.FunctionFormatException;

public class ParseDependencyGraphs {

	public static Graph constructDependencyGraph(final String functionLabel, final String nodeLabel, final Table functionTable) throws FunctionFormatException{

		Graph dependGraph = createEmptyGraph(nodeLabel, functionTable);

		dependGraph = addNodes(functionLabel,nodeLabel,functionTable,dependGraph);


		dependGraph = createDependencyEdges(functionLabel,functionTable,dependGraph);

		return dependGraph;
	}

	private static Graph createEmptyGraph(final String nodeLabel, final Table functionTable){
		Graph emptyGraph = null;

		int labelColumn = -1;

		Table dependencyNodeTable;
		Table dependencyEdgeTable;

		Schema s = functionTable.getSchema();

		Schema dependencyNodeSchema = new Schema();
		Schema dependencyEdgeSchema = new Schema();

		dependencyEdgeSchema.addColumn("source", int.class);
		dependencyEdgeSchema.addColumn("target", int.class);

		dependencyEdgeTable = dependencyEdgeSchema.instantiate();



		labelColumn = functionTable.getSchema().getColumnIndex(nodeLabel);
		dependencyNodeSchema = new Schema();
		for(int i = 0; i < s.getColumnCount(); i++){
			String columnName = s.getColumnName(i);
			if(i == labelColumn){
				columnName = "label";
			}
			dependencyNodeSchema.addColumn(columnName, s.getColumnType(i));
		}

		if(labelColumn < 0){
			dependencyNodeSchema.addColumn("label",String.class);
		}

		dependencyNodeTable = dependencyNodeSchema.instantiate();

		emptyGraph = new Graph(dependencyNodeTable,dependencyEdgeTable,true);

		return emptyGraph;
	}

	private static Graph addNodes(final String functionLabel, final String nodeLabel, final Table functionTable, Graph g) throws FunctionFormatException{
		int labelColumn = functionTable.getColumnNumber("nodeLabel");
		int functionColumn = functionTable.getSchema().getColumnIndex(functionLabel);
		//create the nodes
		for(int i = 0; i < functionTable.getRowCount(); i++){
			int nodeID = g.addNodeRow();

			if(labelColumn < 0){
				g.getNode(nodeID).set(g.getNodeTable().getColumnNumber("label"), new Integer(i+1).toString());
			}
			for(int j = 0; j < functionTable.getColumnCount(); j++){

				if(j==functionColumn){
					//parse the function
					g.getNode(nodeID).set(j,parseFunctionString(functionTable.get(i, j).toString()));
				}
				else if(j == labelColumn){
					String label = functionTable.get(i,j).toString();
					if(label.equals(""))
						g.getNode(nodeID).set(j, new Integer(i+1).toString());
					else
						g.getNode(nodeID).set(j, functionTable.get(i,j));
				}
				else{
					g.getNode(nodeID).set(j, functionTable.get(i, j));
				}

			}
		}

		return g;
	}

	private static Graph createDependencyEdges(final String functionLabel, final Table functionTable, Graph g){
		int functionColumn = functionTable.getSchema().getColumnIndex(functionLabel);
		for(int i = 0; i < functionTable.getRowCount(); i++){
			Node n = g.getNode(i);
			String function = n.getString(functionColumn);
			constructUniqueEdges(function,n,g);	
		}
		return g;
	}



	private static Graph constructUniqueEdges(String functionString, Node n,Graph g) throws NumberFormatException{
		Pattern p = Pattern.compile("x\\d+");
		Matcher m = p.matcher(functionString);	
		String nodeIdentification;
		HashSet uniqueEdges = new HashSet();
		while(m.find()){
			nodeIdentification = m.group();
			nodeIdentification = nodeIdentification.substring(1);

			Integer sourceID = new Integer(nodeIdentification);
			uniqueEdges.add(sourceID);

		}
		for(Iterator it = uniqueEdges.iterator(); it.hasNext();){
			int source = ((Integer)it.next()).intValue();
			source--;
			g.addEdge(source, n.getRow());
		}
		return g;
	}

	private static String parseFunctionString(String func)throws FunctionFormatException{
		String s = func;

		s = s.replaceAll("\\s","");
		//s = s.replace("^f\\d*=", "");
		String[] functionArray = s.split("=");

		if(functionArray.length != 2){
			throw new FunctionFormatException("Your expression must begin with \"f\" followed by a row number followed by \"=\". " +
					"The malformed expression looked like this: " + func);
		}

		s = functionArray[1];


		return s;

	}

	public static Graph constructPseudoGraph(final String functionLabel, final String nodeLabel, final Table functionTable) throws FunctionFormatException{
		Graph pseudoGraph = createEmptyGraph(nodeLabel,functionTable);

		pseudoGraph = addNodes(functionLabel,nodeLabel,functionTable,pseudoGraph);

		pseudoGraph = createPseudoEdges(functionLabel,functionTable,pseudoGraph);

		return pseudoGraph;
	}

	private static Graph createPseudoEdges(final String functionLabel, final Table functionTable, Graph g) throws FunctionFormatException{

		for(int i = 0; i < functionTable.getRowCount(); i++){
			Node n = g.addNode();
			String pseudoLabel = parseFunctionString(functionTable.get(i,functionLabel).toString());
			n.set("label", pseudoLabel);
			constructUniqueEdges(pseudoLabel,n,g);
			g.addEdge(n.getRow(),i);
		}


		return g;
	}

}
