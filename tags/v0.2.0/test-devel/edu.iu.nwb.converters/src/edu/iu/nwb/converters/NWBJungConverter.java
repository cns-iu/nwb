package edu.iu.nwb.converters;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Dictionary;
import java.util.HashSet;
import java.util.Iterator;

import org.cishell.framework.CIShellContext;
import org.cishell.framework.algorithm.Algorithm;
import org.cishell.framework.algorithm.AlgorithmFactory;
import org.cishell.framework.algorithm.DataValidator;
import org.cishell.framework.data.BasicData;
import org.cishell.framework.data.Data;
import org.cishell.framework.data.DataProperty;
import org.osgi.service.metatype.MetaTypeProvider;

import edu.iu.nwb.nwbpersisters.Edge;
import edu.iu.nwb.nwbpersisters.NWBModel;
import edu.iu.nwb.nwbpersisters.Node;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.io.GraphMLFile;

/**
 * @author Weixia(Bonnie) Huang 
 */
public class NWBJungConverter implements AlgorithmFactory, DataValidator {


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
        return new NWBJungConverterAlg(dm);
    }
    
    public String validate(Data[] dm) {
        //need to validate that the input dm contains NWBModel data object
    	Object nwbData = dm[0].getData();
    	if (nwbData instanceof NWBModel){
    		return "The input is a real NWBModel object.";
    	}  	
    	else{
    		return "Error: Input is not a real NWBModel object!"; 
    	}
    }
   
    private class NWBJungConverterAlg implements Algorithm {
        Data[] dm;
        
        public NWBJungConverterAlg(Data[] dm) {
            this.dm = dm;
        }
        /**
    	 * Executes this NWBJungConverterAlgorithm.
    	 * from NWBModel-->GraphML xml file -->Jung Graph DM
    	 * 
    	 */
        public Data[] execute() {  	
    		NWBModel nwbModel = (NWBModel) dm[0].getData();
    		Data[] jungDM;
    		    	       
     		String path = System.getProperty("user.dir");
    		File tempDir = new File(path + File.separator + "temp");
    		if(!tempDir.exists())
    		    tempDir.mkdir();

    		
    		File tempFile;
    		
    		try{
    			tempFile = File.createTempFile("graph-ml", ".xml", tempDir);
    			writeGraphMl(nwbModel, tempFile);
		
    			Graph model = (new GraphMLFile()).load(tempFile.getPath()) ;
    			String label = tempFile.getName();
    			jungDM = new Data[]{new BasicData(model, Graph.class.getName()) };
    			Dictionary jungMetaData = jungDM[0].getMetaData();
    			jungMetaData.put(DataProperty.LABEL, label);
    			jungMetaData.put(DataProperty.TYPE, DataProperty.NETWORK_TYPE);
    			return jungDM;
    		} catch (IOException e) {
    			e.printStackTrace();
    		}      
    				
    	    return null;
    	}
        /**
         * Write out the data model to GraphML format
         * @param nwbModel The NWBmodel to save
         * @param tempFile The temporary file to write to
         */
        private void writeGraphMl(NWBModel nwbModel, File tempFile) {
        	try {
        		PrintWriter out
        		   = new PrintWriter(new BufferedWriter(new FileWriter(tempFile)));
        		//write the header
        		out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        		out.println("<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns\"  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
        		out.println("xsi:schemaLocation=\"http://graphml.graphdrawing.org/xmlns http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd\">");
        		out.println("<graph id=\"" + nwbModel.getClass() + "\" edgedefault=\"undirected\">");
        		
        		//write the nodes
        		Iterator nodeIter = nwbModel.getNodes();			
        		while (nodeIter.hasNext()) {
        			Node bnc = (Node)nodeIter.next();
        			Object numAttr = bnc.getPropertyValue(Node.ID);
        			out.println("<node id=\"" + numAttr.toString() + "\"><data key=\"label\">"+bnc.toString()+"</data></node>");
        		}
        		
        		//Write the undirected edges
        		Iterator   edgeIter       = nwbModel.getUndirectedEdges();
        		HashSet    edgesProcessed = new HashSet();
        		int        edgeNumber     = 0;
        		while (edgeIter.hasNext()) {
        			Edge bec = (Edge)edgeIter.next();
        			
        			if (!edgesProcessed.contains(""
        					+ bec.getPropertyValue(Edge.ORIGIN)
        					+ bec.getPropertyValue(Edge.DEST)) &&
        				!edgesProcessed.contains(""
        					+ bec.getPropertyValue(Edge.DEST)
        					+ bec.getPropertyValue(Edge.ORIGIN))) {
        				out.println("<edge id=\"e" + edgeNumber + "\" source=\""
        						+ bec.getPropertyValue(Edge.ORIGIN)
        						+ "\" target=\""
        						+ bec.getPropertyValue(Edge.DEST) + "\">"
        						+ "</edge>");
        				edgeNumber++;
        				edgesProcessed.add(""
        						+ bec.getPropertyValue(Edge.ORIGIN)
        						+ bec.getPropertyValue(Edge.DEST));
        			}
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
    





