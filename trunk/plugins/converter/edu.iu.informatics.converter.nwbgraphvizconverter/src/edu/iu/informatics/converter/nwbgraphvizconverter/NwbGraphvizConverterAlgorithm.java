/* 
 * InfoVis CyberInfrastructure: A Data-Code-Compute Resource for Research
 * and Education in Information Visualization (http://iv.slis.indiana.edu)
 */
package edu.iu.informatics.converter.nwbgraphvizconverter;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import net.claribole.zgrviewer.dot.Graph;
import edu.iu.iv.common.property.PropertyMap;
import edu.iu.iv.core.IVC;
import edu.iu.iv.core.UnsupportedModelException;
import edu.iu.iv.core.algorithm.AbstractAlgorithm;
import edu.iu.iv.core.algorithm.AlgorithmProperty;
import edu.iu.iv.core.datamodels.BasicDataModel;
import edu.iu.iv.core.datamodels.DataModel;
import edu.iu.iv.core.datamodels.DataModelProperty;
import edu.iu.iv.core.datamodels.DataModelType;
import edu.iu.nwb.common.model.BasicNWBModel;
import edu.iu.nwb.core.model.NWBModel;

/**
 * Class to implement the Algorithm for this IVC Plug-in.
 *
 * @author
 */
public class NwbGraphvizConverterAlgorithm extends AbstractAlgorithm {    
    private static final String ALGORITHM_NAME = "Nwb Graphviz Converter";
    
    private DataModel model;
    /**
     * Creates a new NwbGraphvizConverterAlgorithm.
     */
	public NwbGraphvizConverterAlgorithm(DataModel model) {
	    propertyMap.put(AlgorithmProperty.LABEL, ALGORITHM_NAME);
	    this.model = model;
	}
	
	/**
	 * Executes this NwbGraphvizConverterAlgorithm.
	 * 
	 * @return true if the Algorithm was successful, false if not
	 */
	public boolean execute(){
		if (model.getData() instanceof Graph) {
			BasicNWBModel nwbModel = new BasicNWBModel();
			Graph g = (Graph)model.getData();
			net.claribole.zgrviewer.dot.Node[] nodes = g.getNodes();
			
			for (int i = 0; i < nodes.length; ++i) {
				String nodeString = nodes[i].toString().trim();
				String nodeId     = nodeString.substring(0, nodeString.indexOf(" "));

				String attrString = nodeString.substring(nodeId.length());
				attrString = attrString.substring(attrString.indexOf("[")+1, attrString.lastIndexOf("]"));
				
				Map map = getAttributeMap(attrString);
				edu.iu.nwb.core.model.Node nwbNode = new edu.iu.nwb.core.model.Node();
				nwbNode.setPropertyValue(edu.iu.nwb.core.model.Node.ID,    nodeId);
				if (map.containsKey("label")) {
					nwbNode.setPropertyValue(edu.iu.nwb.core.model.Node.LABEL, map.get("label"));
				}
				else {
					nwbNode.setPropertyValue(edu.iu.nwb.core.model.Node.LABEL, nodeId);					
				}
				nwbModel.addNode(nodeId, nwbNode);
				//System.out.println(attrString);
			}
			
			net.claribole.zgrviewer.dot.Edge[] edges = g.getEdges();
			for (int i = 0; i < edges.length; ++i) {
				String edgeString = edges[i].toString().trim();
				
				String[] edgeList = getEdge(edgeString);
				edu.iu.nwb.core.model.Edge nwbEdge = new edu.iu.nwb.core.model.Edge();
				nwbEdge.setPropertyValue(edu.iu.nwb.core.model.Edge.ORIGIN, edgeList[0]);
				nwbEdge.setPropertyValue(edu.iu.nwb.core.model.Edge.DEST,   edgeList[1]);
				if (g.directed) {
					nwbModel.addDirectedEdge(nwbEdge);
				}
				else {
					nwbModel.addUndirectedEdge(nwbEdge);
				}
				//System.out.println(edgeList[0] + " " + edgeList[1]);
				//System.out.println(edgeString);
			}
			
			String label = g.id;
			DataModelType type = DataModelType.NETWORK;

			DataModel dataModel = new BasicDataModel(nwbModel);
			PropertyMap propMap = dataModel.getProperties();
			propMap.put(DataModelProperty.LABEL, label);
			propMap.put(DataModelProperty.TYPE, type);
			dataModel.getProperties().setPropertyValue(DataModelProperty.PARENT, this.model);
			
			try {
				//Add the datamodel to IVC
				IVC.getInstance().addModel(dataModel);
			} catch (UnsupportedModelException e) {
				e.printStackTrace();
			}

			return true;
		}
		else if (model.getData() instanceof NWBModel) {
			try {
				PropertyMap propMap = model.getProperties();				
				String dataModelLabel = (String)propMap.getPropertyValue(DataModelProperty.LABEL);
				Graph g = new Graph(dataModelLabel);
				
				NWBModel nwbModel = (NWBModel)model.getData();
				Iterator nodeIter = nwbModel.getNodes();
				while (nodeIter.hasNext()) {
					edu.iu.nwb.core.model.Node nwbNode = (edu.iu.nwb.core.model.Node)nodeIter.next();
					String nodeId = (String)nwbNode.getPropertyValue(edu.iu.nwb.core.model.Node.ID);
					net.claribole.zgrviewer.dot.BasicNode dotNode = new net.claribole.zgrviewer.dot.BasicNode(g, nodeId);
					g.addNode(dotNode);
				}
				
				Iterator edgeIter = nwbModel.getUndirectedEdges();
				while (edgeIter.hasNext()) {
					edu.iu.nwb.core.model.Edge nwbEdge = (edu.iu.nwb.core.model.Edge)edgeIter.next();
					String src = (String)nwbEdge.getPropertyValue(edu.iu.nwb.core.model.Edge.ORIGIN);
					String tgt = (String)nwbEdge.getPropertyValue(edu.iu.nwb.core.model.Edge.DEST);
					net.claribole.zgrviewer.dot.BasicNode srcNode = new net.claribole.zgrviewer.dot.BasicNode(g, src);
					net.claribole.zgrviewer.dot.BasicNode tgtNode = new net.claribole.zgrviewer.dot.BasicNode(g, tgt);
					net.claribole.zgrviewer.dot.Edge dotEdge = new net.claribole.zgrviewer.dot.Edge(g, srcNode, tgtNode);
					g.addEdge(dotEdge);	
				}
				
				String label = g.id;
				DataModelType type = DataModelType.NETWORK;

				DataModel dataModel = new BasicDataModel(g);
				PropertyMap dotPropMap = dataModel.getProperties();
				dotPropMap.put(DataModelProperty.LABEL, label);
				dotPropMap.put(DataModelProperty.TYPE, type);
				dataModel.getProperties().setPropertyValue(DataModelProperty.PARENT, this.model);
				
				try {
					//Add the datamodel to IVC
					IVC.getInstance().addModel(dataModel);
				} catch (UnsupportedModelException e) {
					e.printStackTrace();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return true;
		}
	    return false;
	}
	
	private String[] getEdge(String edgeString) {
		String[] list = new String[2];
		
		StringTokenizer st = new StringTokenizer(edgeString, "->");
		String source = st.nextToken().trim();
		String target  = st.nextToken().trim();
		target = target.substring(0, target.indexOf(" "));
		
		list[0] = source;
		list[1] = target;
		
		return list;
	}
	
	private Map getAttributeMap(String attrString) {
		Hashtable tbl        = new Hashtable();
		
		while (attrString.length() > 0) {
			//get key value pair
			String key  = attrString.substring(0, attrString.indexOf("=")).trim();
			
			attrString  = attrString.substring(attrString.indexOf("="));
			int valStrt = attrString.indexOf("\"") + 1;
			int valEnd  = attrString.indexOf("\"", valStrt+1);
			String val  = attrString.substring(valStrt, valEnd).trim();
			
			attrString = attrString.substring(valEnd+1).trim();
			
			tbl.put(key, val);
			//System.out.println(key + " -> " + val);
			//System.out.println(attrString);
		}

		return tbl;
	}
}