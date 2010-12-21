package edu.iu.nwb.converters;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Dictionary;
import java.util.Iterator;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.DataValidator;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.metatype.MetaTypeProvider;

import edu.berkeley.guir.prefuse.graph.io.XMLGraphReader;
import edu.berkeley.guir.prefuse.graph.Graph;

import edu.iu.nwb.nwbpersisters.Edge;
import edu.iu.nwb.nwbpersisters.NWBModel;
import edu.iu.nwb.nwbpersisters.Node;

/**
 * @author Weixia(Bonnie) Huang 
 */
public class NWBPrefuseConverter implements AlgorithmFactory, DataValidator {


    /**
     * @see org.cishell.framework.algorithm.AlgorithmFactory#createParameters(org.cishell.framework.data.Data[])
     */
    public MetaTypeProvider createParameters(Data[] dm) {
        return null;
    }

    /**
     * @see org.cishell.framework.algorithm.AlgorithmFactory#createAlgorithm(org.cishell.framework.data.Data[], java.util.Dictionary, org.cishell.framework.CIShellContext)
     */
    public Algorithm createAlgorithm(Data[] dm, Dictionary parameters,
            CIShellContext context) {
        return new NWBPrefuseConverterAlg(dm);
    }
    
    public String validate(Data[] dm) {
        //need to validate that the input dm contains NWBModel data object
    	Object nwbData = dm[0].getData();
    	if (nwbData instanceof NWBModel){
    		return "The input is a real NWBModel object.";
    	}  	
    	else{
    		return "Error: Input is not a real edu.iu.nwb.nwbpersister.NWBModel object!"; 
    	}
    }
   
    private class NWBPrefuseConverterAlg implements Algorithm {
        Data[] dm;
        
        public NWBPrefuseConverterAlg(Data[] dm) {
            this.dm = dm;
        }
        /**
    	 * Executes this NWBPrefuseConverterAlgorithm.
    	 * from NWBModel-->prefuse GMML graph xml -->DM
    	 * 
    	 */
        public Data[] execute() {  	
    		NWBModel nwbModel = (NWBModel) dm[0].getData();
    		Data[] prefuseDM;
    

    		String path = System.getProperty("user.dir");
    		File tempDir = new File(path + File.separator + "temp");
    		if(!tempDir.exists())
    		    tempDir.mkdir();

    		
    		File tempFile;
    		
    		try{
    			tempFile = File.createTempFile("graph-ml-prefuse-", ".xml", tempDir);
    			writeGraphMl(nwbModel, tempFile);
    			
    			XMLGraphReader xgr = new XMLGraphReader() ;
    			Graph model = xgr.loadGraph(tempFile) ;
    			String label = tempFile.getName();
    			prefuseDM = new Data[]{new BasicData(model, Graph.class.getName()) };
    			Dictionary prefuseMetaData = prefuseDM[0].getMetaData();
    			prefuseMetaData.put(DataProperty.LABEL, label);
    			prefuseMetaData.put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
    			return prefuseDM;
    		} catch (IOException e) {
    			e.printStackTrace();
    		}      
    				
    	    return null;
    	}

    	/**
    	 * Write out graphML format given an NWB model
    	 * @param nwbModel The model to write
    	 * @param tempFile The temporary file to write to
    	 */
    	private void writeGraphMl(NWBModel nwbModel, File tempFile) {

    		try {
    			PrintWriter out = new PrintWriter(new BufferedWriter(
    					new FileWriter(tempFile)));

    			//Write the header
    			out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    			out
    					.println("<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns\">");
    			out.println("<graph edgedefault=\"undirected\">");

    			//Write out the nodes
    			Iterator nodeIter = nwbModel.getNodes();
    			while (nodeIter.hasNext()) {
    				Node bnc = (Node) nodeIter
    						.next();
    				String nodeId = (String)bnc
    						.getPropertyValue(Node.ID);
    				if (nodeId.charAt(0) != '"') {
    					nodeId = "\"" + nodeId + "\"";
    				}
    				String nodeLabel = (String)bnc
    						.getPropertyValue(Node.LABEL);
    				if (nodeLabel.charAt(0) != '"') {
    					nodeLabel = "\"" + nodeLabel + "\"";
    				}

    				out.println("<node id=" + nodeId.toString() + " label=" + nodeLabel + "></node>");
    			}

    			//Write out the undirected edges
    			Iterator edgeIter = nwbModel.getUndirectedEdges();
    			while (edgeIter.hasNext()) {
    				Edge bec = (Edge) edgeIter.next();

    				out.println("<edge source=\""
    						+ bec.getPropertyValue(Edge.ORIGIN) + "\" target=\""
    						+ bec.getPropertyValue(Edge.DEST) + "\">" + "</edge>");
    			}

    			out.println("</graph>");
    			out.println("</graphml>");
    			out.close();
    		} catch (IOException e) {
    			e.printStackTrace();
    		}		
    	
        
    	}
    }
}
    


