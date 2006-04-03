/* 
 * InfoVis CyberInfrastructure: A Data-Code-Compute Resource for Research
 * and Education in Information Visualization (http://iv.slis.indiana.edu)
 */
package edu.iu.informatics.converter.nwbjungconverter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Iterator;

import edu.iu.iv.common.property.PropertyMap;
import edu.iu.iv.core.IVC;
import edu.iu.iv.core.UnsupportedModelException;
import edu.iu.iv.core.algorithm.AbstractAlgorithm;
import edu.iu.iv.core.algorithm.AlgorithmProperty;
import edu.iu.iv.core.datamodels.BasicDataModel;
import edu.iu.iv.core.datamodels.DataModel;
import edu.iu.iv.core.datamodels.DataModelProperty;
import edu.iu.iv.core.datamodels.DataModelType;
import edu.iu.iv.core.persistence.BasicFileResourceDescriptor;
import edu.iu.iv.core.persistence.PersistenceException;
import edu.iu.iv.persisters.standard.JUNGGraphMLPersister;
import edu.iu.nwb.core.model.Edge;
import edu.iu.nwb.core.model.NWBModel;
import edu.iu.nwb.core.model.Node;

/**
 * Class to implement the Algorithm for this IVC Plug-in.
 *
 * @author
 */
public class NwbJungConverterAlgorithm extends AbstractAlgorithm {    
    private static final String ALGORITHM_NAME = "NWB -> Jung Conversion";
    private DataModel dm;
    
    /**
     * Creates a new NwbJungConverterAlgorithm.
     */
	public NwbJungConverterAlgorithm(DataModel dm) {
	    propertyMap.put(AlgorithmProperty.LABEL, ALGORITHM_NAME);
	    
	    this.dm = dm;
	}
	
	/**
	 * Executes this NwbJungConverterAlgorithm.
	 * 
	 * @return true if the Algorithm was successful, false if not
	 */
	public boolean execute(){
		NWBModel nwbModel = (NWBModel) dm.getData();

		String temp = IVC.getInstance().getTemporaryFilesFolder();
		File tempFile;
		try {
			tempFile = File.createTempFile("graph-ml-", ".xml", new File(temp));
			writeGraphMl(nwbModel, tempFile);

			try {
				JUNGGraphMLPersister persister = new JUNGGraphMLPersister();

				Object model;
				model = persister.restore(new BasicFileResourceDescriptor(
							tempFile));

				String label = tempFile.getName();
				DataModelType type = DataModelType.NETWORK;

				DataModel dataModel = new BasicDataModel(model);
				PropertyMap propMap = dataModel.getProperties();
				propMap.put(DataModelProperty.LABEL, label);
				propMap.put(DataModelProperty.TYPE, type);
				try {
					IVC.getInstance().addModel(dataModel);
				} catch (UnsupportedModelException e) {
					e.printStackTrace();
				}
			} catch (PersistenceException e1) {
				e1.printStackTrace();
			} catch (OutOfMemoryError e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	private void writeGraphMl(NWBModel nwbModel, File tempFile) {
		try {
			PrintWriter out
			   = new PrintWriter(new BufferedWriter(new FileWriter(tempFile)));
			
			out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			out.println("<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns\"  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
			out.println("xsi:schemaLocation=\"http://graphml.graphdrawing.org/xmlns http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd\">");
			out.println("<graph id=\"" + nwbModel.getClass() + "\" edgedefault=\"undirected\">");
			
			Iterator nodeIter = nwbModel.getNodes();
			
			while (nodeIter.hasNext()) {
				Node bnc = (Node)nodeIter.next();
				Object numAttr = bnc.getPropertyValue(Node.ID);
				out.println("<node id=\"" + numAttr.toString() + "\"><data key=\"label\">"+bnc.toString()+"</data></node>");
			}
			
			
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